package org.kse.gui

import javax.swing.table.AbstractTableModel

class KeyStoreTableModel(
    private val keyStoreTableColumns: KeyStoreTableColumns
): AbstractTableModel() {

    private val nofColumns = keyStoreTableColumns.getNofColumns()
    private val columns = keyStoreTableColumns.getColumns()

    private var data = Array(0) {
        arrayOf<Any>(
            0
        )
    }

    override fun getColumnName(columnIndex: Int): String = columns[columnIndex].title

    override fun getColumnClass(columnIndex: Int): Class<*> = columns[columnIndex].type

    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = false

    override fun getRowCount(): Int = data.size

    override fun getColumnCount(): Int = nofColumns

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any = data[rowIndex][columnIndex]
}