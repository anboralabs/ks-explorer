package co.anbora.labs.kse.fileTypes.core

import java.nio.charset.StandardCharsets
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
}