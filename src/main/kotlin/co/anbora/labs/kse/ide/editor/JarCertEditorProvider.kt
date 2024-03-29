package co.anbora.labs.kse.ide.editor

import co.anbora.labs.kse.ide.gui.view.DViewCertificate
import co.anbora.labs.kse.ide.gui.view.DViewError
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.kse.crypto.filetype.CryptoFileType
import org.kse.crypto.signing.JarParser
import java.security.cert.X509Certificate

private const val JAR_CERT_EDITOR_TYPE_ID = "co.anbora.labs.kse.jar.cert.editor"
class JarCertEditorProvider: EditorProvider() {

    override fun fileTypes(): Set<CryptoFileType> = setOf(
        CryptoFileType.JAR
    )

    override fun getEditorTypeId(): String = JAR_CERT_EDITOR_TYPE_ID

    override fun createLicensedEditorAsync(project: Project, file: VirtualFile): FileEditor {
        val certificates = jarCertificates(file)
        return if (certificates.isNotEmpty()) {
            DViewCertificate(project, file, certificates, DViewCertificate.IMPORT_EXPORT) { _, _ ->
                //view.restartView(KeyStoreExploreActionUtils.openCertificate(cert.toByteArray(), file.name))
            }
        } else {
            DViewError(project, file, "Invalid JAR Cert")
        }
    }

    private fun jarCertificates(file: VirtualFile): Array<X509Certificate> {
        val jarParser = JarParser(file.toNioPath().toFile())
        return jarParser.signerCerificates
    }
}
