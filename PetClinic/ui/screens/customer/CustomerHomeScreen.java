package PetClinic.ui.screens;

import PetClinic.ui.components.RoundedPanel;
import PetClinic.ui.components.UiTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Home screen for logged-in customers.
 * Shows a welcome banner, quick stats, and their upcoming appointments.
 */
public class CustomerHomeScreen extends JPanel {

    public CustomerHomeScreen() {
        setLayout(new BorderLayout(0, 28));
        setBackground(UiTheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
    }

    // ── Header ─────────────────────────────────────────────────────────

    private JPanel buildHeader() {
        RoundedPanel banner = new RoundedPanel(UiTheme.BLUE, 20);
        banner.setLayout(new BorderLayout(20, 0));
        banner.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));
        banner.setPreferredSize(new Dimension(0, 110));

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 6));
        text.setOpaque(false);

        JLabel title = new JLabel("Welcome back!");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 26f));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Here's a summary of your upcoming vet appointments.");
        sub.setFont(UiTheme.BODY_FONT.deriveFont(13f));
        sub.setForeground(new Color(255, 255, 255, 200));

        text.add(title);
        text.add(sub);
        banner.add(text, BorderLayout.CENTER);

        JButton bookBtn = UiTheme.pillButton("+ Book Appointment", UiTheme.ORANGE, Color.WHITE, 12);
        bookBtn.setPreferredSize(new Dimension(180, 42));
        banner.add(bookBtn, BorderLayout.EAST);

        return banner;
    }

    // ── Center ─────────────────────────────────────────────────────────

    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 28));
        center.setOpaque(false);

        // Stats row
        JPanel stats = new JPanel(new GridLayout(1, 3, 20, 0));
        stats.setOpaque(false);
        stats.setPreferredSize(new Dimension(0, 90));
        stats.add(statCard("Upcoming",    "2",  UiTheme.BLUE));
        stats.add(statCard("Completed",   "5",  new Color(39, 174, 96)));
        stats.add(statCard("Registered Pets", "2", UiTheme.ORANGE));
        center.add(stats, BorderLayout.NORTH);

        // Upcoming appointments table
        center.add(buildAppointmentTable(), BorderLayout.CENTER);

        return center;
    }

    private JPanel statCard(String label, String value, Color color) {
        RoundedPanel card = new RoundedPanel(Color.WHITE, 18);
        card.setLayout(new BorderLayout(10, 0));
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JPanel stripe = new JPanel();
        stripe.setBackground(color);
        stripe.setPreferredSize(new Dimension(5, 0));
        card.add(stripe, BorderLayout.WEST);

        JPanel info = new JPanel(new GridLayout(2, 1, 0, 2));
        info.setOpaque(false);

        JLabel val = new JLabel(value);
        val.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 24f));
        val.setForeground(color);

        JLabel lbl = new JLabel(label);
        lbl.setFont(UiTheme.BODY_FONT.deriveFont(11f));
        lbl.setForeground(UiTheme.TEXT_GRAY);

        info.add(val);
        info.add(lbl);
        card.add(info, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildAppointmentTable() {
        RoundedPanel card = new RoundedPanel(Color.WHITE, 20);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JLabel title = new JLabel("Your Upcoming Appointments");
        title.setFont(UiTheme.SUBTITLE_FONT);
        title.setForeground(UiTheme.TEXT_MAIN);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        card.add(title, BorderLayout.NORTH);

        String[] cols = {"Date", "Time", "Pet", "Reason", "Vet", "Status"};
        Object[][] data = {
            {"2025-06-10", "09:00 AM", "Buddy",  "Annual Checkup",  "Dr. Smith", "Confirmed"},
            {"2025-06-18", "02:30 PM", "Misty",  "Vaccination",     "Dr. Smith", "Pending"},
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        table.setRowHeight(48);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(UiTheme.BODY_FONT);
        table.getTableHeader().setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD));
        table.getTableHeader().setBackground(new Color(250, 251, 252));
        table.getTableHeader().setForeground(UiTheme.TEXT_GRAY);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.BORDER));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                setBackground(sel ? UiTheme.LIGHT_BLUE : Color.WHITE);
                if (col == 5) {
                    String s = String.valueOf(v);
                    setForeground("Confirmed".equals(s) ? new Color(39,174,96) : UiTheme.ORANGE);
                    setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD, 12f));
                } else {
                    setForeground(UiTheme.TEXT_MAIN);
                    setFont(UiTheme.BODY_FONT);
                }
                return this;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }
}
