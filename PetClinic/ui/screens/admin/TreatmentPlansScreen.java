package PetClinic.ui.screens.admin;

import PetClinic.model.scheduling.Appointment;
import PetClinic.service.ClinicService;
import PetClinic.ui.components.FloatingInput;
import PetClinic.ui.components.RoundedPanel;
import PetClinic.ui.components.UiTheme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class TreatmentPlansScreen extends JPanel {
    private final ClinicService clinicService;
    private final DefaultTableModel model;
    private final JTable table;
    private final List<Appointment> visibleAppointments = new ArrayList<>();

    public TreatmentPlansScreen(ClinicService clinicService) {
        this.clinicService = clinicService;
        setLayout(new BorderLayout(0, 30));
        setBackground(UiTheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel titles = new JPanel(new GridLayout(2, 1));
        titles.setOpaque(false);
        JLabel title = new JLabel("Treatment Plans");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UiTheme.TEXT_BLUE);
        JLabel subtitle = new JLabel("Create clinical plans for confirmed appointments");
        subtitle.setFont(UiTheme.BODY_FONT);
        subtitle.setForeground(UiTheme.TEXT_GRAY);
        titles.add(title);
        titles.add(subtitle);
        header.add(titles, BorderLayout.WEST);

        JButton create = UiTheme.pillButton("Create Plan", UiTheme.ORANGE, Color.WHITE, 13);
        create.setPreferredSize(new Dimension(150, 45));
        create.addActionListener(e -> showCreateDialog());
        header.add(create, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        String[] columns = {"Date", "Time", "Owner", "Pet", "Reason", "Status", "Plan"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(46);
        table.setFont(UiTheme.BODY_FONT);
        table.getTableHeader().setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD));

        JPanel card = new RoundedPanel(Color.WHITE, 22);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        refreshTable();
        new Timer(1500, e -> refreshTable()).start();
    }

    private void refreshTable() {
        visibleAppointments.clear();
        model.setRowCount(0);
        for (Appointment appointment : clinicService.getAppointments()) {
            if (!"Confirmed".equals(appointment.getStatus()) && !"Completed".equals(appointment.getStatus())) continue;
            visibleAppointments.add(appointment);
            model.addRow(new Object[]{
                    appointment.getDate(),
                    appointment.getTimeslot().getStartTime(),
                    appointment.getPet().getOwner().getName(),
                    appointment.getPet().getName(),
                    appointment.getReason(),
                    appointment.getStatus(),
                    appointment.getTreatmentPlan() == null ? "None" : "Created"
            });
        }
    }

    private Appointment selectedAppointment() {
        int row = table.getSelectedRow();
        if (row < 0 || row >= visibleAppointments.size()) return null;
        return visibleAppointments.get(row);
    }

    private void showCreateDialog() {
        Appointment appointment = selectedAppointment();
        if (appointment == null) {
            JOptionPane.showMessageDialog(this, "Select a confirmed appointment first.", "Treatment Plan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 12));
        form.setPreferredSize(new Dimension(360, 390));
        FloatingInput condition = new FloatingInput("Diagnosis / Condition", false);
        FloatingInput notes = new FloatingInput("Clinical Notes", false);
        FloatingInput medication = new FloatingInput("Medication", false);
        FloatingInput dosage = new FloatingInput("Dosage", false);
        FloatingInput duration = new FloatingInput("Duration Days", false);
        FloatingInput procedure = new FloatingInput("Procedure", false);
        FloatingInput procedureNotes = new FloatingInput("Procedure Notes", false);
        form.add(condition);
        form.add(notes);
        form.add(medication);
        form.add(dosage);
        form.add(duration);
        form.add(procedure);
        form.add(procedureNotes);

        int result = JOptionPane.showConfirmDialog(this, form, "Create Treatment Plan", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        try {
            int days = duration.getText().trim().isEmpty() ? 1 : Integer.parseInt(duration.getText().trim());
            clinicService.createTreatmentPlan(appointment, condition.getText(), notes.getText(), medication.getText(), dosage.getText(), days, procedure.getText(), procedureNotes.getText());
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Duration must be a number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
