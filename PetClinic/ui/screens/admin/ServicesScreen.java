package PetClinic.ui.screens.admin;

import PetClinic.model.billing.Service;
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
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

public class ServicesScreen extends JPanel {
    private final ClinicService clinicService;
    private final DefaultTableModel model;

    public ServicesScreen(ClinicService clinicService) {
        this.clinicService = clinicService;
        setLayout(new BorderLayout(0, 30));
        setBackground(UiTheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Manage Services");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UiTheme.TEXT_BLUE);
        header.add(title, BorderLayout.WEST);
        JButton add = UiTheme.pillButton("+ Add Service", UiTheme.BLUE, Color.WHITE, 13);
        add.setPreferredSize(new Dimension(150, 45));
        add.addActionListener(e -> showAddDialog());
        header.add(add, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Service", "Price"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(46);
        table.setFont(UiTheme.BODY_FONT);
        JPanel card = new RoundedPanel(Color.WHITE, 22);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Service service : clinicService.getServices()) {
            model.addRow(new Object[]{service.getName(), "PHP " + service.getPrice()});
        }
    }

    private void showAddDialog() {
        JPanel form = new JPanel(new GridLayout(2, 1, 0, 12));
        FloatingInput name = new FloatingInput("Service Name", false);
        FloatingInput price = new FloatingInput("Price", false);
        form.add(name);
        form.add(price);
        int result = JOptionPane.showConfirmDialog(this, form, "Add Service", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;
        try {
            clinicService.addService(name.getText(), Double.parseDouble(price.getText().trim()));
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price must be a number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
