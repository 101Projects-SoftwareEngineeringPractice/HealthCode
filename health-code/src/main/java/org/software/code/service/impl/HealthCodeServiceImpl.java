package org.software.code.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.software.code.client.UserClient;
import org.software.code.common.result.Result;
import org.software.code.dao.HealthCodeDao;
import org.software.code.dto.GetCodeDto;
import org.software.code.dto.HealthCodeInfoDto;
import org.software.code.dto.HealthQRCodeDto;
import org.software.code.dto.UserInfoDto;
import org.software.code.mapper.HealthCodeMapper;
import org.software.code.service.HealthCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HealthCodeServiceImpl implements HealthCodeService {
    @Autowired
    private UserClient userClient;
    @Autowired
    private HealthCodeMapper healthCodeMapper;

    String secretKey = "qrtoken_secret_key";

    @Override
    public void applyHealthCode(long uid) {
        HealthCodeDao healthCodeDao = healthCodeMapper.getHealthCodeByUID(uid);
        if (healthCodeDao != null) {
            throw new IllegalArgumentException("healthCode with this uid already exists: " + uid);
        }
    }

    @Override
    public HealthQRCodeDto getHealthCode(long uid) {
        HealthCodeDao healthCodeDao = healthCodeMapper.getHealthCodeByUID(uid);
        if (healthCodeDao == null) {
            throw new RuntimeException("healthCode not found with UID: " + uid);
        }
        HealthQRCodeDto healthQRCodeDto = new HealthQRCodeDto();
        healthQRCodeDto.setStatus(healthCodeDao.getColor());
        healthQRCodeDto.setQrcode_token(generateJWTQRCodeToken(uid));
        return healthQRCodeDto;
    }

    @Override
    public void transcodingHealthCodeEvents(long uid, int event) {
        HealthCodeDao healthCodeDao = healthCodeMapper.getHealthCodeByUID(uid);
        if (healthCodeDao == null) {
            throw new RuntimeException("healthCode not found with UID: " + uid);
        }
        healthCodeDao.setColor(event);
        healthCodeMapper.updateColorByUID(event, uid);


    }

    /****接口***/
    @Override
    public void applyCode(long uid, String name, String phoneNumber, String identityCard, int districtId, int streetId, int communityId, String address) {
        userClient.addUserInfo(uid, name, phoneNumber, identityCard, districtId, streetId, communityId, address);
        HealthCodeDao healthCodeDao=new HealthCodeDao();
        healthCodeDao.setUid(uid);
        healthCodeDao.setColor(1);
        healthCodeMapper.addHealthCode(healthCodeDao);
    }

    @Override
    public GetCodeDto getCode(long uid) {
        HealthCodeDao healthCodeDao = healthCodeMapper.getHealthCodeByUID(uid);
        if (healthCodeDao==null){
            throw new RuntimeException("healthCode not found with UID: " + uid);
        }
        Result<?> result = userClient.getUserByUID(uid);
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoDto userInfoDto = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        GetCodeDto getCodeDto = new GetCodeDto();
        getCodeDto.setToken(generateJWTQRCodeToken(uid));
        getCodeDto.setStatus(healthCodeDao.getColor());
        getCodeDto.setName(userInfoDto.getName());
        return getCodeDto;
    }

    @Override
    public HealthCodeInfoDto getHealthCodeInfo(String identityCard) {
        Result<?> result = userClient.getUserByID(identityCard);
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoDto userInfoDto = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        long uid = userInfoDto.getUid();
        HealthCodeDao healthCodeDao = healthCodeMapper.getHealthCodeByUID(uid);
        if (healthCodeDao==null){
            throw new RuntimeException("healthCode not found with UID: " + uid);
        }
        HealthCodeInfoDto healthCodeInfoDto = new HealthCodeInfoDto();
        healthCodeInfoDto.setUid(uid);
        healthCodeInfoDto.setName(userInfoDto.getName());
        healthCodeInfoDto.setStatus(healthCodeDao.getColor());
        healthCodeInfoDto.setIdentity_card(identityCard);
        return healthCodeInfoDto;
    }


    /*****工具****/
    @Override
    public long extractUidValidateToken(String token) {
        Result<?> result = userClient.extractUidValidateToken(token);
        return (Long) result.getData();
    }

    private String generateJWTQRCodeToken(long uid) {
        // 设置 JWT Token 的过期时间，例如设置为 1 分钟
        long expirationTime = 60000; // 1 minter

        // 生成 JWT Token
        String qrcode_token = Jwts.builder()
                .setSubject(Long.toString(uid))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return qrcode_token;
    }
    @Override
    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public long extractUID(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
    @Override
    public long extractUIDValidateQRCodeToken(String qrcode_token) {
        if (!validateToken(qrcode_token)) {
            throw new RuntimeException("Token invalid");
        }
        return extractUID(qrcode_token);
    }

    /*****测试**/
//    public String getHealthCodeInfo(String code) {
//        return "Health code info for " + code;
//    }


}
