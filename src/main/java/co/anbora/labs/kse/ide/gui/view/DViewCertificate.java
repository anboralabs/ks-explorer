package co.anbora.labs.kse.ide.gui.view;

import co.anbora.labs.kse.ide.gui.CertEditor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import java.awt.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import javax.swing.*;
import javax.swing.tree.*;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kse.crypto.CryptoException;
import org.kse.crypto.KeyInfo;
import org.kse.crypto.keypair.KeyPairUtil;
import org.kse.crypto.x509.X500NameUtils;
import org.kse.crypto.x509.X509CertUtil;
import org.kse.gui.CursorUtil;
import org.kse.gui.PlatformUtil;
import org.kse.gui.crypto.JCertificateFingerprint;
import org.kse.gui.crypto.JDistinguishedName;
import org.kse.gui.dialogs.CertificateTreeCellRend;
import org.kse.gui.dialogs.DViewPem;
import org.kse.gui.error.DError;
import org.kse.utilities.StringUtils;

public class DViewCertificate extends CertEditor {

  protected JRootPane rootPane;

  private static ResourceBundle res =
      ResourceBundle.getBundle("org/kse/gui/dialogs/resources");

  public static final int NONE = 0;
  public static final int IMPORT = 1;
  public static final int EXPORT = 2;
  public static final int IMPORT_EXPORT = 3;

  private int importExport = 0;

  private JLabel jlHierarchy;
  private JTree jtrHierarchy;
  private JScrollPane jspHierarchy;
  private JLabel jlVersion;
  private JTextField jtfVersion;
  private JLabel jlSubject;
  private JDistinguishedName jdnSubject;
  private JLabel jlIssuer;
  private JDistinguishedName jdnIssuer;
  private JLabel jlSerialNumberHex;
  private JTextField jtfSerialNumberHex;
  private JLabel jlSerialNumberDec;
  private JTextField jtfSerialNumberDec;
  private JLabel jlValidFrom;
  private JTextField jtfValidFrom;
  private JLabel jlValidUntil;
  private JTextField jtfValidUntil;
  private JLabel jlPublicKey;
  private JTextField jtfPublicKey;
  private JButton jbViewPublicKeyDetails;
  private JLabel jlSignatureAlgorithm;
  private JTextField jtfSignatureAlgorithm;
  private JLabel jlFingerprint;
  private JCertificateFingerprint jcfFingerprint;
  private JButton jbExtensions;
  private JButton jbPem;
  private JButton jbAsn1;
  private JButton jbImport;
  private JButton jbExport;
  private JButton jbOK;
  private JButton jbVerify;
  private X509Certificate[] chain;

  private final Project project;

  public DViewCertificate(Project project, VirtualFile file,
                          X509Certificate[] certs, int importExport,
                          BiConsumer<DViewCertificate, String> consumer)
      throws CryptoException {
    super(project, file);
    this.project = project;
    this.importExport = importExport;
    this.chain = certs;
    Document document = FileDocumentManager.getInstance().getDocument(file);
    if (document != null) {
      document.addDocumentListener(new DocumentListener() {
        @Override
        public void documentChanged(@NotNull DocumentEvent event) {
          consumer.accept(DViewCertificate.this, document.getText());
        }
      }, this);
    }
    initComponents(certs);
  }

  public void restartView(X509Certificate[] certs) throws CryptoException {
    jtrHierarchy.setModel(new DefaultTreeModel(createCertificateNodes(certs)));
    TreeNode topNode = (TreeNode)jtrHierarchy.getModel().getRoot();
    expandTree(jtrHierarchy, new TreePath(topNode));
    // select (first) leaf in certificate tree
    DefaultMutableTreeNode firstLeaf =
        ((DefaultMutableTreeNode)topNode).getFirstLeaf();
    jtrHierarchy.setSelectionPath(new TreePath(firstLeaf.getPath()));
  }

