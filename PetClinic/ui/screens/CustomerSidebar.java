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
import java.awt.RenderingHints;
import java.util.function.Consumer;

/**
 * Sidebar for the customer-facing dashboard.
 *
 * Nav items:
 *   Home | Appointments | My Pets | Settings | Logout
 */
public class CustomerSidebar extends JPanel {

    private static final int COLLAPSED = 70;
    private static final int EXPANDED  = 200;

    private final Consumer<String> navigator;
    private final String logoutTarget;
    private boolean expanded;
    private Timer timer;
    private String activeTab = "Home";

    public CustomerSidebar(Consumer<String> navigator, String logoutTarget) {
        this.navigator     = navigator;
        this.logoutTarget  = logoutTarget;
        setLayout(null);
        setBackground(UiTheme.BLUE);
        setPreferredSize(new Dimension(COLLAPSED, 1));
        add(new SmallSidebarLogo());
        addMenuItems();
    }

    private void addMenuItems() {
        add(navButton(IconType.MENU,         "",             24,  "toggle"));
        add(navButton(IconType.HOME,         "Home",        110,  "Home"));
        add(navButton(IconType.CALENDAR,     "Appointments",165,  "Appointments"));
        add(navButton(IconType.PETS,         "My Pets",     220,  "My Pets"));
        add(navButton(IconType.SETTINGS,     "Settings",    275,  "Settings"));
        add(navButton(IconType.LOGOUT,       "Logout",      999,  logoutTarget));  // pinned to bottom
    }

    private JButton navButton(IconType type, String label, int y, String target) {
        JButton btn = new JButton(label, new NavIcon(type)) {
            @Override
            protected void paintComponent(Graphics g) {
                if (label.equals(activeTab) || (label.isEmpty() && expanded)) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 40));
                    g2.fillRoundRect(5, 0, getWidth() - 10, getHeight(), 12, 12);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        btn.putClientProperty("label", label);
        btn.putClientProperty("y", y);
        btn.setBounds(0, y, COLLAPSED, 44);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setForeground(Color.WHITE);
        btn.setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD, 14f));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setIconTextGap(15);

        btn.addActionListener(e -> {
            if ("toggle".equals(target)) {
                toggleSidebar();
            } else {
                if (!target.equals(logoutTarget)) {
                    activeTab = label;
                    repaint();
                }
                navigator.accept(target);
            }
        });

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.setForeground(UiTheme.ORANGE); }
            @Override public void mouseExited (java.awt.event.MouseEvent e) { btn.setForeground(Color.WHITE); }
        });
        return btn;
    }

    @Override
    public void doLayout() {
        boolean wide = getWidth() > COLLAPSED + 30;

        for (java.awt.Component c : getComponents()) {
            if (c instanceof SmallSidebarLogo logo) {
                logo.setVisible(wide);
                logo.setBounds(20, 60, 160, 40);
                continue;
            }
            if (!(c instanceof JButton btn)) continue;

            String label = (String) btn.getClientProperty("label");
            int    y     = (Integer) btn.getClientProperty("y");

            if ("Logout".equals(label)) y = getHeight() - 80;

            btn.setText(wide ? label : "");
            btn.setHorizontalAlignment(wide ? SwingConstants.LEFT : SwingConstants.CENTER);
            btn.setBounds(wide ? 10 : 0, y, wide ? getWidth() - 20 : getWidth(), 44);
        }
    }

    private void toggleSidebar() {
        if (timer != null && timer.isRunning()) return;
        expanded = !expanded;
        int target = expanded ? EXPANDED : COLLAPSED;
        timer = new Timer(10, null);
        timer.addActionListener(e -> {
            int w = getPreferredSize().width;
            if (w == target) { timer.stop(); return; }
            int step = Math.max(1, Math.abs(target - w) / 4);
            setPreferredSize(new Dimension(w + (target > w ? step : -step), getHeight()));
            revalidate();
            if (getParent() != null) getParent().repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(UiTheme.BLUE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    // ── Icon types ────────────────────────────────────────────────────

    private enum IconType { MENU, HOME, CALENDAR, PETS, SETTINGS, LOGOUT }

    // ── Inner classes ─────────────────────────────────────────────────

    private static class SmallSidebarLogo extends JPanel {
        SmallSidebarLogo() { setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(UiTheme.LOGO_FONT.deriveFont(22f));
            g2.setColor(Color.WHITE);
            g2.drawString("Care", 0, 20);
            g2.setColor(UiTheme.ORANGE);
            g2.drawString("Haven", g2.getFontMetrics().stringWidth("Care ") - 5, 20);
            g2.dispose();
        }
    }

    private static class NavIcon implements Icon {
        private final IconType type;
        NavIcon(IconType type) { this.type = type; }
        @Override public int getIconWidth()  { return 24; }
        @Override public int getIconHeight() { return 24; }
        @Override
        public void paintIcon(java.awt.Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getForeground());
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.translate(x, y);
            switch (type) {
                // Hamburger
                case MENU     -> { g2.drawLine(4,6,20,6); g2.drawLine(4,12,20,12); g2.drawLine(4,18,20,18); }
                // House
                case HOME     -> { g2.drawPolygon(new int[]{12,3,21}, new int[]{3,11,11}, 3);
                                   g2.drawRect(7,11,10,10); g2.drawRect(9,15,6,6); }
                // Calendar
                case CALENDAR -> { g2.drawRoundRect(4,6,16,14,2,2); g2.drawLine(4,10,20,10);
                                   g2.drawLine(8,4,8,8); g2.drawLine(16,4,16,8); }
                // Paw print (simplified)
                case PETS     -> { g2.fillOval(11,4,4,4); g2.fillOval(5,7,3,3);
                                   g2.fillOval(16,7,3,3); g2.fillOval(7,10,3,4);
                                   g2.fillOval(14,10,3,4);
                                   g2.fillOval(9,13,6,6); }
                // Gear
                case SETTINGS -> { g2.drawOval(8,8,8,8); g2.drawOval(4,4,16,16); }
                // Power/logout arrow
                case LOGOUT   -> { g2.drawArc(4,4,16,16,135,270); g2.drawLine(12,2,12,12); }
            }
            g2.dispose();
        }
    }
}
