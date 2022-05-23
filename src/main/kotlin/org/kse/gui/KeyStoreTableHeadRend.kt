package org.kse.gui

import co.anbora.labs.kse.lang.settings.Settings.ENTRY_TYPE
import co.anbora.labs.kse.lang.settings.Settings.EXPIRY_STATUS
import co.anbora.labs.kse.lang.settings.Settings.LOCK_STATUS
import java.awt.Component
import java.util.*
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JTable
import javax.swing.border.EmptyBorder
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
        header.border = EmptyBorder(0,5,0,0)

        val columnOption = keyStoreTableColumns.getColumns()[column]
        columnOption?.let {
            header.toolTipText = res.getString(it.tooltip)
        }
    }
}