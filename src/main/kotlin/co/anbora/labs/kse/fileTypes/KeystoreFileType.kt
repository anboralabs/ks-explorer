package co.anbora.labs.kse.fileTypes

import co.anbora.labs.kse.ide.icons.KSIcons
import co.anbora.labs.kse.fileTypes.KSLanguage.LANGUAGE_NAME
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object KeystoreFileType: LanguageFileType(KSLanguage) {

    private const val EXTENSION = "jks"

    override fun getName(): String = LANGUAGE_NAME

    override fun getDescription(): String = "Java Keystore explorer"

    override fun getDefaultExtension(): String = EXTENSION

    override fun getIcon(): Icon = KSIcons.FILE

}
