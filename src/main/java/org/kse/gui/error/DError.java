/*
 * Copyright 2004 - 2013 Wayne Grant
 *           2013 - 2021 Kai Kramer
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
package org.kse.gui.error;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import java.awt.*;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.kse.gui.JEscDialog;
import org.kse.gui.PlatformUtil;

/**
 * Displays an error message with the option to display the stack trace.
 *
 */
public class DError extends JEscDialog {
  private static final long serialVersionUID = 1L;

  private static ResourceBundle res =
      ResourceBundle.getBundle("org/kse/gui/error/resources");

  private JPanel jpError;
  private JLabel jlError;
  private JPanel jpButtons;
  private JButton jbDetails;
  private JButton jbOK;

  private Throwable error;

  /**
   * Creates new DError dialog where the parent is a Project.
   *  @param parent
   *            Parent frame
   * @param error
   */
  public DError(Project parent, Throwable error) {
    super(parent, DialogWrapper.IdeModalityType.IDE);
    this.setTitle(res.getString("DError.Title"));
    this.error = error;
    initComponents();
  }

  /**
   * Display an error for the supplied Project as application modal.
   *
   * @param parent
   *            Project
   * @param error
   *            Error
   */
  public static void displayError(Project parent, Throwable error) {
    DError dError = new DError(parent, error);
    dError.show();
  }

  private void initComponents() {
    jpError = new JPanel(new FlowLayout(FlowLayout.CENTER));
    jpError.setBorder(new EmptyBorder(5, 5, 5, 5));

    jlError = new JLabel(formatError());
    ImageIcon icon = new ImageIcon(getClass().getResource("images/error.png"));
    jlError.setIcon(icon);
    jlError.setHorizontalTextPosition(SwingConstants.TRAILING);
    jlError.setIconTextGap(15);

    jpError.add(jlError);

    // Buttons
    jbDetails = new JButton(res.getString("DError.jbDetails.text"));
    PlatformUtil.setMnemonic(
        jbDetails, res.getString("DError.jbDetails.mnemonic").charAt(0));

    jbOK = new JButton(res.getString("DError.jbOK.text"));
    jbOK.addActionListener(evt -> okPressed());

    jpButtons = PlatformUtil.createDialogButtonPanel(jbOK, null, jbDetails);

    getContentPane().add(jpError, BorderLayout.CENTER);
    getContentPane().add(jpButtons, BorderLayout.SOUTH);

    setResizable(false);

    getRootPane().setDefaultButton(jbOK);

    pack();
  }

  private String formatError() {
    String message = error.getMessage();

    if (message != null) {
      return MessageFormat.format("<html>{0}:<br>{1}</html>",
                                  error.getClass().getName(),
                                  breakLine(message));
    } else {
      return error.getClass().getName();
    }
  }

  private String breakLine(String line) {
    StringBuilder sb = new StringBuilder();

    StringTokenizer strTok = new StringTokenizer(line, " ");

    String currentLine = "";

    while (strTok.hasMoreTokens()) {
      String word = strTok.nextToken();

      if (currentLine.length() == 0) {
        currentLine += word;
        continue;
      }

      if ((currentLine.length() + word.length() + 1) <= 50) {
        currentLine += " ";
        currentLine += word;
      } else {
        if (sb.length() > 0) {
          sb.append("<br>");
        }

        sb.append(currentLine);
        currentLine = word;
      }
    }

    if (sb.length() > 0) {
      sb.append("<br>");
    }

    sb.append(currentLine);

    return sb.toString();
  }

  private void okPressed() { closeDialog(); }

  private void closeDialog() {
    doCancelAction();
    dispose();
  }
}
