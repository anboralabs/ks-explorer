package co.anbora.labs.kse.fileTypes.core

import com.intellij.diff.editor.DiffVirtualFile
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.SingleRootFileViewProvider
import org.kse.crypto.filetype.CryptoFileType
import org.kse.crypto.filetype.CryptoFileUtil
import org.kse.crypto.signing.JarParser
import java.security.cert.X509Certificate
import java.util.function.Function


object CertFile {

    private val jarFileTypes: Set<CryptoFileType> = setOf(
        CryptoFileType.JAR
    )

    private fun isJarFileType(file: VirtualFile): Boolean = try {
        val fileType = CryptoFileUtil.detectFileType(file.toNioPath().toFile())
        fileType in jarFileTypes
    } catch (ex: Exception) {
        false
    }

    fun jarCertificates(file: VirtualFile): Array<X509Certificate> {
        val jarParser = JarParser(file.toNioPath().toFile())
        return jarParser.signerCerificates
    }

    fun acceptJarCertFile(file: VirtualFile, function: Function<VirtualFile, Array<X509Certificate>>): Boolean {
        return isJarFileType(file)
                && !SingleRootFileViewProvider.isTooLargeForContentLoading(file)
                && file !is DiffVirtualFile
                && function.apply(file).isNotEmpty()
    }

    fun acceptCertFile(file: VirtualFile, function: Function<VirtualFile, Array<X509Certificate>>): Boolean {
        return isCertFileType(file)
                && !SingleRootFileViewProvider.isTooLargeForContentLoading(file)
                && file !is DiffVirtualFile
                && function.apply(file).isNotEmpty()
    }

    private val certFileTypes: Set<CryptoFileType> = setOf(
        CryptoFileType.CERT
    )

    private fun isCertFileType(file: VirtualFile): Boolean = try {
        val fileType = CryptoFileUtil.detectFileType(file.toNioPath().toFile())
        fileType in certFileTypes
    } catch (ex: Exception) {
        false
    }

}