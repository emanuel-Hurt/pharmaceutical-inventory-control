package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import model.Percentage;
import model.Product;
import model.Provider;
import model.Purchase;
import model.daos.PercentageDAO;
import model.daos.ProductDAO;
import model.daos.ProviderDAO;
import model.daos.PurchaseDAO;
import resources.DoubleVerifier;
import resources.IntVerifier;
import view.ErrorModal;
import view.InitialWindow;
import view.InputTypePanel;
import view.ProductRegistrationPanel;
import view.ProductRegistrationPanel;
import view.PurchaseHistoryPanel;
import view.SelectRestockPanel;
import view.SuccessModal;

/**
 *
 * @author EmanuelHurt
 */
public class ProductRegisterController extends MouseAdapter implements ActionListener {
    
    private ProductRegistrationPanel productRegistrationPanel;
    
    private ProviderDAO providerDAO;
    private ProductDAO productDAO;
    private PercentageDAO percentageDAO;
    private PurchaseDAO purchaseDAO;
    
    private InitialWindow iniWindow;
    
    public ProductRegisterController(ProviderDAO provDAO, ProductDAO prodDAO, PercentageDAO percenDAO, PurchaseDAO purDAO, InitialWindow iniWindow) {
        this.providerDAO = provDAO;
        this.productDAO = prodDAO;
        this.percentageDAO = percenDAO;
        this.purchaseDAO = purDAO;
        
        this.iniWindow = iniWindow;
    }
    
