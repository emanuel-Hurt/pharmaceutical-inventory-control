package model.daos;

import java.sql.Date;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.ArrayList;
import model.DatabaseConnection;
import model.FileConfig;
import model.Product;

/**
 *
 * @author EmanuelHurt
 */
public class ProductDAO {
    
    private final FileConfig fileConfig;
    
    public ProductDAO(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }
    
    public List<Product> listById() {
        String query = "SELECT "
                + "id_product, "
                + "name_product, "
                + "name_provider, "
                + "pharma_form, "
                + "concentration, "
                + "price, "
                + "existence, "
                + "due_date, "
                + "cod_product, "
                + "num_lote, "
                + "generic_name "
                + "FROM products, providers "
                + "WHERE products.id_provider=providers.id_provider;";
        return toList(query);
    }
    
    public List<Product> alphabeticalList() {
        String query = "SELECT "
                + "id_product, "
                + "name_product, "
                + "name_provider, "
                + "pharma_form, "
                + "concentration, "
                + "price, "
                + "existence, "
                + "due_date, "
                + "cod_product, "
                + "num_lote, "
                + "generic_name "
                + "FROM products, providers "
                + "WHERE products.id_provider=providers.id_provider ORDER BY name_product;";
        return toList(query);
    }
    
    public List<Product> listByPrice() {
        String query = "SELECT "
                + "id_product, "
                + "name_product, "
                + "name_provider, "
                + "pharma_form, "
                + "concentration, "
                + "price, existence, "
                + "due_date, "
                + "cod_product, "
                + "num_lote, "
                + "generic_name "
                + "FROM products, providers "
                + "WHERE products.id_provider=providers.id_provider ORDER BY price;";
        return toList(query);
    }
    //ESTE METODO DEVUELVE UNA LISTA DE PRODUCTOS QUE COINCIDAN CON LAS INICIALES ENVIADAS
    public List<Product> listByNameProduct(String chars) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT "
                + "id_product, "
                + "name_product, "
                + "name_provider, "
                + "pharma_form, "
                + "concentration, "
                + "price, "
                + "existence, "
                + "due_date, "
                + "cod_product, "
                + "num_lote, "
                + "generic_name "
                + "FROM products, providers "
                + "WHERE name_product LIKE ? AND providers.id_provider=products.id_provider;";
        
        String param = chars + "%";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setString(1, param);
            
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                
                Product product = new Product();
                
                product.setIdProduct(resultSet.getInt(1));
                product.setNameProduct(resultSet.getString(2));
                product.setNameProvider(resultSet.getString(3));
                product.setPharmaForm(resultSet.getString(4));
                product.setConcentration(resultSet.getString(5));
                product.setPrice(resultSet.getDouble(6));
                product.setExistence(resultSet.getInt(7));
                product.setDueDate(resultSet.getDate(8));
                product.setCodProduct(resultSet.getString(9));
                product.setNumLote(resultSet.getString(10));
                product.setGenericName(resultSet.getString(11));
                
