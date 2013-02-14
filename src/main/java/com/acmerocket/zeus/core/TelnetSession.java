package com.acmerocket.zeus.core;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TelnetSession {
    private static final Logger LOG = LoggerFactory.getLogger(TelnetSession.class);

    private final InetAddress address;
    private final int port;
    
    private BufferedInputStream in;
    private PrintStream out;
    
    public TelnetSession(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public synchronized void connect() throws IOException {
        if (this.isConnected()) {
            LOG.debug("Already connected");
            return;
        }
        LOG.info("Connecting to {}", this);
        Socket socket = new Socket(address, port);
        this.out = new PrintStream(socket.getOutputStream());
        this.in = new BufferedInputStream(socket.getInputStream());
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
        
        this.out.print(cmd);
        this.out.flush();
        
        // FIXME
        byte[] buffer = new byte[1024];
        this.in.read(buffer);
        String result = new String(buffer);
        result.trim();
        
        return result;
    }
    
    public synchronized boolean isConnected() {
        return (in != null && out != null);
    }
    
    private void ensureConnected() throws IOException {
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
        return "[" + this.address.getHostName() + ":" + this.port + "]";
    }
}
