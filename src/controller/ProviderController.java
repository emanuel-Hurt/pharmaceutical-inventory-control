package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Provider;
import model.daos.ProviderDAO;
import view.ErrorModal;
import view.ProvidersRegisterPanel;
import view.SuccessModal;

/**
 *
 * @author EmanuelHurt
 */
public class ProviderController extends MouseAdapter {
    
    private final ProviderDAO providerDAO;
    private ProvidersRegisterPanel providersPanel;
    private JTable tableProviders;
    
    public ProviderController(ProviderDAO provDAO) {
        providerDAO = provDAO;
    }
    
    public void setProvidersRegisterPanel(ProvidersRegisterPanel panel) {
        providersPanel = panel;
        providersPanel.getLblBtnRegister().addMouseListener(this);
        tableProviders = providersPanel.getTableProviders();
        updateProvidersTable();
    }
    
    private void updateProvidersTable() {
        //OBTENER PROVEEDORES
        ArrayList<Provider> providersList = (ArrayList<Provider>)providerDAO.providersList();
        //VACIAR LA TABLA PREVIAMENTE
        DefaultTableModel modelTable = (DefaultTableModel)tableProviders.getModel();
        if (modelTable.getRowCount() > 0) {
            modelTable.setRowCount(0);
        }
        //LLENAR LA TABLA CON LOS DATOS OBTENIDOS
        for (Provider provider : providersList) {

            String values[] = {provider.getIdProvider()+"",provider.getName(),
                               provider.getEmail(), provider.getPhone()+""};
            
            modelTable.addRow(values);           
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
