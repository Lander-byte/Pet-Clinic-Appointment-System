package PetClinic.ui.screens;

import PetClinic.ui.components.FloatingInput;
import PetClinic.ui.components.RoundedPanel;
import PetClinic.ui.components.UiTheme;
import PetClinic.ui.components.WhiteUnderline;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class UserRegisterScreen extends JPanel {
    private static final int DEFAULT_WIDTH = 940;
    private static final int DEFAULT_HEIGHT = 670;
    private static final int FIELD_WIDTH = 280;
    private static final int FIELD_HEIGHT = 48;
    private final JPanel bluePanel;
    private final JPanel registerForm;
    private final JLabel welcomeTitle;
    private final WhiteUnderline underline;
    private final JLabel welcomeBody;
    private final JButton backToLogin;
    private final JLabel title;
    private final FloatingInput username;
    private final FloatingInput email;
    private final FloatingInput pass;
    private final FloatingInput confirm;
    private final JButton register;

    public UserRegisterScreen(RegisterHandler onRegister, Runnable onLogin) {
        this.setLayout((LayoutManager)null);
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(940, 670));
        this.bluePanel = new RoundedPanel(UiTheme.BLUE, 0);
        this.bluePanel.setLayout((LayoutManager)null);
        this.add(this.bluePanel);
        this.welcomeTitle = UiTheme.centeredLabel("Welcome Back!", UiTheme.TITLE_FONT.deriveFont(1, 32.0F), Color.WHITE);
        this.bluePanel.add(this.welcomeTitle);
        this.underline = new WhiteUnderline();
        this.bluePanel.add(this.underline);
        this.welcomeBody = UiTheme.centeredLabel("<html><div style='text-align:center;'>To keep connected with us please<br>login with your personal info.</div></html>", UiTheme.BODY_FONT.deriveFont(14.0F), Color.WHITE);
        this.bluePanel.add(this.welcomeBody);
        this.backToLogin = UiTheme.pillButton("SIGN IN", Color.WHITE, UiTheme.BLUE, 14);
        this.backToLogin.addActionListener((e) -> onLogin.run());
        this.bluePanel.add(this.backToLogin);
        this.registerForm = new JPanel((LayoutManager)null);
        this.registerForm.setOpaque(false);
        this.add(this.registerForm);
        this.title = UiTheme.centeredLabel("Create Account", UiTheme.TITLE_FONT.deriveFont(1, 32.0F), UiTheme.TEXT_BLUE);
        this.registerForm.add(this.title);
        JLabel subtitle = UiTheme.centeredLabel("Join our community of pet lovers", UiTheme.BODY_FONT, UiTheme.TEXT_GRAY);
        subtitle.setName("subtitle");
        this.registerForm.add(subtitle);
        this.username = new FloatingInput("Username", false);
        this.registerForm.add(this.username);
        this.email = new FloatingInput("Email", false);
        this.registerForm.add(this.email);
        this.pass = new FloatingInput("Password", true);
        this.registerForm.add(this.pass);
        this.confirm = new FloatingInput("Confirm Password", true);
        this.registerForm.add(this.confirm);
        this.register = UiTheme.pillButton("SIGN UP", UiTheme.BLUE, Color.WHITE, 14);
        this.register.addActionListener((e) -> onRegister.register(this.username, this.email, this.pass, this.confirm));
        this.registerForm.add(this.register);
    }

    public void doLayout() {
        int width = this.getWidth();
        int height = this.getHeight();
        int leftWidth = (int)((double)width * 0.45);
        int rightWidth = width - leftWidth;
        float scale = UiTheme.layoutScale(width, height);
        this.bluePanel.setBounds(0, 0, leftWidth, height);
        this.registerForm.setBounds(leftWidth, 0, rightWidth, height);
        int ly = (height - UiTheme.scaled(250, scale)) / 2;
        this.welcomeTitle.setBounds(0, ly, leftWidth, UiTheme.scaled(40, scale));
        this.underline.setBounds((leftWidth - UiTheme.scaled(60, scale)) / 2, ly + UiTheme.scaled(45, scale), UiTheme.scaled(60, scale), 4);
        this.welcomeBody.setBounds(20, ly + UiTheme.scaled(70, scale), leftWidth - 40, UiTheme.scaled(60, scale));
        this.backToLogin.setBounds((leftWidth - UiTheme.scaled(180, scale)) / 2, ly + UiTheme.scaled(160, scale), UiTheme.scaled(180, scale), UiTheme.scaled(45, scale));
        int fieldWidth = UiTheme.scaled(280, scale);
        int fieldHeight = UiTheme.scaled(48, scale);
        int x = (rightWidth - fieldWidth) / 2;
        int startY = (height - UiTheme.scaled(480, scale)) / 2;
        this.title.setBounds(0, startY, rightWidth, UiTheme.scaled(40, scale));

        for(Component c : this.registerForm.getComponents()) {
            if ("subtitle".equals(c.getName())) {
                c.setBounds(0, startY + UiTheme.scaled(45, scale), rightWidth, UiTheme.scaled(20, scale));
            }
        }

        this.username.setBounds(x, startY + UiTheme.scaled(90, scale), fieldWidth, fieldHeight);
        this.email.setBounds(x, startY + UiTheme.scaled(150, scale), fieldWidth, fieldHeight);
        this.pass.setBounds(x, startY + UiTheme.scaled(210, scale), fieldWidth, fieldHeight);
        this.confirm.setBounds(x, startY + UiTheme.scaled(270, scale), fieldWidth, fieldHeight);
        this.register.setBounds(x, startY + UiTheme.scaled(350, scale), fieldWidth, UiTheme.scaled(50, scale));
        this.username.setUiScale(scale);
        this.email.setUiScale(scale);
        this.pass.setUiScale(scale);
        this.confirm.setUiScale(scale);
    }
}
