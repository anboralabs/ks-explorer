package org.kse.gui.column

import co.anbora.labs.kse.lang.settings.Settings
import org.kse.crypto.keystore.KeyStoreType
import org.kse.gui.ColumnValues
import org.kse.utilities.history.KeyStoreHistory

class LockStatusColumn(
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
        val type = KeyStoreType.resolveJce(keyStore.type)

        val entryType = data.getOrDefault(Pair(rowIndex, Settings.ENTRY_TYPE), "")

        // Lock column - only applies to KeyStores types that actually support passwords for entries
        if ((entryType == ColumnValues.KEY_PAIR_ENTRY || entryType == ColumnValues.KEY_ENTRY) && type.hasEntryPasswords()) {
            data[Pair(rowIndex, Settings.LOCK_STATUS)] = currentState.getEntryPassword(alias) == null
        } else {
            data[Pair(rowIndex, Settings.LOCK_STATUS)] = null // Lock status does not apply
        }
    }
}