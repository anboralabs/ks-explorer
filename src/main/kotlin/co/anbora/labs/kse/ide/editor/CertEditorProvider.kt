package co.anbora.labs.kse.ide.editor

import co.anbora.labs.kse.ide.gui.view.DViewCertificate
import co.anbora.labs.kse.ide.gui.view.DViewError
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.jdom.Element
import org.kse.crypto.filetype.CryptoFileType
import org.kse.gui.actions.KeyStoreExploreActionUtils.openCertificate
import java.util.function.BiConsumer

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
                    DViewCertificate(project, file, certificates, DViewCertificate.IMPORT_EXPORT) { view, cert ->
                        view.restartView(openCertificate(cert.toByteArray(), file.name))
                    }
                } else {
                    DViewError(project, file, "Invalid Cert")
                }
            }
        }
    }

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.PLACE_BEFORE_DEFAULT_EDITOR
}
