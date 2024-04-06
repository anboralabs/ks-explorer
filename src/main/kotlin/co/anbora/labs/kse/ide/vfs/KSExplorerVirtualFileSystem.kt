package co.anbora.labs.kse.ide.vfs

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileListener
import com.intellij.openapi.vfs.VirtualFileSystem

class KSExplorerVirtualFileSystem: VirtualFileSystem(), DumbAware {

    private val ERROR_INVALID_OPERATION: String = "Invalid operation"
    private val PROTOCOL: String = "ks-explorer"

    private val fileListeners: MutableList<VirtualFileListener> = ArrayList()

    override fun getProtocol(): String = PROTOCOL

    override fun findFileByPath(path: String): VirtualFile? = null

    override fun refresh(asynchronous: Boolean) = Unit

    override fun refreshAndFindFileByPath(path: String): VirtualFile? = null

    override fun addVirtualFileListener(listener: VirtualFileListener) {
        fileListeners.add(listener)
    }

    override fun removeVirtualFileListener(listener: VirtualFileListener) {
        fileListeners.remove(listener)
    }

    override fun deleteFile(requestor: Any?, vFile: VirtualFile) = Unit

    override fun moveFile(requestor: Any?, vFile: VirtualFile, newParent: VirtualFile) = Unit

    override fun renameFile(requestor: Any?, vFile: VirtualFile, newName: String) = Unit

    override fun createChildFile(requestor: Any?, vDir: VirtualFile, fileName: String): VirtualFile = throw RuntimeException(ERROR_INVALID_OPERATION)

    override fun createChildDirectory(requestor: Any?, vDir: VirtualFile, dirName: String): VirtualFile = throw RuntimeException(ERROR_INVALID_OPERATION)

    override fun copyFile(
        requestor: Any?,
        virtualFile: VirtualFile,
        newParent: VirtualFile,
        copyName: String
    ): VirtualFile = throw RuntimeException(ERROR_INVALID_OPERATION)

    override fun isReadOnly(): Boolean = true

    companion object {
        val INSTANCE get() = KSExplorerVirtualFileSystem()
    }
}
