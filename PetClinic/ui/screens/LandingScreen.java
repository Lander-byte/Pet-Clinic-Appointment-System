package PetClinic.ui.screens;

import PetClinic.ui.components.Logo;
import PetClinic.ui.components.UiTheme;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;

public class LandingScreen extends JPanel {
    private static final int PANEL_WIDTH = 880;

    public LandingScreen(Runnable onLogin) {
        setLayout(null);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(PANEL_WIDTH, 590));

        Logo logo = new Logo(92);
        logo.setBounds(UiTheme.centeredX(PANEL_WIDTH, 470), 190, 470, 130);
        add(logo);

        JButton login = UiTheme.pillButton("LOGIN", UiTheme.BLUE, Color.WHITE, 12);
        login.setBounds(UiTheme.centeredX(PANEL_WIDTH, 260), 370, 260, 40);
        login.addActionListener(e -> onLogin.run());
        add(login);
    }
}