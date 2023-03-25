package co.anbora.labs.kse.ide.editor

import co.anbora.labs.kse.ide.gui.view.DViewCertificate
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.kse.crypto.filetype.CryptoFileType
import org.kse.crypto.signing.JarParser
import java.security.cert.X509Certificate
import java.util.function.Predicate

private const val JAR_CERT_EDITOR_TYPE_ID = "co.anbora.labs.kse.jar.cert.editor"
class JarCertEditorProvider: EditorProvider() {

    private val jarFileTypes: Set<CryptoFileType> = setOf(
        CryptoFileType.JAR
    )

    override fun fileTypes(): Set<CryptoFileType> = jarFileTypes

    override fun acceptFile(): Predicate<VirtualFile> = Predicate { jarCertificates(it).isNotEmpty() }

    override fun getEditorTypeId(): String = JAR_CERT_EDITOR_TYPE_ID

    override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
        return object : AsyncFileEditorProvider.Builder() {
            override fun build(): FileEditor {
                val certificates = jarCertificates(file)
                return DViewCertificate(project, file, certificates, DViewCertificate.IMPORT_EXPORT)
            }
        }
    }

    private fun jarCertificates(file: VirtualFile): Array<X509Certificate> {
        val jarParser = JarParser(file.toNioPath().toFile())
        return jarParser.signerCerificates
    }
}