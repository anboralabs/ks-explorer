package co.anbora.labs.kse.lang

import co.anbora.labs.kse.ide.icons.JKSIcons
import co.anbora.labs.kse.lang.JKSLanguage.LANGUAGE_NAME
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object JKSFileType: LanguageFileType(JKSLanguage) {

    private const val EXTENSION = "jks"

    override fun getName(): String = LANGUAGE_NAME

    override fun getDescription(): String = "Keystore explorer"

    override fun getDefaultExtension(): String = EXTENSION

    override fun getIcon(): Icon = JKSIcons.FILE

}
