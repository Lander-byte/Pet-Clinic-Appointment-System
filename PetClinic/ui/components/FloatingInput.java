package PetClinic.ui.components;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class FloatingInput extends JPanel {
    private static final int RESTING_Y = 8;
    private static final int FLOATING_Y = -1;
    private static final int RESTING_SIZE = 10;
    private static final int FLOATING_SIZE = 9;

    private final JTextField input;
    private final JLabel label;
    private final Timer animation;
    private boolean active;
    private float progress;
    private float uiScale = 1f;

    public FloatingInput(String labelText, boolean secure) {
        setLayout(null);
        setOpaque(false);

        input = secure ? new JPasswordField() : new JTextField();
        input.setBorder(BorderFactory.createEmptyBorder(8, 10, 0, 10));
        input.setBackground(UiTheme.FIELD);
        input.setForeground(new Color(45, 45, 45));
        input.setFont(new Font("Arial", Font.PLAIN, 10));
        input.setOpaque(false);
        if (input instanceof JPasswordField) {
            ((JPasswordField) input).setEchoChar('*');
        }
        add(input);

        label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, RESTING_SIZE));
        label.setForeground(new Color(75, 75, 75));
        add(label);

        animation = new Timer(8, e -> animateLabel());
        input.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                updateActiveState();
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateActiveState();
            }
        });
        input.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateActiveState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateActiveState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateActiveState();
            }
        });
    }

    public String getText() {
        if (input instanceof JPasswordField) {
            return new String(((JPasswordField) input).getPassword());
        }
        return input.getText();
    }

    public void clear() {
        input.setText("");
        updateActiveState();
    }

    public void setUiScale(float uiScale) {
        this.uiScale = uiScale;
        int horizontalPadding = UiTheme.scaled(10, uiScale);
        input.setBorder(BorderFactory.createEmptyBorder(UiTheme.scaled(8, uiScale), horizontalPadding, 0, horizontalPadding));
        input.setFont(UiTheme.scaledFont(new Font("Arial", Font.PLAIN, 10), Font.PLAIN, 10, uiScale));
        layoutFloatingLabel();
    }

    @Override
    public void doLayout() {
        input.setBounds(0, 0, getWidth(), getHeight());
        layoutFloatingLabel();
    }

    private void updateActiveState() {
        boolean shouldFloat = input.hasFocus() || !input.getText().isEmpty();
        if (active == shouldFloat) {
            return;
        }
        active = shouldFloat;
        if (!animation.isRunning()) {
            animation.start();
        }
    }

    private void animateLabel() {
        float target = active ? 1f : 0f;
        if (Math.abs(progress - target) < 0.01f) {
            progress = target;
            animation.stop();
        } else {
            progress += progress < target ? 0.12f : -0.12f;
            progress = Math.max(0f, Math.min(1f, progress));
        }
        layoutFloatingLabel();
        repaint();
    }

    private void layoutFloatingLabel() {
        int y = Math.round((RESTING_Y + ((FLOATING_Y - RESTING_Y) * progress)) * uiScale);
        int size = Math.round((RESTING_SIZE + ((FLOATING_SIZE - RESTING_SIZE) * progress)) * uiScale);
        label.setFont(new Font("Arial", Font.PLAIN, size));
        int horizontalPadding = UiTheme.scaled(10, uiScale);
        label.setBounds(horizontalPadding, y, Math.max(0, getWidth() - (horizontalPadding * 2)), UiTheme.scaled(14, uiScale));
        label.setForeground(blend(new Color(75, 75, 75), UiTheme.BLUE, progress));
    }

    private Color blend(Color from, Color to, float amount) {
        int red = Math.round(from.getRed() + ((to.getRed() - from.getRed()) * amount));
        int green = Math.round(from.getGreen() + ((to.getGreen() - from.getGreen()) * amount));
        int blue = Math.round(from.getBlue() + ((to.getBlue() - from.getBlue()) * amount));
        return new Color(red, green, blue);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(UiTheme.FIELD);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.setStroke(new BasicStroke(2f * uiScale));
        g2.setColor(blend(new Color(170, 170, 170), UiTheme.BLUE, progress));
        int horizontalPadding = UiTheme.scaled(10, uiScale);
        int underlineY = getHeight() - UiTheme.scaled(2, uiScale);
        g2.drawLine(horizontalPadding, underlineY, getWidth() - horizontalPadding, underlineY);
        g2.dispose();
        super.paintComponent(g);
    }
}