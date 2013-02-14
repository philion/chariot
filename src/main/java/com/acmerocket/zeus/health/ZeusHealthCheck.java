package com.acmerocket.zeus.health;

import com.yammer.metrics.core.HealthCheck;

public class ZeusHealthCheck extends HealthCheck {

    public ZeusHealthCheck() {
        super("zeus");
    }

    @Override
    protected Result check() throws Exception {
        
        // make sure the device manager is healty
        // and that devices are reachable
        
        return Result.healthy();
    }
}
