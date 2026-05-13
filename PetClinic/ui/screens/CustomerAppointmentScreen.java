package PetClinic.ui.screens;

import PetClinic.ui.components.FloatingInput;
import PetClinic.ui.components.RoundedPanel;
import PetClinic.ui.components.UiTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Appointment screen for CUSTOMERS.
 * Customers can book new appointments and cancel pending ones.
 * They cannot modify status — that is admin/vet only.
 */
public class CustomerAppointmentScreen extends JPanel {

    private DefaultTableModel model;
    private JTable table;

    public CustomerAppointmentScreen() {
        setLayout(new BorderLayout(0, 28));
        setBackground(UiTheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTableCard(), BorderLayout.CENTER);
    }

    // ── Header ─────────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titles = new JPanel(new GridLayout(2, 1, 0, 4));
        titles.setOpaque(false);

        JLabel title = new JLabel("My Appointments");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UiTheme.TEXT_BLUE);

        JLabel sub = new JLabel("Book a new appointment or cancel an existing one");
        sub.setFont(UiTheme.BODY_FONT);
        sub.setForeground(UiTheme.TEXT_GRAY);

        titles.add(title);
        titles.add(sub);
        header.add(titles, BorderLayout.WEST);

        JButton bookBtn = UiTheme.pillButton("+ Book Appointment", UiTheme.ORANGE, Color.WHITE, 13);
        bookBtn.setPreferredSize(new Dimension(190, 44));
        bookBtn.addActionListener(e -> showBookingDialog());
        header.add(bookBtn, BorderLayout.EAST);

        return header;
    }

    // ── Table card ──────────────────────────────────────────────────────

    private JPanel buildTableCard() {
        RoundedPanel card = new RoundedPanel(Color.WHITE, 20);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] cols = {"Date", "Time", "Pet", "Reason", "Vet", "Status"};
        Object[][] data = {
            {"2025-06-10", "09:00 AM", "Buddy", "Annual Checkup", "Dr. Smith", "Confirmed"},
            {"2025-06-18", "02:30 PM", "Misty", "Vaccination",    "Dr. Smith", "Pending"},
            {"2025-05-02", "11:00 AM", "Buddy", "Ear Cleaning",   "Dr. Smith", "Completed"},
        };

        DefaultTableModel mdl = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        // assign to field via local var trick so final table ref works in lambdas
        model = mdl;

        JTable tbl = new JTable(model);
        table = tbl;
        tbl.setRowHeight(50);
        tbl.setShowGrid(false);
        tbl.setIntercellSpacing(new Dimension(0, 0));
        tbl.setFont(UiTheme.BODY_FONT);
        tbl.setSelectionBackground(UiTheme.LIGHT_BLUE);
        tbl.getTableHeader().setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD));
        tbl.getTableHeader().setBackground(new Color(250, 251, 252));
        tbl.getTableHeader().setForeground(UiTheme.TEXT_GRAY);
        tbl.getTableHeader().setPreferredSize(new Dimension(0, 44));
        tbl.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.BORDER));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
                setBackground(sel ? UiTheme.LIGHT_BLUE : Color.WHITE);
                if (col == 5) {
                    String s = String.valueOf(v);
                    setForeground(switch (s) {
                        case "Confirmed"  -> new Color(39, 174, 96);
                        case "Completed"  -> UiTheme.TEXT_GRAY;
                        case "Cancelled"  -> new Color(231, 76, 60);
                        default           -> UiTheme.ORANGE;          // Pending
                    });
                    setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD, 12f));
                } else {
                    setForeground(UiTheme.TEXT_MAIN);
                    setFont(UiTheme.BODY_FONT);
                }
                return this;
            }
        };
        for (int i = 0; i < tbl.getColumnCount(); i++)
            tbl.getColumnModel().getColumn(i).setCellRenderer(renderer);

        // Right-click: customers can only cancel pending appointments
        JPopupMenu popup = new JPopupMenu();
        JMenuItem cancelItem = new JMenuItem("Cancel Appointment");
        cancelItem.setFont(UiTheme.BODY_FONT.deriveFont(12f));
        cancelItem.setForeground(new Color(231, 76, 60));
        cancelItem.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        cancelItem.addActionListener(e -> cancelSelected());
        popup.add(cancelItem);

        tbl.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e)  { selectAndMaybeShow(e); }
            @Override public void mouseReleased(MouseEvent e) { if (e.isPopupTrigger()) tryShowPopup(e, popup); }
            private void selectAndMaybeShow(MouseEvent e) {
                int row = tbl.rowAtPoint(e.getPoint());
                if (row >= 0) tbl.setRowSelectionInterval(row, row);
                if (e.isPopupTrigger()) tryShowPopup(e, popup);
            }
            private void tryShowPopup(MouseEvent e, JPopupMenu p) {
                int row = tbl.getSelectedRow();
                if (row < 0) return;
                String status = String.valueOf(model.getValueAt(row, 5));
                // Only allow cancelling Pending or Confirmed appointments
                cancelItem.setEnabled("Pending".equals(status) || "Confirmed".equals(status));
                p.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        JScrollPane scroll = new JScrollPane(tbl);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    // ── Actions ────────────────────────────────────────────────────────

    private void cancelSelected() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "Cancel your appointment on " + model.getValueAt(row, 0) + "?",
                "Cancel Appointment", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION)
            model.setValueAt("Cancelled", row, 5);
    }

    private void showBookingDialog() {
        JPanel form = new JPanel(new GridLayout(4, 1, 0, 14));
        form.setPreferredSize(new Dimension(340, 240));

        FloatingInput petField    = new FloatingInput("Pet Name", false);
        FloatingInput dateField   = new FloatingInput("Date (YYYY-MM-DD)", false);
        FloatingInput timeField   = new FloatingInput("Preferred Time (e.g. 10:00 AM)", false);
        FloatingInput reasonField = new FloatingInput("Reason for Visit", false);

        form.add(petField);
        form.add(dateField);
        form.add(timeField);
        form.add(reasonField);

        int result = JOptionPane.showConfirmDialog(this, form,
                "Book an Appointment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String pet    = petField.getText().trim();
            String date   = dateField.getText().trim();
            String reason = reasonField.getText().trim();
            if (pet.isEmpty() || date.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Pet name and date are required.", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            model.addRow(new Object[]{
                date,
                timeField.getText().trim().isEmpty() ? "TBD" : timeField.getText().trim(),
                pet, reason.isEmpty() ? "General" : reason,
                "Dr. Smith", "Pending"
            });
        }
    }
}