    //COLOCADO DEL PANEL PARA EL REGISTRO DEL PRODUCTO
    public void setProductRegisterPanel(ProductRegistrationPanel registrationPanel, boolean loadComboBox) {
        
        this.productRegistrationPanel = registrationPanel;
        
        this.productRegistrationPanel.getLblBtnAcept().addMouseListener(this);
        this.productRegistrationPanel.getBtnOk().addActionListener(this);
        
        this.productRegistrationPanel.getLblUnitCost().setText("");
        
        if (loadComboBox) {
            //CREACION Y CARGADO DE VALORES AL MODELO DE COMBOBOX
            DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
            ArrayList<Provider> providers = (ArrayList<Provider>)providerDAO.providersList();
            for (Provider provider : providers) {
                comboModel.addElement(provider.getName());
            }
            //CARGAR MODELO AL COMBOBOX DEL PANEL
            this.productRegistrationPanel.getComBoxProvider().setModel(comboModel);
        }
        
        //COLOCACION DE VALIDADORES A LOS CAMPOS PARA VALORES NUMERICOS
        JTextField quantityField = productRegistrationPanel.getFieldQuantity();
        JTextField totalCostField = this.productRegistrationPanel.getFieldTotalCostProduct();
        JTextField unitPriceField = this.productRegistrationPanel.getFieldUnitPrice();
        
        JLabel errorLbl = this.productRegistrationPanel.getErrorLbl();
        
        quantityField.setInputVerifier(new IntVerifier(quantityField.getBorder(), errorLbl));
        totalCostField.setInputVerifier(new DoubleVerifier(totalCostField.getBorder(), errorLbl));
        unitPriceField.setInputVerifier(new DoubleVerifier(unitPriceField.getBorder(), errorLbl));
        
        this.productRegistrationPanel.getBtnLblBack().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                
                if (loadComboBox) {
                    InputTypePanel selectPurchasePanel = new InputTypePanel();
                    PurchaseController purchaseController = new PurchaseController(purchaseDAO,productDAO,providerDAO,percentageDAO,iniWindow);
                    purchaseController.setOptionsPurchasePanel(selectPurchasePanel);
                    iniWindow.changeCenterPanel(selectPurchasePanel);
                } else {
                    //crear panel para buscar el producto a reponer
                    SelectRestockPanel restockPanel = new SelectRestockPanel();
                    PurchaseController purchaseController = new PurchaseController(purchaseDAO,productDAO,providerDAO,percentageDAO,iniWindow);
                    ProductRestockController productRestockController = new ProductRestockController(productDAO, iniWindow, purchaseController);
                    productRestockController.setProductRestockPanel(restockPanel);
                    iniWindow.changeCenterPanel(restockPanel);
                }
                
            }
        });
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        Object eventSource = e.getSource();
        //CONTROLA EL PANEL DE REGISTRO DE PRODUCTO
        if (productRegistrationPanel != null) {
            if (eventSource.equals(productRegistrationPanel.getLblBtnAcept())) {
                if (allFull()) {
                    Product product = new Product();
                    if (prepareProduct(product)){
                        //GUARDAR EL PRODUCTO
                        if (productRegistrationPanel.updateProduct()) { //reposicion

                            Product oldProduct = productDAO.readProduct(product.getCodProduct());
                            int nowExistence = oldProduct.getExistence() + product.getExistence();
                            product.setExistence(nowExistence);

                            if (productDAO.updateProduct(product)) {
                                int idProduct = productDAO.readProduct(product.getCodProduct()).getIdProduct();
                                Purchase purchase = new Purchase();
                                preparePurchase(purchase,idProduct);
                                if (purchaseDAO.createPurchase(purchase)) {
                                    SuccessModal modal = new SuccessModal("Producto Registrado Exitosamente");
                                    clearPurchaseNewProductPanel();
                                } else {
                                    ErrorModal errorModal = new ErrorModal("Error al Registrar Compra de Producto");
                                }
                            }
                            else {
                                ErrorModal errorModal = new ErrorModal("Error al Actualizar el Producto");
                            }
                        }else { //crear nuevo
                            if (productDAO.verifyExistence(product)) {
                                ErrorModal errorModal = new ErrorModal("El Código de producto que intenta registrar ya existe");
                            } else {
                                int idProduct = productDAO.createProduct(product);
                                if (idProduct > 0) {
                                    Purchase purchase = new Purchase();
                                    preparePurchase(purchase, idProduct);
                                    if (purchaseDAO.createPurchase(purchase)) {
                                        SuccessModal modal = new SuccessModal("Producto Registrado Exitosamente");
                                        clearPurchaseNewProductPanel();
                                    } else {
                                        ErrorModal errorModal = new ErrorModal("Error al registrar el producto");
                                    }

                                }
                                else {
                                    ErrorModal errorModal = new ErrorModal("Error al Crear el Nuevo Producto");
                                }
                            }

                        }
                    } else {
                        ErrorModal erroModal = new ErrorModal("Use valores númericos para los campos cantidad y costos");
                    }
                    
                }
                else {
                    ErrorModal errorModal = new ErrorModal("Hay campos obligatorios que no fueron llenados");
                }
            }          
        }
    }
    
    private boolean allFull() {
        
        boolean resp = false;
        
        //OBTENCION DE LOS DATOS DEL DETALLE DE COMPRA
        String purchaseDate = productRegistrationPanel.getFieldPurchaseDate().getText();
        
        //OBTENCION DE LOS DATOS DE LA COMPRA DEL PRODUCTO RESPECTIVO
        String nameProduct = productRegistrationPanel.getFieldProductName().getText();
        String pharmaForm = productRegistrationPanel.getFieldPharmaForm().getText();
        String concentration = productRegistrationPanel.getFieldConcentration().getText();
        String dueDate = productRegistrationPanel.getFieldDueDate().getText();
        String strQuantity = productRegistrationPanel.getFieldQuantity().getText();
        String strTotalCostProduct = productRegistrationPanel.getFieldTotalCostProduct().getText();
        String strUnitCost = productRegistrationPanel.getLblUnitCost().getText();
        String strUnitPrice = productRegistrationPanel.getFieldUnitPrice().getText();
        String codProduct = productRegistrationPanel.getFieldCodProduct().getText();
        String genericName = productRegistrationPanel.getFieldGenericName().getText();
        
        if (!purchaseDate.isEmpty() && !nameProduct.isEmpty() &&
              !pharmaForm.isEmpty() && !concentration.isEmpty() && !dueDate.isEmpty() &&
              !strQuantity.isEmpty() && !strTotalCostProduct.isEmpty() && !strUnitCost.isEmpty()
              && !strUnitPrice.isEmpty() && !codProduct.isEmpty() && !genericName.isEmpty()
                && !purchaseDate.isEmpty()) {
            
            resp = true;
        }
        
        return resp;
    }
    
    private boolean prepareProduct(Product product) {
        try {
            product.setNameProduct(productRegistrationPanel.getFieldProductName().getText());
            product.setGenericName(productRegistrationPanel.getFieldGenericName().getText());
            product.setPharmaForm(productRegistrationPanel.getFieldPharmaForm().getText());
            product.setConcentration(productRegistrationPanel.getFieldConcentration().getText());

            Date dueDate = Date.valueOf(productRegistrationPanel.getFieldDueDate().getText());
            product.setDueDate(dueDate);

            double unitPrice = Double.parseDouble(productRegistrationPanel.getFieldUnitPrice().getText());

            product.setPrice(unitPrice);


            int existence = Integer.parseInt(productRegistrationPanel.getFieldQuantity().getText());
            product.setExistence(existence);

            product.setCodProduct(productRegistrationPanel.getFieldCodProduct().getText());
            product.setNumLote(productRegistrationPanel.getFieldNumLote().getText());

            //obtencion del idProvider
            String nameProvider = (String)productRegistrationPanel.getComBoxProvider().getSelectedItem();               
            Provider provider = providerDAO.readProvider(nameProvider);
            int idProvider = provider.getIdProvider();
            product.setIdProvider(idProvider);
            
            return true;
            
        }catch(NumberFormatException ex) {
            return false;
        }
    }
    
    private void preparePurchase(Purchase purchase, int idProduct) {
        int quantity = Integer.parseInt(productRegistrationPanel.getFieldQuantity().getText());
        purchase.setQuantity(quantity);
        double totalCost = Double.parseDouble(productRegistrationPanel.getFieldTotalCostProduct().getText());
        purchase.setCost(totalCost);
        purchase.setIdProduct(idProduct);
        Date purchaseDate = Date.valueOf(productRegistrationPanel.getFieldPurchaseDate().getText());
        purchase.setPurchaseDate(purchaseDate);
        purchase.setDetail(productRegistrationPanel.getAreaDescription().getText());
    }
    
    //LIMPIAR LOS CAMPOS DEL PANEL DE REGISTRO DE PRODUCTO (ProductRegisterPanel)
    private void clearPurchaseNewProductPanel() {
        //OBTENCION DE LOS DATOS DE LA COMPRA DEL PRODUCTO RESPECTIVO
        productRegistrationPanel.getFieldProductName().setText("");
        productRegistrationPanel.getFieldGenericName().setText("");
        productRegistrationPanel.getFieldPharmaForm().setText("");
        productRegistrationPanel.getFieldConcentration().setText("");
        productRegistrationPanel.getFieldDueDate().setText("");
        productRegistrationPanel.getFieldQuantity().setText("");
        productRegistrationPanel.getFieldTotalCostProduct().setText("");
        productRegistrationPanel.getLblUnitCost().setText("");
        productRegistrationPanel.getFieldUnitPrice().setText("");
        productRegistrationPanel.getFieldCodProduct().setText("");
        productRegistrationPanel.getFieldNumLote().setText("");
        productRegistrationPanel.getFieldPurchaseDate().setText("");
    }
    
    public void actionPerformed(ActionEvent e) {
        Object eventSource = e.getSource();
        //CONTROLA LOS EVENTOS DEL PANEL PURCHASENEWPRODUCTPANEL
        if (productRegistrationPanel != null) {
            //SE VERIFICA QUE EL EVENTO PROVENGA DEL BOTON BTNOK
            if (eventSource.equals(productRegistrationPanel.getBtnOk())) {
                String strQuantity = productRegistrationPanel.getFieldQuantity().getText();
                String strTotalCostProduct = productRegistrationPanel.getFieldTotalCostProduct().getText();
               
                try {
                    double quantity = Double.parseDouble(strQuantity);
                    double totalCostProduct = Double.parseDouble(strTotalCostProduct);

                    double unitCost = totalCostProduct / quantity;                   
                    unitCost = Math.round(unitCost * Math.pow(10, 2)) / Math.pow(10, 2);

                    productRegistrationPanel.getLblUnitCost().setText(unitCost + "");
                    
                    Percentage percentage = percentageDAO.readPercentage(1);                   
                    double salePercentage = percentage.getSalePercentage();
                    
                    double extraAmount = unitCost * (salePercentage / 100.0);
                    extraAmount = Math.round(extraAmount * Math.pow(10, 2)) / Math.pow(10, 2);
                    
                    double uPrice = unitCost + extraAmount;
                    uPrice = Math.round(uPrice * Math.pow(10, 2)) / Math.pow(10, 2);
                    
                    productRegistrationPanel.getFieldUnitPrice().setText(uPrice + "");
                    
                    String sSalePercentage = "(+ " + salePercentage + " % para venta)";
                    productRegistrationPanel.getLblSalePercentage().setText(sSalePercentage);
                    
                }catch(NumberFormatException ex) {
                    ErrorModal erroModal = new ErrorModal("Use valores númericos para los campos cantidad y costos");
                }
            }           
        }
    }
}
