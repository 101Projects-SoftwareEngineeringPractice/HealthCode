package org.software.code.service.impl;

import org.software.code.common.consts.FSMConst;
import org.software.code.dao.HealthCode;
import org.software.code.service.HealthCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class HealthCodeServiceImpl implements HealthCodeService {
    @Autowired
    private StateMachineService<FSMConst.HealthCodeColor, FSMConst.HealthCodeEvent> stateMachineService;

    public String getHealthCodeInfo(String code) {
        return "Health code info for " + code;
    }

    @Transactional
    public int updateHealthCode(FSMConst.HealthCodeEvent event) {
        HealthCode healthCode = new HealthCode();
        healthCode.setColor(FSMConst.HealthCodeColor.GREEN.ordinal());

        String stateMachineId = String.valueOf(healthCode.getId());

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
            return healthCode.getColor();
        } finally {
            stateMachine.stopReactively().block();
            stateMachineService.releaseStateMachine(stateMachineId);
        }
    }
}
