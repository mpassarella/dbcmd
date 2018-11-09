package it.passarella.dbcmd.integration;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLManager {

    private Connection conn;
    private List<String> tables;

	public SQLManager(String host, String username, String password) {
	
        this(ConnectionFactory.create(host, username, password));
	}

    public SQLManager(Connection conn) {

        this.conn = conn;
        this.tables = new ArrayList<String>();
    }

    public void changeCatalog(String catalog) {
    
        if(this.conn != null) {
            
            try { 

                this.conn.setCatalog(catalog);

            } catch(SQLException ex) {
            
                ex.printStackTrace();
            }
        }
        
        this.loadTables(null);        
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

    public ResultSet executeQuery(String sql) {

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

    public List<String> showTables(String tablePattern) {
        
        this.loadTables(tablePattern);
        
        return this.tables;
    }

    public void closeConnection() {
        
        try {

            this.conn.close();

        } catch(SQLException ex) {
        
            ex.printStackTrace();
        }
    }
}
