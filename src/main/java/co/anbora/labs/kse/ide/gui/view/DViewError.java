package co.anbora.labs.kse.ide.gui.view;

import co.anbora.labs.kse.ide.gui.CertEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DViewError extends CertEditor {

  private final String errorMessage;

  private JPanel jpError;
  private JLabel jlError;

  public DViewError(Project project, VirtualFile file, String errorMessage) {
    super(project, file);
    this.errorMessage = errorMessage;
    initComponents();
  }

  private void initComponents() {
    jpError = new JPanel(new FlowLayout(FlowLayout.CENTER));
    jpError.setBorder(new EmptyBorder(5, 5, 5, 5));

    jlError = new JLabel(errorMessage);
    ImageIcon icon = new ImageIcon(getClass().getResource("images/error.png"));
    jlError.setIcon(icon);
    jlError.setHorizontalTextPosition(SwingConstants.TRAILING);
    jlError.setIconTextGap(15);

    jpError.add(jlError);

    getContentPane().add(jpError, BorderLayout.CENTER);
  }

  @Override
  public @NotNull JComponent getComponent() {
    return getRootPane();
  }

  @Override
  public @Nullable JComponent getPreferredFocusedComponent() {
    return getRootPane();
  }
}
