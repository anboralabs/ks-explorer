package co.anbora.labs.kse.ide.vfs

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.kse.crypto.Password
import org.kse.crypto.keystore.KeyStoreType
import org.kse.crypto.x509.X509CertUtil
import org.kse.gui.error.DProblem
import org.kse.gui.error.Problem
import org.kse.gui.password.DGetPassword
import org.kse.utilities.history.KeyStoreState
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.cert.X509Certificate
import java.text.MessageFormat
import java.util.*
import javax.crypto.SecretKey

object VirtualFileHelper {

    private val res = ResourceBundle.getBundle("org/kse/gui/actions/resources")

    fun showCertificateSelectedEntry(project: Project, parent: VirtualFile, alias: String, keyStore: KeyStore) {
        val certificate = keyStore.getCertificateChain(alias) ?: arrayOf(keyStore.getCertificate(alias))
        val certs: Array<X509Certificate> = X509CertUtil.convertCertificates(certificate)

        val virtualFile = CertVirtualFile(alias, parent, certs)
        openInEditor(project, virtualFile)
    }

    fun showKeySelectedEntry(project: Project, parent: VirtualFile, alias: String, keyStore: KeyStore, state: KeyStoreState) {
        val password = getEntryPassword(project, alias, state) ?: return

        val keyStore: KeyStore = state.getKeyStore()
        val key = keyStore.getKey(alias, password.toCharArray())

        when (key) {
            is SecretKey -> {

            }
            is PrivateKey -> {
                showKeySelectedPrivateEntry(project, parent, alias, key)
            }
            is PublicKey -> {
                showKeySelectedPublicEntry(project, parent, alias, key)
            }
        }
    }

    private fun showKeySelectedPrivateEntry(project: Project, parent: VirtualFile, alias: String, privateKey: PrivateKey) {
        val virtualFile = PrivateKeyVirtualFile(alias, parent, privateKey)
        openInEditor(project, virtualFile)
    }

    private fun showKeySelectedPublicEntry(project: Project, parent: VirtualFile, alias: String, publicKey: PublicKey) {
        val virtualFile = PublicKeyVirtualFile(alias, parent, publicKey)
        openInEditor(project, virtualFile)
    }

    private fun openInEditor(project: Project, virtualFile: KSVirtualFile) {
        FileEditorManager.getInstance(project).openFileEditor(
            OpenFileDescriptor(project, virtualFile),
            false
        )
    }

    private fun getEntryPassword(project: Project, alias: String?, state: KeyStoreState): Password? {
        var password = state.getEntryPassword(alias)
        if (password == null) {
            password = if (!KeyStoreType.resolveJce(state.keyStore.type).hasEntryPasswords()) {
                Password(null as CharArray?)
            } else {
                unlockEntry(project, alias, state)
            }
        }
        return password
    }

    private fun unlockEntry(project: Project, alias: String?, state: KeyStoreState): Password? {
        return try {
            val keyStore = state.keyStore
            val dGetPassword = DGetPassword(
                project,
                MessageFormat.format(res.getString("KeyStoreExplorerAction.UnlockEntry.Title"), alias)
            )
            dGetPassword.show()

            val password = dGetPassword.password ?: return null
            keyStore.getKey(alias, password.toCharArray()) // Test password is correct
            state.setEntryPassword(alias, password)

            password
        } catch (ex: GeneralSecurityException) {
            val problemStr = MessageFormat.format(
                res.getString("KeyStoreExplorerAction.NoUnlockEntry.Problem"),
                alias
            )
            val causes = arrayOf<String>(res.getString("KeyStoreExplorerAction.PasswordIncorrectEntry.Cause"))
            val problem = Problem(problemStr, causes, ex)
            val dProblem = DProblem(
                project, res.getString("KeyStoreExplorerAction.ProblemUnlockingEntry.Title"),
                problem
            )
            dProblem.show()
            null
        }
    }

}
