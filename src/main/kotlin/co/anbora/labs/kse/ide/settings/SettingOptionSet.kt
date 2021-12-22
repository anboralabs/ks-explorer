package co.anbora.labs.kse.ide.settings

import co.anbora.labs.kse.lang.settings.Settings.ENTRY_TYPE
import co.anbora.labs.kse.lang.settings.Settings.EXPIRY_STATUS
import co.anbora.labs.kse.lang.settings.Settings.ICON_SIZE
import co.anbora.labs.kse.lang.settings.Settings.INIT_COLUMN
import co.anbora.labs.kse.lang.settings.Settings.LOCK_STATUS
import java.util.*

data class SettingOptionSet(
    var type: Option = Option(
        "KeyStoreTableHeadRend.NameColumn.tooltip",
        "KeyStoreTableModel.NameColumn",
        TypeClass.STRING,
        colWidth = ICON_SIZE,
        active = true,
        index = ENTRY_TYPE
    ),
    var lockStatus: Option = Option(
        "KeyStoreTableHeadRend.NameColumn.tooltip",
        "KeyStoreTableModel.NameColumn",
        TypeClass.BOOLEAN,
        colWidth = ICON_SIZE,
        active = true,
        index = LOCK_STATUS
    ),
    var certStatus: Option = Option(
        "KeyStoreTableHeadRend.NameColumn.tooltip",
        "KeyStoreTableModel.NameColumn",
        TypeClass.INTEGER,
        colWidth = ICON_SIZE,
        active = true,
        index = EXPIRY_STATUS
    ),
    var entryName: Option = Option(
        "KeyStoreTableHeadRend.NameColumn.tooltip",
        "KeyStoreTableModel.NameColumn",
        TypeClass.STRING,
        active = true
    ),
    var keySize: Option = Option(
        "KeyStoreTableHeadRend.KeySizeColumn.tooltip",
        "KeyStoreTableModel.KeySizeColumn",
        TypeClass.INTEGER,
        active = true
    ),
    var certificateExpiry: Option = Option(
        "KeyStoreTableHeadRend.CertExpiryColumn.tooltip",
        "KeyStoreTableModel.CertExpiryColumn",
        TypeClass.DATE,
        colWidth = " 20.00.2000 00:00:00 MESZ ".length,
        active = true
    ),
    var subjectKeyIdentifier: Option = Option(
        "KeyStoreTableHeadRend.SKIColumn.tooltip",
        "KeyStoreTableModel.SKIColumn",
        TypeClass.STRING
    ),
    var issuerDistinguishedName: Option = Option(
        "KeyStoreTableHeadRend.IssuerDNColumn.tooltip",
        "KeyStoreTableModel.IssuerDNColumn",
        TypeClass.STRING
    ),
    var issuerCommonName: Option = Option(
        "KeyStoreTableHeadRend.IssuerCNColumn.tooltip",
        "KeyStoreTableModel.IssuerCNColumn",
        TypeClass.STRING
    ),
    var issuerOrganizationName: Option = Option(
        "KeyStoreTableHeadRend.IssuerOColumn.tooltip",
        "KeyStoreTableModel.IssuerOColumn",
        TypeClass.STRING
    ),
    var algorithm: Option = Option(
        "KeyStoreTableHeadRend.AlgorithmColumn.tooltip",
        "KeyStoreTableModel.AlgorithmColumn",
        TypeClass.STRING,
        active = true
    ),
    var curve: Option = Option(
        "KeyStoreTableHeadRend.CurveColumn.tooltip",
        "KeyStoreTableModel.CurveColumn",
        TypeClass.STRING
    ),
    var lastModified: Option = Option(
        "KeyStoreTableHeadRend.LastModifiedColumn.tooltip",
        "KeyStoreTableModel.LastModifiedColumn",
        TypeClass.DATE,
        colWidth = " 20.00.2000 00:00:00 MESZ ".length,
        active = true
    ),
    var authorityKeyIdentifier: Option = Option(
        "KeyStoreTableHeadRend.AKIColumn.tooltip",
        "KeyStoreTableModel.AKIColumn",
        TypeClass.STRING
    ),
    var subjectDistinguishedName: Option = Option(
        "KeyStoreTableHeadRend.SubjectDNColumn.tooltip",
        "KeyStoreTableModel.SubjectDNColumn",
        TypeClass.STRING
    ),
    var subjectCommonName: Option = Option(
        "KeyStoreTableHeadRend.SubjectCNColumn.tooltip",
        "KeyStoreTableModel.SubjectCNColumn",
        TypeClass.STRING
    ),
    var subjectOrganizationName: Option = Option(
        "KeyStoreTableHeadRend.SubjectOColumn.tooltip",
        "KeyStoreTableModel.SubjectOColumn",
        TypeClass.STRING
    )
) {

    private val defaultOptions = listOf(type, lockStatus, certStatus)

    private val options = listOf(
        entryName, algorithm, keySize, curve, certificateExpiry,
        lastModified, authorityKeyIdentifier,
        subjectKeyIdentifier,
        issuerDistinguishedName, subjectDistinguishedName,
        issuerCommonName, subjectCommonName,
        issuerOrganizationName, subjectOrganizationName
    )

    fun getColumnSettings(): List<Option> = defaultOptions + options.filter { it.active }

    fun setIndexColumns(order: () -> Int) {
        options.forEach {
            if (it.active) {
                it.index = order()
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
