package co.anbora.labs.kse.ide.editor

import co.anbora.labs.kse.fileTypes.core.CertUtils.decodeIfBase64
import co.anbora.labs.kse.ide.gui.view.DViewError
import co.anbora.labs.kse.ide.gui.view.DViewPublicKey
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.apache.commons.io.FileUtils
import org.kse.crypto.filetype.CryptoFileType
import org.kse.crypto.publickey.OpenSslPubUtil
import java.io.File
import java.security.PublicKey

private const val PRIVATE_EDITOR_TYPE_ID = "co.anbora.labs.kse.public.editor"
class PublicKeyEditorProvider: EditorProvider() {
    override fun fileTypes(): Set<CryptoFileType> = setOf(
        CryptoFileType.OPENSSL_PUB
    )

    override fun getEditorTypeId(): String = PRIVATE_EDITOR_TYPE_ID

    override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
        return object : AsyncFileEditorProvider.Builder() {
            override fun build(): FileEditor {
                val public = openPublicKey(file.toNioPath().toFile())
                return if (public != null) {
                    DViewPublicKey(project, file, public)
                } else {
                    DViewError(project, file, "Invalid Public Key Cert")
                }
            }
        }
    }

    private fun openPublicKey(file: File): PublicKey? = try {
        val data: ByteArray = decodeIfBase64(FileUtils.readFileToByteArray(file))
        OpenSslPubUtil.load(data)
    } catch (ex: Exception) {
        null
    }

}