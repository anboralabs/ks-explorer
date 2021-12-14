package co.anbora.labs.kse.ide.settings

import co.anbora.labs.kse.lang.core.StorageHelper.KS_STATE_STORAGE_FILE
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "KSEditorSettings",
    storages = [Storage(KS_STATE_STORAGE_FILE)]
)
class KSEditorSettings: PersistentStateComponent<OptionSet> {

    private var currentOptions = OptionSet()

    override fun getState(): OptionSet = currentOptions

    override fun loadState(state: OptionSet) {
       this.currentOptions = state
    }

    var keySize: Boolean
        get() = state.keySize
        set(value) { state.keySize = value }

    var certificateExpiry: Boolean
        get() = state.certificateExpiry
        set(value) { state.certificateExpiry = value }

    var subjectKeyIdentifier: Boolean
        get() = state.subjectKeyIdentifier
        set(value) { state.subjectKeyIdentifier = value }

    var issuerDistinguishedName: Boolean
        get() = state.issuerDistinguishedName
        set(value) { state.issuerDistinguishedName = value }

    var issuerCommonName: Boolean
        get() = state.issuerCommonName
        set(value) { state.issuerCommonName = value }

    var issuerOrganizationName: Boolean
        get() = state.issuerOrganizationName
        set(value) { state.issuerOrganizationName = value }

    var algorithm: Boolean
        get() = state.algorithm
        set(value) { state.algorithm = value }

    var curve: Boolean
        get() = state.curve
        set(value) { state.curve = value }

    var lastModified: Boolean
        get() = state.lastModified
        set(value) { state.lastModified = value }

    var authorityKeyIdentifier: Boolean
        get() = state.authorityKeyIdentifier
        set(value) { state.authorityKeyIdentifier = value }

    var subjectDistinguishedName: Boolean
        get() = state.subjectDistinguishedName
        set(value) { state.subjectDistinguishedName = value }

    var subjectCommonName: Boolean
        get() = state.subjectCommonName
        set(value) { state.subjectCommonName = value }

    var subjectOrganizationName: Boolean
        get() = state.subjectOrganizationName
        set(value) { state.subjectOrganizationName = value }

    companion object {
        @JvmStatic
        fun getInstance(): KSEditorSettings {
            val application = ApplicationManager.getApplication()
            return if (application.isDisposed) KSEditorSettings() else application.getService(
                KSEditorSettings::class.java
            )
        }
    }
}