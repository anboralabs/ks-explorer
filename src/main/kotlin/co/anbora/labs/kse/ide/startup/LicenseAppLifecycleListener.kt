package co.anbora.labs.kse.ide.startup

import co.anbora.labs.kse.license.CheckLicense
import com.intellij.ide.AppLifecycleListener
import com.intellij.util.concurrency.AppExecutorUtil
import java.util.concurrent.TimeUnit

/**
 * Schedules the license validation at application level, independent of any project lifecycle.
 * This ensures the check is performed even if no project is opened or if projects are closed.
 */
class LicenseAppLifecycleListener : AppLifecycleListener {
    override fun welcomeScreenDisplayed() {
        // Schedule the license validation 5 minutes after IDE startup to ensure LicensingFacade is initialized
        AppExecutorUtil.getAppScheduledExecutorService().schedule({
            val licensed = CheckLicense.isLicensed() ?: false
            // Always request license if not licensed, regardless of project state.
            if (!licensed) {
                CheckLicense.requestLicense("Support plugin.")
            }
        }, 5, TimeUnit.MINUTES)
    }
}
