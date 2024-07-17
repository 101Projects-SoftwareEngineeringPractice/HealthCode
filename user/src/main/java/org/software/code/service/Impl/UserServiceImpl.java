package org.software.code.service.Impl;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Funnels;
import org.software.code.client.HealthCodeClient;
import org.software.code.common.JWTUtil;
import org.software.code.common.RedisUtil;
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

import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
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
    private HealthCodeClient healthCodeClient;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private KafkaConsumer kafkaConsumer;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private BloomFilter<CharSequence> bloomFilter;
    private JWTUtil uidJwtUtil = new JWTUtil("uid_token_secret_key", 3600000);
    private JWTUtil tidJwtUtil = new JWTUtil("tid_token_secret_key", 3600000);
    private JWTUtil midJwtUtil = new JWTUtil("mid_token_secret_key", 3600000);
    // 假设JWT Token的签名密钥
    String secretKey = "token_secret_key";

    // 初始化布隆过滤器和雪花算法生成器
    @PostConstruct
    public void init() {
        // 假设预期插入的元素个数为 1000000，误判率为 0.01
        bloomFilter = BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8),
                1000000,
                0.01);
    }

    @Override
//    @Cacheable(value = "user_info", key = "#uid")
    public UserInfoDto getUserByUID(long uid) {
        UserInfoDao userInfo = userInfoMapper.getUserInfoByUID(uid);
        if (userInfo == null) {
            throw new RuntimeException("User not found with UID: " + uid);
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
            throw new RuntimeException("User not found with ID: " + identity_card);
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(userInfo, userInfoDto);
        return userInfoDto;
    }

    @Override
    public String userLogin(String code) {
        String openID = getOpenIDFromWX(code);
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

        String token = uidJwtUtil.generateJWToken(uid);
        return token;
    }


    @Override
    public String nucleicAcidTestUserLogin(String identityCard, String password) {
        NucleicAcidTestPersonnelDao userDao = nucleicAcidTestPersonnelMapper.getNucleicAcidTestPersonnelByID(identityCard);
        if (userDao == null) {
            throw new RuntimeException("nucleicAcidTestPersonnel Login Failed: " + identityCard);
        }
        if (!userDao.getStatus()) {
            throw new RuntimeException("nucleicAcidTestPersonnel Login Failed: " + identityCard);
        }
        if (!passwordEncoder.matches(password, userDao.getPassword_hash())) {
            throw new RuntimeException("nucleicAcidTestPersonnel Login Failed: " + identityCard);
        }
        String token = tidJwtUtil.generateJWToken(userDao.getTid());
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
            throw new IllegalArgumentException("NucleicAcidTestPersonnel with this identity card already exists.");
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
            throw new IllegalArgumentException("HealthCodeManager with this identity card already exists.");
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
            throw new RuntimeException("healthCodeMangerMapper Login Failed: " + identityCard);
        }
        if (!userDao.getStatus()) {
            throw new RuntimeException("healthCodeMangerMapper Login Failed: " + identityCard);
        }
        if (!passwordEncoder.matches(password, userDao.getPassword_hash())) {
            throw new RuntimeException("healthCodeMangerMapper Login Failed: " + identityCard);
        }
        String token = midJwtUtil.generateJWToken(userDao.getMid());
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
            userInfoMapper.addUserInfo(userInfoDao);
        } else {
            if (userInfoDao.getUid() != uid) {
                throw new RuntimeException("User Update Failed: " + identityCard);
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
    public long extractUidValidateToken(String token) {
        return uidJwtUtil.extractID(token);
    }

    @Override
    public long extractTidValidateToken(String token) {
        return tidJwtUtil.extractID(token);
    }

    @Override
    public long extractMidValidateToken(String token) {
        return midJwtUtil.extractID(token);
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
            userInfoMapper.addUserInfo(userInfoDao);
        } else {
            throw new IllegalArgumentException("User already exists.: " + uid);
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
            userInfoMapper.updateUserInfo(userInfoDao);
        } else {
            throw new RuntimeException("User Update Failed: " + uid);
        }
    }


    @Override
    public void nucleicAcidOpposite(long tid) {
        NucleicAcidTestPersonnelDao uerDao = nucleicAcidTestPersonnelMapper.getNucleicAcidTestPersonnelByTID(tid);
        Boolean status = uerDao.getStatus();
        healthCodeMangerMapper.updateStatusByMID(!status, tid);
    }

    @Override
    public void manageOpposite(long mid) {
        HealthCodeManagerDao uerDao = healthCodeMangerMapper.getHealthCodeManagerByMID(mid);
        Boolean status = uerDao.getStatus();
        healthCodeMangerMapper.updateStatusByMID(!status, mid);
    }


    private String getOpenIDFromWX(String code) {
        // 假设微信接口的URL和参数
        String wxApiUrl = "https://api.weixin.qq.com/sns/jscode2session";
        String appId = "your_app_id";
        String secret = "your_app_secret";
        String grantType = "authorization_code";

        // 创建 RestTemplate 实例
        RestTemplate restTemplate = new RestTemplate();

        // 构建请求 URL
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=%s", wxApiUrl, appId, secret, code, grantType);

        // 发送 GET 请求，获取微信接口返回的数据
        String response = restTemplate.getForObject(url, String.class);

        String openid = ""; // 初始化 OpenID
        try {
            // 解析 JSON 数据，获取 openid
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            // openid = jsonNode.get("openid").asText();
            openid = "openid-" + code;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // 处理异常，例如记录日志或返回默认值
        }
        return openid;
    }
}
