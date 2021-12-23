package org.kse.gui.column

import org.kse.crypto.KeyInfo
import org.kse.gui.Certificate
import org.kse.utilities.history.KeyStoreHistory

class KeySizeColumn(
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

        val keyInfo: KeyInfo? = Certificate.getKeyInfo(alias, keyStore, currentState)

        if (keyInfo != null) {
            data[Pair(rowIndex, colIndex)] = keyInfo.size
        }
    }
}