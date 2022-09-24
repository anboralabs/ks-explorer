package org.kse.gui.column

import co.anbora.labs.kse.fileTypes.settings.Settings
import org.kse.gui.Certificate
import org.kse.utilities.history.KeyStoreHistory
import java.util.*

class CertStatusColumn(
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
        val expiry: Date? = Certificate.getCertificateExpiry(alias, keyStore)
        val c = Calendar.getInstance()
        val a = Calendar.getInstance()
        c.time = Date() // Now use today date.
        a.time = Date() // Now use today date.
        a.add(Calendar.DATE, Settings.EXPIRY_WAR_N_DAYS) // Adding warning interval

        if (expiry == null) {
            data[Pair(rowIndex, Settings.EXPIRY_STATUS)] = null // No expiry - must be a key entry
        } else {
            if (expiry.before(c.time)) {
                data[Pair(rowIndex, Settings.EXPIRY_STATUS)] = 2 // Expired
            } else {
                if (expiry.before(a.time)) {
                    data[Pair(rowIndex, Settings.EXPIRY_STATUS)] = 1 // Almost expired
                } else {
                    data[Pair(rowIndex, Settings.EXPIRY_STATUS)] = 0 // Not expired
                }
            }
        }
    }
}