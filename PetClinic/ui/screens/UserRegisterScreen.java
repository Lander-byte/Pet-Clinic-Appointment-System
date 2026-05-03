package ui.screens;

import ui.components.FloatingInput;
import ui.components.RoundedPanel;
import ui.components.UiTheme;
import ui.components.WhiteUnderline;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class UserRegisterScreen extends JPanel {
    private static final int DEFAULT_WIDTH = 940;
    private static final int DEFAULT_HEIGHT = 670;
    private static final int FIELD_WIDTH = 200;
    private static final int FIELD_HEIGHT = 34;

    private final JPanel bluePanel;
    private final JPanel registerForm;
    private final JLabel welcomeTitle;
    private final WhiteUnderline underline;
    private final JLabel welcomeBody;
    private final JButton backToLogin;
    private final JLabel title;
    private final JLabel note;
    private final FloatingInput username;
    private final FloatingInput email;
    private final FloatingInput pass;
    private final FloatingInput confirm;
    private final JButton register;

    public UserRegisterScreen(RegisterHandler onRegister, Runnable onLogin) {
        setLayout(null);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

        bluePanel = new JPanel(null);
        bluePanel.setBackground(UiTheme.BLUE);
        add(bluePanel);

        welcomeTitle = UiTheme.centeredLabel("Welcome Back!", UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 24f), Color.WHITE);
        bluePanel.add(welcomeTitle);

        underline = new WhiteUnderline();
        bluePanel.add(underline);

        welcomeBody = UiTheme.centeredLabel("<html><div style='text-align:center;'>Already have an Account?<br>Sign in your registered account and<br>start using CareHaven.</div></html>",
                UiTheme.BODY_FONT.deriveFont(10f), Color.WHITE);
        bluePanel.add(welcomeBody);

        backToLogin = UiTheme.outlineButton("Sign In");
        backToLogin.addActionListener(e -> onLogin.run());
        bluePanel.add(backToLogin);

        registerForm = new RoundedPanel(Color.WHITE, 24);
        registerForm.setLayout(null);
        registerForm.setBackground(Color.WHITE);
        add(registerForm);

        title = UiTheme.centeredLabel("Register your Account", UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 24f), UiTheme.TEXT_BLUE);
        registerForm.add(title);

        note = UiTheme.centeredLabel("fill up the information provided", UiTheme.BODY_FONT.deriveFont(9f), Color.BLACK);
        registerForm.add(note);

        username = new FloatingInput("Username", false);
        registerForm.add(username);

        email = new FloatingInput("Email", false);
        registerForm.add(email);

        pass = new FloatingInput("Password", true);
        registerForm.add(pass);

        confirm = new FloatingInput("Confirm Password", true);
        registerForm.add(confirm);

        register = UiTheme.pillButton("Register", UiTheme.BLUE, Color.WHITE, 10);
        register.addActionListener(e -> onRegister.register(username, email, pass, confirm));
        registerForm.add(register);
    }

    @Override
    public void doLayout() {
        int width = Math.max(getWidth(), DEFAULT_WIDTH);
        int height = Math.max(getHeight(), DEFAULT_HEIGHT);
        int half = width / 2;
        int rightWidth = width - half;
        float scale = UiTheme.layoutScale(width, height);
        int fieldWidth = UiTheme.scaled(FIELD_WIDTH, scale);
        int fieldHeight = UiTheme.scaled(FIELD_HEIGHT, scale);
        int buttonHeight = UiTheme.scaled(30, scale);
        int titleHeight = UiTheme.scaled(35, scale);
        welcomeTitle.setFont(UiTheme.scaledFont(UiTheme.TITLE_FONT, Font.BOLD, 24, scale));
        title.setFont(UiTheme.scaledFont(UiTheme.TITLE_FONT, Font.BOLD, 24, scale));
        note.setFont(UiTheme.scaledFont(UiTheme.BODY_FONT, Font.PLAIN, 9, scale));
        welcomeBody.setFont(UiTheme.scaledFont(UiTheme.BODY_FONT, Font.PLAIN, 10, scale));
        backToLogin.setFont(UiTheme.scaledFont(new Font("Arial", Font.PLAIN, 10), Font.PLAIN, 10, scale));
        register.setFont(UiTheme.scaledFont(new Font("Arial", Font.PLAIN, 10), Font.PLAIN, 10, scale));
        username.setUiScale(scale);
        email.setUiScale(scale);
        pass.setUiScale(scale);
        confirm.setUiScale(scale);

        bluePanel.setBounds(0, 0, half, height);
        registerForm.setBounds(half, 0, rightWidth, height);

        int contentY = Math.max(UiTheme.scaled(145, scale), (height - UiTheme.scaled(190, scale)) / 2);
        welcomeTitle.setBounds(0, contentY, half, titleHeight);
        underline.setBounds(UiTheme.centeredX(half, UiTheme.scaled(86, scale)), contentY + UiTheme.scaled(38, scale), UiTheme.scaled(86, scale), UiTheme.scaled(8, scale));
        welcomeBody.setBounds(0, contentY + UiTheme.scaled(58, scale), half, UiTheme.scaled(68, scale));
        backToLogin.setBounds(UiTheme.centeredX(half, fieldWidth), contentY + UiTheme.scaled(145, scale), fieldWidth, buttonHeight);

        int x = UiTheme.centeredX(rightWidth, fieldWidth);
        int startY = Math.max(UiTheme.scaled(85, scale), (height - UiTheme.scaled(385, scale)) / 2);
        title.setBounds(0, startY, rightWidth, titleHeight);
        note.setBounds(0, startY + UiTheme.scaled(43, scale), rightWidth, UiTheme.scaled(18, scale));
        username.setBounds(x, startY + UiTheme.scaled(75, scale), fieldWidth, fieldHeight);
        email.setBounds(x, startY + UiTheme.scaled(124, scale), fieldWidth, fieldHeight);
        pass.setBounds(x, startY + UiTheme.scaled(173, scale), fieldWidth, fieldHeight);
        confirm.setBounds(x, startY + UiTheme.scaled(222, scale), fieldWidth, fieldHeight);
        register.setBounds(x, startY + UiTheme.scaled(282, scale), fieldWidth, buttonHeight);
    }
}
