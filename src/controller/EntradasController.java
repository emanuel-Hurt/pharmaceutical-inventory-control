package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import model.Purchase;
import model.daos.ProductDAO;
import model.daos.ProviderDAO;
import model.daos.PurchaseDAO;
import view.EntradasPanel;
import view.ErrorModal;
import view.QuestionModal;
import view.SuccessModal;

/**
 *
 * @author EmanuelHurt
 */
public class EntradasController {
    
    private EntradasPanel entradasPanel;
    
    private ProductDAO productDAO;
    private ProviderDAO providerDAO;
    private PurchaseDAO purchaseDAO;
    private ArrayList<Purchase> purchases;
    
    public EntradasController(ProductDAO prodao, ProviderDAO provdao, PurchaseDAO purchdao) {
        productDAO = prodao;
        providerDAO = provdao;
        purchaseDAO = purchdao;
    }
    
    public void setEntradasPanel(EntradasPanel panel) {
        entradasPanel = panel;
        
        purchases = (ArrayList)purchaseDAO.getAllPurchases();
        fillPurchasesTable();
        
        entradasPanel.getLblBtnDelete().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int rowSelected = entradasPanel.getTblPurchases().getSelectedRow();
                if (rowSelected != -1) {
                    int idPurchase = purchases.get(rowSelected).getIdPurchase();
                    if (purchaseDAO.deletePurchase(idPurchase)) {
                        purchases.remove(rowSelected);
                        SuccessModal modal = new SuccessModal("Registro de compra eliminado exitosamente.");
                        fillPurchasesTable();
                    } else {
                        ErrorModal modal = new ErrorModal("Error de conexión, contactar con el desarrollador");
                    }
                } else {
                    QuestionModal modal = new QuestionModal("Debe seleccionar una fila");
                }
            }
        });
    }
    private void fillPurchasesTable() {
        DefaultTableModel model = (DefaultTableModel)entradasPanel.getTblPurchases().getModel();
        
        if (model.getRowCount() > 0) {
            model.setRowCount(0);
        }
        
        int i = 1;
        //double totalCost = 0.0;
        for (Purchase purchase : purchases) {
            String row[] = {
                            i+"",
                            purchase.getNameProvider(),
                            purchase.getNameProduct(),
                            purchase.getQuantity()+"",
                            purchase.getCost()+"",
                            purchase.getPurchaseDate()+""
                            };
            model.addRow(row);
            //totalCost = totalCost + purchase.getCost();
            i++;
        }
        //totalCost = Math.round(totalCost * Math.pow(10, 2)) / Math.pow(10, 2);
        //entradasPanel.getLblTotalCost().setText(totalCost+"");
    }
}
