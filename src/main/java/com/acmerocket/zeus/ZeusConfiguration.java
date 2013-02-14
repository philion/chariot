package com.acmerocket.zeus;

import org.hibernate.validator.constraints.NotEmpty;

import com.acmerocket.zeus.core.Template;
import com.yammer.dropwizard.config.Configuration;

public class ZeusConfiguration extends Configuration {
    @NotEmpty
    private String template;
    
    @NotEmpty
    private String defaultName = "Stranger";

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public Template buildTemplate() {
        return new Template(template, defaultName);
    }
}
