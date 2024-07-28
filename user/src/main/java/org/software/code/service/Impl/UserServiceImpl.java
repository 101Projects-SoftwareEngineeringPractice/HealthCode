package org.software.code.service.Impl;

import cn.hutool.core.util.IdUtil;
import com.google.common.hash.Funnels;
import org.software.code.common.JWTUtil;
import org.software.code.common.RedisUtil;
import org.software.code.common.WeChatUtil;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.dao.HealthCodeManagerDao;
import org.software.code.dao.NucleicAcidTestPersonnelDao;
import org.software.code.dao.UidMappingDao;
import org.software.code.dao.UserInfoDao;
import org.software.code.dto.HealthCodeManagerDto;
import org.software.code.dto.NucleicAcidTestPersonnelDto;
import org.software.code.dto.UserInfoDto;
import org.software.code.kafka.KafkaConsumer;
import org.software.code.kafka.KafkaProducer;
import org.software.code.mapper.HealthCodeMangerMapper;
import org.software.code.mapper.NucleicAcidTestPersonnelMapper;
import org.software.code.mapper.UserInfoMapper;
import org.software.code.mapper.UidMappingMapper;
import org.software.code.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.hash.BloomFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.software.code.common.except.ExceptionEnum.MANAGER_USER_DELETE_FAIL;
import static org.software.code.common.except.ExceptionEnum.USER_LOGIN_EXIST_NULL_FAIL;


@Service
public class UserServiceImpl implements UserService {

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

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private BloomFilter<CharSequence> bloomFilter;

    // 加载持久化的布隆过滤器数据
    private static final String BLOOM_FILTER_FILE = "bloomfilter.data";

    @PostConstruct
    public void init() {
        File file = new File(BLOOM_FILTER_FILE);
        if (file.exists()) {
            try (InputStream is = Files.newInputStream(file.toPath())) {
                bloomFilter = BloomFilter.readFrom(is, Funnels.stringFunnel(Charset.defaultCharset()));
            } catch (IOException e) {
                e.printStackTrace();
                // 初始化新的布隆过滤器实例
                bloomFilter = BloomFilter.create(
                        Funnels.stringFunnel(StandardCharsets.UTF_8),
                        1000000,
                        0.01);
            }
        } else {
            bloomFilter = BloomFilter.create(
                    Funnels.stringFunnel(StandardCharsets.UTF_8),
                    1000000,
                    0.01);
        }
    }

