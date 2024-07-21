package org.software.code.service.Impl;

import cn.hutool.core.util.IdUtil;
import com.google.common.hash.Funnels;
import org.software.code.common.JWTUtil;
import org.software.code.common.RedisUtil;
import org.software.code.common.WeChatUtil;
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
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.hash.BloomFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


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
    private BloomFilter<CharSequence> bloomFilter = BloomFilter.create(
            Funnels.stringFunnel(StandardCharsets.UTF_8),
            1000000,
            0.01);

    @Override
//    @Cacheable(value = "user_info", key = "#uid")
    public UserInfoDto getUserByUID(long uid) {
        UserInfoDao userInfo = userInfoMapper.getUserInfoByUID(uid);
        if (userInfo == null) {
            throw new NullPointerException("用户不存在，请重试");
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        try {
            BeanUtils.copyProperties(userInfo, userInfoDto);
        } catch (Exception e) {
            throw new RuntimeException("服务执行错误，请稍后重试");
        }
        return userInfoDto;
    }

    @Override
//    @Cacheable(value = "user_info", key = "#identity_card")
    public UserInfoDto getUserByID(String identity_card) {
        UserInfoDao userInfo = userInfoMapper.getUserInfoByID(identity_card);
        if (userInfo == null) {
            throw new NullPointerException("用户不存在，请重试");
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        try {
            BeanUtils.copyProperties(userInfo, userInfoDto);
        } catch (Exception e) {
            throw new RuntimeException("服务执行错误，请稍后重试");
        }
        return userInfoDto;
    }

    @Override
    public String userLogin(String code) {
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
            userMappingMapper.addUserMapping(uidMappingDao); // 假设从存储中获取UID的方法
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
            throw new NullPointerException("用户不存在，请重试");
        }
        if (!userDao.getStatus()) {
            throw new NullPointerException("用户不存在，请重试");
        }
        if (!passwordEncoder.matches(password, userDao.getPassword_hash())) {
            throw new NullPointerException("用户不存在，请重试");
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
            throw new IllegalArgumentException("用户已存在，请重试");
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
            throw new IllegalArgumentException("用户已存在，请重试");
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
        try {
            healthCodeMangerMapper.addHealthCodeManager(newUserDao); // 保存用户到数据库
        } catch (Exception e) {
            throw new RuntimeException("服务执行错误，请稍后重试");
        }
    }

    @Override
    public String managerLogin(String identityCard, String password) {
        HealthCodeManagerDao userDao = healthCodeMangerMapper.getHealthCodeManagerByID(identityCard);
        if (userDao == null) {
            throw new NullPointerException("用户不存在，请重试");
        }
        if (!userDao.getStatus()) {
            throw new NullPointerException("用户不存在，请重试");
        }
        if (!passwordEncoder.matches(password, userDao.getPassword_hash())) {
            throw new NullPointerException("用户不存在，请重试");
        }
        String token = JWTUtil.generateJWToken(userDao.getMid(), 3600000);
        return token;
    }

    @Override
    public void modifyUserInfo(long uid, String name, String phoneNumber, String identityCard, int district, int street, int community, String address) {
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
            try {
                userInfoMapper.addUserInfo(userInfoDao);
            } catch (Exception e) {
                throw new RuntimeException("服务执行错误，请稍后重试");
            }
        } else {
            if (userInfoDao.getUid() != uid) {
                throw new RuntimeException("用户信息错误，请重试");
            }
            userInfoDao.setName(name);
            userInfoDao.setPhone_number(phoneNumber);
            userInfoDao.setDistrict(district);
            userInfoDao.setStreet(street);
            userInfoDao.setCommunity(community);
            userInfoDao.setAddress(address);
            try {
                userInfoMapper.updateUserInfo(userInfoDao);
            } catch (Exception e) {
                throw new RuntimeException("服务执行错误，请稍后重试");
            }
        }
    }

    @Override
    public void statusNucleicAcidTestUser(long tid, boolean status) {
        try {
            nucleicAcidTestPersonnelMapper.updateStatusByTID(status, tid);
        } catch (Exception e) {
            throw new RuntimeException("服务执行错误，请稍后重试");
        }
    }

    @Override
    public void statusManager(long mid, boolean status) {
        try {
            healthCodeMangerMapper.updateStatusByMID(status, mid);
        } catch (Exception e) {
            throw new RuntimeException("服务执行错误，请稍后重试");
        }
    }


    @Override
    public void addUserInfo(long uid, String name, String phoneNumber, String identityCard, int district, int street, int community, String address) {
        // 查询数据库中是否存在该用户信息
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
            try {
                userInfoMapper.addUserInfo(userInfoDao);
            } catch (Exception e) {
                throw new RuntimeException("服务执行错误，请稍后重试");
            }
        } else {
            throw new IllegalArgumentException("用户已存在，请重试");
        }
    }

    @Override
    public void userModify(long uid, String name, String phoneNumber, int districtId, int streetId, int communityId, String address) {
        // 查询数据库中是否存在该用户信息
        UserInfoDao userInfoDao = userInfoMapper.getUserInfoByUID(uid);
        if (userInfoDao != null) {
            userInfoDao.setName(name);
            userInfoDao.setPhone_number(phoneNumber);
            userInfoDao.setDistrict(districtId);
            userInfoDao.setStreet(streetId);
            userInfoDao.setCommunity(communityId);
            userInfoDao.setAddress(address);
            try {
                userInfoMapper.updateUserInfo(userInfoDao);
            } catch (Exception e) {
                throw new RuntimeException("服务执行错误，请稍后重试");
            }
        } else {
            throw new NullPointerException("用户不存在，请重试");
        }
    }


    @Override
    public void nucleicAcidOpposite(long tid) {
        NucleicAcidTestPersonnelDao uerDao = nucleicAcidTestPersonnelMapper.getNucleicAcidTestPersonnelByTID(tid);
        Boolean status = uerDao.getStatus();
        try {
            healthCodeMangerMapper.updateStatusByMID(!status, tid);
        } catch (Exception e) {
            throw new RuntimeException("服务执行错误，请稍后重试");
        }
    }

    @Override
    public void manageOpposite(long mid) {
        HealthCodeManagerDao uerDao = healthCodeMangerMapper.getHealthCodeManagerByMID(mid);
        Boolean status = uerDao.getStatus();
        try {
            healthCodeMangerMapper.updateStatusByMID(!status, mid);
        } catch (Exception e) {
            throw new RuntimeException("服务执行错误，请稍后重试");
        }
    }
}
