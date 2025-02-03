package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import model.Percentage;
import model.Product;
import model.Provider;
import model.Purchase;
import model.daos.PercentageDAO;
import model.daos.ProductDAO;
import model.daos.ProviderDAO;
import model.daos.PurchaseDAO;
import view.ErrorModal;
import view.ProductRegisterPanel;
import view.SuccessModal;

/**
 *
 * @author EmanuelHurt
 */
public class ProductRegisterController extends MouseAdapter implements ActionListener {
    
    private ProductRegisterPanel productRegisterPanel;
    
    private ProviderDAO providerDAO;
    private ProductDAO productDAO;
    private PercentageDAO percentageDAO;
    private PurchaseDAO purchaseDAO;
    
    public ProductRegisterController(ProviderDAO provDAO, ProductDAO prodDAO, PercentageDAO percenDAO, PurchaseDAO purDAO) {
        this.providerDAO = provDAO;
        this.productDAO = prodDAO;
        this.percentageDAO = percenDAO;
        this.purchaseDAO = purDAO;
    }
    
    //COLOCADO DEL PANEL PARA EL REGISTRO DEL PRODUCTO
    public void setProductRegisterPanel(ProductRegisterPanel purchasePanel, boolean loadComboBox) {
        
        this.productRegisterPanel = purchasePanel;
        
        this.productRegisterPanel.getLblBtnAcept().addMouseListener(this);
        this.productRegisterPanel.getBtnOk().addActionListener(this);
        
        this.productRegisterPanel.getLblUnitCost().setText("");
        
        if (loadComboBox) {
            //CREACION Y CARGADO DE VALORES AL MODELO DE COMBOBOX
            DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
            ArrayList<Provider> providers = (ArrayList<Provider>)providerDAO.providersList();
            for (Provider provider : providers) {
                comboModel.addElement(provider.getName());
            }
            //CARGAR MODELO AL COMBOBOX DEL PANEL
            this.productRegisterPanel.getComBoxProvider().setModel(comboModel);
        }
        
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        Object eventSource = e.getSource();
        //CONTROLA EL PANEL DE REGISTRO DE PRODUCTO
        if (productRegisterPanel != null) {
            if (eventSource.equals(productRegisterPanel.getLblBtnAcept())) {
                if (allFull()) {
                    Product product = new Product();
                    prepareProduct(product);
                    //GUARDAR EL PRODUCTO
                    if (productRegisterPanel.updateProduct()) { //reposicion
                        
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
        String purchaseDate = productRegisterPanel.getFieldPurchaseDate().getText();
        
        //OBTENCION DE LOS DATOS DE LA COMPRA DEL PRODUCTO RESPECTIVO
        String nameProduct = productRegisterPanel.getFieldProductName().getText();
        String pharmaForm = productRegisterPanel.getFieldPharmaForm().getText();
        String concentration = productRegisterPanel.getFieldConcentration().getText();
        String dueDate = productRegisterPanel.getFieldDueDate().getText();
        String strQuantity = productRegisterPanel.getFieldQuantity().getText();
        String strTotalCostProduct = productRegisterPanel.getFieldTotalCostProduct().getText();
        String strUnitCost = productRegisterPanel.getLblUnitCost().getText();
        String strUnitPrice = productRegisterPanel.getFieldUnitPrice().getText();
        String codProduct = productRegisterPanel.getFieldCodProduct().getText();
        String genericName = productRegisterPanel.getFieldGenericName().getText();
        
        if (!purchaseDate.isEmpty() && !nameProduct.isEmpty() &&
              !pharmaForm.isEmpty() && !concentration.isEmpty() && !dueDate.isEmpty() &&
              !strQuantity.isEmpty() && !strTotalCostProduct.isEmpty() && !strUnitCost.isEmpty()
              && !strUnitPrice.isEmpty() && !codProduct.isEmpty() && !genericName.isEmpty()
                && !purchaseDate.isEmpty()) {
            
            resp = true;
        }
        
        return resp;
    }
    
    private void prepareProduct(Product product) {
        product.setNameProduct(productRegisterPanel.getFieldProductName().getText());
        product.setGenericName(productRegisterPanel.getFieldGenericName().getText());
        product.setPharmaForm(productRegisterPanel.getFieldPharmaForm().getText());
        product.setConcentration(productRegisterPanel.getFieldConcentration().getText());

        Date dueDate = Date.valueOf(productRegisterPanel.getFieldDueDate().getText());
        product.setDueDate(dueDate);

        double unitPrice = Double.parseDouble(productRegisterPanel.getFieldUnitPrice().getText());

        product.setPrice(unitPrice);


        int existence = Integer.parseInt(productRegisterPanel.getFieldQuantity().getText());
        product.setExistence(existence);

        product.setCodProduct(productRegisterPanel.getFieldCodProduct().getText());
        product.setNumLote(productRegisterPanel.getFieldNumLote().getText());

        //obtencion del idProvider
        String nameProvider = (String)productRegisterPanel.getComBoxProvider().getSelectedItem();               
        Provider provider = providerDAO.readProvider(nameProvider);
        int idProvider = provider.getIdProvider();
        product.setIdProvider(idProvider);
    }
    
    private void preparePurchase(Purchase purchase, int idProduct) {
        int quantity = Integer.parseInt(productRegisterPanel.getFieldQuantity().getText());
        purchase.setQuantity(quantity);
        double totalCost = Double.parseDouble(productRegisterPanel.getFieldTotalCostProduct().getText());
        purchase.setCost(totalCost);
        purchase.setIdProduct(idProduct);
        Date purchaseDate = Date.valueOf(productRegisterPanel.getFieldPurchaseDate().getText());
        purchase.setPurchaseDate(purchaseDate);
        purchase.setDetail(productRegisterPanel.getAreaDescription().getText());
    }
    
    //LIMPIAR LOS CAMPOS DEL PANEL DE REGISTRO DE PRODUCTO (ProductRegisterPanel)
    private void clearPurchaseNewProductPanel() {
        //OBTENCION DE LOS DATOS DE LA COMPRA DEL PRODUCTO RESPECTIVO
        productRegisterPanel.getFieldProductName().setText("");
        productRegisterPanel.getFieldPharmaForm().setText("");
        productRegisterPanel.getFieldConcentration().setText("");
        productRegisterPanel.getFieldDueDate().setText("");
        productRegisterPanel.getFieldQuantity().setText("");
        productRegisterPanel.getFieldTotalCostProduct().setText("");
        productRegisterPanel.getLblUnitCost().setText("");
        productRegisterPanel.getFieldUnitPrice().setText("");
        productRegisterPanel.getFieldCodProduct().setText("");
        productRegisterPanel.getFieldNumLote().setText("");
    }
    
    public void actionPerformed(ActionEvent e) {
        Object eventSource = e.getSource();
        //CONTROLA LOS EVENTOS DEL PANEL PURCHASENEWPRODUCTPANEL
        if (productRegisterPanel != null) {
            //SE VERIFICA QUE EL EVENTO PROVENGA DEL BOTON BTNOK
            if (eventSource.equals(productRegisterPanel.getBtnOk())) {
                String strQuantity = productRegisterPanel.getFieldQuantity().getText();
                String strTotalCostProduct = productRegisterPanel.getFieldTotalCostProduct().getText();
               
                try {
                    double quantity = Double.parseDouble(strQuantity);
                    double totalCostProduct = Double.parseDouble(strTotalCostProduct);

                    double unitCost = totalCostProduct / quantity;                   
                    unitCost = Math.round(unitCost * Math.pow(10, 2)) / Math.pow(10, 2);

                    productRegisterPanel.getLblUnitCost().setText(unitCost + "");
                    
                    Percentage percentage = percentageDAO.readPercentage(1);                   
                    double salePercentage = percentage.getSalePercentage();
                    
                    double extraAmount = unitCost * (salePercentage / 100.0);
                    extraAmount = Math.round(extraAmount * Math.pow(10, 2)) / Math.pow(10, 2);
                    
                    double uPrice = unitCost + extraAmount;
                    uPrice = Math.round(uPrice * Math.pow(10, 2)) / Math.pow(10, 2);
                    
                    productRegisterPanel.getFieldUnitPrice().setText(uPrice + "");
                    
                    String sSalePercentage = "(+ " + salePercentage + " % para venta)";
                    productRegisterPanel.getLblSalePercentage().setText(sSalePercentage);
                    
                }catch(NumberFormatException ex) {
                    ErrorModal erroModal = new ErrorModal("Use valores númericos para los campos cantidad y costos");
                }
            }           
        }
    }
}
