package ui.screens.customer;

import model.billing.Invoice;
import model.user.Owner;
import model.user.User;
import service.ClinicService;
import ui.components.RoundedPanel;
import ui.components.UiTheme;

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

public class CustomerBillingScreen extends JPanel {
    private final Owner owner;
    private final ClinicService clinicService;
    private final DefaultTableModel model;
    private final JTable table;
    private final List<Invoice> visibleInvoices = new ArrayList<>();

    public CustomerBillingScreen(User user, ClinicService clinicService) {
        this.owner = user instanceof Owner ? (Owner) user : null;
        this.clinicService = clinicService;

        setLayout(new BorderLayout(0, 28));
        setBackground(UiTheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel titles = new JPanel(new GridLayout(2, 1, 0, 4));
        titles.setOpaque(false);
        JLabel title = new JLabel("Payments");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UiTheme.TEXT_BLUE);
        JLabel subtitle = new JLabel("View invoices and make payments");
        subtitle.setFont(UiTheme.BODY_FONT);
        subtitle.setForeground(UiTheme.TEXT_GRAY);
        titles.add(title);
        titles.add(subtitle);
        header.add(titles, BorderLayout.WEST);
        JButton pay = UiTheme.pillButton("Pay Selected", UiTheme.ORANGE, Color.WHITE, 13);
        pay.setPreferredSize(new Dimension(140, 45));
        pay.addActionListener(e -> paySelected());
        header.add(pay, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Pet", "Service", "Amount", "Status", "Paid Via"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(46);
        table.setFont(UiTheme.BODY_FONT);

        JPanel card = new RoundedPanel(Color.WHITE, 22);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        refreshTable();
        new Timer(1500, e -> refreshTable()).start();
    }

    private void refreshTable() {
        visibleInvoices.clear();
        model.setRowCount(0);
        if (owner == null) return;
        for (Invoice invoice : clinicService.getInvoicesForOwner(owner)) {
            visibleInvoices.add(invoice);
            model.addRow(new Object[]{
                    invoice.getAppointment().getPet().getName(),
                    invoice.getService().getName(),
                    "PHP " + invoice.getTotalAmount(),
                    invoice.isPaid() ? "Paid" : "Unpaid",
                    invoice.isPaid() ? invoice.getPaymentMethod() : "-"
            });
        }
    }

    private void paySelected() {
        int row = table.getSelectedRow();
        if (row < 0 || row >= visibleInvoices.size()) {
            JOptionPane.showMessageDialog(this, "Select an invoice first.", "Payment", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Invoice invoice = visibleInvoices.get(row);
        if (invoice.isPaid()) {
            JOptionPane.showMessageDialog(this, "This invoice is already paid.", "Payment", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JComboBox<String> methods = new JComboBox<>(new String[]{"Cash", "GCash", "Card"});
        int result = JOptionPane.showConfirmDialog(this, methods, "Payment Method", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;
        clinicService.payInvoice(invoice, String.valueOf(methods.getSelectedItem()));
        refreshTable();
    }
}
