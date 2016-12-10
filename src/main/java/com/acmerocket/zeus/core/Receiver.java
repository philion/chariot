package com.acmerocket.zeus.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public interface Receiver {
    public Receiver powerOn();
    public Receiver powerOff();
    public Receiver setInput(String input);
    public Receiver volumeUp();
    public Receiver volumeDown();
    
    public Receiver setVolume(String value);
    public Receiver setName(String name);

    public String name();
    public String volume(); 
    public String input();
    public String status();
    
    public static final class Factory {
        public static Receiver wrap(Device device) {
            InvocationHandler handler = new ReceiverHandler(device);
            return (Receiver)Proxy.newProxyInstance(Receiver.class.getClassLoader(), 
                    new Class[] { Receiver.class }, handler);            
        }
    }
    
    public static final class ReceiverHandler implements InvocationHandler {
        private final Device delegate;
        private ReceiverHandler(Device device) { this.delegate = device; }
        @Override
        public Object invoke(Object proxy, Method method, Object[] objArgs) throws Throwable {
            //System.out.println(">> " + method.getName() + (objArgs != null ? Arrays.asList(objArgs) : "[]"));
            
            String command = method.getName();
//            // strip 'set'
//            if (command.startsWith("set")) {
//                command = command.substring(3);
//                command = StringUtils.uncapitalize(command);
//            }
            
            // process capitol chars
            StringBuffer cmdBuffer = new StringBuffer();
            for (char ch : command.toCharArray()) {
                if (Character.isUpperCase(ch)) {
                    cmdBuffer.append('-').append(Character.toLowerCase(ch));
                }
                else {
                    cmdBuffer.append(ch);
                }
            }
            command = cmdBuffer.toString();
            
            // build args
            String[] args = null;
            if (objArgs != null && objArgs.length > 0) {
                args = new String[objArgs.length];
                for (int i = 0; i < objArgs.length; i++) {
                    args[i] = objArgs[i].toString();
                }
            }
            
            //System.out.println("<< " + command + (objArgs != null ? Arrays.asList(args) : "[]"));
            
            String response = this.delegate.sendCommand(command, args);
            
            // return either a string or a Receiver
            Class<?> rtnType = method.getReturnType();
            if (rtnType.isAssignableFrom(Receiver.class)) {
                return proxy;
            }
            else {
                return response;
            }            
        }
    }
    // NOTE: Volume value for Denon is 0-based, 1 off from the 1-based display. Increments in 0.5% total volume.
}
