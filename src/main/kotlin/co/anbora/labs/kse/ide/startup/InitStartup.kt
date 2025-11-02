package co.anbora.labs.kse.ide.startup

import co.anbora.labs.kse.license.CheckLicense
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.util.concurrency.AppExecutorUtil
import java.util.concurrent.TimeUnit

class InitStartup: ProjectActivity {
    override suspend fun execute(project: Project) {
        AppExecutorUtil.getAppScheduledExecutorService().schedule({
            val licensed = CheckLicense.isLicensed() ?: false

            if (!licensed && !project.isDisposed) {
                CheckLicense.requestLicense("Support plugin.")
            }
        }, 5, TimeUnit.MINUTES)
    }
}