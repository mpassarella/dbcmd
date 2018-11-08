package it.passarella.dbcmd.integration;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.passarella.dbcmd.output.OutputManager;

public class SQLManager {

    private Connection conn;
    private OutputManager output;
    private List<String> tables;

	public SQLManager(String host, String username, String password, PrintStream out) {
	
        this(ConnectionFactory.create(host, username, password), out);
	}

    public SQLManager(Connection conn, PrintStream out) {

        this.conn = conn;
        this.output = new OutputManager(out);
        
        this.tables = new ArrayList<String>();
    }

    public void changeSchema(String schema) {
    
        if(this.conn != null) {
            
            try { 

                this.conn.setSchema(schema);

            } catch(SQLException ex) {
            
                ex.printStackTrace();
            }
        }
        
        this.loadTables(null);        
    }

    private void loadTables(String tablePattern) {

        ResultSet result = null;
        
        this.tables.clear();

        try {
            
            result = this.conn.getMetaData().getTables(this.conn.getCatalog(), this.conn.getSchema(), tablePattern, null);
            
            while(result.next()) {
                 
                this.tables.add(result.getString(3));
            }
            
            result.close();

        } catch (SQLException ex) {

            ex.printStackTrace();

        } finally {

            result = null;
        }
    }

    private ResultSet executeQuery(String sql) {

        ResultSet result = null; 

        try {
            
            Statement stmt = this.conn.createStatement();
            result = stmt.executeQuery(sql.replaceAll(";", ""));
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            result = null; 
        }

        return result;
    }  

    public void execute(String sql) {
        
        ResultSet result = null; 

        result = this.executeQuery(sql);

        if(result != null) {        

            this.output.printResultSet(result);
        }
    } 

    public void showTables(String tablePattern) {
        
        this.loadTables(tablePattern);
        this.output.printTableList(this.tables);
    }
}
