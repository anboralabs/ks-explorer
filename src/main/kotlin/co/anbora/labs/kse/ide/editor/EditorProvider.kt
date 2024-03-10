package co.anbora.labs.kse.ide.editor

import co.anbora.labs.kse.ide.gui.view.DViewError
import co.anbora.labs.kse.license.CheckLicense
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.SingleRootFileViewProvider
import org.kse.crypto.filetype.CryptoFileType
import org.kse.crypto.filetype.CryptoFileUtil

abstract class EditorProvider: AsyncFileEditorProvider, DumbAware {

    abstract fun fileTypes(): Set<CryptoFileType>

    fun isFileType(file: VirtualFile): Boolean = try {
        val fileType = CryptoFileUtil.detectFileType(file.toNioPath().toFile())
        fileType in fileTypes()
    } catch (ex: Exception) {
        false
    }

    override fun accept(project: Project, file: VirtualFile): Boolean {
        return isFileType(file)
                && !SingleRootFileViewProvider.isTooLargeForContentLoading(file)
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        val licensed = CheckLicense.isLicensed() ?: true
        if (!licensed) {
            return DViewError(project, file, "If you want to support my work, please buy a license only 5 USD per year for plugin maintaining.")
        }
        return createLicensedEditorAsync(project, file)
    }

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR

    override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
        return object : AsyncFileEditorProvider.Builder() {
            override fun build(): FileEditor {
                val licensed = CheckLicense.isLicensed() ?: true
                if (!licensed) {
                    return DViewError(project, file, "If you want to support my work, please buy a license only 3 USD per year for plugin maintaining.")
                }
                return createLicensedEditorAsync(project, file)
            }
        }
    }

    abstract fun createLicensedEditorAsync(project: Project, file: VirtualFile): FileEditor

}
