package org.kse.gui

import java.util.*

object ColumnValues {

    private val res = ResourceBundle.getBundle("org/kse/gui/resources")
    /** Type column value for a key pair entry  */
    val KEY_PAIR_ENTRY: String = res.getString("KeyStoreTableModel.KeyPairEntry")

    /** Type column value for a trusted certificate entry  */
    val TRUST_CERT_ENTRY: String = res.getString("KeyStoreTableModel.TrustCertEntry")

    /** Type column value for a key entry  */
    val KEY_ENTRY: String = res.getString("KeyStoreTableModel.KeyEntry")

}