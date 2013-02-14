package com.acmerocket.zeus.core;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

public class HttpDevice extends Device {

    @JsonCreator
    public HttpDevice(Map<String, Object> props) {
        super(props);
        // TODO Auto-generated constructor stub
    }

}
