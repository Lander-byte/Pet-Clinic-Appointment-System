package ui;

import model.user.User;
import model.user.UserAccountStore;
import ui.components.FloatingInput;
import ui.screens.AdminLoginScreen;
import ui.screens.DashboardScreen;
import ui.screens.LandingScreen;
import ui.screens.UserLoginScreen;
import ui.screens.UserRegisterScreen;

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
    private final UserAccountStore accountStore = new UserAccountStore();
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
        cards.add(new UserLoginScreen(this::loginCustomer, () -> show(USER_REGISTER), () -> show(ADMIN_LOGIN)), USER_LOGIN);
        cards.add(new UserRegisterScreen(this::registerCustomer, () -> show(USER_LOGIN)), USER_REGISTER);
        cards.add(new AdminLoginScreen(this::loginAdmin), ADMIN_LOGIN);
        
        // Updated Dashboard instances with the new 3-argument constructor
        cards.add(new DashboardScreen(true, this::show, LANDING), ADMIN_DASHBOARD);
        cards.add(new DashboardScreen(false, this::show, LANDING), USER_DASHBOARD);

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
        User user = accountStore.authenticateCustomer(username.getText(), password.getText());
        if (user == null) {
            showMessage("Login failed", "Please enter a registered customer username and password.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentUser = user;
        username.clear();
        password.clear();
        show(USER_DASHBOARD);
    }

    private void registerCustomer(FloatingInput username, FloatingInput email, FloatingInput password, FloatingInput confirm) {
        String passwordText = password.getText();
        if (!passwordText.equals(confirm.getText())) {
            showMessage("Registration failed", "Password and confirm password must match.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            accountStore.registerCustomer(username.getText(), email.getText(), passwordText);
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
        User user = accountStore.authenticateAdmin(username.getText(), password.getText());
        if (user == null) {
            showMessage("Admin login failed", "Use a staff or veterinarian account.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentUser = user;
        username.clear();
        password.clear();
        show(ADMIN_DASHBOARD);
    }

    private void showMessage(String title, String message, int type) {
        JOptionPane.showMessageDialog(frame, message, title, type);
    }
}
