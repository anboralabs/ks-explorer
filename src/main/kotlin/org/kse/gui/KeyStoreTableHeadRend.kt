package org.kse.gui

import co.anbora.labs.kse.lang.settings.Settings.ENTRY_TYPE
import co.anbora.labs.kse.lang.settings.Settings.EXPIRY_STATUS
import co.anbora.labs.kse.lang.settings.Settings.LOCK_STATUS
import java.awt.Component
import java.util.*
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JTable
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.TableCellRenderer

class KeyStoreTableHeadRend(
    private val delegate: TableCellRenderer
): DefaultTableCellRenderer() {

    private val res = ResourceBundle.getBundle("org/kse/gui/resources")
    private val keyStoreTableColumns = KeyStoreTableColumns()

    override fun getTableCellRendererComponent(
        table: JTable?,
        value: Any?,
        isSelected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ): Component {

        val component = delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)

        when(component) {
            is JLabel -> renderHead(component, value, column)
            else -> Unit
        }

        return component
    }

    private fun renderHead(header: JLabel, value: Any?, column: Int) {
        when (column) {
            ENTRY_TYPE -> renderStatusColumn(header, "images/table/type_heading.png", "KeyStoreTableHeadRend.TypeColumn.tooltip")
            LOCK_STATUS -> renderStatusColumn(header, "images/table/lock_status_heading.png", "KeyStoreTableHeadRend.LockStatusColumn.tooltip")
            EXPIRY_STATUS -> renderStatusColumn(header, "images/table/cert_expiry_status_heading.png", "KeyStoreTableHeadRend.CertExpiryStatusColumn.tooltip")
            else -> renderConfiguredColumns(header, value, column)
        }
    }

    private fun renderStatusColumn(header: JLabel, pathIcon: String, resTooltip: String) {
        header.text = ""
        val icon = ImageIcon(javaClass.getResource(pathIcon))
        header.icon = icon
        header.horizontalAlignment = CENTER
        header.verticalAlignment = CENTER

        header.toolTipText = res.getString(resTooltip)
    }

    private fun renderConfiguredColumns(header: JLabel, value: Any?, column: Int) {
        header.text = value as String?
        header.horizontalAlignment = LEFT
        header.icon = null

        when (column) {
            keyStoreTableColumns.colEntryName() -> {
                header.toolTipText = res.getString("KeyStoreTableHeadRend.NameColumn.tooltip")
            }
            keyStoreTableColumns.colAlgorithm() -> {
                header.toolTipText = res.getString("KeyStoreTableHeadRend.AlgorithmColumn.tooltip")
            }
            keyStoreTableColumns.colKeySize() -> {
                header.toolTipText = res.getString("KeyStoreTableHeadRend.KeySizeColumn.tooltip")
            }
            keyStoreTableColumns.colCertificateExpiry() -> {
                header.toolTipText = res.getString("KeyStoreTableHeadRend.CertExpiryColumn.tooltip")
            }
            keyStoreTableColumns.colLastModified() -> {
                header.toolTipText = res.getString("KeyStoreTableHeadRend.LastModifiedColumn.tooltip")
            }
            keyStoreTableColumns.colAKI() -> {
                header.toolTipText = res.getString("KeyStoreTableHeadRend.AKIColumn.tooltip")
            }
            keyStoreTableColumns.colSKI() -> {
                header.toolTipText = res.getString("KeyStoreTableHeadRend.SKIColumn.tooltip")
            }
            keyStoreTableColumns.colIssuerDN() -> {
                header.toolTipText = res.getString("KeyStoreTableHeadRend.IssuerDNColumn.tooltip")
            }
            keyStoreTableColumns.colSubjectDN() -> {
                header.toolTipText = res.getString("KeyStoreTableHeadRend.SubjectDNColumn.tooltip")
            }
            keyStoreTableColumns.colIssuerCN() -> {
                header.toolTipText = res.getString("KeyStoreTableHeadRend.IssuerCNColumn.tooltip")
            }
            keyStoreTableColumns.colSubjectCN() -> {
                header.toolTipText = res.getString("KeyStoreTableHeadRend.SubjectCNColumn.tooltip")
            }
            keyStoreTableColumns.colIssuerO() -> {
                header.toolTipText = res.getString("KeyStoreTableHeadRend.IssuerOColumn.tooltip")
            }
            keyStoreTableColumns.colSubjectO() -> {
                header.toolTipText = res.getString("KeyStoreTableHeadRend.SubjectOColumn.tooltip")
            }
            keyStoreTableColumns.colCurve() -> {
                header.toolTipText = res.getString("KeyStoreTableHeadRend.CurveColumn.tooltip")
            }
        }
    }
}