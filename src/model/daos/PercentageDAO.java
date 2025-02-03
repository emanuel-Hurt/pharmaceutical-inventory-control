package model.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.DatabaseConnection;
import model.FileConfig;
import model.Percentage;
import model.Product;

/**
 *
 * @author EmanuelHurt
 */
public class PercentageDAO {
    
    private final FileConfig fileConfig;
    
    public PercentageDAO(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }
    
    public boolean createPercentage(Percentage percentage) {
        boolean resp = false;
        
        String query = "INSERT INTO percentages (sale_percentage, discount_percentage) VALUES (?,?);";
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setDouble(1, percentage.getSalePercentage());
            preparedStatement.setDouble(2, percentage.getDiscountPercentage());
            
            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                resp = true;
            }
        }catch(SQLException ex) {
            System.out.println("ERROR AL CONECTAR EN createPercentage");
        }finally {
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN createPercentage");
                }
            }
            if (connect != null) {
                try{
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT EN createPercentage");
                }
            }
        }
        
        return resp;
    }
    
    public Percentage readPercentage(int id) {
        Percentage percentage = new Percentage();
        
        String query = "SELECT * FROM percentages WHERE id_percentage=?";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1,id);
            
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                percentage.setIdPercentage(resultSet.getInt(1));
                percentage.setSalePercentage(resultSet.getDouble(2));
                percentage.setDiscountPercentage(resultSet.getDouble(3));
            }
        }
        catch(SQLException ex) {
            System.out.println("ERROR AL CONECTAR EN readPercentage");
        }
        finally {
            if (resultSet != null) {
                try{
                    resultSet.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR RESULTSET EN readPercentage");
                }
            }
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN readPercentage");
                }
            }
            if (connect != null) {
                try{
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT EN readPercentage");
                }
            }
        }
        
        return percentage;
    }
    
    public boolean updatePercentage(Percentage percentage) {
        boolean resp = false;

        String query = "UPDATE percentages SET sale_percentage=?, discount_percentage=? WHERE id_percentage=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setDouble(1, percentage.getSalePercentage());
            preparedStatement.setDouble(2, percentage.getDiscountPercentage());
            preparedStatement.setInt(3, percentage.getIdPercentage());
            
            int rows = preparedStatement.executeUpdate();
            
            if(rows > 0) {
                resp = true;
            }
            
        }
        catch(SQLException ex) {
            System.out.println("ERROR EN CONECCION EN UpdatePercentage");
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN UpdatePercentage");
                }
            }
            if (connect != null) {
                try {
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT EN UpdatePercentage");
                }
            }
        }
        
        return resp;
    }
}
