package PetClinic.ui.screens;

import PetClinic.ui.components.RoundedPanel;
import PetClinic.ui.components.UiTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserDashboardOverview extends JPanel {
    public UserDashboardOverview() {
        setLayout(new BorderLayout(0, 30));
        setBackground(UiTheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UiTheme.TEXT_BLUE);
        header.add(title, BorderLayout.NORTH);
        
        JLabel subtitle = new JLabel("Welcome back! Here's what's happening today.");
        subtitle.setFont(UiTheme.BODY_FONT);
        subtitle.setForeground(UiTheme.TEXT_GRAY);
        header.add(subtitle, BorderLayout.SOUTH);
        
        add(header, BorderLayout.NORTH);

        // Center Content
        JPanel centerPanel = new JPanel(new BorderLayout(0, 35));
        centerPanel.setOpaque(false);

        // Stats Cards Row
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        statsPanel.setOpaque(false);
        statsPanel.add(createStatCard("Total Appointments", "12", UiTheme.BLUE, "↑ 2 today"));
        statsPanel.add(createStatCard("Total Customers", "45", UiTheme.ORANGE, "↑ 5 this week"));
        statsPanel.add(createStatCard("Pending Requests", "5", new Color(46, 204, 113), "Requires action"));
        centerPanel.add(statsPanel, BorderLayout.NORTH);

        // Recent Appointments List
        JPanel listPanel = new RoundedPanel(Color.WHITE, 25);
        listPanel.setLayout(new BorderLayout());
        listPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        JLabel listTitle = new JLabel("Upcoming Appointments Today");
        listTitle.setFont(UiTheme.SUBTITLE_FONT);
        listTitle.setForeground(UiTheme.TEXT_MAIN);
        listTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        listPanel.add(listTitle, BorderLayout.NORTH);

        String[] cols = {"Time", "Customer", "Pet", "Status"};
        Object[][] data = {
            {"09:00 AM", "John Doe", "Buddy", "Confirmed"},
            {"10:30 AM", "Jane Smith", "Misty", "Confirmed"},
            {"02:00 PM", "Mike Ross", "Harvey", "Pending"},
            {"04:15 PM", "Sarah Conn", "Termi", "Confirmed"}
        };
        
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        JTable table = new JTable(model);
        table.setRowHeight(45);
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
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                if (column == 3) { // Status coloring
                    String status = (String) value;
                    if ("Confirmed".equals(status)) setForeground(new Color(46, 204, 113));
                    else if ("Pending".equals(status)) setForeground(UiTheme.ORANGE);
                } else {
                    setForeground(UiTheme.TEXT_MAIN);
                }
                setBackground(isSelected ? UiTheme.LIGHT_BLUE : Color.WHITE);
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        listPanel.add(scroll, BorderLayout.CENTER);

        centerPanel.add(listPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String label, String value, Color color, String trend) {
        RoundedPanel card = new RoundedPanel(color, 25);
        card.setLayout(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(UiTheme.TITLE_FONT.deriveFont(42f));
        lblValue.setForeground(Color.WHITE);

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        
        JLabel lblTitle = new JLabel(label);
        lblTitle.setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD, 14f));
        lblTitle.setForeground(new Color(255, 255, 255, 210));
        
        JLabel lblTrend = new JLabel(trend);
        lblTrend.setFont(UiTheme.BODY_FONT.deriveFont(11f));
        lblTrend.setForeground(new Color(255, 255, 255, 160));
        
        info.add(lblTitle);
        info.add(lblTrend);

        card.add(lblValue, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);

        return card;
    }
}
 
