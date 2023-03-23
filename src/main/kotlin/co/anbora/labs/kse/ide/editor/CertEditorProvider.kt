package co.anbora.labs.kse.ide.editor

import co.anbora.labs.kse.fileTypes.CertFileType.CERT_EDITOR_TYPE_ID
import co.anbora.labs.kse.fileTypes.core.CertFile.acceptCertFile
import co.anbora.labs.kse.ide.gui.view.DViewCertificate
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.kse.gui.actions.KeyStoreExploreActionUtils.openCertificate

class CertEditorProvider: AsyncFileEditorProvider, DumbAware {
    override fun accept(project: Project, file: VirtualFile): Boolean = acceptCertFile(file) {
        openCertificate(file.toNioPath().toFile())
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor =
        createEditorAsync(project, file).build()

    override fun getEditorTypeId(): String = CERT_EDITOR_TYPE_ID

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR

    override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
        return object : AsyncFileEditorProvider.Builder() {
            override fun build(): FileEditor {
                val certificates = openCertificate(file.toNioPath().toFile())
                return DViewCertificate(project, file, certificates, DViewCertificate.IMPORT_EXPORT)
            }
        }
    }
}