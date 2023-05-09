package co.anbora.labs.kse.ide.editor

import com.intellij.diff.editor.DiffVirtualFile
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.SingleRootFileViewProvider
import org.kse.crypto.filetype.CryptoFileType
import org.kse.crypto.x509.X509CertUtil.isBase64Certificate
import org.kse.crypto.x509.X509CertUtil.isPemCertificate

private const val CERT_EDITOR_TYPE_ID = "co.anbora.labs.kse.pem.cert.editor"

class PemCertEditorProvider: CertEditorProvider() {

    override fun fileTypes(): Set<CryptoFileType> = setOf(
        CryptoFileType.CERT
    )

    override fun getEditorTypeId(): String = CERT_EDITOR_TYPE_ID

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.PLACE_BEFORE_DEFAULT_EDITOR

    override fun accept(project: Project, file: VirtualFile): Boolean {
        return isFileType(file)
                && !SingleRootFileViewProvider.isTooLargeForContentLoading(file)
                && file !is DiffVirtualFile
                && (isPemCertificate(file.contentsToByteArray()) || isBase64Certificate(file.contentsToByteArray()))
    }
}