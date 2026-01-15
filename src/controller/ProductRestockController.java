package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import model.Product;
import model.daos.ProductDAO;
import view.InitialWindow;
import view.InputTypePanel;
import view.ProductRegistrationPanel;
import view.SelectRestockPanel;
import view.QuestionModal;

/**
 *
 * @author EmanuelHurt
 */
public class ProductRestockController {
    
    private InitialWindow iniWindow;
    private SelectRestockPanel productRestockPanel;
    private ProductDAO productDAO;
    private PurchaseController purchaseController;
    
    public ProductRestockController(ProductDAO productDAO, InitialWindow iniWin, PurchaseController purController) {
        iniWindow = iniWin;
        this.productDAO = productDAO;
        purchaseController = purController;
    }
    
    //COLOCADO DEL PANEL QUE CONTIENE LA TABLA PARA LA SELECCION DEL PRODUCTO A REPONER
    public void setProductRestockPanel(SelectRestockPanel restockPanel) {
        this.productRestockPanel = restockPanel;
        //PONER A LA ESCUCHA LOS ELEMENTOS DEL PANEL
        this.productRestockPanel.getLblBtnSearch().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                makeSearch();
            }
        });
        this.productRestockPanel.getTableProducts().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    registerRestock();
                }
            }
        });
        
        //LLENAR LA TABLA DE PRODUCTOS       
        DefaultTableModel modelProductsTable = (DefaultTableModel)this.productRestockPanel.getTableProducts().getModel();
        
        ArrayList<Product> productsList = (ArrayList<Product>)productDAO.listById();
        
        int cont = 1;
        
        for (Product product : productsList) {

            String values[] = {
                cont+"", 
                product.getNameProvider(),
                product.getNameProduct(), 
                product.getGenericName(), 
                product.getPharmaForm(),
                product.getConcentration(), 
                product.getCodProduct(),
                product.getExistence()+""
            };

            modelProductsTable.addRow(values);
            
            cont++;
        }
        
        //FUNCIONALIDAD DEL BOTON "ATRÁS"
        this.productRestockPanel.getBtnLblBack().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                InputTypePanel inputTypePanel = new InputTypePanel();
                purchaseController.setOptionsPurchasePanel(inputTypePanel);
                iniWindow.changeCenterPanel(inputTypePanel);
            }
        });
    }
    
    private void makeSearch() {
        String nameProduct = productRestockPanel.getFieldNameProduct().getText(); 
        if (!nameProduct.isEmpty()) {
            //VACIAR LA TABLA PREVIAMENTE (SI ESTA LLENA)      
            DefaultTableModel modelProductsTable = (DefaultTableModel)productRestockPanel.getTableProducts().getModel();
            if (modelProductsTable.getRowCount() > 0) {
                modelProductsTable.setRowCount(0);
            }
            //OBTENEMOS LOS PRODUCTOS QUE COINCIDAN CON EL NOMBRE INGRESADO
            ArrayList<Product> productsList = (ArrayList<Product>) productDAO.listByNameProduct(nameProduct);
            if (!productsList.isEmpty()) {
                //LLENAMOS LA TABLA
                int count = 1;
                for (Product product : productsList) {

                    String values[] = {
                        count+"", 
                        product.getNameProvider(),
                        product.getNameProduct(),
                        product.getGenericName(), 
                        product.getPharmaForm(),
                        product.getConcentration(), 
                        product.getCodProduct(),
                        product.getExistence()+""
                    };

                    modelProductsTable.addRow(values);

                    count ++;
                }
            }
            else {                                            
                QuestionModal modal = new QuestionModal("No hay resultados para el producto " + nameProduct);                       
            }                   
        } else {
            QuestionModal modal = new QuestionModal("Ingrese el nombre o las iniciales de un producto.");
        }
    }
    
    private void registerRestock() {
        int iRow = productRestockPanel.getTableProducts().getSelectedRow();
                
        if (iRow != -1) { //controla de que haya una fila seleccionada
            DefaultTableModel tableModel = (DefaultTableModel)productRestockPanel.getTableProducts().getModel();

            //DEJAMOS QUE EL CONTROLADOR PADRE SE ENCARGUE
            purchaseController.productRestockRegisterPanel(tableModel,iRow);
        } else {
            QuestionModal questionModal = new QuestionModal("Seleccione una fila para realizar el Restock");
        }
    }
    
}
