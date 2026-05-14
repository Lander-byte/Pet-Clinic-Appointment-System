package PetClinic.ui.screens.customer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.function.BiConsumer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import PetClinic.ui.components.FloatingInput;
import PetClinic.ui.components.RoundedPanel;
import PetClinic.ui.components.UiTheme;
import PetClinic.ui.components.WhiteUnderline;

public class CustomerLoginScreen extends JPanel {
    private static final int DEFAULT_WIDTH = 940;
    private static final int DEFAULT_HEIGHT = 670;
    private static final int FIELD_WIDTH = 280;
    private static final int FIELD_HEIGHT = 50;
    private final JPanel loginForm;
    private final JPanel bluePanel;
    private final JLabel title;
    private final FloatingInput username;
    private final FloatingInput pass;
    private final JCheckBox remember;
    private final JLabel forgot;
    private final JButton signIn;
    private final JLabel adminLoginLink;
    private final JLabel welcomeTitle;
    private final WhiteUnderline underline;
    private final JLabel welcomeBody;
    private final JButton createAccount;
    private final JButton backBtn;

    public CustomerLoginScreen(BiConsumer<FloatingInput, FloatingInput> onLogin, Runnable onRegister, final Runnable onAdminLogin, Runnable onBack) {
        this.setLayout((LayoutManager)null);
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(940, 670));
        this.loginForm = new JPanel((LayoutManager)null);
        this.loginForm.setOpaque(false);
        this.add(this.loginForm);

        this.backBtn = new JButton("← Back");
        this.backBtn.setFont(UiTheme.BODY_FONT.deriveFont(java.awt.Font.BOLD, 12f));
        this.backBtn.setForeground(UiTheme.TEXT_GRAY);
        this.backBtn.setContentAreaFilled(false);
        this.backBtn.setBorder(null);
        this.backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.backBtn.addActionListener(e -> onBack.run());
        this.loginForm.add(this.backBtn);

