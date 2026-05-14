package PetClinic.ui.screens.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import PetClinic.model.billing.Invoice;
import PetClinic.model.scheduling.Appointment;
import PetClinic.model.user.User;
import PetClinic.service.ClinicService;
import PetClinic.ui.components.RoundedPanel;
import PetClinic.ui.components.UiTheme;

public class DashboardOverview extends JPanel {
    private final ClinicService clinicService;
    private final JPanel statsPanel = new JPanel(new GridLayout(1, 3, 25, 0));
    private final DefaultTableModel model;

    public DashboardOverview(User user, ClinicService clinicService) {
        this.clinicService = clinicService;
        this.setLayout(new BorderLayout(0, 30));
        this.setBackground(UiTheme.BG_LIGHT);
        this.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(1, 28.0F));
        title.setForeground(UiTheme.TEXT_BLUE);
        header.add(title, "North");
        String welcomeMsg = user != null ? "Welcome back, " + user.getName() + "." : "Welcome back.";
        JLabel subtitle = new JLabel(welcomeMsg);
        subtitle.setFont(UiTheme.BODY_FONT);
        subtitle.setForeground(UiTheme.TEXT_GRAY);
        header.add(subtitle, "South");
        this.add(header, "North");
        JPanel centerPanel = new JPanel(new BorderLayout(0, 35));
        centerPanel.setOpaque(false);
        this.statsPanel.setOpaque(false);
        centerPanel.add(this.statsPanel, "North");
        JPanel listPanel = new RoundedPanel(Color.WHITE, 25);
        listPanel.setLayout(new BorderLayout());
        listPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        JLabel listTitle = new JLabel("Latest Appointment Requests");
        listTitle.setFont(UiTheme.SUBTITLE_FONT);
        listTitle.setForeground(UiTheme.TEXT_MAIN);
        listTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        listPanel.add(listTitle, "North");
        this.model = new DefaultTableModel(new String[]{"Date", "Time", "Customer", "Pet", "Status"}, 0) {
            {
                Objects.requireNonNull(DashboardOverview.this);
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(this.model);
        table.setRowHeight(45);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(UiTheme.BODY_FONT);
        table.getTableHeader().setFont(UiTheme.BODY_FONT.deriveFont(1));
        table.getTableHeader().setBackground(new Color(250, 251, 252));
        table.getTableHeader().setForeground(UiTheme.TEXT_GRAY);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            {
                Objects.requireNonNull(DashboardOverview.this);
            }

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                this.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                if (column == 4) {
                    String status = String.valueOf(value);
                    if ("Confirmed".equals(status)) {
                        this.setForeground(new Color(46, 204, 113));
                    } else if (!"Cancelled".equals(status) && !"Rejected".equals(status)) {
                        this.setForeground(UiTheme.ORANGE);
                    } else {
                        this.setForeground(Color.RED);
                    }

                    this.setFont(UiTheme.BODY_FONT.deriveFont(1));
                } else {
                    this.setForeground(UiTheme.TEXT_MAIN);
                    this.setFont(UiTheme.BODY_FONT);
                }

                this.setBackground(isSelected ? UiTheme.LIGHT_BLUE : Color.WHITE);
                return this;
            }
        };

        for(int i = 0; i < table.getColumnCount(); ++i) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder((Border)null);
        scroll.getViewport().setBackground(Color.WHITE);
        listPanel.add(scroll, "Center");
        centerPanel.add(listPanel, "Center");
        this.add(centerPanel, "Center");
        this.refresh();
        (new Timer(1500, (e) -> this.refresh())).start();
    }

    private void refresh() {
        int total = this.clinicService.getAppointments().size();
        int pending = 0;
        int unpaid = 0;

        for(Appointment appointment : this.clinicService.getAppointments()) {
            if ("Pending".equals(appointment.getStatus())) {
                ++pending;
            }
        }

        for(Invoice invoice : this.clinicService.getInvoices()) {
            if (!invoice.isPaid()) {
                ++unpaid;
            }
        }

        this.statsPanel.removeAll();
        this.statsPanel.add(this.createStatCard("Appointments", String.valueOf(total), UiTheme.BLUE, "All requests"));
        this.statsPanel.add(this.createStatCard("Pending", String.valueOf(pending), UiTheme.ORANGE, "Needs review"));
        this.statsPanel.add(this.createStatCard("Unpaid Invoices", String.valueOf(unpaid), new Color(46, 204, 113), "Billing"));
        this.statsPanel.revalidate();
        this.statsPanel.repaint();
        this.model.setRowCount(0);

        for(Appointment appointment : this.clinicService.getAppointments()) {
            this.model.addRow(new Object[]{appointment.getDate(), appointment.getTimeslot().getStartTime(), appointment.getPet().getOwner().getName(), appointment.getPet().getName(), appointment.getStatus()});
        }

    }

    private JPanel createStatCard(String label, String value, Color color, String trend) {
        RoundedPanel card = new RoundedPanel(color, 25);
        card.setLayout(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(UiTheme.TITLE_FONT.deriveFont(42.0F));
        lblValue.setForeground(Color.WHITE);
        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        JLabel lblTitle = new JLabel(label);
        lblTitle.setFont(UiTheme.BODY_FONT.deriveFont(1, 14.0F));
        lblTitle.setForeground(new Color(255, 255, 255, 210));
        JLabel lblTrend = new JLabel(trend);
        lblTrend.setFont(UiTheme.BODY_FONT.deriveFont(11.0F));
        lblTrend.setForeground(new Color(255, 255, 255, 160));
        info.add(lblTitle);
        info.add(lblTrend);
        card.add(lblValue, "West");
        card.add(info, "Center");
        return card;
    }
}
