package it.passarella.dbcmd.integration; 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection create(String host, String username, String password) {

        Connection conn = null;

        try { 

            if(host.contains("mysql")) {

                Class.forName("com.mysql.cj.jdbc.Driver");
            }
 
            conn = DriverManager.getConnection(host, username, password);

        } catch (SQLException|ClassNotFoundException ex) {
           
            ex.printStackTrace(); 
            conn = null;
        }

        return conn;
    }
}
