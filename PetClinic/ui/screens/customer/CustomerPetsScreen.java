package PetClinic.ui.screens.customer;

import PetClinic.model.clinical.TreatmentPlan;
import PetClinic.model.pet.Pet;
import PetClinic.model.user.Owner;
import PetClinic.model.user.User;
import PetClinic.service.ClinicService;
import PetClinic.ui.components.RoundedPanel;
import PetClinic.ui.components.UiTheme;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.List;

public class CustomerPetsScreen extends JPanel {
    private final Owner owner;
    private final ClinicService clinicService;
    private final JPanel petsGrid = new JPanel();
    private final JPanel detailPanel = new JPanel(new BorderLayout());

    public CustomerPetsScreen(User user, ClinicService clinicService) {
        this.owner = user instanceof Owner ? (Owner) user : null;
        this.clinicService = clinicService;

        setLayout(new BorderLayout(0, 28));
        setBackground(UiTheme.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel header = new JPanel(new GridLayout(2, 1, 0, 4));
        header.setOpaque(false);
        JLabel title = new JLabel("My Pets");
        title.setFont(UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UiTheme.TEXT_BLUE);
        JLabel sub = new JLabel("View pets and treatment history from completed vet visits");
        sub.setFont(UiTheme.BODY_FONT);
        sub.setForeground(UiTheme.TEXT_GRAY);
        header.add(title);
        header.add(sub);
        add(header, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(24, 0));
        body.setOpaque(false);
        petsGrid.setLayout(new javax.swing.BoxLayout(petsGrid, javax.swing.BoxLayout.Y_AXIS));
        petsGrid.setOpaque(false);

        JScrollPane petScroll = new JScrollPane(petsGrid);
        petScroll.setBorder(null);
        petScroll.getViewport().setBackground(UiTheme.BG_LIGHT);
        petScroll.setPreferredSize(new Dimension(300, 0));

        detailPanel.setOpaque(false);
        body.add(petScroll, BorderLayout.WEST);
        body.add(detailPanel, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);

        refreshPets();
        new Timer(1500, e -> refreshPets()).start();
    }

    private void refreshPets() {
        petsGrid.removeAll();
        List<Pet> pets = owner == null ? List.of() : clinicService.getPetsForOwner(owner);
        if (pets.isEmpty()) {
            JLabel empty = new JLabel("No pets yet. Booking an appointment registers a pet.");
            empty.setFont(UiTheme.BODY_FONT);
            empty.setForeground(UiTheme.TEXT_GRAY);
            petsGrid.add(empty);
            showEmptyDetail();
        } else {
            for (Pet pet : pets) {
                petsGrid.add(petCard(pet));
                petsGrid.add(javax.swing.Box.createVerticalStrut(14));
            }
        }
        petsGrid.revalidate();
        petsGrid.repaint();
    }

    private JPanel petCard(Pet pet) {
        RoundedPanel card = new RoundedPanel(Color.WHITE, 18);
        card.setLayout(new BorderLayout(14, 0));
        card.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        card.setMaximumSize(new Dimension(280, 92));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        card.add(new PawBadge(speciesColor(pet)), BorderLayout.WEST);

        JPanel info = new JPanel(new GridLayout(2, 1, 0, 3));
        info.setOpaque(false);
        JLabel name = new JLabel(pet.getName());
        name.setFont(UiTheme.BODY_FONT.deriveFont(Font.BOLD, 14f));
        name.setForeground(UiTheme.TEXT_MAIN);
        JLabel type = new JLabel(pet.getSpecies().toString());
        type.setFont(UiTheme.BODY_FONT.deriveFont(12f));
        type.setForeground(UiTheme.TEXT_GRAY);
        info.add(name);
        info.add(type);
        card.add(info, BorderLayout.CENTER);
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { showDetail(pet); }
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(UiTheme.LIGHT_BLUE);
                card.repaint();
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.repaint();
            }
        });
        return card;
    }

    private void showEmptyDetail() {
        detailPanel.removeAll();
        JLabel hint = new JLabel("Select a pet to view treatment history", JLabel.CENTER);
        hint.setFont(UiTheme.BODY_FONT);
        hint.setForeground(UiTheme.TEXT_GRAY);
        detailPanel.add(hint, BorderLayout.CENTER);
        detailPanel.revalidate();
        detailPanel.repaint();
    }

    private void showDetail(Pet pet) {
        detailPanel.removeAll();
        RoundedPanel card = new RoundedPanel(Color.WHITE, 20);
        card.setLayout(new BorderLayout(0, 16));
        card.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel header = new JPanel(new BorderLayout(14, 0));
        header.setOpaque(false);
        header.add(new PawBadge(speciesColor(pet)), BorderLayout.WEST);
        JLabel title = new JLabel(pet.getName() + " Medical History");
        title.setFont(UiTheme.SUBTITLE_FONT);
        title.setForeground(UiTheme.TEXT_BLUE);
        header.add(title, BorderLayout.CENTER);
        card.add(header, BorderLayout.NORTH);

        JTextArea history = new JTextArea(historyText(pet));
        history.setEditable(false);
        history.setOpaque(false);
        history.setLineWrap(true);
        history.setWrapStyleWord(true);
        history.setFont(UiTheme.BODY_FONT.deriveFont(13f));
        history.setForeground(UiTheme.TEXT_MAIN);
        card.add(new JScrollPane(history), BorderLayout.CENTER);

        detailPanel.add(card, BorderLayout.CENTER);
        detailPanel.revalidate();
        detailPanel.repaint();
    }

    private String historyText(Pet pet) {
        StringBuilder text = new StringBuilder();
        if (pet.getMedicalHistory().getTreatmentPlans().isEmpty()) {
            return "No treatment plans yet. A veterinarian can add treatment notes after a confirmed visit.";
        }
        int count = 1;
        for (TreatmentPlan plan : pet.getMedicalHistory().getTreatmentPlans()) {
            text.append("Treatment Plan ").append(count++).append("\n");
            text.append(plan).append("\n");
        }
        return text.toString();
    }

    private Color speciesColor(Pet pet) {
        return switch (pet.getSpecies()) {
            case DOG -> UiTheme.BLUE;
            case CAT -> UiTheme.ORANGE;
            case BIRD -> new Color(155, 89, 182);
            case AQUATIC -> new Color(26, 188, 156);
            case FARM_ANIMAL -> new Color(142, 99, 48);
            default -> new Color(46, 204, 113);
        };
    }

    private static class PawBadge extends JPanel {
        private final Color color;

        PawBadge(Color color) {
            this.color = color;
            setOpaque(false);
            setPreferredSize(new Dimension(42, 42));
            setMinimumSize(new Dimension(42, 42));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 35));
            g2.fillOval(0, 0, 42, 42);
            g2.setColor(color);
            g2.fillOval(17, 20, 12, 12);
            g2.fillOval(8, 13, 8, 9);
            g2.fillOval(17, 8, 8, 9);
            g2.fillOval(27, 13, 8, 9);
            g2.fillOval(12, 28, 18, 9);
            g2.dispose();
        }
    }
}