                list.add(product);
            }
            
        }
        catch(SQLException ex) {
        }
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
        
        return list;
    }
    
    public List<Product> listByPharmaForm(String chars) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT "
                + "id_product, "
                + "name_product, "
                + "name_provider, "
                + "pharma_form, "
                + "concentration, "
                + "price, "
                + "existence, "
                + "due_date, "
                + "cod_product, "
                + "num_lote, "
                + "generic_name "
                + "FROM products, providers "
                + "WHERE pharma_form LIKE ? AND providers.id_provider=products.id_provider;";
        
        String param = chars + "%";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setString(1, param);
            
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                
                Product product = new Product();
                
                product.setIdProduct(resultSet.getInt(1));
                product.setNameProduct(resultSet.getString(2));
                product.setNameProvider(resultSet.getString(3));
                product.setPharmaForm(resultSet.getString(4));
                product.setConcentration(resultSet.getString(5));
                product.setPrice(resultSet.getDouble(6));
                product.setExistence(resultSet.getInt(7));
                product.setDueDate(resultSet.getDate(8));
                product.setCodProduct(resultSet.getString(9));
                product.setNumLote(resultSet.getString(10));
                product.setGenericName(resultSet.getString(11));
                
                list.add(product);
            }
            
        }
        catch(SQLException ex) {
        }
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
        
        return list;
    }
    
    private List<Product> toList(String query) {
        
        Connection connec = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        List<Product> products = new ArrayList<>();
        
        try {
            
            connec = DatabaseConnection.getConnection(fileConfig);
            
            statement = connec.createStatement();
            resultSet = statement.executeQuery(query);                      
            
            while(resultSet.next()) {                               
                
                Product product = new Product();
                
                product.setIdProduct(resultSet.getInt(1));
                product.setNameProduct(resultSet.getString(2));
                product.setNameProvider(resultSet.getString(3));
                product.setPharmaForm(resultSet.getString(4));
                product.setConcentration(resultSet.getString(5));
                product.setPrice(resultSet.getDouble(6));
                product.setExistence(resultSet.getInt(7));
                product.setDueDate(resultSet.getDate(8));
                product.setCodProduct(resultSet.getString(9));
                product.setNumLote(resultSet.getString(10));
                product.setGenericName(resultSet.getString(11));
                
                products.add(product);
                
            }         
            
        } catch (SQLException ex) {
            System.out.println("error al obtener los productos");
        }
        finally {
            
            if(resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    System.out.println("error al cerrar el resultSet");
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                }
                catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR EL STATEMENT DE PRODUCTDAO");
                }
            }
            if(connec != null) {
                try {
                    connec.close();
                } catch (SQLException ex) {
                    System.out.println("error al cerrar la conexion");
                }
            }
        }
        
        return products;
    }   
    
    //agrega un nuevo producto
    public int createProduct(Product product) {
        
        int idRegister = -1;
        
        String name = product.getNameProduct();
        String pharmaForm = product.getPharmaForm();
        String concentration = product.getConcentration();
        Date dueDate = product.getDueDate();
        double price = product.getPrice();
        int existence = product.getExistence();
        String codProduct = product.getCodProduct();
        String numLote = product.getNumLote();
        int idProvider = product.getIdProvider();
        String genericName = product.getGenericName();
                      
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "INSERT INTO products (name_product, pharma_form, concentration, due_date, price, existence, cod_Product, num_lote, id_provider, generic_name) VALUES (?,?,?,?,?,?,?,?,?,?);";
               
        try {
            
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, pharmaForm);
            preparedStatement.setString(3, concentration);
            preparedStatement.setDate(4, dueDate);
            preparedStatement.setDouble(5, price);
            preparedStatement.setInt(6, existence);
            preparedStatement.setString(7, codProduct);
            preparedStatement.setString(8, numLote);
            preparedStatement.setInt(9, idProvider);
            preparedStatement.setString(10, genericName);
            
            int numRows = preparedStatement.executeUpdate();
            
            if (numRows > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    idRegister = resultSet.getInt(1);
                }
            }
            
        } catch (Exception ex) {
            System.out.println("error al agregar nuevo producto");
            System.out.println(ex.getCause());
        }
        finally {
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR EL PREPAREDSTATEMENT EN ADDPRODUCT");
                }
            }
            if(connect != null) {
                try {
                    connect.close();
                } catch (SQLException ex) {
                    System.out.println("error al intentar cerrar la conexión con la DDBB");
                }
            }
        }
        return idRegister;
    }
    
    public Product readProduct(String codProduct) {
        
        Product theProduct = new Product();
        
        String query = "SELECT * FROM products WHERE cod_product=?";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1,codProduct);
            
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                theProduct.setIdProduct(resultSet.getInt(1));
                theProduct.setNameProduct(resultSet.getString(2));
                theProduct.setPharmaForm(resultSet.getString(3));
                theProduct.setConcentration(resultSet.getString(4));
                theProduct.setDueDate(resultSet.getDate(5));
                theProduct.setPrice(resultSet.getDouble(6));
                theProduct.setExistence(resultSet.getInt(7));
                theProduct.setCodProduct(resultSet.getString(8));
                theProduct.setNumLote(resultSet.getString(9));
                theProduct.setIdProvider(resultSet.getInt(10));
                theProduct.setGenericName(resultSet.getString(11));
            }
        }
        catch(SQLException ex) {
            System.out.println("ERROR AL CONECTAR EN getProduct");
        }
        finally {
            if (resultSet != null) {
                try{
                    resultSet.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR RESULTSET EN readProduct");
                }
            }
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN readProduct");
                }
            }
            if (connect != null) {
                try{
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT EN readProduct");
                }
            }
        }
        
        return theProduct;
    }
    
    public Product getProduct(int idProd) {
        Product product = new Product();
        //id, name, forma, concentracion, fechaVence, precio, existencia, cod, lote, idProv, generico
        String query = "SELECT "
                + "id_product, "
                + "name_product, "
                + "pharma_form, "
                + "concentration, "
                + "due_date, "
                + "price, "
                + "existence, "
                + "cod_product, "
                + "num_lote, "
                + "name_provider, "
                + "generic_name "
                + "FROM products, providers "
                + "WHERE id_product=? AND products.id_provider=providers.id_provider;";
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, idProd);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                product.setIdProduct(resultSet.getInt(1));
                product.setNameProduct(resultSet.getString(2));
                product.setPharmaForm(resultSet.getString(3));
                product.setConcentration(resultSet.getString(4));
                product.setDueDate(resultSet.getDate(5));
                product.setPrice(resultSet.getDouble(6));
                product.setExistence(resultSet.getInt(7));
                product.setCodProduct(resultSet.getString(8));
                product.setNumLote(resultSet.getString(9));
                product.setNameProvider(resultSet.getString(10));
                product.setGenericName(resultSet.getString(11));
            }
        }catch(SQLException ex) {
            System.out.println("ERROR EN ProductDAO.getProduct()");
            System.out.println(ex.getMessage());
        }finally {
            if (resultSet != null) {
                try{
                    resultSet.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR RESULTSET EN getProduct");
                }
            }
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN getProduct");
                }
            }
            if (connect != null) {
                try{
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT EN getProduct");
                }
            }
        }
        
        return product;
    }
    
    public boolean updateProduct(Product product) {
        
        boolean resp = false;
        
        int idProduct = product.getIdProduct();
        String nameProduct = product.getNameProduct();
        String pharmaForm = product.getPharmaForm();
        String concentration = product.getConcentration();
        Date dueDate = product.getDueDate();
        double price = product.getPrice();
        String codProduct = product.getCodProduct();
        String numLote = product.getNumLote();
        int idProvider = product.getIdProvider();
        String genericName = product.getGenericName();
        int existence = product.getExistence();
        
        String query = "UPDATE products "
                + "SET "
                + "name_product=?, "
                + "pharma_form=?, "
                + "concentration=?, "
                + "due_date=?, "
                + "price=?, "
                + "num_lote=?, "
                + "id_provider=?, "
                + "generic_name=?, "
                + "existence=? "
                + "WHERE cod_product=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setString(1, nameProduct);
            preparedStatement.setString(2, pharmaForm);
            preparedStatement.setString(3, concentration);
            preparedStatement.setDate(4, dueDate);
            preparedStatement.setDouble(5, price);
            preparedStatement.setString(6, numLote);
            preparedStatement.setInt(7, idProvider);
            preparedStatement.setString(8, genericName);
            preparedStatement.setInt(9, existence);
            preparedStatement.setString(10, codProduct);
            
            int rows = preparedStatement.executeUpdate();
            
            if(rows > 0) {
                resp = true;
            }
            
        }
        catch(SQLException e) {
            System.out.println("ERROR EN ProducDAO.updateProduct()");
            System.out.println(e.getMessage());
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN UPDATE PRODUCT");
                }
            }
            if (connect != null) {
                try {
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT EN UPDATE PRODUCT");
                }
            }
        }
        
        return resp;
    }
    
    public boolean deleteProduct(int idProduct) {
        
        boolean resp = false;
        
        String query = "DELETE FROM products WHERE id_product=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setInt(1, idProduct);
            
            int rows = preparedStatement.executeUpdate();
            
            if (rows > 0) {
                resp = true;
            }
            
        }catch(SQLException ex) {
            System.out.println("ERROR AL CONECTAR AL ELIMINAR PRODUCT");
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT AL ELIMINAR PRODUCT");
                }
            }
            if (connect != null) {
                try {
                    connect.close();
                }catch(SQLException ex) {
                    System.out.println("ERROR AL CERRAR CONNECT AL ELIMINAR PRODUCT");
                }
            }
        }
        
        return resp;
    }
    
    public boolean updateExistence(String codProd, int quantity) {
        boolean resp = false;
        
        //Actualizar existencia del producto
        Product oldProduct = readProduct(codProd);
        
        if (oldProduct.getNameProduct() != null) {
            
            int existence = oldProduct.getExistence() + quantity;
            
            String query = "UPDATE products SET existence=? WHERE cod_product=?;";
        
            Connection connect = null;
            PreparedStatement preparedStatement = null;

            try {
                connect = DatabaseConnection.getConnection(fileConfig);

                preparedStatement = connect.prepareStatement(query);

                
                preparedStatement.setInt(1, existence);
                preparedStatement.setString(2, codProd);

                int rows = preparedStatement.executeUpdate();

                if(rows > 0) {
                    resp = true;
                }

            }
            catch(SQLException ex) {
                System.out.println("ERROR EN CONECCION EN UPDATE PRODUCT");
            }
            finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    }catch(SQLException ex) {
                        System.out.println("ERROR AL CERRAR PREPAREDSTATEMENT EN UPDATE PRODUCT");
                    }
                }
                if (connect != null) {
                    try {
                        connect.close();
                    }catch(SQLException ex) {
                        System.out.println("ERROR AL CERRAR CONNECT EN UPDATE PRODUCT");
                    }
                }
            }
        }          
        
        return resp;
    }
    
    public List<Product> getProductsToExpire(int weeks) {
        ArrayList<Product> products = new ArrayList<>();
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        String query = "SELECT "
                + "id_product, "
                + "name_product, "
                + "pharma_form, "
                + "concentration, "
                + "due_date, "
                + "price, "
                + "existence, "
                + "name_provider "
                     + "FROM products, providers "
                     + "WHERE "
                + "products.id_provider=providers.id_provider AND "
                + "due_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL ? WEEK);";
        
        try {
            connect = DatabaseConnection.getConnection(fileConfig);
            
            preparedStatement = connect.prepareStatement(query);
            
            preparedStatement.setInt(1, weeks);
            
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                
                Product product = new Product();
                
                product.setIdProduct(resultSet.getInt(1));
                product.setNameProduct(resultSet.getString(2));
                product.setPharmaForm(resultSet.getString(3));
                product.setConcentration(resultSet.getString(4));
                product.setDueDate(resultSet.getDate(5));
                product.setPrice(resultSet.getDouble(6));
                product.setExistence(resultSet.getInt(7));
                product.setNameProvider(resultSet.getString(8));
                
                products.add(product);
            }
            
        }
        catch(SQLException ex) {
        }
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
        
        return products;
    }
    
    public boolean verifyExistence(Product product) {
        boolean resp = false;
        
        String query = "SELECT * "
                     + "FROM products "
                     + "WHERE "
                     + "cod_product=?;";
        
        Connection connec = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            
            connec = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connec.prepareStatement(query);
            preparedStatement.setString(1, product.getCodProduct());
            
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                resp = true;
            }
            
        }catch(SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
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
            if (connec != null) {
                try {
                    connec.close();
                }catch(SQLException ex) {}
            }
        }
        
        return resp;
    }
    
    public List<Product> getListOfMinimumExistence(int minimum) {
        ArrayList<Product> list = new ArrayList<>();
        
        String query = "SELECT * "
                + "FROM products "
                + "WHERE existence<=?;";
        
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            
            connect = DatabaseConnection.getConnection(fileConfig);
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, minimum);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                Product product = new Product();
                product.setIdProduct(resultSet.getInt(1));
                product.setNameProduct(resultSet.getString(2));
                product.setPharmaForm(resultSet.getString(3));
                product.setConcentration(resultSet.getString(4));
                product.setDueDate(resultSet.getDate(5));
                product.setPrice(resultSet.getDouble(6));
                product.setExistence(resultSet.getInt(7));
                product.setCodProduct(resultSet.getString(8));
                product.setNumLote(resultSet.getString(9));
                product.setIdProvider(resultSet.getInt(10));
                product.setGenericName(resultSet.getString(11));
                
                list.add(product);
            }
            
        }catch(SQLException ex) {
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
        
        return list;
    }
}
