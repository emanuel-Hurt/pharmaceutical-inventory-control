package model.daos;

import model.FileConfig;
import model.User;
import model.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author EmanuelHurt
 */
public class UserDAO {
    
    private final FileConfig fileConfig;
    
    public UserDAO(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }
    
    public int createUser(User user) {
        
        int idUser = -1;
        
        String query = "INSERT INTO users "
                + "("
                + "name, "
                + "last_name, "
                + "m_last_name, "
                + "cell_phone, "
                + "address, "
                + "password, "
                + "id_rol, "
                + "username"
                + ") "
                + "VALUES (?,?,?,?,?,?,?,?);";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3,user.getmLastName());
            preparedStatement.setInt(4, user.getCellPhone());
            preparedStatement.setString(5, user.getAddress());
            preparedStatement.setString(6, user.getPassword());
            preparedStatement.setInt(7, user.getIdRol());
            preparedStatement.setString(8, user.getUsername());
            
            int rows = preparedStatement.executeUpdate();
            
            if (rows > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    idUser = resultSet.getInt(1);
                }
            }
            
        }
        catch(SQLException ex) {
            System.out.println("ERROR AL CONECTAR USERDAO");
            System.out.println(ex.getMessage());
        }       
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }catch (SQLException ex) {}
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT USERDAO");
                }
            }
            if (connect != null) {
                try {
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT USERDAO");
                }
            }
        }
        
        return idUser;
    }
    
    public User readUser(String username) {
        
        User user = new User();
        
        String query = "SELECT * FROM users WHERE username=?";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);          
            preparedStatement.setString(1, username);
            
            resultSet = preparedStatement.executeQuery();                      
            
            if (resultSet.next()) {
                
                user.setIdUser(resultSet.getInt(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setmLastName(resultSet.getString(4));
                user.setCellPhone(resultSet.getInt(5));
                user.setAddress(resultSet.getString(6));
                user.setPassword(resultSet.getString(7));
                user.setIdRol(resultSet.getInt(8));
                user.setUsername(resultSet.getString(9));
            }           
            
        }
        catch(SQLException ex) {
            System.out.println("ERROR EN UserDAO.readUser()");
            System.out.println(ex.getMessage());
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }
                catch(Exception ex) {System.out.println("error al cerrar resultSet user");}
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }catch(Exception ex) {System.out.println("error al cerrar preparedStatement user");}
            }
            if (connect != null) {
                try {
                    connect.close();
                }
                catch(Exception ex) {System.out.println("error al cerrar conexión user");}
            }
        }
              
        return user;
    }
    
    public boolean updateUser(User user, String username) {
        
        boolean resp = false;
        
        String query = "UPDATE users "
                + "SET "
                + "name=?, "
                + "last_name=?, "
                + "m_last_name=?, "
                + "cell_phone=?, "
                + "address=?, "
                + "password=?, "
                + "id_rol=?, "
                + "username=? "
                + "WHERE username=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setString(1,user.getName());           
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getmLastName());
            preparedStatement.setInt(4, user.getCellPhone());
            preparedStatement.setString(5, user.getAddress());
            preparedStatement.setString(6, user.getPassword());
            preparedStatement.setInt(7, user.getIdRol());
            preparedStatement.setString(8, user.getUsername());
            preparedStatement.setString(9, username);
            
            int rows = preparedStatement.executeUpdate();
            
            if (rows > 0) {
                resp = true;
            }
            
        }
        catch(SQLException ex) {
            System.out.println("ERROR AL CONECTAR EN UPDATEUSER");
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN UPDATEUSER");
                }
            }
            if (connect != null) {
                try {
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT EN UPDATEUSER");
                }
            }
        }
        
        return resp;
    }
    
    public boolean deleteUser(String username) {
        boolean resp = false;
        
        String query = "DELETE FROM users WHERE username=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setString(1, username);
            
            int rows = preparedStatement.executeUpdate();
            
            if (rows > 0) {
                resp = true;
            }
            
        }catch(SQLException ex) {
            System.out.println("ERROR DE CONEXION EN UserDao.delete");
            System.out.println(ex.getMessage());
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN DELETE USER");
                }
            }
            if (connect != null) {
                try {
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT EN DELETE USER");
                }
            }
        }
        
        return resp;
    }
    
    public String verifyUserLogin(String userName) {
        
        String password = "";
        
        String query = "SELECT password FROM users WHERE username=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, userName);
            
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                password = resultSet.getString(1);
            }                    
            
        }
        catch(SQLException ex) {
            System.out.println("ERROR EN UserDAO.verifyUserLogin()");
            System.out.println(ex.getMessage());
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR RESULTSET EN searchedUserByUserName");
                }
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
    
    public List<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        
        String query = "SELECT "
                + "users.id_user, "
                + "name, "
                + "last_name, "
                + "m_last_name, "
                + "cell_phone, "
                + "address, "
                + "the_password, "
                + "id_rol, "
                + "username "
                + "FROM users, passwords "
                + "WHERE users.id_user=passwords.id_user;";
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                User user = new User();
                user.setIdUser(resultSet.getInt(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setmLastName(resultSet.getString(4));
                user.setCellPhone(resultSet.getInt(5));
                user.setAddress(resultSet.getString(6));
                user.setPassword(resultSet.getString(7));
                user.setIdRol(resultSet.getInt(8));
                user.setUsername(resultSet.getString(9));
                
                users.add(user);
            }
            
        }catch (SQLException ex){
            System.out.println("ERROR EN UserDAO.getUsers()");
            System.out.println(ex.getMessage());
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }catch(SQLException ex) {
                    System.out.println();
                }
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
        
        return users;
    }
}
