package it.passarella.dbcmd.main;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import it.passarella.dbcmd.integration.SQLManager;
import it.passarella.dbcmd.output.OutputManager;

public class CommandDispatcher {

	private static CommandDispatcher cd = null;
	private SQLManager manager = null;
    private OutputManager output = null;
    private List<String> history = null;
	
	private CommandDispatcher(String host, String username, String password, PrintStream out) {
	
		this.manager = new SQLManager(host, username, password);
        this.output = new OutputManager(out);
        this.history = new ArrayList<String>();
	}
	
	public static CommandDispatcher getInstance(String host, String username, String password, PrintStream out) {
	
		if(CommandDispatcher.cd == null){
			
			CommandDispatcher.cd = new CommandDispatcher(host, username, password, out);
		} 
		
		return CommandDispatcher.cd;
	}
	
	public boolean manage(String command) {
	
        boolean finished = false;

        this.history.add(command);

        if(command.contains(";")){

            this.output.printResultSet(this.manager.executeQuery(command.replaceAll(";", "")));

        } else if(command.equalsIgnoreCase("quit")){

            finished = true;
        } else if(command.startsWith(":")){

            if(command.substring(1, 3).equalsIgnoreCase("st")) {
                
                this.showTables(command);
            }     

            if(command.substring(1, 3).equalsIgnoreCase("cs")) {
                
                this.changeSchema(command);
            }     

            if(command.substring(1, 3).equalsIgnoreCase("sh")) {
                
                this.showHistory();
            }
        } else if(command.startsWith("!")){
           
            this.execHistoryCommand(command.substring(1)); 
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
}
