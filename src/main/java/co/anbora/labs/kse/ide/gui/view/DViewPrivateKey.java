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
package co.anbora.labs.kse.ide.gui.view;

import co.anbora.labs.kse.ide.gui.CertEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import net.miginfocom.swing.MigLayout;
import org.bouncycastle.jcajce.provider.asymmetric.edec.BCEdDSAPrivateKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kse.KSE;
import org.kse.crypto.CryptoException;
import org.kse.crypto.KeyInfo;
import org.kse.crypto.keypair.KeyPairType;
import org.kse.crypto.keypair.KeyPairUtil;
import org.kse.gui.CursorUtil;
import org.kse.gui.JEscDialog;
import org.kse.gui.LnfUtil;
import org.kse.gui.PlatformUtil;
import org.kse.gui.error.DError;
import org.kse.utilities.DialogViewer;
import org.kse.utilities.asn1.Asn1Exception;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Displays the details of a private key with the option to display its fields
 * if it is of a supported type (RSA or DSA).
 */
public class DViewPrivateKey extends CertEditor {
    private static final long serialVersionUID = 1L;

    private static ResourceBundle res = ResourceBundle.getBundle("org/kse/gui/dialogs/resources");
    private static ResourceBundle resActions = ResourceBundle.getBundle("org/kse/gui/actions/resources");

    private JLabel jlAlgorithm;
    private JTextField jtfAlgorithm;
    private JLabel jlKeySize;
    private JTextField jtfKeySize;
    private JLabel jlFormat;
    private JTextField jtfFormat;
    private JLabel jlEncoded;
    private JTextArea jtaEncoded;
    private JScrollPane jspEncoded;
    private JButton jbExport;
    private JButton jbPem;
    private JButton jbFields;
    private JButton jbAsn1;
    private JButton jbOK;

    private String alias;
    private PrivateKey privateKey;

    /**
     * Creates new DViewPrivateKey dialog where the parent is a dialog.
     *
     * @param project     Parent dialog
     * @param file      The dialog title
     * @param privateKey Private key to display
     * @throws CryptoException A problem was encountered getting the private key's details
     */
    public DViewPrivateKey(Project project, VirtualFile file, @Nullable PrivateKey privateKey) throws CryptoException {
        super(project, file);
        this.privateKey = privateKey;
        initComponents();
        jbExport.setVisible(false);
    }

    public void loadPrivateKey(@NotNull PrivateKey privateKey) throws CryptoException {
        this.privateKey = privateKey;
        this.populateDialog();
    }

