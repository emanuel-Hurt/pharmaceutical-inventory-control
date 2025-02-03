package controller;

import model.daos.PurchaseDAO;
import view.ProductRegisterPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import model.daos.PercentageDAO;
import model.daos.ProductDAO;
import model.daos.ProviderDAO;
import view.InitialWindow;
import view.OptionsPurchasePanel;
import view.ProductRestockPanel;
/**
 *
 * @author EmanuelHurt
 */
public class PurchaseController extends MouseAdapter {
    
    private final ProductDAO productDAO;
    private final ProviderDAO providerDAO;
    private final PercentageDAO percentageDAO;
    private final PurchaseDAO purchaseDAO;
    
    private OptionsPurchasePanel optionsPurchasePanel; //su vista principal
    
    private ProductRestockController productRestockController;
    private ProductRegisterController productRegisterController;
    
    private final InitialWindow iniWindow;
    
    public PurchaseController(PurchaseDAO purchaseDAO, ProductDAO productDAO, ProviderDAO provDAO, PercentageDAO percenDAO, InitialWindow iniWindow) {
        this.productDAO = productDAO;
        this.providerDAO = provDAO;
        this.percentageDAO = percenDAO;
        this.purchaseDAO = purchaseDAO;
        this.iniWindow = iniWindow;
        
        productRestockController = new ProductRestockController(productDAO, iniWindow, this);
        productRegisterController = new ProductRegisterController(providerDAO, this.productDAO, percentageDAO, this.purchaseDAO);
    }
    //COLOCADO DEL PANEL PARA LA ELECCION DEL TIPO DE REGISTRO QUE SE VA A HACER
    public void setOptionsPurchasePanel(OptionsPurchasePanel optionsPurchasePanel) {
        this.optionsPurchasePanel = optionsPurchasePanel;
        this.optionsPurchasePanel.getLblBtnRestock().addMouseListener(this);
        this.optionsPurchasePanel.getLblBtnNewProduct().addMouseListener(this);
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
        Object eventSource = e.getSource();
        //CONTROLA EL PANEL DE SELECCION DE RESTOCK O NUEVO PRODUCTO
        if (optionsPurchasePanel != null) {
            //HABILITAMOS EL PANEL REGISTRAR NUEVO PRODUCTO
            if (eventSource.equals(optionsPurchasePanel.getLblBtnNewProduct())) {
                ProductRegisterPanel productRegisterPanel = new ProductRegisterPanel(false);
                productRegisterController.setProductRegisterPanel(productRegisterPanel, true);
                iniWindow.changeCenterPanel(productRegisterPanel);
                //LLEVAR LOS DEMAS PANELES A NULL
                optionsPurchasePanel = null;
            }
            //HABILITAMOS EL PANEL REGISTRAR RESTOCK
            else if (eventSource.equals(optionsPurchasePanel.getLblBtnRestock())) {
                //crear panel para buscar el producto a reponer
                ProductRestockPanel restockPanel = new ProductRestockPanel();
                productRestockController.setProductRestockPanel(restockPanel);
                iniWindow.changeCenterPanel(restockPanel);
                //DESHABILITAR LOS OTROS DOS PANELES
                optionsPurchasePanel = null;
            }
        }
    }
    
    //METODO LLAMADO DESDE EL CONTROLADOR DE RESTOCK
    public void productRestockRegisterPanel(DefaultTableModel tableModel, int iRow) {
        ProductRegisterPanel productRegisterPanel = new ProductRegisterPanel(true);
        //PONE TODOS LOS ELEMENTOS DE ESTE PANEL A LA ESCUCHA
        productRegisterController.setProductRegisterPanel(productRegisterPanel, false);
        //LLENA LOS CAMPOS DEL PANEL CON LOS DATOS DEL PRODUCTO SELECCIONADO
        productRegisterPanel.getFieldProductName().setText(tableModel.getValueAt(iRow, 2)+"");
        productRegisterPanel.getFieldGenericName().setText(tableModel.getValueAt(iRow, 3)+"");
        productRegisterPanel.getFieldPharmaForm().setText(tableModel.getValueAt(iRow, 4)+"");
        productRegisterPanel.getFieldConcentration().setText(tableModel.getValueAt(iRow, 5)+"");
        productRegisterPanel.getFieldCodProduct().setText(tableModel.getValueAt(iRow, 6)+"");
        
        productRegisterPanel.getFieldProductName().setEditable(false);
        productRegisterPanel.getFieldGenericName().setEditable(false);
        productRegisterPanel.getFieldPharmaForm().setEditable(false);
        productRegisterPanel.getFieldConcentration().setEditable(false);
        productRegisterPanel.getFieldCodProduct().setEditable(false);
        
        //LLENADO DE DATOS PARA EL COMBOBOX PROVIDER
        DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
        String providerName = tableModel.getValueAt(iRow, 1)+"";
        comboModel.addElement(providerName);
        productRegisterPanel.getComBoxProvider().setModel(comboModel);
        
        iniWindow.changeCenterPanel(productRegisterPanel);
    }
    
}
