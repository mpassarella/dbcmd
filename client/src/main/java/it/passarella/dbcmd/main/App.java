package it.passarella.dbcmd.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import it.passarella.dbcmd.integration.ConnectionFactory;
import it.passarella.dbcmd.integration.SQLManager;

public class App {

    public static void main( String[] args ) { 

        StringBuilder sql = new StringBuilder("");
        Scanner input = null;

        Connection conn = null;
        SQLManager manager = null;

        boolean finished = false;
     
        if(args.length == 3) {
            
            try {

                conn = ConnectionFactory.create(args[0], args[1], args[2]);
                manager = new SQLManager(conn, System.out);
                input = new Scanner(System.in);
                
                while(!finished) {

                    System.out.print("SQL>");
                    sql.append(input.nextLine());
                    
                    if(sql.toString().contains(";")){
                        
                        manager.execute(sql.toString().replaceAll(";", ""));
                        
                        sql = new StringBuilder("");

                    } else if(sql.toString().equalsIgnoreCase("quit")){
                        
                        finished = true;
                    }  
                }
            } catch(SQLException ex) {
                
                ex.printStackTrace();
            }
            
        } else {
            
            usage();
        }
    }

    public static void usage() {
        
        System.out.println("DBCMD USAGE SHEET");
        System.out.println("java -jar dbcmd.jar connectionString username password");
    }
}
