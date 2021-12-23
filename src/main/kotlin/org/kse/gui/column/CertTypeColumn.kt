package org.kse.gui.column

import co.anbora.labs.kse.lang.settings.Settings
import org.kse.crypto.keystore.KeyStoreUtil
import org.kse.gui.Certificate.getEntryType
import org.kse.gui.ColumnValues
import org.kse.utilities.history.KeyStoreHistory

class CertTypeColumn(
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
        data[Pair(rowIndex, Settings.ENTRY_TYPE)] = getEntryType(history, alias)
    }
}