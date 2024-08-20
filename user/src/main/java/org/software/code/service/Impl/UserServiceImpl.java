package org.software.code.service.Impl;

import cn.hutool.core.util.IdUtil;
import com.google.common.hash.Funnels;
import org.software.code.common.utils.JWTUtil;
import org.software.code.common.utils.RedisUtil;
import org.software.code.common.utils.WeChatUtil;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.dao.HealthCodeManagerDao;
import org.software.code.dao.NucleicAcidTestPersonnelDao;
import org.software.code.dao.UidMappingDao;
import org.software.code.dao.UserInfoDao;
import org.software.code.model.dto.HealthCodeManagerDto;
import org.software.code.model.dto.NucleicAcidTestPersonnelDto;
import org.software.code.model.dto.UserInfoDto;
import org.software.code.kafka.KafkaConsumer;
import org.software.code.kafka.KafkaProducer;
import org.software.code.mapper.HealthCodeMangerMapper;
import org.software.code.mapper.NucleicAcidTestPersonnelMapper;
import org.software.code.mapper.UserInfoMapper;
import org.software.code.mapper.UidMappingMapper;
import org.software.code.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.hash.BloomFilter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UidMappingMapper userMappingMapper;
    @Autowired
    private HealthCodeMangerMapper healthCodeMangerMapper;
    @Autowired
    private NucleicAcidTestPersonnelMapper nucleicAcidTestPersonnelMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private KafkaConsumer kafkaConsumer;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private BloomFilter<CharSequence> bloomFilter;

    private static final String BLOOM_FILTER_FILE = "bloomfilter.data";

    @PostConstruct
    public void init() {
        bloomFilter = getInitBloomFilter();
        logger.info("Bloom filter initialized.");
    }

    @PreDestroy
    public void shutdown() {
        saveBloomFilter();
        logger.info("Bloom filter saved.");
    }

    private BloomFilter<CharSequence> getInitBloomFilter() {
        try {
            File file = new File(BLOOM_FILTER_FILE);
            if (file.exists()) {
                InputStream is = Files.newInputStream(file.toPath());
                bloomFilter = BloomFilter.readFrom(is, Funnels.stringFunnel(Charset.defaultCharset()));
                logger.info("Bloom filter loaded from file.");
            }
        } catch (IOException e) {
            logger.error("Failed to load Bloom filter: {}", e.getMessage());
        }
        if (bloomFilter == null) {
            try {
                bloomFilter = BloomFilter.create(
                        Funnels.stringFunnel(StandardCharsets.UTF_8),
                        1000000,
                        0.01);
                logger.info("New Bloom filter created.");
            } catch (Exception e) {
                logger.error("Failed to create Bloom filter: {}", e.getMessage());
            }
        }
        return bloomFilter;
    }

    private void saveBloomFilter() {
        try (OutputStream os = Files.newOutputStream(Paths.get(BLOOM_FILTER_FILE))) {
            bloomFilter.writeTo(os);
            logger.info("Bloom filter saved to file.");
        } catch (IOException e) {
            logger.error("Failed to save Bloom filter: {}", e.getMessage());
        }
    }

    @Override
    public UserInfoDto getUserByUID(long uid) {
        UserInfoDao userInfo = userInfoMapper.getUserInfoByUID(uid);
        if (userInfo == null) {
            logger.error("User not found for UID: {}", uid);
            throw new BusinessException(ExceptionEnum.UID_NOT_FIND);
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(userInfo, userInfoDto);
        return userInfoDto;
    }

    @Override
    public UserInfoDto getUserByID(String identity_card) {
        UserInfoDao userInfo = userInfoMapper.getUserInfoByID(identity_card);
        if (userInfo == null) {
            logger.error("User not found for identity card: {}", identity_card);
            throw new BusinessException(ExceptionEnum.ID_NOT_FIND);
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(userInfo, userInfoDto);
        return userInfoDto;
    }

    private long getInLogin(String openID) {
        long uid = -1;
        UidMappingDao uidMappingDao = userMappingMapper.getUidMappingByOpenID(openID);
        if (uidMappingDao != null) {
            uid = uidMappingDao.getUid();
        }
        return uid;
    }

    private long addInLogin(String openID) {
        long uid;
        try {
            uid = IdUtil.getSnowflake().nextId();
            UidMappingDao uidMappingDao = new UidMappingDao();
            uidMappingDao.setUid(uid);
            uidMappingDao.setOpenid(openID);
            userMappingMapper.addUserMapping(uidMappingDao);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error during user login: {}", e.getMessage());
            uid = -1;
        }
        return uid;
    }

    @Override
    public String userLogin(String code) {
        String openID = WeChatUtil.getOpenIDFromWX(code);
        if (bloomFilter == null) { // 确保bloomFilter已经初始化
            getInitBloomFilter();
        }
        long uid = -1;
        if (bloomFilter != null && !bloomFilter.mightContain(openID)) {
            uid = addInLogin(openID);
        }
        if (uid == -1) {
            uid = getInLogin(openID);
            if (uid == -1) {
                uid = addInLogin(openID);
            }
        }
        return JWTUtil.generateJWToken(uid, 3600000);
    }

    @Override
    public String nucleicAcidTestUserLogin(String identityCard, String password) {
        NucleicAcidTestPersonnelDao userDao = nucleicAcidTestPersonnelMapper.getNucleicAcidTestPersonnelByID(identityCard);
        if (userDao == null) {
            logger.error("User not found for identity card: {}", identityCard);
            throw new BusinessException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        if (!userDao.getStatus()) {
            logger.error("User is inactive for identity card: {}", identityCard);
            throw new BusinessException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        if (!passwordEncoder.matches(password, userDao.getPassword_hash())) {
            logger.error("Password mismatch for identity card: {}", identityCard);
            throw new BusinessException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        String token = JWTUtil.generateJWToken(userDao.getTid(), 3600000);
        return token;
    }

    @Override
    public List<NucleicAcidTestPersonnelDto> getNucleicAcidTestUser() {
        List<NucleicAcidTestPersonnelDao> userDaoList = nucleicAcidTestPersonnelMapper.getNucleicAcidUserInfoList();
        return userDaoList.stream()
                .map(userDao -> {
                    NucleicAcidTestPersonnelDto userDto = new NucleicAcidTestPersonnelDto();
                    BeanUtils.copyProperties(userDao, userDto);
                    return userDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthCodeManagerDto> getManagerUser() {
        List<HealthCodeManagerDao> userDaoList = healthCodeMangerMapper.getHealthCodeManagerList();
        return userDaoList.stream()
                .map(userDao -> {
                    HealthCodeManagerDto userDto = new HealthCodeManagerDto();
                    BeanUtils.copyProperties(userDao, userDto);
                    return userDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void newNucleicAcidTestUser(String identityCard, String password, String name) {
        NucleicAcidTestPersonnelDao existingUserDao = nucleicAcidTestPersonnelMapper.getNucleicAcidTestPersonnelByID(identityCard);
        if (existingUserDao != null) {
            logger.error("Duplicate identity card found: {}", identityCard);
            throw new BusinessException(ExceptionEnum.ID_EXIST);
        }
        NucleicAcidTestPersonnelDao newUserDao = new NucleicAcidTestPersonnelDao();
        newUserDao.setIdentity_card(identityCard);
        String password_hash = passwordEncoder.encode(password);
        newUserDao.setPassword_hash(password_hash);
        newUserDao.setName(name);
        newUserDao.setStatus(true);
        long tid = IdUtil.getSnowflake().nextId();
        newUserDao.setTid(tid);
        nucleicAcidTestPersonnelMapper.addNucleicAcidTestPersonnel(newUserDao);
    }

    @Override
    public void newMangerUser(String identityCard, String password, String name) {
        HealthCodeManagerDao existingUserDao = healthCodeMangerMapper.getHealthCodeManagerByID(identityCard);
        if (existingUserDao != null) {
            logger.error("Duplicate identity card found: {}", identityCard);
            throw new BusinessException(ExceptionEnum.ID_EXIST);
        }
        HealthCodeManagerDao newUserDao = new HealthCodeManagerDao();
        newUserDao.setIdentity_card(identityCard);
        String password_hash = passwordEncoder.encode(password);
        newUserDao.setPassword_hash(password_hash);
        newUserDao.setName(name);
        newUserDao.setStatus(true);
        long mid = IdUtil.getSnowflake().nextId();
        newUserDao.setMid(mid);
        healthCodeMangerMapper.addHealthCodeManager(newUserDao);
    }

    @Override
    public String managerLogin(String identityCard, String password) {
        HealthCodeManagerDao userDao = healthCodeMangerMapper.getHealthCodeManagerByID(identityCard);
        if (userDao == null) {
            logger.error("Manager not found for identity card: {}", identityCard);
            throw new BusinessException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        if (!userDao.getStatus()) {
            logger.error("Manager is inactive for identity card: {}", identityCard);
            throw new BusinessException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        if (!passwordEncoder.matches(password, userDao.getPassword_hash())) {
            logger.error("Password mismatch for identity card: {}", identityCard);
            throw new BusinessException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        String token = JWTUtil.generateJWToken(userDao.getMid(), 3600000);
        return token;
    }

    @Override
    public void modifyUserInfo(long uid, String name, String phoneNumber, String identityCard, int district, int street, long community, String address) {
        UserInfoDao userInfoDao = userInfoMapper.getUserInfoByID(identityCard);
        if (userInfoDao == null) {
            userInfoDao = new UserInfoDao();
            userInfoDao.setUid(uid);
            userInfoDao.setIdentity_card(identityCard);
            userInfoDao.setName(name);
            userInfoDao.setPhone_number(phoneNumber);
            userInfoDao.setDistrict(district);
            userInfoDao.setStreet(street);
            userInfoDao.setCommunity(community);
            userInfoDao.setAddress(address);
            userInfoMapper.addUserInfo(userInfoDao);

        } else {
            if (userInfoDao.getUid() != uid) {
                logger.error("UID mismatch for identity card: {}, expected UID: {}, found UID: {}", identityCard, uid, userInfoDao.getUid());
                throw new BusinessException(ExceptionEnum.ID_EXIST);
            }
            userInfoDao.setName(name);
            userInfoDao.setPhone_number(phoneNumber);
            userInfoDao.setDistrict(district);
            userInfoDao.setStreet(street);
            userInfoDao.setCommunity(community);
            userInfoDao.setAddress(address);
            userInfoMapper.updateUserInfo(userInfoDao);
        }
    }

    @Override
    public void statusNucleicAcidTestUser(long tid, boolean status) {
        nucleicAcidTestPersonnelMapper.updateStatusByTID(status, tid);
    }

    @Override
    public void statusManager(long mid, boolean status) {
        healthCodeMangerMapper.updateStatusByMID(status, mid);
    }

    @Override
    public void addUserInfo(long uid, String name, String phoneNumber, String identityCard, int district, int street, long community, String address) {
        UserInfoDao userInfoDao_id = userInfoMapper.getUserInfoByID(identityCard);
        if (userInfoDao_id != null) {
            logger.error("Duplicate identity card found: {}", identityCard);
            throw new BusinessException(ExceptionEnum.ID_EXIST);
        }
        UserInfoDao userInfoDao_phone = userInfoMapper.getUserInfoByPhone(phoneNumber);
        if (userInfoDao_phone != null) {
            logger.error("Duplicate phone number found: {}", phoneNumber);
            throw new BusinessException(ExceptionEnum.PHONE_EXIST);
        }
        UserInfoDao userInfoDao = userInfoMapper.getUserInfoByUID(uid);
        if (userInfoDao == null) {
            userInfoDao = new UserInfoDao();
            userInfoDao.setUid(uid);
            userInfoDao.setIdentity_card(identityCard);
            userInfoDao.setName(name);
            userInfoDao.setPhone_number(phoneNumber);
            userInfoDao.setDistrict(district);
            userInfoDao.setStreet(street);
            userInfoDao.setCommunity(community);
            userInfoDao.setAddress(address);
            userInfoMapper.addUserInfo(userInfoDao);
        } else {
            userInfoDao.setUid(uid);
            userInfoDao.setIdentity_card(identityCard);
            userInfoDao.setName(name);
            userInfoDao.setPhone_number(phoneNumber);
            userInfoDao.setDistrict(district);
            userInfoDao.setStreet(street);
            userInfoDao.setCommunity(community);
            userInfoDao.setAddress(address);
            userInfoMapper.updateUserInfo(userInfoDao);
        }
    }

    @Override
    public void userModify(long uid, String name, String phoneNumber, int districtId, int streetId, long communityId, String address) {
        UserInfoDao userInfoDao = userInfoMapper.getUserInfoByUID(uid);
        if (userInfoDao != null) {
            userInfoDao.setName(name);
            userInfoDao.setPhone_number(phoneNumber);
            userInfoDao.setDistrict(districtId);
            userInfoDao.setStreet(streetId);
            userInfoDao.setCommunity(communityId);
            userInfoDao.setAddress(address);
            userInfoMapper.updateUserInfo(userInfoDao);
        } else {
            logger.error("User not found for UID: {}", uid);
            throw new BusinessException(ExceptionEnum.UID_NOT_FIND);
        }
    }

    @Override
    public void nucleicAcidOpposite(long tid) {
        NucleicAcidTestPersonnelDao uerDao = nucleicAcidTestPersonnelMapper.getNucleicAcidTestPersonnelByTID(tid);
        if (uerDao == null) {
            logger.error("Nucleic acid test user not found for TID: {}", tid);
            throw new BusinessException(ExceptionEnum.NUCLEIC_ACID_TEST_USER_NOT_FIND);
        }
        Boolean status = uerDao.getStatus();
        nucleicAcidTestPersonnelMapper.updateStatusByTID(!status, tid);
    }

    @Override
    public void manageOpposite(long mid) {
        HealthCodeManagerDao uerDao = healthCodeMangerMapper.getHealthCodeManagerByMID(mid);
        if (uerDao == null) {
            throw new BusinessException(ExceptionEnum.MANAGER_USER_NOT_FIND);
        }
        Boolean status = uerDao.getStatus();
        healthCodeMangerMapper.updateStatusByMID(!status, mid);
    }

    @Override
    public String userLogin_test(String code) {
        String openID = "openid-" + code;
        if (bloomFilter == null) { // 确保bloomFilter已经初始化
            getInitBloomFilter();
        }
        long uid = -1;
        if (bloomFilter != null && !bloomFilter.mightContain(openID)) {
            uid = addInLogin(openID);
            if (uid == -1) {
                uid = getInLogin(openID);
            }
            bloomFilter.put(openID);
            return JWTUtil.generateJWToken(uid, 3600000);
        }
        uid = getInLogin(openID);
        if (uid == -1) {
            uid = addInLogin(openID);
        }
        return JWTUtil.generateJWToken(uid, 3600000);
    }


    @Override
    public void deleteUserInfo(long uid) {
        if (userInfoMapper.existsById(uid)) {
            userInfoMapper.deleteById(uid);
        } else {
            logger.warn("Attempted to delete non-existent user info for UID: {}", uid);
        }
    }
}