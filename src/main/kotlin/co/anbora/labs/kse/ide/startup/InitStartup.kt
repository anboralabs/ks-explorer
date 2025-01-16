package co.anbora.labs.kse.ide.startup

import co.anbora.labs.kse.license.CheckLicense
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class InitStartup: ProjectActivity {
    override suspend fun execute(project: Project) {
        val licensed = CheckLicense.isLicensed() ?: false

        if (!licensed) {
            CheckLicense.requestLicense("Support plugin.")
        }
    }
}