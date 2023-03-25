package co.anbora.labs.kse.fileTypes

import co.anbora.labs.kse.ide.icons.KSIcons
import com.intellij.openapi.fileTypes.UserBinaryFileType
import javax.swing.Icon

object KeystoreFileType: UserBinaryFileType() {

    private const val FILETYPE_NAME = "KS"

    const val EDITOR_NAME = "keystore"

    const val EDITOR_SETTINGS_ID = "co.anbora.labs.kse.editor.settings"

    override fun getName(): String = FILETYPE_NAME

    override fun getDescription(): String = "Java Keystore explorer"

    override fun getDisplayName(): String = "KS explorer"

    override fun getIcon(): Icon = KSIcons.FILE

}
