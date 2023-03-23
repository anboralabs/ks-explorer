package co.anbora.labs.kse.fileTypes

import co.anbora.labs.kse.ide.icons.KSIcons
import com.intellij.openapi.fileTypes.UserBinaryFileType
import javax.swing.Icon

object CertFileType: UserBinaryFileType() {

    private const val FILETYPE_NAME = "Cert"

    const val EDITOR_NAME = "cert"

    const val JAR_CERT_EDITOR_TYPE_ID = "co.anbora.labs.kse.jar.cert.editor"
    const val CERT_EDITOR_TYPE_ID = "co.anbora.labs.kse.cert.editor"
    const val CLR_EDITOR_TYPE_ID = "co.anbora.labs.kse.clr.editor"
    const val CSR_EDITOR_TYPE_ID = "co.anbora.labs.kse.csr.editor"
    const val PRIVATE_KEY_EDITOR_TYPE_ID = "co.anbora.labs.kse.private.key.editor"
    const val PUBLIC_KEY_EDITOR_TYPE_ID = "co.anbora.labs.kse.public.key.editor"

    override fun getName(): String = FILETYPE_NAME

    override fun getDescription(): String = "Java Cert explorer"

    override fun getDisplayName(): String = "KS cert explorer"

    override fun getIcon(): Icon = KSIcons.FILE

}