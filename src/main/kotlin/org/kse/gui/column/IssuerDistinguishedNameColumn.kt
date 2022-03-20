package org.kse.gui.column

import org.kse.gui.Certificate
import org.kse.gui.Certificate.getCertificateIssuerDN
import org.kse.gui.ColumnValues.KEY_ENTRY
import org.kse.utilities.history.KeyStoreHistory

class IssuerDistinguishedNameColumn(
    tooltip: String,
    title: String,
    type: Class<out Any>,
    index: Int,
    width: Int
): TableColumn(tooltip, title, type, index, width) {
    override fun accept(
        rowIndex: Int,
        colIndex: Int,
        alias: String,
        history: KeyStoreHistory,
        data: MutableMap<Pair<Int, Int>, Any?>
    ) {
        val currentState = history.currentState
        val keyStore = currentState.keyStore

        val entryType: String = Certificate.getEntryType(history, alias)

        if (entryType != KEY_ENTRY) {
            data[Pair(rowIndex, colIndex)] = getCertificateIssuerDN(alias, keyStore)
        } else {
            data[Pair(rowIndex, colIndex)] = null
        }
    }
}