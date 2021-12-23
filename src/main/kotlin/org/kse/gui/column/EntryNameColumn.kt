package org.kse.gui.column

import org.kse.utilities.history.KeyStoreHistory

class EntryNameColumn(
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
        data[Pair(rowIndex, colIndex)] = alias
    }
}