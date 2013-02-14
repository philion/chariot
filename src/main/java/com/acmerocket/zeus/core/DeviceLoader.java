package com.acmerocket.zeus.core;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DeviceLoader {
    
    private final ObjectMapper mapper = new ObjectMapper();
    
//    public DeviceLoader() {
//        this.mapper = initializeMapper();
//    }
//    
//    private static ObjectMapper initializeMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        
//        // load device modeule
//        //SimpleModule deviceModule = new SimpleModule("Device").addDeserializer(Device.class, new DeviceDeserializer());
//        //mapper.registerModule(deviceModule);
//
//        return mapper;
//    }

    public DeviceSet load(String deviceFile) throws IOException {
        return this.load(new File(deviceFile));
    }
    
    public DeviceSet load(File deviceFile) throws IOException {
        DeviceSet devices = this.mapper.readValue(deviceFile, DeviceSet.class);

//        for (Map<String,String> description : descriptions) {
//            String name = description.get("name");
//            
//            // load the model
//            String modelName = description.get("model");
//            Model model = Model.load(modelName);
//            
//        }
        
        return devices;
    }
    
//    private static final class DescriptionSet extends ArrayList<Map<String,String>>{
//        
//        private Map<String,DeviceDescription> devices = new HashMap<String,DeviceDescription>();
//        
//        public synchronized void setDevices(Collection<DeviceDescription> devices) {
//            for (DeviceDescription device : devices) {
//                this.devices.put(device.getName(), device);
//            }
//        }
//    }
    
//    private static class DeviceDeserializer extends JsonDeserializer<Device> {
//
//        @Override
//        public Device deserialize(JsonParser parser, DeserializationContext context) {
//            // first, need to figure out which device to create...
//            
//            //parser.get
//            
//            return null;
//        }
//    }
    
    public static void main(String[] args) throws Exception {
        DeviceLoader loader = new DeviceLoader();
        
        DeviceSet devices = loader.load("number9.json");
        System.out.println(devices);
        
        Receiver receiver = devices.reciever("denon");
        System.out.println("volume: " + receiver.volume());
        
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
