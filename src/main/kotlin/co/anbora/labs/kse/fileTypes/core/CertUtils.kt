package co.anbora.labs.kse.fileTypes.core

import org.apache.commons.io.FileUtils
import org.kse.crypto.Password
import org.kse.crypto.filetype.CryptoFileType
import org.kse.crypto.filetype.CryptoFileUtil
import org.kse.crypto.privatekey.MsPvkUtil
import org.kse.crypto.privatekey.OpenSslPvkUtil
import org.kse.crypto.privatekey.Pkcs8Util
import java.io.File
import java.nio.charset.StandardCharsets
import java.security.PrivateKey
import java.util.*

object CertUtils {

    fun decodeIfBase64(data: ByteArray): ByteArray {
        try {
            return Base64.getDecoder().decode(String(data, StandardCharsets.US_ASCII).trim { it <= ' ' })
        } catch (e: IllegalArgumentException) {
            // was not valid b64
        }
        return data
    }

    fun load(file: File, password: Password): PrivateKey? {

        val data: ByteArray = decodeIfBase64(FileUtils.readFileToByteArray(file))

        return when (CryptoFileUtil.detectFileType(file)) {
            CryptoFileType.ENC_PKCS8_PVK -> Pkcs8Util.loadEncrypted(data, password)
            CryptoFileType.ENC_OPENSSL_PVK -> OpenSslPvkUtil.loadEncrypted(data, password)
            CryptoFileType.ENC_MS_PVK -> MsPvkUtil.loadEncrypted(data, password)
            else -> null
        }
    }
}
