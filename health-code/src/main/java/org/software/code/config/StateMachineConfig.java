package org.software.code.config;


import org.software.code.common.consts.FSMConst.HealthCodeColor;
import org.software.code.common.consts.FSMConst.HealthCodeEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;


@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<HealthCodeColor, HealthCodeEvent> {

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

