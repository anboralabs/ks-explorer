package co.anbora.labs.kse.ide.settings

import java.util.*

enum class TypeClass(val type: Class<out Any>) {

    BOOLEAN(Boolean::class.java),
    INTEGER(Int::class.java),
    STRING(String::class.java),
    DATE(Date::class.java);

}