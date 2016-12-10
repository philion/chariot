package com.acmerocket.chariot.cli;

import static java.lang.String.format;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Nonnull;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.ParsedLine;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acmerocket.chariot.core.DeviceException;
import com.acmerocket.chariot.core.DeviceLoader;
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
    
    //private final Terminal terminal;
    private final PrintWriter out;
    //private final Scanner scanner;
    private final LineReader reader;

    private final DeviceSet devices;
    
    public ChariotShell(DeviceSet arg) throws IOException {
        this.devices = arg;
        
        Terminal terminal = TerminalBuilder.terminal();
        
         this.reader = LineReaderBuilder.builder()
        		.terminal(terminal)
                .completer(new DeviceCompleter())
                .build();

        this.out = new PrintWriter(terminal.output());
        //this.scanner = new Scanner(terminal.input());
    }

    private void output(@Nonnull String format, @Nonnull Object... args) {
        this.out.print(format(format, args));
        this.out.flush();
    }
    
    public void run() {
        //while (this.scanner.hasNext()) {
        while (true) {
            String input = null;
            try {
                input = this.reader.readLine(this.prompt);
            } 
            catch (UserInterruptException e) { /* ignore */ } 
            catch (EndOfFileException e) {
                return;
            }
            
        	// check empty
        	if (input == null || input.length() == 0) {
        		// display help
        		this.displayHelp();
        		continue; // FIXME
        	}
        	
            String[] parameters = input.split("\\s+");
            String deviceName = parameters[0];
            
            if (EXIT_COMMANDS.contains(deviceName)) {
                //output("Exit command %s issued, exiting.", deviceName);
            	LOG.info("Exiting.");
            	return;
            }
            
        	if (parameters.length == 1) {
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
	            //LOG.info("## device={}, command={}, opts={}", deviceName, command, opts);
	            
	            try {
	            	String result = this.devices.sendCommand(deviceName, command, opts);
	            	output("%s %s: %s", deviceName, command, result);
	            }
	            catch (DeviceException ex) {
	            	output("%s\n", ex.getMessage());
	            	output("Valid input: %s", ex.getValidInput());
	                continue; // FIXME
	            }
            }            
        }
    }

    /**
	 * Show standard help, for the provided devices
	 */
	protected void displayHelp() {
        this.output("Valid devices are: %s", this.devices.getDeviceNames());
	}

	public static void main(String[] args) throws IOException {
		String toLoad = "living-room";
		if (args != null && args.length > 0) {
			toLoad = args[0];
		}
		
        DeviceLoader loader = new DeviceLoader();
        DeviceSet devices = loader.load(toLoad);
        
        ChariotShell shell = new ChariotShell(devices);        
        shell.run();
        shell.close();
        
        System.exit(0);
    }

	/* (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		this.out.close();
		//this.reader.close();
	}

//    private void execute(String[] args) throws UnsupportedEncodingException {
//        String command = String.join(" ", args) + " quit\n";
//        InputStream commandInput = new ByteArrayInputStream(command.getBytes("UTF-8"));
//        InputStream oldInput = System.in;
//        try {
//            System.setIn(commandInput);
//            //output("executing: %s", command);
//        } 
//        finally {
//            System.setIn(oldInput);
//        }
//    }

    // (device-name|macro-name) (action) (params)
    // i.e. denon volume-up, denon power-on, appletv hulu, etc.
    // macro: (device-a power-on, device-b power-on, device-a input device-b,
    // device-a output device-c
	
	private static class DeviceCompleter implements Completer {
		@Override
		public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
			// TODO Auto-generated method stub
			
		}
	}
}
