package com.acmerocket.chariot.core;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TelnetDevice extends Device {
    private static final String LF = "\n";
    private static final int DEFAULT_PORT = 23;
    
    @JsonCreator
    public TelnetDevice(Map<String,Object> props) throws IOException {
        super(props);
        
        String address = (String)props.get("address");
        
        int port = DEFAULT_PORT;
        if (props.containsKey("port")) {
            port = (Integer)props.get("port");
        }
        String eol = LF;
        if (props.containsKey("eol")) {
            eol = (String)props.get("eol");
        }
        
        this.setDriver(new TelnetDriver(address, port, eol));        
    }
    
//    public TelnetDevice(String address, int port, String eol) throws IOException {
//        this.setDriver(new TelnetDriver(address, port, eol));        
//    }
//
//    public TelnetDevice(String address) throws IOException {
//        this(address, DEFAULT_PORT, LF);
//    }

//    public static void main(String[] args) throws Exception {
//        TelnetDevice device = null;//new TelnetDevice("192.168.1.21");
//        
//        //System.out.println("Turning on");
//        //device.turnOnDirect();
//        
//        //Thread.sleep(1 * 1000);
//        
//        //String status = device.sendCmd("PW?");
//        //System.out.println("Status: " + status);
//        
//        //Thread.sleep(10 * 1000);
//        
//        String input = device.sendCommand("SI?");
//        System.out.println("Input: " + input);
//        
//        //System.out.println("Turning off");
//        //device.turnOff();
//        
//        //Thread.sleep(2 * 1000);
//        
//        String status = device.sendCommand("PW?");
//        System.out.println("Status: " + status);
//        
//    }
}
