package org.kse.gui

import org.kse.crypto.CryptoException
import org.kse.crypto.keystore.KeyStoreType
import org.kse.crypto.keystore.KeyStoreUtil
import org.kse.gui.column.InvalidColumn
import org.kse.utilities.history.KeyStoreHistory
import java.security.GeneralSecurityException
import java.util.*
import javax.swing.table.AbstractTableModel

class KeyStoreTableModel(
    private val keyStoreTableColumns: KeyStoreTableColumns
): AbstractTableModel() {

    private var nofRows = 0
    private val nofColumns = keyStoreTableColumns.getNofColumns()
    private val columns = keyStoreTableColumns.getColumns()

    private lateinit var history: KeyStoreHistory
    private val data: MutableMap<Pair<Int, Int>, Any?> = mutableMapOf()

    @Throws(GeneralSecurityException::class, CryptoException::class)
    fun load(history: KeyStoreHistory) {
        this.history = history
        val currentState = history.currentState

        val keyStore = currentState.keyStore
        val type = KeyStoreType.resolveJce(keyStore.type)

        val aliases = keyStore.aliases()

        val sortedAliases: TreeMap<String, String> = TreeMap<String, String>(AliasComparator())

        while (aliases.hasMoreElements()) {
            val alias = aliases.nextElement()
            if (!KeyStoreUtil.isSupportedEntryType(alias, keyStore)) {
                continue
            }
            sortedAliases[alias] = alias
        }

        nofRows = sortedAliases.size

        sortedAliases.entries.withIndex().forEach {
            val alias: String = it.value.key
            columns.forEach { (column, tableColumn) ->
                tableColumn.accept(
                    it.index,
                    column,
                    alias,
                    history,
                    data
                )
            }
        }

        fireTableDataChanged()
    }

    override fun getColumnName(columnIndex: Int): String = columns.getOrDefault(columnIndex, InvalidColumn()).title

    override fun getColumnClass(columnIndex: Int): Class<*> = columns.getOrDefault(columnIndex, InvalidColumn()).type

    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = false

    override fun getRowCount(): Int = nofRows

    override fun getColumnCount(): Int = nofColumns

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? = data[Pair(rowIndex, columnIndex)]

    private class AliasComparator : Comparator<String> {
        override fun compare(name1: String, name2: String): Int {
            return name1.compareTo(name2, ignoreCase = true)
        }
    }
}
