package com.acmerocket.chariot.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DeviceLoader {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceLoader.class);
    
    private static final String ROOM_PATH = "/areas/";
    private static final String ROOM_EXT = ".json";

    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    public static DeviceSet load(String deviceFile) throws IOException {
    	// resolve device file
    	URL url = DeviceLoader.class.getResource(ROOM_PATH + deviceFile + ROOM_EXT);
    	LOG.debug("Found URL: {}", url);
    	
        return load(url.openStream());
    }
    
    public static DeviceSet load(File deviceFile) throws IOException {    	
        DeviceSet devices = MAPPER.readValue(deviceFile, DeviceSet.class);
        return devices;
    }
    
    public static DeviceSet load(InputStream deviceIn) throws IOException {    	
        return MAPPER.readValue(deviceIn, DeviceSet.class);
    }
    
    public static void main(String[] args) throws Exception {        
        DeviceSet devices = load("living-room");
        LOG.info("Devices: {}", devices);
        LOG.info("Commands: {}", devices.getCommands());
        
        //Receiver receiver = devices.reciever("denon");
        //LOG.debug("volume: {}", receiver.volume());
        //receiver.setVolume("50");
        //LOG.debug("volume: {}", receiver.volume());


        
        //receiver.pwrOff();
        //LOG.debug("receiver: {}", receiver);
        
//        receiver.pwrOn();
//        
//        Thread.sleep(10000);
//        
//        System.out.println("volume: " + receiver.volume());
//        
//        Thread.sleep(1000);
//
//        receiver.setVolume("63"); // FIXME ?
//        
//        Thread.sleep(1000);
//        
//        System.out.println("volume: " + receiver.volume());
//        
//        Thread.sleep(1000);
//        
//        receiver.pwrOff();
    }
}
