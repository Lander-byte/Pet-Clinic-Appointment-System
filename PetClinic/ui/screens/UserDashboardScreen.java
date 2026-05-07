package ui.screens;

import ui.components.Sidebar;
import ui.components.UiTheme;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.function.Consumer;

public class DashboardScreen extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel contentCards;
    private final Consumer<String> topLevelNavigator;
    private final String logoutTarget;

    public DashboardScreen(boolean admin, Consumer<String> topLevelNavigator, String logoutTarget) {
        super(new BorderLayout());
        this.topLevelNavigator = topLevelNavigator;
        this.logoutTarget = logoutTarget;
        setBackground(Color.WHITE);

        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);
        contentCards.setOpaque(false);

        // Add real internal dashboard screens
        contentCards.add(new DashboardOverview(), "Overview");
        contentCards.add(new AppointmentsScreen(), "Appointments");
        contentCards.add(new CustomersScreen(), "Customers");
        contentCards.add(new SettingsScreen(), "Settings");

        Sidebar sidebar = new Sidebar(this::handleNavigation, logoutTarget);
        add(sidebar, BorderLayout.WEST);
        add(contentCards, BorderLayout.CENTER);
        
        cardLayout.show(contentCards, "Overview");
    }

    private void handleNavigation(String target) {
        if (target.equals(logoutTarget)) {
            topLevelNavigator.accept(target);
        } else {
            cardLayout.show(contentCards, target);
        }
    }
}
 
