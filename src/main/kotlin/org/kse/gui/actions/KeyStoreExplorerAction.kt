package org.kse.gui.actions

import com.intellij.openapi.project.Project
import org.kse.gui.error.DError
import org.kse.gui.statusbar.StatusBar
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

abstract class KeyStoreExplorerAction(
    private val project: Project,
    private val statusBar: StatusBar
): AbstractAction() {

    override fun actionPerformed(e: ActionEvent?) {
        try {
            statusBar.setDefaultStatusBarText()
            doAction()
        } catch (ex: Exception) {
            DError.displayError(project, ex)
        }
    }

    abstract fun doAction()

}