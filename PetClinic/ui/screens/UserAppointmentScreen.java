package ui.screens;

import ui.components.FloatingInput;
import ui.components.RoundedPanel;
import ui.components.UiTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AppointmentsScreen extends JPanel {
    private final DefaultTableModel model;
    private final JTable table;

    public AppointmentsScreen() {
        setLayout(new BorderLayout(0, 30));
        setBackground(UiTheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JPanel titleGroup = new JPanel(new GridLayout(2, 1));
        titleGroup.setOpaque(false);
        JLabel title = new JLabel("Appointments");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UiTheme.TEXT_BLUE);
        JLabel subtitle = new JLabel("Manage and schedule your clinic sessions");
        subtitle.setFont(UiTheme.BODY_FONT);
        subtitle.setForeground(UiTheme.TEXT_GRAY);
        titleGroup.add(title);
        titleGroup.add(subtitle);
        header.add(titleGroup, BorderLayout.WEST);

        JButton addBtn = UiTheme.pillButton("+ New Appointment", UiTheme.ORANGE, Color.WHITE, 13);
        addBtn.setPreferredSize(new Dimension(180, 45));
        addBtn.addActionListener(e -> showAddAppointmentDialog());
        header.add(addBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Content Table Container
        JPanel tableContainer = new RoundedPanel(Color.WHITE, 25);
        tableContainer.setLayout(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Date", "Time", "Customer", "Pet", "Reason", "Status"};
        Object[][] data = {
            {"2023-11-01", "09:00 AM", "John Doe", "Buddy", "Checkup", "Confirmed"},
            {"2023-11-01", "10:30 AM", "Jane Smith", "Misty", "Vaccination", "Pending"},
            {"2023-11-02", "02:00 PM", "Mike Ross", "Harvey", "Surgery", "Confirmed"}
        };

        model = new DefaultTableModel(data, columns) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(50);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(UiTheme.BODY_FONT);
        table.getTableHeader().setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD));
        table.getTableHeader().setBackground(new Color(250, 251, 252));
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.BORDER));

        // Right-click menu for actions
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBorder(BorderFactory.createLineBorder(UiTheme.BORDER));
        JMenuItem confirmItem = new JMenuItem("Set as Confirmed");
        JMenuItem cancelItem = new JMenuItem("Set as Cancelled");
        JMenuItem deleteItem = new JMenuItem("Delete");
        
        confirmItem.addActionListener(e -> updateStatus("Confirmed"));
        cancelItem.addActionListener(e -> updateStatus("Cancelled"));
        deleteItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) model.removeRow(row);
        });

        popupMenu.add(confirmItem);
        popupMenu.add(cancelItem);
        popupMenu.addSeparator();
        popupMenu.add(deleteItem);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row != -1) table.setRowSelectionInterval(row, row);
                if (e.isPopupTrigger()) popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
                if (column == 5) { // Status
                    String status = (String) value;
                    if ("Confirmed".equals(status)) setForeground(new Color(46, 204, 113));
                    else if ("Cancelled".equals(status)) setForeground(Color.RED);
                    else setForeground(UiTheme.ORANGE);
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

    private void updateStatus(String status) {
        int row = table.getSelectedRow();
        if (row != -1) model.setValueAt(status, row, 5);
    }

    private void showAddAppointmentDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 15, 15));
        panel.setPreferredSize(new Dimension(350, 300));
        FloatingInput customer = new FloatingInput("Customer Name", false);
        FloatingInput pet = new FloatingInput("Pet Name", false);
        FloatingInput date = new FloatingInput("Date (MM/DD/YYYY)", false);
        FloatingInput reason = new FloatingInput("Reason", false);
        panel.add(customer); panel.add(pet); panel.add(date); panel.add(reason);

        int result = JOptionPane.showConfirmDialog(this, panel, "Schedule New Appointment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            model.addRow(new Object[]{date.getText(), "10:00 AM", customer.getText(), pet.getText(), reason.getText(), "Pending"});
        }
    }
}
 
