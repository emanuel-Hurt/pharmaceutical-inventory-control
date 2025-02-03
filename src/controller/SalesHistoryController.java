package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Product;
import model.ProductSale;
import model.SaleDetail;
import model.daos.*;
import view.ErrorModal;
import view.QuestionModal;
import view.SaleDetailModal;
import view.SalesHistoryPanel;
import view.SuccessModal;
/**
 *
 * @author EmanuelHurt
 */
public class SalesHistoryController {
    
    private ProductDAO productDAO;
    private ProductSaleDAO productSaleDAO;
    private SaleDetailDAO saleDetailDAO;
    
    private SalesHistoryPanel salesHistoryPanel;
    
    private ArrayList<SaleDetail> salesDetailsList;
    
    public SalesHistoryController(ProductDAO prodDAO, ProductSaleDAO prodSaleDAO, SaleDetailDAO saleDetDAO) {
        productDAO = prodDAO;
        productSaleDAO = prodSaleDAO;
        saleDetailDAO = saleDetDAO;
    }
    
    public void setSalesHistoryPanel(SalesHistoryPanel panel) {
        salesHistoryPanel = panel;
        //OBTENER LOS DETALLES DE VENTA
        salesDetailsList = (ArrayList)saleDetailDAO.getSalesDetails();
        //MOSTRAR LOS DATOS EN LA TABLA
        fillSaleDetailTable();
        //PONER A LA ESCUCHA LA TABLA
        salesHistoryPanel.getTblSalesHistory().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JTable table = salesHistoryPanel.getTblSalesHistory();
                if (e.getClickCount() == 2) {
                    int rowSelected = table.getSelectedRow();
                    if (rowSelected != -1) {
                        showSaleDetailModal(rowSelected);
                    }
                }
            }
        });
        
        salesHistoryPanel.getLblBtnDelete().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int rowSelected = salesHistoryPanel.getTblSalesHistory().getSelectedRow();
                if (rowSelected > -1) {
                    int idSaleDetail = salesDetailsList.get(rowSelected).getIdSaleDetail();
                    productSaleDAO.deleteProductSalesForIdSaleDetail(idSaleDetail);
                    if (saleDetailDAO.deleteSalesDetail(idSaleDetail)) {
                        salesDetailsList.remove(rowSelected);
                        SuccessModal modal = new SuccessModal("Detalle de Venta eliminado");
                        fillSaleDetailTable();
                    } else {
                        ErrorModal modal = new ErrorModal("Error de conexión, contactar con el desarrollador.");
                    }
                    
                } else {
                    QuestionModal modal = new QuestionModal("Debe seleccionar una fila.");
                }
            }
        });
    }
    private void showSaleDetailModal(int row) {
        int idSaleDetail = salesDetailsList.get(row).getIdSaleDetail();
        ArrayList<ProductSale> productSaleList = (ArrayList)productSaleDAO.productSaleListByIdSaleDetail(idSaleDetail);
        SaleDetailModal saleDetailModal = new SaleDetailModal();
        DefaultTableModel model = (DefaultTableModel)saleDetailModal.getTblSaleDetail().getModel();
        for (ProductSale productSale : productSaleList) {
            //proveedor, producto, forma, unidades, monto
            Product product = productDAO.getProduct(productSale.getIdProduct());
            
            String data[] = {
                product.getNameProvider(),
                product.getNameProduct(),
                product.getPharmaForm(),
                productSale.getQuantity()+"",
                product.getPrice()+""
            };
            
            model.addRow(data);
        }

        DefaultTableModel historyModel = (DefaultTableModel)salesHistoryPanel.getTblSalesHistory().getModel();
        
        saleDetailModal.getLblTotalMount().setText((String)historyModel.getValueAt(row, 5));
        saleDetailModal.getLblClient().setText((String)historyModel.getValueAt(row, 3));
        saleDetailModal.getLblDate().setText((String)historyModel.getValueAt(row, 1));
        saleDetailModal.getLblHour().setText((String)historyModel.getValueAt(row, 2));
        saleDetailModal.showSaleDetailModal();
    }
    
    private void fillSaleDetailTable() {
        DefaultTableModel model = (DefaultTableModel)salesHistoryPanel.getTblSalesHistory().getModel();
        
        if (model.getRowCount() > 0) {
            model.setRowCount(0);
        }
        
        int n = 1;
        for (SaleDetail salesDetail : salesDetailsList) {
            String row[] = {
                n+"",
                salesDetail.getDateSale()+"",
                salesDetail.getHourSale()+"",
                salesDetail.getNameClient(),
                salesDetail.getDescription(),
                salesDetail.getTotalPrice()+""
            };
            n++;
            model.addRow(row);
        }
    }
}
