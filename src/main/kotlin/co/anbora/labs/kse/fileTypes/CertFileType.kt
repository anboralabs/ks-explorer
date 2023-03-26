package co.anbora.labs.kse.fileTypes

import co.anbora.labs.kse.ide.icons.KSIcons
import com.intellij.openapi.fileTypes.UserBinaryFileType
import org.kse.KSE
import java.security.Security
import javax.swing.Icon

object CertFileType: UserBinaryFileType() {

    init {
        Security.addProvider(KSE.BC)
    }

    private const val FILETYPE_NAME = "Cert"

    const val EDITOR_NAME = "cert"

    const val PRIVATE_KEY_EDITOR_TYPE_ID = "co.anbora.labs.kse.private.key.editor"
    const val PUBLIC_KEY_EDITOR_TYPE_ID = "co.anbora.labs.kse.public.key.editor"

    override fun getName(): String = FILETYPE_NAME

    override fun getDescription(): String = "Java Cert explorer"

    override fun getDisplayName(): String = "KS cert explorer"

    override fun getIcon(): Icon = KSIcons.CERTIFICATE

}