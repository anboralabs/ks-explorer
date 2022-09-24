package co.anbora.labs.kse.ide.settings;

import com.intellij.application.options.editor.EditorOptionsProvider;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static co.anbora.labs.kse.fileTypes.KeystoreFileType.EDITOR_SETTINGS_ID;


public class KSEditorSettingsProvider implements EditorOptionsProvider {

    private JCheckBox entryNameCheckBox;
    private JCheckBox algorithmCheckBox;
    private JCheckBox keySizeCheckBox;
    private JCheckBox certificateExpiryCheckBox;
    private JCheckBox subjectKeyIdentifierCheckBox;
    private JCheckBox issuerDistinguishedNameDNCheckBox;
    private JCheckBox issuerCommonNameCNCheckBox;
    private JCheckBox issuerOrganizationNameOCheckBox;
    private JCheckBox curveCheckBox;
    private JCheckBox lastModifiedCheckBox;
    private JCheckBox authorityKeyIdentifierCheckBox;
    private JCheckBox subjectDistinguishedNameDNCheckBox;
    private JCheckBox subjectCommonNameCNCheckBox;
    private JCheckBox subjectOrganizationNameOCheckBox;
    private JPanel myMainPanel;

    @Override
    public @NotNull @NonNls String getId() {
        return EDITOR_SETTINGS_ID;
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "KeyStore Viewer";
    }

    @Override
    public @Nullable @NonNls String getHelpTopic() {
        return "Editor Options for KeyStore files";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return myMainPanel;
    }

    @Override
    public boolean isModified(@NotNull JToggleButton toggleButton, boolean value) {
        return toggleButton.isSelected() != value;
    }

    @Override
    public boolean isModified() {
        KSEditorSettings editorSettings = KSEditorSettings.getInstance();
        return  isModified(entryNameCheckBox, editorSettings.getEntryName())
                || isModified(keySizeCheckBox, editorSettings.getKeySize())
                || isModified(certificateExpiryCheckBox, editorSettings.getCertificateExpiry())
                || isModified(subjectKeyIdentifierCheckBox, editorSettings.getSubjectKeyIdentifier())
                || isModified(issuerDistinguishedNameDNCheckBox, editorSettings.getIssuerDistinguishedName())
                || isModified(issuerCommonNameCNCheckBox, editorSettings.getIssuerCommonName())
                || isModified(issuerOrganizationNameOCheckBox, editorSettings.getIssuerOrganizationName())
                || isModified(algorithmCheckBox, editorSettings.getAlgorithm())
                || isModified(curveCheckBox, editorSettings.getCurve())
                || isModified(lastModifiedCheckBox, editorSettings.getLastModified())
                || isModified(authorityKeyIdentifierCheckBox, editorSettings.getAuthorityKeyIdentifier())
                || isModified(subjectDistinguishedNameDNCheckBox, editorSettings.getSubjectDistinguishedName())
                || isModified(subjectCommonNameCNCheckBox, editorSettings.getSubjectCommonName())
                || isModified(subjectOrganizationNameOCheckBox, editorSettings.getSubjectOrganizationName());
    }

    @Override
    public void reset() {
        KSEditorSettings editorSettings = KSEditorSettings.getInstance();
        entryNameCheckBox.setSelected(editorSettings.getEntryName());
        keySizeCheckBox.setSelected(editorSettings.getKeySize());
        certificateExpiryCheckBox.setSelected(editorSettings.getCertificateExpiry());
        subjectKeyIdentifierCheckBox.setSelected(editorSettings.getSubjectKeyIdentifier());
        issuerDistinguishedNameDNCheckBox.setSelected(editorSettings.getIssuerDistinguishedName());
        issuerCommonNameCNCheckBox.setSelected(editorSettings.getIssuerCommonName());
        issuerOrganizationNameOCheckBox.setSelected(editorSettings.getIssuerOrganizationName());
        algorithmCheckBox.setSelected(editorSettings.getAlgorithm());
        curveCheckBox.setSelected(editorSettings.getCurve());
        lastModifiedCheckBox.setSelected(editorSettings.getLastModified());
        authorityKeyIdentifierCheckBox.setSelected(editorSettings.getAuthorityKeyIdentifier());
        subjectDistinguishedNameDNCheckBox.setSelected(editorSettings.getSubjectDistinguishedName());
        subjectCommonNameCNCheckBox.setSelected(editorSettings.getSubjectCommonName());
        subjectOrganizationNameOCheckBox.setSelected(editorSettings.getSubjectOrganizationName());
    }

    @Override
    public void apply() throws ConfigurationException {
        KSEditorSettings editorSettings = KSEditorSettings.getInstance();
        editorSettings.setEntryName(entryNameCheckBox.isSelected());
        editorSettings.setKeySize(keySizeCheckBox.isSelected());
        editorSettings.setCertificateExpiry(certificateExpiryCheckBox.isSelected());
        editorSettings.setSubjectKeyIdentifier(subjectKeyIdentifierCheckBox.isSelected());
        editorSettings.setIssuerDistinguishedName(issuerDistinguishedNameDNCheckBox.isSelected());
        editorSettings.setIssuerCommonName(issuerCommonNameCNCheckBox.isSelected());
        editorSettings.setIssuerOrganizationName(issuerOrganizationNameOCheckBox.isSelected());
        editorSettings.setAlgorithm(algorithmCheckBox.isSelected());
        editorSettings.setCurve(curveCheckBox.isSelected());
        editorSettings.setLastModified(lastModifiedCheckBox.isSelected());
        editorSettings.setAuthorityKeyIdentifier(authorityKeyIdentifierCheckBox.isSelected());
        editorSettings.setSubjectDistinguishedName(subjectDistinguishedNameDNCheckBox.isSelected());
        editorSettings.setSubjectCommonName(subjectCommonNameCNCheckBox.isSelected());
        editorSettings.setSubjectOrganizationName(subjectOrganizationNameOCheckBox.isSelected());
    }
}
