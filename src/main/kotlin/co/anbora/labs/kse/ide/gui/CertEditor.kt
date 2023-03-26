package co.anbora.labs.kse.ide.gui

import co.anbora.labs.kse.fileTypes.CertFileType.EDITOR_NAME
import co.anbora.labs.kse.ide.editor.KSViewerState
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.UserDataHolder
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Container
import java.beans.PropertyChangeListener
import javax.swing.JRootPane

abstract class CertEditor(): Container(), FileEditor, FileEditorLocation {

    private lateinit var projectArg: Project
    private lateinit var fileArg: VirtualFile
    private var rootPane: JRootPane = createRootPane()

    constructor(projectArg: Project, fileArg: VirtualFile) : this() {
        this.projectArg = projectArg
        this.fileArg = fileArg
        containerInit()
    }

    protected open fun containerInit() {
        setRootPane(createRootPane())
    }

    protected open fun createRootPane(): JRootPane {
        val rp = JRootPane()
        // NOTE: this uses setOpaque vs LookAndFeel.installProperty as there
        // is NO reason for the RootPane not to be opaque. For painting to
        // work the contentPane must be opaque, therefor the RootPane can
        // also be opaque.
        rp.isOpaque = true
        return rp
    }

    protected open fun setRootPane(root: JRootPane) {
        remove(rootPane)
        rootPane = root
        add(rootPane, BorderLayout.CENTER)
    }

    override fun remove(comp: Component) {
        if (comp === rootPane) {
            super.remove(comp)
        } else {
            getContentPane().remove(comp)
        }
    }

    open fun getContentPane(): Container {
        return getRootPane().contentPane
    }

    open fun getRootPane(): JRootPane {
        return rootPane
    }

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