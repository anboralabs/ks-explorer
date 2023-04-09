/*
 * Copyright 2004 - 2013 Wayne Grant
 *           2013 - 2023 Kai Kramer
 *
 * This file is part of KeyStore Explorer.
 *
 * KeyStore Explorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeyStore Explorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with KeyStore Explorer.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.kse.gui.password;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.kse.crypto.Password;
import org.kse.gui.JEscDialog;
import org.kse.gui.PlatformUtil;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

/**
 * Dialog used for entering a masked password.
 */
public class DGetPassword extends JEscDialog {
    private static final long serialVersionUID = 1L;

    private static ResourceBundle res = ResourceBundle.getBundle("org/kse/gui/password/resources");

    private static final String CANCEL_KEY = "CANCEL_KEY";
    private JLabel jlPassword;
    private JPasswordField jpfPassword;
    private JPanel jpPassword;
    private JButton jbOK;
    private JButton jbCancel;
    private JPanel jpButtons;

    private Password password;

    /**
     * Creates new DGetPassword dialog where the parent is a frame.
     *
     * @param parent Parent frame
     * @param title  The dialog's title
     */
    public DGetPassword(Project parent, String title) {
        super(parent, DialogWrapper.IdeModalityType.PROJECT);
        setTitle(title);
        initComponents();
    }

    private void initComponents() {
        getContentPane().setLayout(new BorderLayout());

        jlPassword = new JLabel(res.getString("DGetPassword.jlPassword.text"));
        jpfPassword = new JPasswordField(15);

        jbOK = new JButton(res.getString("DGetPassword.jbOK.text"));
        jbOK.addActionListener(evt -> okPressed());

        jbCancel = new JButton(res.getString("DGetNewPassword.jbCancel.text"));
        jbCancel.addActionListener(evt -> cancelPressed());
        jbCancel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), CANCEL_KEY);
        jbCancel.getActionMap().put(CANCEL_KEY, new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent evt) {
                cancelPressed();
            }
        });

        jpPassword = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jpPassword.add(jlPassword);
        jpPassword.add(jpfPassword);
        jpPassword.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5),
                                                new CompoundBorder(new EtchedBorder(), new EmptyBorder(5, 5, 5, 5))));

        jpButtons = PlatformUtil.createDialogButtonPanel(jbOK, jbCancel);

        getContentPane().add(jpPassword, BorderLayout.CENTER);
        getContentPane().add(jpButtons, BorderLayout.SOUTH);

        setResizable(false);

        getRootPane().setDefaultButton(jbOK);

        pack();
    }

    /**
     * Get the password set in the dialog.
     *
     * @return The password or null if none was set
     */
    public Password getPassword() {
        return password;
    }

    private void okPressed() {
        password = new Password(jpfPassword.getPassword());
        closeDialog();
    }

    private void cancelPressed() {
        closeDialog();
    }

    private void closeDialog() {
        doCancelAction();
        dispose();
    }
}
