package org.software.code.common.config;


import org.software.code.common.consts.FSMConst;
import org.software.code.common.consts.FSMConst.HealthCodeColor;
import org.software.code.common.consts.FSMConst.HealthCodeEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

import java.util.EnumSet;


@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<HealthCodeColor, HealthCodeEvent> {

    @Bean
    public StateMachineService<FSMConst.HealthCodeColor, FSMConst.HealthCodeEvent> stateMachineService(
            StateMachineFactory<HealthCodeColor, HealthCodeEvent> stateMachineFactory) {
        return new DefaultStateMachineService<>(stateMachineFactory);
    }

    @Override
    public void configure(StateMachineStateConfigurer<HealthCodeColor, HealthCodeEvent> states) throws Exception {
        states
                .withStates()
                .initial(HealthCodeColor.GREEN)
                .states(EnumSet.allOf(HealthCodeColor.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<HealthCodeColor, HealthCodeEvent> transitions) throws Exception {
        transitions
                .withExternal().source(HealthCodeColor.GREEN).target(HealthCodeColor.RED).event(HealthCodeEvent.FORCE_RED)
                .and()
                .withExternal().source(HealthCodeColor.GREEN).target(HealthCodeColor.YELLOW).event(HealthCodeEvent.FORCE_YELLOW)
                .and()
                .withExternal().source(HealthCodeColor.YELLOW).target(HealthCodeColor.RED).event(HealthCodeEvent.FORCE_RED)
                .and()
                .withExternal().source(HealthCodeColor.YELLOW).target(HealthCodeColor.GREEN).event(HealthCodeEvent.FORCE_GREEN)
                .and()
                .withExternal().source(HealthCodeColor.RED).target(HealthCodeColor.GREEN).event(HealthCodeEvent.FORCE_GREEN)
                .and()
                .withExternal().source(HealthCodeColor.RED).target(HealthCodeColor.YELLOW).event(HealthCodeEvent.FORCE_YELLOW);
    }
}

