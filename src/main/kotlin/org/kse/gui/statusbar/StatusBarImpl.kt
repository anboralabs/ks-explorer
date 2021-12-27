package org.kse.gui.statusbar

import com.intellij.openapi.project.Project
import org.kse.gui.HistoryKeyStore
import org.kse.gui.error.DError
import org.kse.utilities.history.KeyStoreHistory
import java.security.KeyStoreException
import java.text.MessageFormat
import java.util.*
import javax.swing.JLabel

class StatusBarImpl(
    private val project: Project,
    private val statusBar: JLabel,
    private val historyKS: HistoryKeyStore
): StatusBar {

    private val res = ResourceBundle.getBundle("org/kse/gui/resources")

    override fun setStatusBarText(status: String?) {
        statusBar.text = status
    }

    override fun setDefaultStatusBarText() {
        val history: KeyStoreHistory? = historyKS.getActiveKeyStoreHistory()

        if (history == null) {
            setStatusBarText(res.getString("KseFrame.noKeyStore.statusbar"))
        } else {
            setStatusBarText(getKeyStoreStatusText(history))
        }
    }

    private fun getKeyStoreStatusText(history: KeyStoreHistory): String? {
        // Status Text: 'KeyStore Type, Size, Path'
        val currentState = history.currentState
        val ksLoaded = currentState.keyStore
        val size: Int = try {
            ksLoaded.size()
        } catch (ex: KeyStoreException) {
            DError.displayError(project, ex)
            return ""
        }
        val keyStoreType = currentState.type
        val aliases: Array<String> = historyKS.getSelectedEntryAliases()
        return MessageFormat.format(
            res.getString("KseFrame.entries.statusbar"),
            keyStoreType.friendly(), size, aliases.size, history.path
        )
    }
}