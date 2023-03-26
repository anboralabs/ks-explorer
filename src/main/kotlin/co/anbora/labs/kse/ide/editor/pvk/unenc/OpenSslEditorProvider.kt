package co.anbora.labs.kse.ide.editor.pvk.unenc

import co.anbora.labs.kse.fileTypes.core.CertUtils
import co.anbora.labs.kse.ide.editor.EditorProvider
import co.anbora.labs.kse.ide.gui.view.DViewPrivateKey
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.apache.commons.io.FileUtils
import org.kse.crypto.filetype.CryptoFileType
import org.kse.crypto.privatekey.OpenSslPvkUtil
import java.io.File
import java.security.PrivateKey
import java.util.function.Predicate

private const val PRIVATE_EDITOR_TYPE_ID = "co.anbora.labs.kse.private.unenc.ssl.editor"
class OpenSslEditorProvider: EditorProvider() {
    override fun fileTypes(): Set<CryptoFileType> = setOf(
        CryptoFileType.UNENC_OPENSSL_PVK
    )

    override fun getEditorTypeId(): String = PRIVATE_EDITOR_TYPE_ID

    override fun acceptFile(): Predicate<VirtualFile> = Predicate {
        openPrivateKey(it.toNioPath().toFile()) != null
    }

    override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
        return object : AsyncFileEditorProvider.Builder() {
            override fun build(): FileEditor {
                val privateKey = openPrivateKey(file.toNioPath().toFile())
                return DViewPrivateKey(project, file, privateKey)
            }
        }
    }

    private fun openPrivateKey(file: File?): PrivateKey? = try {
        if (file == null) {
            null
        }
        val data: ByteArray = CertUtils.decodeIfBase64(FileUtils.readFileToByteArray(file))
        OpenSslPvkUtil.load(data)
    } catch (ex: Exception) {
        null
    }
}