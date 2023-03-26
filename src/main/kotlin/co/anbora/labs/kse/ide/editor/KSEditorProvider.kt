package co.anbora.labs.kse.ide.editor

import co.anbora.labs.kse.ide.gui.swing.KeyStoreFrame
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.kse.crypto.filetype.CryptoFileType
import java.util.function.Predicate

private const val EDITOR_TYPE_ID = "co.anbora.labs.kse.editor"
class KSEditorProvider: EditorProvider() {

    override fun fileTypes(): Set<CryptoFileType> = setOf(
        CryptoFileType.JCEKS_KS,
        CryptoFileType.JKS_KS,
        CryptoFileType.PKCS12_KS,
        CryptoFileType.BKS_KS,
        CryptoFileType.BKS_V1_KS,
        CryptoFileType.BCFKS_KS,
        CryptoFileType.UBER_KS
    )

    override fun getEditorTypeId(): String = EDITOR_TYPE_ID

    override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
        return object : AsyncFileEditorProvider.Builder() {
            override fun build(): FileEditor =
                KeyStoreFrame(project, file)
        }
    }
}