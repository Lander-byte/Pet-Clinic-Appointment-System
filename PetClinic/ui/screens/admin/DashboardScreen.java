package PetClinic.ui.screens.admin;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.util.function.Consumer;
import javax.swing.JPanel;
import PetClinic.model.user.User;
import PetClinic.service.ClinicService;
import PetClinic.ui.components.CustomerSidebar;
import PetClinic.ui.components.Sidebar;
import PetClinic.ui.screens.customer.CustomerAppointmentScreen;
import PetClinic.ui.screens.customer.CustomerBillingScreen;
import PetClinic.ui.screens.customer.CustomerHomeScreen;
import PetClinic.ui.screens.customer.CustomerPetsScreen;
import PetClinic.ui.screens.customer.CustomersScreen;

public class DashboardScreen extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel contentCards;
    private final Consumer<String> topLevelNavigator;
    private final String logoutTarget;
    private final User user;

    public DashboardScreen(User user, ClinicService clinicService, boolean admin, Consumer<String> topLevelNavigator, String logoutTarget) {
        super(new BorderLayout());
        this.user = user;
        this.topLevelNavigator = topLevelNavigator;
        this.logoutTarget = logoutTarget;
        this.setBackground(Color.WHITE);
        this.cardLayout = new CardLayout();
        this.contentCards = new JPanel(this.cardLayout);
        this.contentCards.setOpaque(false);
        if (admin) {
            this.contentCards.add(new DashboardOverview(user, clinicService), "Overview");
            this.contentCards.add(new ProfileScreen(user), "Profile");
            this.contentCards.add(new AppointmentsScreen(clinicService, user), "Appointments");
            if (clinicService.isVeterinarian(user)) {
                this.contentCards.add(new TreatmentPlansScreen(clinicService), "Treatment Plans");
            }

            if (clinicService.isStaff(user)) {
                this.contentCards.add(new BillingScreen(clinicService), "Billing");
                this.contentCards.add(new ServicesScreen(clinicService), "Services");
                this.contentCards.add(new CustomersScreen(clinicService.getAccountStore()), "Customers");
            }

            this.contentCards.add(new SettingsScreen(), "Settings");
            Sidebar sidebar = new Sidebar(user, this::handleNavigation, logoutTarget);
            this.add(sidebar, "West");
            this.cardLayout.show(this.contentCards, "Overview");
        } else {
            this.contentCards.add(new CustomerHomeScreen(user, clinicService), "Home");
            this.contentCards.add(new ProfileScreen(user), "Profile");
            this.contentCards.add(new CustomerAppointmentScreen(user, clinicService), "Appointments");
            this.contentCards.add(new CustomerPetsScreen(user, clinicService), "My Pets");
            this.contentCards.add(new CustomerBillingScreen(user, clinicService), "Payments");
            this.contentCards.add(new SettingsScreen(), "Settings");
            CustomerSidebar sidebar = new CustomerSidebar(this::handleNavigation, logoutTarget);
            this.add(sidebar, "West");
            this.cardLayout.show(this.contentCards, "Home");
        }

        this.add(this.contentCards, "Center");
    }

    private void handleNavigation(String target) {
        if (target.equals(this.logoutTarget)) {
            this.topLevelNavigator.accept(target);
        } else {
            this.cardLayout.show(this.contentCards, target);
        }

    }
}
