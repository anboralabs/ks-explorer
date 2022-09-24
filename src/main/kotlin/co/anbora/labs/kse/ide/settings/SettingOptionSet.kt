package co.anbora.labs.kse.ide.settings

import co.anbora.labs.kse.fileTypes.settings.Settings.ENTRY_TYPE
import co.anbora.labs.kse.fileTypes.settings.Settings.EXPIRY_STATUS
import co.anbora.labs.kse.fileTypes.settings.Settings.ICON_SIZE
import co.anbora.labs.kse.fileTypes.settings.Settings.INIT_COLUMN
import co.anbora.labs.kse.fileTypes.settings.Settings.LOCK_STATUS

data class SettingOptionSet(
    var type: Option = Option(
        "KeyStoreTableHeadRend.NameColumn.tooltip",
        "KeyStoreTableModel.NameColumn",
        TypeClass.STRING,
        optionType = OptionType.CERT_TYPE,
        colWidth = ICON_SIZE,
        active = true,
        index = ENTRY_TYPE
    ),
    var lockStatus: Option = Option(
        "KeyStoreTableHeadRend.NameColumn.tooltip",
        "KeyStoreTableModel.NameColumn",
        TypeClass.BOOLEAN,
        optionType = OptionType.LOCK_STATUS,
        colWidth = ICON_SIZE,
        active = true,
        index = LOCK_STATUS
    ),
    var certStatus: Option = Option(
        "KeyStoreTableHeadRend.NameColumn.tooltip",
        "KeyStoreTableModel.NameColumn",
        TypeClass.INTEGER,
        optionType = OptionType.CERT_STATUS,
        colWidth = ICON_SIZE,
        active = true,
        index = EXPIRY_STATUS
    ),
    var entryName: Option = Option(
        "KeyStoreTableHeadRend.NameColumn.tooltip",
        "KeyStoreTableModel.NameColumn",
        TypeClass.STRING,
        optionType = OptionType.ENTRY_NAME,
        active = true
    ),
    var keySize: Option = Option(
        "KeyStoreTableHeadRend.KeySizeColumn.tooltip",
        "KeyStoreTableModel.KeySizeColumn",
        TypeClass.INTEGER,
        optionType = OptionType.KEY_SIZE,
        active = true
    ),
    var certificateExpiry: Option = Option(
        "KeyStoreTableHeadRend.CertExpiryColumn.tooltip",
        "KeyStoreTableModel.CertExpiryColumn",
        TypeClass.DATE,
        optionType = OptionType.CERTIFICATE_EXPIRY,
        colWidth = " 20.00.2000 00:00:00 MESZ ".length,
        active = true
    ),
    var subjectKeyIdentifier: Option = Option(
        "KeyStoreTableHeadRend.SKIColumn.tooltip",
        "KeyStoreTableModel.SKIColumn",
        TypeClass.STRING,
        optionType = OptionType.SKI,
    ),
    var issuerDistinguishedName: Option = Option(
        "KeyStoreTableHeadRend.IssuerDNColumn.tooltip",
        "KeyStoreTableModel.IssuerDNColumn",
        TypeClass.STRING,
        optionType = OptionType.ISSUER_DN,
    ),
    var issuerCommonName: Option = Option(
        "KeyStoreTableHeadRend.IssuerCNColumn.tooltip",
        "KeyStoreTableModel.IssuerCNColumn",
        TypeClass.STRING,
        optionType = OptionType.ISSUER_CN,
    ),
    var issuerOrganizationName: Option = Option(
        "KeyStoreTableHeadRend.IssuerOColumn.tooltip",
        "KeyStoreTableModel.IssuerOColumn",
        TypeClass.STRING,
        optionType = OptionType.ISSUER_O,
    ),
    var algorithm: Option = Option(
        "KeyStoreTableHeadRend.AlgorithmColumn.tooltip",
        "KeyStoreTableModel.AlgorithmColumn",
        TypeClass.STRING,
        optionType = OptionType.ALGORITHM,
        active = true
    ),
    var curve: Option = Option(
        "KeyStoreTableHeadRend.CurveColumn.tooltip",
        "KeyStoreTableModel.CurveColumn",
        TypeClass.STRING,
        optionType = OptionType.CURVE,
    ),
    var lastModified: Option = Option(
        "KeyStoreTableHeadRend.LastModifiedColumn.tooltip",
        "KeyStoreTableModel.LastModifiedColumn",
        TypeClass.DATE,
        optionType = OptionType.LAST_MODIFIED,
        colWidth = " 20.00.2000 00:00:00 MESZ ".length,
        active = true
    ),
    var authorityKeyIdentifier: Option = Option(
        "KeyStoreTableHeadRend.AKIColumn.tooltip",
        "KeyStoreTableModel.AKIColumn",
        TypeClass.STRING,
        optionType = OptionType.AKI,
    ),
    var subjectDistinguishedName: Option = Option(
        "KeyStoreTableHeadRend.SubjectDNColumn.tooltip",
        "KeyStoreTableModel.SubjectDNColumn",
        TypeClass.STRING,
        optionType = OptionType.SUBJECT_DN,
    ),
    var subjectCommonName: Option = Option(
        "KeyStoreTableHeadRend.SubjectCNColumn.tooltip",
        "KeyStoreTableModel.SubjectCNColumn",
        TypeClass.STRING,
        optionType = OptionType.SUBJECT_CN,
    ),
    var subjectOrganizationName: Option = Option(
        "KeyStoreTableHeadRend.SubjectOColumn.tooltip",
        "KeyStoreTableModel.SubjectOColumn",
        TypeClass.STRING,
        optionType = OptionType.SUBJECT_O,
    )
) {

    private val defaultOptions = listOf(type, lockStatus, certStatus)

    private val options: List<Option> by lazy {
        listOf(
            entryName, algorithm, keySize, curve, certificateExpiry,
            lastModified, authorityKeyIdentifier,
            subjectKeyIdentifier,
            issuerDistinguishedName, subjectDistinguishedName,
            issuerCommonName, subjectCommonName,
            issuerOrganizationName, subjectOrganizationName
        )
    }

    fun getColumnSettings(): List<Option> = defaultOptions + options.filter { it.active }

    fun setIndexColumns(order: () -> Int) {
        options.forEach {
            if (it.active) {
                it.index = order()
            }
        }
    }

    fun merge(configuredOptions: SettingOptionSet) {
        val mapOptions = (defaultOptions + options).associateBy { it.optionType }.toMutableMap()
        configuredOptions.getColumnSettings().forEach {
            mapOptions.computeIfPresent(it.optionType) { _, oldValue ->
                oldValue.active = it.active
                oldValue
            }
        }
    }

    fun getActiveOptions(): Int {
        return (defaultOptions + options).count { it.active }
    }

    fun resetOptions() {
        options.forEach {
            it.index = INIT_COLUMN
        }
    }
}
