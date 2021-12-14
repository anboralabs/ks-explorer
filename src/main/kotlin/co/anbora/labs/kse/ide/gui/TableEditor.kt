package co.anbora.labs.kse.ide.gui

import co.anbora.labs.kse.lang.KSLanguage.EDITOR_NAME
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.UserDataHolder
import com.intellij.openapi.util.UserDataHolderBase
import java.beans.PropertyChangeListener

abstract class TableEditor(
    private val userDataHolder: UserDataHolder = UserDataHolderBase()
): FileEditor, FileEditorLocation {

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
}