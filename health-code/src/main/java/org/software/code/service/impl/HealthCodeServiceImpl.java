package org.software.code.service.impl;

import org.software.code.common.consts.FSMConst;
import org.software.code.dao.HealthCode;
import org.software.code.service.HealthCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.statemachine.support.DefaultStateMachineContext;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class HealthCodeServiceImpl implements HealthCodeService {
    @Autowired
    private StateMachineService<FSMConst.HealthCodeColor, FSMConst.HealthCodeEvent> stateMachineService;
    @Autowired

    private UserClient userClient;
    @Autowired
    private HealthCodeMapper healthCodeMapper;

    @Override
    public void applyHealthCode(long uid) {
        HealthCodeDao healthCodeDao = healthCodeMapper.getHealthCodeByUID(uid);
        if (healthCodeDao != null) {
            throw new IllegalArgumentException("健康码信息已存在，请重试");
        }
    }

    @Override
    public HealthQRCodeDto getHealthCode(long uid) {
        HealthCodeDao healthCodeDao = healthCodeMapper.getHealthCodeByUID(uid);
        if (healthCodeDao == null) {
            throw new NullPointerException("健康码信息不存在，请重试");
        }
        HealthQRCodeDto healthQRCodeDto = new HealthQRCodeDto();
        healthQRCodeDto.setStatus(healthCodeDao.getColor());
        healthQRCodeDto.setQrcode_token(JWTUtil.generateJWToken(uid, 60000));
        return healthQRCodeDto;
    }

    @Override
    public int transcodingHealthCodeEvents(long uid, FSMConst.HealthCodeEvent event) {
        HealthCodeDao healthCode = healthCodeMapper.getHealthCodeByUID(uid);
        if (healthCode == null) {
            throw new NullPointerException("健康码信息不存在，请重试");
        }
        healthCode.setColor(FSMConst.HealthCodeColor.GREEN.ordinal());

        String stateMachineId = String.valueOf(healthCode.getUid());

        StateMachine<FSMConst.HealthCodeColor, FSMConst.HealthCodeEvent> stateMachine = stateMachineService.acquireStateMachine(stateMachineId);
        try {
            stateMachine.stopReactively().block();
            stateMachine.getStateMachineAccessor()
                    .doWithAllRegions(access -> access.resetStateMachineReactively(
                            new DefaultStateMachineContext<>(FSMConst.HealthCodeColor.values()[healthCode.getColor()], null, null, null)).block());
            stateMachine.startReactively().block();
            stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(event).build())).blockFirst();
            FSMConst.HealthCodeColor newColor = stateMachine.getState().getId();
            healthCode.setColor(newColor.ordinal());
            int color = healthCode.getColor();
            healthCodeMapper.updateColorByUID(color, uid);
            return color;
        } catch (Exception e) {
            throw new RuntimeException("服务执行错误，请稍后重试");
        } finally {
            stateMachine.stopReactively().block();
            stateMachineService.releaseStateMachine(stateMachineId);
        }
    }

    @Override
    public void applyCode(long uid, String name, String phoneNumber, String identityCard, int districtId, int streetId, int communityId, String address) {
        userClient.addUserInfo(uid, name, phoneNumber, identityCard, districtId, streetId, communityId, address);
        HealthCodeDao healthCodeDao = new HealthCodeDao();
        healthCodeDao.setUid(uid);
        healthCodeDao.setColor(1);
        try {
            healthCodeMapper.addHealthCode(healthCodeDao);
        } catch (Exception e) {
            throw new RuntimeException("服务执行错误，请稍后重试");
        }

    }

    @Override
    public GetCodeDto getCode(long uid) {
        HealthCodeDao healthCodeDao = healthCodeMapper.getHealthCodeByUID(uid);
        if (healthCodeDao == null) {
            throw new NullPointerException("健康码信息不存在，请重试");
        }
        UserInfoDto userInfoDto;
        try {
            Result<?> result = userClient.getUserByUID(uid);
            ObjectMapper objectMapper = new ObjectMapper();
            userInfoDto = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        } catch (Exception e) {
            throw new NullPointerException("用户不存在，请重试");
        }
        GetCodeDto getCodeDto = new GetCodeDto();
        getCodeDto.setToken(JWTUtil.generateJWToken(uid, 60000));
        getCodeDto.setStatus(healthCodeDao.getColor());
        getCodeDto.setName(userInfoDto.getName());
        return getCodeDto;
    }

    @Override
    public HealthCodeInfoDto getHealthCodeInfo(String identityCard) {
        UserInfoDto userInfoDto;
        try {
            Result<?> result = userClient.getUserByID(identityCard);
            ObjectMapper objectMapper = new ObjectMapper();
            userInfoDto = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        } catch (Exception e) {
            throw new RuntimeException("服务执行错误，请稍后重试");
        }
        if (userInfoDto == null) {
            throw new NullPointerException("用户不存在，请重试");
        }
        long uid = userInfoDto.getUid();
        HealthCodeDao healthCodeDao = healthCodeMapper.getHealthCodeByUID(uid);
        if (healthCodeDao == null) {
            throw new NullPointerException("健康码信息不存在，请重试");
        }
        HealthCodeInfoDto healthCodeInfoDto = new HealthCodeInfoDto();
        healthCodeInfoDto.setUid(uid);
        healthCodeInfoDto.setName(userInfoDto.getName());
        healthCodeInfoDto.setStatus(healthCodeDao.getColor());
        healthCodeInfoDto.setIdentity_card(identityCard);
        return healthCodeInfoDto;
    }
}
