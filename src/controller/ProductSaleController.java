package controller;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Product;
import model.ProductSale;
import model.daos.ProductDAO;
import model.daos.ProductSaleDAO;
import view.ErrorModal;
import view.InitialWindow;
import view.SaleDetailModal;
import view.SaleModal;
import view.SalePanel;
import view.SuccessfullSaleModal;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import model.Client;
import model.Percentage;
import model.SaleDetail;
import model.daos.ClientDAO;
import model.daos.PercentageDAO;
import model.daos.SaleDetailDAO;

/**
 *
 * @author EmanuelHurt
 */
public class ProductSaleController extends MouseAdapter {
    
    private final InitialWindow iniWindow;
    private SalePanel searchProductPanel;
    private SaleDetailModal saleDetailPanel;
    
    private SaleListController saleListController;
    
    private final ProductSaleDAO productSaleDAO;
    private final ClientDAO clientDAO;
    private final SaleDetailDAO saleDetailDAO;
    private final PercentageDAO percentageDAO;
    private final ProductDAO productDAO;
    
    private final ArrayList<ProductSale> productsSaleList;
    private double totalAmount;
    
    private final int idUser;
    
    public ProductSaleController(int idUser,ProductSaleDAO productSaleDAO, ProductDAO productDAO, ClientDAO clientDAO, SaleDetailDAO detailDAO, PercentageDAO percenDAO, InitialWindow iniWindow) {
        this.idUser = idUser;
        this.productSaleDAO = productSaleDAO;
        this.productDAO = productDAO;
        this.clientDAO = clientDAO;
        this.saleDetailDAO = detailDAO;
        this.percentageDAO = percenDAO;
        this.iniWindow = iniWindow;
        
        this.saleDetailPanel = null;
        
        productsSaleList = new ArrayList<>();
        
        totalAmount = 0.0;
    }
    
    public ArrayList<ProductSale> getProductsSaleList() {
        return productsSaleList;
    }
    
    public void setSearchProductPanel(SalePanel searchPanel) {
        searchProductPanel = searchPanel;
        
        totalAmount = 0.0;
        this.productsSaleList.clear();
        
        //CREACION DEL CONTROLADOR PARA EL PANEL QUE LISTA LAS VENTAS
        saleListController = new SaleListController(this.searchProductPanel.getProductsSaleListPanel(), this.searchProductPanel.getLblTotalAmount(), this);
        
        //ELEMENTOS DEL SearchProductPanel A LA ESCUCHA
        searchProductPanel.getBtnSearch().addMouseListener(this);
        this.searchProductPanel.getlblBtnMakeSale().addMouseListener(this);
        
        this.searchProductPanel.getSearchField().addActionListener(e -> {
            String searchedProduct = searchProductPanel.getSearchField().getText();
            if (searchedProduct.length() != 0) {
                showFoundProducts(searchedProduct);
            }
        });
        
        //LlENAR LA TABLA DEL SearchProductPanel CON LOS PRODUCTOS
        DefaultTableModel model = (DefaultTableModel)searchProductPanel.getTableProductsFound().getModel();
        ArrayList<Product> products = (ArrayList<Product>)productDAO.listById();
        
        int count = 1;
        for (Product product : products) {
            String row[] = {count+"",
                            product.getCodProduct(), 
                            product.getNameProvider(),
                            product.getNameProduct(),
                            product.getGenericName(),
                            product.getPharmaForm(),
                            product.getConcentration(), 
                            product.getPrice()+"",
                            product.getExistence()+""};
            
            model.addRow(row);
            
            count++;
        }
        
        //PARA QUE LA TABLA RESPONDA AL DOBLE CLICK
        searchProductPanel.setController(this);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
        Object eventSource = e.getSource();
        //ESTE BLOQUE ES PARA ESCUCHAR LOS EVENTOS DE searchProductPanel
        if (searchProductPanel != null) {
            if(eventSource.equals(searchProductPanel.getBtnSearch())) {         
                String searchedProduct = searchProductPanel.getSearchField().getText();
                if (searchedProduct.length() != 0) {
                    showFoundProducts(searchedProduct);
                }
            }
            
            else if(searchProductPanel.getlblBtnMakeSale().equals(eventSource)) {
                
                if (searchProductPanel.getProductsSaleListPanel().getComponentCount() > 0) {
                    //CREAR MODAL PARA DETALLE DE VENTA SaleDetail
                    this.saleDetailPanel = new SaleDetailModal(this,totalAmount);
                    saleDetailPanel.setModalFrame();//muestra el panel en un modal
                } else {
                    ErrorModal erroModal = new ErrorModal("Debe seleccionar productos para la venta");
                }
                
            }
        }      
    }
    
