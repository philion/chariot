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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acmerocket.zeus.core.Device;
import com.acmerocket.zeus.core.DeviceLoader;
import com.acmerocket.zeus.core.DeviceSet;

public class ControlShell {
    private static final Logger LOG = LoggerFactory.getLogger(ControlShell.class);

    public static final String DEFAULT_PROMPT = "\nzeus> ";

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
        final Scanner scanner = new Scanner(System.in);
        output(this.prompt);

        while (scanner.hasNext()) {
        	
        	String input = scanner.nextLine().trim();
        	
        	// check empty
        	if (input == null || input.length() == 0) {
        		// display help
        		this.displayHelp();
        		break; // FIXME
        	}
        	
            String[] parameters = input.split("\\s+");
            String deviceName = parameters[0];
            
            if (EXIT_COMMANDS.contains(deviceName)) {
                output("Exit command %s issued, exiting.", deviceName);
                break; // FIXME
            }
            
            Device device = this.devices.get(deviceName);
            if (device != null) {
            	if (parameters.length == 1) {
	                output("Valid commands: %s", device.getCommands());
            	}
            	else {
		            String command = parameters[1];
		            String[] opts = Arrays.copyOfRange(parameters, 2, parameters.length);
		            //LOG.info("## device={}, command={}, opts={}", deviceName, command, opts);
		            
		            try {
		            	String result = device.sendCommand(command, opts);
		            	output("%s", result);
		            }
		            catch (IllegalArgumentException iae) {
		                output("Unknown command: %s, for device={}", command, deviceName);
		                output("Valid commands: %s", device.getCommands());
		                break; // FIXME
		            }
            	}
            }
            else {
                output("Unknown device: %s", deviceName);
        		this.displayHelp();
            }
            
            output(this.prompt);
        }
        scanner.close();
    }

    /**
	 * Show standard help, for the provided devices
	 */
	protected void displayHelp() {
        this.output("Valid devices are: %s", this.devices.getDeviceNames());
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
