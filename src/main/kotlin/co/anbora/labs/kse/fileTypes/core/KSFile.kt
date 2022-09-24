package co.anbora.labs.kse.fileTypes.core

import co.anbora.labs.kse.fileTypes.KSLanguage
import com.intellij.diff.editor.DiffVirtualFile
import com.intellij.lang.LanguageUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.SingleRootFileViewProvider

object KSFile {

    fun acceptKSFile(project: Project, file: VirtualFile): Boolean {
        return isKSFile(
            project,
            file
        ) && !SingleRootFileViewProvider.isTooLargeForContentLoading(file) && file !is DiffVirtualFile
    }

    private fun isKSFile(project: Project?, file: VirtualFile?): Boolean {
        if (project == null || file == null) {
            return false
        }
        val language = LanguageUtil.getLanguageForPsi(project, file)
        return language != null && language.isKindOf(KSLanguage)
    }

}