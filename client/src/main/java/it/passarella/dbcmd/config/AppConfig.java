package it.passarella.dbcmd.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private Properties properties;
    private InputStream input;

    public AppConfig() {
        
        this.input = null;
        this.properties = new Properties();
    }

    public void loadProperties(String propertyFile) {

        try { 

            this.input = new FileInputStream(propertyFile);

            if(this.input != null) {

                this.properties.load(this.input);
            } 
        } catch(IOException ex) {
            
            ex.printStackTrace();
            this.properties = new Properties();
        }
    }

    public Properties getProperties() {
        return properties;
    }
}
