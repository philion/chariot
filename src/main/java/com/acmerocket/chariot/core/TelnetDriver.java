package com.acmerocket.chariot.core;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acmerocket.chariot.util.NetworkUtility;

public class TelnetDriver implements Driver {
    private static final Logger LOG = LoggerFactory.getLogger(TelnetDriver.class);
    
    public static final int DEFAULT_PORT = 23;
    
    private final InetAddress address;
    private final int port;
    private final String eol;
    
    private BufferedInputStream in;
    private PrintStream out;
    
    public TelnetDriver(InetAddress address, int port, String eol) {
        this.address = address;
        this.port = port;
        this.eol = eol;
    }
    
    public TelnetDriver(InetAddress address, int port) {
        this(address, port, "\n");
    }
    
    public TelnetDriver(String address, int port, String eol) throws UnknownHostException {
        this(InetAddress.getByName(address), port, eol);
    }
        
    public TelnetDriver(String address, int port) throws UnknownHostException {
        this(InetAddress.getByName(address), port);
    }
    
    public TelnetDriver(InetAddress address) {
        this(address, DEFAULT_PORT);
    }
    
    public TelnetDriver(String address) throws UnknownHostException {
        this(address, DEFAULT_PORT);
    }
    
    /**
     * Send a Wake-on-LAN packet to this device
     */
    public void wakeUp() {
        NetworkUtility.sendWakeOnLan(this.getAddress());
    }

    public synchronized void connect() throws IOException {
        if (this.isConnected()) {
            LOG.trace("Already connected");
            return;
        }
        Socket socket = new Socket(address, port);
        this.out = new PrintStream(socket.getOutputStream());
        this.in = new BufferedInputStream(socket.getInputStream());
        LOG.debug("Connected to {}", this);
    }
    
    public synchronized void disconnect() throws IOException {
        this.in.close();
        this.in = null;
        
        this.out.close();
        this.out = null;
    }
    
    public synchronized void reconnect() throws IOException {
        this.disconnect();
        
        try { 
            Thread.sleep(500); 
        } catch (InterruptedException e) {}
        
        this.connect();
    }
    
    public synchronized String sendRawCommand(String cmd) throws IOException {
        this.ensureConnected();
        
        this.out.print(cmd + this.eol);
        this.out.flush();
        
        // FIXME
        byte[] buffer = new byte[2048];
        this.in.read(buffer);
        String result = new String(buffer);
        result.trim();
        
        return result;
    }
    
    public synchronized boolean isConnected() {
        return (in != null && out != null);
    }
    
    private synchronized void ensureConnected() throws IOException {
        // NOTE: On connection IO errors, the session should be disconnected by calling this.disconnect();
        if (! this.isConnected()) {
            this.connect();
        }
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
    
    public String toString() {
        return this.address.getHostName() + ":" + this.port;
    }
}
