package ui.screens.admin;

import model.user.User;
import ui.components.FloatingInput;
import ui.components.RoundedPanel;
import ui.components.UiTheme;

import javax.swing.*;
import java.awt.*;

public class ProfileScreen extends JPanel {
    private final User user;
    private final FloatingInput nameInput;
    private final FloatingInput emailInput;
    private final FloatingInput phoneInput;
    private final FloatingInput addressInput;

    public ProfileScreen(User user) {
        this.user = user;
        setLayout(new BorderLayout());
        setBackground(UiTheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("User Profile");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UiTheme.TEXT_BLUE);
        header.add(title, BorderLayout.NORTH);
        
        JLabel subtitle = new JLabel("View and update your personal information");
        subtitle.setFont(UiTheme.BODY_FONT);
        subtitle.setForeground(UiTheme.TEXT_GRAY);
        header.add(subtitle, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // Profile Picture Circle
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UiTheme.BLUE);
                g2.fillOval(0, 0, 100, 100);
                
                g2.setColor(Color.WHITE);
                g2.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 40f));
                String initial = user.getName().isEmpty() ? "?" : user.getName().substring(0, 1).toUpperCase();
                FontMetrics fm = g2.getFontMetrics();
                int x = (100 - fm.stringWidth(initial)) / 2;
                int y = (100 + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(initial, x, y);
                g2.dispose();
            }
        };
        avatarPanel.setOpaque(false);
        avatarPanel.setPreferredSize(new Dimension(100, 100));
        
        JPanel avatarContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        avatarContainer.setOpaque(false);
        avatarContainer.add(avatarPanel);
        
        JButton editAvatar = new JButton("Change Photo");
        editAvatar.setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD));
        editAvatar.setForeground(UiTheme.BLUE);
        editAvatar.setContentAreaFilled(false);
        editAvatar.setBorder(null);
        editAvatar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        avatarContainer.add(Box.createHorizontalStrut(20));
        avatarContainer.add(editAvatar);
        
        content.add(avatarContainer, gbc);

        // Info Section
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        
        RoundedPanel panel = new RoundedPanel(Color.WHITE, 25);
        panel.setLayout(new GridLayout(2, 2, 25, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        nameInput = new FloatingInput("Full Name", false);
        nameInput.setText(user.getName());
        
        emailInput = new FloatingInput("Email Address", false);
        emailInput.setText(user.getEmail());
        
        phoneInput = new FloatingInput("Phone Number", false);
        phoneInput.setText(user.getPhone());
        
        addressInput = new FloatingInput("Address", false);
        addressInput.setText(user.getAddress());

        panel.add(nameInput);
        panel.add(emailInput);
        panel.add(phoneInput);
        panel.add(addressInput);

        content.add(panel, gbc);

        // Role & ID info (Read only)
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 10, 0, 0);
        JLabel metaLabel = new JLabel("Role: " + user.getRole() + " | Account ID: #" + user.getUserId());
        metaLabel.setFont(UiTheme.BODY_FONT.deriveFont(Font.ITALIC));
        metaLabel.setForeground(UiTheme.TEXT_GRAY);
        content.add(metaLabel, gbc);

        // Save Button
        gbc.gridy = 3;
        gbc.insets = new Insets(40, 0, 0, 0);
        JButton saveBtn = UiTheme.pillButton("Save Changes", UiTheme.BLUE, Color.WHITE, 13);
        saveBtn.setPreferredSize(new Dimension(160, 45));
        saveBtn.addActionListener(e -> saveProfile());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(saveBtn);
        content.add(btnPanel, gbc);

        gbc.gridy = 4;
        gbc.weighty = 1.0;
        content.add(Box.createVerticalGlue(), gbc);

        add(content, BorderLayout.CENTER);
    }

    private void saveProfile() {
        String name = nameInput.getText().trim();
        String email = emailInput.getText().trim();
        String phone = phoneInput.getText().trim();
        if (name.length() < 2) {
            JOptionPane.showMessageDialog(this, "Name must be at least 2 characters.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!email.isEmpty() && (!email.contains("@") || !email.contains("."))) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!phone.isEmpty() && !phone.matches("[0-9+()\\-\\s]{7,20}")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid phone number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(addressInput.getText().trim());
        JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
