package org.software.code.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.software.code.client.UserClient;
import org.software.code.common.JWTUtil;
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

@Service
public class HealthCodeServiceImpl implements HealthCodeService {
    @Autowired
    private UserClient userClient;
    @Autowired
    private HealthCodeMapper healthCodeMapper;
    private JWTUtil qrCodeJwtUtil=new JWTUtil("qr_code_token_secret_key",3600000);

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
        healthQRCodeDto.setQrcode_token(qrCodeJwtUtil.generateJWToken(uid));
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
        getCodeDto.setToken(qrCodeJwtUtil.generateJWToken(uid));
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



    @Override
    public long extractUidValidateToken(String token) {
        Result<?> result = userClient.extractUidValidateToken(token);
        return (Long) result.getData();
    }
    @Override
    public long extractMidValidateToken(String token) {
        Result<?> result = userClient.extractMidValidateToken(token);
        return (Long) result.getData();
    }

    @Override
    public long extractUIDValidateQRCodeToken(String qrcode_token) {
        return qrCodeJwtUtil.extractID(qrcode_token);
    }
//



}
