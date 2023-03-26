package co.anbora.labs.kse.ide.editor

import com.intellij.diff.editor.DiffVirtualFile
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.SingleRootFileViewProvider
import org.kse.crypto.filetype.CryptoFileType
import org.kse.crypto.filetype.CryptoFileUtil
import java.util.function.Predicate

abstract class EditorProvider: AsyncFileEditorProvider, DumbAware {

    abstract fun fileTypes(): Set<CryptoFileType>

    private fun isFileType(file: VirtualFile): Boolean = try {
        val fileType = CryptoFileUtil.detectFileType(file.toNioPath().toFile())
        fileType in fileTypes()
    } catch (ex: Exception) {
        false
    }

    open fun acceptFile(): Predicate<VirtualFile> = Predicate { true }

    override fun accept(project: Project, file: VirtualFile): Boolean {
        return isFileType(file)
                && !SingleRootFileViewProvider.isTooLargeForContentLoading(file)
                && file !is DiffVirtualFile
                && acceptFile().test(file)
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor =
        createEditorAsync(project, file).build()

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR

}
