package co.anbora.labs.kse.ide.gui.swing;

import co.anbora.labs.kse.ide.gui.TableEditor;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TableEditorSwing extends TableEditor {

    private JPanel panelMain;
    private JTable tblEditor;
    private JScrollPane tableScrollPanel;

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

    @Override
    public boolean isValid() {
        return false;
    }
}