  private void initComponents(X509Certificate[] certs) throws CryptoException {
    jlHierarchy =
        new JLabel(res.getString("DViewCertificate.jlHierarchy.text"));

    jtrHierarchy = new JTree(createCertificateNodes(certs));
    jtrHierarchy.setRowHeight(Math.max(18, jtrHierarchy.getRowHeight()));
    jtrHierarchy.getSelectionModel().setSelectionMode(
        TreeSelectionModel.SINGLE_TREE_SELECTION);
    ToolTipManager.sharedInstance().registerComponent(jtrHierarchy);
    jtrHierarchy.setCellRenderer(new CertificateTreeCellRend());
    jtrHierarchy.setRootVisible(false);

    TreeNode topNode = (TreeNode)jtrHierarchy.getModel().getRoot();
    expandTree(jtrHierarchy, new TreePath(topNode));

    jspHierarchy = PlatformUtil.createScrollPane(
        jtrHierarchy, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jspHierarchy.setPreferredSize(new Dimension(100, 75));

    jlVersion = new JLabel(res.getString("DViewCertificate.jlVersion.text"));

    jtfVersion = new JTextField(40);
    jtfVersion.setEditable(false);
    jtfVersion.setToolTipText(
        res.getString("DViewCertificate.jtfVersion.tooltip"));

    jlSubject = new JLabel(res.getString("DViewCertificate.jlSubject.text"));

    jdnSubject = new JDistinguishedName(
        res.getString("DViewCertificate.Subject.Title"), 40, false);
    jdnSubject.setToolTipText(
        res.getString("DViewCertificate.jdnSubject.tooltip"));

    jlIssuer = new JLabel(res.getString("DViewCertificate.jlIssuer.text"));

    jdnIssuer = new JDistinguishedName(
        res.getString("DViewCertificate.Issuer.Title"), 40, false);
    jdnIssuer.setToolTipText(
        res.getString("DViewCertificate.jdnIssuer.tooltip"));

    jlSerialNumberHex =
        new JLabel(res.getString("DViewCertificate.jlSerialNumberHex.text"));

    jtfSerialNumberHex = new JTextField(40);
    jtfSerialNumberHex.setEditable(false);
    jtfSerialNumberHex.setToolTipText(
        res.getString("DViewCertificate.jtfSerialNumberHex.tooltip"));
    jtfSerialNumberHex.setCaretPosition(0);

    jlSerialNumberDec =
        new JLabel(res.getString("DViewCertificate.jlSerialNumberDec.text"));

    jtfSerialNumberDec = new JTextField(40);
    jtfSerialNumberDec.setEditable(false);
    jtfSerialNumberDec.setToolTipText(
        res.getString("DViewCertificate.jtfSerialNumberDec.tooltip"));
    jtfSerialNumberDec.setCaretPosition(0);

    jlValidFrom =
        new JLabel(res.getString("DViewCertificate.jlValidFrom.text"));

    jtfValidFrom = new JTextField(40);
    jtfValidFrom.setEditable(false);
    jtfValidFrom.setToolTipText(
        res.getString("DViewCertificate.jtfValidFrom.tooltip"));

    jlValidUntil =
        new JLabel(res.getString("DViewCertificate.jlValidUntil.text"));

    jtfValidUntil = new JTextField(40);
    jtfValidUntil.setEditable(false);
    jtfValidUntil.setToolTipText(
        res.getString("DViewCertificate.jtfValidUntil.tooltip"));

    jlPublicKey =
        new JLabel(res.getString("DViewCertificate.jlPublicKey.text"));

    jtfPublicKey = new JTextField(40);
    jtfPublicKey.setEditable(false);
    jtfPublicKey.setToolTipText(
        res.getString("DViewCertificate.jtfPublicKey.tooltip"));

    jbViewPublicKeyDetails = new JButton();
    jbViewPublicKeyDetails.setToolTipText(
        res.getString("DViewCertificate.jbViewPublicKeyDetails.tooltip"));
    jbViewPublicKeyDetails.setIcon(
        new ImageIcon(Toolkit.getDefaultToolkit().createImage(
            getClass().getResource("images/viewpubkey.png"))));

    jlSignatureAlgorithm =
        new JLabel(res.getString("DViewCertificate.jlSignatureAlgorithm.text"));

    jtfSignatureAlgorithm = new JTextField(40);
    jtfSignatureAlgorithm.setEditable(false);
    jtfSignatureAlgorithm.setToolTipText(
        res.getString("DViewCertificate.jtfSignatureAlgorithm.tooltip"));

    jlFingerprint =
        new JLabel(res.getString("DViewCertificate.jlFingerprint.text"));

    jcfFingerprint = new JCertificateFingerprint(project, 30);

    jbExtensions =
        new JButton(res.getString("DViewCertificate.jbExtensions.text"));
    jbExtensions.setToolTipText(
        res.getString("DViewCertificate.jbExtensions.tooltip"));
    PlatformUtil.setMnemonic(
        jbExtensions,
        res.getString("DViewCertificate.jbExtensions.mnemonic").charAt(0));

    jbPem = new JButton(res.getString("DViewCertificate.jbPem.text"));
    jbPem.setToolTipText(res.getString("DViewCertificate.jbPem.tooltip"));
    PlatformUtil.setMnemonic(
        jbPem, res.getString("DViewCertificate.jbPem.mnemonic").charAt(0));

    jbAsn1 = new JButton(res.getString("DViewCertificate.jbAsn1.text"));
    jbAsn1.setToolTipText(res.getString("DViewCertificate.jbAsn1.tooltip"));
    PlatformUtil.setMnemonic(
        jbAsn1, res.getString("DViewCertificate.jbAsn1.mnemonic").charAt(0));

    jbImport = new JButton(
        res.getString("DViewCertificate.jbImportExport.import.text"));
    jbImport.setToolTipText(
        res.getString("DViewCertificate.jbImportExport.import.tooltip"));
    jbImport.setVisible(importExport == IMPORT ||
                        importExport == IMPORT_EXPORT);
    PlatformUtil.setMnemonic(
        jbImport,
        res.getString("DViewCertificate.jbImport.mnemonic").charAt(0));

    jbExport = new JButton(
        res.getString("DViewCertificate.jbImportExport.export.text"));
    jbExport.setToolTipText(
        res.getString("DViewCertificate.jbImportExport.export.tooltip"));
    jbExport.setVisible(importExport == EXPORT ||
                        importExport == IMPORT_EXPORT);
    PlatformUtil.setMnemonic(
        jbExport,
        res.getString("DViewCertificate.jbExport.mnemonic").charAt(0));

    jbOK = new JButton(res.getString("DViewCertificate.jbOK.text"));

    jbVerify = new JButton(res.getString("DViewCertificate.jbVerify.text"));
    jbVerify.setToolTipText(res.getString("DViewCertificate.jbVerify.tooltip"));
    PlatformUtil.setMnemonic(
        jbVerify,
        res.getString("DViewCertificate.jbVerify.mnemonic").charAt(0));

    Container pane = getContentPane();
    pane.setLayout(
        new MigLayout("insets dialog, fill", "[right]unrel[]", "[]unrel[]"));
    pane.add(jlHierarchy, "");
    pane.add(jspHierarchy, "sgx, wrap");
    pane.add(jlVersion, "");
    pane.add(jtfVersion, "sgx, wrap");
    pane.add(jlSubject, "");
    pane.add(jdnSubject, "wrap");
    pane.add(jlIssuer, "");
    pane.add(jdnIssuer, "wrap");
    pane.add(jlSerialNumberHex, "");
    pane.add(jtfSerialNumberHex, "wrap");
    pane.add(jlSerialNumberDec, "");
    pane.add(jtfSerialNumberDec, "wrap");
    pane.add(jlValidFrom, "");
    pane.add(jtfValidFrom, "wrap");
    pane.add(jlValidUntil, "");
    pane.add(jtfValidUntil, "wrap");
    pane.add(jlPublicKey, "");
    pane.add(jtfPublicKey, "spanx, split");
    pane.add(jbViewPublicKeyDetails, "wrap");
    pane.add(jlSignatureAlgorithm, "");
    pane.add(jtfSignatureAlgorithm, "wrap");
    pane.add(jlFingerprint, "");
    pane.add(jcfFingerprint, "spanx, growx, wrap");

    pane.add(jbImport, "hidemode 1, spanx, split");
    pane.add(jbExport, "hidemode 1");
    pane.add(jbExtensions, "");
    pane.add(jbPem, "");
    pane.add(jbVerify, "");
    pane.add(jbAsn1, "wrap");
    pane.add(new JSeparator(), "spanx, growx, wrap 15:push");
    // pane.add(jbOK, "spanx, tag ok");

    jtrHierarchy.addTreeSelectionListener(evt -> {
      try {
        CursorUtil.setCursorBusy(DViewCertificate.this);
        populateDetails();
      } finally {
        CursorUtil.setCursorFree(DViewCertificate.this);
      }
    });

    /*jbOK.addActionListener(evt -> okPressed());

    jbExport.addActionListener(evt -> {
        try {
            CursorUtil.setCursorBusy(DViewCertificate.this);
            exportPressed();
        } finally {
            CursorUtil.setCursorFree(DViewCertificate.this);
        }
    });

    jbExtensions.addActionListener(evt -> {
        try {
            CursorUtil.setCursorBusy(DViewCertificate.this);
            extensionsPressed();
        } finally {
            CursorUtil.setCursorFree(DViewCertificate.this);
        }
    });

    jbImport.addActionListener(evt -> {
        try {
            CursorUtil.setCursorBusy(DViewCertificate.this);
            importPressed();
        } finally {
            CursorUtil.setCursorFree(DViewCertificate.this);
        }
    });

    jbViewPublicKeyDetails.addActionListener(evt -> {
        try {
            CursorUtil.setCursorBusy(DViewCertificate.this);
            pubKeyDetailsPressed();
        } finally {
            CursorUtil.setCursorFree(DViewCertificate.this);
        }
    });*/

    jbPem.addActionListener(evt -> {
      try {
        CursorUtil.setCursorBusy(DViewCertificate.this);
        pemEncodingPressed();
      } finally {
        CursorUtil.setCursorFree(DViewCertificate.this);
      }
    });

    /*jbAsn1.addActionListener(evt -> {
        try {
            CursorUtil.setCursorBusy(DViewCertificate.this);
            asn1DumpPressed();
        } finally {
            CursorUtil.setCursorFree(DViewCertificate.this);
        }
    });

    jbVerify.addActionListener(evt -> {
        try {
            CursorUtil.setCursorBusy(DViewCertificate.this);
            verifyPressed();
        } finally {
            CursorUtil.setCursorFree(DViewCertificate.this);
        }
    });*/

    // select (first) leaf in certificate tree
    DefaultMutableTreeNode firstLeaf =
        ((DefaultMutableTreeNode)topNode).getFirstLeaf();
    jtrHierarchy.setSelectionPath(new TreePath(firstLeaf.getPath()));

    // getRootPane().setDefaultButton(jbOK);

    // SwingUtilities.invokeLater(() -> jbOK.requestFocus());
  }

  private void expandTree(JTree tree, TreePath parent) {
    TreeNode node = (TreeNode)parent.getLastPathComponent();
    if (node.getChildCount() >= 0) {
      for (Enumeration<?> enumNodes = node.children();
           enumNodes.hasMoreElements();) {
        TreeNode subNode = (TreeNode)enumNodes.nextElement();
        TreePath path = parent.pathByAddingChild(subNode);
        expandTree(tree, path);
      }
    }

    tree.expandPath(parent);
  }

  private X509Certificate getSelectedCertificate() {
    TreePath[] selections = jtrHierarchy.getSelectionPaths();

    if (selections == null) {
      return null;
    }

    return (X509Certificate)((DefaultMutableTreeNode)selections[0]
                                 .getLastPathComponent())
        .getUserObject();
  }

  private void populateDetails() {
    X509Certificate cert = getSelectedCertificate();

    if (cert == null) {
      jdnSubject.setEnabled(false);
      jdnIssuer.setEnabled(false);
      jbViewPublicKeyDetails.setEnabled(false);
      jcfFingerprint.setEnabled(false);
      jbExtensions.setEnabled(false);
      jbPem.setEnabled(false);
      jbAsn1.setEnabled(false);

      jtfVersion.setText("");
      jdnSubject.setDistinguishedName(null);
      jdnIssuer.setDistinguishedName(null);
      jtfSerialNumberHex.setText("");
      jtfSerialNumberDec.setText("");
      jtfValidFrom.setText("");
      jtfValidUntil.setText("");
      jtfPublicKey.setText("");
      jtfSignatureAlgorithm.setText("");
      jcfFingerprint.setEncodedCertificate(null);
    } else {
      jdnSubject.setEnabled(true);
      jdnIssuer.setEnabled(true);
      jbViewPublicKeyDetails.setEnabled(true);
      jbExtensions.setEnabled(true);
      jbPem.setEnabled(true);
      jbAsn1.setEnabled(true);

      try {
        Date currentDate = new Date();

        Date startDate = cert.getNotBefore();
        Date endDate = cert.getNotAfter();

        boolean notYetValid = currentDate.before(startDate);
        boolean noLongerValid = currentDate.after(endDate);

        jtfVersion.setText(Integer.toString(cert.getVersion()));
        jtfVersion.setCaretPosition(0);

        jdnSubject.setDistinguishedName(X500NameUtils.x500PrincipalToX500Name(
            cert.getSubjectX500Principal()));

        jdnIssuer.setDistinguishedName(X500NameUtils.x500PrincipalToX500Name(
            cert.getIssuerX500Principal()));

        jtfSerialNumberHex.setText(X509CertUtil.getSerialNumberAsHex(cert));
        jtfSerialNumberHex.setCaretPosition(0);

        jtfSerialNumberDec.setText(X509CertUtil.getSerialNumberAsDec(cert));
        jtfSerialNumberDec.setCaretPosition(0);

        jtfValidFrom.setText(StringUtils.formatDate(startDate));

        if (notYetValid) {
          jtfValidFrom.setText(MessageFormat.format(
              res.getString("DViewCertificate.jtfValidFrom.notyetvalid.text"),
              jtfValidFrom.getText()));
          jtfValidFrom.setForeground(Color.red);
        } else {
          jtfValidFrom.setForeground(jtfVersion.getForeground());
        }
        jtfValidFrom.setCaretPosition(0);

        jtfValidUntil.setText(StringUtils.formatDate(endDate));

        if (noLongerValid) {
          jtfValidUntil.setText(MessageFormat.format(
              res.getString("DViewCertificate.jtfValidUntil.expired.text"),
              jtfValidUntil.getText()));
          jtfValidUntil.setForeground(Color.red);
        } else {
          jtfValidUntil.setForeground(jtfVersion.getForeground());
        }
        jtfValidUntil.setCaretPosition(0);

        KeyInfo keyInfo = KeyPairUtil.getKeyInfo(cert.getPublicKey());
        jtfPublicKey.setText(keyInfo.getAlgorithm());
        Integer keySize = keyInfo.getSize();

        if (keySize != null) {
          jtfPublicKey.setText(MessageFormat.format(
              res.getString("DViewCertificate.jtfPublicKey.text"),
              jtfPublicKey.getText(), "" + keySize));
        } else {
          jtfPublicKey.setText(MessageFormat.format(
              res.getString("DViewCertificate.jtfPublicKey.text"),
              jtfPublicKey.getText(), "?"));
        }
        if (cert.getPublicKey() instanceof ECPublicKey) {
          jtfPublicKey.setText(jtfPublicKey.getText() + " (" +
                               keyInfo.getDetailedAlgorithm() + ")");
        }
        jtfPublicKey.setCaretPosition(0);

        jtfSignatureAlgorithm.setText(
            X509CertUtil.getCertificateSignatureAlgorithm(cert));
        jtfSignatureAlgorithm.setCaretPosition(0);

        byte[] encodedCertificate;
        try {
          encodedCertificate = cert.getEncoded();
        } catch (CertificateEncodingException ex) {
          throw new CryptoException(
              res.getString(
                  "DViewCertificate.NoGetEncodedCert.exception.message"),
              ex);
        }

        jcfFingerprint.setEncodedCertificate(encodedCertificate);

        // jcfFingerprint.setFingerprintAlg(DigestType.SHAKE256);

        Set<?> critExts = cert.getCriticalExtensionOIDs();
        Set<?> nonCritExts = cert.getNonCriticalExtensionOIDs();

        if ((critExts != null && !critExts.isEmpty()) ||
            (nonCritExts != null && !nonCritExts.isEmpty())) {
          jbExtensions.setEnabled(true);
        } else {
          jbExtensions.setEnabled(false);
        }
      } catch (CryptoException e) {
        DError.displayError(this.project, e);
        dispose();
      }
    }

    disableUnImplementedButtons();
  }

  public void disableUnImplementedButtons() {
    // TODO delete each line for button implemented
    jbImport.setEnabled(false);
    jbExport.setEnabled(false);
    jbExtensions.setEnabled(false);
    jbVerify.setEnabled(false);
    jbAsn1.setEnabled(false);
  }

  private DefaultMutableTreeNode
  createCertificateNodes(X509Certificate[] certs) {
    DefaultMutableTreeNode certsNode = new DefaultMutableTreeNode();

    Set<X509Certificate> originalSet =
        new TreeSet<>(new X509CertificateComparator());
    Collections.addAll(originalSet, certs);
    Set<X509Certificate> certSet =
        new TreeSet<>(new X509CertificateComparator());
    Collections.addAll(certSet, certs);

    // first find certs with no issuer in set and add them to the tree
    certSet.stream()
        .filter(cert -> !isIssuerInSet(cert, certSet))
        .forEach(cert -> certsNode.add(new DefaultMutableTreeNode(cert)));
    certSet.removeIf(cert -> !isIssuerInSet(cert, originalSet));

    // then add root certs
    certSet.stream()
        .filter(X509CertUtil::isCertificateSelfSigned)
        .forEach(cert -> certsNode.add(new DefaultMutableTreeNode(cert)));
    certSet.removeIf(X509CertUtil::isCertificateSelfSigned);

    // then attach the other certs to their issuers
    while (!certSet.isEmpty()) {

      List<X509Certificate> toBeRemoved = new ArrayList<>();
      for (X509Certificate cert : certSet) {

        DefaultMutableTreeNode issuerNode = findIssuer(cert, certsNode);

        if (issuerNode != null) {
          issuerNode.add(new DefaultMutableTreeNode(cert));
          toBeRemoved.add(cert);
        }
      }
      certSet.removeAll(toBeRemoved);
    }

    return certsNode;
  }

  private DefaultMutableTreeNode findIssuer(X509Certificate cert,
                                            DefaultMutableTreeNode node) {
    // Matches on certificate's distinguished name

    // If certificate is self-signed then finding an issuer is irrelevant
    if (cert.getIssuerX500Principal().equals(cert.getSubjectX500Principal())) {
      return null;
    }

    Object nodeObj = node.getUserObject();

    if (nodeObj instanceof X509Certificate) {
      X509Certificate nodeCert = (X509Certificate)nodeObj;

      if (cert.getIssuerX500Principal().equals(
              nodeCert.getSubjectX500Principal())) {
        return node;
      }
    }

    for (int i = 0; i < node.getChildCount(); i++) {
      DefaultMutableTreeNode issuerNode =
          findIssuer(cert, (DefaultMutableTreeNode)node.getChildAt(i));

      if (issuerNode != null) {
        return issuerNode;
      }
    }

    return null;
  }

  private boolean isIssuerInSet(X509Certificate cert,
                                Set<X509Certificate> certSet) {
    // Matches on certificate's distinguished name

    // If certificate is self-signed then finding an issuer is irrelevant
    if (cert.getIssuerX500Principal().equals(cert.getSubjectX500Principal())) {
      return false;
    }

    for (X509Certificate certToTest : certSet) {
      if (cert.getIssuerX500Principal().equals(
              certToTest.getSubjectX500Principal())) {
        return true;
      }
    }

    return false;
  }

  @Override
  public @NotNull JComponent getComponent() {
    return getRootPane();
  }

  @Override
  public @Nullable JComponent getPreferredFocusedComponent() {
    return getRootPane();
  }

  private class X509CertificateComparator
      implements Comparator<X509Certificate> {

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(X509Certificate cert1, X509Certificate cert2) {

      // Compare certificates for equality. Where all we care about is if
      // the certificates are equal or not - the order is unimportant
      if (cert1.equals(cert2)) {
        return 0;
      }

      // Compare on subject DN
      int i = cert1.getSubjectX500Principal().toString().compareTo(
          cert2.getSubjectX500Principal().toString());

      if (i != 0) {
        return i;
      }

      // Compare on issuer DN
      i = cert1.getIssuerX500Principal().toString().compareTo(
          cert2.getIssuerX500Principal().toString());

      if (i != 0) {
        return i;
      }

      // If all else fails then compare serial numbers - if this is the
      // same and the DNs are the same then it is probably the same certificate
      // anyway
      return cert1.getSerialNumber()
          .subtract(cert2.getSerialNumber())
          .intValue();
    }
  }

  private void pemEncodingPressed() {
    X509Certificate cert = getSelectedCertificate();

    try {
      DViewPem dViewCertPem = new DViewPem(
          project, res.getString("DViewCertificate.Pem.Title"), cert);
      dViewCertPem.show();
    } catch (CryptoException e) {
      DError.displayError(project, e);
    }
  }
}
