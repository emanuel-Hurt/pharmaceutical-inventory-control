package controller;

import java.awt.Color;
import view.InitialWindow;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import model.FileConfig;
import model.Product;
import model.TimeNotification;
import model.User;
import model.daos.*;
import view.*;
/**
 *
 * @author EmanuelHurt
 */
public class InitialController extends MouseAdapter {
    
    private final InitialWindow iniWindow;
    private final FileConfig fileConfig;
    
    private ControlPanel controlPanel;
    
    private final ProductDAO productDAO;
    private final TimeNotificationDAO timeNotificationDAO;
    
    private final ProductController productController;
    private final PurchaseController purchaseController;
    private final ProductSaleController productSaleController;
    private final SettingsController settingsController;
    private final ProviderController providerController;
    private final EntradasController entradasController;
    private final SalesHistoryController salesHistoryController;
    
    private JLabel selected;
    private Color enterColor, exitColor, selectColor;
    
    private final User user;
    
    private JLabel lblBtnControlPanel, lblBtnProducts, lblBtnBuys;
    private JLabel lblBtnSale, lblBtnSettings, lblBtnProviders, lblBtnInputProducts;
    private JLabel lblBtnSalesHistory;
    private JLabel lblBtnCloseSesion;
    
    public InitialController(InitialWindow initialWindow, FileConfig fileConfig, User user) {
        iniWindow = initialWindow;
        this.fileConfig = fileConfig;
        this.user = user;
        
        //CREACION DE LOS CONTROLADORES     
        productDAO = new ProductDAO(fileConfig);
        timeNotificationDAO = new TimeNotificationDAO(fileConfig);
        
        ProductSaleDAO productSaleDAO = new ProductSaleDAO(fileConfig);
        PercentageDAO percentageDAO = new PercentageDAO(fileConfig);
        UserDAO userDAO = new UserDAO(fileConfig);
        PasswordDAO passwordDAO = new PasswordDAO(fileConfig);
        TimeNotificationDAO timeNotificationDAO = new TimeNotificationDAO(fileConfig);
        settingsController = new SettingsController(percentageDAO,userDAO,passwordDAO, productSaleDAO,timeNotificationDAO);
        
        PurchaseDAO purchaseDAO = new PurchaseDAO(fileConfig);
        ProviderDAO providerDAO = new ProviderDAO(fileConfig);
        purchaseController = new PurchaseController(purchaseDAO,productDAO,providerDAO,percentageDAO,iniWindow);
        
        productController = new ProductController(productDAO,providerDAO, percentageDAO);
        
        ClientDAO clientDAO = new ClientDAO(fileConfig);
        SaleDetailDAO saleDetailDAO = new SaleDetailDAO(fileConfig);
        int idU = this.user.getIdUser();
        productSaleController = new ProductSaleController(idU,productSaleDAO, productDAO, clientDAO, saleDetailDAO, percentageDAO, iniWindow);           
        
        providerController = new ProviderController(providerDAO);
        
        entradasController = new EntradasController(productDAO, providerDAO, purchaseDAO);
        
        salesHistoryController = new SalesHistoryController(productDAO, productSaleDAO, saleDetailDAO);
        
        //Guardamos las referencias de los botones de la ventana inicial
        lblBtnControlPanel = iniWindow.getLblBtnControlPanel();
        lblBtnProducts = iniWindow.getBtnProducts();
        lblBtnBuys = iniWindow.getBtnBuys();
        lblBtnSale = iniWindow.getBtnNewSale();
        lblBtnSettings = iniWindow.getLblBtnSettings();
        lblBtnProviders = iniWindow.getLblBtnProviders();
        lblBtnInputProducts = iniWindow.getLblBtnInputProducts();
        lblBtnSalesHistory = iniWindow.getLblBtnSalesHistory();
        lblBtnCloseSesion = iniWindow.getLblBtnCloseSesion();
        
        //ponemos a la escucha los botones
        lblBtnControlPanel.addMouseListener(this);
        lblBtnProducts.addMouseListener(this);
        lblBtnBuys.addMouseListener(this);
        lblBtnSale.addMouseListener(this);
        lblBtnSettings.addMouseListener(this);
        lblBtnProviders.addMouseListener(this);
        lblBtnInputProducts.addMouseListener(this);
        lblBtnSalesHistory.addMouseListener(this);
        lblBtnCloseSesion.addMouseListener(this);
        
        //definimos los colores para los botones, enterColor, y seleccionado
        enterColor = new Color(81, 81, 169);
        exitColor = new Color(18,19,130);
        selectColor = new Color(0, 91, 197);
        selected = lblBtnControlPanel;
        lblBtnControlPanel.setBackground(selectColor);
    }
    
//    public void setControlPanel(ControlPanel panel) {
//        controlPanel = panel;
//        
//        controlPanel.getBtnBuys().addMouseListener(this);
//        controlPanel.getBtnNewSale().addMouseListener(this);
//        controlPanel.getBtnProductsList().addMouseListener(this);
//        
//        putProductsToExpire();
//        putProductsWhitExistenceMinimum();
//    }
//    
    public void activeInitialWindow() {
        putCurrentDate();
        putNameToLblUser();
        
        iniWindow.setLocationRelativeTo(null);
        iniWindow.setVisible(true);
    }
    
