package model.daos;

import model.Client;
import model.FileConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.DatabaseConnection;

/**
 *
 * @author EmanuelHurt
 */
public class ClientDAO {
    
    private final FileConfig fileConfig;
    
    public ClientDAO(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }
    
    //ESTE METODO ES LLAMADO AL MOMENTO DE REGISTRAR LA VENTA
    public void registerClient(Client client) {
        
        Client theClient = readClient(client.getCodClient());
        if (theClient.getCodClient() == null) {
            createClient(client);
        }
        
    }
    
    public boolean createClient(Client client) {
        boolean resp = false;
        
        String query = "INSERT INTO client (cod_client, name_client) VALUES (?,?);";
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, client.getCodClient());
            preparedStatement.setString(2, client.getName());
            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                resp = true;
            }
        }catch(SQLException ex) {
            System.out.println("ERROR AL CONECTAR EN createClient");
        }finally {
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN readClient");
                }
            }
            if (connect != null) {
                try{
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT EN readClient");
                }
            }
        }
        
        return resp;
    }
    
    public Client readClient(String cod) {
        Client client = new Client();
        
        String query = "SELECT * FROM client WHERE cod_client=?";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, cod);
            
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                client.setCodClient(resultSet.getString(1));
                client.setName(resultSet.getString(2));
            }
        }catch(SQLException ex) {
            System.out.println("ERROR AL CONECTAR EN readClient" + ex.getMessage());
        }finally {
            if (resultSet != null) {
                try{
                    resultSet.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR RESULTSET EN readClient");
                }
            }
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN readClient");
                }
            }
            if (connect != null) {
                try{
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT EN readClient");
                }
            }
        }
        
        return client;
    }
}