    //ESTE MUESTRA EL SALE PANEL, ES LLAMADO DESDE LA VISTA DE LA TABLA DE PRODUCTOS (AL DOBLE CLICK)
    public void showSalesPanelForSelectedProduct(int iRow) {
        JTable tableData = searchProductPanel.getTableProductsFound();
        
        if (iRow != -1) {                      

            DefaultTableModel model = (DefaultTableModel)tableData.getModel();
            
            String strExistence = (String)model.getValueAt(iRow, 8);
            int existence = Integer.parseInt(strExistence);
            
            if (existence > 0) {

                String codProduct = (String)model.getValueAt(iRow, 1);
                
                if (!existInTheProductsSaleList(codProduct)) {
                    
                    Product product = productDAO.readProduct(codProduct);
                
                    Percentage percentage = percentageDAO.readPercentage(1);
                    double discountPercentage = percentage.getDiscountPercentage();

                    //LOS ENVIA AL PANEL DE VENTA DEL PRODUCTO salePanel
                    SaleModal salePanel = new SaleModal(discountPercentage,product, this);
                    
                } else {
                    ErrorModal modal = new ErrorModal("El Producto ya se encuentra en la lista");
                }
                
            }
            else {
                ErrorModal modal = new ErrorModal("Producto sin Existencia");
            }
        }
    }
    
    private boolean existInTheProductsSaleList(String codProd) {
        boolean resp = false;
        
        for (ProductSale productSale : productsSaleList) {
            if (productSale.getCodProduct().equals(codProd)) {
                resp = true;
                break;
            }
        }
        
        return resp;
    }
    
    //MUESTRA EN LA TABLA LOS DATOS HALLADOS SEGUN EL NOMBREPRODUCTO
    public void showFoundProducts(String searchedProduct) {
        
        //OBTENCION DE LOS DATOS
        ArrayList<Product> listByName = (ArrayList) productDAO.listByNameProduct(searchedProduct);
        
        ArrayList<Product> listByPharmaForm = (ArrayList) productDAO.listByPharmaForm(searchedProduct);
        
        if (!listByName.isEmpty()) {
            fillSearchedTable(listByName);
        } else if (!listByPharmaForm.isEmpty()) {
            fillSearchedTable(listByPharmaForm);
        }
        
    }
    private void fillSearchedTable(List<Product> list) {
        DefaultTableModel modelTable = (DefaultTableModel)searchProductPanel.getTableProductsFound().getModel();
        //VACIAR LA TABLA SI CONTIENE DATOS
        if (modelTable.getRowCount() > 0) {
            modelTable.setRowCount(0);
        }
        
        int count = 1;
        for (Product product : list) {
            String row[] = {count+"",
                            product.getCodProduct(),
                            product.getNameProvider(),
                            product.getNameProduct(),
                            product.getGenericName(),
                            product.getPharmaForm(),
                            product.getConcentration(), 
                            product.getPrice()+"",
                            product.getExistence()+""};
            
            modelTable.addRow(row);
            
            count++;
        }
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double amount) {
        totalAmount = amount;
    }
    
    //ESTE METODO ES LLAMADO DESDE LA VISTA SalePanel CUANDO SE CONFIRMA LA VENTA
    public void addProductSaleToPanel(ProductSale prodSale, Product prod) {
        JPanel salePanel = new JPanel();
        salePanel.setBackground(new Color(213, 245, 227));
        JLabel lblNameProduct = new JLabel(prod.getNameProduct());
        JLabel lblConcentration = new JLabel(prod.getConcentration());
        JLabel lblQuantity = new JLabel(" | Cantidad: " + prodSale.getQuantity());
        JLabel lblTotalPrice = new JLabel(" | Bs. " + prodSale.getTotalPrice());
        
        JLabel lblEliminate = new JLabel();
        lblEliminate.setOpaque(true);
        lblEliminate.setBackground(new Color(213, 245, 227));     
        ImageIcon iconEliminate = new ImageIcon(getClass().getResource("/images/iconRemove.png"));
        lblEliminate.setIcon(iconEliminate);
        lblEliminate.setHorizontalAlignment(JLabel.CENTER);
        lblEliminate.addMouseListener(saleListController);
        
        salePanel.add(lblNameProduct);
        salePanel.add(lblConcentration);
        salePanel.add(lblQuantity);
        salePanel.add(lblTotalPrice);
        salePanel.add(lblEliminate);
        
        searchProductPanel.getProductsSaleListPanel().add(salePanel);
        searchProductPanel.getProductsSaleListPanel().revalidate();
        searchProductPanel.getProductsSaleListPanel().repaint();
        
        //ACTUALIZAR EL MONTO TOTAL
        double total = prodSale.getTotalPrice();
        totalAmount = totalAmount + total;
        totalAmount = Math.round(totalAmount * Math.pow(10, 2)) / Math.pow(10,2);
        
        String sTotalAmount = "Monto Total: Bs. ";
        searchProductPanel.getLblTotalAmount().setText(sTotalAmount + totalAmount);
    }

