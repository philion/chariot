package com.acmerocket.zeus;

import com.acmerocket.zeus.cli.RenderCommand;
import com.acmerocket.zeus.health.ZeusHealthCheck;
import com.acmerocket.zeus.resources.DevicesResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class ZeusService extends Service<ZeusConfiguration> {
    public static void main(String[] args) throws Exception {
        new ZeusService().run(args);
    }

    @Override
    public void initialize(Bootstrap<ZeusConfiguration> bootstrap) {
        bootstrap.setName("zeus");
        bootstrap.addCommand(new RenderCommand());
        bootstrap.addBundle(new AssetsBundle());
    }

    @Override
    public void run(ZeusConfiguration configuration, Environment environment) {

        environment.addHealthCheck(new ZeusHealthCheck());
        environment.addResource(new DevicesResource());

    }
}
