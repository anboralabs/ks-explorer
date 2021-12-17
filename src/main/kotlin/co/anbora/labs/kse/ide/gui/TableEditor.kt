package co.anbora.labs.kse.ide.gui

import co.anbora.labs.kse.ide.editor.KSViewerState
import co.anbora.labs.kse.lang.KSLanguage.EDITOR_NAME
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.UserDataHolder
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.annotations.NotNull
import java.beans.PropertyChangeListener

abstract class TableEditor(
    private val projectArg: Project,
    private val fileArg: VirtualFile
): FileEditor, FileEditorLocation {

    private val userDataHolder: UserDataHolder = UserDataHolderBase()
    private var viewerState: KSViewerState = KSViewerState()

    override fun getName(): String = EDITOR_NAME

    override fun addPropertyChangeListener(listener: PropertyChangeListener) = Unit

    override fun removePropertyChangeListener(listener: PropertyChangeListener) = Unit

    override fun dispose() = Unit

    override fun isModified(): Boolean = false

    override fun getCurrentLocation(): FileEditorLocation = this

    override fun getEditor(): FileEditor = this

    override fun <T : Any?> getUserData(key: Key<T>): T? = userDataHolder.getUserData(key)

    override fun <T : Any?> putUserData(key: Key<T>, value: T?) = userDataHolder.putUserData(key, value)

    override fun compareTo(other: FileEditorLocation?): Int = 1

    override fun isValid(): Boolean = this.fileArg.isValid

    override fun getFile(): VirtualFile = this.fileArg

    override fun getState(level: FileEditorStateLevel): FileEditorState = viewerState

    override fun setState(state: FileEditorState) {
        val newState = state as? KSViewerState
        this.viewerState = newState ?: KSViewerState()
    }
}