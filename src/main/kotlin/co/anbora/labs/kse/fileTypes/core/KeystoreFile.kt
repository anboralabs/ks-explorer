package co.anbora.labs.kse.fileTypes.core

import com.intellij.diff.editor.DiffVirtualFile
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.SingleRootFileViewProvider
import org.kse.crypto.filetype.CryptoFileType
import org.kse.crypto.filetype.CryptoFileType.JCEKS_KS
import org.kse.crypto.filetype.CryptoFileType.JKS_KS
import org.kse.crypto.filetype.CryptoFileType.PKCS12_KS
import org.kse.crypto.filetype.CryptoFileType.BKS_KS
import org.kse.crypto.filetype.CryptoFileType.BKS_V1_KS
import org.kse.crypto.filetype.CryptoFileType.BCFKS_KS
import org.kse.crypto.filetype.CryptoFileType.UBER_KS
import org.kse.crypto.filetype.CryptoFileUtil

object KeystoreFile {

    private val fileTypes: Set<CryptoFileType> = setOf(
        JCEKS_KS, JKS_KS, PKCS12_KS, BKS_KS, BKS_V1_KS, BCFKS_KS, UBER_KS
    )

    fun acceptKSFile(file: VirtualFile): Boolean {
        return isKSFile(
            file
        ) && !SingleRootFileViewProvider.isTooLargeForContentLoading(file) && file !is DiffVirtualFile
    }

    private fun isKSFile(file: VirtualFile?): Boolean {
        return file != null && isKeystoreFileType(file)
    }

    private fun isKeystoreFileType(file: VirtualFile): Boolean = try {
        val fileType = CryptoFileUtil.detectFileType(file.toNioPath().toFile())
        fileType in fileTypes
    } catch (ex: Exception) {
        false
    }

}