package org.kse.gui.column

import org.kse.crypto.keystore.KeyStoreType
import org.kse.utilities.history.KeyStoreHistory

class LastModifiedColumn(
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
        // Modified date column - only applies to non-PKCS #11/#12 KeyStores
        if (keyStore.type != KeyStoreType.PKCS12.jce()
            && keyStore.type != KeyStoreType.PKCS11.jce()
        ) {
            data[Pair(rowIndex, colIndex)] = keyStore.getCreationDate(alias)
        } else {
            data[Pair(rowIndex, colIndex)] = null
        }
    }
}