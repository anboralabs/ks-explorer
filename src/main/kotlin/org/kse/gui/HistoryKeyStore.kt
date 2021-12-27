package org.kse.gui

import org.kse.utilities.history.KeyStoreHistory

interface HistoryKeyStore {

    fun getSelectedEntryAliases(): Array<String>

    fun getActiveKeyStoreHistory(): KeyStoreHistory?

}