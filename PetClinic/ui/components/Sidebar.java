package PetClinic.ui.components;

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
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.function.Consumer;

public class Sidebar extends JPanel {
    private static final int COLLAPSED = 70;
    private final String adminDashboardTarget;
    private static final int EXPANDED = 200;

    private final Consumer<String> navigator;
    private final String landingTarget;
    private final SmallSidebarLogo logo;
    private boolean expanded;
    private Timer timer;
    private String activeTab = "Overview";

    public Sidebar(Consumer<String> navigator, String adminDashboardTarget, String landingTarget) {
        this.navigator = navigator;
        this.adminDashboardTarget = adminDashboardTarget;
        this.landingTarget = landingTarget;
        setLayout(null);
        setBackground(UiTheme.BLUE);
        setPreferredSize(new Dimension(COLLAPSED, 1));
        logo = new SmallSidebarLogo();
        add(logo);
        addMenuItems();
    }

    private void addMenuItems() {
        add(sideButton(new SidebarIcon(IconType.MENU), "", 24, "toggle"));
        add(sideButton(new SidebarIcon(IconType.DASHBOARD), "Dashboard", 110, "Overview"));
        add(sideButton(new SidebarIcon(IconType.CALENDAR), "Appointments", 165, "Appointments"));
        add(sideButton(new SidebarIcon(IconType.CUSTOMER), "Customers", 220, "Customers"));
        add(sideButton(new SidebarIcon(IconType.SETTINGS), "Settings", 275, "Settings"));
        add(sideButton(new SidebarIcon(IconType.LOGOUT), "Logout", 580, landingTarget));
    }

    private JButton sideButton(Icon icon, String text, int y, String target) {
        JButton button = new JButton(text, icon) {
            @Override
            protected void paintComponent(Graphics g) {
                if (text.equals(activeTab) || (text.isEmpty() && expanded)) {
                   Graphics2D g2 = (Graphics2D) g.create();
                   g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                   g2.setColor(new Color(255, 255, 255, 40));
                   g2.fillRoundRect(5, 0, getWidth() - 10, getHeight(), 12, 12);
                   g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        button.putClientProperty("label", text);
        button.putClientProperty("y", y);
        button.setBounds(0, y, COLLAPSED, 44);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setForeground(Color.WHITE);
        button.setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD, 14f));
        button.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setIconTextGap(15);

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
        logo.setBounds(20, 60, 160, 40);
        
        for (java.awt.Component component : getComponents()) {
            if (!(component instanceof JButton)) continue;

            JButton button = (JButton) component;
            String label = (String) button.getClientProperty("label");
            int y = (Integer) button.getClientProperty("y");
            
            // Adjust logout position to bottom
            if ("Logout".equals(label)) {
                y = getHeight() - 80;
            }
            
            button.setText(showLabels ? label : "");
            button.setHorizontalAlignment(showLabels ? SwingConstants.LEFT : SwingConstants.CENTER);
            button.setBounds(showLabels ? 10 : 0, y, showLabels ? getWidth() - 20 : getWidth(), 44);
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
            int step = Math.max(1, Math.abs(targetWidth - width) / 4);
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
        
        // Gradient or subtle accent
        g2.setColor(new Color(255, 255, 255, 10));
        g2.fillRect(getWidth() - 1, 0, 1, getHeight());
        
        g2.dispose();
    }

    private enum IconType { MENU, DASHBOARD, CALENDAR, CUSTOMER, SETTINGS, LOGOUT }

    private static class SmallSidebarLogo extends JPanel {
        SmallSidebarLogo() { setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Font logoFont = UiTheme.LOGO_FONT.deriveFont(22f);
            g2.setFont(logoFont);
            g2.setColor(Color.WHITE);
            g2.drawString("Care", 0, 20);
            g2.setColor(UiTheme.ORANGE);
            g2.drawString("Haven", g2.getFontMetrics().stringWidth("Care ") - 5, 20);
            g2.dispose();
        }
    }

    private static class SidebarIcon implements Icon {
        private final IconType type;
        SidebarIcon(IconType type) { this.type = type; }
        @Override public int getIconWidth() { return 24; }
        @Override public int getIconHeight() { return 24; }
        @Override
        public void paintIcon(java.awt.Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getForeground());
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.translate(x, y);
            switch (type) {
                case MENU -> { g2.drawLine(4, 6, 20, 6); g2.drawLine(4, 12, 20, 12); g2.drawLine(4, 18, 20, 18); }
                case DASHBOARD -> { g2.drawOval(4, 4, 16, 16); g2.drawLine(12, 4, 12, 12); g2.drawLine(12, 12, 18, 12); }
                case CALENDAR -> { g2.drawRoundRect(4, 6, 16, 14, 2, 2); g2.drawLine(4, 10, 20, 10); g2.drawLine(8, 4, 8, 8); g2.drawLine(16, 4, 16, 8); }
                case CUSTOMER -> { g2.drawOval(7, 5, 10, 10); g2.drawArc(4, 15, 16, 10, 0, 180); }
                case SETTINGS -> { g2.drawOval(8, 8, 8, 8); g2.drawOval(4, 4, 16, 16); }
                case LOGOUT -> { g2.drawArc(4, 4, 16, 16, 135, 270); g2.drawLine(12, 2, 12, 12); }
            }
            g2.dispose();
        }
    }
}
