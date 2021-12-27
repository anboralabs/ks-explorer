package org.kse.gui.actions

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
import javax.swing.JPasswordField

class OpenAction(
    private val project: Project,
    private val statusBar: StatusBar,
    private val store: AddKeyStore,
    private val fileEditor: FileEditor,
    private val passwordField: JPasswordField
): KeyStoreExplorerAction(project, statusBar) {
    override fun doAction() {
        val file = fileEditor.file
        if (file != null) {
            val keyStoreFile: File = file.toNioPath().toFile()
            val password = Password(passwordField.password)
            val openedKeyStore: KeyStore = loadKeyStore(keyStoreFile, password)
                ?: throw CryptoException(MessageFormat.format(res.getString("OpenAction.PasswordIncorrectKeyStore.Cause")))
            store.addKeyStore(openedKeyStore, keyStoreFile, password)
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