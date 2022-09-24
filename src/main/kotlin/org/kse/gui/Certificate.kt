package org.kse.gui

import co.anbora.labs.kse.fileTypes.settings.Settings.EXPIRY_WAR_N_DAYS
import org.bouncycastle.asn1.DEROctetString
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier
import org.bouncycastle.asn1.x509.Extension
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.kse.crypto.CryptoException
import org.kse.crypto.KeyInfo
import org.kse.crypto.KeyType
import org.kse.crypto.keypair.KeyPairUtil
import org.kse.crypto.keystore.KeyStoreUtil
import org.kse.crypto.secretkey.SecretKeyType
import org.kse.crypto.secretkey.SecretKeyUtil
import org.kse.crypto.x509.KseX500NameStyle
import org.kse.crypto.x509.X500NameUtils
import org.kse.crypto.x509.X509CertUtil
import org.kse.utilities.history.KeyStoreHistory
import org.kse.utilities.history.KeyStoreState
import org.kse.utilities.io.HexUtil
import java.security.*
import java.security.cert.X509Certificate
import java.util.*
import javax.crypto.SecretKey

object Certificate {

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    @Throws(KeyStoreException::class, CryptoException::class)
    private fun getCertificate(alias: String, keyStore: KeyStore): X509Certificate? {
        var x509Cert: X509Certificate? = if (KeyStoreUtil.isTrustedCertificateEntry(alias, keyStore)) {
            X509CertUtil.convertCertificate(keyStore.getCertificate(alias))
        } else {
            val chain = keyStore.getCertificateChain(alias) ?: return null
            // Key pair - first certificate in chain will be for the private key
            val x509Chain = X509CertUtil.orderX509CertChain(X509CertUtil.convertCertificates(chain))
            x509Chain[0]
        }
        return x509Cert
    }

    @Throws(CryptoException::class, KeyStoreException::class)
    fun getCertificateSubjectDN(alias: String, keyStore: KeyStore): String? {
        val x509Cert: X509Certificate? = getCertificate(alias, keyStore)
        return X500NameUtils.x500PrincipalToX500Name(x509Cert?.subjectX500Principal)?.toString()
    }

    @Throws(CryptoException::class, KeyStoreException::class)
    fun getCertificateSubjectCN(alias: String, keyStore: KeyStore): String? {
        val x509Cert = getCertificate(alias, keyStore)
        return X500NameUtils.extractCN(x509Cert?.subjectX500Principal)
    }

    @Throws(CryptoException::class, KeyStoreException::class)
    fun getCertificateSubjectO(alias: String, keyStore: KeyStore): String? {
        val x509Cert = getCertificate(alias, keyStore)
        val subject = X500NameUtils.x500PrincipalToX500Name(x509Cert?.subjectX500Principal)
        return X500NameUtils.getRdn(subject, KseX500NameStyle.O)
    }

    @Throws(CryptoException::class, KeyStoreException::class)
    fun getCertificateIssuerDN(alias: String, keyStore: KeyStore): String? {
        val x509Cert = getCertificate(alias, keyStore)
        return X500NameUtils.x500PrincipalToX500Name(x509Cert?.issuerX500Principal)?.toString()
    }

    @Throws(CryptoException::class, KeyStoreException::class)
    fun getCertificateIssuerCN(alias: String, keyStore: KeyStore): String? {
        val x509Cert = getCertificate(alias, keyStore)
        return X500NameUtils.extractCN(x509Cert?.issuerX500Principal)
    }

    @Throws(CryptoException::class, KeyStoreException::class)
    fun getCertificateIssuerO(alias: String, keyStore: KeyStore): String? {
        val x509Cert = getCertificate(alias, keyStore)
        val issuer = X500NameUtils.x500PrincipalToX500Name(x509Cert?.issuerX500Principal)
        return X500NameUtils.getRdn(issuer, KseX500NameStyle.O)
    }

    @Throws(CryptoException::class, KeyStoreException::class)
    fun getCertificateAKI(alias: String, keyStore: KeyStore): String? {
        val x509Cert = getCertificate(alias, keyStore)
        return try {
            val akiValue = x509Cert!!.getExtensionValue(Extension.authorityKeyIdentifier.id)
            val octets = DEROctetString.getInstance(akiValue).octets
            val akiBytes = AuthorityKeyIdentifier.getInstance(octets).keyIdentifier
            HexUtil.getHexString(akiBytes)
        } catch (e: Exception) {
            "-"
        }
    }

