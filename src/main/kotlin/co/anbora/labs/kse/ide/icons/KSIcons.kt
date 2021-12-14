package co.anbora.labs.kse.ide.icons

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object KSIcons {

    val FILE = getIcon("icon_file.svg")

    private fun getIcon(path: String): Icon {
        return IconLoader.findIcon("/icons/$path") as Icon
    }
}
