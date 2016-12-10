package com.acmerocket.zeus.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DeviceLoader {
    private static final Logger LOG = LoggerFactory.getLogger(Device.class);

    private final ObjectMapper mapper = new ObjectMapper();
    
    public DeviceSet load(String deviceFile) throws IOException {
    	// resolve device file
    	URL url = this.getClass().getResource("/areas/" + deviceFile + ".json");
    	LOG.info("Found URL: {}", url);
        return this.load(url.openStream());
    }
    
    public DeviceSet load(File deviceFile) throws IOException {    	
        DeviceSet devices = this.mapper.readValue(deviceFile, DeviceSet.class);
        return devices;
    }
    
    public DeviceSet load(InputStream deviceIn) throws IOException {    	
        return this.mapper.readValue(deviceIn, DeviceSet.class);
    }
    
    public static void main(String[] args) throws Exception {
        DeviceLoader loader = new DeviceLoader();
        
        DeviceSet devices = loader.load("living-room");
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
