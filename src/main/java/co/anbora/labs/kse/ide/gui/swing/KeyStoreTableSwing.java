package co.anbora.labs.kse.ide.gui.swing;

import co.anbora.labs.kse.ide.gui.TableEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kse.crypto.CryptoException;
import org.kse.crypto.Password;
import org.kse.crypto.keystore.KeyStoreType;
import org.kse.crypto.keystore.KeyStoreUtil;
import org.kse.gui.AddKeyStore;
import org.kse.gui.KeyStoreTableColumns;
import org.kse.gui.KeyStoreTableModel;
import co.anbora.labs.kse.ide.gui.render.ColumnRender;
import org.kse.gui.error.DError;
import org.kse.gui.statusbar.StatusBar;
import org.kse.utilities.history.KeyStoreHistory;
import org.kse.utilities.history.KeyStoreState;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class KeyStoreTableSwing extends TableEditor
        implements StatusBar, AddKeyStore {

    private final ResourceBundle res = ResourceBundle.getBundle("org/kse/gui/resources");

    private final int autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS;

    private JPanel panelMain;
    private JPasswordField passwordField;
    private JButton OKButton;
    private JTable tblEditor;
    private JLabel jlStatusBar;

    private KeyStoreHistory activeHistory;
    private ColumnRender tableCustomRenderer;
    private final KeyStoreTableColumns keyStoreTableColumns = new KeyStoreTableColumns();

    public KeyStoreTableSwing(@NotNull Project projectArg, @NotNull VirtualFile fileArg) {
        super(projectArg, fileArg);
        tableCustomRenderer = new ColumnRender(keyStoreTableColumns, res);
        createUIComponents();
        OKButton.addActionListener(e -> {
            try {
                File keyStoreFile = getFile().toNioPath().toFile();
                Password password = new Password(passwordField.getPassword());
                KeyStore keyStore = loadKeyStore(keyStoreFile, password);
                addKeyStore(keyStore, keyStoreFile, password);
            } catch (Exception ex) {
                DError.displayError(projectArg, ex);
            }
            setDefaultStatusBarText();
        });
        setDefaultStatusBarText();
    }

    private KeyStore loadKeyStore(File keyStoreFile, Password password) {
        // try to load keystore
        try {
            return KeyStoreUtil.load(keyStoreFile, password);
        } catch (CryptoException | FileNotFoundException klex) {
            // show icon error
            return null;
        }
    }

    @Override
    public void addKeyStore(KeyStore keyStore, File keyStoreFile, Password password) throws GeneralSecurityException, CryptoException {
        KeyStoreHistory history = new KeyStoreHistory(keyStore, keyStoreFile, password);
        setActiveHistory(history);
        ((KeyStoreTableModel) tblEditor.getModel()).load(history);
    }

    private void setActiveHistory(KeyStoreHistory history) {
        this.activeHistory = history;
    }

    /**
     * Get the active KeyStore history.
     *
     * @return The KeyStore history or null if no KeyStore is active
     */
    public KeyStoreHistory getActiveKeyStoreHistory() {
        return this.activeHistory;
    }

    /**
     * Get the aliases of all entries currently selected in the KeyStore
     *
     * @return Selected aliases (may be an empty array, but never null)
     */
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

    @Override
    public void setStatusBarText(String status) {
        jlStatusBar.setText(status);
    }

    @Override
    public void setDefaultStatusBarText() {
        KeyStoreHistory history = getActiveKeyStoreHistory();

        if (history == null) {
            setStatusBarText(res.getString("KseFrame.noKeyStore.statusbar"));
        } else {
            setStatusBarText(getKeyStoreStatusText(history));
        }
    }

    private String getKeyStoreStatusText(KeyStoreHistory history) {
        // Status Text: 'KeyStore Type, Size, Path'
        KeyStoreState currentState = history.getCurrentState();

        KeyStore ksLoaded = currentState.getKeyStore();

        int size;
        try {
            size = ksLoaded.size();
        } catch (KeyStoreException ex) {
            //DError.displayError(frame, ex);
            return "";
        }

        KeyStoreType keyStoreType = currentState.getType();
        String[] aliases = getSelectedEntryAliases();

        return MessageFormat.format(res.getString("KseFrame.entries.statusbar"),
                keyStoreType.friendly(), size, aliases.length, history.getPath());
    }
}
