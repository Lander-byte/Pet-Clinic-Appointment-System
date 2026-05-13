package PetClinic.ui.screens;

import PetClinic.ui.components.FloatingInput;
import PetClinic.ui.components.RoundedPanel;
import PetClinic.ui.components.UiTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Settings / Profile screen for CUSTOMERS.
 * Lets them update their personal info and change their password.
 */
public class CustomerSettingsScreen extends JPanel {

    public CustomerSettingsScreen() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UiTheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        add(buildHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gbc.gridy = 0; gbc.insets = new Insets(28, 0, 0, 0);
        content.add(section("Personal Information", buildProfilePanel(), 180), gbc);

        gbc.gridy = 1; gbc.insets = new Insets(32, 0, 0, 0);
        content.add(section("Change Password", buildPasswordPanel(), 120), gbc);

        gbc.gridy = 2; gbc.insets = new Insets(32, 0, 0, 0);
        content.add(buildActions(), gbc);

        gbc.gridy = 3; gbc.weighty = 1.0;
        content.add(Box.createVerticalGlue(), gbc);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(UiTheme.BG_LIGHT);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    // ── Header ─────────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 4));
        header.setOpaque(false);
        JLabel title = new JLabel("My Profile & Settings");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UiTheme.TEXT_BLUE);
        JLabel sub = new JLabel("Update your personal information and security preferences");
        sub.setFont(UiTheme.BODY_FONT);
        sub.setForeground(UiTheme.TEXT_GRAY);
        header.add(title);
        header.add(sub);
        return header;
    }

    // ── Profile fields ──────────────────────────────────────────────────

    private JPanel buildProfilePanel() {
        RoundedPanel panel = new RoundedPanel(Color.WHITE, 22);
        panel.setLayout(new GridLayout(2, 2, 22, 18));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        panel.add(new FloatingInput("Full Name", false));
        panel.add(new FloatingInput("Email Address", false));
        panel.add(new FloatingInput("Phone Number", false));
        panel.add(new FloatingInput("Home Address", false));

        return panel;
    }

    // ── Password fields ─────────────────────────────────────────────────

    private JPanel buildPasswordPanel() {
        RoundedPanel panel = new RoundedPanel(Color.WHITE, 22);
        panel.setLayout(new GridLayout(1, 2, 22, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        panel.add(new FloatingInput("Current Password", true));
        panel.add(new FloatingInput("New Password", true));

        return panel;
    }

    // ── Action buttons ──────────────────────────────────────────────────

    private JPanel buildActions() {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actions.setOpaque(false);

        JButton saveBtn = UiTheme.pillButton("Save Changes", UiTheme.BLUE, Color.WHITE, 13);
        saveBtn.setPreferredSize(new Dimension(160, 44));
        saveBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Profile updated successfully.", "Saved", JOptionPane.INFORMATION_MESSAGE));

        JButton resetBtn = new JButton("Reset");
        resetBtn.setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD, 12f));
        resetBtn.setForeground(UiTheme.TEXT_GRAY);
        resetBtn.setContentAreaFilled(false);
        resetBtn.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 0));
        resetBtn.setFocusPainted(false);
        resetBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        actions.add(saveBtn);
        actions.add(resetBtn);
        return actions;
    }

    // ── Section wrapper ─────────────────────────────────────────────────

    private JPanel section(String title, JPanel body, int bodyHeight) {
        JPanel wrap = new JPanel(new BorderLayout(0, 12));
        wrap.setOpaque(false);

        JLabel lbl = new JLabel(title);
        lbl.setFont(UiTheme.SUBTITLE_FONT);
        lbl.setForeground(UiTheme.TEXT_MAIN);
        wrap.add(lbl, BorderLayout.NORTH);

        body.setPreferredSize(new Dimension(720, bodyHeight));
        wrap.add(body, BorderLayout.CENTER);
        return wrap;
    }
}
