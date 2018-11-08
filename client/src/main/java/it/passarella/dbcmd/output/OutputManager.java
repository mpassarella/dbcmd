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

    public void printTableList(List<String> tables) {


        for(String table : tables) {

            this.out.println(table);
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
