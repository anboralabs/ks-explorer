package co.anbora.labs.kse.ide.gui.view;

import co.anbora.labs.kse.ide.gui.CertEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DViewError extends CertEditor {

    

    @Override
    public @NotNull JComponent getComponent() {
        return getRootPane();
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return getRootPane();
    }
}
