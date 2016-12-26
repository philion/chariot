package com.acmerocket.chariot.cli;

import static java.lang.String.format;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Nonnull;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acmerocket.chariot.core.DeviceException;
import com.acmerocket.chariot.core.DeviceSet;

public class ChariotShell implements Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(ChariotShell.class);

    public static final String DEFAULT_PROMPT = "\nchariot> ";

    private static final Set<String> EXIT_COMMANDS;
    static {
        final SortedSet<String> ecmds = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        ecmds.addAll(Arrays.asList("exit", "done", "quit", "end", "fino"));
        EXIT_COMMANDS = Collections.unmodifiableSortedSet(ecmds);
    }
    
    private String prompt = DEFAULT_PROMPT;
    
    private final PrintWriter out;
    private final LineReader reader;

    private final DeviceSet devices = new DeviceSet(); // empty
    
    public ChariotShell() throws IOException {        
        Terminal terminal = TerminalBuilder.terminal();
        
         this.reader = LineReaderBuilder.builder()
        		.terminal(terminal)
                //.completer(new StringsCompleter(this.devices.getDeviceNames(), this.devices.getCommands()))
                .build();
         
        this.out = new PrintWriter(terminal.output());
    }

    private void output(@Nonnull String format, @Nonnull Object... args) {
        this.out.print(format(format, args));
        this.out.flush();
    }
    
    protected void handleInput(final String input) throws EndOfFileException {
        String[] parameters = input.split("\\s+");
        String deviceName = parameters[0];

        // TODO: More flexible command structure, with each device it's own "command"
        // TODO: Stnadard ways to generate grammers/intentions from config
        if (EXIT_COMMANDS.contains(deviceName)) {
        	output("%s: exiting.\n", deviceName);
        	throw new EndOfFileException(deviceName);
        }
        else if (parameters.length == 1) { // assume device name only
    		try {
    			output("commands: %s", this.devices.getCommands(deviceName));
    		}
    		catch (DeviceException ex) {
            	output("%s\n", ex.getMessage());
            	output("valid: %s", ex.getValidInput());
    		}
    	}
    	else {
            String command = parameters[1];
            String[] opts = Arrays.copyOfRange(parameters, 2, parameters.length);
            LOG.debug("device={}, command={}, opts={}", deviceName, command, opts);
            
            try {
            	String result = this.devices.sendCommand(deviceName, command, opts);
            	output("%s %s: %s", deviceName, command, result);
            }
            catch (DeviceException ex) {
            	output("%s\n", ex.getMessage());
            	output("Valid input: %s", ex.getValidInput());
            }
        }
    }
    
    public void run() {
        while (true) {
            try {
                String input = this.reader.readLine(this.prompt);
            	if (input == null || input.length() == 0) {
            		this.printHelp();
            	}
            	else {
            		// handle the input
            		this.handleInput(input);
            	}
            } 
            catch (UserInterruptException e) { /* ignore */ } 
            catch (EndOfFileException e) {
            	// all done
                break;
            }
        }
    }

    /**
	 * Show standard help, for the provided devices
	 */
	protected void printHelp() {
        this.output("Valid devices are: %s", this.devices.getDeviceNames());
	}

	public static void main(String[] args) throws IOException {
        ChariotShell shell = new ChariotShell();
        
        if (args != null && args.length > 0) {
        	shell.handleInput(String.join(" ", args));
        }
        else {
        	shell.run();
        }        
        shell.close();
        System.exit(0);
    }

	@Override
	public void close() throws IOException {
		this.out.close();
	}
}
