package ui.screens;

import ui.components.FloatingInput;
import ui.components.RoundedPanel;
import ui.components.UiTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomersScreen extends JPanel {
    private final DefaultTableModel model;
    private int nextId = 1005;

    public CustomersScreen() {
        setLayout(new BorderLayout(0, 30));
        setBackground(UiTheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JPanel titleGroup = new JPanel(new GridLayout(2, 1));
        titleGroup.setOpaque(false);
        JLabel title = new JLabel("Customer Directory");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UiTheme.TEXT_BLUE);
        JLabel subtitle = new JLabel("View and manage your registered clients");
        subtitle.setFont(UiTheme.BODY_FONT);
        subtitle.setForeground(UiTheme.TEXT_GRAY);
        titleGroup.add(title);
        titleGroup.add(subtitle);
        header.add(titleGroup, BorderLayout.WEST);

        JButton addBtn = UiTheme.pillButton("+ Add Customer", UiTheme.BLUE, Color.WHITE, 13);
        addBtn.setPreferredSize(new Dimension(160, 45));
        addBtn.addActionListener(e -> showAddCustomerDialog());
        header.add(addBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Content Table Container
        JPanel tableContainer = new RoundedPanel(Color.WHITE, 25);
        tableContainer.setLayout(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"ID", "Name", "Email", "Phone", "Total Pets", "Status"};
        Object[][] data = {
            {"#1001", "John Doe", "john@example.com", "09123456789", "2", "Active"},
            {"#1002", "Jane Smith", "jane@gmail.com", "09987654321", "1", "Active"},
            {"#1003", "Mike Ross", "mike.r@outlook.com", "09555444333", "3", "Inactive"},
            {"#1004", "Sarah Conn", "s.conn@company.com", "09111222333", "1", "Active"}
        };

        model = new DefaultTableModel(data, columns) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(model);
        table.setRowHeight(50);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(UiTheme.BODY_FONT);
        table.getTableHeader().setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD));
        table.getTableHeader().setBackground(new Color(250, 251, 252));
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.BORDER));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
                if (column == 5) { // Status
                    String status = (String) value;
                    if ("Active".equals(status)) setForeground(new Color(46, 204, 113));
                    else setForeground(UiTheme.TEXT_GRAY);
                    setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD));
                } else {
                    setForeground(UiTheme.TEXT_MAIN);
                }
                setBackground(isSelected ? UiTheme.LIGHT_BLUE : Color.WHITE);
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(renderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(tableContainer, BorderLayout.CENTER);
    }

    private void showAddCustomerDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 15, 15));
        panel.setPreferredSize(new Dimension(350, 250));
        FloatingInput name = new FloatingInput("Full Name", false);
        FloatingInput email = new FloatingInput("Email Address", false);
        FloatingInput phone = new FloatingInput("Phone Number", false);

        panel.add(name);
        panel.add(email);
        panel.add(phone);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Customer", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            if (name.getText().isEmpty() || email.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Email are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            model.addRow(new Object[]{"#" + (nextId++), name.getText(), email.getText(), phone.getText(), "0", "Active"});
        }
    }
}
 
