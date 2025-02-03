package model.daos;

import java.util.ArrayList;
import java.util.List;
import model.FileConfig;
import model.ProductSale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.DatabaseConnection;
import java.sql.Statement;

/**
 *
 * @author EmanuelHurt
 */
public class ProductSaleDAO {
    
    private final FileConfig fileConfig;
    
    public ProductSaleDAO(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }
    
    public boolean createProductSale(ProductSale productSale) {
        
        boolean resp = false;
        
        String query = "INSERT INTO product_sales (quantity, total_price, id_sales_detail, id_product, id_user) VALUES (?,?,?,?,?);";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setInt(1, productSale.getQuantity());
            preparedStatement.setDouble(2, productSale.getTotalPrice());
            preparedStatement.setInt(3, productSale.getIdSalesDetail());
            preparedStatement.setInt(4, productSale.getIdProduct());
            preparedStatement.setInt(5, productSale.getIdUser());
            
            int numRows = preparedStatement.executeUpdate();
            
            if (numRows > 0) {
                resp = true;
            }
            
        }
        catch(SQLException ex) {
            System.out.println("ERROR EN ProductSaleDAO.createProductSale()");
            System.out.println(ex.getMessage());
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRA EL PreparedStatement EN CreateProductSale");
                }
            }
            if (connect != null) {
                try {
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECTION EN CreateProductSale");
                }
            }
        }
        
        return resp;
    }
    
    public ProductSale readProductSale(int idProductSale) {
        
        ProductSale sale = new ProductSale();
        
        String query = "SELECT * FROM product_sales WHERE id_sale=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setInt(1, idProductSale);
            
            resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next()) {
                sale.setIdSale(resultSet.getInt(1));
                sale.setQuantity(resultSet.getInt(2));
                sale.setTotalPrice(resultSet.getDouble(3));
                sale.setIdSalesDetail(resultSet.getInt(4));
                sale.setIdProduct(resultSet.getInt(5));
                sale.setIdUser(resultSet.getInt(6));
            }
            
        }
        catch(SQLException ex) {}
        finally {
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
        
        return sale;
    }
    
    public boolean updateProductSale(ProductSale productSale) {
        
        boolean resp = false;
        
        String query = "UPDATE product_sale "
                + "SET quantity=?, "
                + "totalPrice=?, "
                + "id_sales_detail=?, "
                + "id_product=?, "
                + "id_user=? "
                + "WHERE id_sale=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setInt(1, productSale.getQuantity());
            preparedStatement.setDouble(2, productSale.getTotalPrice());
            preparedStatement.setInt(3, productSale.getIdSalesDetail());
            preparedStatement.setInt(4, productSale.getIdProduct());
            preparedStatement.setInt(5, productSale.getIdUser());
            preparedStatement.setInt(6, productSale.getIdSale());
            
            int numRows = preparedStatement.executeUpdate();
            
            if (numRows > 0) {
                resp = true;
            }
            
        }catch(SQLException ex) {}
        finally {
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
        
        return resp;
    }
    
    public boolean deleteProductSale(int idProductSale) {
        
        boolean resp = false;
        
        String query = "DELETE FROM product_sales WHERE id_sale=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setInt(1, idProductSale);
            
            int numRows = preparedStatement.executeUpdate();
            
            if (numRows > 0) {
                resp = true;
            }
            
        }catch(SQLException ex) {}        
        finally {
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
        
        return resp;
    }
    
    public List<ProductSale> productSaleList() {
        
        List<ProductSale> sales = new ArrayList<>();
        
        String query = "SELECT * FROM product_sales";
        
        Connection connect = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            statement = connect.createStatement();
            
            resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
                ProductSale sale = new ProductSale();
                sale.setIdSale(resultSet.getInt(1));
                sale.setQuantity(resultSet.getInt(2));
                sale.setTotalPrice(resultSet.getDouble(3));
                sale.setIdSalesDetail(resultSet.getInt(4));
                sale.setIdProduct(resultSet.getInt(5));
                sale.setIdUser(resultSet.getInt(6));
                
                sales.add(sale);
            }
            
        }catch(SQLException ex) {}
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
        
        return sales;
    }
    //FALTA MEJORAR, AGREGAR MEJORAS A LA QUERY
    public List<ProductSale> productSaleListByIdSaleDetail(int idSaleDetail) {
        ArrayList<ProductSale> list = new ArrayList<>();
        
        String query = "SELECT * "
                + "FROM product_sales "
                + "WHERE id_sales_detail=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, idSaleDetail);
            
            resultSet = preparedStatement.executeQuery();
            //id, quantiy, totalPrice,idSalesDetail,idProduct,idUser
            while (resultSet.next()) {
                ProductSale productSale = new ProductSale();
                productSale.setIdSale(resultSet.getInt(1));
                productSale.setQuantity(resultSet.getInt(2));
                productSale.setTotalPrice(resultSet.getDouble(3));
                productSale.setIdSalesDetail(resultSet.getInt(4));
                productSale.setIdProduct(resultSet.getInt(5));
                productSale.setIdUser(resultSet.getInt(6));
                
                list.add(productSale);
            }
            
        }catch(SQLException ex) {
            System.out.println("ERROR EN ProductSaleDAO.productSaleListByIdSaleDetail");
        }finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }catch(SQLException ex) {
                    System.out.print("error al cerrar resultSet en ProductSaleDAO");
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("Error al cerrar preparedStatement en ProductSaleDAO");
                }
            }
            if (connect != null) {
                try {
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("Error al cerrar connection en ProductSaleDAO");
                }
            }
        }
        
        return list;
    }
    //CAMBIA EL idUser DE LOS ProductSales al id del admin
    public boolean backupProductSales(int idUserDeleted) {
        boolean resp = false;
        
        String query = "UPDATE product_sales "
                + "SET id_user=? "
                + "WHERE id_user=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, 1);
            preparedStatement.setInt(2, idUserDeleted);
            int rowsAfected = preparedStatement.executeUpdate();
            if (rowsAfected > 0) {
                resp = true;
            }
        }catch(SQLException ex) {
            System.out.println("ERROR EN ProductSaleDAO.backupProductSales()");
            System.out.println(ex.getMessage());
        }finally {
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
        
        return resp;
    }
    
    public boolean deleteProductSalesForIdSaleDetail(int idSaleDetail) {
        boolean resp = false;
        
        String query = "DELETE FROM product_sales WHERE id_sales_detail=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, idSaleDetail);
            
            int numRows = preparedStatement.executeUpdate();
            
            if (numRows > 0) {
                resp = true;
            }
            
        }catch(SQLException ex) {
            System.out.println("ERROR EN ProductSaleDAO.deleteProductSalesForIdSaleDetail");
            System.out.println(ex.getMessage());
        }
        finally {
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
        
        return resp;
    }
}
