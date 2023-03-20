package co.anbora.labs.kse.ide.editor

import co.anbora.labs.kse.fileTypes.KeystoreFileType.EDITOR_TYPE_ID
import co.anbora.labs.kse.ide.gui.swing.KeyStoreFrame
import co.anbora.labs.kse.fileTypes.core.KeystoreFile.acceptKSFile
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class KSEditorProvider: AsyncFileEditorProvider, DumbAware {

    override fun accept(project: Project, file: VirtualFile): Boolean = acceptKSFile(file)

    override fun createEditor(project: Project, file: VirtualFile): FileEditor =
        createEditorAsync(project, file).build()

    override fun getEditorTypeId(): String = EDITOR_TYPE_ID

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR

    override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
        return object : AsyncFileEditorProvider.Builder() {
            override fun build(): FileEditor =
                KeyStoreFrame(project, file)
        }
    }
}