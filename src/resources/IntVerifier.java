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
public class IntVerifier extends InputVerifier {
    
    private final Border originalBorder;
    private JLabel errorLbl;
    
    public IntVerifier(Border border, JLabel errorLbl) {
        this.originalBorder = border;
        this.errorLbl = errorLbl;
    }

    @Override
    public boolean verify(JComponent input) {
        JTextField textField = (JTextField)input;
        String text = textField.getText();
        try {
            Integer.parseInt(text);
            textField.setBorder(originalBorder);
            errorLbl.setText("");
            return true;
        } catch (NumberFormatException ex) {
            textField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            this.errorLbl.setForeground(Color.RED);
            this.errorLbl.setText("Ingrese valores enteros");
            return false;
        }
    }
    
}
