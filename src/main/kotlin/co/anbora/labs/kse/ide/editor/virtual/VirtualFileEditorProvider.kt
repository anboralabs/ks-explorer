package co.anbora.labs.kse.ide.editor.virtual

import co.anbora.labs.kse.ide.editor.EditorProvider
import co.anbora.labs.kse.ide.gui.view.DViewError
import co.anbora.labs.kse.ide.vfs.KSVirtualFile
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.kse.crypto.filetype.CryptoFileType

class VirtualFileEditorProvider: EditorProvider() {

    override fun fileTypes(): Set<CryptoFileType> {
        return emptySet()
    }

    override fun createLicensedEditorAsync(project: Project, file: VirtualFile): FileEditor {
        return if (file is KSVirtualFile) {
            file.getFileEditor(project)
        } else {
            DViewError(project, file, "Invalid file")
        }
    }

    override fun getEditorTypeId(): String = PRIVATE_EDITOR_TYPE_ID

    override fun accept(project: Project, file: VirtualFile): Boolean {
        return file is KSVirtualFile
    }

    companion object {
        val PRIVATE_EDITOR_TYPE_ID = "co.anbora.labs.kse.virtual.editor"
    }
}