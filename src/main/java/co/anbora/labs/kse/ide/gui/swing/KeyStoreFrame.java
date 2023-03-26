package co.anbora.labs.kse.ide.gui.swing;

import co.anbora.labs.kse.ide.gui.TableEditor;
import co.anbora.labs.kse.ide.gui.render.ColumnRender;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.util.messages.MessageBus;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kse.crypto.CryptoException;
import org.kse.crypto.Password;
import org.kse.gui.*;
import org.kse.gui.actions.NewAction;
import org.kse.gui.actions.OpenAction;
import org.kse.gui.actions.PressEnterAction;
import org.kse.gui.actions.behavior.ActionBehavior;
import org.kse.gui.actions.behavior.OpenKeyStoreImpl;
import org.kse.gui.statusbar.StatusBar;
import org.kse.gui.statusbar.StatusBarImpl;
import org.kse.utilities.history.KeyStoreHistory;

public class KeyStoreFrame
    extends TableEditor implements AddKeyStore, HistoryKeyStore {

  private final ResourceBundle res =
      ResourceBundle.getBundle("org/kse/gui/resources");

  private final int autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS;

  private JPanel panelMain;
  private JTable tblEditor;
  private JLabel jlStatusBar;
  private JToolBar jtbToolBar;
  private JPanel centerPanel;

  private final UnlockPanel customUnlockPanel =
      new UnlockPanel("Unlock Keystore");

  private KeyStoreHistory activeHistory;
  private final ColumnRender tableCustomRenderer;
  private final KeyStoreTableColumns keyStoreTableColumns =
      new KeyStoreTableColumns();
  private final StatusBar statusBar;
  private OpenAction openAction;

  // Toolbar controls
  private JButton jbNew;
  private JButton jbOpen;
  private JButton jbSave;
  private JButton jbUndo;
  private JButton jbRedo;
  private JButton jbCut;
  private JButton jbCopy;
  private JButton jbPaste;
  private JButton jbGenerateKeyPair;
  private JButton jbGenerateSecretKey;
  private JButton jbImportTrustedCertificate;
  private JButton jbImportKeyPair;
  private JButton jbSetPassword;
  private JButton jbProperties;
  private JButton jbExamineFile;
  private JButton jbExamineClipboard;
  private JButton jbExamineSsl;
  private JButton jbHelp;

  // Actions
  private NewAction newAction;

  public KeyStoreFrame(@NotNull Project projectArg,
                       @NotNull VirtualFile fileArg) {
    super(projectArg, fileArg);
    tableCustomRenderer = new ColumnRender(keyStoreTableColumns, res);
    statusBar = new StatusBarImpl(projectArg, jlStatusBar, this);

    createUIComponents();
    initUnlockPanel(projectArg);
    initActions(projectArg, statusBar);
    initToolbar();

    statusBar.setDefaultStatusBarText();

    addFileListener(projectArg.getMessageBus());
  }

  private void initActions(Project projectArg, StatusBar statusBar) {
    newAction = new NewAction(projectArg, statusBar);
  }

  private void initUnlockPanel(@NotNull Project projectArg) {
    ActionBehavior actionBehavior =
        new OpenKeyStoreImpl(projectArg, statusBar, this, this,
                             customUnlockPanel.getPasswordField());
    openAction = new OpenAction(projectArg, statusBar, actionBehavior);

    customUnlockPanel.addActionListener(openAction);
    customUnlockPanel.addKeyListener(
        new PressEnterAction(projectArg, statusBar, actionBehavior));
  }

  private void addFileListener(MessageBus messageBus) {
    messageBus.connect().subscribe(
        VirtualFileManager.VFS_CHANGES, new BulkFileListener() {
          @Override
          public void after(@NotNull List<? extends VFileEvent> events) {
            events.forEach((Consumer<VFileEvent>)vFileEvent -> {
              if (vFileEvent instanceof VFileContentChangeEvent) {
                openAction.doAction();
              }
            });
          }
        });
  }

  private void initToolbar() {
    /*jbNew = new JButton();
    jbNew.setAction(newAction);
    jbNew.setText(null);
    PlatformUtil.setMnemonic(jbNew, 0);
    jbNew.setFocusable(false);
    jbNew.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent evt) {
        statusBar.setStatusBarText((String)
    newAction.getValue(Action.LONG_DESCRIPTION));
      }

      @Override
      public void mouseExited(MouseEvent evt) {
        statusBar.setDefaultStatusBarText();
      }
    });

    jtbToolBar.setFloatable(false);
    jtbToolBar.setRollover(true);

    jtbToolBar.add(jbNew);
    jtbToolBar.add(jbSave);

    jtbToolBar.addSeparator();

    jtbToolBar.add(jbUndo);
    jtbToolBar.add(jbRedo);

    jtbToolBar.addSeparator();

    jtbToolBar.add(jbCut);
    jtbToolBar.add(jbCopy);
    jtbToolBar.add(jbPaste);

    jtbToolBar.addSeparator();

    jtbToolBar.add(jbGenerateKeyPair);
    jtbToolBar.add(jbGenerateSecretKey);
    jtbToolBar.add(jbImportTrustedCertificate);
    jtbToolBar.add(jbImportKeyPair);
    jtbToolBar.add(jbSetPassword);
    jtbToolBar.add(jbProperties);

    jtbToolBar.addSeparator();

    jtbToolBar.add(jbExamineFile);
    jtbToolBar.add(jbExamineClipboard);
    jtbToolBar.add(jbExamineSsl);

    jtbToolBar.addSeparator();

    jtbToolBar.add(jbHelp);*/
  }

  @Override
  public void addKeyStore(KeyStore keyStore, File keyStoreFile,
                          Password password)
      throws GeneralSecurityException, CryptoException {
    KeyStoreHistory history =
        new KeyStoreHistory(keyStore, keyStoreFile, password);
    setActiveHistory(history);
    ((KeyStoreTableModel)tblEditor.getModel()).load(history);
    customUnlockPanel.setVisible(false);
  }

  private void setActiveHistory(KeyStoreHistory history) {
    this.activeHistory = history;
  }

  /**
   * Get the active KeyStore history.
   *
   * @return The KeyStore history or null if no KeyStore is active
   */
  @Override
  public KeyStoreHistory getActiveKeyStoreHistory() {
    return this.activeHistory;
  }

  /**
   * Get the aliases of all entries currently selected in the KeyStore
   *
   * @return Selected aliases (may be an empty array, but never null)
   */
  @Override
  public String[] getSelectedEntryAliases() {
    JTable jtKeyStore = getActiveKeyStoreTable();
    int[] rows = jtKeyStore.getSelectedRows();

    String[] retval = new String[rows.length];

    for (int i = 0; i < rows.length; i++) {
      retval[i] = (String)jtKeyStore.getValueAt(rows[i], 3);
    }

    return retval;
  }

  private JTable getActiveKeyStoreTable() { return tblEditor; }

  private void createUIComponents() {
    centerPanel.add(customUnlockPanel.getComponent(), BorderLayout.NORTH);

    KeyStoreTableModel ksModel = new KeyStoreTableModel(keyStoreTableColumns);
    tblEditor.setModel(ksModel);

    RowSorter<KeyStoreTableModel> sorter = new TableRowSorter<>(ksModel);
    tblEditor.setRowSorter(sorter);

    tblEditor.setShowGrid(false);
    tblEditor.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    tblEditor.getTableHeader().setReorderingAllowed(false);
    tblEditor.setAutoResizeMode(autoResizeMode);
    tblEditor.setRowHeight(Math.max(
        18,
        tblEditor.getRowHeight())); // min. height of 18 because of 16x16 icons

    tableCustomRenderer.accept(tblEditor);
  }

  @Override
  public @NotNull JComponent getComponent() {
    return this.panelMain;
  }

  @Override
  public @Nullable JComponent getPreferredFocusedComponent() {
    return this.tblEditor;
  }
}
