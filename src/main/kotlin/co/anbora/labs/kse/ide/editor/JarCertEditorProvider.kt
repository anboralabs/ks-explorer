package co.anbora.labs.kse.ide.editor

import co.anbora.labs.kse.fileTypes.CertFileType.JAR_CERT_EDITOR_TYPE_ID
import co.anbora.labs.kse.fileTypes.core.CertFile.acceptJarCertFile
import co.anbora.labs.kse.fileTypes.core.CertFile.jarCertificates
import co.anbora.labs.kse.ide.gui.view.DViewCertificate
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class JarCertEditorProvider: AsyncFileEditorProvider, DumbAware {
    override fun accept(project: Project, file: VirtualFile): Boolean = acceptJarCertFile(file)

    override fun createEditor(project: Project, file: VirtualFile): FileEditor =
        createEditorAsync(project, file).build()

    override fun getEditorTypeId(): String = JAR_CERT_EDITOR_TYPE_ID

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR

    override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
        return object : AsyncFileEditorProvider.Builder() {
            override fun build(): FileEditor {
                val certificates = jarCertificates(file)
                return DViewCertificate(project, certificates, DViewCertificate.IMPORT_EXPORT)
            }
        }
    }
}