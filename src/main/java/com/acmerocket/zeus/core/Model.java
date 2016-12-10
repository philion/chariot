package com.acmerocket.zeus.core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;

public class Model {

    private final String name;
    private final String driverName;
    private final Map<String,String> commands;
    private final List<String> roles;
    
    public Model(String name, String driver, Map<String,String> commands, List<String> roles) {
        this.name = name;
        this.driverName = driver;
        this.commands = commands;
        this.roles = roles;
    }

    public String getDriverName() {
        return driverName;
    }

    public Map<String,String> getCommands() {
        return commands;
    }
    
    public Set<String> getCommandNames() {
    	return this.commands.keySet();
    }

    public List<String> getRoles() {
        return roles;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String toString() {
        return this.name;
    }
    
    public static Model load(String modelName) throws IOException {
        if (modelName == null || modelName.length() == 0) {
            throw new IOException("Invalid model: '" + modelName + "'");
        }
        
        String name = "devices/models/" + modelName + ".yml";
        InputStream in = Model.class.getResourceAsStream(name);
        if (in != null) {
            return load(new InputStreamReader(in));
        }
        else {
            File file = new File(System.getProperty("user.dir") + "/src/main/resources/" + name);
            if (file.isFile()) {
                return load(new FileReader(file));
            }
            else {
                throw new IOException("Unknown model: " + modelName + " (" + file.getAbsolutePath() + ")");
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public static Model load(Reader in) throws IOException {
        Yaml parser = new Yaml();
        Map<String,Object> modelMap = (Map<String, Object>)parser.load(in);
        
        String name = (String)modelMap.get("name");
        String driverName = (String)modelMap.get("driver");
        Map<String,String> commands = (Map<String, String>) modelMap.get("commands");
        
        List<String> roles = (List<String>) modelMap.get("roles");
        
        return new Model(name, driverName, commands, roles);
    }
    
    public static void main(String[] args) throws IOException {
        Model model = Model.load("denon-avr-2112ci");
        System.out.println(model);
    }
}
