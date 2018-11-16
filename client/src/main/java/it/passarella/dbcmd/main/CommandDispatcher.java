package it.passarella.dbcmd.main;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import it.passarella.dbcmd.config.AppConfig;
import it.passarella.dbcmd.integration.SQLManager;
import it.passarella.dbcmd.output.OutputManager;

public class CommandDispatcher {

	private static CommandDispatcher cd = null;

	private SQLManager manager = null;
    private OutputManager output = null;
    private List<String> history = null;
    private AppConfig config = null;
	
	private CommandDispatcher(String host, String username, String password, PrintStream out) {
	
		this.manager = new SQLManager(host, username, password);
        this.output = new OutputManager(out);
        this.history = new ArrayList<String>();
        this.config = null;
	}
	
	private CommandDispatcher(String propertyFile, PrintStream out) {
	
		this.manager = null;
        this.output = new OutputManager(out);
        this.history = new ArrayList<String>();
        this.config = new AppConfig();
        this.config.loadProperties(propertyFile);
        this.history = this.config.loadHistoryFromFile(this.config.getProperties().getProperty("cmdhistory.file"));
	}

	public static CommandDispatcher getInstance(String host, String username, String password, PrintStream out) {
	
		if(CommandDispatcher.cd == null){
			
			CommandDispatcher.cd = new CommandDispatcher(host, username, password, out);
		} 
		
		return CommandDispatcher.cd;
	}

	public static CommandDispatcher getInstance(String propertyFile, PrintStream out) {
	
		if(CommandDispatcher.cd == null){
			
			CommandDispatcher.cd = new CommandDispatcher(propertyFile, out);
		} 
		
		return CommandDispatcher.cd;
	}

    public void changeDBConnection(String command) {
        
        if(command.length() > 3) {

            String dbConn = command.substring(3).trim();

            if(this.manager != null) {
                
                this.manager.closeConnection();
            }

            this.manager = new SQLManager(this.config.getProperties().getProperty(dbConn), 
                                          this.config.getProperties().getProperty(dbConn + ".username"),
                                          this.config.getProperties().getProperty(dbConn + ".password")
            ); 
        }
    } 

	public boolean manage(String command) {
	
        boolean finished = false;

        this.history.add(command);

        if(command.contains(";")){

            this.output.printResultSet(this.manager.executeQuery(command.replaceAll(";", "")));

        } else if(command.equalsIgnoreCase("quit")){

            finished = true;
            this.config.saveHistoryToFile(this.config.getProperties().getProperty("cmdhistory.file"), this.history);
            
        } else if(command.startsWith(":")){

            // SHOW TABLES
            if(command.substring(1, 3).equalsIgnoreCase("st")) {
                
                this.showTables(command);
            }     

            // CHANGE SCHEMA
            if(command.substring(1, 3).equalsIgnoreCase("cs")) {
                
                this.changeSchema(command);
            }     

            // CHANGE CATALOG
            if(command.substring(1, 3).equalsIgnoreCase("cc")) {
                
                this.changeCatalog(command);
            }     

            // SHOW COMMAND HISTORY
            if(command.substring(1, 3).equalsIgnoreCase("sh")) {
                
                this.showHistory();
            }

            // CHANGE DB CONNECTION
            if(command.substring(1, 3).equalsIgnoreCase("cd")) {
                
                this.changeDBConnection(command);
            }     
        } else if(command.startsWith("!")){
           
            this.execHistoryCommand(command.substring(1)); 
        } else if(command.startsWith("@")){
        
            this.executeSQLScript(command.substring(1));
        }

        return finished;
	}

    private void showTables(String command) {

        if(command.length() > 3) {

           this.output.printList(this.manager.showTables(command.substring(3).trim()));
        } else {
            this.output.printList(this.manager.showTables(null));
        } 
    }

    private void changeSchema(String command) {

        if(command.length() > 3) {

            this.manager.changeSchema(command.substring(3).trim());
        }   
    }

    private void changeCatalog(String command) {

        if(command.length() > 3) {

            this.manager.changeCatalog(command.substring(3).trim());
        }   
    }

    private void showHistory() {

        this.output.printHistory(this.history);
    }
    
    private void execHistoryCommand(String value) {

        int historyIndex = 0; 
             
        try {

            historyIndex = Integer.valueOf(value);
            this.manage(this.history.get(historyIndex));

        } catch(NumberFormatException ex) {
            
        }
    }

    private void executeSQLScript(String fileName) {
        
        this.manager.executeCall(this.config.loadFile(fileName));
    }
}
