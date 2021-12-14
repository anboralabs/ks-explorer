package co.anbora.labs.kse.ide.settings

data class OptionSet(
    var entryName: Boolean = true,
    var keySize: Boolean = true,
    var certificateExpiry: Boolean = true,
    var subjectKeyIdentifier: Boolean = false,
    var issuerDistinguishedName: Boolean = false,
    var issuerCommonName: Boolean = false,
    var issuerOrganizationName: Boolean = false,
    var algorithm: Boolean = true,
    var curve: Boolean = false,
    var lastModified: Boolean = true,
    var authorityKeyIdentifier: Boolean = false,
    var subjectDistinguishedName: Boolean = false,
    var subjectCommonName: Boolean = false,
    var subjectOrganizationName: Boolean = false
)
