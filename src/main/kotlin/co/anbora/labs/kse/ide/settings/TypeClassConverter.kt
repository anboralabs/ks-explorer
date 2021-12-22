package co.anbora.labs.kse.ide.settings

import com.intellij.util.xmlb.Converter

class TypeClassConverter: Converter<TypeClass>() {
    override fun toString(value: TypeClass): String = value.name

    override fun fromString(value: String): TypeClass? = TypeClass.values().firstOrNull { it.name == value }
}