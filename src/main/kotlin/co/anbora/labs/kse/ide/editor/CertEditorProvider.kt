package co.anbora.labs.kse.ide.editor

import co.anbora.labs.kse.ide.gui.view.DViewCertificate
import co.anbora.labs.kse.ide.gui.view.DViewError
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.kse.crypto.filetype.CryptoFileType
import org.kse.gui.actions.KeyStoreExploreActionUtils.openCertificate
import java.util.function.Predicate

private const val CERT_EDITOR_TYPE_ID = "co.anbora.labs.kse.cert.editor"
class CertEditorProvider: EditorProvider() {

    override fun fileTypes(): Set<CryptoFileType> = setOf(
        CryptoFileType.CERT
    )

    override fun getEditorTypeId(): String = CERT_EDITOR_TYPE_ID

    override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
        return object : AsyncFileEditorProvider.Builder() {
            override fun build(): FileEditor {
                val certificates = openCertificate(file.toNioPath().toFile())
                return if (certificates.isNotEmpty()) {
                    DViewCertificate(project, file, certificates, DViewCertificate.IMPORT_EXPORT)
                } else {
                    DViewError(project, file, "Invalid Cert")
                }
            }
        }
    }
}