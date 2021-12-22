package co.anbora.labs.kse.ide.gui.swing;

import co.anbora.labs.kse.ide.gui.TableEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kse.gui.*;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ResourceBundle;

public class KeyStoreTableSwing extends TableEditor {

    private static final double FF = 0.7;
    private static int iFontSize = (int) (LnfUtil.getDefaultFontSize() * FF);

    private int autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS;

    private JPanel panelMain;
    private JPasswordField passwordField1;
    private JButton OKButton;
    private JTable tblEditor;

    private KeyStoreTableColumns keyStoreTableColumns = new KeyStoreTableColumns();

    public KeyStoreTableSwing(@NotNull Project projectArg, @NotNull VirtualFile fileArg) {
        super(projectArg, fileArg);
    }

    private void createUIComponents() {
        KeyStoreTableModel ksModel = new KeyStoreTableModel(keyStoreTableColumns);
        tblEditor = new JBTable(ksModel);

        RowSorter<KeyStoreTableModel> sorter = new TableRowSorter<>(ksModel);
        tblEditor.setRowSorter(sorter);

        tblEditor.setShowGrid(false);
        tblEditor.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblEditor.getTableHeader().setReorderingAllowed(false);
        tblEditor.setAutoResizeMode(autoResizeMode);
        tblEditor.setRowHeight(Math.max(18, tblEditor.getRowHeight())); // min. height of 18 because of 16x16 icons

        addCustomRenderers(tblEditor);
    }

    private void addCustomRenderers(JTable jtKeyStore) {
        ResourceBundle res = ResourceBundle.getBundle("org/kse/gui/resources");
        jtKeyStore.setAutoResizeMode(autoResizeMode);
        for (int i = 0; i < jtKeyStore.getColumnCount(); i++) {
            int width = 0;
            TableColumn column = jtKeyStore.getColumnModel().getColumn(i);

            // new, size columns based on title. Columns are resizable by default
            // http://www.java2s.com/Tutorial/Java/0240__Swing/Setcolumnwidthbasedoncellrenderer.htm
            for (int row = 0; row < jtKeyStore.getRowCount(); row++) {
                width = 0;
                TableCellRenderer renderer = jtKeyStore.getCellRenderer(row, i);
                Component comp = renderer.getTableCellRendererComponent(jtKeyStore, jtKeyStore.getValueAt(row, i),
                        false, false, row, i);
                width = Math.max(width, comp.getPreferredSize().width);
            }
            int l = width;

            if (i == keyStoreTableColumns.colEntryName()) {
                column.setMinWidth((2 + res.getString("KeyStoreTableModel.NameColumn")).length() * iFontSize);
                column.setPreferredWidth(
                        Math.max(1 + res.getString("KeyStoreTableModel.NameColumn").length(), 20) * iFontSize);
                column.setMaxWidth(
                        Math.max(2 + res.getString("KeyStoreTableModel.NameColumn").length(), 50) * iFontSize);
            }
            if (i == keyStoreTableColumns.colAlgorithm()) {
                column.setMinWidth((4) * iFontSize);
                column.setPreferredWidth(
                        Math.max(1 + res.getString("KeyStoreTableModel.AlgorithmColumn").length(), 4) * iFontSize);
                column.setMaxWidth(
                        Math.max(2 + res.getString("KeyStoreTableModel.AlgorithmColumn").length(), 5) * iFontSize);
            }
            if (i == keyStoreTableColumns.colKeySize()) {
                column.setMinWidth((4) * iFontSize);
                column.setPreferredWidth(
                        Math.max(1 + res.getString("KeyStoreTableModel.KeySizeColumn").length(), (l + 1)) * iFontSize);
                column.setMaxWidth(
                        Math.max(2 + res.getString("KeyStoreTableModel.KeySizeColumn").length(), l + 1) * iFontSize);
            }
            if (i == keyStoreTableColumns.colCurve()) {
                column.setMinWidth(8 * iFontSize);
                column.setPreferredWidth(
                        1 + Math.max(res.getString("KeyStoreTableModel.CurveColumn").length(), l) * iFontSize);
                column.setMaxWidth(2
                        + Math.max("brainpool999r1".length(), res.getString("KeyStoreTableModel.CurveColumn").length())
                        * iFontSize);
            }
            if (i == keyStoreTableColumns.colCertificateExpiry()) {
                l = "20.00.2000 00:00:00 MESZ".length();
                column.setMinWidth("20.00.2000".length() * iFontSize);
                column.setPreferredWidth(
                        1 + Math.max(res.getString("KeyStoreTableModel.CertExpiryColumn").length(), l) * iFontSize);
                column.setMaxWidth(
                        2 + Math.max(res.getString("KeyStoreTableModel.CertExpiryColumn").length(), l) * iFontSize);
            }
            if (i == keyStoreTableColumns.colLastModified()) {
                l = "20.09.2000 00:00:00 MESZ".length();
                column.setMinWidth("20.00.2000".length() * iFontSize);
                column.setPreferredWidth(
                        1 + Math.max(res.getString("KeyStoreTableModel.LastModifiedColumn").length(), l) * iFontSize);
                column.setMaxWidth(
                        2 + Math.max(res.getString("KeyStoreTableModel.LastModifiedColumn").length(), l) * iFontSize);
            }
            if (i == keyStoreTableColumns.colAKI()) {
                l = 41;
                column.setMinWidth(8 * iFontSize);
                column.setPreferredWidth(
                        1 + Math.max(res.getString("KeyStoreTableModel.AKIColumn").length(), l) * iFontSize);
                column.setMaxWidth(
                        Math.max(2 + res.getString("KeyStoreTableModel.AKIColumn").length(), (l + 1)) * iFontSize);
            }
            if (i == keyStoreTableColumns.colSKI()) {
                l = 41;
                column.setMinWidth(8 * iFontSize);
                column.setPreferredWidth(
                        Math.max(2 + res.getString("KeyStoreTableModel.SKIColumn").length(), (l + 1)) * iFontSize);
                column.setMaxWidth(
                        Math.max(2 + res.getString("KeyStoreTableModel.SKIColumn").length(), (l + 1)) * iFontSize);
            }
            if (i == keyStoreTableColumns.colIssuerCN()) {
                column.setMinWidth(8 * iFontSize);
                column.setPreferredWidth(
                        Math.max(2 + res.getString("KeyStoreTableModel.IssuerCNColumn").length(), (l + 1)) * iFontSize);
                column.setMaxWidth(100 * iFontSize);
            }
            if (i == keyStoreTableColumns.colIssuerDN()) {
                column.setMinWidth(8 * iFontSize);
                column.setPreferredWidth(
                        Math.max(2 + res.getString("KeyStoreTableModel.IssuerDNColumn").length(), (l + 1)) * iFontSize);
                column.setMaxWidth(100 * iFontSize);
            }
            if (i == keyStoreTableColumns.colIssuerO()) {
                column.setMinWidth(8 * iFontSize);
                column.setPreferredWidth(
                        Math.max(2 + res.getString("KeyStoreTableModel.IssuerOColumn").length(), (l + 1)) * iFontSize);
                column.setMaxWidth(100 * iFontSize);
            }

            column.setHeaderRenderer(new KeyStoreTableHeadRend(jtKeyStore.getTableHeader().getDefaultRenderer()));
            column.setCellRenderer(new KeyStoreTableCellRend());
        }
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
