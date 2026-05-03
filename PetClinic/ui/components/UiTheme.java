package PetClinic.ui.components;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

public final class UiTheme {
    public static final Color BLUE = new Color(0, 88, 128);
    public static final Color LOGO_BLUE = new Color(39, 91, 145);
    public static final Color ORANGE = new Color(255, 129, 69);
    public static final Color FIELD = new Color(210, 210, 210);
    public static final Color TEXT_BLUE = new Color(0, 73, 117);
    public static final Color LABEL_GRAY = new Color(155, 155, 155);

    public static final Font LOGO_FONT = new Font("Lilita One", Font.BOLD, 88);
    public static final Font TITLE_FONT = new Font("Lilita One", Font.BOLD, 26);
    public static final Font BODY_FONT = new Font("Arial", Font.BOLD, 11);

    private UiTheme() {
    }

    public static JButton pillButton(String text, Color background, Color foreground, int fontSize) {
        JButton button = new RoundedButton(text, background, background, 24);
        button.setForeground(foreground);
        button.setFont(new Font("Arial", Font.PLAIN, fontSize));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JButton outlineButton(String text) {
        JButton button = new RoundedButton(text, ORANGE, Color.WHITE, 20);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 10));
        button.setBorder(BorderFactory.createEmptyBorder());
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