    private void initComponents() throws CryptoException {

        jlAlgorithm = new JLabel(res.getString("DViewPrivateKey.jlAlgorithm.text"));

        jtfAlgorithm = new JTextField();
        jtfAlgorithm.setEditable(false);
        jtfAlgorithm.setToolTipText(res.getString("DViewPrivateKey.jtfAlgorithm.tooltip"));

        jlKeySize = new JLabel(res.getString("DViewPrivateKey.jlKeySize.text"));

        jtfKeySize = new JTextField();
        jtfKeySize.setEditable(false);
        jtfKeySize.setToolTipText(res.getString("DViewPrivateKey.jtfKeySize.tooltip"));

        jlFormat = new JLabel(res.getString("DViewPrivateKey.jlFormat.text"));

        jtfFormat = new JTextField();
        jtfFormat.setEditable(false);
        jtfFormat.setToolTipText(res.getString("DViewPrivateKey.jtfFormat.tooltip"));

        jlEncoded = new JLabel(res.getString("DViewPrivateKey.jlEncoded.text"));

        jtaEncoded = new JTextArea();
        jtaEncoded.setFont(new Font(Font.MONOSPACED, Font.PLAIN, LnfUtil.getDefaultFontSize()));
        jtaEncoded.setBackground(jtfFormat.getBackground());
        jtaEncoded.setEditable(false);
        jtaEncoded.setLineWrap(true);
        jtaEncoded.setToolTipText(res.getString("DViewPrivateKey.jtfEncoded.tooltip"));

        jspEncoded = PlatformUtil.createScrollPane(jtaEncoded, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                   ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jspEncoded.setBorder(jtfFormat.getBorder());

        
        jbExport = new JButton(res.getString("DViewPrivateKey.jbExport.text"));
        PlatformUtil.setMnemonic(jbExport, res.getString("DViewPrivateKey.jbExport.mnemonic").charAt(0));
        jbExport.setToolTipText(res.getString("DViewPrivateKey.jbExport.tooltip"));
        
        jbPem = new JButton(res.getString("DViewPrivateKey.jbPem.text"));
        PlatformUtil.setMnemonic(jbPem, res.getString("DViewPrivateKey.jbPem.mnemonic").charAt(0));
        jbPem.setToolTipText(res.getString("DViewPrivateKey.jbPem.tooltip"));

        jbFields = new JButton(res.getString("DViewPrivateKey.jbFields.text"));
        PlatformUtil.setMnemonic(jbFields, res.getString("DViewPrivateKey.jbFields.mnemonic").charAt(0));
        jbFields.setToolTipText(res.getString("DViewPrivateKey.jbFields.tooltip"));

        jbAsn1 = new JButton(res.getString("DViewPrivateKey.jbAsn1.text"));
        PlatformUtil.setMnemonic(jbAsn1, res.getString("DViewPrivateKey.jbAsn1.mnemonic").charAt(0));
        jbAsn1.setToolTipText(res.getString("DViewPrivateKey.jbAsn1.tooltip"));

        jbOK = new JButton(res.getString("DViewPrivateKey.jbOK.text"));

        // layout
        Container pane = getContentPane();
        pane.setLayout(new MigLayout("insets dialog", "[right]unrel[]", "[]unrel[]"));
        pane.add(jlAlgorithm, "");
        pane.add(jtfAlgorithm, "growx, pushx, wrap");
        pane.add(jlKeySize, "");
        pane.add(jtfKeySize, "growx, pushx, wrap");
        pane.add(jlFormat, "");
        pane.add(jtfFormat, "growx, pushx, wrap");
        pane.add(jlEncoded, "");
        //pane.add(jspEncoded, "width 300lp:300lp:300lp, height 100lp:100lp:100lp, wrap");
        pane.add(jspEncoded, "growx, pushx, wrap");


        //pane.add(jbExport, "spanx, split");
        //pane.add(jbPem, "");
        //pane.add(jbFields, "");
        //pane.add(jbAsn1, "wrap");
        //pane.add(new JSeparator(), "spanx, growx, wrap unrel:push");
        //pane.add(jbOK, "spanx, tag ok");

        // actions

        /*jbExport.addActionListener(evt -> exportPressed());

        jbOK.addActionListener(evt -> okPressed());

        jbPem.addActionListener(evt -> {
            try {
                CursorUtil.setCursorBusy(DViewPrivateKey.this);
                pemEncodingPressed();
            } finally {
                CursorUtil.setCursorFree(DViewPrivateKey.this);
            }
        });

        jbFields.addActionListener(evt -> {
            try {
                CursorUtil.setCursorBusy(DViewPrivateKey.this);
                fieldsPressed();
            } finally {
                CursorUtil.setCursorFree(DViewPrivateKey.this);
            }
        });

        jbAsn1.addActionListener(evt -> {
            try {
                CursorUtil.setCursorBusy(DViewPrivateKey.this);
                asn1DumpPressed();
            } finally {
                CursorUtil.setCursorFree(DViewPrivateKey.this);
            }
        });*/

        populateDialog();

        //getRootPane().setDefaultButton(jbOK);

        //SwingUtilities.invokeLater(() -> jbOK.requestFocus());
    }

    /*private void exportPressed() {
    	KeyPairType keyPairType = KeyPairUtil.getKeyPairType(privateKey);
        DExportPrivateKeyType dExportPrivateKeyType = new DExportPrivateKeyType((JFrame) this.getParent(), keyPairType);
        dExportPrivateKeyType.setLocationRelativeTo(null);
        dExportPrivateKeyType.setVisible(true);

        if (!dExportPrivateKeyType.exportTypeSelected()) {
            return;
        }
        try
        {
            if (dExportPrivateKeyType.exportPkcs8()) {
            	PrivateKeyUtils.exportAsPkcs8(privateKey, alias,(JFrame) this.getParent(), applicationSettings, resActions);
            } else if (dExportPrivateKeyType.exportPvk()) {
            	PrivateKeyUtils.exportAsPvk(privateKey, alias, (JFrame) this.getParent(), applicationSettings, resActions);
            } else {
            	PrivateKeyUtils.exportAsOpenSsl(privateKey, alias, (JFrame) this.getParent(), applicationSettings, resActions);
            }
        }
        catch (Exception ex) {
            DError.displayError((JFrame) this.getParent(), ex);
        }

	}*/
    
	private void populateDialog() throws CryptoException {
        if (privateKey != null) {
            KeyInfo keyInfo = KeyPairUtil.getKeyInfo(privateKey);

            jtfAlgorithm.setText(keyInfo.getAlgorithm());

            if (privateKey instanceof ECPrivateKey) {
                jtfAlgorithm.setText(jtfAlgorithm.getText() + " (" + keyInfo.getDetailedAlgorithm() + ")");
            }

            Integer keyLength = keyInfo.getSize();

            if (keyLength != null) {
                jtfKeySize.setText(MessageFormat.format(res.getString("DViewPrivateKey.jtfKeySize.text"), "" + keyLength));
            } else {
                jtfKeySize.setText(MessageFormat.format(res.getString("DViewPrivateKey.jtfKeySize.text"), "?"));
            }

            jtfFormat.setText(privateKey.getFormat());

            jtaEncoded.setText(new BigInteger(1, privateKey.getEncoded()).toString(16).toUpperCase());
            jtaEncoded.setCaretPosition(0);

            jbFields.setEnabled((privateKey instanceof RSAPrivateKey) || (privateKey instanceof DSAPrivateKey) ||
                    (privateKey instanceof ECPrivateKey) || (privateKey instanceof BCEdDSAPrivateKey));
        }
    }

    /*private void pemEncodingPressed() {
        try {
            DViewPem dViewCsrPem = new DViewPem(this, res.getString("DViewPrivateKey.Pem.Title"), privateKey);
            dViewCsrPem.setLocationRelativeTo(this);
            dViewCsrPem.setVisible(true);
        } catch (CryptoException e) {
            DError.displayError(this, e);
        }
    }

    private void fieldsPressed() {
        DViewAsymmetricKeyFields dViewAsymmetricKeyFields = new DViewAsymmetricKeyFields(this, privateKey);
        dViewAsymmetricKeyFields.setLocationRelativeTo(this);
        dViewAsymmetricKeyFields.setVisible(true);
    }

    private void asn1DumpPressed() {
        try {
            DViewAsn1Dump dViewAsn1Dump = new DViewAsn1Dump(this, privateKey);
            dViewAsn1Dump.setLocationRelativeTo(this);
            dViewAsn1Dump.setVisible(true);
        } catch (Asn1Exception | IOException e) {
            DError.displayError(this, e);
        }
    }

    private void okPressed() {
        closeDialog();
    }*/

    private void closeDialog() {
        setVisible(false);
        dispose();
    }

    @Override
    public @NotNull JComponent getComponent() {
        return getRootPane();
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return getRootPane();
    }
}
