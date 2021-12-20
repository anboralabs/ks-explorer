package co.anbora.labs.kse.ide.settings

import co.anbora.labs.kse.lang.settings.Settings.INIT_COLUMN

data class Option(
    var active: Boolean = false,
    var index: Int = INIT_COLUMN
)