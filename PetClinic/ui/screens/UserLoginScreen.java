package ui.screens;

import ui.components.FloatingInput;
import ui.components.RoundedPanel;
import ui.components.UiTheme;
import ui.components.WhiteUnderline;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;

public class UserLoginScreen extends JPanel {
    private static final int DEFAULT_WIDTH = 940;
    private static final int DEFAULT_HEIGHT = 670;
    private static final int FIELD_WIDTH = 200;
    private static final int FIELD_HEIGHT = 34;

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

    public UserLoginScreen(BiConsumer<FloatingInput, FloatingInput> onLogin, Runnable onRegister, Runnable onAdminLogin) {
        setLayout(null);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

        loginForm = new RoundedPanel(Color.WHITE, 24);
        loginForm.setLayout(null);
        loginForm.setBackground(Color.WHITE);
        add(loginForm);

        title = UiTheme.centeredLabel("Log in your Account", UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 24f), UiTheme.TEXT_BLUE);
        loginForm.add(title);

        username = new FloatingInput("Username", false);
        loginForm.add(username);

        pass = new FloatingInput("Password", true);
        loginForm.add(pass);

        remember = new JCheckBox("Remember Me");
        remember.setFont(new Font("Arial", Font.PLAIN, 9));
        remember.setOpaque(false);
        loginForm.add(remember);

        forgot = new JLabel("Forgot Password?");
        forgot.setFont(new Font("Arial", Font.PLAIN, 9));
        forgot.setForeground(Color.BLACK);
        loginForm.add(forgot);

        signIn = UiTheme.pillButton("Sign In", UiTheme.BLUE, Color.WHITE, 10);
        signIn.addActionListener(e -> onLogin.accept(username, pass));
        loginForm.add(signIn);

        adminLoginLink = new JLabel("<html><u>Login as Admin</u></html>", SwingConstants.CENTER);
        adminLoginLink.setFont(new Font("Arial", Font.PLAIN, 9));
        adminLoginLink.setForeground(Color.BLACK);
        adminLoginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        adminLoginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onAdminLogin.run();
            }
        });
        loginForm.add(adminLoginLink);

        bluePanel = new JPanel(null);
        bluePanel.setBackground(UiTheme.BLUE);
        add(bluePanel);

        welcomeTitle = UiTheme.centeredLabel("Hello, Friend!", UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 24f), Color.WHITE);
        bluePanel.add(welcomeTitle);

        underline = new WhiteUnderline();
        bluePanel.add(underline);

        welcomeBody = UiTheme.centeredLabel("<html><div style='text-align:center;'>Don't have an Account?<br>Fill up your personal information to register<br>and start using CareHaven.</div></html>",
                UiTheme.BODY_FONT.deriveFont(10f), Color.WHITE);
        bluePanel.add(welcomeBody);

        createAccount = UiTheme.outlineButton("Sign Up");
        createAccount.addActionListener(e -> onRegister.run());
        bluePanel.add(createAccount);
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
        title.setFont(UiTheme.scaledFont(UiTheme.TITLE_FONT, Font.BOLD, 24, scale));
        welcomeTitle.setFont(UiTheme.scaledFont(UiTheme.TITLE_FONT, Font.BOLD, 24, scale));
        welcomeBody.setFont(UiTheme.scaledFont(UiTheme.BODY_FONT, Font.PLAIN, 10, scale));
        createAccount.setFont(UiTheme.scaledFont(new Font("Arial", Font.PLAIN, 10), Font.PLAIN, 10, scale));
        signIn.setFont(UiTheme.scaledFont(new Font("Arial", Font.PLAIN, 10), Font.PLAIN, 10, scale));
        adminLoginLink.setFont(UiTheme.scaledFont(new Font("Arial", Font.PLAIN, 9), Font.PLAIN, 9, scale));
        remember.setFont(UiTheme.scaledFont(new Font("Arial", Font.PLAIN, 9), Font.PLAIN, 9, scale));
        forgot.setFont(UiTheme.scaledFont(new Font("Arial", Font.PLAIN, 9), Font.PLAIN, 9, scale));
        username.setUiScale(scale);
        pass.setUiScale(scale);

        loginForm.setBounds(0, 0, half, height);
        bluePanel.setBounds(half, 0, rightWidth, height);

        int x = UiTheme.centeredX(half, fieldWidth);
        int startY = Math.max(UiTheme.scaled(78, scale), (height - UiTheme.scaled(305, scale)) / 2);
        title.setBounds(0, startY, half, titleHeight);
        username.setBounds(x, startY + UiTheme.scaled(78, scale), fieldWidth, fieldHeight);
        pass.setBounds(x, startY + UiTheme.scaled(127, scale), fieldWidth, fieldHeight);
        remember.setBounds(x - UiTheme.scaled(1, scale), startY + UiTheme.scaled(171, scale), UiTheme.scaled(122, scale), UiTheme.scaled(18, scale));
        forgot.setBounds(x + fieldWidth - UiTheme.scaled(82, scale), startY + UiTheme.scaled(173, scale), UiTheme.scaled(110, scale), UiTheme.scaled(15, scale));
        signIn.setBounds(x, startY + UiTheme.scaled(208, scale), fieldWidth, buttonHeight);
        adminLoginLink.setBounds(x, startY + UiTheme.scaled(247, scale), fieldWidth, UiTheme.scaled(18, scale));

        int contentY = Math.max(UiTheme.scaled(145, scale), (height - UiTheme.scaled(190, scale)) / 2);
        welcomeTitle.setBounds(0, contentY, rightWidth, titleHeight);
        underline.setBounds(UiTheme.centeredX(rightWidth, UiTheme.scaled(86, scale)), contentY + UiTheme.scaled(38, scale), UiTheme.scaled(86, scale), UiTheme.scaled(8, scale));
        welcomeBody.setBounds(0, contentY + UiTheme.scaled(58, scale), rightWidth, UiTheme.scaled(68, scale));
        createAccount.setBounds(UiTheme.centeredX(rightWidth, fieldWidth), contentY + UiTheme.scaled(145, scale), fieldWidth, buttonHeight);
    }
}
