package controller;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import model.Password;
import model.Percentage;
import model.User;
import model.daos.PasswordDAO;
import model.daos.PercentageDAO;
import model.daos.ProductSaleDAO;
import model.daos.TimeNotificationDAO;
import model.daos.UserDAO;
import resources.BCrypt;
import view.AddUserWindow;
import view.ErrorModal;
import view.ExpireNotificationPanel;
import view.PercentagesPanel;
import view.SuccessModal;
import view.UserManagementPanel;
import view.UsersWindow;

/**
 *
 * @author EmanuelHurt
 */
public class SettingsController extends MouseAdapter {
    
    private PercentagesPanel percentagesPanel;
    private ExpireNotificationPanel expireNotificationPanel;
    private UserManagementPanel userManagementPanel;
    
    private final PercentageDAO percentageDao;
    private final UserDAO userDAO;
    private final PasswordDAO passwordDAO;
    private final ProductSaleDAO productSaleDAO;
    private final TimeNotificationDAO timeNotificationDAO;
    
    private ArrayList<User> users;
    //private final HashSet<Integer> rowsEdited;
    
    public SettingsController(PercentageDAO percentageDAO, UserDAO userDAO, PasswordDAO passDAO, ProductSaleDAO prodSaleDAO, TimeNotificationDAO timeNotiDAO) {
        this.percentageDao = percentageDAO;
        this.userDAO = userDAO;
        this.passwordDAO = passDAO;
        this.productSaleDAO = prodSaleDAO;
        this.timeNotificationDAO = timeNotiDAO;

        //rowsEdited = new HashSet<>();
    }
    
    public void setPercentagesPanel(PercentagesPanel panel) {
        this.percentagesPanel = panel;
        
        Percentage pastPercentage = percentageDao.readPercentage(1);

        percentagesPanel.getFieldSalePercentage().setText(pastPercentage.getSalePercentage()+"");
        percentagesPanel.getFieldDiscountPercentage().setText(pastPercentage.getDiscountPercentage()+"");
        
        this.percentagesPanel.getLblBtnAcept().addMouseListener(this);
    }
    
