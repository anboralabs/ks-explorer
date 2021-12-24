package org.kse.gui

import co.anbora.labs.kse.ide.settings.Option
import co.anbora.labs.kse.ide.settings.OptionType
import org.kse.gui.column.*
import java.util.*

object TableColumnMapper {

    private val res = ResourceBundle.getBundle("org/kse/gui/resources")

    fun mapOptionToColumn(option: Option): TableColumn {
        val title = res.getString(option.keyTitle)
        val width = when (option.colWidth < 1) {
            true -> title.length
            else -> option.colWidth
        }
        return when (option.optionType) {
            OptionType.CERT_TYPE -> mapToCertType(option, title, width)
            OptionType.LOCK_STATUS -> mapToLockStatus(option, title, width)
            OptionType.CERT_STATUS -> mapToCertStatus(option, title, width)
            OptionType.ENTRY_NAME -> mapToEntryName(option, title, width)
            OptionType.KEY_SIZE -> mapToKeySize(option, title, width)
            OptionType.CERTIFICATE_EXPIRY -> mapToCertificateExpiry(option, title, width)
            OptionType.SKI -> mapToSubjectKeyIdentifier(option, title, width)
            OptionType.ISSUER_DN -> mapToIssuerDistinguishedName(option, title, width)
            OptionType.ISSUER_CN -> mapToIssuerCommonName(option, title, width)
            OptionType.ISSUER_O -> mapToIssuerOrganizationName(option, title, width)
            OptionType.ALGORITHM -> mapToAlgorithm(option, title, width)
            OptionType.CURVE -> mapToCurve(option, title, width)
            OptionType.LAST_MODIFIED -> mapToLastModified(option, title, width)
            OptionType.AKI -> mapToAuthorityKeyIdentifier(option, title, width)
            OptionType.SUBJECT_DN -> mapToSubjectDistinguishedName(option, title, width)
            OptionType.SUBJECT_CN -> mapToSubjectCommonName(option, title, width)
            OptionType.SUBJECT_O -> mapToSubjectOrganizationName(option, title, width)
            else -> throw IllegalArgumentException()
        }
    }

    private fun mapToCertType(
        option: Option,
        title: String,
        width: Int
    ): CertTypeColumn =
        CertTypeColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = width
        )

    private fun mapToLockStatus(
        option: Option,
        title: String,
        width: Int
    ): LockStatusColumn =
        LockStatusColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = width
        )

    private fun mapToCertStatus(
        option: Option,
        title: String,
        width: Int
    ): CertStatusColumn =
        CertStatusColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = width
        )

    private fun mapToEntryName(
        option: Option,
        title: String,
        width: Int
    ): EntryNameColumn =
        EntryNameColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = width
        )

    private fun mapToKeySize(
        option: Option,
        title: String,
        width: Int
    ): KeySizeColumn =
        KeySizeColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = width
        )

    private fun mapToCertificateExpiry(
        option: Option,
        title: String,
        width: Int
    ): CertificateExpiryColumn =
        CertificateExpiryColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = width
        )

    private fun mapToSubjectKeyIdentifier(
        option: Option,
        title: String,
        width: Int
    ): SubjectKeyIdentifierColumn =
        SubjectKeyIdentifierColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = width
        )

    private fun mapToIssuerDistinguishedName(
        option: Option,
        title: String,
        width: Int
    ): IssuerDistinguishedNameColumn =
        IssuerDistinguishedNameColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = width
        )

    private fun mapToIssuerCommonName(
        option: Option,
        title: String,
        width: Int
    ): IssuerCommonNameColumn =
        IssuerCommonNameColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = width
        )

    private fun mapToIssuerOrganizationName(
        option: Option,
        title: String,
        width: Int
    ): IssuerOrganizationNameColumn =
        IssuerOrganizationNameColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = width
        )

    private fun mapToAlgorithm(
        option: Option,
        title: String,
        width: Int
    ): AlgorithmColumn =
        AlgorithmColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = width
        )

    private fun mapToCurve(
        option: Option,
        title: String,
        width: Int
    ): CurveColumn =
        CurveColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = width
        )

    private fun mapToLastModified(
        option: Option,
        title: String,
        width: Int
    ): LastModifiedColumn =
        LastModifiedColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = width
        )

    private fun mapToAuthorityKeyIdentifier(
        option: Option,
        title: String,
        width: Int
    ): AuthorityKeyIdentifierColumn =
        AuthorityKeyIdentifierColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = width
        )

    private fun mapToSubjectDistinguishedName(
        option: Option,
        title: String,
        width: Int
    ): SubjectDistinguishedNameColumn =
        SubjectDistinguishedNameColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = width
        )

    private fun mapToSubjectCommonName(
        option: Option,
        title: String,
        width: Int
    ): SubjectCommonNameColumn =
        SubjectCommonNameColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = width
        )

    private fun mapToSubjectOrganizationName(
        option: Option,
        title: String,
        width: Int
    ): SubjectOrganizationNameColumn =
        SubjectOrganizationNameColumn(
            tooltip = res.getString(option.keyToolTip),
            title = title,
            type = option.typeClass?.type ?: String::class.java,
            index = option.index,
            width = width
        )
}