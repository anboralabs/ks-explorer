package org.kse.gui.actions

import com.intellij.openapi.project.Project
import org.kse.gui.error.DError
import org.kse.gui.statusbar.StatusBar
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.util.*

abstract class KeyStoreExplorerKeyAction(
    private val project: Project,
    private val statusBar: StatusBar
): KeyAdapter() {

    protected val res = ResourceBundle.getBundle("org/kse/gui/actions/resources")

    override fun keyPressed(e: KeyEvent?) {
        try {
            if (KeyEvent.VK_ENTER == e?.keyCode) {
                statusBar.setDefaultStatusBarText()
                doAction()
            }
        } catch (ex: Exception) {
            DError.displayError(project, ex)
        }
    }

    abstract fun doAction()

}
