package ui.screens;

import ui.components.FloatingInput;
import ui.components.RoundedPanel;
import ui.components.UiTheme;
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
        
        JLabel subtitle = new JLabel("Manage your clinic profile and security preferences");
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

        // Profile Section
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 0, 0, 0);
        content.add(createSection("Clinic Profile", createProfilePanel(), 210), gbc);
        
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

    private JPanel createProfilePanel() {
        RoundedPanel panel = new RoundedPanel(Color.WHITE, 25);
        panel.setLayout(new GridLayout(2, 2, 25, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        panel.add(new FloatingInput("Clinic Name", false));
        panel.add(new FloatingInput("Email Address", false));
        panel.add(new FloatingInput("Phone Number", false));
        panel.add(new FloatingInput("Clinic Address", false));
        
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
 
