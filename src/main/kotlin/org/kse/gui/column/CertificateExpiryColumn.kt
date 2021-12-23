package org.kse.gui.column

import org.kse.gui.Certificate.getCertificateExpiry
import org.kse.utilities.history.KeyStoreHistory
import java.util.*

class CertificateExpiryColumn(
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
        // Expiry status column
        val expiry: Date? = getCertificateExpiry(alias, keyStore)
        // Expiry date column
        if (expiry != null) {
            data[Pair(rowIndex, colIndex)] = expiry
        } else {
            data[Pair(rowIndex, colIndex)] = null // No expiry date - must be a key entry
        }
    }
}