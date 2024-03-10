package co.anbora.labs.kse.ide.vfs

import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import org.kse.crypto.Password
import org.kse.crypto.keystore.KeyStoreType
import org.kse.crypto.publickey.OpenSslPubUtil
import org.kse.crypto.x509.X509CertUtil
import org.kse.gui.crypto.privatekey.PrivateKeyUtils
import org.kse.gui.error.DProblem
import org.kse.gui.error.Problem
import org.kse.gui.password.DGetPassword
import org.kse.utilities.history.KeyStoreState
import java.nio.file.Files
import java.nio.file.Path
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.cert.X509Certificate
import java.text.MessageFormat
import java.util.*
import java.util.concurrent.CompletableFuture
import javax.crypto.SecretKey

object VirtualFileHelper {

    private val res = ResourceBundle.getBundle("org/kse/gui/actions/resources")
    private const val CERT_EXTENSION = ".cer"
    private const val PUBLIC_KEY_EXTENSION = ".pub"
    private const val PRIVATE_KEY_EXTENSION = ".pvk"

    fun showCertificateSelectedEntry(project: Project, alias: String, keyStore: KeyStore) {
        val certificate = keyStore.getCertificateChain(alias) ?: arrayOf(keyStore.getCertificate(alias))
        val certs: Array<X509Certificate> = X509CertUtil.convertCertificates(certificate)
        val pem = X509CertUtil.getCertsEncodedX509Pem(certs)

        val certPath = generateTempFile(alias, pem.toByteArray(), CERT_EXTENSION)
        openInEditor(project, certPath)
    }

    fun showKeySelectedEntry(project: Project, alias: String, keyStore: KeyStore, state: KeyStoreState) {
        val password = getEntryPassword(project, alias, state) ?: return

        val keyStore: KeyStore = state.getKeyStore()
        val key = keyStore.getKey(alias, password.toCharArray())

        when (key) {
            is SecretKey -> {

            }
            is PrivateKey -> {
                showKeySelectedPrivateEntry(project, alias, key)
            }
            is PublicKey -> {
                showKeySelectedPublicEntry(project, alias, key)
            }
        }
    }

    private fun showKeySelectedPrivateEntry(project: Project, alias: String, privateKey: PrivateKey) {
        val encoded = PrivateKeyUtils.getOpenSslEncodedPrivateKey(privateKey, true, null, null)
        val certPath = generateTempFile(alias, encoded, PRIVATE_KEY_EXTENSION)
        openInEditor(project, certPath)
    }

    private fun showKeySelectedPublicEntry(project: Project, alias: String, publicKey: PublicKey) {
        val encoded = OpenSslPubUtil.getPem(publicKey).toByteArray()
        val certPath = generateTempFile(alias, encoded, PUBLIC_KEY_EXTENSION)
        openInEditor(project, certPath)
    }

    private fun generateTempFile(alias: String, data: ByteArray, ext: String): Path {
        // create a temporary file
        val tempFile: Path = Files.createTempFile(alias, ext)
        val generatedPath = tempFile.resolveSibling(alias + ext)
        // Writes a string to the above temporary file
        Files.write(generatedPath, data)
        return generatedPath
    }

    private fun openInEditor(project: Project, file: Path){
        CompletableFuture.supplyAsync {
            LocalFileSystem.getInstance().findFileByNioFile(file)
        }.whenComplete { vfs, u ->
            if (vfs != null) {
                invokeLater {
                    FileEditorManager.getInstance(project).openFileEditor(
                        OpenFileDescriptor(project, vfs),
                        false
                    )
                }
            }
        }
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
