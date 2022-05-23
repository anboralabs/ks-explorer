package org.kse.gui

import co.anbora.labs.kse.ide.settings.KSEditorSettings
import org.kse.gui.TableColumnMapper.mapOptionToColumn
import org.kse.gui.column.TableColumn

class KeyStoreTableColumns(
    private val editor: KSEditorSettings = KSEditorSettings.getInstance()
) {

    init {
        sortCol()
    }

    private fun sortCol() {
        var col = 2
        editor.resetColumns()
        editor.sortColumns {
            ++col
        }
    }

    fun getColumns(): Map<Int, TableColumn> = editor.activeColumns().associate { it.index to mapOptionToColumn(it) }

    fun getNofColumns(): Int = editor.nofColumns()

    fun colEntryName(): Int = editor.state.entryName.index

    fun colAlgorithm(): Int = editor.state.algorithm.index

    fun colKeySize(): Int = editor.state.keySize.index

    fun colCertificateExpiry(): Int = editor.state.certificateExpiry.index

    fun colLastModified(): Int = editor.state.lastModified.index

    fun colAKI(): Int = editor.state.authorityKeyIdentifier.index

    fun colSKI(): Int = editor.state.subjectKeyIdentifier.index

    fun colIssuerDN(): Int = editor.state.issuerDistinguishedName.index

    fun colSubjectDN(): Int = editor.state.subjectDistinguishedName.index

    fun colIssuerCN(): Int = editor.state.issuerDistinguishedName.index

    fun colSubjectCN(): Int = editor.state.subjectCommonName.index

    fun colIssuerO(): Int = editor.state.issuerOrganizationName.index

    fun colSubjectO(): Int = editor.state.subjectOrganizationName.index

    fun colCurve(): Int = editor.state.curve.index

}
