package org.kse.gui.column

import org.kse.crypto.KeyInfo
import org.kse.gui.Certificate.getAlgorithmName
import org.kse.gui.Certificate.getKeyInfo
import org.kse.utilities.history.KeyStoreHistory

class AlgorithmColumn(
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

        val keyInfo: KeyInfo? = getKeyInfo(alias, keyStore, currentState)

        if (keyInfo != null) {
            data[Pair(rowIndex, colIndex)] = getAlgorithmName(keyInfo)
        }
    }
}