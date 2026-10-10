package com.gridweaver.statemachine;

import com.gridweaver.model.BatteryState;
import java.util.EnumSet;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachineFactory
public class BatteryStateMachineConfig extends StateMachineConfigurerAdapter<BatteryState, BatteryEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<BatteryState, BatteryEvent> states) throws Exception {
        states.withStates()
                .initial(BatteryState.IDLE)
                .states(EnumSet.allOf(BatteryState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<BatteryState, BatteryEvent> transitions) throws Exception {
        transitions
                .withExternal().source(BatteryState.IDLE).target(BatteryState.CHARGING).event(BatteryEvent.SOLAR_SURPLUS)
                .and().withExternal().source(BatteryState.CHARGING).target(BatteryState.IDLE).event(BatteryEvent.RECOVERED)
                .and().withExternal().source(BatteryState.IDLE).target(BatteryState.DISCHARGING).event(BatteryEvent.ENERGY_DEMAND_HIGH)
                .and().withExternal().source(BatteryState.CHARGING).target(BatteryState.DISCHARGING).event(BatteryEvent.ENERGY_DEMAND_HIGH)
                .and().withExternal().source(BatteryState.IDLE).target(BatteryState.FAULT).event(BatteryEvent.BATTERY_LOW)
                .and().withExternal().source(BatteryState.CHARGING).target(BatteryState.FAULT).event(BatteryEvent.BATTERY_LOW)
                .and().withExternal().source(BatteryState.DISCHARGING).target(BatteryState.FAULT).event(BatteryEvent.BATTERY_LOW)
                .and().withExternal().source(BatteryState.FAULT).target(BatteryState.IDLE).event(BatteryEvent.RECOVERED)
                .and().withExternal().source(BatteryState.IDLE).target(BatteryState.FAULT).event(BatteryEvent.FAULT_DETECTED)
                .and().withExternal().source(BatteryState.CHARGING).target(BatteryState.FAULT).event(BatteryEvent.FAULT_DETECTED)
                .and().withExternal().source(BatteryState.DISCHARGING).target(BatteryState.FAULT).event(BatteryEvent.FAULT_DETECTED);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<BatteryState, BatteryEvent> config) throws Exception {
        config.withConfiguration()
                .autoStartup(true);
    }
}
