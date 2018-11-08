package it.passarella.dbcmd.main;

import java.io.PrintStream;
import it.passarella.dbcmd.integration.SQLManager;

public class CommandDispatcher {

	private static CommandDispatcher cd = null;
	private SQLManager manager = null;
	
	private CommandDispatcher(String host, String username, String password, PrintStream out) {
	
		this.manager = new SQLManager(host, username, password, out);
	}
	
	public static CommandDispatcher getInstance(String host, String username, String password, PrintStream out) {
	
		if(CommandDispatcher.cd == null){
			
			CommandDispatcher.cd = new CommandDispatcher(host, username, password, out);
		} 
		
		return CommandDispatcher.cd;
	}
	
	public boolean manage(String command) {
	
        boolean finished = false;

        if(command.contains(";")){

            manager.execute(command.replaceAll(";", ""));

        } else if(command.equalsIgnoreCase("quit")){

            finished = true;
        } else if(command.startsWith(":")){

            if(command.substring(1, 3).equalsIgnoreCase("st")) {
                
                if(command.length() > 3) {

                    manager.showTables(command.substring(3).trim());
                } else {
                    manager.showTables(null);
                } 
            }     

            if(command.substring(1, 3).equalsIgnoreCase("cs")) {
                
                if(command.length() > 3) {

                    manager.changeSchema(command.substring(3).trim());
                }   
            }     
        }

        return finished;
	}
}
