package com.acmerocket.zeus.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DeviceSet {
    
    private Map<String,Device> devices = new HashMap<String,Device>();
    
    public synchronized void setDevices(Collection<Device> devices) {
        for (Device device : devices) {
            this.devices.put(device.getName(), device);
        }
    }
    
    public Device get(String name) {
        return this.devices.get(name);
    }
    
    public String toString() {
        return this.devices.toString();
    }
    
    public Receiver reciever(String name) {
        Device device = this.get(name);
        return Receiver.Factory.wrap(device);
    }

    public String getDeviceNames() {
        return this.devices.keySet().toString();
    }
}
