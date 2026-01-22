package controller;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import model.Product;
import model.daos.ProductDAO;
import view.ErrorModal;
import view.ProductsPanel;
import java.sql.Date;
import model.daos.ProviderDAO;
import view.SuccessModal;

import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import model.Percentage;
import model.daos.PercentageDAO;
import view.QuestionModal;
import view.TableDetailsModal;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;
import view.NameFileModal;
/**
 *
 * @author EmanuelHurt
 */
public class ProductController extends MouseAdapter {
    
    private ProductsPanel productsPanel;
    
    private final ProductDAO productDAO;
    private ProviderDAO providerDAO;
    private final PercentageDAO percentageDAO;
    
    private ArrayList<Product> productsList;
    private String nameToExportFile;
    
    public ProductController(ProductDAO productDAO, ProviderDAO proviDAO, PercentageDAO percenDAO) {
        this.productDAO = productDAO;
        this.providerDAO = proviDAO;
        percentageDAO = percenDAO;
        nameToExportFile = "";
        productsList = (ArrayList) this.productDAO.alphabeticalList();
    }
    
    public void setProductsPanel(ProductsPanel productsPanel) {
        this.productsPanel = productsPanel;
        
        productsPanel.getLblBtnExport().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String path = getFilePath();
                if (path.length() > 0) {

                    try {
                        NameFileModal nameModal = new NameFileModal(ProductController.this);
                        if (nameToExportFile != "") {
                            exportToExcel(path);
                            SuccessModal modal = new SuccessModal("Archivo exportado exitosamente");
                            nameToExportFile = "";
                        }
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                        ErrorModal modal = new ErrorModal("Error al exportar el archivo");
                    }
                }
            }
        });
        
        productsPanel.getLblBtnSearch().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                makeSearch();
            }
        });
        showProducts();
        putTableListening();
    }
    //LLENA LA TABLA EN ORDEN ALFABÉTICO
    private void showProducts() {
        //VACIAR LA TABLA PREVIAMENTE
        DefaultTableModel modelProductsTable = (DefaultTableModel)productsPanel.getProductsTable().getModel();
        if (modelProductsTable.getRowCount() > 0) {
            modelProductsTable.setRowCount(0);
        }
        //LLENAR LA TABLA CON LOS DATOS OBTENIDOS
        int idProduct;
        String codProd, nameProduct, genericName, nameProvider, pharmaForm, concentration, dueDate;
        double price;
        int existence;
        int n = 1;
        
        for (Product product : this.productsList) {          
            idProduct = product.getIdProduct();
            codProd = product.getCodProduct();
            nameProduct = product.getNameProduct();
            genericName = product.getGenericName();
            nameProvider = product.getNameProvider();
            pharmaForm = product.getPharmaForm();
            concentration = product.getConcentration();
            price = product.getPrice();
            existence = product.getExistence();
            dueDate = product.getDueDate().toString();
            String values[] = {n+"",codProd,nameProvider,nameProduct,genericName,pharmaForm,concentration,price+"",existence+"",dueDate};
            
            modelProductsTable.addRow(values);
            
            n++;
        }
    }
    private void putTableListening() {
        DefaultTableModel modelProductsTable = (DefaultTableModel)productsPanel.getProductsTable().getModel();
        modelProductsTable.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int nRow = e.getFirstRow();
                    
                    try {
                        String sPriceU = (String)modelProductsTable.getValueAt(nRow, 7);
                        double price = Double.parseDouble(sPriceU);
                
                        String strExistence = (String)modelProductsTable.getValueAt(nRow, 8);
                        int existence = Integer.parseInt(strExistence);
                        
                        String sDueDate = (String)modelProductsTable.getValueAt(nRow, 9);
                        Date dueDate = Date.valueOf(sDueDate);
                        
                        String providerName = (String)modelProductsTable.getValueAt(nRow, 2);
                        int idProvider = providerDAO.readProvider(providerName).getIdProvider();
                        if (idProvider > 0) {
                            //SE DEBEN ACTUALIZAR LOS DATOS
                            //String cod = (String)modelProductsTable.getValueAt(nRow, 1);
                            String productName = (String)modelProductsTable.getValueAt(nRow, 3);
                            String genericName = (String)modelProductsTable.getValueAt(nRow, 4);
                            String pharmaForm = (String)modelProductsTable.getValueAt(nRow, 5);
                            String concentration = (String)modelProductsTable.getValueAt(nRow, 6);
                            //ACTUALIZAR PRODUCTO EN EL ARRAYLIST
                            Product product = productsList.get(nRow);
                            product.setNameProduct(productName);
                            product.setNameProvider(providerName);
                            product.setIdProvider(idProvider);
                            product.setGenericName(genericName);
                            product.setPharmaForm(pharmaForm);
                            product.setConcentration(concentration);
                            product.setDueDate(dueDate);
                            product.setPrice(price);
                            product.setExistence(existence);
                            //ACTUALIZAR EN LA BBDD
                            productDAO.updateProduct(product);
                            
                        } else {
                            ErrorModal modal = new ErrorModal("Proveedor: "+providerName+" es incorrecto.");
                        }
                        
                        
                    }catch(NumberFormatException ex) {
                        ErrorModal modal = new ErrorModal("Ingrese valores numéricos en los campos de cantidad.");
                    }
                    catch(IllegalArgumentException ex) {
                        ErrorModal modal = new ErrorModal("La Fecha debe mantener el formato AAAA-MM-DD");
                    }
                }
            }
        });
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        JLabel source = (JLabel)e.getSource();
        
        if (source.equals(productsPanel.getLblBtnExport())) {
            productsPanel.getLblBtnExport().setBackground(new Color(127,179,213));
        }
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        JLabel source = (JLabel)e.getSource();
        
        if (source.equals(productsPanel.getLblBtnExport())) {
            productsPanel.getLblBtnExport().setBackground(Color.WHITE);
        }
    }
    
    public void showDetailsModal() {
        DefaultTableModel model = (DefaultTableModel)productsPanel.getProductsTable().getModel();
        //7 (precio u) y 8 (saldo)
        int numRows = model.getRowCount();
        
        Percentage percentage = percentageDAO.readPercentage(1);
        double salePercentage = percentage.getSalePercentage();
        
        double totalCost = 0.0;
        int saldoTotal = 0;
        
        for (int row = 0; row < numRows; row++) {
            String sPrice = (String)model.getValueAt(row, 7);
            String sSaldo = (String)model.getValueAt(row, 8);
            
            double priceU = Double.parseDouble(sPrice);
            int saldo = Integer.parseInt(sSaldo);
            
            //quitamos el % de venta para obtener le costo unitario
            double costU = priceU - (priceU * salePercentage/100.0); //costo Unitario
            
            double rowCost = costU * saldo;
            
            totalCost = totalCost + rowCost;
            saldoTotal = saldoTotal + saldo;
        }
        
        totalCost = Math.round(totalCost * Math.pow(10, 2)) / Math.pow(10, 2);
        
        //MOSTRAR LOS RESULTADOS EN EL MODAL DE DETALLES
        TableDetailsModal detailsModal = new TableDetailsModal();
        DefaultTableModel detailsModel = (DefaultTableModel)detailsModal.getTblDetails().getModel();
        String results[] = {saldoTotal+"", totalCost+""};
        detailsModel.addRow(results);
        detailsModal.show();
    }
    
    private String getFilePath() {
        String path = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        FileNameExtensionFilter filtroTxt = new FileNameExtensionFilter("Archivo de Excel (.xlsx)", "xlsx");
        fileChooser.setFileFilter(filtroTxt);
        int result = fileChooser.showOpenDialog(null);
        if (result  == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            path = selectedFile.getAbsolutePath();
        }
        return path;
    }
    
    private void exportToExcel(String filePath) throws IOException {
        // Crear un nuevo libro de trabajo
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Datos");

        // Obtener el modelo de la tabla
        TableModel model = productsPanel.getProductsTable().getModel();

        // Crear la fila para los encabezados
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < model.getColumnCount(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(model.getColumnName(i));
        }

        // Agregar las filas de datos
        for (int row = 0; row < model.getRowCount(); row++) {
            Row dataRow = sheet.createRow(row + 1);
            for (int col = 0; col < model.getColumnCount(); col++) {
                Cell cell = dataRow.createCell(col);
                Object value = model.getValueAt(row, col);
                if (value != null) {
                    if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else {
                        cell.setCellValue(value.toString());
                    }
                } else {
                    cell.setCellValue("");
                }
            }
        }

        // Guardar el archivo
        try {
            FileOutputStream fileOut = new FileOutputStream(filePath+"/"+nameToExportFile+".xlsx");
            workbook.write(fileOut);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        // Cerrar el libro de trabajo
        workbook.close();
    } 
    
    public void setNameToExportFile(String nameToFile) {
        nameToExportFile = nameToFile;
    }
    
    private void makeSearch() {
        String searchedName = productsPanel.getTxtFieldNameProduct().getText();
        if (!searchedName.isEmpty()) {
            searchedName = searchedName.trim();
            searchedName = searchedName.toUpperCase();
            //ArrayList<Product> products = (ArrayList)productDAO.listByNameProduct(searchedName);
            List<Integer> indexes = matchName(searchedName);
            if (indexes.size() > 0) {
                JTable table = this.productsPanel.getProductsTable();
                table.setRowSelectionInterval(indexes.get(0), indexes.get(indexes.size()-1));
                table.scrollRectToVisible(table.getCellRect(indexes.get(0), 0, true));
            } else {
                QuestionModal modal = new QuestionModal("Sin coincidencias para "+searchedName+" en los registros.");
            }
            
        } else {
            QuestionModal modal = new QuestionModal("Ingrese nombre para la búsqueda.");
        }
    }
    
    private List<Integer> matchName(String searchedName) {
        
        return this.productsList.stream()
                .filter(product -> product.getNameProduct().contains(searchedName))
                .map(productSelect -> productsList.indexOf(productSelect))
                .toList();               
    }
}
