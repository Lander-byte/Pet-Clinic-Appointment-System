package PetClinic.ui.screens.customer;

import PetClinic.model.pet.Species;
import PetClinic.model.user.Owner;
import PetClinic.model.user.User;
import PetClinic.service.ClinicService;
import PetClinic.ui.components.FloatingInput;
import PetClinic.ui.components.RoundedPanel;
import PetClinic.ui.components.UiTheme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

public class CustomerHomeScreen extends JPanel {
    private final Owner owner;
    private final ClinicService clinicService;

    private final FloatingInput petName;
    private final FloatingInput date;
    private final FloatingInput time;
    private final FloatingInput reason;
    private final JComboBox<String> species;

    public CustomerHomeScreen(User user, ClinicService clinicService) {
        this.owner = user instanceof Owner ? (Owner) user : null;
        this.clinicService = clinicService;

        setLayout(new BorderLayout(0, 28));
        setBackground(UiTheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel header = new JPanel(new GridLayout(2, 1, 0, 4));
        header.setOpaque(false);
        JLabel title = new JLabel("Book Appointment");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UiTheme.TEXT_BLUE);
        JLabel subtitle = new JLabel("Send a schedule request for admin approval");
        subtitle.setFont(UiTheme.BODY_FONT);
        subtitle.setForeground(UiTheme.TEXT_GRAY);
        header.add(title);
        header.add(subtitle);
        add(header, BorderLayout.NORTH);

        RoundedPanel formCard = new RoundedPanel(Color.WHITE, 24);
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        petName = new FloatingInput("Pet Name", false);
        date = new FloatingInput("Date (YYYY-MM-DD)", false);
        time = new FloatingInput("Preferred Time (e.g. 10:00 AM)", false);
        reason = new FloatingInput("Reason for Visit", false);
        species = new JComboBox<>(new String[]{"Dog", "Cat", "Bird", "Aquatic", "Mammal", "Farm Animal"});
        species.setFont(UiTheme.BODY_FONT);
        species.setPreferredSize(new Dimension(220, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 18, 18);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        formCard.add(petName, gbc);
        gbc.gridx = 1;
        formCard.add(species, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        formCard.add(date, gbc);
        gbc.gridx = 1;
        formCard.add(time, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        formCard.add(reason, gbc);

        JButton bookButton = UiTheme.pillButton("Book Appointment", UiTheme.ORANGE, Color.WHITE, 14);
        bookButton.setPreferredSize(new Dimension(190, 46));
        bookButton.addActionListener(e -> bookAppointment());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actions.setOpaque(false);
        actions.add(bookButton);

        gbc.gridy = 3;
        gbc.insets = new Insets(12, 0, 0, 0);
        formCard.add(actions, gbc);

        add(formCard, BorderLayout.CENTER);
    }

    private void bookAppointment() {
        if (owner == null) {
            JOptionPane.showMessageDialog(this, "Only customers can book appointments.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String petValue = petName.getText().trim();
        String dateValue = date.getText().trim();
        String timeValue = time.getText().trim();
        String reasonValue = reason.getText().trim();

        if (petValue.isEmpty() || dateValue.isEmpty() || timeValue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pet name, date, and time are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            clinicService.requestAppointment(owner, petValue, selectedSpecies(), dateValue, timeValue, reasonValue);
            petName.clear();
            date.clear();
            time.clear();
            reason.clear();

            JOptionPane.showMessageDialog(this,
                    "Appointment request sent. Check Appointments for status updates.",
                    "Appointment Requested",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Species selectedSpecies() {
        String value = String.valueOf(species.getSelectedItem()).toUpperCase().replace(" ", "_");
        try {
            return Species.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return Species.DOG;
        }
    }
}
