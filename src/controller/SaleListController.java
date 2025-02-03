package controller;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author EmanuelHurt
 */
public class SaleListController extends MouseAdapter {
    
    private final JPanel productsSaleListPanel;
    private JLabel lblTotalAmount;
    private ProductSaleController productSaleController;
    
    public SaleListController(JPanel listPanel, JLabel lblTotalAmount, ProductSaleController parentController) {
        productsSaleListPanel = listPanel;      
        this.lblTotalAmount = lblTotalAmount;
        this.productSaleController = parentController;
    }
    
    //ESTE METODO ES USADO SOLO PARA ELIMINAR ELEMENTOS DEL PANEL DE LISTA DE VENTA
    @Override
    public void mouseClicked(MouseEvent e) {
        JLabel lblEliminate = (JLabel)e.getSource();
        JPanel panel = (JPanel)lblEliminate.getParent();
        int idx = productsSaleListPanel.getComponentZOrder(panel);
        
        //OBTENCION DEL MONTO GUARDADO HASTA EL MOMENTO
        double mount = productSaleController.getProductsSaleList().get(idx).getTotalPrice();
        
        //ACTUALIZACION DEL MONTO TOTAL Y PRESENTACION EN LA VISTA
        double totAmount = productSaleController.getTotalAmount();
        totAmount = totAmount - mount;
        totAmount = Math.round(totAmount * Math.pow(10, 2)) / Math.pow(10, 2);
        productSaleController.setTotalAmount(totAmount);
        String sMount = "Monto Total: Bs. ";
        lblTotalAmount.setText(sMount + totAmount);
        
        //ELIMINARLO DEL ARRAYLIST
        productSaleController.getProductsSaleList().remove(idx);
        //ELIMINARLO DE LA VISTA
        productsSaleListPanel.remove(panel);
        productsSaleListPanel.revalidate();
        productsSaleListPanel.repaint();
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        JLabel lblEliminate = (JLabel) e.getSource();
        lblEliminate.setBackground(Color.red);
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        JLabel lblEliminate = (JLabel) e.getSource();
        lblEliminate.setBackground(new Color(213, 245, 227));
    }
}
