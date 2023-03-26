package org.kse.gui

import co.anbora.labs.kse.fileTypes.settings.Settings.ENTRY_TYPE
import co.anbora.labs.kse.fileTypes.settings.Settings.EXPIRY_STATUS
import co.anbora.labs.kse.fileTypes.settings.Settings.LOCK_STATUS
import org.bouncycastle.util.encoders.Hex
import org.kse.gui.ColumnValues.KEY_PAIR_ENTRY
import org.kse.gui.ColumnValues.TRUST_CERT_ENTRY
import org.kse.utilities.StringUtils
import java.awt.Component
import java.util.*
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JTable
import javax.swing.table.DefaultTableCellRenderer

class KeyStoreTableCellRend: DefaultTableCellRenderer() {

    private val res = ResourceBundle.getBundle("org/kse/gui/resources")

    override fun getTableCellRendererComponent(
        table: JTable?,
        value: Any?,
        isSelected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ): Component {

        val cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column) as JLabel

        return when (column) {
            ENTRY_TYPE -> renderEntryType(cell, value)
            LOCK_STATUS -> renderLockStatus(cell, value)
            EXPIRY_STATUS -> renderExpiryStatus(cell, value)
            else -> writeCell(cell, value)
        }
    }

    private fun renderEntryType(cell: JLabel, value: Any?): JLabel {
        lateinit var icon: ImageIcon

        if (KEY_PAIR_ENTRY == value) {
            icon = ImageIcon(
                javaClass.getResource(
                    "images/table/keypair_entry.png"
                )
            )
            cell.toolTipText = res.getString("KeyStoreTableCellRend.KeyPairEntry.tooltip")
        } else if (TRUST_CERT_ENTRY == value) {
            icon = ImageIcon(
                javaClass.getResource(
                    "images/table/trustcert_entry.png"
                )
            )
            cell.toolTipText = res.getString("KeyStoreTableCellRend.TrustCertEntry.tooltip")
        } else {
            icon = ImageIcon(javaClass.getResource("images/table/key_entry.png"))
            cell.toolTipText = res.getString("KeyStoreTableCellRend.KeyEntry.tooltip")
        }

        cell.icon = icon
        cell.text = ""
        cell.verticalAlignment = CENTER
        cell.horizontalAlignment = CENTER

        return cell
    }

    private fun renderLockStatus(cell: JLabel, value: Any?): JLabel {
        if (value == null) {
            // No lock status available (not a key pair entry or PKCS #12 KeyStore)
            cell.icon = null
            cell.text = "-"
            cell.toolTipText = res.getString("KeyStoreTableCellRend.NoLockStatus.tooltip")
            cell.horizontalAlignment = CENTER
        } else {
            lateinit var icon: ImageIcon
            if (value == java.lang.Boolean.TRUE) {
                // Locked
                icon = ImageIcon(
                    javaClass.getResource(
                        "images/table/locked_entry.png"
                    )
                )
                cell.toolTipText = res.getString("KeyStoreTableCellRend.LockedEntry.tooltip")
            } else {
                // Unlocked
                icon = ImageIcon(
                    javaClass.getResource(
                        "images/table/unlocked_entry.png"
                    )
                )
                cell.toolTipText = res.getString("KeyStoreTableCellRend.UnlockedEntry.tooltip")
            }
            cell.icon = icon
            cell.text = ""
            cell.verticalAlignment = CENTER
            cell.horizontalAlignment = CENTER
        }

        return cell
    }

    private fun renderExpiryStatus(cell: JLabel, value: Any?): JLabel {
        if (value == null) {
            // No cert expired status available (must be a key entry)
            cell.icon = null
            cell.text = "-"
            cell.toolTipText = res.getString("KeyStoreTableCellRend.NoCertExpiry.tooltip")
            cell.horizontalAlignment = CENTER
        } else {
            var icon: ImageIcon?
            if (value == 2) {
                // Expired
                icon = ImageIcon(
                    javaClass.getResource(
                        "images/table/cert_expired_entry.png"
                    )
                )
                cell.toolTipText = res.getString("KeyStoreTableCellRend.CertExpiredEntry.tooltip")
            } else {
                if (value == 1) {
                    // Almost Expired
                    icon = ImageIcon(
                        javaClass
                            .getResource("images/table/cert_old_entry.png")
                    )
                    cell.toolTipText = res.getString("KeyStoreTableCellRend.CertAlmostExpiredEntry.tooltip")
                } else {
                    // Unexpired
                    icon = ImageIcon(
                        javaClass.getResource(
                            "images/table/cert_unexpired_entry.png"
                        )
                    )
                    cell.toolTipText = res.getString("KeyStoreTableCellRend.CertUnexpiredEntry.tooltip")
                }
            }
            cell.icon = icon
            cell.text = ""
            cell.verticalAlignment = CENTER
            cell.horizontalAlignment = CENTER
        }

        return cell
    }

    private fun writeCell(cell: JLabel, value: Any?): JLabel {
        try {
            if (value == null) {
                cell.text = "-"
                cell.toolTipText = res.getString("KeyStoreTableCellRend.Unavailable.tooltip")
                cell.horizontalAlignment = CENTER
            } else {
                if (value is String) {
                    cell.text = value
                    cell.toolTipText = text
                    cell.horizontalAlignment = LEFT
                } else {
                    if (value is Int) {
                        if (value in 0..9999) cell.text = value.toString() else cell.text =
                            "X" + String.format("%x", value)
                        cell.toolTipText = text
                        cell.horizontalAlignment = LEFT
                    } else {
                        if (value is Date) {
                            cell.text = StringUtils.formatDate(value as Date?)
                            cell.toolTipText = text
                            cell.horizontalAlignment = LEFT
                        } else {
                            if (value is ByteArray) {
                                cell.text = Hex.toHexString(value as ByteArray?)
                                cell.toolTipText = text
                                cell.horizontalAlignment = LEFT
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            cell.text = "?"
            cell.toolTipText = res.getString("KeyStoreTableCellRend.Format.tooltip")
            cell.horizontalAlignment = CENTER
        }
        return cell
    }
}
