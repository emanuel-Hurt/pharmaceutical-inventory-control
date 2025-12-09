package controller;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;

import model.FileConfig;
import model.User;
import model.daos.UserDAO;
import resources.BCrypt;
import view.AccessWindow;
import view.ControlPanel;
import view.CtrlPanel;
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
        window.getUserField().addActionListener(e -> verify());
        window.getPasswordField().addActionListener(e -> verify());
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {    
        verify();
    }
    
    private void verify() {
        String userName = window.getUserField().getText();
        String password = window.getPasswordField().getText();
        
        if (userName.length() != 0 && password.length() != 0) {
            
            User user = userDAO.readUser(userName);
            
            if (user.getName() != null) {
                if (BCrypt.checkpw(password, user.getPassword())) {
                //entra a la ventana inicial
                InitialWindow iniWindow = new InitialWindow();
                //ControlPanel controlPanel = new ControlPanel(); //QUITARLO PORQUE ES MUY VIEJO
                CtrlPanel ctrlPanel = new CtrlPanel(); //ESTE ES EL NUEVO QUE DEBE QUEDARSE
                iniWindow.changeCenterPanel(ctrlPanel);
                InitialController iniController = new InitialController(iniWindow,fileConfig,user);     
                //iniController.setControlPanel(controlPanel);//PONER AQUI EL NUEVO CONTROL PANEL
                
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
