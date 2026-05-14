package PetClinic.ui.components;

import PetClinic.model.user.User;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.function.Consumer;

public class Sidebar extends JPanel {
    private static final int COLLAPSED = 70;
    private static final int EXPANDED = 220;

    private final Consumer<String> navigator;
    private final String landingTarget;
    private final SmallSidebarLogo logo;
    private final boolean admin;
    private final User user;
    private boolean expanded;
    private Timer timer;
    private String activeTab = "Overview";

    public Sidebar(boolean admin, Consumer<String> navigator, String landingTarget) {
        this(null, admin, navigator, landingTarget);
    }

    public Sidebar(User user, Consumer<String> navigator, String landingTarget) {
        this(user, true, navigator, landingTarget);
    }

    private Sidebar(User user, boolean admin, Consumer<String> navigator, String landingTarget) {
        this.admin = admin;
        this.user = user;
        this.navigator = navigator;
        this.landingTarget = landingTarget;
        setLayout(null);
        setBackground(UiTheme.BLUE);
        setPreferredSize(new Dimension(COLLAPSED, 1));
        logo = new SmallSidebarLogo();
        add(logo);
        addMenuItems();
    }

    private void addMenuItems() {
        // Menu toggle at the top
        add(sideButton(new SidebarIcon(IconType.MENU), "", 25, "toggle"));

        // Profile section
        add(sideButton(new SidebarIcon(IconType.PROFILE), "Profile", 175, "Profile"));

        // Navigation items
        add(sideButton(new SidebarIcon(IconType.DASHBOARD), "Dashboard", 240, "Overview"));
        add(sideButton(new SidebarIcon(IconType.CALENDAR), "Appointments", 295, "Appointments"));

        if (admin) {
            int y = 350;
            if (isVeterinarian()) {
                add(sideButton(new SidebarIcon(IconType.CLINICAL), "Treatment Plans", y, "Treatment Plans"));
                y += 55;
            }
            if (isStaff()) {
                add(sideButton(new SidebarIcon(IconType.BILLING), "Billing", y, "Billing"));
                y += 55;
                add(sideButton(new SidebarIcon(IconType.SERVICE), "Services", y, "Services"));
                y += 55;
                add(sideButton(new SidebarIcon(IconType.CUSTOMER), "Customers", y, "Customers"));
                y += 55;
            }
            add(sideButton(new SidebarIcon(IconType.SETTINGS), "Settings", y, "Settings"));
        } else {
            // Shift Settings up if Customers is hidden
            add(sideButton(new SidebarIcon(IconType.SETTINGS), "Settings", 350, "Settings"));
        }

        // Logout pinned to bottom
        add(sideButton(new SidebarIcon(IconType.LOGOUT), "Logout", 580, landingTarget));
    }

    private boolean isVeterinarian() {
        return user != null && "Veterinarian".equalsIgnoreCase(user.getRole());
    }

    private boolean isStaff() {
        return user != null && "Staff".equalsIgnoreCase(user.getRole());
    }

