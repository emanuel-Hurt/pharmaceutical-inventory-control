package view;

/**
 *
 * @author EmanuelHurt
 */
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

public class HoverEffect extends MouseAdapter {
    
    private JComponent component;
    private Color oldColor, newColor;
    
    public HoverEffect(JComponent component, Color hoverColor) {
        this.component = component;
        oldColor = component.getBackground();
        newColor = hoverColor;
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        component.setBackground(newColor);
    }
    @Override
    public void mouseExited(MouseEvent e) {
        component.setBackground(oldColor);
    }
}
