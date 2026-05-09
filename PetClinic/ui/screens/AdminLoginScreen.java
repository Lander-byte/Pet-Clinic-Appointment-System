package PetClinic.ui.screens;

import PetClinic.ui.components.FloatingInput;
import PetClinic.ui.components.Logo;
import PetClinic.ui.components.RoundedPanel;
import PetClinic.ui.components.UiTheme;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.function.BiConsumer;

public class AdminLoginScreen extends JPanel {
    private static final int DEFAULT_WIDTH = 940;
    private static final int DEFAULT_HEIGHT = 670;

    private final Logo logo;
    private final RoundedPanel loginCard;
    private final JLabel title;
    private final FloatingInput username;
    private final FloatingInput password;
    private final JButton signIn;

    public AdminLoginScreen(BiConsumer<FloatingInput, FloatingInput> onLogin) {
        setLayout(null);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

        logo = new Logo(52);
        add(logo);

        loginCard = new RoundedPanel(UiTheme.BLUE, 22);
        loginCard.setLayout(null);
        add(loginCard);

        title = UiTheme.centeredLabel("Admin Login", UiTheme.TITLE_FONT.deriveFont(Font.BOLD, 22f), UiTheme.ORANGE);
        loginCard.add(title);

        username = new FloatingInput("Username", false);
        loginCard.add(username);

        password = new FloatingInput("Password", true);
        loginCard.add(password);

        signIn = UiTheme.pillButton("Sign In", UiTheme.ORANGE, Color.WHITE, 10);
        signIn.addActionListener(e -> onLogin.accept(username, password));
        loginCard.add(signIn);
    }

    @Override
    public void doLayout() {
        int width = Math.max(getWidth(), DEFAULT_WIDTH);
        int height = Math.max(getHeight(), DEFAULT_HEIGHT);
        float scale = UiTheme.layoutScale(width, height);

        int logoWidth = UiTheme.scaled(260, scale);
        int logoHeight = UiTheme.scaled(85, scale);
        logo.setBounds(UiTheme.centeredX(width, logoWidth), Math.max(UiTheme.scaled(58, scale), (height - UiTheme.scaled(410, scale)) / 2), logoWidth, logoHeight);

        int cardWidth = UiTheme.scaled(230, scale);
        int cardHeight = UiTheme.scaled(200, scale);
        int cardX = UiTheme.centeredX(width, cardWidth);
        int cardY = logo.getY() + logoHeight + UiTheme.scaled(28, scale);
        loginCard.setBounds(cardX, cardY, cardWidth, cardHeight);

        title.setFont(UiTheme.scaledFont(UiTheme.TITLE_FONT, Font.BOLD, 22, scale));
        signIn.setFont(UiTheme.scaledFont(new Font("Arial", Font.PLAIN, 10), Font.PLAIN, 10, scale));
        username.setUiScale(scale);
        password.setUiScale(scale);

        int fieldWidth = UiTheme.scaled(174, scale);
        int fieldHeight = UiTheme.scaled(28, scale);
        int fieldX = UiTheme.centeredX(cardWidth, fieldWidth);
        title.setBounds(0, UiTheme.scaled(20, scale), cardWidth, UiTheme.scaled(30, scale));
        username.setBounds(fieldX, UiTheme.scaled(66, scale), fieldWidth, fieldHeight);
        password.setBounds(fieldX, UiTheme.scaled(106, scale), fieldWidth, fieldHeight);
        signIn.setBounds(fieldX, UiTheme.scaled(158, scale), fieldWidth, UiTheme.scaled(30, scale));
    }
}