    public void setExpireNotificationPanel(ExpireNotificationPanel panel) {
        expireNotificationPanel = panel;
        
        int weeks = timeNotificationDAO.readTimeNotification(1).getQuantityToNotify();
        expireNotificationPanel.getTxtFieldWeeks().setText(weeks+"");
        
        this.expireNotificationPanel.getLblBtnAcept().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    int numWeeks = Integer.parseInt(expireNotificationPanel.getTxtFieldWeeks().getText());
                    if (numWeeks > 0) {
                        if (timeNotificationDAO.updateTimeNotification(1, numWeeks)) {
                            SuccessModal modal = new SuccessModal("Actualización realizada.");
                        } else {
                            ErrorModal modal = new ErrorModal("Error de conexión, contacte al desarrollador");
                        }
                    } else {
                        ErrorModal modal = new ErrorModal("Ingrese un número de semanas válido");
                    }
                }catch(NumberFormatException ex) {
                    ErrorModal modal = new ErrorModal("Ingrese un número de semanas válido");
                }
            }
        });
    }
    
    public void setUserManagementPanel(UserManagementPanel panel) {
        userManagementPanel = panel;
        userManagementPanel.getLblBtnShowUsers().addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                UsersWindow window = new UsersWindow();
                setUsersWindow(window);
            }
        });
        userManagementPanel.getLblBtnAddUser().addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                AddUserWindow window = new AddUserWindow();
                setAddUserWindow(window);
            }
        });
    }
    
    private void setUsersWindow(UsersWindow window) {
        //LLENADO DE LA TABLA
        fillTheTableUsers(window);
        
        DefaultTableModel model = (DefaultTableModel)window.getTblUsers().getModel();
        
        //CAPTURA MODIFICACIONES EN LA TABLA
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    try {                      
                        String userName = model.getValueAt(row, 0).toString();
                        String name = model.getValueAt(row, 1).toString();
                        String lastname = model.getValueAt(row, 2).toString();
                        String mLastname = model.getValueAt(row, 3).toString();
                        int phone = Integer.parseInt(model.getValueAt(row, 4).toString());
                        String address = model.getValueAt(row, 5).toString();
                        String pass = model.getValueAt(row, 6).toString();
                        String hashed = BCrypt.hashpw(pass, BCrypt.gensalt(12));                                             
                        
                        User user = users.get(row); //obtendo los datos iniciales del usuario
                        
                        if (!users.get(row).getName().equals("admin")) {
                            int idRol = 2;

                            //user.setIdUser(users.get(row).getIdUser());
                            user.setUsername(userName);
                            user.setName(name);
                            user.setLastName(lastname);
                            user.setmLastName(mLastname);
                            user.setCellPhone(phone);
                            user.setAddress(address);
                            
                            user.setIdRol(idRol); 
                        }
                                              
                        user.setPassword(hashed);
                        userDAO.updateUser(user);
                        
                        Password password = new Password();
                        password.setIdUser(user.getIdUser());
                        password.setThePassword(pass);
                        passwordDAO.updatePassword(password);
                        
                    }catch(NumberFormatException ex) {
                        //rowsEdited.remove(row);
                        window.showErrorMessage("Número cel/telf inválido");
                    }
                }
            }
        });
        
        //CONTROL DEL BOTON ELIMINAR
        window.getLblBtnDelete().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = window.getTblUsers().getSelectedRow();
                if (row != -1) {
                    String username = window.getTblUsers().getModel().getValueAt(row, 0).toString();
                    User user = userDAO.readUser(username);

                    if (user.getIdRol() != 1) {

                        productSaleDAO.backupProductSales(user.getIdUser());
                        if (userDAO.deleteUser(username)) {
                            fillTheTableUsers(window);
                            window.showSuccessfullMessage("Usuario eliminado.");
                        } else {
                            window.showErrorMessage("Error al tratar de eliminar Usuario.");
                        }

                    } else {
                        ErrorModal modal = new ErrorModal("Usuario admin no puede ser eleminado.");
                    }
                } else {
                    ErrorModal errorModal = new ErrorModal("Debe seleccionar la fila que desea eliminar.");
                }               
            }
        });
        
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
    private boolean nonEmptyData(UsersWindow usersWindow) {
        boolean resp = false;
        return resp;
    }
    private void fillTheTableUsers(UsersWindow window) {
        
        DefaultTableModel model = (DefaultTableModel)window.getTblUsers().getModel();

        if (model.getRowCount() > 0) {
            model.setRowCount(0);
        }
        users = (ArrayList<User>)userDAO.getUsers();
        for (User user : users) {
            String row[] = {
                user.getUsername(),
                user.getName(),
                user.getLastName(),
                user.getmLastName(),
                user.getCellPhone()+"",
                user.getAddress(),
                user.getPassword()
            };
            
            model.addRow(row);
        }
    }
    
    private void setAddUserWindow(AddUserWindow addUserWindow) {
        addUserWindow.getLblBtnSave().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JLabel lblBtn = (JLabel)e.getSource();
                if (addUserWindow.isFilesFull()) {
                    if (addUserWindow.correctData()) { //verifica que no se use la palabra reservada 'admin'
                        
                        try {
                        
                            String name = addUserWindow.getTxFieldName().getText();
                            String lastName = addUserWindow.getTxFieldLastName().getText();
                            String mLastName = addUserWindow.getTxFieldMLastName().getText();

                            User user = new User();
                            user.setName(name);
                            user.setLastName(lastName);
                            user.setmLastName(mLastName);
                            int cell;
                            if (addUserWindow.getTxFieldCel().getText().isEmpty()) {
                                cell = 0;
                            } else {
                                cell = Integer.parseInt(addUserWindow.getTxFieldCel().getText());
                                user.setCellPhone(cell);
                            }
                            user.setAddress(addUserWindow.getTxFieldAddress().getText());
                            user.setUsername(name+lastName+mLastName);

                            String password = addUserWindow.getTxFieldPassword().getText();

                            if (saveUser(user, password)) {
                                lblBtn.setBackground(Color.GRAY);
                                addUserWindow.showSuccessfullSave();
                                addUserWindow.getLblBtnSave().removeMouseListener(this);
                                desactivateFields(addUserWindow);
                                
                            } else {
                                addUserWindow.showNotSave("Error de conexión, contacte al desarrollador");
                            }

                        }catch (NumberFormatException ex) {
                            addUserWindow.showErrorCell();
                        }
                        
                    } else {
                        addUserWindow.showNotSave("Nombre admin es de uso restringido. Datos no válidos");
                    }
                    
                } else {
                    addUserWindow.showNotSave("Los campos obligatorios deben ser llenados");
                }
            }
        });
        
        addUserWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addUserWindow.setResizable(false);
        addUserWindow.setLocationRelativeTo(null);
        addUserWindow.setVisible(true);
    }
    private boolean saveUser(User user, String pass) {
        boolean resp = false;
        
        String hashed = BCrypt.hashpw(pass, BCrypt.gensalt(12));
        
        user.setIdRol(2);
        user.setPassword(hashed);
        int idUser = userDAO.createUser(user);
        
        if (idUser > 0) {
            Password plainPassword = new Password();
            plainPassword.setIdUser(idUser);
            plainPassword.setThePassword(pass);
            if (passwordDAO.createPassword(plainPassword)) {
                resp = true;
            }
        }
        
        return resp;
    }
    
    public void mouseClicked(MouseEvent e) {
        JLabel source = (JLabel)e.getSource();
        
        if (percentagesPanel != null) {
            
            String pastSalePercentage = percentagesPanel.getFieldSalePercentage().getText();
            String pastDiscountPercentage = percentagesPanel.getFieldDiscountPercentage().getText();
            
            if (source.equals(percentagesPanel.getLblBtnAcept())) {
                
                percentagesPanel.getLblMessageError().setForeground(Color.WHITE);
                
                try {
                    double salePercentage = Double.parseDouble(percentagesPanel.getFieldSalePercentage().getText());
                    double discountPercentage = Double.parseDouble(percentagesPanel.getFieldDiscountPercentage().getText());
                    
                    Percentage percentage = new Percentage();
                    percentage.setIdPercentage(1);
                    percentage.setSalePercentage(salePercentage);
                    percentage.setDiscountPercentage(discountPercentage);                   
                    
                    if (percentageDao.updatePercentage(percentage)) {
                        SuccessModal successModa = new SuccessModal("Porcentaje Establecido");
                    }
                    else {
                        ErrorModal  errorModal = new ErrorModal("Ingrese valores numéricos");
                    }
                    
                }catch(NumberFormatException ex) {
                    percentagesPanel.getLblMessageError().setForeground(Color.red);
                }
            }
        }
    }
    
    public void desactivateFields(AddUserWindow addUserWindow) {
        addUserWindow.getTxFieldName().setEnabled(false);
        addUserWindow.getTxFieldLastName().setEnabled(false);
        addUserWindow.getTxFieldMLastName().setEnabled(false);
        addUserWindow.getTxFieldAddress().setEnabled(false);
        addUserWindow.getTxFieldCel().setEnabled(false);
        addUserWindow.getTxFieldPassword().setEnabled(false);
    }
}
