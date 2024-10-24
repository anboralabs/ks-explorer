package co.anbora.labs.kse.ide.editor

import co.anbora.labs.kse.fileTypes.CertFileType
import co.anbora.labs.kse.fileTypes.KeystoreFileType
import co.anbora.labs.kse.fileTypes.PemFileType
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

private val extensions = setOf(
    "pem", "cer", "crt", //pem
    "pub", "key", "p7", "p7b", "pkipath", "spc", "p10", "spkac", "pkcs8", "pvk", "crl", //certs
    "jks", "jceks", "bks", "p12", "uber", "bcfks", "pfx" //keystore
)

abstract class EditorProvider: AsyncFileEditorProvider, DumbAware {

    abstract fun fileTypes(): Set<CryptoFileType>

    fun isFileType(file: VirtualFile): Boolean {
        return try {
            val isCompatibleExtension = isValidFileType(file)
            if (!isCompatibleExtension) {
                return false
            }
            val fileType = CryptoFileUtil.detectFileType(file.toNioPath().toFile())
            fileType in fileTypes()
        } catch (ex: Exception) {
            false
        }
    }

    private fun isValidFileType(file: VirtualFile): Boolean {
        return file.fileType is KeystoreFileType
                || file.fileType is CertFileType
                || file.fileType is PemFileType
    }

    override fun accept(project: Project, file: VirtualFile): Boolean {
        return  isFileType(file)
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
                    return DViewError(project, file, "If you want to support my work, please buy a license only 5 USD per year for plugin maintaining.")
                }
                return createLicensedEditorAsync(project, file)
            }
        }
    }

    abstract fun createLicensedEditorAsync(project: Project, file: VirtualFile): FileEditor

}
