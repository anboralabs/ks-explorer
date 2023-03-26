package org.kse.gui

import org.kse.crypto.CryptoException
import java.security.GeneralSecurityException
import java.security.PrivateKey

interface AddPrivateKey {

    @Throws(GeneralSecurityException::class, CryptoException::class)
    fun addPrivateKey(privateKey: PrivateKey?)

}