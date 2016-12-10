package com.acmerocket.chariot;

import com.acmerocket.chariot.cli.RenderCommand;
import com.acmerocket.chariot.health.ChariotHealthCheck;
import com.acmerocket.chariot.resources.DevicesResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class ChariotService extends Service<ChariotConfig> {
    public static void main(String[] args) throws Exception {
        new ChariotService().run(args);
    }

    @Override
    public void initialize(Bootstrap<ChariotConfig> bootstrap) {
        bootstrap.setName("chariot");
        bootstrap.addCommand(new RenderCommand());
        bootstrap.addBundle(new AssetsBundle());
    }

    @Override
    public void run(ChariotConfig configuration, Environment environment) {

        environment.addHealthCheck(new ChariotHealthCheck());
        environment.addResource(new DevicesResource());

    }
}
