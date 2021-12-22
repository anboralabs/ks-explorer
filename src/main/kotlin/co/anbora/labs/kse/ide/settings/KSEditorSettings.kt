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
class KSEditorSettings: PersistentStateComponent<SettingOptionSet> {

    private var currentOptions = SettingOptionSet()

    override fun getState(): SettingOptionSet = currentOptions

    override fun loadState(state: SettingOptionSet) {
       this.currentOptions = state
    }

    var entryName: Boolean
        get() = state.entryName.active
        set(value) { state.entryName.active = value }

    var keySize: Boolean
        get() = state.keySize.active
        set(value) { state.keySize.active = value }

    var certificateExpiry: Boolean
        get() = state.certificateExpiry.active
        set(value) { state.certificateExpiry.active = value }

    var subjectKeyIdentifier: Boolean
        get() = state.subjectKeyIdentifier.active
        set(value) { state.subjectKeyIdentifier.active = value }

    var issuerDistinguishedName: Boolean
        get() = state.issuerDistinguishedName.active
        set(value) { state.issuerDistinguishedName.active = value }

    var issuerCommonName: Boolean
        get() = state.issuerCommonName.active
        set(value) { state.issuerCommonName.active = value }

    var issuerOrganizationName: Boolean
        get() = state.issuerOrganizationName.active
        set(value) { state.issuerOrganizationName.active = value }

    var algorithm: Boolean
        get() = state.algorithm.active
        set(value) { state.algorithm.active = value }

    var curve: Boolean
        get() = state.curve.active
        set(value) { state.curve.active = value }

    var lastModified: Boolean
        get() = state.lastModified.active
        set(value) { state.lastModified.active = value }

    var authorityKeyIdentifier: Boolean
        get() = state.authorityKeyIdentifier.active
        set(value) { state.authorityKeyIdentifier.active = value }

    var subjectDistinguishedName: Boolean
        get() = state.subjectDistinguishedName.active
        set(value) { state.subjectDistinguishedName.active = value }

    var subjectCommonName: Boolean
        get() = state.subjectCommonName.active
        set(value) { state.subjectCommonName.active = value }

    var subjectOrganizationName: Boolean
        get() = state.subjectOrganizationName.active
        set(value) { state.subjectOrganizationName.active = value }

    fun resetColumns() = state.resetOptions()

    fun sortColumns(order: () -> Int) = state.setIndexColumns(order)

    fun nofColumns(): Int = state.getActiveOptions()

    fun activeColumns(): List<Option> = state.getColumnSettings()

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