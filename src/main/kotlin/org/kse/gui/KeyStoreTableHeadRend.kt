package org.kse.gui

import java.awt.Component
import java.util.*
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JTable
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.TableCellRenderer

private const val ENTRY_TYPE = 0
private const val LOCK_STATUS = 1
private const val EXPIRY_STATUS = 2

class KeyStoreTableHeadRend(
    private val delegate: TableCellRenderer
): DefaultTableCellRenderer() {

    private val res = ResourceBundle.getBundle("org/kse/gui/resources")

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
            is JLabel -> renderHead(component, column)
            else -> Unit
        }

        return component
    }

    private fun renderHead(header: JLabel, column: Int) {
        when (column) {
            ENTRY_TYPE -> renderStatusColumn(header, "images/table/type_heading.png", "KeyStoreTableHeadRend.TypeColumn.tooltip")
            LOCK_STATUS -> renderStatusColumn(header, "images/table/lock_status_heading.png", "KeyStoreTableHeadRend.LockStatusColumn.tooltip")
            EXPIRY_STATUS -> renderStatusColumn(header, "images/table/cert_expiry_status_heading.png", "KeyStoreTableHeadRend.CertExpiryStatusColumn.tooltip")
            else -> Unit
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
}