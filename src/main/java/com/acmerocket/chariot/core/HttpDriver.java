package com.acmerocket.chariot.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpDriver implements Driver {
    
    @Override
    public String sendRawCommand(String command) {
        try {
            URL commandUri = new URL(command);
            URLConnection conn = commandUri.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            StringBuffer result = new StringBuffer();
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine).append('\n');
            }
            in.close();
            return result.toString();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
