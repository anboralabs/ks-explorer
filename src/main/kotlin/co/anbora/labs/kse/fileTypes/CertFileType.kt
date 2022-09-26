package co.anbora.labs.kse.fileTypes

import co.anbora.labs.kse.ide.icons.KSIcons
import com.intellij.openapi.fileTypes.UserBinaryFileType
import javax.swing.Icon

object CertFileType: UserBinaryFileType() {

    private const val FILETYPE_NAME = "Cert"

    override fun getName(): String = FILETYPE_NAME

    override fun getDescription(): String = "Java Cert explorer"

    override fun getDisplayName(): String = "KS cert explorer"

    override fun getIcon(): Icon = KSIcons.FILE

}