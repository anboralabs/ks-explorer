package co.anbora.labs.kse.ide.gui.swing;

import co.anbora.labs.kse.ide.gui.TableEditor;
import co.anbora.labs.kse.ide.gui.render.ColumnRender;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kse.crypto.CryptoException;
import org.kse.crypto.Password;
import org.kse.gui.AddKeyStore;
import org.kse.gui.HistoryKeyStore;
import org.kse.gui.KeyStoreTableColumns;
import org.kse.gui.KeyStoreTableModel;
import org.kse.gui.actions.OpenAction;
import org.kse.gui.statusbar.StatusBar;
import org.kse.gui.statusbar.StatusBarImpl;
import org.kse.utilities.history.KeyStoreHistory;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.io.File;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ResourceBundle;

public class KeyStoreTableSwing extends TableEditor
        implements AddKeyStore, HistoryKeyStore {

    private final ResourceBundle res = ResourceBundle.getBundle("org/kse/gui/resources");

    private final int autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS;

    private JPanel panelMain;
    private JPasswordField passwordField;
    private JButton OKButton;
    private JTable tblEditor;
    private JLabel jlStatusBar;
    private JPanel unlockPanel;

    private KeyStoreHistory activeHistory;
    private final ColumnRender tableCustomRenderer;
    private final KeyStoreTableColumns keyStoreTableColumns = new KeyStoreTableColumns();
    private final StatusBar statusBar;

    public KeyStoreTableSwing(@NotNull Project projectArg, @NotNull VirtualFile fileArg) {
        super(projectArg, fileArg);
        tableCustomRenderer = new ColumnRender(keyStoreTableColumns, res);
        statusBar = new StatusBarImpl(projectArg, jlStatusBar, this);

        createUIComponents();
        OKButton.addActionListener(new OpenAction(projectArg, statusBar, this, this, passwordField));
        statusBar.setDefaultStatusBarText();
    }

    @Override
    public void addKeyStore(KeyStore keyStore, File keyStoreFile, Password password) throws GeneralSecurityException, CryptoException {
        KeyStoreHistory history = new KeyStoreHistory(keyStore, keyStoreFile, password);
        setActiveHistory(history);
        ((KeyStoreTableModel) tblEditor.getModel()).load(history);
        unlockPanel.setVisible(false);
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
            retval[i] = (String) jtKeyStore.getValueAt(rows[i], 3);
        }

        return retval;
    }

    private JTable getActiveKeyStoreTable() {
        return tblEditor;
    }

    private void createUIComponents() {
        KeyStoreTableModel ksModel = new KeyStoreTableModel(keyStoreTableColumns);
        tblEditor.setModel(ksModel);

        RowSorter<KeyStoreTableModel> sorter = new TableRowSorter<>(ksModel);
        tblEditor.setRowSorter(sorter);

        tblEditor.setShowGrid(false);
        tblEditor.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblEditor.getTableHeader().setReorderingAllowed(false);
        tblEditor.setAutoResizeMode(autoResizeMode);
        tblEditor.setRowHeight(Math.max(18, tblEditor.getRowHeight())); // min. height of 18 because of 16x16 icons

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
