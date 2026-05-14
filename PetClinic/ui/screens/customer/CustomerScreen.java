package ui.screens.customer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.user.Owner;
import model.user.UserAccountStore;
import ui.components.FloatingInput;
import ui.components.RoundedPanel;
import ui.components.UiTheme;

public class CustomersScreen extends JPanel {
    private final DefaultTableModel model;
    private final UserAccountStore accountStore;

    public CustomersScreen(UserAccountStore accountStore) {
        this.accountStore = accountStore;
        this.setLayout(new BorderLayout(0, 30));
        this.setBackground(UiTheme.BG_LIGHT);
        this.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel titleGroup = new JPanel(new GridLayout(2, 1));
        titleGroup.setOpaque(false);
        JLabel title = new JLabel("Customer Directory");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(1, 28.0F));
        title.setForeground(UiTheme.TEXT_BLUE);
        JLabel subtitle = new JLabel("View and manage your registered clients");
        subtitle.setFont(UiTheme.BODY_FONT);
        subtitle.setForeground(UiTheme.TEXT_GRAY);
        titleGroup.add(title);
        titleGroup.add(subtitle);
        header.add(titleGroup, "West");
        JButton addBtn = UiTheme.pillButton("+ Add Customer", UiTheme.BLUE, Color.WHITE, 13);
        addBtn.setPreferredSize(new Dimension(160, 45));
        addBtn.addActionListener((e) -> this.showAddCustomerDialog());
        header.add(addBtn, "East");
        this.add(header, "North");
        JPanel tableContainer = new RoundedPanel(Color.WHITE, 25);
        tableContainer.setLayout(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        String[] columns = new String[]{"ID", "Name", "Email", "Phone", "Status"};
        this.model = new DefaultTableModel(columns, 0) {
            {
                Objects.requireNonNull(CustomersScreen.this);
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.refreshTable();
        JTable table = new JTable(this.model);
        table.setRowHeight(50);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(UiTheme.BODY_FONT);
        table.getTableHeader().setFont(UiTheme.BODY_FONT.deriveFont(1));
        table.getTableHeader().setBackground(new Color(250, 251, 252));
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.BORDER));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            {
                Objects.requireNonNull(CustomersScreen.this);
            }

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                this.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
                if (column == 4) {
                    this.setForeground(new Color(46, 204, 113));
                    this.setFont(UiTheme.BODY_FONT.deriveFont(1));
                } else {
                    this.setForeground(UiTheme.TEXT_MAIN);
                }

                this.setBackground(isSelected ? UiTheme.LIGHT_BLUE : Color.WHITE);
                return c;
            }
        };

        for(int i = 0; i < table.getColumnCount(); ++i) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder((Border)null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableContainer.add(scrollPane, "Center");
        this.add(tableContainer, "Center");
    }

    private void refreshTable() {
        this.model.setRowCount(0);

        for(Owner owner : this.accountStore.getCustomers()) {
            this.model.addRow(new Object[]{"#" + owner.getUserId(), owner.getName(), owner.getEmail(), owner.getPhone(), "Active"});
        }

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
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Customer", 2, -1);
        if (result == 0) {
            try {
                if (name.getText().trim().length() < 2) {
                    throw new IllegalArgumentException("Customer name must be at least 2 characters.");
                }

                if (!phone.getText().trim().isEmpty() && !phone.getText().trim().matches("[0-9+()\\-\\s]{7,20}")) {
                    throw new IllegalArgumentException("Please enter a valid phone number.");
                }

                Owner owner = this.accountStore.registerCustomer(name.getText(), email.getText(), "password123");
                owner.setName(name.getText().trim());
                owner.setPhone(phone.getText().trim());
                this.refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", 0);
            }
        }

    }
}
