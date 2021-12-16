package co.anbora.labs.kse.ide.gui.swing;

import co.anbora.labs.kse.ide.gui.TableEditor;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class KeyStoreTableSwing extends TableEditor {

    private JPanel panelMain;
    private JTable tblEditor;
    private JScrollPane tableScrollPanel;
    private JPanel passwordPanel;
    private JTextField textField1;
    private JButton button1;

    public KeyStoreTableSwing(@NotNull Project projectArg, @NotNull VirtualFile fileArg) {
        super(projectArg, fileArg);
    }

    private void createUIComponents() {
        tblEditor = new JBTable(new DefaultTableModel(0, 0));
    }

    @Override
    public @NotNull JComponent getComponent() {
        return this.panelMain;
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return this.tblEditor;
    }

    @Override
    public void setState(@NotNull FileEditorState state) {

    }
}
