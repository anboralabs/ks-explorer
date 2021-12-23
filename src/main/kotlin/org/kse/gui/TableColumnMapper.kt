package org.kse.gui

import co.anbora.labs.kse.ide.settings.Option
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
        return when (option) {
            is Option.CertType -> mapToCertType(option, title, width)
            is Option.LockStatus -> mapToLockStatus(option, title, width)
            is Option.CertStatus -> mapToCertStatus(option, title, width)
            is Option.EntryName -> mapToEntryName(option, title, width)
            is Option.KeySize -> mapToKeySize(option, title, width)
            is Option.CertificateExpiry -> mapToCertificateExpiry(option, title, width)
            is Option.SubjectKeyIdentifier -> mapToSubjectKeyIdentifier(option, title, width)
            is Option.IssuerDistinguishedName -> mapToIssuerDistinguishedName(option, title, width)
            is Option.IssuerCommonName -> mapToIssuerCommonName(option, title, width)
            is Option.IssuerOrganizationName -> mapToIssuerOrganizationName(option, title, width)
            is Option.Algorithm -> mapToAlgorithm(option, title, width)
            is Option.Curve -> mapToCurve(option, title, width)
            is Option.LastModified -> mapToLastModified(option, title, width)
            is Option.AuthorityKeyIdentifier -> mapToAuthorityKeyIdentifier(option, title, width)
            is Option.SubjectDistinguishedName -> mapToSubjectDistinguishedName(option, title, width)
            is Option.SubjectCommonName -> mapToSubjectCommonName(option, title, width)
            is Option.SubjectOrganizationName -> mapToSubjectOrganizationName(option, title, width)
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