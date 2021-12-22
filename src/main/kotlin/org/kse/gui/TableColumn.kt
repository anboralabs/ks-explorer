package org.kse.gui

data class TableColumn(
    val tooltip: String,
    val title: String,
    val type: Class<out Any>,
    val index: Int,
    val width: Int
)
