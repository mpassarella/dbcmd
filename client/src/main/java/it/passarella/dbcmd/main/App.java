package it.passarella.dbcmd.main;

import java.util.Scanner;

public class App {

    public static void main( String[] args ) { 

        StringBuilder command = new StringBuilder("");
        Scanner input = null;

        CommandDispatcher cd = null;
        
        boolean finished = false;


        if(args.length > 0) {


            if(args.length == 3) {
                
                cd = CommandDispatcher.getInstance(args[0], args[1], args[2], System.out); 
                input = new Scanner(System.in);
                
            } else if(args.length == 1) {
                
                cd = CommandDispatcher.getInstance(args[0], System.out);            
                input = new Scanner(System.in);
            } 

            while(!finished) {

                System.out.print("SQL>");
                command.append(input.nextLine());
                
                finished = cd.manage(command.toString());
                
                command = new StringBuilder("");
            }
        } else {

            usage();
        }
    }

    public static void usage() {
        
        System.out.println("DBCMD USAGE SHEET");
        System.out.println("java -jar dbcmd.jar connectionString username password");
        System.out.println("java -jar dbcmd.jar propertyFile");
    }
}
