package it.passarella.dbcmd.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class AppConfig {

    private Properties properties;
    private BufferedReader input;
    private BufferedWriter output;

    public AppConfig() {
        
        this.input = null;
        this.output = null;
        this.properties = new Properties();
    }

    public void loadProperties(String propertyFile) {

        try { 

            this.input = new BufferedReader(new FileReader(propertyFile));

            if(this.input != null) {

                this.properties.load(this.input);
            } 
        } catch(IOException ex) {
            
            ex.printStackTrace();
            this.properties = new Properties();
        }
    }

    public String loadFile(String filename) {

        StringBuilder file = new StringBuilder("");
    
        try {
        
            this.input = new BufferedReader(new FileReader(filename));
            
            if(this.input != null) {
        
                String line = this.input.readLine();

                while(line != null) {
                    
                    file.append(line);
                    line = this.input.readLine();
                } 

                this.input.close();
            }

        } catch(IOException ex) {

            ex.printStackTrace();
        } catch (Exception ex) {
        
            ex.printStackTrace();
        }

        return file.toString();
    }

    public List<String> loadHistoryFromFile(String filename) {

        List<String> list = new ArrayList<String>();
    
        try {
    
            Files.lines(Paths.get(filename)).forEach(i -> list.add(i));
       
        } catch(IOException ex) {

            ex.printStackTrace();
        } catch (Exception ex) {
        
            ex.printStackTrace();
        }

        return list;
    }

    public void saveHistoryToFile(String filename, List<String> history) {
    
        try {
        
            this.output = new BufferedWriter(new FileWriter(filename));
            
            if(this.output != null) {
        
                history.stream()
                      .distinct()
                      .filter(i -> !i.startsWith("!"))
                      .forEach( i -> { 
                        try {

                            this.output.write(i + "\r\n");

                        } catch(Exception e) {
        
                            e.printStackTrace(); 
                        } 
                    }
                );
                this.output.flush();
                this.output.close();
            }

        } catch(IOException ex) {

            ex.printStackTrace();
        } catch (Exception ex) {
        
            ex.printStackTrace();
        }
    }

    public Properties getProperties() {
        return properties;
    }
}
