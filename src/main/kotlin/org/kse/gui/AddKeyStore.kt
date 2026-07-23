package org.kse.gui

import org.kse.crypto.CryptoException
import org.kse.crypto.keystore.KseKeyStore
import org.kse.gui.passwordmanager.Password
import java.io.File
import java.security.GeneralSecurityException
import java.security.KeyStore

interface AddKeyStore {

    @Throws(GeneralSecurityException::class, CryptoException::class)
    fun addKeyStore(keyStore: KseKeyStore?, keyStoreFile: File?, password: Password?)

}