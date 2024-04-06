package co.anbora.labs.kse.ide.vfs

import co.anbora.labs.kse.fileTypes.VirtualFileType
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileSystem
import java.io.InputStream
import java.io.OutputStream

abstract class KSVirtualFile(
    private val alias: String,
    private val parent: VirtualFile,
    private val data: ByteArray
): VirtualFile(), DumbAware {

    abstract fun getFileEditor(project: Project): FileEditor

    override fun getName(): String = alias

    override fun getFileSystem(): VirtualFileSystem = KSExplorerVirtualFileSystem.INSTANCE

    override fun getPath(): String = parent.path

    override fun isWritable(): Boolean = false

    override fun isDirectory(): Boolean = false

    override fun isValid(): Boolean = parent.isValid

    override fun getParent(): VirtualFile = parent

    override fun getChildren(): Array<VirtualFile> = parent.children

    override fun getOutputStream(requestor: Any?, newModificationStamp: Long, newTimeStamp: Long): OutputStream {
        return parent.getOutputStream(requestor, newModificationStamp, newTimeStamp);
    }

    override fun contentsToByteArray(): ByteArray = data

    override fun getTimeStamp(): Long = parent.timeStamp

    override fun getLength(): Long = contentsToByteArray().size.toLong()

    override fun refresh(asynchronous: Boolean, recursive: Boolean, postRunnable: Runnable?) = Unit

    override fun getInputStream(): InputStream {
        return parent.inputStream
    }

    override fun getFileType(): FileType = VirtualFileType
}