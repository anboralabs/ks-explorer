package org.kse.gui.actions

import com.intellij.openapi.project.Project
import org.kse.gui.actions.behavior.ActionBehavior
import org.kse.gui.statusbar.StatusBar

class PressEnterAction(
    private val project: Project,
    private val statusBar: StatusBar,
    private val openAction: ActionBehavior
): KeyStoreExplorerKeyAction(project, statusBar) {
    override fun doAction() = openAction.doAction()
}