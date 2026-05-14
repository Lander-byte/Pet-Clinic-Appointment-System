package PetClinic.ui.screens.admin;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.util.Objects;
import java.util.function.BiConsumer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import PetClinic.ui.components.FloatingInput;
import PetClinic.ui.components.Logo;
import PetClinic.ui.components.RoundedPanel;
import PetClinic.ui.components.UiTheme;

public class AdminLoginScreen extends JPanel {
    private static final int DEFAULT_WIDTH = 940;
    private static final int DEFAULT_HEIGHT = 670;
    private final Logo logo;
    private final RoundedPanel loginCard;
    private final JLabel title;
    private final FloatingInput username;
    private final FloatingInput password;
    private final JButton signIn;
    private final JButton backBtn;

    public AdminLoginScreen(BiConsumer<FloatingInput, FloatingInput> onLogin, Runnable onBack) {
        this.setLayout((LayoutManager)null);
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(940, 670));

        this.backBtn = new JButton("← Back");
        this.backBtn.setFont(UiTheme.BODY_FONT.deriveFont(java.awt.Font.BOLD, 12f));
        this.backBtn.setForeground(Color.WHITE);
        this.backBtn.setContentAreaFilled(false);
        this.backBtn.setBorder(null);
        this.backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.backBtn.addActionListener(e -> onBack.run());
        this.add(this.backBtn);

        this.logo = new Logo(52, Color.WHITE);
        this.add(this.logo);
        this.loginCard = new RoundedPanel(Color.WHITE, 30) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 20));
                g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 30, 30);
                g2.dispose();
            }
        };
        this.loginCard.setLayout((LayoutManager)null);
        this.add(this.loginCard);
        this.title = UiTheme.centeredLabel("Admin Portal", UiTheme.TITLE_FONT.deriveFont(1, 24.0F), UiTheme.TEXT_BLUE);
        this.loginCard.add(this.title);
        JLabel subtitle = UiTheme.centeredLabel("Please sign in to continue", UiTheme.BODY_FONT, UiTheme.TEXT_GRAY);
        subtitle.setName("subtitle");
        this.loginCard.add(subtitle);
        this.username = new FloatingInput("Username", false);
        this.loginCard.add(this.username);
        this.password = new FloatingInput("Password", true);
        this.loginCard.add(this.password);
        this.username.addActionListener((e) -> onLogin.accept(this.username, this.password));
        this.password.addActionListener((e) -> onLogin.accept(this.username, this.password));
        this.signIn = UiTheme.pillButton("SIGN IN", UiTheme.ORANGE, Color.WHITE, 14);
        this.signIn.addActionListener((e) -> onLogin.accept(this.username, this.password));
        this.loginCard.add(this.signIn);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0.0F, 0.0F, UiTheme.BLUE, (float)this.getWidth(), (float)this.getHeight(), UiTheme.DARK_BLUE);
        g2.setPaint(gp);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2.setColor(new Color(255, 255, 255, 15));
        g2.fillOval(this.getWidth() - 200, -100, 400, 400);
        g2.fillOval(-150, this.getHeight() - 250, 400, 400);
        g2.dispose();
    }

    public void doLayout() {
        int width = this.getWidth();
        int height = this.getHeight();
        float scale = UiTheme.layoutScale(width, height);
        this.backBtn.setBounds(UiTheme.scaled(20, scale), UiTheme.scaled(20, scale), UiTheme.scaled(80, scale), UiTheme.scaled(30, scale));
        int logoWidth = UiTheme.scaled(280, scale);
        int logoHeight = UiTheme.scaled(100, scale);
        this.logo.setBounds((width - logoWidth) / 2, UiTheme.scaled(80, scale), logoWidth, logoHeight);
        int cardWidth = UiTheme.scaled(320, scale);
        int cardHeight = UiTheme.scaled(380, scale);
        int cardX = (width - cardWidth) / 2;
        int cardY = this.logo.getY() + logoHeight + UiTheme.scaled(40, scale);
        this.loginCard.setBounds(cardX, cardY, cardWidth, cardHeight);
        this.title.setBounds(0, UiTheme.scaled(40, scale), cardWidth, UiTheme.scaled(40, scale));

        for(Component c : this.loginCard.getComponents()) {
            if ("subtitle".equals(c.getName())) {
                c.setBounds(0, UiTheme.scaled(75, scale), cardWidth, UiTheme.scaled(20, scale));
            }
        }

        int fieldWidth = UiTheme.scaled(260, scale);
        int fieldHeight = UiTheme.scaled(50, scale);
        int fieldX = (cardWidth - fieldWidth) / 2;
        this.username.setBounds(fieldX, UiTheme.scaled(120, scale), fieldWidth, fieldHeight);
        this.password.setBounds(fieldX, UiTheme.scaled(185, scale), fieldWidth, fieldHeight);
        this.signIn.setBounds(fieldX, UiTheme.scaled(270, scale), fieldWidth, UiTheme.scaled(50, scale));
        this.username.setUiScale(scale);
        this.password.setUiScale(scale);
    }
}
