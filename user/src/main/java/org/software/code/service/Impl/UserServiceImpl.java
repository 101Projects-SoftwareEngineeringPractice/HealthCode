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

@Service
public class UserServiceImpl implements UserService {

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
    @Cacheable(value = "user_info", key = "#uid")
    public UserInfoDto getInfo(Long uid) {
        UserInfoDao userInfoDao = userMapper.find(uid);
        if (userInfoDao == null) {
            return null;
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setId(userInfoDao.getId());
        userInfoDto.setName(userInfoDao.getName());
        return userInfoDto;
    }

    @Override
    @CacheEvict(value = "user_info", key = "#uid")
    public void updateInfo(Long uid, int district) {
        userMapper.updateUser(district, uid);
    }
}
