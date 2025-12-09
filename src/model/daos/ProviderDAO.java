package model.daos;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;
import model.FileConfig;
import model.Provider;

/**
 *
 * @author EmanuelHurt
 */
public class ProviderDAO {
    
    private final FileConfig fileConfig;
    
    public ProviderDAO(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }
    
    public Provider readProvider(String nameProvider) {
        
        Provider provider = new Provider();
        provider.setIdProvider(0);
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM providers WHERE name_provider=?;";
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1,nameProvider);
            
            resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()) {
                provider.setIdProvider(resultSet.getInt(1));
                provider.setName(resultSet.getString(2));
                provider.setEmail(resultSet.getString(3));
                provider.setPhone(resultSet.getInt(4));
            }
            
        }
        catch(SQLException ex) {
            System.out.println("error al obtener datos de provider");
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }
                catch(SQLException ex) {}
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }
                catch(SQLException ex) {}
            }
            if (connect != null) {
                try{
                    connect.close();
                }
                catch(SQLException ex) {}
            }
        }
        
        return provider;
    }
    
    public boolean createProvider(Provider provider) {

        boolean resp = false;
        
        String name = provider.getName();
        String email = provider.getEmail();
        int phone = provider.getPhone();
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO providers (name_provider, email, phone) VALUES (?,?,?);";
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setInt(3, phone);
            
            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                resp = true;
            }
        }
        catch(Exception ex) {
            System.out.println("error al crear nuevo usuario");
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    System.out.println("error al cerrar el preparedStatement");
                }
            }
            if (connect != null) {
                try {
                    connect.close();
                }
                catch (SQLException ex) {
                    System.out.println("error al cerrar la conexión");
                }
            }
        }
        return resp;
    }
    
    public boolean updateProvider(Provider updatedProvider) {
        
        boolean resp = false;
        
        String query = "UPDATE providers SET name_provider=?, email=?, phone=? WHERE id_provider=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setString(1, updatedProvider.getName());
            preparedStatement.setString(2, updatedProvider.getEmail());
            preparedStatement.setInt(3, updatedProvider.getPhone());
            preparedStatement.setInt(4, updatedProvider.getIdProvider());
            
            int rows = preparedStatement.executeUpdate();
            
            if (rows > 0) {
                resp = true;
            }
        }catch(SQLException ex) {
            System.out.println("ERROR AL CONECTAR EN updateProvider");
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR CERRANDO PREPAREDSTATEMENT EN updateProvider");
                }
            }
            if (connect != null) {
                try {
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR CERRANDO CONNECT EN updateProvider");
                }
            }
        }
        
        return resp;
    }
    
    public boolean deleteProvider(Provider provider) {
        boolean resp = false;
        
        String query = "DELETE FROM providers WHERE id_provider=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setInt(1, provider.getIdProvider());
            
            int rows = preparedStatement.executeUpdate();
            
            if (rows > 0) {
                resp = true;
            }
        }catch(SQLException ex) {
            System.out.println("ERROR AL CONECTAR EN updateProvider");
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR CERRANDO PREPAREDSTATEMENT EN updateProvider");
                }
            }
            if (connect != null) {
                try {
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR CERRANDO CONNECTION EN updateProvider");
                }
            }
        }
        
        return resp;
    }
    
    public List<Provider> providersList() {
        List<Provider> providersList = new ArrayList<>();
        
        Connection connect = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        String query = "SELECT * FROM providers ORDER BY name_provider;";
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
            
            int id, phone;
            String name, email;
            
            while(resultSet.next()) {
                id = resultSet.getInt(1);
                name = resultSet.getString(2);
                email = resultSet.getString(3);
                phone = resultSet.getInt(4);
                
                Provider provider = new Provider();
                provider.setIdProvider(id);
                provider.setName(name);
                provider.setEmail(email);
                provider.setPhone(phone);
                
                providersList.add(provider);
            }
        }
        catch(Exception ex) {
            System.out.println("error al obtener lista de proveedores");
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    System.out.println("error al cerrar el resultSet");
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println("error al cerrar el statement de proveedores");
                }
            }
            if (connect != null) {
                try{
                    connect.close();
                }
                catch(SQLException ex) {
                    System.out.println("error al cerrar la conexion de lista proveedores");
                }
            }
        }
        
        return providersList;
    }
}
