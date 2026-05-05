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
    private static final int COLLAPSED = 60;
    private static final int EXPANDED = 164;

    private final Consumer<String> navigator;
    private final String dashboardTarget;
    private final String landingTarget;
    private final SmallSidebarLogo logo;
    private boolean expanded;
    private Timer timer;

    public Sidebar(Consumer<String> navigator, String dashboardTarget, String landingTarget) {
        this.navigator = navigator;
        this.dashboardTarget = dashboardTarget;
        this.landingTarget = landingTarget;
        setLayout(null);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(COLLAPSED, 1));
        logo = new SmallSidebarLogo();
        add(logo);
        addMenuItems();
    }

    private void addMenuItems() {
        add(sideButton(new SidebarIcon(IconType.MENU), "", 24, null));
        add(sideButton(new SidebarIcon(IconType.DASHBOARD), "Dashboard", 118, dashboardTarget));
        add(sideButton(new SidebarIcon(IconType.CALENDAR), "Appointments", 168, dashboardTarget));
        add(sideButton(new SidebarIcon(IconType.CUSTOMER), "Customers", 218, dashboardTarget));
        add(sideButton(new SidebarIcon(IconType.SETTINGS), "Settings", 268, dashboardTarget));
        add(sideButton(new SidebarIcon(IconType.LOGOUT), "Logout", 318, landingTarget));
    }

    private JButton sideButton(Icon icon, String text, int y, String target) {
        JButton button = new JButton(text, icon);
        button.putClientProperty("label", text);
        button.putClientProperty("y", y);
        button.setBounds(0, y, COLLAPSED, 34);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setForeground(Color.WHITE);
        button.setBackground(UiTheme.BLUE);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setIconTextGap(14);
        button.setMargin(new Insets(0, 0, 0, 0));

        if (text.isEmpty()) {
            button.addActionListener(e -> toggleSidebar());
        } else {
            button.addActionListener(e -> navigator.accept(target));
        }

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
        boolean showLabels = getWidth() > COLLAPSED + 20;
        logo.setVisible(showLabels);
        logo.setBounds(54, 18, 95, 36);
        for (java.awt.Component component : getComponents()) {
            if (!(component instanceof JButton)) {
                continue;
            }

            JButton button = (JButton) component;
            String label = (String) button.getClientProperty("label");
            int y = (Integer) button.getClientProperty("y");
            button.setText(showLabels ? label : "");
            button.setHorizontalAlignment(showLabels ? SwingConstants.LEFT : SwingConstants.CENTER);
            button.setBounds(showLabels ? 15 : 0, y, showLabels ? getWidth() - 28 : getWidth(), 34);
        }
    }

    private void toggleSidebar() {
        if (timer != null && timer.isRunning()) {
            return;
        }
        expanded = !expanded;
        int target = expanded ? EXPANDED : COLLAPSED;
        timer = new Timer(8, null);
        timer.addActionListener(e -> {
            int width = getPreferredSize().width;
            if (width == target) {
                timer.stop();
                return;
            }
            int direction = target > width ? 1 : -1;
            int next = width + (direction * 6);
            if ((direction > 0 && next > target) || (direction < 0 && next < target)) {
                next = target;
            }
            setPreferredSize(new Dimension(next, getHeight()));
            revalidate();
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(UiTheme.BLUE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.fillRect(0, 0, Math.max(0, getWidth() - 10), getHeight());
        g2.dispose();
    }

    private enum IconType {
        MENU, DASHBOARD, CALENDAR, CUSTOMER, SETTINGS, LOGOUT
    }

    private static class SmallSidebarLogo extends JPanel {
        SmallSidebarLogo() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Font logo = UiTheme.LOGO_FONT.deriveFont(20f);
            g2.setFont(logo);
            int careWidth = g2.getFontMetrics().stringWidth("Care");
            g2.setColor(Color.WHITE);
            g2.drawString("Care", 0, 22);
            g2.setColor(UiTheme.ORANGE);
            g2.drawString("Haven", careWidth, 22);

            g2.setFont(new Font("Lilita One", Font.PLAIN, 8));
            g2.setColor(new Color(230, 230, 230));
            g2.drawString("Veterinary", 23, 33);
            g2.dispose();
        }
    }

    private static class SidebarIcon implements Icon {
        private final IconType type;

        SidebarIcon(IconType type) {
            this.type = type;
        }

        @Override
        public int getIconWidth() {
            return 26;
        }

        @Override
        public int getIconHeight() {
            return 26;
        }

        @Override
        public void paintIcon(java.awt.Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getForeground());
            g2.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.translate(x, y);

            switch (type) {
                case MENU:
                    drawMenu(g2);
                    break;
                case DASHBOARD:
                    drawDashboard(g2);
                    break;
                case CALENDAR:
                    drawCalendar(g2);
                    break;
                case CUSTOMER:
                    drawCustomer(g2);
                    break;
                case SETTINGS:
                    drawSettings(g2);
                    break;
                case LOGOUT:
                    drawLogout(g2);
                    break;
                default:
                    break;
            }

            g2.dispose();
        }

        private void drawMenu(Graphics2D g2) {
            g2.drawLine(2, 6, 24, 6);
            g2.drawLine(2, 13, 24, 13);
            g2.drawLine(2, 20, 24, 20);
        }

        private void drawDashboard(Graphics2D g2) {
            g2.drawOval(2, 3, 21, 21);
            g2.drawLine(13, 3, 13, 13);
            g2.drawLine(13, 13, 23, 13);
        }

        private void drawCalendar(Graphics2D g2) {
            g2.drawRoundRect(3, 5, 20, 18, 2, 2);
            g2.drawLine(3, 10, 23, 10);
            g2.drawLine(8, 2, 8, 7);
            g2.drawLine(18, 2, 18, 7);
            g2.fillOval(8, 14, 2, 2);
            g2.fillOval(13, 14, 2, 2);
            g2.fillOval(18, 14, 2, 2);
        }

        private void drawCustomer(Graphics2D g2) {
            g2.drawOval(3, 3, 20, 20);
            g2.drawOval(9, 8, 8, 8);
            g2.drawArc(7, 14, 12, 10, 20, 140);
        }

        private void drawSettings(Graphics2D g2) {
            java.awt.Polygon gear = new java.awt.Polygon();
            for (int i = 0; i < 16; i++) {
                double angle = Math.toRadians(-90 + (i * 22.5));
                double radius = i % 2 == 0 ? 11.5 : 8.5;
                int px = 13 + (int) Math.round(Math.cos(angle) * radius);
                int py = 13 + (int) Math.round(Math.sin(angle) * radius);
                gear.addPoint(px, py);
            }
            g2.drawPolygon(gear);
            g2.drawOval(9, 9, 8, 8);
        }

        private void drawLogout(Graphics2D g2) {
            g2.drawLine(13, 5, 22, 5);
            g2.drawLine(22, 5, 22, 21);
            g2.drawLine(13, 21, 22, 21);
            g2.drawLine(4, 13, 15, 13);
            g2.drawLine(4, 13, 10, 7);
            g2.drawLine(4, 13, 10, 19);
        }
    }
}