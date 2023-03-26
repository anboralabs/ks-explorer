package co.anbora.labs.kse.ide.editor

import co.anbora.labs.kse.fileTypes.core.CertUtils.decodeIfBase64
import co.anbora.labs.kse.ide.gui.view.DViewCsr
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.apache.commons.io.FileUtils
import org.bouncycastle.pkcs.PKCS10CertificationRequest
import org.kse.crypto.csr.pkcs10.Pkcs10Util
import org.kse.crypto.filetype.CryptoFileType
import java.io.File
import java.util.function.Predicate

private const val CSR_EDITOR_TYPE_ID = "co.anbora.labs.kse.csr.pkcs10.editor"
class CsrPkcs10EditorProvider: EditorProvider() {
    override fun fileTypes(): Set<CryptoFileType> = setOf(
        CryptoFileType.PKCS10_CSR
    )

    override fun getEditorTypeId(): String = CSR_EDITOR_TYPE_ID

    override fun acceptFile(): Predicate<VirtualFile> = Predicate {
        openCsr(it.toNioPath().toFile()) != null
    }

    override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
        return object : AsyncFileEditorProvider.Builder() {
            override fun build(): FileEditor {
                val csr = openCsr(file.toNioPath().toFile())
                return DViewCsr(project, file, csr)
            }
        }
    }

    private fun openCsr(file: File?): PKCS10CertificationRequest? = try {
        if (file == null) {
            null
        }
        val data: ByteArray = decodeIfBase64(FileUtils.readFileToByteArray(file))
        Pkcs10Util.loadCsr(data)
    } catch (ex: Exception) {
        null
    }

}