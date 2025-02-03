package model.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.DatabaseConnection;
import model.FileConfig;
import model.Product;
import model.TimeNotification;

/**
 *
 * @author EmanuelHurt
 */
public class TimeNotificationDAO {
    
    private final FileConfig fileConfig;
    
    public TimeNotificationDAO(FileConfig file) {
        fileConfig = file;
    }
    
    public TimeNotification readTimeNotification(int id) {
    
        TimeNotification time = new TimeNotification();
        
        String query = "SELECT * FROM times_notifications WHERE id_time_notification=?";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1,id);
            
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                time.setIdNotification(resultSet.getInt(1));
                time.setQuantityToNotify(resultSet.getInt(2));
            }
        }
        catch(SQLException ex) {
            System.out.println("ERROR AL CONECTAR EN TimeNotificationDAO");
        }
        finally {
            if (resultSet != null) {
                try{
                    resultSet.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR RESULTSET EN TimeNotificationDAO");
                }
            }
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN TimeNotificationDAO");
                }
            }
            if (connect != null) {
                try{
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT EN TimeNotificationDAO");
                }
            }
        }
        
        return time;
    }
    
    public boolean updateTimeNotification(int id, int weeks) {
        
        boolean resp = false;
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        String query = "UPDATE times_notifications "
                + "SET quantity_to_notify=? "
                + "WHERE id_time_notification=?;";
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setInt(1, weeks);
            preparedStatement.setInt(2, id);
            
            int rows = preparedStatement.executeUpdate();
            
            if(rows > 0) {
                resp = true;
            }
            
        }
        catch(SQLException e) {
            System.out.println("ERROR EN UPDATE TimeNotification");
            System.out.println(e.getMessage());
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN UPDATE TimeNotification");
                }
            }
            if (connect != null) {
                try {
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT EN UPDATE TimeNotification");
                }
            }
        }
        
        return resp;
    }
}
