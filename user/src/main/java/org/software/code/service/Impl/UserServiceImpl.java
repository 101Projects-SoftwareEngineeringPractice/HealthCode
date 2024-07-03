package org.software.code.service.Impl;

import org.software.code.client.HealthCodeClient;
import org.software.code.common.RedisUtil;
import org.software.code.dao.UserInfoDao;
import org.software.code.dto.UserInfoDto;
import org.software.code.kafka.KafkaProducer;
import org.software.code.mapper.UserMapper;
import org.software.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HealthCodeClient healthCodeClient;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public String testOthers() {
        redisUtil.setValue("test", "test");
        kafkaProducer.sendMessage("test", "nnd");
        return healthCodeClient.getHealthCodeInfo("tnnd");
    }

    @Override
    @Cacheable(value = "user_info", key = "#uid") //检测缓存
    public UserInfoDto getInfo(Long uid) {
        return null;
    }

    @Override
    @CacheEvict(value = "user_info", key = "#uid") //移除缓存
    public void updateInfo(Long uid, int district) {
        userMapper.updateUser(district, uid);
    }

    @Override
    @Cacheable(value = "user_info", key = "#uid") //检测缓存
    public UserInfoDto getUserByUID(long uid) {
        UserInfoDao userInfo = userMapper.getUserByUID(uid);
        logger.error("User found: {}", userInfo);
        if (userInfo == null) {
            UserInfoDto nullInfo = new UserInfoDto();
            nullInfo.setUid(0L);
            return nullInfo;
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(userInfo, userInfoDto);
        return userInfoDto;
    }

    @Override
    @Cacheable(value = "user_info", key = "#identity_card")
    public UserInfoDto getUserByID(String identity_card) {
        UserInfoDao userInfo = userMapper.getUserByID(identity_card);
        logger.error("User found: {}", userInfo);
        if (userInfo == null) {
            UserInfoDto nullInfo = new UserInfoDto();
            nullInfo.setUid(0L);
            return nullInfo;
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(userInfo, userInfoDto);
        return userInfoDto;
    }
}
