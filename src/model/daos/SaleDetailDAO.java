package model.daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;
import model.FileConfig;
import model.SaleDetail;

/**
 *
 * @author EmanuelHurt
 */
public class SaleDetailDAO {
    
    private FileConfig fileConfig;
    
    public SaleDetailDAO(FileConfig file) {
        fileConfig = file;
    }
    
    public int createSaleDetail(SaleDetail saleDetail) {
        int idRegister = 0;       
                      
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO "
                + "sales_details (date_sale, total_price, description, cod_client, hour_sale) "
                     + "VALUES (?,?,?,?,?);";
        ResultSet resultSet = null;
               
        try {
            
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            
            preparedStatement.setDate(1, saleDetail.getDateSale());
            preparedStatement.setDouble(2, saleDetail.getTotalPrice());
            preparedStatement.setString(3, saleDetail.getDescription());
            preparedStatement.setString(4, saleDetail.getCodClient());
            preparedStatement.setTime(5, saleDetail.getHourSale());
            
            int numRows = preparedStatement.executeUpdate();
            
            if (numRows > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    idRegister = resultSet.getInt(1);
                }
            }
            
        } catch (Exception ex) {
            System.out.println("error al agregar detalle de venta");
            System.out.println(ex.getCause());
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR EL RESULTSET EN SaleDetailDAO");
                }
            }
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR EL PREPAREDSTATEMENT EN SaleDetailDAO");
                }
            }
            if(connect != null) {
                try {
                    connect.close();
                } catch (SQLException ex) {
                    System.out.println("error al intentar cerrar la conexión con la DDBB en SaleDetailDAO");
                }
            }
        }
        return idRegister;
    }
    
    public List<SaleDetail> getSalesDetails() {
        ArrayList<SaleDetail> list = new ArrayList<>();
        
        String query = "SELECT "
                + "id_sales_detail, "
                + "date_sale, "
                + "total_price, "
                + "description, "
                + "hour_sale, "
                + "name_client "
                + "FROM sales_details, client "
                + "WHERE sales_details.cod_client=client.cod_client "
                + "ORDER BY date_sale DESC, hour_sale DESC;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                SaleDetail saleDetail = new SaleDetail();
                saleDetail.setIdSaleDetail(resultSet.getInt(1));
                saleDetail.setDateSale(resultSet.getDate(2));
                saleDetail.setTotalPrice(resultSet.getDouble(3));
                saleDetail.setDescription(resultSet.getString(4));
                saleDetail.setHourSale(resultSet.getTime(5));
                saleDetail.setNameClient(resultSet.getString(6));
                
                list.add(saleDetail);
            }
            
        }catch(SQLException ex) {
            System.out.println("ERROR EN SaleDetailDAO.getSalesDetails");
            System.out.println(ex.getMessage());
        }finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR EL RESULTSET EN SaleDetailDAO.getSalesDetails");
                }
            }
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR EL PREPAREDSTATEMENT EN SaleDetailDAO.getSalesDetails");
                }
            }
            if(connect != null) {
                try {
                    connect.close();
                } catch (SQLException ex) {
                    System.out.println("error al intentar cerrar la conexión con la DDBB en SaleDetailDAO.getSalesDetails");
                }
            }
        }
        
        return list;
    }
    
    public boolean deleteSalesDetail(int idSaleDetail) {
        boolean resp = false;
        
        String query = "DELETE FROM sales_details "
                + "WHERE id_sales_detail=?";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, idSaleDetail);
            int rowsAfected = preparedStatement.executeUpdate();
            if (rowsAfected > 0) {
                resp = true;
            }
        }catch(SQLException ex) {
            System.out.println("ERROR EN SaleDetailDAO.delete");
            System.out.println(ex.getMessage());
        }finally {
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR EL PREPAREDSTATEMENT EN SaleDetailDAO");
                }
            }
            if(connect != null) {
                try {
                    connect.close();
                } catch (SQLException ex) {
                    System.out.println("error al intentar cerrar la conexión con la DDBB en SaleDetailDAO");
                }
            }
        }
        
        return resp;
    }
}
