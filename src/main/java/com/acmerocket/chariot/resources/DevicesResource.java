package com.acmerocket.chariot.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acmerocket.chariot.core.Device;
import com.acmerocket.chariot.core.DeviceManager;

@Path("/devices")
@Produces(MediaType.APPLICATION_JSON)
public class DevicesResource {
    private static final Logger LOG = LoggerFactory.getLogger(DevicesResource.class);

    private DeviceManager deviceManager; 
    
    @GET
    //@Timed(name = "get-requests")
    //@CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
    public List<Device> getDevices() {
        return this.deviceManager.getDevices();
    }
    
    @GET @Path("/{name}")
    public Device getDevice(@PathParam("name") String name) {
        return this.deviceManager.getDevice(name);
    }

    // form: /devices/someDevice?command=on
    @PUT @POST @Path("/{name}")
    public String sendCommand(@PathParam("name") String name, @QueryParam("command") String command) {
        LOG.info("Sending command: {} -> {}", name, command);
        return this.getDevice(name).sendCommand(command, null);
    }
}
