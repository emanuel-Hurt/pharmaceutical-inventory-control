package model.daos;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;
import model.FileConfig;
import model.Purchase;
/**
 *
 * @author EmanuelHurt
 */
public class PurchaseDAO {
    
    private final FileConfig fileConfig;
    
    public PurchaseDAO(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }
    
    public boolean createPurchase(Purchase purchase) {
        
        boolean resp = false;
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO purchase "
                + "(quantity, cost, id_product, purchase_date, detail) "
                + "VALUES (?,?,?,?,?);";
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setInt(1, purchase.getQuantity());
            preparedStatement.setDouble(2, purchase.getCost());
            preparedStatement.setInt(3, purchase.getIdProduct());
            preparedStatement.setDate(4, purchase.getPurchaseDate());
            preparedStatement.setString(5, purchase.getDetail());
            
            int numRows = preparedStatement.executeUpdate();
            
            if (numRows > 0) {
                resp = true;
            }
        }
        catch(SQLException ex) {
            System.out.println("ERROR AL CONECTAR CON LA BBDD EN createPurchase");
            System.out.println(ex.getMessage());
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }
                catch(SQLException ex) {
                    System.out.println("error al cerrar el preparedStatement de purchase");
                }
            }
            if (connect != null) {
                try {
                    connect.close();
                }
                catch(SQLException ex) {
                    System.out.println("error al cerrar connection de purchase");
                }
            }
        }
        
        return resp;
    }
    
    //RECIBE EL ID DEL PRODUCTO PARA LOCALIZAR LA COMPRA RESPECTIVA
    public Purchase readPurchase(int idProd) {
        Purchase purchase = new Purchase();
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        String query = "SELECT * FROM purchase WHERE id_product=?;";
        ResultSet resultSet = null;
        
        try {
            
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1,idProd);
            
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                purchase.setIdPurchase(resultSet.getInt(0));
                purchase.setQuantity(resultSet.getInt(1));
                purchase.setCost(resultSet.getDouble(2));
                purchase.setIdProduct(resultSet.getInt(3));
                purchase.setPurchaseDate(resultSet.getDate(4));
                purchase.setDetail(resultSet.getString(5));
            }
            
        }catch(SQLException ex) {
            System.out.println("FALLO AL CONECTAR CON LA BBDD en PurchaseDAO.readPurchase");
            System.out.println(ex.getMessage());
        }finally {
            if (resultSet != null) {
                try{
                    resultSet.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR RESULTSET EN readPurchase");
                }
            }
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN readPurchase");
                }
            }
            if (connect != null) {
                try{
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT EN readPurchase");
                }
            }
        }
        
        return purchase;
    }
    
    public boolean deletePurchase(int idPurchase) {
        boolean resp = false;
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        String query = "DELETE FROM purchase WHERE id_purchase=?;";
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, idPurchase);
            
            int rows = preparedStatement.executeUpdate();
            
            if (rows > 0) {
                resp = true;
            }
            
        }catch(SQLException ex) {
            System.out.println("ERROR DE CONEXION EN Purchase.deletePurchase");
            System.out.println(ex.getMessage());
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN Purchase.deletePurchase");
                }
            }
            if (connect != null) {
                try {
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT EN Purchase.deletePurchase");
                }
            }
        }
        
        return resp;
    }
    
    public List<Purchase> getAllPurchases() {
        List<Purchase> purchases = new ArrayList<>();
        
        Connection connect = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        String query = "SELECT "
                + "name_product, "
                + "name_provider, "
                + "quantity, "
                + "cost, "
                + "purchase_date, "
                + "detail, "
                + "id_purchase "
                + "FROM purchase, products, providers "
                + "WHERE purchase.id_product=products.id_product AND products.id_provider=providers.id_provider;";
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            statement = connect.createStatement();
            
            //preparedStatement.setString(1, param);
            
            resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
                Purchase purchase = new Purchase();
                purchase.setNameProduct(resultSet.getString(1));
                purchase.setNameProvider(resultSet.getString(2));
                purchase.setQuantity(resultSet.getInt(3));
                purchase.setCost(resultSet.getDouble(4));
                purchase.setPurchaseDate(resultSet.getDate(5));
                purchase.setDetail(resultSet.getString(6));
                purchase.setIdPurchase(resultSet.getInt(7));
                
                purchases.add(purchase);
            }
            
        }
        catch(SQLException ex) {
            System.out.println("ERROR AL CONECTAR EN PurchaseDAO.getAllPurchases");
            System.out.println(ex.getMessage());
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }catch(SQLException ex) {}
            }
            if (statement != null) {
                try {
                    statement.close();
                }catch(SQLException ex) {}
            }
            if (connect != null) {
                try {
                    connect.close();
                }catch(SQLException ex) {}
            }
        }
        
        return purchases;
    }
}
