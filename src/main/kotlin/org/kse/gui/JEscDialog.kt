package org.kse.gui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.AbstractAction
import javax.swing.JComponent
import javax.swing.JRootPane
import javax.swing.KeyStroke

open class JEscDialog(
    private val project: Project,
    private val type: IdeModalityType
): DialogWrapper(project, true, type) {
    override fun createCenterPanel(): JComponent {
        val rootPane = JRootPane()

        // Escape key closes dialogs
        val stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0)
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke, "escapeKey")
        rootPane.actionMap.put("escapeKey", object : AbstractAction() {
            private val serialVersionUID = 1L
            override fun actionPerformed(e: ActionEvent) {
                doCancelAction()
                dispose()
            }
        })
        return rootPane
    }
}