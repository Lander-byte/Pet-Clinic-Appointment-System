//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package PetClinic.ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CustomerSidebar extends JPanel {
    private static final int COLLAPSED = 70;
    private static final int EXPANDED = 200;
    private final Consumer<String> navigator;
    private final String logoutTarget;
    private boolean expanded;
    private Timer timer;
    private String activeTab = "Home";

    public CustomerSidebar(Consumer<String> navigator, String logoutTarget) {
        this.navigator = navigator;
        this.logoutTarget = logoutTarget;
        this.setLayout((LayoutManager)null);
        this.setBackground(UiTheme.BLUE);
        this.setPreferredSize(new Dimension(70, 1));
        this.add(new SmallSidebarLogo());
        this.addMenuItems();
    }

    private void addMenuItems() {
        this.add(this.navButton(CustomerSidebar.IconType.MENU, "", 24, "toggle"));
        this.add(this.navButton(CustomerSidebar.IconType.HOME, "Home", 110, "Home"));
        this.add(this.navButton(CustomerSidebar.IconType.PROFILE, "Profile", 165, "Profile"));
        this.add(this.navButton(CustomerSidebar.IconType.CALENDAR, "Appointments", 220, "Appointments"));
        this.add(this.navButton(CustomerSidebar.IconType.PETS, "My Pets", 275, "My Pets"));
        this.add(this.navButton(CustomerSidebar.IconType.PAYMENTS, "Payments", 330, "Payments"));
        this.add(this.navButton(CustomerSidebar.IconType.SETTINGS, "Settings", 385, "Settings"));
        this.add(this.navButton(CustomerSidebar.IconType.LOGOUT, "Logout", 999, this.logoutTarget));
    }

    private JButton navButton(IconType type, final String label, int y, String target) {
        final JButton btn = new JButton(label, new NavIcon(type)) {
            {
                Objects.requireNonNull(CustomerSidebar.this);
            }

            protected void paintComponent(Graphics g) {
                if (label.equals(CustomerSidebar.this.activeTab) || label.isEmpty() && CustomerSidebar.this.expanded) {
                    Graphics2D g2 = (Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 40));
                    g2.fillRoundRect(5, 0, this.getWidth() - 10, this.getHeight(), 12, 12);
                    g2.dispose();
                }

                super.paintComponent(g);
            }
        };
        btn.putClientProperty("label", label);
        btn.putClientProperty("y", y);
        btn.setBounds(0, y, 70, 44);
        btn.setHorizontalAlignment(0);
        btn.setForeground(Color.WHITE);
        btn.setFont(UiTheme.BODY_FONT.deriveFont(1, 14.0F));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(12));
        btn.setIconTextGap(15);
        btn.addActionListener((e) -> {
            if ("toggle".equals(target)) {
                this.toggleSidebar();
            } else {
                if (!target.equals(this.logoutTarget)) {
                    this.activeTab = label;
                    this.repaint();
                }

                this.navigator.accept(target);
            }

        });
        btn.addMouseListener(new MouseAdapter() {
            {
                Objects.requireNonNull(CustomerSidebar.this);
            }

            public void mouseEntered(MouseEvent e) {
                btn.setForeground(UiTheme.ORANGE);
            }

            public void mouseExited(MouseEvent e) {
                btn.setForeground(Color.WHITE);
            }
        });
        return btn;
    }

    public void doLayout() {
        boolean wide = this.getWidth() > 100;

        for(Component c : this.getComponents()) {
            if (c instanceof SmallSidebarLogo logo) {
                logo.setVisible(wide);
                logo.setBounds(20, 60, 160, 40);
            } else if (c instanceof JButton btn) {
                String label = (String)btn.getClientProperty("label");
                int y = (Integer)btn.getClientProperty("y");
                if ("Logout".equals(label)) {
                    y = this.getHeight() - 80;
                }

                btn.setText(wide ? label : "");
                btn.setHorizontalAlignment(wide ? 2 : 0);
                btn.setBounds(wide ? 10 : 0, y, wide ? this.getWidth() - 20 : this.getWidth(), 44);
            }
        }

    }

    private void toggleSidebar() {
        if (this.timer == null || !this.timer.isRunning()) {
            this.expanded = !this.expanded;
            int target = this.expanded ? 200 : 70;
            this.timer = new Timer(10, (ActionListener)null);
            this.timer.addActionListener((e) -> {
                int w = this.getPreferredSize().width;
                if (w == target) {
                    this.timer.stop();
                } else {
                    int step = Math.max(1, Math.abs(target - w) / 4);
                    this.setPreferredSize(new Dimension(w + (target > w ? step : -step), this.getHeight()));
                    this.revalidate();
                    if (this.getParent() != null) {
                        this.getParent().repaint();
                    }

                }
            });
            this.timer.start();
        }
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setColor(UiTheme.BLUE);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2.dispose();
    }

    private static enum IconType {
        MENU,
        HOME,
        PROFILE,
        CALENDAR,
        PETS,
        PAYMENTS,
        SETTINGS,
        LOGOUT;
    }

    private static class SmallSidebarLogo extends JPanel {
        SmallSidebarLogo() {
            this.setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(UiTheme.LOGO_FONT.deriveFont(22.0F));
            g2.setColor(Color.WHITE);
            g2.drawString("Care", 0, 20);
            g2.setColor(UiTheme.ORANGE);
            g2.drawString("Haven", g2.getFontMetrics().stringWidth("Care ") - 5, 20);
            g2.dispose();
        }
    }

    private static class NavIcon implements Icon {
        private final IconType type;

        NavIcon(IconType type) {
            this.type = type;
        }

        public int getIconWidth() {
            return 24;
        }

        public int getIconHeight() {
            return 24;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getForeground());
            g2.setStroke(new BasicStroke(2.0F, 1, 1));
            g2.translate(x, y);
            switch (this.type.ordinal()) {
                case 0:
                    g2.drawLine(4, 6, 20, 6);
                    g2.drawLine(4, 12, 20, 12);
                    g2.drawLine(4, 18, 20, 18);
                    break;
                case 1:
                    g2.drawPolygon(new int[]{12, 3, 21}, new int[]{3, 11, 11}, 3);
                    g2.drawRect(7, 11, 10, 10);
                    g2.drawRect(9, 15, 6, 6);
                    break;
                case 2:
                    g2.drawOval(7, 4, 10, 10);
                    g2.drawArc(4, 15, 16, 8, 0, 180);
                    break;
                case 3:
                    g2.drawRoundRect(4, 6, 16, 14, 2, 2);
                    g2.drawLine(4, 10, 20, 10);
                    g2.drawLine(8, 4, 8, 8);
                    g2.drawLine(16, 4, 16, 8);
                    break;
                case 4:
                    g2.fillOval(11, 4, 4, 4);
                    g2.fillOval(5, 7, 3, 3);
                    g2.fillOval(16, 7, 3, 3);
                    g2.fillOval(7, 10, 3, 4);
                    g2.fillOval(14, 10, 3, 4);
                    g2.fillOval(9, 13, 6, 6);
                    break;
                case 5:
                    g2.drawRect(5, 5, 14, 16);
                    g2.drawLine(8, 10, 16, 10);
                    g2.drawLine(8, 15, 14, 15);
                    break;
                case 6:
                    g2.drawOval(8, 8, 8, 8);
                    g2.drawOval(4, 4, 16, 16);
                    break;
                case 7:
                    g2.drawArc(4, 4, 16, 16, 135, 270);
                    g2.drawLine(12, 2, 12, 12);
            }

            g2.dispose();
        }
    }
}
