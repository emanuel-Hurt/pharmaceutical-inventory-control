package model.daos;

import model.FileConfig;
import model.Password;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.DatabaseConnection;

/**
 *
 * @author EmanuelHurt
 */
public class PasswordDAO {
    
    private final FileConfig fileConfig;
    
    public PasswordDAO(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }
    
    public boolean createPassword(Password pass) {
        boolean resp = false;
        
        String query = "INSERT INTO passwords ("
                + "the_password, "
                + "id_user"
                + ") "
                + "VALUES ("
                + "?,"
                + "?"
                + ");";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, pass.getThePassword());
            preparedStatement.setInt(2, pass.getIdUser());
            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                resp = true;
            }
            
        }catch(SQLException ex) {
            System.out.println("ERROR EN PasswordDAO.createPassword()");
        }finally {
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN PasswordDAO.createPassword");
                    System.out.println(ex.getMessage());
                }
            }
            if (connect != null) {
                try{
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT EN PasswordDAO.createPassword");
                }
            }
        }
        
        return resp;
    }
    
    public boolean updatePassword(Password pass) {
        boolean resp = false;
        
        String query = "UPDATE passwords "
                + "SET "
                + "the_password=? "
                + "WHERE id_user=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1,pass.getThePassword());
            preparedStatement.setInt(2, pass.getIdUser());
            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                resp = true;
            }
            
        }catch(SQLException ex) {
            System.out.println("ERROR EN PasswordDAO.updatePassword()");
            System.out.println(ex.getMessage());
        }finally {
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN PasswordDAO.updatePassword");
                }
            }
            if (connect != null) {
                try{
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT EN PasswordDAO.updatePassword");
                }
            }
        }
        
        return resp;
    }
    
    public Password readPassword(int idPass) {
        Password password = new Password();
        
        String query = "SELECT * "
                + "FROM passwords "
                + "WHERE id_password=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, idPass);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                password.setIdPassword(resultSet.getInt(1));
                password.setThePassword(resultSet.getString(2));
                password.setIdUser(resultSet.getInt(3));
            }
            
        }catch(SQLException ex) {
            System.out.println("ERROR EN PasswordDAO.readPassword()");
            System.out.println(ex.getMessage());
        }finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }catch(SQLException ex) {}
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }catch(SQLException ex) {}
            }
            if (connect != null) {
                try {
                    connect.close();
                }catch(SQLException ex) {}
            }
        }
        
        return password;
    }
    
    public boolean deletePassword(int idPass) {
        boolean resp = false;
        
        String query = "DELETE FROM passwords WHERE id_password=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, idPass);
            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                resp = true;
            }
            
        }catch(SQLException ex) {
            System.out.println("ERROR EN PasswordDAO.deletePassword()");
            System.out.println(ex.getMessage());
        }finally {
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN PasswordDAO.updatePassword");
                }
            }
            if (connect != null) {
                try{
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT EN PasswordDAO.updatePassword");
                }
            }
        }
        
        return resp;
    }
}
