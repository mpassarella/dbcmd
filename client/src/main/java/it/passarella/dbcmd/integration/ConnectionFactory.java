package it.passarella.dbcmd.integration; 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection create(String host, String username, String password) throws SQLException {
        
        return DriverManager.getConnection(host, username, password);
    }
}