    @Throws(CryptoException::class, KeyStoreException::class)
    fun getCertificateSKI(alias: String, keyStore: KeyStore): String? {
        val x509Cert = getCertificate(alias, keyStore)
        return try {
            val skiValue = x509Cert!!.getExtensionValue(Extension.subjectKeyIdentifier.id)
            val octets = DEROctetString.getInstance(skiValue).octets
            val skiBytes = SubjectKeyIdentifier.getInstance(octets).keyIdentifier
            HexUtil.getHexString(skiBytes)
        } catch (e: java.lang.Exception) {
            "-"
        }
    }

    fun getEntryType(history: KeyStoreHistory, alias: String): String {
        val currentState = history.currentState
        val keyStore = currentState.keyStore

        return if (KeyStoreUtil.isTrustedCertificateEntry(alias, keyStore)) {
            ColumnValues.TRUST_CERT_ENTRY
        } else if (KeyStoreUtil.isKeyPairEntry(alias, keyStore)) {
            ColumnValues.KEY_PAIR_ENTRY
        } else {
            ColumnValues.KEY_ENTRY
        }
    }

    fun getAlgorithmName(keyInfo: KeyInfo): String? {
        var algorithm = keyInfo.algorithm
        if (keyInfo.keyType == KeyType.SYMMETRIC) {
            // Try and get friendly algorithm name for secret key
            val secretKeyType = SecretKeyType.resolveJce(algorithm)
            if (secretKeyType != null) {
                algorithm = secretKeyType.friendly()
            }
        }
        return algorithm
    }

    @Throws(CryptoException::class, KeyStoreException::class)
    fun getCertificateExpiry(alias: String, keyStore: KeyStore): Date? {
        return if (KeyStoreUtil.isTrustedCertificateEntry(alias, keyStore)) {
            X509CertUtil.convertCertificate(keyStore.getCertificate(alias)).notAfter
        } else {
            val chain = keyStore.getCertificateChain(alias)
                ?: // Key entry - no expiry date
                return null

            // Key pair - first certificate in chain will be for the private key
            val x509Chain = X509CertUtil.orderX509CertChain(X509CertUtil.convertCertificates(chain))
            if (EXPIRY_WAR_N_DAYS < 1) {
                x509Chain[0].notAfter
            } else {
                val cal = Calendar.getInstance()
                cal[9999, 1] = 1
                var earliest = cal.time
                for (i in x509Chain.indices) {
                    if (x509Chain[i].notAfter.before(earliest)) {
                        earliest = x509Chain[i].notAfter
                    }
                }
                earliest
            }
        }
    }

    @Throws(CryptoException::class, GeneralSecurityException::class)
    fun getKeyInfo(alias: String, keyStore: KeyStore, currentState: KeyStoreState): KeyInfo? {
        if (KeyStoreUtil.isTrustedCertificateEntry(alias, keyStore)) {
            // Get key info from certificate
            val cert = X509CertUtil.convertCertificate(keyStore.getCertificate(alias))
            return KeyPairUtil.getKeyInfo(cert.publicKey)
        } else {
            val chain = keyStore.getCertificateChain(alias)
            if (chain != null) {
                // Key pair - first certificate in chain will be for the private key
                val x509Chain = X509CertUtil.orderX509CertChain(X509CertUtil.convertCertificates(chain))
                return KeyPairUtil.getKeyInfo(x509Chain[0].publicKey)
            } else {
                // Key entry - get key info if entry is unlocked
                if (currentState.getEntryPassword(alias) != null) {
                    var keyPassword: CharArray? = currentState.getEntryPassword(alias).toCharArray()
                    val key = keyStore.getKey(alias, keyPassword)
                    when (key) {
                        is SecretKey -> {
                            return SecretKeyUtil.getKeyInfo(key)
                        }
                        is PrivateKey -> {
                            return KeyPairUtil.getKeyInfo(key)
                        }
                        is PublicKey -> {
                            return KeyPairUtil.getKeyInfo(key)
                        }
                    }
                }
            }
        }
        return null
    }

}
