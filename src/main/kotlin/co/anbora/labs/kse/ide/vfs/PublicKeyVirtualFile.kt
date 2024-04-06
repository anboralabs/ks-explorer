package co.anbora.labs.kse.ide.vfs

import co.anbora.labs.kse.ide.gui.view.DViewPublicKey
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.kse.crypto.publickey.OpenSslPubUtil
import java.security.PublicKey

class PublicKeyVirtualFile(
    private val alias: String,
    private val parent: VirtualFile,
    private val publicKey: PublicKey
): KSVirtualFile(alias, parent, OpenSslPubUtil.getPem(publicKey).toByteArray()) {
    override fun getFileEditor(project: Project): FileEditor {
        return DViewPublicKey(project, this, publicKey)
    }
}