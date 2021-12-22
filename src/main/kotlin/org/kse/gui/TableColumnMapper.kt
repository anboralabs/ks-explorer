package org.kse.gui

import co.anbora.labs.kse.ide.settings.Option
import java.util.*

object TableColumnMapper {

    private val res = ResourceBundle.getBundle("org/kse/gui/resources")

    fun mapOptionToColumn(option: Option): TableColumn {
        val title = res.getString(option.keyTitle)
        return TableColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = when (option.colWidth < 1) {
                true -> title.length
                else -> option.colWidth
            }
        )
    }
}