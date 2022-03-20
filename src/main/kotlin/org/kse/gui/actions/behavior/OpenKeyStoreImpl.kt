package org.kse.gui.actions.behavior

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import org.kse.crypto.CryptoException
import org.kse.crypto.Password
import org.kse.crypto.keystore.KeyStoreUtil
import org.kse.gui.AddKeyStore
import org.kse.gui.statusbar.StatusBar
import java.io.File
import java.io.FileNotFoundException
import java.security.KeyStore
import java.text.MessageFormat
import java.util.*
import javax.swing.JPasswordField

class OpenKeyStoreImpl(
    private val project: Project,
    private val statusBar: StatusBar,
    private val store: AddKeyStore,
    private val fileEditor: FileEditor,
    private val passwordField: JPasswordField
): ActionBehavior {

    private val res = ResourceBundle.getBundle("org/kse/gui/actions/resources")

    override fun doAction() {
        val file = fileEditor.file
        if (file != null) {
            val keyStoreFile: File = file.toNioPath().toFile()
            val password = Password(passwordField.password)
            val openedKeyStore: KeyStore = loadKeyStore(keyStoreFile, password)
                ?: throw CryptoException(MessageFormat.format(res.getString("OpenAction.PasswordIncorrectKeyStore.Cause")))
            store.addKeyStore(openedKeyStore, keyStoreFile, password)
            statusBar.setDefaultStatusBarText()
        }
    }

    private fun loadKeyStore(keyStoreFile: File, password: Password): KeyStore? {
        // try to load keystore
        return try {
            KeyStoreUtil.load(keyStoreFile, password)
        } catch (klex: CryptoException) {
            // show icon error
            null
        } catch (klex: FileNotFoundException) {
            null
        }
    }
}
