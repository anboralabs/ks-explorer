package org.kse.gui.actions

import com.intellij.openapi.project.Project
import org.kse.gui.statusbar.StatusBar
import java.awt.Toolkit
import javax.swing.ImageIcon
import javax.swing.KeyStroke

class NewAction(
    private val project: Project,
    private val statusBar: StatusBar
): KeyStoreExplorerAction(project, statusBar) {

    init {
        putValue(
            ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                'N',
                Toolkit.getDefaultToolkit().menuShortcutKeyMaskEx
            )
        )
        putValue(LONG_DESCRIPTION, res.getString("NewAction.statusbar"))
        putValue(NAME, res.getString("NewAction.text"))
        putValue(SHORT_DESCRIPTION, res.getString("NewAction.tooltip"))
        putValue(
            SMALL_ICON,
            ImageIcon(Toolkit.getDefaultToolkit().createImage(javaClass.getResource("images/new.png")))
        )
    }

    override fun doAction() {

    }
}
