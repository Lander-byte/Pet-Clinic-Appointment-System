package PetClinic.ui.components;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

public final class UiTheme {
    //main color area
    public static final Color BLUE = new Color(0, 88, 128);
    public static final Color LIGHT_BLUE = new Color(235, 248, 255);
    public static final Color DARK_BLUE = new Color(0, 58, 85);
    public static final Color ORANGE = new Color(255, 129, 69);
    //bg color area
    public static final Color BG_WHITE = Color.WHITE;
    public static final Color BG_LIGHT = new Color(248, 249, 250);
    public static final Color BORDER = new Color(230, 230, 230);
    public static final Color FIELD = new Color(242, 242, 242);
    //text color area
    public static final Color TEXT_MAIN = new Color(33, 37, 41);
    public static final Color TEXT_BLUE = new Color(0, 73, 117);
    public static final Color TEXT_GRAY = new Color(108, 117, 125);
    //font styles area
    public static final Font LOGO_FONT = new Font("Lilita One", Font.BOLD, 88);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);

    private UiTheme() {
    }

    public static JButton pillButton(String text, Color background, Color foreground, int fontSize) {
        JButton button = new RoundedButton(text, background, background, 24);
        button.setForeground(foreground);
        button.setFont(BUTTON_FONT.deriveFont((float)fontSize));
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JButton outlineButton(String text) {
        JButton button = new RoundedButton(text, new Color(0,0,0,0), ORANGE, 20);
        button.setForeground(ORANGE);
        button.setFont(BUTTON_FONT.deriveFont(11f));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JLabel centeredLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setForeground(color);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }

    public static float layoutScale(int width, int height) {
        return Math.max(1f, Math.min(1.45f, Math.min(width / 940f, height / 670f)));
    }

    public static int scaled(int value, float scale) {
        return Math.round(value * scale);
    }

    public static Font scaledFont(Font base, int style, int size, float scale) {
        return base.deriveFont(style, size * scale);
    }

    public static int centeredX(int width, int childWidth) {
        return Math.max(0, (width - childWidth) / 2);
    }
}