    private JButton sideButton(Icon icon, String text, int y, String target) {
        JButton button = new JButton(text, icon) {
            @Override
            protected void paintComponent(Graphics g) {
                if (text.equals(activeTab) || ("".equals(text) && "toggle".equals(target))) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 35));
                    g2.fillRoundRect(8, 0, getWidth() - 16, getHeight(), 12, 12);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        button.putClientProperty("label", text);
        button.putClientProperty("y", y);
        button.setBounds(0, y, COLLAPSED, 46);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setForeground(Color.WHITE);
        button.setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD, 14f));
        button.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setIconTextGap(18);

        button.addActionListener(e -> {
            if ("toggle".equals(target)) {
                toggleSidebar();
            } else {
                if (!target.equals(landingTarget)) {
                    activeTab = text;
                    repaint();
                }
                navigator.accept(target);
            }
        });

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setForeground(UiTheme.ORANGE);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
        });
        return button;
    }

    @Override
    public void doLayout() {
        boolean showLabels = getWidth() > COLLAPSED + 30;

        logo.setVisible(showLabels);
        logo.setBounds(25, 100, 180, 50);

        for (java.awt.Component component : getComponents()) {
            if (!(component instanceof JButton)) continue;

            JButton button = (JButton) component;
            String label = (String) button.getClientProperty("label");
            int y = (Integer) button.getClientProperty("y");

            if ("Logout".equals(label)) {
                y = getHeight() - 85;
            }

            button.setText(showLabels ? label : "");
            button.setHorizontalAlignment(showLabels ? SwingConstants.LEFT : SwingConstants.CENTER);

            int x = showLabels ? 15 : 0;
            int w = showLabels ? getWidth() - 30 : getWidth();
            button.setBounds(x, y, w, 46);
        }
    }

    private void toggleSidebar() {
        if (timer != null && timer.isRunning()) return;
        expanded = !expanded;
        int targetWidth = expanded ? EXPANDED : COLLAPSED;
        timer = new Timer(10, null);
        timer.addActionListener(e -> {
            int width = getPreferredSize().width;
            if (width == targetWidth) {
                timer.stop();
                return;
            }
            int step = Math.max(1, Math.abs(targetWidth - width) / 3);
            int next = width + (targetWidth > width ? step : -step);
            setPreferredSize(new Dimension(next, getHeight()));
            revalidate();
            if (getParent() != null) getParent().repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(UiTheme.BLUE);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(new Color(255, 255, 255, 15));
        g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());

        g2.dispose();
    }

    private enum IconType { MENU, PROFILE, DASHBOARD, CALENDAR, CUSTOMER, SETTINGS, LOGOUT, CLINICAL, BILLING, SERVICE }

    private static class SmallSidebarLogo extends JPanel {
        SmallSidebarLogo() { setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Font logoFont = UiTheme.LOGO_FONT.deriveFont(26f);
            g2.setFont(logoFont);
            g2.setColor(Color.WHITE);
            g2.drawString("Care", 0, 30);
            g2.setColor(UiTheme.ORANGE);
            g2.drawString("Haven", g2.getFontMetrics().stringWidth("Care "), 30);
            g2.dispose();
        }
    }

    private static class SidebarIcon implements Icon {
        private final IconType type;
        SidebarIcon(IconType type) { this.type = type; }
        @Override public int getIconWidth() { return 26; }
        @Override public int getIconHeight() { return 26; }
        @Override
        public void paintIcon(java.awt.Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getForeground());
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.translate(x, y);
            switch (type) {
                case MENU -> { g2.drawLine(4, 7, 22, 7); g2.drawLine(4, 13, 22, 13); g2.drawLine(4, 19, 22, 19); }
                case PROFILE -> {
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawOval(2, 2, 22, 22);
                    g2.fillOval(7, 6, 12, 12);
                    g2.drawArc(4, 15, 18, 10, 0, 180);
                }
                case DASHBOARD -> { g2.drawOval(4, 4, 18, 18); g2.drawLine(13, 4, 13, 13); g2.drawLine(13, 13, 19, 13); }
                case CALENDAR -> { g2.drawRoundRect(4, 6, 18, 16, 3, 3); g2.drawLine(4, 11, 22, 11); g2.drawLine(9, 4, 9, 8); g2.drawLine(17, 4, 17, 8); }
                case CUSTOMER -> { g2.drawOval(8, 5, 10, 10); g2.drawArc(5, 16, 16, 10, 0, 180); }
                case CLINICAL -> { g2.drawOval(6, 6, 14, 14); g2.drawLine(13, 8, 13, 18); g2.drawLine(8, 13, 18, 13); }
                case BILLING -> { g2.drawRect(6, 4, 14, 18); g2.drawLine(9, 9, 17, 9); g2.drawLine(9, 13, 17, 13); g2.drawLine(9, 17, 14, 17); }
                case SERVICE -> { g2.drawRoundRect(5, 6, 16, 14, 3, 3); g2.drawLine(9, 10, 17, 10); g2.drawLine(9, 15, 17, 15); }
                case SETTINGS -> {
                    g2.drawOval(9, 9, 8, 8);
                    for(int i=0; i<8; i++) {
                        double angle = Math.toRadians(i*45);
                        g2.drawLine((int)(13+Math.cos(angle)*8), (int)(13+Math.sin(angle)*8), (int)(13+Math.cos(angle)*11), (int)(13+Math.sin(angle)*11));
                    }
                }
                case LOGOUT -> { g2.drawArc(5, 5, 16, 16, 135, 270); g2.drawLine(13, 3, 13, 13); }
            }
            g2.dispose();
        }
    }
}
