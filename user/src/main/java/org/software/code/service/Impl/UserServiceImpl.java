package org.software.code.service.Impl;

import cn.hutool.core.util.IdUtil;
import com.google.common.hash.Funnels;
import org.software.code.common.utils.JWTUtil;
import org.software.code.common.utils.WeChatUtil;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.model.entity.HealthCodeManager;
import org.software.code.model.entity.NucleicAcidTestPersonnel;
import org.software.code.model.entity.UidMapping;
import org.software.code.model.entity.UserInfo;
import org.software.code.model.dto.*;
import org.software.code.dao.HealthCodeMangerDao;
import org.software.code.dao.NucleicAcidTestPersonnelDao;
import org.software.code.dao.UserInfoDao;
import org.software.code.dao.UidMappingDao;
import org.software.code.model.input.*;
import org.software.code.service.UserService;
import org.springframework.beans.BeanUtils;
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
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.hash.BloomFilter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Resource
    private UserInfoDao userInfoDao;
    @Resource
    private UidMappingDao uidMappingDao;
    @Resource
    private HealthCodeMangerDao healthCodeMangerDao;
    @Resource
    private NucleicAcidTestPersonnelDao nucleicAcidTestPersonnelDao;

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
        UserInfo userInfo = userInfoDao.getUserInfoByUID(uid);
        if (userInfo == null) {
            logger.error("User not found for UID: {}", uid);
            throw new BusinessException(ExceptionEnum.UID_NOT_FIND);
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(userInfo, userInfoDto);
        return userInfoDto;
    }

    @Override
    public UserInfoDto getUserByID(String identityCard) {
        UserInfo userInfo = userInfoDao.getUserInfoByID(identityCard);
        if (userInfo == null) {
            logger.error("User not found for identity card: {}", identityCard);
            throw new BusinessException(ExceptionEnum.ID_NOT_FIND);
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(userInfo, userInfoDto);
        return userInfoDto;
    }

    private long getInLogin(String openID) {
        long uid = -1;
        UidMapping uidMapping = uidMappingDao.getUidMappingByOpenID(openID);
        if (uidMapping != null) {
            uid = uidMapping.getUid();
        }
        return uid;
    }

    private long addInLogin(String openID) {
        long uid;
        try {
            uid = IdUtil.getSnowflake().nextId();
            UidMapping uidMapping = new UidMapping();
            uidMapping.setUid(uid);
            uidMapping.setOpenid(openID);
            uidMappingDao.addUserMapping(uidMapping);

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
    public String nucleicAcidTestUserLogin(NucleicAcidsLoginRequest request) {
        String identityCard=request.getIdentityCard();
        String password=request.getPassword();
        NucleicAcidTestPersonnel nucleicAcidTestPersonnel = nucleicAcidTestPersonnelDao.getNucleicAcidTestPersonnelByID(identityCard);
        if (nucleicAcidTestPersonnel == null) {
            logger.error("User not found for identity card: {}", identityCard);
            throw new BusinessException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        if (!nucleicAcidTestPersonnel.getStatus()) {
            logger.error("User is inactive for identity card: {}", identityCard);
            throw new BusinessException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        if (!passwordEncoder.matches(password, nucleicAcidTestPersonnel.getPasswordHash())) {
            logger.error("Password mismatch for identity card: {}", identityCard);
            throw new BusinessException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        String token = JWTUtil.generateJWToken(nucleicAcidTestPersonnel.getTid(), 3600000);
        return token;
    }

    @Override
    public List<NucleicAcidTestPersonnelDto> getNucleicAcidTestUser() {
        List<NucleicAcidTestPersonnel> nucleicAcidUserInfoList = nucleicAcidTestPersonnelDao.getNucleicAcidUserInfoList();
        return nucleicAcidUserInfoList.stream()
                .map(nucleicAcidTestPersonnel -> {
                    NucleicAcidTestPersonnelDto userDto = new NucleicAcidTestPersonnelDto();
                    BeanUtils.copyProperties(nucleicAcidTestPersonnel, userDto);
                    return userDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<HealthCodeManagerDto> getManagerUser() {
        List<HealthCodeManager> healthCodeManagerList = healthCodeMangerDao.getHealthCodeManagerList();
        return healthCodeManagerList.stream()
                .map(healthCodeManager -> {
                    HealthCodeManagerDto userDto = new HealthCodeManagerDto();
                    BeanUtils.copyProperties(healthCodeManager, userDto);
                    return userDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void newNucleicAcidTestUser(CreateNucleicAcidRequest request) {
        String identityCard=request.getIdentityCard();
        String password=request.getPassword();
        String name=request.getName();
        NucleicAcidTestPersonnel existingUser = nucleicAcidTestPersonnelDao.getNucleicAcidTestPersonnelByID(identityCard);
        if (existingUser != null) {
            logger.error("Duplicate identity card found: {}", identityCard);
            throw new BusinessException(ExceptionEnum.ID_EXIST);
        }
        NucleicAcidTestPersonnel newUser = new NucleicAcidTestPersonnel();
        newUser.setIdentityCard(identityCard);
        String passwordHash = passwordEncoder.encode(password);
        newUser.setPasswordHash(passwordHash);
        newUser.setName(name);
        newUser.setStatus(true);
        long tid = IdUtil.getSnowflake().nextId();
        newUser.setTid(tid);
        nucleicAcidTestPersonnelDao.addNucleicAcidTestPersonnel(newUser);
    }

    @Override
    public void newMangerUser(CreateManageRequest request) {
        String identityCard=request.getIdentityCard();
        String password=request.getPassword();
        String name=request.getName();
        HealthCodeManager existingUser = healthCodeMangerDao.getHealthCodeManagerByID(identityCard);
        if (existingUser != null) {
            logger.error("Duplicate identity card found: {}", identityCard);
            throw new BusinessException(ExceptionEnum.ID_EXIST);
        }
        HealthCodeManager newUser = new HealthCodeManager();
        newUser.setIdentityCard(identityCard);
        String passwordHash = passwordEncoder.encode(password);
        newUser.setPasswordHash(passwordHash);
        newUser.setName(name);
        newUser.setStatus(true);
        long mid = IdUtil.getSnowflake().nextId();
        newUser.setMid(mid);
        healthCodeMangerDao.addHealthCodeManager(newUser);
    }

    @Override
    public String managerLogin(ManagerLoginRequest request) {
        String identityCard=request.getIdentityCard();
        String password=request.getPassword();
        HealthCodeManager healthCodeManager = healthCodeMangerDao.getHealthCodeManagerByID(identityCard);
        if (healthCodeManager == null) {
            logger.error("Manager not found for identity card: {}", identityCard);
            throw new BusinessException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        if (!healthCodeManager.getStatus()) {
            logger.error("Manager is inactive for identity card: {}", identityCard);
            throw new BusinessException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        if (!passwordEncoder.matches(password, healthCodeManager.getPasswordHash())) {
            logger.error("Password mismatch for identity card: {}", identityCard);
            throw new BusinessException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        String token = JWTUtil.generateJWToken(healthCodeManager.getMid(), 3600000);
        return token;
    }

    @Override
    public void modifyUserInfo(UserInfoRequest request) {
        UserInfo userInfo = userInfoDao.getUserInfoByID(request.getIdentityCard());
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setUid(request.getUid());
            userInfo.setIdentityCard(request.getIdentityCard());
            userInfo.setName(request.getName());
            userInfo.setPhoneNumber(request.getPhoneNumber());
            userInfo.setDistrict(request.getDistrict());
            userInfo.setStreet(request.getStreet());
            userInfo.setCommunity(request.getCommunity());
            userInfo.setAddress(request.getAddress());
            userInfoDao.addUserInfo(userInfo);

        } else {
            if (!Objects.equals(userInfo.getUid(), request.getUid())) {
                logger.error("UID mismatch for identity card: {}, expected UID: {}, found UID: {}",request.getIdentityCard(), request.getUid(), userInfo.getUid());
                throw new BusinessException(ExceptionEnum.ID_EXIST);
            }
            userInfo.setName(request.getName());
            userInfo.setPhoneNumber(request.getPhoneNumber());
            userInfo.setDistrict(request.getDistrict());
            userInfo.setStreet(request.getStreet());
            userInfo.setCommunity(request.getCommunity());
            userInfo.setAddress(request.getAddress());
            userInfoDao.updateUserInfo(userInfo);
        }
    }

    @Override
    public void statusNucleicAcidTestUser(StatusNucleicAcidTestUserRequest request) {
        nucleicAcidTestPersonnelDao.updateStatusByTID(request.getStatus(), request.getTid());
    }

    @Override
    public void statusManager(StatusManagerRequest request) {
        healthCodeMangerDao.updateStatusByMID(request.getStatus(), request.getMid());
    }

    @Override
    public void addUserInfo(UserInfoRequest request) {
        UserInfo userInfo_id = userInfoDao.getUserInfoByID(request.getIdentityCard());
        if (userInfo_id != null) {
            logger.error("Duplicate identity card found: {}", request.getIdentityCard());
            throw new BusinessException(ExceptionEnum.ID_EXIST);
        }
        UserInfo userInfo_phone = userInfoDao.getUserInfoByPhone(request.getPhoneNumber());
        if (userInfo_phone != null) {
            logger.error("Duplicate phone number found: {}", request.getPhoneNumber());
            throw new BusinessException(ExceptionEnum.PHONE_EXIST);
        }
        UserInfo userInfo = userInfoDao.getUserInfoByUID(request.getUid());
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setUid(request.getUid());
            userInfo.setIdentityCard(request.getIdentityCard());
            userInfo.setName(request.getName());
            userInfo.setPhoneNumber(request.getPhoneNumber());
            userInfo.setDistrict(request.getDistrict());
            userInfo.setStreet(request.getStreet());
            userInfo.setCommunity(request.getCommunity());
            userInfo.setAddress(request.getAddress());
            userInfoDao.addUserInfo(userInfo);
        } else {
            userInfo.setUid(request.getUid());
            userInfo.setIdentityCard(request.getIdentityCard());
            userInfo.setName(request.getName());
            userInfo.setPhoneNumber(request.getPhoneNumber());
            userInfo.setDistrict(request.getDistrict());
            userInfo.setStreet(request.getStreet());
            userInfo.setCommunity(request.getCommunity());
            userInfo.setAddress(request.getAddress());
            userInfoDao.updateUserInfo(userInfo);
        }
    }

    @Override
    public void userModify(UserModifyInput input) {
        UserInfo userInfo = userInfoDao.getUserInfoByUID(input.getUid());
        if (userInfo != null) {
            userInfo.setName(input.getName());
            userInfo.setPhoneNumber(input.getPhoneNumber());
            userInfo.setDistrict(input.getDistrictId());
            userInfo.setStreet(input.getDistrictId());
            userInfo.setCommunity(input.getCommunityId());
            userInfo.setAddress(input.getAddress());
            userInfoDao.updateUserInfo(userInfo);
        } else {
            logger.error("User not found for UID: {}", input.getUid());
            throw new BusinessException(ExceptionEnum.UID_NOT_FIND);
        }
    }

    @Override
    public void nucleicAcidOpposite(long tid) {
        NucleicAcidTestPersonnel nucleicAcidTestPersonnel = nucleicAcidTestPersonnelDao.getNucleicAcidTestPersonnelByTID(tid);
        if (nucleicAcidTestPersonnel == null) {
            logger.error("Nucleic acid test user not found for TID: {}", tid);
            throw new BusinessException(ExceptionEnum.NUCLEIC_ACID_TEST_USER_NOT_FIND);
        }
        Boolean status = nucleicAcidTestPersonnel.getStatus();
        nucleicAcidTestPersonnelDao.updateStatusByTID(!status, tid);
    }

    @Override
    public void manageOpposite(long mid) {
        HealthCodeManager healthCodeManager = healthCodeMangerDao.getHealthCodeManagerByMID(mid);
        if (healthCodeManager == null) {
            throw new BusinessException(ExceptionEnum.MANAGER_USER_NOT_FIND);
        }
        Boolean status = healthCodeManager.getStatus();
        healthCodeMangerDao.updateStatusByMID(!status, mid);
    }

    @Override
    public String userLoginTest(String code) {
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
        if (userInfoDao.existsById(uid)) {
            userInfoDao.deleteById(uid);
        } else {
            logger.warn("Attempted to delete non-existent user info for UID: {}", uid);
        }
    }
}