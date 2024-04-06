package co.anbora.labs.kse.fileTypes

import co.anbora.labs.kse.ide.icons.KSIcons
import com.intellij.openapi.fileTypes.UserBinaryFileType
import javax.swing.Icon

object VirtualFileType: UserBinaryFileType() {

    override fun getName(): String = "VirtualCert"

    override fun getDescription(): String = "Java Cert explorer for certs inside keystores"

    override fun getDisplayName(): String = "KS virtual cert explorer"

    override fun getIcon(): Icon = KSIcons.CERTIFICATE
}