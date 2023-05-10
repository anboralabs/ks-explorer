package co.anbora.labs.kse.ide.editor

import co.anbora.labs.kse.fileTypes.core.CertUtils
import co.anbora.labs.kse.ide.gui.view.DViewCsr
import co.anbora.labs.kse.ide.gui.view.DViewError
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.apache.commons.io.FileUtils
import org.kse.crypto.csr.spkac.Spkac
import org.kse.crypto.filetype.CryptoFileType
import java.io.File

private const val CSR_EDITOR_TYPE_ID = "co.anbora.labs.kse.csr.spkac.editor"
class CsrSpkacEditorProvider: EditorProvider() {
    override fun fileTypes(): Set<CryptoFileType> = setOf(
        CryptoFileType.SPKAC_CSR
    )

    override fun getEditorTypeId(): String = CSR_EDITOR_TYPE_ID

    override fun createLicensedEditorAsync(project: Project, file: VirtualFile): FileEditor  {
        val csr = openCsr(file.toNioPath().toFile())
        return if (csr != null) {
            DViewCsr(project, file, csr)
        } else {
            DViewError(project, file, "Invalid CSR Cert")
        }
    }

    private fun openCsr(file: File?): Spkac? = try {
        val data: ByteArray = CertUtils.decodeIfBase64(FileUtils.readFileToByteArray(file))
        Spkac(data)
    } catch (ex: Exception) {
        null
    }

}
