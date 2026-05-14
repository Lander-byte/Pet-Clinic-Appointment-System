package PetClinic.ui.screens.customer;

import PetClinic.model.scheduling.Appointment;
import PetClinic.model.user.Owner;
import PetClinic.model.user.User;
import PetClinic.service.ClinicService;
import PetClinic.ui.components.RoundedPanel;
import PetClinic.ui.components.UiTheme;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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

public class CustomerAppointmentScreen extends JPanel {
    private final Owner owner;
    private final ClinicService clinicService;
    private final List<Appointment> visibleAppointments = new ArrayList<>();
    private DefaultTableModel model;
    private JTable table;

    public CustomerAppointmentScreen(User user, ClinicService clinicService) {
        this.owner = user instanceof Owner ? (Owner) user : null;
        this.clinicService = clinicService;

        setLayout(new BorderLayout(0, 28));
        setBackground(UiTheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTableCard(), BorderLayout.CENTER);
        refreshTable();
        new Timer(1500, e -> refreshTable()).start();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titles = new JPanel(new GridLayout(2, 1, 0, 4));
        titles.setOpaque(false);
        JLabel title = new JLabel("Appointment History");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UiTheme.TEXT_BLUE);

        JLabel sub = new JLabel("Review previous appointment requests and admin status updates");
        sub.setFont(UiTheme.BODY_FONT);
        sub.setForeground(UiTheme.TEXT_GRAY);

        titles.add(title);
        titles.add(sub);
        header.add(titles, BorderLayout.WEST);

        return header;
    }

    private JPanel buildTableCard() {
        RoundedPanel card = new RoundedPanel(Color.WHITE, 20);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] cols = {"Date", "Time", "Pet", "Reason", "Vet", "Status"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(50);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(UiTheme.BODY_FONT);
        table.setSelectionBackground(UiTheme.LIGHT_BLUE);
        table.getTableHeader().setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD));
        table.getTableHeader().setBackground(new Color(250, 251, 252));
        table.getTableHeader().setForeground(UiTheme.TEXT_GRAY);
        table.getTableHeader().setPreferredSize(new Dimension(0, 44));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.BORDER));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
                setBackground(sel ? UiTheme.LIGHT_BLUE : Color.WHITE);
                if (col == 5) {
                    String status = String.valueOf(v);
                    if ("Confirmed".equals(status)) setForeground(new Color(39, 174, 96));
                    else if ("Completed".equals(status)) setForeground(UiTheme.TEXT_GRAY);
                    else if ("Cancelled".equals(status)) setForeground(new Color(231, 76, 60));
                    else setForeground(UiTheme.ORANGE);
                    setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD, 12f));
                } else {
                    setForeground(UiTheme.TEXT_MAIN);
                    setFont(UiTheme.BODY_FONT);
                }
                return this;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(renderer);

        JPopupMenu popup = new JPopupMenu();
        JMenuItem cancelItem = new JMenuItem("Cancel Request");
        cancelItem.setFont(UiTheme.BODY_FONT.deriveFont(12f));
        cancelItem.setForeground(new Color(231, 76, 60));
        cancelItem.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        cancelItem.addActionListener(e -> cancelSelected());
        popup.add(cancelItem);

        table.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { selectAndMaybeShow(e); }
            @Override public void mouseReleased(MouseEvent e) { selectAndMaybeShow(e); }

            private void selectAndMaybeShow(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) table.setRowSelectionInterval(row, row);
                if (e.isPopupTrigger() && table.getSelectedRow() >= 0) {
                    Appointment appointment = selectedAppointment();
                    cancelItem.setEnabled(appointment != null && !"Completed".equals(appointment.getStatus()));
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private void refreshTable() {
        visibleAppointments.clear();
        model.setRowCount(0);
        if (owner == null) return;
        for (Appointment appointment : clinicService.getAppointmentsForOwner(owner)) {
            visibleAppointments.add(appointment);
            model.addRow(new Object[]{
                    appointment.getDate(),
                    appointment.getTimeslot().getStartTime(),
                    appointment.getPet().getName(),
                    appointment.getReason(),
                    appointment.getVeterinarian().getName(),
                    appointment.getStatus()
            });
        }
    }

    private Appointment selectedAppointment() {
        int row = table.getSelectedRow();
        if (row < 0 || row >= visibleAppointments.size()) return null;
        return visibleAppointments.get(row);
    }

    private void cancelSelected() {
        Appointment appointment = selectedAppointment();
        if (appointment == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "Cancel your appointment request on " + appointment.getDate() + "?",
                "Cancel Appointment",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            clinicService.updateAppointmentStatus(appointment, "Cancelled");
            refreshTable();
        }
    }
}