    @PreDestroy
    public void shutdown() {
        try (OutputStream os = Files.newOutputStream(Paths.get(BLOOM_FILTER_FILE))) {
            bloomFilter.writeTo(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
//    @Cacheable(value = "user_info", key = "#uid")
    public UserInfoDto getUserByUID(long uid) {
        UserInfoDao userInfo = userInfoMapper.getUserInfoByUID(uid);
        if (userInfo == null) {
            throw new BusinessException(ExceptionEnum.UID_NOT_FIND);
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(userInfo, userInfoDto);
        return userInfoDto;
    }

    @Override
//    @Cacheable(value = "user_info", key = "#identity_card")
    public UserInfoDto getUserByID(String identity_card) {
        UserInfoDao userInfo = userInfoMapper.getUserInfoByID(identity_card);
        if (userInfo == null) {
            throw new BusinessException(ExceptionEnum.ID_NOT_FIND);
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(userInfo, userInfoDto);
        return userInfoDto;
    }

    @Override
    public String userLogin(String code) {
        // 确保bloomFilter已经初始化
        if (bloomFilter == null) {
            init();
        }

        String openID = WeChatUtil.getOpenIDFromWX(code);
        boolean exists = bloomFilter.mightContain(openID);

        // 如果OpenID不存在，则生成新的UID（假设使用雪花算法生成）
        long uid;
        if (!exists) {
            uid = IdUtil.getSnowflake().nextId();
            // 将新的OpenID添加到布隆过滤器中
            bloomFilter.put(openID);
            UidMappingDao uidMappingDao = new UidMappingDao();
            uidMappingDao.setUid(uid);
            uidMappingDao.setOpenid(openID);
            try {
                userMappingMapper.addUserMapping(uidMappingDao); // 假设从存储中获取UID的方法
            } catch (Exception e) {
                uid = userMappingMapper.getUidMappingByOpenID(openID).getUid(); // 假设从存储中获取UID的方法
            }
        } else {
            // 如果OpenID已存在，则获取其对应的UID（假设通过数据库或缓存获取）
            uid = userMappingMapper.getUidMappingByOpenID(openID).getUid(); // 假设从存储中获取UID的方法
        }
        String token = JWTUtil.generateJWToken(uid, 3600000);
        return token;
    }

    @Override
    public String nucleicAcidTestUserLogin(String identityCard, String password) {
        NucleicAcidTestPersonnelDao userDao = nucleicAcidTestPersonnelMapper.getNucleicAcidTestPersonnelByID(identityCard);
        if (userDao == null) {
            throw new BusinessException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        if (!userDao.getStatus()) {
            throw new BusinessException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        if (!passwordEncoder.matches(password, userDao.getPassword_hash())) {
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
        // 检查是否存在相同身份证号的用户
        NucleicAcidTestPersonnelDao existingUserDao = nucleicAcidTestPersonnelMapper.getNucleicAcidTestPersonnelByID(identityCard);
        if (existingUserDao != null) {
            throw new BusinessException(ExceptionEnum.ID_EXIST);
        }
        // 创建新用户
        NucleicAcidTestPersonnelDao newUserDao = new NucleicAcidTestPersonnelDao();  // 创建新用户
        newUserDao.setIdentity_card(identityCard);
        String password_hash = passwordEncoder.encode(password);
        newUserDao.setPassword_hash(password_hash);
        newUserDao.setName(name);
        newUserDao.setStatus(true);
        long tid = IdUtil.getSnowflake().nextId();
        newUserDao.setTid(tid);
        nucleicAcidTestPersonnelMapper.addNucleicAcidTestPersonnel(newUserDao); // 保存用户到数据库
    }

    @Override
    public void newMangerUser(String identityCard, String password, String name) {
        // 检查是否存在相同身份证号的用户
        HealthCodeManagerDao existingUserDao = healthCodeMangerMapper.getHealthCodeManagerByID(identityCard);
        if (existingUserDao != null) {
            throw new BusinessException(ExceptionEnum.ID_EXIST);
        }
        // 创建新用户
        HealthCodeManagerDao newUserDao = new HealthCodeManagerDao();  // 创建新用户
        newUserDao.setIdentity_card(identityCard);
        String password_hash = passwordEncoder.encode(password);
        newUserDao.setPassword_hash(password_hash);
        newUserDao.setName(name);
        newUserDao.setStatus(true);
        long mid = IdUtil.getSnowflake().nextId();
        newUserDao.setMid(mid);
        healthCodeMangerMapper.addHealthCodeManager(newUserDao); // 保存用户到数据库
    }

    @Override
    public String managerLogin(String identityCard, String password) {
        HealthCodeManagerDao userDao = healthCodeMangerMapper.getHealthCodeManagerByID(identityCard);
        if (userDao == null) {
            throw new BusinessException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        if (!userDao.getStatus()) {
            throw new BusinessException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        if (!passwordEncoder.matches(password, userDao.getPassword_hash())) {
            throw new BusinessException(ExceptionEnum.USER_PASSWORD_ERROR);
        }
        String token = JWTUtil.generateJWToken(userDao.getMid(), 3600000);
        return token;
    }

    @Override
    public void modifyUserInfo(long uid, String name, String phoneNumber, String identityCard, int district, int street, long community, String address) {
        // 查询数据库中是否存在该用户信息
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
        // 查询数据库中是否存在该用户信息
        UserInfoDao userInfoDao_id = userInfoMapper.getUserInfoByID(identityCard);
        if (userInfoDao_id != null) throw new BusinessException(ExceptionEnum.ID_EXIST);
        UserInfoDao userInfoDao_phone = userInfoMapper.getUserInfoByPhone(phoneNumber);
        if (userInfoDao_phone != null) throw new BusinessException(ExceptionEnum.PHONE_EXIST);
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
        // 查询数据库中是否存在该用户信息
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
            throw new BusinessException(ExceptionEnum.UID_NOT_FIND);
        }
    }


    @Override
    public void nucleicAcidOpposite(long tid) {
        NucleicAcidTestPersonnelDao uerDao = nucleicAcidTestPersonnelMapper.getNucleicAcidTestPersonnelByTID(tid);
        if (uerDao == null) {
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
        // 确保bloomFilter已经初始化
        if (bloomFilter == null) {
            init();
        }

        String openID = "openid-" + code;
        boolean exists = bloomFilter.mightContain(openID);

        // 如果OpenID不存在，则生成新的UID（假设使用雪花算法生成）
        long uid;
        if (!exists) {
            try {
                uid = IdUtil.getSnowflake().nextId();

                UidMappingDao uidMappingDao = new UidMappingDao();
                uidMappingDao.setUid(uid);
                uidMappingDao.setOpenid(openID);
                userMappingMapper.addUserMapping(uidMappingDao); // 假设从存储中获取UID的方法
                bloomFilter.put(openID); // 将新的OpenID添加到布隆过滤器中
            } catch (Exception e) {
                UidMappingDao uidMappingDao = userMappingMapper.getUidMappingByOpenID(openID); // 从存储中获取UID的方法
                if (uidMappingDao == null) {
                    throw new BusinessException(ExceptionEnum.USER_LOGIN_EXIST_NULL_FAIL);
                }
                uid = uidMappingDao.getUid();
            }
        } else { // 如果OpenID已存在，则获取其对应的UID（假设通过数据库或缓存获取）
            try {
                uid = userMappingMapper.getUidMappingByOpenID(openID).getUid(); // 从存储中获取UID的方法
            } catch (Exception e) {
                uid = IdUtil.getSnowflake().nextId();
                UidMappingDao uidMappingDao = new UidMappingDao();
                uidMappingDao.setUid(uid);
                uidMappingDao.setOpenid(openID);
                userMappingMapper.addUserMapping(uidMappingDao); // 假设从存储中获取UID的方法
                bloomFilter.put(openID); // 将新的OpenID添加到布隆过滤器中
            }
        }
        String token = JWTUtil.generateJWToken(uid, 3600000);
        return token;
    }

    @Override
    public void deleteUserInfo(long uid) {
        if (userInfoMapper.existsById(uid)) {
            userInfoMapper.deleteById(uid);
        }
    }
}
