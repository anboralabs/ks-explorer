package co.anbora.labs.kse.ide.gui

import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JPasswordField


class KeyStorePasswordDialog: DialogWrapper(true) {

    private val inputPassword = JPasswordField()

    init {
        title = "KeyStore Password"
        init()
    }

    override fun createCenterPanel(): JComponent {
        val root = JPanel(BorderLayout())
        root.preferredSize = Dimension(200, 30)

        root.add(inputPassword, BorderLayout.CENTER);

        return root
    }

    fun getPassword(): String = inputPassword.password.toString()
}