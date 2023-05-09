package co.anbora.labs.kse.ide.editor

import co.anbora.labs.kse.ide.gui.view.DViewCertificate
import co.anbora.labs.kse.ide.gui.view.DViewError
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.kse.crypto.filetype.CryptoFileType
import org.kse.crypto.x509.X509CertUtil.isBase64Certificate
import org.kse.crypto.x509.X509CertUtil.isPemCertificate
import org.kse.gui.actions.KeyStoreExploreActionUtils.openCertificate

private const val CERT_EDITOR_TYPE_ID = "co.anbora.labs.kse.cert.editor"
open class CertEditorProvider: EditorProvider() {

    override fun fileTypes(): Set<CryptoFileType> = setOf(
        CryptoFileType.CERT
    )

    override fun getEditorTypeId(): String = CERT_EDITOR_TYPE_ID

    override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
        return object : AsyncFileEditorProvider.Builder() {
            override fun build(): FileEditor {
                val certificates = openCertificate(file.toNioPath().toFile())
                return if (certificates.isNotEmpty()) {
                    DViewCertificate(project, file, certificates, DViewCertificate.IMPORT_EXPORT) { view, cert ->
                        view.restartView(openCertificate(cert.toByteArray(), file.name))
                    }
                } else {
                    DViewError(project, file, "Invalid Cert")
                }
            }
        }
    }

    override fun accept(project: Project, file: VirtualFile): Boolean {
        return super.accept(project, file)
                && !(isPemCertificate(file.contentsToByteArray())
                    || isBase64Certificate(file.contentsToByteArray()))
    }
}
