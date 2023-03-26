package co.anbora.labs.kse.ide.gui.swing;

import co.anbora.labs.kse.ide.gui.CertEditor;
import co.anbora.labs.kse.ide.gui.view.DViewPrivateKey;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kse.crypto.CryptoException;
import org.kse.gui.AddPrivateKey;
import org.kse.gui.actions.OpenAction;
import org.kse.gui.actions.PressEnterAction;
import org.kse.gui.actions.behavior.ActionBehavior;
import org.kse.gui.actions.behavior.OpenPrivateKeyImpl;
import org.kse.gui.statusbar.StatusBar;

import javax.swing.*;
import java.awt.*;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

public class PrivateKeyWithPasswordView extends CertEditor implements StatusBar, AddPrivateKey {

    private final UnlockPanel customUnlockPanel = new UnlockPanel("Unlock Private key");
    private DViewPrivateKey privateKeyView;

    private JComponent view;

    private OpenAction openAction;

    public PrivateKeyWithPasswordView(
            @NotNull Project projectArg,
            @NotNull VirtualFile fileArg
    ) throws CryptoException {
        super(projectArg, fileArg);
        createUIComponents();
        initUnlockPanel(projectArg);
    }

    private void createUIComponents() throws CryptoException {
        privateKeyView = new DViewPrivateKey(this.projectArg, this.fileArg, null);
        view = privateKeyView.getComponent();

        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        pane.add(customUnlockPanel.getComponent(), BorderLayout.NORTH);
        pane.add(view, BorderLayout.CENTER);

        view.setVisible(false);
    }

    private void initUnlockPanel(@NotNull Project projectArg) {
        ActionBehavior actionBehavior =
                new OpenPrivateKeyImpl( this, this, customUnlockPanel.getPasswordField());
        openAction = new OpenAction(projectArg, this, actionBehavior);

        customUnlockPanel.addActionListener(openAction);
        customUnlockPanel.addKeyListener(new PressEnterAction(projectArg, this, actionBehavior));
    }

    @Override
    public @NotNull JComponent getComponent() {
        return getRootPane();
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return getRootPane();
    }

    @Override
    public void setStatusBarText(String status) {

    }

    @Override
    public void setDefaultStatusBarText() {

    }

    @Override
    public void addPrivateKey(@Nullable PrivateKey privateKey) throws GeneralSecurityException, CryptoException {
        if (privateKey != null) {
            customUnlockPanel.setVisible(false);
            view.setVisible(true);
            privateKeyView.loadPrivateKey(privateKey);
        }
    }
}
