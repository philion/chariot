package com.acmerocket.zeus.core;

import java.io.IOException;

public interface Loader {
    
    public void load(DeviceSet devices, String loadInfo) throws IOException;

}
