package co.anbora.labs.kse.ide.editor

import co.anbora.labs.kse.ide.gui.view.DViewCertificate
import co.anbora.labs.kse.ide.gui.view.DViewCrl
import co.anbora.labs.kse.ide.gui.view.DViewError
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.apache.commons.io.FileUtils
import org.kse.crypto.filetype.CryptoFileType
import org.kse.crypto.x509.X509CertUtil
import org.kse.gui.actions.KeyStoreExploreActionUtils
import java.io.File
import java.security.cert.X509CRL
import java.util.function.Predicate

private const val CLR_EDITOR_TYPE_ID = "co.anbora.labs.kse.clr.editor"
class CrlEditorProvider: EditorProvider() {

    override fun fileTypes(): Set<CryptoFileType> = setOf(
        CryptoFileType.CRL
    )

    override fun getEditorTypeId(): String = CLR_EDITOR_TYPE_ID

    override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
        return object : AsyncFileEditorProvider.Builder() {
            override fun build(): FileEditor {
                val clr = openClr(file.toNioPath().toFile())
                return if (clr != null) {
                    DViewCrl(project, file, clr)
                } else {
                    DViewError(project, file, "Invalid CLR Cert")
                }
            }
        }
    }

    private fun openClr(file: File?): X509CRL? = try {
        val data = FileUtils.readFileToByteArray(file)
        X509CertUtil.loadCRL(data)
    } catch (ex: Exception) {
        null
    }

}