package ui.screens.admin;

import model.scheduling.Appointment;
import model.user.User;
import service.ClinicService;
import ui.components.RoundedPanel;
import ui.components.UiTheme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class AppointmentsScreen extends JPanel {
    private final ClinicService clinicService;
    private final User user;
    private final DefaultTableModel model;
    private final JTable table;
    private final List<Appointment> visibleAppointments = new ArrayList<>();

    public AppointmentsScreen(ClinicService clinicService, User user) {
        this.clinicService = clinicService;
        this.user = user;

        setLayout(new BorderLayout(0, 30));
        setBackground(UiTheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titleGroup = new JPanel(new GridLayout(2, 1));
        titleGroup.setOpaque(false);
        JLabel title = new JLabel(clinicService.isVeterinarian(user) ? "Veterinarian Schedule" : "Staff Schedule");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UiTheme.TEXT_BLUE);
        JLabel subtitle = new JLabel("View and update clinic appointment requests");
        subtitle.setFont(UiTheme.BODY_FONT);
        subtitle.setForeground(UiTheme.TEXT_GRAY);
        titleGroup.add(title);
        titleGroup.add(subtitle);
        header.add(titleGroup, BorderLayout.WEST);

        JButton refreshButton = UiTheme.pillButton("Refresh", UiTheme.BLUE, Color.WHITE, 13);
        refreshButton.setPreferredSize(new Dimension(120, 45));
        refreshButton.addActionListener(e -> refreshTable());
        header.add(refreshButton, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel tableContainer = new RoundedPanel(Color.WHITE, 25);
        tableContainer.setLayout(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Date", "Time", "Customer", "Pet", "Reason", "Status"};
        model = new DefaultTableModel(columns, 0) {
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

        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBorder(BorderFactory.createLineBorder(UiTheme.BORDER));
        JMenuItem confirmItem = new JMenuItem("Confirm Appointment");
        JMenuItem rejectItem = new JMenuItem("Reject Request");
        JMenuItem completeItem = new JMenuItem("Mark Completed");
        JMenuItem cancelItem = new JMenuItem("Cancel Appointment");
        JMenuItem deleteItem = new JMenuItem("Delete Request");

        confirmItem.addActionListener(e -> updateSelectedStatus("Confirmed"));
        rejectItem.addActionListener(e -> updateSelectedStatus("Rejected"));
        completeItem.addActionListener(e -> updateSelectedStatus("Completed"));
        cancelItem.addActionListener(e -> updateSelectedStatus("Cancelled"));
        deleteItem.addActionListener(e -> deleteSelectedAppointment());

        popupMenu.add(confirmItem);
        popupMenu.add(rejectItem);
        popupMenu.add(completeItem);
        popupMenu.add(cancelItem);
        popupMenu.addSeparator();
        popupMenu.add(deleteItem);

        table.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { selectAndMaybeShow(e); }
            @Override public void mouseReleased(MouseEvent e) { selectAndMaybeShow(e); }

            private void selectAndMaybeShow(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row != -1) table.setRowSelectionInterval(row, row);
                if (e.isPopupTrigger() && table.getSelectedRow() != -1) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
                if (column == 5) {
                    String status = String.valueOf(value);
                    if ("Confirmed".equals(status)) setForeground(new Color(46, 204, 113));
                    else if ("Completed".equals(status)) setForeground(UiTheme.TEXT_GRAY);
                    else if ("Cancelled".equals(status) || "Rejected".equals(status)) setForeground(Color.RED);
                    else setForeground(UiTheme.ORANGE);
                    setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD));
                } else {
                    setForeground(UiTheme.TEXT_MAIN);
                    setFont(UiTheme.BODY_FONT);
                }
                setHorizontalAlignment(column == 5 ? SwingConstants.CENTER : SwingConstants.LEFT);
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

        refreshTable();
        new Timer(1500, e -> refreshTable()).start();
    }

    private void refreshTable() {
        visibleAppointments.clear();
        model.setRowCount(0);
        for (Appointment appointment : clinicService.getAppointments()) {
            visibleAppointments.add(appointment);
            model.addRow(new Object[]{
                    appointment.getDate(),
                    appointment.getTimeslot().getStartTime(),
                    appointment.getPet().getOwner().getName(),
                    appointment.getPet().getName(),
                    appointment.getReason(),
                    appointment.getStatus()
            });
        }
    }

    private Appointment selectedAppointment() {
        int row = table.getSelectedRow();
        if (row < 0 || row >= visibleAppointments.size()) return null;
        return visibleAppointments.get(row);
    }

    private void updateSelectedStatus(String status) {
        Appointment appointment = selectedAppointment();
        if (appointment == null) return;
        clinicService.updateAppointmentStatus(appointment, status);
        refreshTable();
    }

    private void deleteSelectedAppointment() {
        Appointment appointment = selectedAppointment();
        if (appointment == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this appointment request?",
                "Delete Appointment",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            clinicService.deleteAppointment(appointment);
            refreshTable();
        }
    }
}
