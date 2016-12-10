package com.acmerocket.zeus.cli;

import static java.lang.String.format;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Nonnull;

import com.acmerocket.zeus.core.Device;
import com.acmerocket.zeus.core.DeviceLoader;
import com.acmerocket.zeus.core.DeviceSet;

public class ControlShell {
    public static final String DEFAULT_PROMPT = "zeus> ";

    private static final Set<String> EXIT_COMMANDS;
    static {
        final SortedSet<String> ecmds = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        ecmds.addAll(Arrays.asList("exit", "done", "quit", "end", "fino"));
        EXIT_COMMANDS = Collections.unmodifiableSortedSet(ecmds);

        // final SortedSet<String> hcmds = new
        // TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        // hcmds.addAll(Arrays.asList("help", "helpi", "?"));
        // HELP_COMMANDS = Collections.unmodifiableSet(hcmds);

        // DATE_PATTERN = Pattern.compile("\\d{4}([-\\/])\\d{2}\\1\\d{2}"); //
        // http://regex101.com/r/xB8dR3/1
        // HELP_MESSAGE = format("Please enter some data or enter one of the
        // following commands to exit %s", EXIT_COMMANDS);
    }
    
    private String prompt = DEFAULT_PROMPT;
    private final DeviceSet devices;
    
    public ControlShell(DeviceSet arg) {
        this.devices = arg;
    }

    private void output(@Nonnull final String format, @Nonnull final Object... args) {
        System.out.print(format(format, args));
        System.out.flush();
    }
    
    public void run() {
        final Scanner sis = new Scanner(System.in);
        output(this.prompt);

        while (sis.hasNext()) {
            final String deviceName = sis.next();
            
            if (EXIT_COMMANDS.contains(deviceName)) {
                output("Exit command %s issued, exiting.\n", deviceName);
                break;
            }
            
            Device device = this.devices.get(deviceName);
            if (device != null) {
                final String command = sis.next();
                final String[] opts = sis.nextLine().trim().split("\\s+");
                
                String result = device.sendCommand(command, opts);
                //output("[%s %s](%s) => %s\n", device, command, opts, result);
                output("%s\n", result);
            }
            else {
                output("Unknown device: %s\n", deviceName);
                output("Valid options are: %s\n", this.devices.getDeviceNames());
            }
            
            output(this.prompt);
        }
        sis.close();
    }

    public static void main(final String[] args) throws IOException {
        DeviceLoader loader = new DeviceLoader();
        DeviceSet devices = loader.load("number9.json");
        ControlShell shell = new ControlShell(devices);
        
        if (args != null && args.length > 0) {
            shell.execute(args);
        }
        
        shell.run();
        
        System.exit(0);
    }

    private void execute(String[] args) throws UnsupportedEncodingException {
        String command = String.join(" ", args) + " quit\n";
        InputStream commandInput = new ByteArrayInputStream(command.getBytes("UTF-8"));
        InputStream oldInput = System.in;
        try {
            System.setIn(commandInput);
            //output("executing: %s", command);
        } 
        finally {
            System.setIn(oldInput);
        }
    }

    // (device-name|macro-name) (action) (params)
    // i.e. denon volume-up, denon power-on, appletv hulu, etc.
    // macro: (device-a power-on, device-b power-on, device-a input device-b,
    // device-a output device-c
}
