package org.kse.gui.column

import org.kse.utilities.history.KeyStoreHistory

sealed class TableColumn(
    val tooltip: String,
    val title: String,
    val type: Class<out Any>,
    val index: Int,
    val width: Int
) {
    abstract fun accept(
        rowIndex: Int,
        colIndex: Int,
        alias: String,
        history: KeyStoreHistory,
        data: MutableMap<Pair<Int, Int>, Any?>
    )
}
