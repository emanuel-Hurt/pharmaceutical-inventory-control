package sisfarma;

import controller.AccessController;
import controller.InitialController;
import model.FileConfig;
import model.User;
import model.daos.UserDAO;
import view.AccessWindow;
import view.ControlPanel;
import view.InitialWindow;

/**
 *
 * @author EmanuelHurt
 */

public class SisFarma {
    
    public static void main(String[] args) {       
        
        FileConfig fileConfig = new FileConfig();
        UserDAO userDAO = new UserDAO(fileConfig);
        AccessWindow accessWindow = new AccessWindow();
        AccessController accessController = new AccessController(accessWindow,userDAO,fileConfig);

    }
    
}
