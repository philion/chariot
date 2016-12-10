package com.acmerocket.chariot.health;

import com.yammer.metrics.core.HealthCheck;

public class ChariotHealthCheck extends HealthCheck {

    public ChariotHealthCheck() {
        super("chariot");
    }

    @Override
    protected Result check() throws Exception {
        
        // make sure the device manager is healty
        // and that devices are reachable
        
        return Result.healthy();
    }
}
