package co.anbora.labs.kse.ide.editor.pvk.unenc

import co.anbora.labs.kse.fileTypes.core.CertUtils
import co.anbora.labs.kse.ide.editor.EditorProvider
import co.anbora.labs.kse.ide.gui.view.DViewError
import co.anbora.labs.kse.ide.gui.view.DViewPrivateKey
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.apache.commons.io.FileUtils
import org.kse.crypto.filetype.CryptoFileType
import org.kse.crypto.privatekey.OpenSslPvkUtil
import java.io.File
import java.security.PrivateKey

private const val PRIVATE_EDITOR_TYPE_ID = "co.anbora.labs.kse.private.unenc.ssl.editor"
class OpenSslEditorProvider: EditorProvider() {
    override fun fileTypes(): Set<CryptoFileType> = setOf(
        CryptoFileType.UNENC_OPENSSL_PVK
    )

    override fun getEditorTypeId(): String = PRIVATE_EDITOR_TYPE_ID

    override fun createLicensedEditorAsync(project: Project, file: VirtualFile): FileEditor {
        val privateKey = openPrivateKey(file.toNioPath().toFile())
        return if (privateKey != null) {
            DViewPrivateKey(project, file, privateKey)
        } else {
            DViewError(project, file, "Invalid OpenSSL Cert")
        }
    }

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.PLACE_BEFORE_DEFAULT_EDITOR

    private fun openPrivateKey(file: File?): PrivateKey? = try {
        if (file == null) {
            null
        }
        val data: ByteArray = CertUtils.decodeIfBase64(FileUtils.readFileToByteArray(file))
        OpenSslPvkUtil.load(data)
    } catch (ex: Exception) {
        null
    }
}
