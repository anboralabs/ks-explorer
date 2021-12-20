package co.anbora.labs.kse.ide.settings

import co.anbora.labs.kse.lang.settings.Settings.INIT_COLUMN

data class SettingOptionSet(
    var entryName: Option = Option(true),
    var keySize: Option = Option(true),
    var certificateExpiry: Option = Option(true),
    var subjectKeyIdentifier: Option = Option(),
    var issuerDistinguishedName: Option = Option(),
    var issuerCommonName: Option = Option(),
    var issuerOrganizationName: Option = Option(),
    var algorithm: Option = Option(true),
    var curve: Option = Option(),
    var lastModified: Option = Option(true),
    var authorityKeyIdentifier: Option = Option(),
    var subjectDistinguishedName: Option = Option(),
    var subjectCommonName: Option = Option(),
    var subjectOrganizationName: Option = Option()
) {

    private val options = listOf(
        entryName, algorithm, keySize, curve, certificateExpiry,
        lastModified, authorityKeyIdentifier,
        subjectKeyIdentifier,
        issuerDistinguishedName, subjectDistinguishedName,
        issuerCommonName, subjectCommonName,
        issuerOrganizationName, subjectOrganizationName
    )

    fun setIndexColumns(order: () -> Int) {
        options.forEach {
            if (it.active) {
                it.index = order()
            }
        }
    }

    fun getActiveOptions(): Int {
        return options.count { it.active }
    }

    fun resetOptions() {
        options.forEach {
            it.index = INIT_COLUMN
        }
    }
}
