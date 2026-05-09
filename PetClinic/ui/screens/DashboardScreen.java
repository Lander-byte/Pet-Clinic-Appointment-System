package PetClinic.ui.screens;

import PetClinic.ui.components.Sidebar;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.function.Consumer;

public class DashboardScreen extends JPanel {
    public DashboardScreen(boolean admin, Consumer<String> navigator, String adminDashboardTarget, String landingTarget) {
        super(new BorderLayout());
        setBackground(Color.WHITE);

        if (admin) {
            add(new Sidebar(navigator, adminDashboardTarget, landingTarget), BorderLayout.WEST);
        }

        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        add(content, BorderLayout.CENTER);
    }
}
