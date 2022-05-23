package org.kse.gui.column

import org.kse.utilities.history.KeyStoreHistory

class InvalidColumn: TableColumn("", "", String::class.java, -1, 0) {
    override fun accept(
        rowIndex: Int,
        colIndex: Int,
        alias: String,
        history: KeyStoreHistory,
        data: MutableMap<Pair<Int, Int>, Any?>
    ) = Unit
}
