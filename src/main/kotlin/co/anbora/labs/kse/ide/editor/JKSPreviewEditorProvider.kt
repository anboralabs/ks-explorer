package co.anbora.labs.kse.ide.editor

import co.anbora.labs.kse.lang.JKSLanguage.EDITOR_TYPE_ID
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class JKSPreviewEditorProvider: FileEditorProvider {
    override fun accept(project: Project, file: VirtualFile): Boolean {
        TODO("Not yet implemented")
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        TODO("Not yet implemented")
    }

    override fun getEditorTypeId(): String = EDITOR_TYPE_ID

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR
}