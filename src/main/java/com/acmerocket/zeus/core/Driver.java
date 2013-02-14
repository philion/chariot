package com.acmerocket.zeus.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public interface Driver {
    
    public String sendRawCommand(String command) throws IOException;
    
    public static final class Factory {
        private static final Map<String,Class<? extends Driver>> drivers = new HashMap<String,Class<? extends Driver>>();
        static {
            drivers.put("http", HttpDriver.class);
            drivers.put("telnet", TelnetDriver.class);
        }
        
        public static Driver build(String driverName, Map<String,String> deviceParams) {
            // assume a Map<String,String> constructor
            
            return null;
            
        }
    }
}
