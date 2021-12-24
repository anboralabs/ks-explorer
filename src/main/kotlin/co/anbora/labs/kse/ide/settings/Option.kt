package co.anbora.labs.kse.ide.settings

import co.anbora.labs.kse.lang.settings.Settings.INIT_COLUMN
import com.intellij.util.xmlb.annotations.OptionTag

data class Option(
    val keyToolTip: String = "",
    val keyTitle: String = "",
    @OptionTag(
        converter = TypeClassConverter::class
    ) val typeClass: TypeClass? = null,
    @OptionTag(
        converter = OptionTypeConverter::class
    ) val optionType: OptionType? = null,
    val colWidth: Int = INIT_COLUMN,
    var active: Boolean = false,
    var index: Int = INIT_COLUMN
)