        this.title = UiTheme.centeredLabel("Welcome Back!", UiTheme.TITLE_FONT.deriveFont(1, 32.0F), UiTheme.TEXT_BLUE);
        this.loginForm.add(this.title);
        JLabel subtitle = UiTheme.centeredLabel("Login to manage your appointments", UiTheme.BODY_FONT, UiTheme.TEXT_GRAY);
        subtitle.setName("subtitle");
        this.loginForm.add(subtitle);
        this.username = new FloatingInput("Username", false);
        this.loginForm.add(this.username);
        this.pass = new FloatingInput("Password", true);
        this.loginForm.add(this.pass);
        this.username.addActionListener((e) -> onLogin.accept(this.username, this.pass));
        this.pass.addActionListener((e) -> onLogin.accept(this.username, this.pass));
        this.remember = new JCheckBox("Remember Me");
        this.remember.setFont(UiTheme.BODY_FONT.deriveFont(12.0F));
        this.remember.setForeground(UiTheme.TEXT_GRAY);
        this.remember.setOpaque(false);
        this.loginForm.add(this.remember);
        this.forgot = new JLabel("<html><u>Forgot Password?</u></html>");
        this.forgot.setFont(UiTheme.BODY_FONT.deriveFont(12.0F));
        this.forgot.setForeground(UiTheme.TEXT_GRAY);
        this.forgot.setCursor(Cursor.getPredefinedCursor(12));
        this.forgot.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                CustomerLoginScreen.this.handleForgotPassword();
            }

            public void mouseEntered(MouseEvent e) {
                CustomerLoginScreen.this.forgot.setForeground(UiTheme.BLUE);
            }

            public void mouseExited(MouseEvent e) {
                CustomerLoginScreen.this.forgot.setForeground(UiTheme.TEXT_GRAY);
            }
        });
        this.loginForm.add(this.forgot);
        this.signIn = UiTheme.pillButton("Sign In", UiTheme.BLUE, Color.WHITE, 14);
        this.signIn.addActionListener((e) -> onLogin.accept(this.username, this.pass));
        this.loginForm.add(this.signIn);
        this.adminLoginLink = new JLabel("<html>Login as <b>Admin</b></html>", 0);
        this.adminLoginLink.setFont(UiTheme.BODY_FONT.deriveFont(12.0F));
        this.adminLoginLink.setForeground(UiTheme.TEXT_GRAY);
        this.adminLoginLink.setCursor(Cursor.getPredefinedCursor(12));
        this.adminLoginLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                onAdminLogin.run();
            }

            public void mouseEntered(MouseEvent e) {
                CustomerLoginScreen.this.adminLoginLink.setForeground(UiTheme.BLUE);
            }

            public void mouseExited(MouseEvent e) {
                CustomerLoginScreen.this.adminLoginLink.setForeground(UiTheme.TEXT_GRAY);
            }
        });
        this.loginForm.add(this.adminLoginLink);
        this.bluePanel = new RoundedPanel(UiTheme.BLUE, 0);
        this.bluePanel.setLayout((LayoutManager)null);
        this.add(this.bluePanel);
        this.welcomeTitle = UiTheme.centeredLabel("New Here?", UiTheme.TITLE_FONT.deriveFont(1, 32.0F), Color.WHITE);
        this.bluePanel.add(this.welcomeTitle);
        this.underline = new WhiteUnderline();
        this.bluePanel.add(this.underline);
        this.welcomeBody = UiTheme.centeredLabel("<html><div style='text-align:center;'>Sign up and discover a professional<br>care for your beloved pets.</div></html>", UiTheme.BODY_FONT.deriveFont(14.0F), Color.WHITE);
        this.bluePanel.add(this.welcomeBody);
        this.createAccount = UiTheme.pillButton("SIGN UP", Color.WHITE, UiTheme.BLUE, 14);
        this.createAccount.addActionListener((e) -> onRegister.run());
        this.bluePanel.add(this.createAccount);
    }

    private void handleForgotPassword() {
        String email = JOptionPane.showInputDialog(this, "Enter your email address to reset password:", "Forgot Password", 3);
        if (email != null && !email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Reset instructions sent to: " + email, "Success", 1);
        }

    }

    public void doLayout() {
        int width = this.getWidth();
        int height = this.getHeight();
        int half = (int)((double)width * 0.55);
        int rightWidth = width - half;
        float scale = UiTheme.layoutScale(width, height);
        this.loginForm.setBounds(0, 0, half, height);
        this.bluePanel.setBounds(half, 0, rightWidth, height);
        this.backBtn.setBounds(UiTheme.scaled(20, scale), UiTheme.scaled(20, scale), UiTheme.scaled(80, scale), UiTheme.scaled(30, scale));
        int fieldWidth = UiTheme.scaled(280, scale);
        int fieldHeight = UiTheme.scaled(50, scale);
        int x = (half - fieldWidth) / 2;
        int startY = (height - UiTheme.scaled(400, scale)) / 2;
        this.title.setBounds(0, startY, half, UiTheme.scaled(40, scale));

        for(Component c : this.loginForm.getComponents()) {
            if ("subtitle".equals(c.getName())) {
                c.setBounds(0, startY + UiTheme.scaled(45, scale), half, UiTheme.scaled(20, scale));
            }
        }

        this.username.setBounds(x, startY + UiTheme.scaled(90, scale), fieldWidth, fieldHeight);
        this.pass.setBounds(x, startY + UiTheme.scaled(150, scale), fieldWidth, fieldHeight);
        this.remember.setBounds(x, startY + UiTheme.scaled(205, scale), UiTheme.scaled(130, scale), UiTheme.scaled(20, scale));
        this.forgot.setBounds(x + fieldWidth - UiTheme.scaled(110, scale), startY + UiTheme.scaled(205, scale), UiTheme.scaled(110, scale), UiTheme.scaled(20, scale));
        this.signIn.setBounds(x, startY + UiTheme.scaled(250, scale), fieldWidth, UiTheme.scaled(45, scale));
        this.adminLoginLink.setBounds(0, startY + UiTheme.scaled(310, scale), half, UiTheme.scaled(20, scale));
        int ry = (height - UiTheme.scaled(250, scale)) / 2;
        this.welcomeTitle.setBounds(0, ry, rightWidth, UiTheme.scaled(40, scale));
        this.underline.setBounds((rightWidth - UiTheme.scaled(60, scale)) / 2, ry + UiTheme.scaled(45, scale), UiTheme.scaled(60, scale), 4);
        this.welcomeBody.setBounds(20, ry + UiTheme.scaled(70, scale), rightWidth - 40, UiTheme.scaled(60, scale));
        this.createAccount.setBounds((rightWidth - UiTheme.scaled(180, scale)) / 2, ry + UiTheme.scaled(160, scale), UiTheme.scaled(180, scale), UiTheme.scaled(45, scale));
    }
}
