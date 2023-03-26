package co.anbora.labs.kse.ide.gui.swing;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

public class UnlockPanel {
    private JPanel unlockPanel;
    private JPasswordField passwordField;
    private JButton okButton;

    public UnlockPanel(String title) {
        Border borderTitle = BorderFactory.createTitledBorder(title);
        unlockPanel.setBorder(borderTitle);
    }

    public JComponent getComponent() {
        return unlockPanel;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public void addActionListener(ActionListener l) {
        okButton.addActionListener(l);
    }

    public void addKeyListener(KeyListener l) {
        passwordField.addKeyListener(l);
    }

    public void setVisible(boolean visible) {
        unlockPanel.setVisible(visible);
    }
}
