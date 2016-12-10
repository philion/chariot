package com.acmerocket.chariot.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DeviceSet {
    
    private Map<String,Device> devices = new HashMap<>();
    private Map<String,String> defaults = new HashMap<>();
    
    public synchronized void setDevices(Collection<Device> devices) {
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
