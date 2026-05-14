package ui;

import model.user.User;
import service.ClinicService;
import ui.components.FloatingInput;
import ui.screens.admin.AdminLoginScreen;
import ui.screens.admin.DashboardScreen;
import ui.screens.admin.LandingScreen;
import ui.screens.customer.CustomerLoginScreen;
import ui.screens.customer.CustomerRegisterScreen;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class GUI {
    private static final String LANDING = "landing";
    private static final String USER_LOGIN = "userLogin";
    private static final String USER_REGISTER = "userRegister";
    private static final String ADMIN_LOGIN = "adminLogin";
    private static final String ADMIN_DASHBOARD = "adminDashboard";
    private static final String USER_DASHBOARD = "userDashboard";

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cards;
    private final ClinicService clinicService = new ClinicService();
    private User currentUser;

    public void launch() {
        SwingUtilities.invokeLater(this::createAndShow);
    }

    private void createAndShow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        frame = new JFrame("CareHaven Veterinary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(940, 670));
        frame.setResizable(true);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.add(centerCanvas(new LandingScreen(() -> show(USER_LOGIN))), LANDING);
        cards.add(new CustomerLoginScreen(this::loginCustomer, () -> show(USER_REGISTER), () -> show(ADMIN_LOGIN), () -> show(LANDING)), USER_LOGIN);
        cards.add(new CustomerRegisterScreen(this::registerCustomer, () -> show(USER_LOGIN)), USER_REGISTER);
        cards.add(new AdminLoginScreen(this::loginAdmin, () -> show(USER_LOGIN)), ADMIN_LOGIN);

        frame.setContentPane(cards);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel centerCanvas(JPanel content) {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(Color.WHITE);
        root.add(content, new GridBagConstraints());
        return root;
    }

    private void show(String cardName) {
        cardLayout.show(cards, cardName);
    }

    private void loginCustomer(FloatingInput username, FloatingInput password) {
        String u = username.getText().trim();
        String p = password.getText();
        
        if (u.isEmpty() || p.isEmpty()) {
            showMessage("Login failed", "Please enter both username and password.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = clinicService.getAccountStore().authenticateCustomer(u, p);
        if (user == null) {
            showMessage("Login failed", "Invalid username or password.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentUser = user;
        username.clear();
        password.clear();
        
        // Recreate dashboard for the specific user
        cards.add(new DashboardScreen(currentUser, clinicService, false, this::show, LANDING), USER_DASHBOARD);
        show(USER_DASHBOARD);
    }

    private void registerCustomer(FloatingInput username, FloatingInput email, FloatingInput password, FloatingInput confirm) {
        String passwordText = password.getText();
        if (!passwordText.equals(confirm.getText())) {
            showMessage("Registration failed", "Password and confirm password must match.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            clinicService.getAccountStore().registerCustomer(username.getText(), email.getText(), passwordText);
            username.clear();
            email.clear();
            password.clear();
            confirm.clear();
            showMessage("Registration successful", "Your customer account has been created. Please log in.", JOptionPane.INFORMATION_MESSAGE);
            show(USER_LOGIN);
        } catch (IllegalArgumentException ex) {
            showMessage("Registration failed", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loginAdmin(FloatingInput username, FloatingInput password) {
        String u = username.getText().trim();
        String p = password.getText();

        if (u.isEmpty() || p.isEmpty()) {
            showMessage("Login failed", "Please enter both username and password.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = clinicService.getAccountStore().authenticateAdmin(u, p);
        if (user == null) {
            showMessage("Admin login failed", "Use a staff or veterinarian account.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentUser = user;
        username.clear();
        password.clear();
        
        // Recreate dashboard for the specific admin
        cards.add(new DashboardScreen(currentUser, clinicService, true, this::show, LANDING), ADMIN_DASHBOARD);
        show(ADMIN_DASHBOARD);
    }

    private void showMessage(String title, String message, int type) {
        JOptionPane.showMessageDialog(frame, message, title, type);
    }
}
