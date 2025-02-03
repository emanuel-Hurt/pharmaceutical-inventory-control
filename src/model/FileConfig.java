package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author EmanuelHurt
 */
public class FileConfig {
    
    private String ip, port, ddbb, user, password;
    
    public FileConfig() {
        ip = "";
        port = "";
        ddbb = "";
        user = "";
        password = "";
        
        read();
    }
    
    private void read() {
        
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        
        try {
            
            fileReader = new FileReader("src/configConnection.txt");
            bufferedReader = new BufferedReader(fileReader);
            
            String line = "";
            int numLine = 1;
            int i;
            String data;
            
            while(line != null) {
                line = bufferedReader.readLine();
                if(line != null) {
                    
                    i = line.indexOf(" ");
                    data = line.substring(i+1);
                    
                    switch(numLine) {
                        case 1:
                            ip = data;
                            break;
                        case 2:
                            port = data;
                            break;
                        case 3:
                            ddbb = data;
                            break;
                        case 4:
                            user = data;
                            break;
                        case 5:
                            password = data;
                            break;
                    }                   
                                        
                }
                
                numLine ++;
            }
            
        } catch (IOException ex) {
            System.out.println("archivo no encontrado");
        }
        finally {
            try {
                if(bufferedReader != null) {
                    bufferedReader.close();
                }
                if(fileReader != null) {
                    fileReader.close();
                }
            }
            catch (IOException ex) {
                System.out.println("error al cerrar el archivo");
            }
        }
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getDdbb() {
        return ddbb;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
    
}
