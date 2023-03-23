package co.anbora.labs.kse.ide.editor

import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class PrivateKeyEditorProvider: AsyncFileEditorProvider, DumbAware {
    override fun accept(project: Project, file: VirtualFile): Boolean {
        TODO("Not yet implemented")
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        TODO("Not yet implemented")
    }

    override fun getEditorTypeId(): String {
        TODO("Not yet implemented")
    }

    override fun getPolicy(): FileEditorPolicy {
        TODO("Not yet implemented")
    }

    override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
        TODO("Not yet implemented")
    }
}