    private void putCurrentDate() {
        String days[] = {"Domingo","Lunes","Martes","Miércoles","Jueves","Viernes","Sábado"};
        String months[] = {
                            "Enero",
                            "Febrero",
                            "Marzo",
                            "Abril",
                            "Mayo",
                            "Junio",
                            "Julio",
                            "Agosto",
                            "Septiembre",
                            "Octubre",
                            "Noviembre",
                            "Diciembre"
                            };
        
        GregorianCalendar calendar = new GregorianCalendar();
        int dayMonth = calendar.get(GregorianCalendar.DAY_OF_MONTH);
        int month = calendar.get(GregorianCalendar.MONTH);
        int year = calendar.get(GregorianCalendar.YEAR);
        int nameDay = calendar.get(GregorianCalendar.DAY_OF_WEEK)-1;
        
        String currentDate = days[nameDay] + ", " + dayMonth + " de " + months[month]+ " del " + year+"";
        
        iniWindow.getLblDate().setText(currentDate);
    }
    private void putNameToLblUser() {
        if (user.getName().equals("admin")) {
            iniWindow.getLblUser().setText("ADMINISTRADOR");
        } else {
            iniWindow.getLblUser().setText(user.getName() + " " + user.getLastName() + " " + user.getmLastName());
        }
    }
    private void putProductsToExpire() {
        TimeNotification timeNotification = timeNotificationDAO.readTimeNotification(1);
        int weeks = timeNotification.getQuantityToNotify();
        ArrayList<Product> products = (ArrayList<Product>)productDAO.getProductsToExpire(weeks);
        
        DefaultTableModel model = (DefaultTableModel)controlPanel.getTblProductsToExpire().getModel();
        
        for (Product product : products) {
            String row[] = {
                product.getNameProvider(),
                product.getNameProduct(),
                product.getPharmaForm(),
                product.getExistence()+"",
                product.getDueDate()+""
            };
            model.addRow(row);
        }
    }
    private void putProductsWhitExistenceMinimum() {
        TimeNotification timeNotification = timeNotificationDAO.readTimeNotification(2);
        int quantity = timeNotification.getQuantityToNotify();
        ArrayList<Product> products = (ArrayList)productDAO.getListOfMinimumExistence(quantity);
        
        DefaultTableModel model = (DefaultTableModel)controlPanel.getTblMinimum().getModel();
        
        for (Product product : products) {
            String row[] = {
                product.getNameProvider(),
                product.getNameProduct(),
                product.getPharmaForm(),
                product.getExistence()+"",
                product.getDueDate()+""
            };
            model.addRow(row);
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        selected.setBackground(exitColor);
        if (lblBtnControlPanel.equals(source)) {
            selected = lblBtnControlPanel;
            lblBtnControlPanel.setBackground(selectColor);
            CtrlPanel ctrlPanel = new CtrlPanel();
            //setControlPanel(ctrlPanel);
            iniWindow.changeCenterPanel(ctrlPanel);
        }
        
        else if (source.equals(lblBtnProducts)) {
            
            if (user.getIdRol() == 1) {
                selected = lblBtnProducts;
                lblBtnProducts.setBackground(selectColor);

                ProductsPanel productsPanel = new ProductsPanel(iniWindow);
                productController.setProductsPanel(productsPanel);

                //mostrar los productos en la UI                       
                iniWindow.changeCenterPanel(productsPanel);
            } else {
                ErrorModal modal = new ErrorModal("ACCESO RESTRINGIDO.");
            }
            
        }
        
        else if (source.equals(lblBtnBuys)) {
            
            if (user.getIdRol() == 1) {
                selected = lblBtnBuys;
                lblBtnBuys.setBackground(selectColor);

                InputTypePanel selectPurchasePanel = new InputTypePanel();

                purchaseController.setOptionsPurchasePanel(selectPurchasePanel);

                iniWindow.changeCenterPanel(selectPurchasePanel);
            } else {
                ErrorModal modal = new ErrorModal("ACCESO RESTRINGIDO.");
            }
             
        }
        
        else if (source.equals(lblBtnSale)) {
            selected = lblBtnSale;
            lblBtnSale.setBackground(selectColor);
            
            SalePanel searchPanel = new SalePanel();
            productSaleController.setSearchProductPanel(searchPanel);
            
            iniWindow.changeCenterPanel(searchPanel);
        }
        
        else if (source.equals(lblBtnSettings)) {
            
            if (user.getIdRol() == 1) {
                selected = lblBtnSettings;
                lblBtnSettings.setBackground(selectColor);

                SettingsPanel settingsPanel = new SettingsPanel();

                PercentagesPanel percentagesPanel = new PercentagesPanel();
                ExpireNotificationPanel expireNotificationPanel = new ExpireNotificationPanel();
                UserManagementPanel userManagementPanel = new UserManagementPanel();

                settingsPanel.addToCenterPanel(percentagesPanel);
                settingsPanel.addToCenterPanel(expireNotificationPanel);
                settingsPanel.addToCenterPanel(userManagementPanel);

                settingsController.setPercentagesPanel(percentagesPanel);
                settingsController.setExpireNotificationPanel(expireNotificationPanel);
                settingsController.setUserManagementPanel(userManagementPanel);

                iniWindow.changeCenterPanel(settingsPanel);
            } else {
                ErrorModal modal = new ErrorModal("ACCESO RESTRINGIDO.");
            }
            
        }
        
        else if (source.equals(lblBtnProviders)) {
            
            if (user.getIdRol() == 1) {
                selected = lblBtnProviders;
                lblBtnProviders.setBackground(selectColor);

                ProvidersPanel providersPanel = new ProvidersPanel();
                providerController.setProvidersRegisterPanel(providersPanel);
                iniWindow.changeCenterPanel(providersPanel);
            } else {
                ErrorModal modal = new ErrorModal("ACCESO RESTRINGIDO.");
            }
            
        }
        
        else if (source.equals(lblBtnInputProducts)) {
            
            if (user.getIdRol() == 1) {
                selected = lblBtnInputProducts;
                lblBtnInputProducts.setBackground(selectColor);

                PurchaseHistoryPanel entradasPanel = new PurchaseHistoryPanel();
                entradasController.setEntradasPanel(entradasPanel);
                iniWindow.changeCenterPanel(entradasPanel);
            } else {
                ErrorModal modal = new ErrorModal("ACCESO RESTRINGIDO.");
            }
            
        }
        
        else if (source.equals(lblBtnSalesHistory)) {
            
            if (user.getIdRol() == 1) {
                selected = lblBtnSalesHistory;
                lblBtnSalesHistory.setBackground(selectColor);

                SalesHistoryPanel salesHistoryPanel = new SalesHistoryPanel();
                salesHistoryController.setSalesHistoryPanel(salesHistoryPanel);
                iniWindow.changeCenterPanel(salesHistoryPanel);
            } else {
                ErrorModal modal = new ErrorModal("ACCESO RESTRINGIDO");
            }
            
        }
        
        else if (source.equals(lblBtnCloseSesion)) {
            AccessWindow accessWindow = new AccessWindow();
            UserDAO userDAO = new UserDAO(fileConfig);
            AccessController accessController = new AccessController(accessWindow, userDAO, fileConfig);
            this.iniWindow.dispose();
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        Object source = e.getSource();
        if (source.equals(lblBtnControlPanel)) {
            if (!lblBtnControlPanel.equals(selected)) {
                lblBtnControlPanel.setBackground(enterColor);
            }
        }
        else if (source.equals(lblBtnProducts)) {
            if (!lblBtnProducts.equals(selected)) {
                lblBtnProducts.setBackground(enterColor);
            }
        }
        else if (source.equals(lblBtnBuys)) {
            if (!lblBtnBuys.equals(selected)) {
                lblBtnBuys.setBackground(enterColor);
            }
        }
        else if (source.equals(lblBtnSale)) {
            if (!lblBtnSale.equals(selected)) {
                lblBtnSale.setBackground(enterColor);
            }
        }
        else if (source.equals(lblBtnProviders)) {
            if (!lblBtnProviders.equals(selected)) {
                lblBtnProviders.setBackground(enterColor);
            }
        }
        else if (source.equals(lblBtnSettings)) {
            if (!lblBtnSettings.equals(selected)) {
                lblBtnSettings.setBackground(enterColor);
            }
        }
        else if (source.equals(lblBtnInputProducts)) {
            if (!lblBtnInputProducts.equals(selected)) {
                lblBtnInputProducts.setBackground(enterColor);
            }
        }
        else if (source.equals(lblBtnSalesHistory)) {
            if (!lblBtnSalesHistory.equals(selected)) {
                lblBtnSalesHistory.setBackground(enterColor);
            }
        }
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        Object source = e.getSource();
        if (source.equals(lblBtnControlPanel) && !lblBtnControlPanel.equals(selected)) {
            lblBtnControlPanel.setBackground(exitColor);
        }
        else if (source.equals(lblBtnProducts) && !lblBtnProducts.equals(selected)) {
            lblBtnProducts.setBackground(exitColor);
        }
        else if (source.equals(lblBtnBuys) && !lblBtnBuys.equals(selected)) {
            lblBtnBuys.setBackground(exitColor);
        }
        else if (source.equals(lblBtnSale) && !lblBtnSale.equals(selected)) {
            lblBtnSale.setBackground(exitColor);
        }
        else if (source.equals(lblBtnProviders) && !lblBtnProviders.equals(selected)) {
            lblBtnProviders.setBackground(exitColor);
        }
        else if (source.equals(lblBtnSettings) && !lblBtnSettings.equals(selected)) {
            lblBtnSettings.setBackground(exitColor);
        }
        else if (source.equals(lblBtnInputProducts) && !lblBtnInputProducts.equals(selected)) {
            lblBtnInputProducts.setBackground(exitColor);
        }
        else if (source.equals(lblBtnSalesHistory) && !lblBtnSalesHistory.equals(selected)) {
            lblBtnSalesHistory.setBackground(exitColor);
        }
    }
    
}
