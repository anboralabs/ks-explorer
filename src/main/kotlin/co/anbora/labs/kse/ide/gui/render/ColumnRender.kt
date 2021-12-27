package co.anbora.labs.kse.ide.gui.render

import org.kse.gui.KeyStoreTableCellRend
import org.kse.gui.KeyStoreTableColumns
import org.kse.gui.KeyStoreTableHeadRend
import org.kse.gui.LnfUtil
import java.util.*
import java.util.function.Consumer
import javax.swing.JTable
import kotlin.math.max

private const val FF = 0.7

class ColumnRender(
    private val keyStoreTableColumns: KeyStoreTableColumns,
    private val res: ResourceBundle
): Consumer<JTable> {

    private val iFontSize = (LnfUtil.getDefaultFontSize() * FF).toInt()

    private val autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
    
    override fun accept(jtKeyStore: JTable) {
        jtKeyStore.autoResizeMode = autoResizeMode
        for (i in 0 until jtKeyStore.columnCount) {
            var width = 0
            val column = jtKeyStore.columnModel.getColumn(i)

            // new, size columns based on title. Columns are resizable by default
            // http://www.java2s.com/Tutorial/Java/0240__Swing/Setcolumnwidthbasedoncellrenderer.htm
            for (row in 0 until jtKeyStore.rowCount) {
                width = 0
                val renderer = jtKeyStore.getCellRenderer(row, i)
                val comp = renderer.getTableCellRendererComponent(
                    jtKeyStore, jtKeyStore.getValueAt(row, i),
                    false, false, row, i
                )
                width = max(width, comp.preferredSize.width)
            }
            var l = width
            if (i == keyStoreTableColumns.colEntryName()) {
                column.minWidth =
                    (2.toString() + res.getString("KeyStoreTableModel.NameColumn")).length * iFontSize
                column.preferredWidth = max(
                    1 + res.getString("KeyStoreTableModel.NameColumn").length,
                    20
                ) * iFontSize
                column.maxWidth = max(
                    2 + res.getString("KeyStoreTableModel.NameColumn").length,
                    50
                ) * iFontSize
            }
            if (i == keyStoreTableColumns.colAlgorithm()) {
                column.minWidth = 4 * iFontSize
                column.preferredWidth = max(
                    1 + res.getString("KeyStoreTableModel.AlgorithmColumn").length,
                    4
                ) * iFontSize
                column.maxWidth = max(
                    2 + res.getString("KeyStoreTableModel.AlgorithmColumn").length,
                    5
                ) * iFontSize
            }
            if (i == keyStoreTableColumns.colKeySize()) {
                column.minWidth = 4 * iFontSize
                column.preferredWidth = max(
                    1 + res.getString("KeyStoreTableModel.KeySizeColumn").length,
                    l + 1
                ) * iFontSize
                column.maxWidth = max(
                    2 + res.getString("KeyStoreTableModel.KeySizeColumn").length,
                    l + 1
                ) * iFontSize
            }
            if (i == keyStoreTableColumns.colCurve()) {
                column.minWidth = 8 * iFontSize
                column.preferredWidth = 1 + max(
                    res.getString("KeyStoreTableModel.CurveColumn").length,
                    l
                ) * iFontSize
                column.maxWidth = (2
                        + max("brainpool999r1".length, res.getString("KeyStoreTableModel.CurveColumn").length)
                        * iFontSize)
            }
            if (i == keyStoreTableColumns.colCertificateExpiry()) {
                l = "20.00.2000 00:00:00 MESZ".length
                column.minWidth = "20.00.2000".length * iFontSize
                column.preferredWidth = 1 + max(
                    res.getString("KeyStoreTableModel.CertExpiryColumn").length,
                    l
                ) * iFontSize
                column.maxWidth = 2 + max(
                    res.getString("KeyStoreTableModel.CertExpiryColumn").length,
                    l
                ) * iFontSize
            }
            if (i == keyStoreTableColumns.colLastModified()) {
                l = "20.09.2000 00:00:00 MESZ".length
                column.minWidth = "20.00.2000".length * iFontSize
                column.preferredWidth = 1 + max(
                    res.getString("KeyStoreTableModel.LastModifiedColumn").length,
                    l
                ) * iFontSize
                column.maxWidth = 2 + max(
                    res.getString("KeyStoreTableModel.LastModifiedColumn").length,
                    l
                ) * iFontSize
            }
            if (i == keyStoreTableColumns.colAKI()) {
                l = 41
                column.minWidth = 8 * iFontSize
                column.preferredWidth =
                    1 + max(res.getString("KeyStoreTableModel.AKIColumn").length, l) * iFontSize
                column.maxWidth = max(
                    2 + res.getString("KeyStoreTableModel.AKIColumn").length,
                    l + 1
                ) * iFontSize
            }
            if (i == keyStoreTableColumns.colSKI()) {
                l = 41
                column.minWidth = 8 * iFontSize
                column.preferredWidth = max(
                    2 + res.getString("KeyStoreTableModel.SKIColumn").length,
                    l + 1
                ) * iFontSize
                column.maxWidth = max(
                    2 + res.getString("KeyStoreTableModel.SKIColumn").length,
                    l + 1
                ) * iFontSize
            }
            if (i == keyStoreTableColumns.colIssuerCN()) {
                column.minWidth = 8 * iFontSize
                column.preferredWidth = max(
                    2 + res.getString("KeyStoreTableModel.IssuerCNColumn").length,
                    l + 1
                ) * iFontSize
                column.maxWidth = 100 * iFontSize
            }
            if (i == keyStoreTableColumns.colIssuerDN()) {
                column.minWidth = 8 * iFontSize
                column.preferredWidth = max(
                    2 + res.getString("KeyStoreTableModel.IssuerDNColumn").length,
                    l + 1
                ) * iFontSize
                column.maxWidth = 100 * iFontSize
            }
            if (i == keyStoreTableColumns.colIssuerO()) {
                column.minWidth = 8 * iFontSize
                column.preferredWidth = max(
                    2 + res.getString("KeyStoreTableModel.IssuerOColumn").length,
                    l + 1
                ) * iFontSize
                column.maxWidth = 100 * iFontSize
            }
            column.headerRenderer = KeyStoreTableHeadRend(jtKeyStore.tableHeader.defaultRenderer)
            column.cellRenderer = KeyStoreTableCellRend()
        }
    }
}