package com.acmerocket.chariot.core;

import java.util.HashMap;
import java.util.Map;

public class CommandSet {
    
    private final Map<String,String> commands = new HashMap<String,String>();

    public String lookup(String command) {
        return this.commands.get(command);
    }
}
