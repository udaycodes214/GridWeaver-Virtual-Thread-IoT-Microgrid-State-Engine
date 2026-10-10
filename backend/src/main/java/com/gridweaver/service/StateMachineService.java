package com.gridweaver.service;

import com.gridweaver.model.BatteryState;
import com.gridweaver.statemachine.BatteryEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

@Service
public class StateMachineService {
    private final StateMachineFactory<BatteryState, BatteryEvent> stateMachineFactory;

    @Autowired
    public StateMachineService(StateMachineFactory<BatteryState, BatteryEvent> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public BatteryState transition(BatteryState currentState, double solarPower, double powerDemand, double batteryPercentage,
            boolean faultDetected, boolean stormEnabled) {
        BatteryEvent event = determineEvent(currentState, solarPower, powerDemand, batteryPercentage, faultDetected, stormEnabled);
        if (event == null) {
            return currentState;
        }
        StateMachine<BatteryState, BatteryEvent> machine = stateMachineFactory.getStateMachine();
        machine.start();
        machine.sendEvent(event);
        return machine.getState().getId();
    }

    private BatteryEvent determineEvent(BatteryState currentState, double solarPower, double powerDemand,
            double batteryPercentage, boolean faultDetected, boolean stormEnabled) {
        if (faultDetected || batteryPercentage <= 5) {
            return BatteryEvent.BATTERY_LOW;
        }
        if (stormEnabled && solarPower < 1.5) {
            return BatteryEvent.ENERGY_DEMAND_HIGH;
        }
        if (solarPower > powerDemand + 0.75 && batteryPercentage < 95) {
            return BatteryEvent.SOLAR_SURPLUS;
        }
        if (powerDemand > solarPower + 1.25) {
            return BatteryEvent.ENERGY_DEMAND_HIGH;
        }
        if (currentState == BatteryState.FAULT && batteryPercentage > 30) {
            return BatteryEvent.RECOVERED;
        }
        return null;
    }
}
