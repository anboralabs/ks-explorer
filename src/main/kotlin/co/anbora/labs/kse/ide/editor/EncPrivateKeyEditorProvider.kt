package co.anbora.labs.kse.ide.editor

import co.anbora.labs.kse.ide.gui.swing.PrivateKeyWithPasswordView
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.kse.crypto.filetype.CryptoFileType

private const val PRIVATE_EDITOR_TYPE_ID = "co.anbora.labs.kse.private.enc.editor"
class EncPrivateKeyEditorProvider: EditorProvider() {
    override fun fileTypes(): Set<CryptoFileType> = setOf(
        CryptoFileType.ENC_PKCS8_PVK,
        CryptoFileType.ENC_OPENSSL_PVK,
        CryptoFileType.ENC_MS_PVK,
    )

    override fun getEditorTypeId(): String = PRIVATE_EDITOR_TYPE_ID

    override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
        return object : AsyncFileEditorProvider.Builder() {
            override fun build(): FileEditor =
                PrivateKeyWithPasswordView(project, file)
        }
    }
}