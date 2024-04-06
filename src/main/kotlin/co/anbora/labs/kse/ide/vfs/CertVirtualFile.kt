package co.anbora.labs.kse.ide.vfs

import co.anbora.labs.kse.ide.gui.view.DViewCertificate
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.kse.crypto.x509.X509CertUtil
import java.security.cert.X509Certificate

class CertVirtualFile(
    private val alias: String,
    private val parent: VirtualFile,
    private val certs: Array<X509Certificate>
): KSVirtualFile(alias, parent, X509CertUtil.getCertsEncodedX509Pem(certs).toByteArray()) {
    override fun getFileEditor(project: Project): FileEditor {
        return DViewCertificate(project, this, certs, DViewCertificate.IMPORT_EXPORT) { _, _ -> }
    }
}