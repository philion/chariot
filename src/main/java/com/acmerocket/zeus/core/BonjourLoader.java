package com.acmerocket.zeus.core;

import java.net.InetAddress;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import javax.jmdns.ServiceTypeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BonjourLoader {
    private static final Logger LOG = LoggerFactory.getLogger(BonjourLoader.class);
    
    public DeviceSet load(String loadInfo) {
        
        return null;
    }

    private static class SampleListener implements ServiceListener, ServiceTypeListener {
        @Override
        public void serviceAdded(ServiceEvent event) {
            //LOG.debug("Service added: " + event.getInfo());
        }

        @Override
        public void serviceRemoved(ServiceEvent event) {
            //LOG.debug("Service removed: " + event.getInfo());
        }

        @Override
        public void serviceResolved(ServiceEvent event) {
            LOG.debug("Service resolved: " + event.getInfo());
            
            LOG.debug("Event: " + event);
            LOG.debug("Name: " + event.getName());
            LOG.debug("Host:port: " + event.getInfo().getInet4Addresses()[0] + ":" + event.getInfo().getPort());

        }

        @Override
        public void serviceTypeAdded(ServiceEvent event) {
            //LOG.debug("TYPE: " + event.getType());            
        }

        @Override
        public void subTypeForServiceTypeAdded(ServiceEvent event) {
            //LOG.debug("SUBTYPE: " + event.getType());            
        }
    }

    public static void main(String[] args) throws Exception {
        // Create a JmDNS instance
        JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());

        // Add a service listener
        SampleListener listener = new SampleListener();
        //jmdns.addServiceTypeListener(listener);
        jmdns.addServiceListener("_mediaremotetv._tcp.local.", listener);
        
        // Wait a bit
        Thread.sleep(10000);
        
        LOG.info("Exiting.");
    }
}
