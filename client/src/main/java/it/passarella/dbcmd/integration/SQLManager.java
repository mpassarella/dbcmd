package it.passarella.dbcmd.integration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.PrintStream;

import it.passarella.dbcmd.output.OutputManager;

public class SQLManager {

    private Connection conn;
    private OutputManager output;

    public SQLManager(Connection conn, PrintStream out) {

        this.conn = conn;
        this.output = new OutputManager(out);
    }

    private ResultSet executeQuery(String sql) {

        ResultSet result = null; 

        try {
            
            Statement stmt = this.conn.createStatement();
            result = stmt.executeQuery(sql.replaceAll(";", ""));
            
        } catch (SQLException ex) {
            
            result = null; 
        }

        return result;
    }  

    public void execute(String sql) {
        
        ResultSet result = null; 

        result = this.executeQuery(sql);
        
        this.output.printResultSet(result);
    } 
}
