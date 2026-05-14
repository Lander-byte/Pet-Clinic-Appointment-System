package PetClinic.ui.screens.admin;

import PetClinic.model.billing.Invoice;
import PetClinic.model.billing.Service;
import PetClinic.model.scheduling.Appointment;
import PetClinic.service.ClinicService;
import PetClinic.ui.components.RoundedPanel;
import PetClinic.ui.components.UiTheme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class BillingScreen extends JPanel {
    private final ClinicService clinicService;
    private final DefaultTableModel appointmentModel;
    private final DefaultTableModel invoiceModel;
    private final JTable appointmentTable;
    private final List<Appointment> visibleAppointments = new ArrayList<>();

    public BillingScreen(ClinicService clinicService) {
        this.clinicService = clinicService;
        setLayout(new BorderLayout(0, 30));
        setBackground(UiTheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Billing");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UiTheme.TEXT_BLUE);
        header.add(title, BorderLayout.WEST);
        JButton invoiceButton = UiTheme.pillButton("Generate Invoice", UiTheme.ORANGE, Color.WHITE, 13);
        invoiceButton.setPreferredSize(new Dimension(170, 45));
        invoiceButton.addActionListener(e -> showInvoiceDialog());
        header.add(invoiceButton, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(2, 1, 0, 24));
        body.setOpaque(false);

        appointmentModel = new DefaultTableModel(new String[]{"Date", "Time", "Customer", "Pet", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        appointmentTable = new JTable(appointmentModel);
        body.add(tableCard("Completed / Billable Appointments", appointmentTable));

        invoiceModel = new DefaultTableModel(new String[]{"Customer", "Pet", "Service", "Amount", "Status", "Paid Via"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        body.add(tableCard("Invoices", new JTable(invoiceModel)));

        add(body, BorderLayout.CENTER);
        refreshTables();
        new Timer(1500, e -> refreshTables()).start();
    }

    private JPanel tableCard(String title, JTable table) {
        JPanel card = new RoundedPanel(Color.WHITE, 22);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        JLabel label = new JLabel(title);
        label.setFont(UiTheme.SUBTITLE_FONT);
        label.setForeground(UiTheme.TEXT_MAIN);
        card.add(label, BorderLayout.NORTH);
        table.setRowHeight(40);
        table.setFont(UiTheme.BODY_FONT);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        return card;
    }

    private void refreshTables() {
        visibleAppointments.clear();
        appointmentModel.setRowCount(0);
        for (Appointment appointment : clinicService.getAppointments()) {
            if (!"Completed".equals(appointment.getStatus())) continue;
            visibleAppointments.add(appointment);
            appointmentModel.addRow(new Object[]{
                    appointment.getDate(),
                    appointment.getTimeslot().getStartTime(),
                    appointment.getPet().getOwner().getName(),
                    appointment.getPet().getName(),
                    appointment.getStatus()
            });
        }

        invoiceModel.setRowCount(0);
        for (Invoice invoice : clinicService.getInvoices()) {
            Appointment appointment = invoice.getAppointment();
            invoiceModel.addRow(new Object[]{
                    appointment.getPet().getOwner().getName(),
                    appointment.getPet().getName(),
                    invoice.getService().getName(),
                    "PHP " + invoice.getTotalAmount(),
                    invoice.isPaid() ? "Paid" : "Unpaid",
                    invoice.isPaid() ? invoice.getPaymentMethod() : "-"
            });
        }
    }

    private void showInvoiceDialog() {
        int row = appointmentTable.getSelectedRow();
        if (row < 0 || row >= visibleAppointments.size()) {
            JOptionPane.showMessageDialog(this, "Select a completed appointment first.", "Generate Invoice", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<Service> services = clinicService.getServices();
        JComboBox<Service> serviceBox = new JComboBox<>(services.toArray(new Service[0]));
        int result = JOptionPane.showConfirmDialog(this, serviceBox, "Select Service", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;
        clinicService.generateInvoice(visibleAppointments.get(row), (Service) serviceBox.getSelectedItem());
        refreshTables();
    }
}
