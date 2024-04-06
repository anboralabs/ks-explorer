package co.anbora.labs.kse.ide.vfs

import co.anbora.labs.kse.ide.gui.view.DViewPrivateKey
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.kse.gui.crypto.privatekey.PrivateKeyUtils
import java.security.PrivateKey

class PrivateKeyVirtualFile(
    private val alias: String,
    private val parent: VirtualFile,
    private val privateKey: PrivateKey
): KSVirtualFile(
    alias,
    parent,
    PrivateKeyUtils.getOpenSslEncodedPrivateKey(privateKey, true, null, null)
) {
    override fun getFileEditor(project: Project): FileEditor {
        return DViewPrivateKey(project, this, privateKey)
    }
}
