package model;


/**
 *
 * @author EmanuelHurt
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {   
    
    public static Connection getConnection(FileConfig fileConfig) {
        
        String ip = fileConfig.getIp();
        String port = fileConfig.getPort();
        String ddbb = fileConfig.getDdbb();
        
        String url = "jdbc:mysql://"+ip+":"+port+"/"+ddbb+"?useSSL=false";
        //String url = "jdbc:mysql://localhost:8889/ddbb_farma?useSSL=false";
        String user = fileConfig.getUser();
        String password = fileConfig.getPassword();
        
        Connection connection = null;
        
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            System.out.println("error en la conexion");
            System.out.println(url);
        }
        return connection;
    }
}
