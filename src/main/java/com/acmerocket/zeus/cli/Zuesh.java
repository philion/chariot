package com.acmerocket.zeus.cli;

import java.io.PrintWriter;
import java.util.Scanner;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class Zuesh implements Runnable {
    //private static final Logger LOG = LoggerFactory.getLogger(Zuesh.class);

    public static final String DEFAULT_PROMPT = "zeus> ";
    
    //private final Console console = System.console();
    private String prompt = DEFAULT_PROMPT;
    
    private final Scanner scanner = new Scanner(System.in);
    private final PrintWriter out = new PrintWriter(System.out);
    
    //private final Terminal terminal;

    public Zuesh() {
        //this.terminal = TerminalBuilder.builder().system(true).build();
        //LOG.debug("Console: {}", this.console);
        //LOG.debug("Creating. in={}, out={}", scanner, out);
    }
    
    @Override
    public void run() {
        //LOG.debug("starting");
        this.out.println("starting...");
        this.out.println(this.prompt);
        
        while(scanner.hasNext()) {
            final String next = scanner.next();
            if ("quit".equalsIgnoreCase(next)) {
                this.out.println("quitting.");
                break;
            }
            else { 
                this.out.format("You entered an unclassified String = %s", next); 
            }
            
//            String input = this.scanner.nextLine();
//            if (input != null) {
//                this.out.println("... " + input);
//            }
//            else {
//                this.out.println("quitting...");
//                break;
//            }
//            this.out.println(this.prompt);
        }
        this.scanner.close();
        System.exit(0);
    }
    
    public static void main(String[] args) {
      Thread t = new Thread(new Zuesh());
      t.start();

        
//        try {
//            Zuesh shell = new Zuesh();
//            if (args != null) {
//                for (String line : args) {
//                    shell.input(line);
//                }
//            }        }
//        catch (IOException e) {
//            LOG.error("Unable to start zeush", e);
//        }
    }
}
