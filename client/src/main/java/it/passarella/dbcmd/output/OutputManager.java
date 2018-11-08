package it.passarella.dbcmd.output;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OutputManager {

    private PrintStream out;
    private List<String> columns = null;

    public OutputManager(PrintStream out) {
        
        this.out = out;
        this.columns = new ArrayList<>();
    }

    public void printHistory(List<String> list) {

        for(int i=0; i < list.size(); i++) {

            this.out.println("[" + i + "] - " + list.get(i));
        }

        this.out.println("**********************************************************************************************");
    }

    public void printList(List<String> list) {

        for(String item : list) {

            this.out.println(item);
        }

        this.out.println("**********************************************************************************************");
    }

    public void printResultSet(ResultSet result) {

        this.columns.clear();
        
        try {
       
            for(int i=0; i<result.getMetaData().getColumnCount(); i++) {
                
                this.columns.add(result.getMetaData().getColumnName(i+1));
            }
            

            while(result.next()) {
            

                for(String column : this.columns) {
                    
                    this.out.println(column + " : " + result.getString(column));
                    this.out.println("----------------------------------------------------------------------------------------------");
                }

                this.out.println("**********************************************************************************************");
            }

        } catch (SQLException ex) {

        }
    } 
}
