package co.anbora.labs.kse.ide.settings

import com.intellij.util.xmlb.Converter

class OptionTypeConverter: Converter<OptionType>() {
    override fun toString(value: OptionType): String = value.name

    override fun fromString(value: String): OptionType? = OptionType.values().firstOrNull { it.name == value }
}