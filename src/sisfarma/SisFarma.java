package sisfarma;

import com.formdev.flatlaf.FlatLightLaf;
import controller.AccessController;
import model.FileConfig;
import model.daos.UserDAO;
import view.AccessWindow;

/**
 *
 * @author EmanuelHurt
 */

public class SisFarma {
    
    public static void main(String[] args) {       
        
        FlatLightLaf.setup();
        
        FileConfig fileConfig = new FileConfig();
        UserDAO userDAO = new UserDAO(fileConfig);
        AccessWindow accessWindow = new AccessWindow();
        AccessController accessController = new AccessController(accessWindow,userDAO,fileConfig);

    }
    
}
