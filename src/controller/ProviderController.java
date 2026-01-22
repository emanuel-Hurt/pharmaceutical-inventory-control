package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import model.Provider;
import model.daos.ProviderDAO;
import view.ErrorModal;
import view.ProvidersPanel;
import view.QuestionModal;
import view.SuccessModal;

/**
 *
 * @author EmanuelHurt
 */
public class ProviderController extends MouseAdapter {
    
    private final ProviderDAO providerDAO;
    private ProvidersPanel providersPanel;
    private JTable tableProviders;
    //private HashSet<Integer> editedRows;
    private ArrayList<Provider> providersList;
    
    public ProviderController(ProviderDAO provDAO) {
        providerDAO = provDAO;
        //this.editedRows = new HashSet<>();
        this.providersList = (ArrayList<Provider>)providerDAO.providersList();
    }
    
    public void setProvidersRegisterPanel(ProvidersPanel panel) {
        providersPanel = panel;
        //REGISTRAR NUEVOS PROVEEDORES
        providersPanel.getLblBtnRegister().addMouseListener(this);
        
        //CONTROL Y CAPTURA DE LOS CAMBIOS EN LA TABLA
        tableProviders = providersPanel.getProvidersTable();
        DefaultTableModel tableModel = (DefaultTableModel)this.tableProviders.getModel();
        tableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int nRow = e.getFirstRow();
                String strPhone = (String)tableModel.getValueAt(nRow, 3);
                String email = (String)tableModel.getValueAt(nRow, 2);
                if ((strPhone.matches("\\d+") || strPhone.isEmpty()) && (email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$") || email.isEmpty())) {
                    //ES VALIDO
                    String nameProvider = (String)tableModel.getValueAt(nRow, 1);
                   
                    int phone = Integer.parseInt(strPhone);
                    //ACTUALIZAR EL PROVEEDOR EN EL ARRAYLIST
                    Provider provider = providersList.get(nRow);
                    provider.setName(nameProvider);
                    provider.setEmail(email);
                    provider.setPhone(phone);
                    //ACTUALIZAR EL PROVEEDOR EN LA BBDD
                    providerDAO.updateProvider(provider);
                                      
                } else {
                    ErrorModal modal = new ErrorModal("Email y/o número de telf/cel incorrecto");
                }
            }
        });
        
        //ELIMINAR PROVEEDORES
        providersPanel.getLblBtnDeleteProvider().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                int[] selectedRows = tableProviders.getSelectedRows();
                if (selectedRows.length != 0) {
                    Arrays.stream(selectedRows).forEach(rowIdx -> {
                        providerDAO.deleteProvider(providersList.get(rowIdx));
                        providersList.remove(rowIdx);
                        updateProvidersTable();
                    });
                } else {
                    ErrorModal modal = new ErrorModal("Debe seleccionar la(s) fila(s) que desea eliminar");
                }
            }
        });
        
        updateProvidersTable();
    }
    
    private void updateProvidersTable() {
        //VACIAR LA TABLA PREVIAMENTE
        DefaultTableModel modelTable = (DefaultTableModel)tableProviders.getModel();
        if (modelTable.getRowCount() > 0) {
            modelTable.setRowCount(0);
        }
        //LLENAR LA TABLA CON LOS DATOS DE LA LISTA
        int num = 1;
        for (Provider provider : providersList) {

            String values[] = {num+"",provider.getName(),
                               provider.getEmail(), provider.getPhone()+""};
            
            modelTable.addRow(values);
            
            num++;
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
        JLabel source = (JLabel)e.getSource();
        
        if (providersPanel != null) {
            if (source.equals(providersPanel.getLblBtnRegister())) {
                String nameProvider = providersPanel.getFieldNameProvider().getText();
                if (!nameProvider.isEmpty() && !nameProvider.equals(" ")) {
                    
                    try{
                        Provider provider = new Provider();
                        provider.setName(nameProvider);
                        provider.setEmail(providersPanel.getFieldEmail().getText());
                        String sPhone = providersPanel.getFieldPhone().getText();
                        if (!sPhone.isEmpty()) {
                            provider.setPhone(Integer.parseInt(sPhone));
                        }
                        
                        if (providerDAO.createProvider(provider)) {
                            providersList.add(provider);
                            SuccessModal successModal = new SuccessModal("Registro Realizado Exitosamente");
                            clearFields();
                            updateProvidersTable();
                        }
                        else {
                            ErrorModal errorModal = new ErrorModal("Hubo un error en la conexión con la Base de Datos");
                        }
                        
                    } catch(NumberFormatException ex) {
                        ErrorModal errorModal = new ErrorModal("El campo número de contacto debe contener valores validos");
                    }
                    
                }
                else {
                    ErrorModal errorModal = new ErrorModal("Debe llenar el campo Nombre");
                }
            }
        }
    }
    
    private void clearFields() {
        providersPanel.getFieldNameProvider().setText("");
        providersPanel.getFieldEmail().setText("");
        providersPanel.getFieldPhone().setText("");
    }
}
