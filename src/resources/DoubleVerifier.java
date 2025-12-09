/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resources;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * @author Leo
 */
public class DoubleVerifier extends InputVerifier {
    
    private final Border originalBorder;
    private JLabel errorLbl;
    
    public DoubleVerifier(Border originalBorder, JLabel errorLbl) {
        this.originalBorder = originalBorder;
        this.errorLbl = errorLbl;
    }
    
    @Override
    public boolean verify(JComponent input) {
        JTextField textField = (JTextField)input;
        String text = textField.getText();
        try {
            Double.parseDouble(text);
            textField.setBorder(originalBorder);
            errorLbl.setText("");
            return true;
        } catch (NumberFormatException ex) {
            textField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            this.errorLbl.setForeground(Color.red);
            this.errorLbl.setText("Ingrese valores decimales usando el formato punto");
            return false;
        }
    }
    
}
