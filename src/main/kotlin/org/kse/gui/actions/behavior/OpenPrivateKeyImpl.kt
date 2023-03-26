package org.kse.gui.actions.behavior

import co.anbora.labs.kse.fileTypes.core.CertUtils
import com.intellij.openapi.fileEditor.FileEditor
import org.kse.crypto.CryptoException
import org.kse.crypto.Password
import org.kse.gui.AddPrivateKey
import java.io.File
import java.io.FileNotFoundException
import java.security.PrivateKey
import java.text.MessageFormat
import java.util.*
import javax.swing.JPasswordField

class OpenPrivateKeyImpl(
    private val store: AddPrivateKey,
    private val fileEditor: FileEditor,
    private val passwordField: JPasswordField
): ActionBehavior {
    private val res = ResourceBundle.getBundle("org/kse/gui/actions/resources")

    override fun doAction() {
        val file = fileEditor.file
        if (file != null) {
            val privateKeyFile: File = file.toNioPath().toFile()
            val password = Password(passwordField.password)
            val openedPrivateKey: PrivateKey = loadPrivateKey(privateKeyFile, password)
                ?: throw CryptoException(MessageFormat.format(res.getString("OpenAction.PasswordIncorrectKeyStore.Cause")))
            store.addPrivateKey(openedPrivateKey)
        }
    }

    private fun loadPrivateKey(privateKey: File, password: Password): PrivateKey? {
        // try to load keystore
        return try {
            CertUtils.load(privateKey, password)
        } catch (klex: CryptoException) {
            // show icon error
            null
        } catch (klex: FileNotFoundException) {
            null
        }
    }
}
