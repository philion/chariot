package com.acmerocket.chariot.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")  
@JsonSubTypes({  
    @Type(name="telnet", value=TelnetDevice.class),  
    @Type(name="http", value=HttpDevice.class) 
})  
public class Device {
    private static final Logger LOG = LoggerFactory.getLogger(Device.class);

    private final String name;
    private final String modelName;
    private Driver driver;
    
    private Model model;
    
    protected Device(Map<String, Object> props) {
        this.name = props.get("name").toString();
        this.modelName = props.get("model").toString();
    }

    public String getName() {
        return name;
    }
    
    public String getModelName() {
        return this.modelName;
    }
    
    public String toString() {
        return this.name + "/" + this.modelName + "/" + this.driver;
    }

    public synchronized Model getModel() {
        try {
            if (this.model == null) {
                this.model = this.loadModel(this.modelName);
            }
            return model;
        }
        catch (IOException e) {
            LOG.error("Error loading mode {}", this.modelName, e);
            return null;
        }
    }
    
    protected Model loadModel(String modelName) throws IOException {
        return Model.load(modelName);
    }
    
    public String sendCommand(String command, String[] args) {
        LOG.debug("Sending command: {}" + (args == null || args.length == 0 ? "" : "({})"), command, args);
        
        // build raw command
        // side issue: interface/API is designed for symmetric "device volume" (get) and "device volume 50" to set.
        // This implies that the existance of the param indicates a "set", but there's no way to resolve that through
        // existing "raw command".
        String rawCommand = this.getRawCommand(command);
        LOG.debug("Raw command: {}", rawCommand);
        if (rawCommand == null) {
            throw new DeviceException("Unknown command: " + command, this.getModel().getCommandNames().toString());
        }
                
        String[] rawArgs = this.processCommandArgs(args);
        
        String fullCommand = appendCommands(this.getSeparator(), rawCommand, rawArgs);
        
        try {
            LOG.info(">>> {}", fullCommand);
            String result = this.getDriver().sendRawCommand(fullCommand);

            if (result != null) {
                result = this.processResult(result);
            }
            LOG.info("<<< {}", result);
            return result;
        }
        catch (IOException e) {
            LOG.error("Error sending: {} to {}", fullCommand, this.driver, e);
            return null;
        }
    }
    
    protected char getSeparator() {
        return ' ';
    }

    protected Driver getDriver() {
        return this.driver;
    }

    protected static String appendCommands(char separator, String command, String[] args) {        
        if (args != null && args.length > 0) {
//            StringBuilder buf = new StringBuilder(command);
//
//            for (String arg : args) {
//                buf.append(separator).append(arg);
//            }
//            return buf.toString();
            return String.format(command, (Object[])args);
        }
        else {
            return command;
        }
    }
        
    protected String getRawCommand(String command) {
        return this.getModel().getCommands().get(command);
    }

    protected String[] processCommandArgs(String[] args) {
        if (args != null) {
          LOG.debug("args: {}", Arrays.asList(args));
        }
        return args;
    }
    
    protected String processResult(String result) {
        return result.trim().substring(2);
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
    
    public Collection<String> getCommands() {
    	return this.getModel().getCommandNames();
    }
}
