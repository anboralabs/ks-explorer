package co.anbora.labs.kse.fileTypes

import co.anbora.labs.kse.ide.icons.KSIcons
import com.intellij.openapi.fileTypes.LanguageFileType
import org.kse.KSE
import java.security.Security
import javax.swing.Icon

object PemFileType: LanguageFileType(PemLanguage) {

    init {
        Security.addProvider(KSE.BC)
    }

    private const val FILETYPE_NAME = "Pem"

    override fun getName(): String = FILETYPE_NAME

    override fun getDescription(): String = "Java Pem explorer"

    override fun getDefaultExtension(): String = "pem"

    override fun getIcon(): Icon = KSIcons.CERTIFICATE
}