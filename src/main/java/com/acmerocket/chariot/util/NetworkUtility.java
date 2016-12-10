package com.acmerocket.chariot.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class NetworkUtility {
    private static final Logger LOG = LoggerFactory.getLogger(NetworkUtility.class);

    private NetworkUtility() {}
    
    // ? (10.10.10.1) at 0:7:e:46:ca:e on en0 ifscope [ethernet]
    private static final String ARP_PATTERN_STR = "\\? \\(([\\d\\.]+)\\) at ([\\w:]+) on .*";
    private static final Pattern ARP_PATTERN = Pattern.compile(ARP_PATTERN_STR);
    
    public static void sendWakeOnLan(String ipAddress) throws Exception {
        sendWakeOnLan(InetAddress.getByName(ipAddress));
    }
    
    public static void sendWakeOnLan(InetAddress address) {
        InetAddress broadcast = broadcastAddressFor(address);
        String macAddress = getMacAddressFrom(address);
        sendWakeOnLan(broadcast, macAddress);
    }
    
    public static void sendWakeOnLan(InetAddress broadcast, String macAddress) {
        if (broadcast == null || macAddress == null) {
            LOG.error("Invalid arguments, nothing to send: bcast={}, MAC={}", broadcast, macAddress);
            return;
        }
        
        try {
            byte[] macBytes = getMacBytes(macAddress);
            byte[] bytes = new byte[6 + 16 * macBytes.length];
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) 0xff;
            }
            for (int i = 6; i < bytes.length; i += macBytes.length) {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }
            
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, broadcast, 9); // PORT=9 ??
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();     
            
            LOG.info("Sent Wake-on-LAN packet: broadcast={}, mac={}", broadcast, macAddress);
        }
        catch (IOException e) {
            LOG.error("Failed to send Wake-on-LAN packet", e);
        }
    }
    
    private static byte[] getMacBytes(String macAddress) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macAddress.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }
    
    public static String getMacAddressFrom(InetAddress address) {
        try {
            NetworkInterface iface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()); // HACKY     
            String[] command = { "/usr/sbin/arp", "-a", "-n", "-i", iface.getName() };
            Process proc = Runtime.getRuntime().exec(command);

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            
            String line = null;
            while ((line = in.readLine()) != null) {
                
                Matcher match = ARP_PATTERN.matcher(line);
                match.find();
                String ip = match.group(1);
                String mac = match.group(2);
                
                if (address.getHostAddress().equals(ip)) {
                    return mac;
                }            
            }
            
            proc.waitFor(); 
            LOG.debug("Unable to find MAC address for {}", address);
            return null;
        }
        catch (Exception e) {
            LOG.error("Error getting MAC address for {}", address, e);
            return null;
        }
    }
    
    public static InetAddress broadcastAddressFor(InetAddress address) {
        try {
            byte[] bytes = address.getAddress();
            bytes[3] = (byte)0xff;
            return InetAddress.getByAddress(bytes);
        }
        catch (UnknownHostException e) {
            LOG.error("Unable to determine broadcast address for {}", address, e);
            return null;
        }
    }
    
    public static void main(String[] args) throws Exception {
                
        sendWakeOnLan("192.168.1.21");
        
    }
}
