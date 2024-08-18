package org.software.code.service.impl;

import org.software.code.common.consts.FSMConst;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.dto.*;
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
import org.software.code.mapper.HealthCodeMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class HealthCodeServiceImpl implements HealthCodeService {
    private static final Logger logger = LogManager.getLogger(HealthCodeServiceImpl.class);

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
            logger.error("Health code already exists for UID: {}", uid);
            throw new BusinessException(ExceptionEnum.HEALTH_CODE_EXIST);
        }
    }

    @Override
    public HealthQRCodeDto getHealthCode(long uid) {
        HealthCodeDao healthCodeDao = healthCodeMapper.getHealthCodeByUID(uid);
        if (healthCodeDao == null) {
            logger.error("Health code not found for UID: {}", uid);
            throw new BusinessException(ExceptionEnum.HEALTH_CODE_NOT_FIND);
        }
        HealthQRCodeDto healthQRCodeDto = new HealthQRCodeDto();
        healthQRCodeDto.setStatus(healthCodeDao.getColor());
        healthQRCodeDto.setQrcode_token(JWTUtil.generateJWToken(uid, 60000));
        return healthQRCodeDto;
    }

    @Override
    public void transcodingHealthCodeEvents(long uid, FSMConst.HealthCodeEvent event) {
        HealthCodeDao healthCode = healthCodeMapper.getHealthCodeByUID(uid);
        if (healthCode == null) {
            logger.error("Health code not found for UID: {}", uid);
            throw new BusinessException(ExceptionEnum.HEALTH_CODE_NOT_FIND);
        }
        String stateMachineId = String.valueOf(healthCode.getUid());
        StateMachine<FSMConst.HealthCodeColor, FSMConst.HealthCodeEvent> stateMachine = stateMachineService.acquireStateMachine(stateMachineId);
        stateMachine.stopReactively().block();
        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(access -> access.resetStateMachineReactively(
                        new DefaultStateMachineContext<>(FSMConst.HealthCodeColor.values()[healthCode.getColor()], null, null, null)).block());
        stateMachine.startReactively().block();
        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(event).build())).blockFirst();
        FSMConst.HealthCodeColor newColor = stateMachine.getState().getId();
        int color = newColor.ordinal();
        healthCode.setColor(color);
        healthCodeMapper.updateColorByUID(color, uid);
        stateMachine.stopReactively().block();
        stateMachineService.releaseStateMachine(stateMachineId);
    }

    @Override
    public void applyCode(long uid, String name, String phoneNumber, String identityCard, int districtId, int streetId, long communityId, String address) {
        UserInfoRequest userInfoRequest = new UserInfoRequest(uid, name, phoneNumber, identityCard, districtId, streetId, communityId, address);
        userClient.addUserInfo(userInfoRequest);
        HealthCodeDao healthCodeDao = new HealthCodeDao();
        healthCodeDao.setUid(uid);
        healthCodeDao.setColor(1);
        healthCodeMapper.addHealthCode(healthCodeDao);
    }

    @Override
    public GetCodeDto getCode(long uid) {
        HealthCodeDao healthCodeDao = healthCodeMapper.getHealthCodeByUID(uid);
        if (healthCodeDao == null) {
            logger.error("Health code not found for UID: {}", uid);
            throw new BusinessException(ExceptionEnum.HEALTH_CODE_NOT_FIND);
        }
        Result<?> result = userClient.getUserByUID(uid);
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoDto userInfoDto = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        GetCodeDto getCodeDto = new GetCodeDto();
        getCodeDto.setToken(JWTUtil.generateJWToken(uid, 60000));
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
        if (healthCodeDao == null) {
            logger.error("Health code not found for UID: {}", uid);
            throw new BusinessException(ExceptionEnum.HEALTH_CODE_NOT_FIND);
        }
        HealthCodeInfoDto healthCodeInfoDto = new HealthCodeInfoDto();
        healthCodeInfoDto.setUid(uid);
        healthCodeInfoDto.setName(userInfoDto.getName());
        healthCodeInfoDto.setStatus(healthCodeDao.getColor());
        healthCodeInfoDto.setIdentity_card(identityCard);
        return healthCodeInfoDto;
    }
}