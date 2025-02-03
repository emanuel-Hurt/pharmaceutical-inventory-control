package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;

import model.FileConfig;
import model.User;
import model.daos.UserDAO;
import resources.BCrypt;
import view.AccessWindow;
import view.ControlPanel;
import view.InitialWindow;

/**
 *
 * @author EmanuelHurt
 */
public class AccessController extends MouseAdapter {
    
    private final AccessWindow window;
    private final UserDAO userDAO;
    private final FileConfig fileConfig;
    
    public AccessController(AccessWindow window, UserDAO userDAO, FileConfig fileConfig) {
        this.userDAO = userDAO;
        this.window = window;
        this.fileConfig = fileConfig;
        
        window.getBtnLogin().addMouseListener(this);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
        String userName = window.getUserField().getText();
        String password = window.getPasswordField().getText();
        
        if (userName.length() != 0 && password.length() != 0) {
            
            User user = userDAO.readUser(userName);
            
            if (user.getName() != null) {
                if (BCrypt.checkpw(password, user.getPassword())) {
                //entra a la ventana inicial
                InitialWindow iniWindow = new InitialWindow();
                ControlPanel controlPanel = new ControlPanel();
                iniWindow.changeCenterPanel(controlPanel);
                
                InitialController iniController = new InitialController(iniWindow,fileConfig,user);     
                iniController.setControlPanel(controlPanel);
                iniController.activeInitialWindow();
                
                window.dispose();
                
                } else {
                    window.showErroMessage("Datos incorrectos.");
                }
                
            } else {
                window.showErroMessage("Datos incorrectos.");
            }
            
            
        } else {
            window.showErroMessage("Campos vacios.");
        }
    }
}