    //ES LLAMADO DESDE EL MODAL SaleDetailPanel POR SU BOTON ACEPTAR
    public void registerSale() {
        
        String clientName = this.saleDetailPanel.getFieldNameClient().getText();
        String nitci = this.saleDetailPanel.getFieldNITCI().getText();
        
        if (!clientName.isEmpty() && !nitci.isEmpty()) {
            //1ro GUARDAR DATOS DEL CLIENTE
            Client client = new Client();            
            client.setCodClient(nitci);
            client.setName(clientName);
            
            clientDAO.registerClient(client); //CLIENTE GUARDADO
            
            //2do GUARDAR EL SaleDetail Y OBTENER SU ID
            GregorianCalendar calendar = new GregorianCalendar();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String date = year+"-"+month+"-"+day;
            Date dateSale = Date.valueOf(date);
            Time time = Time.valueOf(LocalTime.now());
            
            SaleDetail saleDetail = new SaleDetail();
            saleDetail.setDateSale(dateSale);
            saleDetail.setTotalPrice(totalAmount);
            saleDetail.setDescription(saleDetailPanel.getTxtAreaDescription().getText());                     
            saleDetail.setCodClient(nitci);
            saleDetail.setHourSale(time);
            int idSaleDetail = saleDetailDAO.createSaleDetail(saleDetail); //DETALLE DE VENTA GUARDADO
            
            // GUARDAR ProductSale, ACTUALIZAR LA EXISTENCIA DE LOS PRODUCTOS
            for (ProductSale productSale: productsSaleList) {
                
                productSale.setIdUser(idUser);
                productSale.setIdSalesDetail(idSaleDetail);
                productSaleDAO.createProductSale(productSale);//GUARDAR EL ProductSale
                //ACTUALIZAR EXISTENCIA DE PRODUCTOS
                int quantity = productSale.getQuantity() * (-1);
                productDAO.updateExistence(productSale.getCodProduct(), quantity);                                      
            }
            
            //vaciar lista
            productsSaleList.clear();
            //ELIMINAR EL MODAL
            saleDetailPanel.getModalFrame().dispose();
            saleDetailPanel = null;

            SuccessfullSaleModal successSale = new SuccessfullSaleModal();

            //ACTUALIZAR VISTA TABLA DE PRODUCTOS
            updateTableOfProducts();

            //LIMPIAR EL PANEL DE LISTA DE PRODUCTOS EN VENTA
            searchProductPanel.getProductsSaleListPanel().removeAll();
            searchProductPanel.getProductsSaleListPanel().revalidate();
            searchProductPanel.getProductsSaleListPanel().repaint();
            //LIMPIAR ETIQUETA DE MONTO TOTAL
            String sTotalAmount = "Monto Total: Bs. ";
            searchProductPanel.getLblTotalAmount().setText(sTotalAmount);
            //reiniciar el valor del monto total
            totalAmount = 0.0;
        }
           
    }
    
    private void updateTableOfProducts() {
        DefaultTableModel model = (DefaultTableModel)searchProductPanel.getTableProductsFound().getModel();
        
        model.setRowCount(0);
        
        ArrayList<Product> products = (ArrayList<Product>)productDAO.listById();
        
        int count = 1;
        for (Product product : products) {
            String row[] = {count+"",
                            product.getCodProduct(),
                            product.getNameProvider(),
                            product.getNameProduct(),
                            product.getGenericName(),
                            product.getPharmaForm(),
                            product.getConcentration(), 
                            product.getPrice()+"",
                            product.getExistence()+""};
            
            model.addRow(row);
            count ++;
        }
    }
}
