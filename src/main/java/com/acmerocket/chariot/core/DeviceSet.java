package com.acmerocket.chariot.core;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceSet {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceSet.class);

    private Map<String,Device> devices = new HashMap<>();
    private Map<String,String> defaults = new HashMap<>();
    
    public synchronized void setDevices(Collection<Device> devices) {
    	this.addAll(devices);
    }
    
    public void addAll(Collection<Device> devices) {
        for (Device device : devices) {
            this.devices.put(device.getName(), device);
        }
    }
    
    public synchronized void setDefaults(Map<String,String> map) {
    	this.defaults = map;
    }
    
    public Device get(String name) {
        Device device = this.devices.get(name);
        if (device != null) {
        	return device;
        }
        else {
        	throw new DeviceException("Unknow device: " + name, this.getDeviceNames().toString());
        }
    }
    
    public String toString() {
        return this.getDeviceNames();
    }
    
    public Receiver reciever(String name) {
        Device device = this.get(name);
        return Receiver.Factory.wrap(device);
    }

    public String getDeviceNames() {
        return this.devices.keySet().toString();
    }
    
    public String getCommands() {
    	Set<String> commands = new HashSet<String>();
    	for (Device device : this.devices.values()) {
    		commands.addAll(device.getCommands());
    	}
    	return commands.toString();
    }
    
    public String getCommands(String device) {
    	return this.get(device).getCommands().toString();
    }
    
    public String sendCommand(String deviceName, String command, String[] opts) {
    	// special case. abstraction?
    	if ("load".equalsIgnoreCase(deviceName)) {
    		try {
				this.load(command);
				if (opts != null && opts.length > 0) {
					for (String opt : opts) {
						this.load(opt);
					}
				}
				return deviceName + ": " + command + String.join(" ", opts); 
			} 
    		catch (IOException ex) {
    			LOG.error("Problem loading area {}", command, ex);
    			return "error, can't load - unknown: " + command + String.join(" ", opts); 
			}
    	}
    	else {
	        Device device = this.devices.get(deviceName);
	        if (device != null) {
	        	String result = device.sendCommand(command, opts);
	        	return result;
	        }
	        else {
	        	throw new DeviceException("Unknow device: " + deviceName, this.getDeviceNames().toString());
	        }
    	}
    }

    /**
     * Load all the devices/macros/etc associated with the name
     * 
     * @param name - the name of the room or macro set to load
     * @throws IOException
     */
    public void load(String name) throws IOException {
    	this.addAll(DeviceLoader.load(name).devices.values());
    }
}
