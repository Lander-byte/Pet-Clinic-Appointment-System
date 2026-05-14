package PetClinic.ui.screens.admin;

import PetClinic.ui.components.FloatingInput;
import PetClinic.ui.components.RoundedPanel;
import PetClinic.ui.components.UiTheme;
import javax.swing.*;
import java.awt.*;

public class SettingsScreen extends JPanel {
    public SettingsScreen() {
        setLayout(new BorderLayout());
        setBackground(UiTheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Settings");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UiTheme.TEXT_BLUE);
        header.add(title, BorderLayout.NORTH);
        
        JLabel subtitle = new JLabel("Manage your application preferences and security");
        subtitle.setFont(UiTheme.BODY_FONT);
        subtitle.setForeground(UiTheme.TEXT_GRAY);
        header.add(subtitle, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // Content Panel with GridBagLayout for stability
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // General Settings Section
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 0, 0, 0);
        content.add(createSection("General Settings", createGeneralSettingsPanel(), 180), gbc);
        
        // Security Section
        gbc.gridy = 1;
        gbc.insets = new Insets(35, 0, 0, 0);
        content.add(createSection("Security & Password", createSecurityPanel(), 130), gbc);

        // Action Buttons
        gbc.gridy = 2;
        gbc.insets = new Insets(40, 0, 0, 0);
        content.add(createActionsPanel(), gbc);

        // Spacer to push everything up
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        content.add(Box.createVerticalGlue(), gbc);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UiTheme.BG_LIGHT);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createSection(String title, JPanel component, int height) {
        JPanel container = new JPanel(new BorderLayout(0, 15));
        container.setOpaque(false);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UiTheme.SUBTITLE_FONT);
        lblTitle.setForeground(UiTheme.TEXT_MAIN);
        container.add(lblTitle, BorderLayout.NORTH);
        
        component.setPreferredSize(new Dimension(750, height));
        container.add(component, BorderLayout.CENTER);
        
        return container;
    }

    private JPanel createGeneralSettingsPanel() {
        RoundedPanel panel = new RoundedPanel(Color.WHITE, 25);
        panel.setLayout(new GridLayout(2, 2, 25, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JCheckBox notifications = new JCheckBox("Enable Desktop Notifications", true);
        notifications.setFont(UiTheme.BODY_FONT);
        notifications.setOpaque(false);
        
        JCheckBox emailAlerts = new JCheckBox("Receive Email Reminders", true);
        emailAlerts.setFont(UiTheme.BODY_FONT);
        emailAlerts.setOpaque(false);

        JPanel themePanel = new JPanel(new BorderLayout(10, 0));
        themePanel.setOpaque(false);
        JLabel themeLabel = new JLabel("App Theme:");
        themeLabel.setFont(UiTheme.BODY_FONT);
        JComboBox<String> themeCombo = new JComboBox<>(new String[]{"Light Mode", "Dark Mode", "System Default"});
        themeCombo.setFont(UiTheme.BODY_FONT);
        themePanel.add(themeLabel, BorderLayout.WEST);
        themePanel.add(themeCombo, BorderLayout.CENTER);

        JPanel langPanel = new JPanel(new BorderLayout(10, 0));
        langPanel.setOpaque(false);
        JLabel langLabel = new JLabel("Language:");
        langLabel.setFont(UiTheme.BODY_FONT);
        JComboBox<String> langCombo = new JComboBox<>(new String[]{"English (US)", "English (UK)", "Filipino"});
        langCombo.setFont(UiTheme.BODY_FONT);
        langPanel.add(langLabel, BorderLayout.WEST);
        langPanel.add(langCombo, BorderLayout.CENTER);

        panel.add(notifications);
        panel.add(emailAlerts);
        panel.add(themePanel);
        panel.add(langPanel);
        
        return panel;
    }

    private JPanel createSecurityPanel() {
        RoundedPanel panel = new RoundedPanel(Color.WHITE, 25);
        panel.setLayout(new GridLayout(1, 2, 25, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        panel.add(new FloatingInput("Current Password", true));
        panel.add(new FloatingInput("New Password", true));
        
        return panel;
    }

    private JPanel createActionsPanel() {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actions.setOpaque(false);
        
        JButton saveBtn = UiTheme.pillButton("Save All Changes", UiTheme.BLUE, Color.WHITE, 13);
        saveBtn.setPreferredSize(new Dimension(180, 45));
        actions.add(saveBtn);
        
        actions.add(Box.createHorizontalStrut(25));
        
        JButton resetBtn = new JButton("Reset Form");
        resetBtn.setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD));
        resetBtn.setForeground(UiTheme.TEXT_GRAY);
        resetBtn.setContentAreaFilled(false);
        resetBtn.setBorder(null);
        resetBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        actions.add(resetBtn);
        
        return actions;
    }
}
