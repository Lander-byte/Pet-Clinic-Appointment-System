package ui.components;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class FloatingInput extends JPanel {
    private static final int RESTING_Y = 12;
    private static final int FLOATING_Y = 4;
    private static final int RESTING_SIZE = 12;
    private static final int FLOATING_SIZE = 10;

    private final JTextField input;
    private final JLabel label;
    private final Timer animation;
    private JButton eyeButton;
    private boolean passwordVisible = false;
    private boolean active;
    private float progress;
    private float uiScale = 1f;

    public FloatingInput(String labelText, boolean secure) {
        setLayout(null);
        setOpaque(false);

        input = secure ? new JPasswordField() : new JTextField();
        input.setBorder(BorderFactory.createEmptyBorder(15, 12, 0, 12));
        input.setBackground(new Color(0, 0, 0, 0));
        input.setForeground(UiTheme.TEXT_MAIN);
        input.setFont(UiTheme.BODY_FONT.deriveFont(14f));
        input.setOpaque(false);
        input.setCaretColor(UiTheme.BLUE);
        
        label = new JLabel(labelText);
        label.setFont(UiTheme.BODY_FONT.deriveFont((float)RESTING_SIZE));
        label.setForeground(UiTheme.TEXT_GRAY);

        add(label); 
        
        if (input instanceof JPasswordField) {
            ((JPasswordField) input).setEchoChar('•');
            
            eyeButton = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(passwordVisible ? UiTheme.BLUE : UiTheme.TEXT_GRAY);
                    int w = getWidth();
                    int h = getHeight();
                    
                    int eyeW = 16;
                    int eyeH = 10;
                    int eyeX = (w - eyeW) / 2;
                    int eyeY = (h - eyeH) / 2;

                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawArc(eyeX, eyeY, eyeW, eyeH, 0, 180);
                    g2.drawArc(eyeX, eyeY, eyeW, eyeH, 180, 180);
                    g2.fillOval(w/2 - 2, h/2 - 2, 4, 4);
                    
                    if (!passwordVisible) {
                        g2.drawLine(eyeX + 2, eyeY + 2, eyeX + eyeW - 2, eyeY + eyeH - 2);
                    }
                    g2.dispose();
                }
            };
            eyeButton.setBorder(null);
            eyeButton.setContentAreaFilled(false);
            eyeButton.setFocusPainted(false);
            eyeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            eyeButton.addActionListener(e -> togglePasswordVisibility());
            add(eyeButton);
            
            input.setBorder(BorderFactory.createEmptyBorder(15, 12, 0, 40));
        }
        add(input);

        animation = new Timer(10, e -> animateLabel());
        input.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) { updateActiveState(); repaint(); }
            @Override
            public void focusLost(FocusEvent e) { updateActiveState(); repaint(); }
        });
        input.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { updateActiveState(); }
            @Override public void removeUpdate(DocumentEvent e) { updateActiveState(); }
            @Override public void changedUpdate(DocumentEvent e) { updateActiveState(); }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, UiTheme.scaled(54, uiScale));
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    private void togglePasswordVisibility() {
        if (input instanceof JPasswordField) {
            passwordVisible = !passwordVisible;
            ((JPasswordField) input).setEchoChar(passwordVisible ? (char) 0 : '•');
            eyeButton.repaint();
        }
    }

    public String getText() {
        if (input instanceof JPasswordField) return new String(((JPasswordField) input).getPassword());
        return input.getText();
    }

    public void clear() {
        input.setText("");
        updateActiveState();
    }

    public void setUiScale(float uiScale) {
        this.uiScale = uiScale;
        int horizontalPadding = UiTheme.scaled(12, uiScale);
        int topPadding = UiTheme.scaled(15, uiScale);
        int rightPadding = (input instanceof JPasswordField) ? UiTheme.scaled(40, uiScale) : horizontalPadding;
        input.setBorder(BorderFactory.createEmptyBorder(topPadding, horizontalPadding, 0, rightPadding));
        input.setFont(UiTheme.scaledFont(UiTheme.BODY_FONT, Font.PLAIN, 14, uiScale));
        layoutFloatingLabel();
    }

    @Override
    public void doLayout() {
        input.setBounds(0, 0, getWidth(), getHeight());
        if (eyeButton != null) {
            int size = UiTheme.scaled(24, uiScale);
            int x = getWidth() - size - UiTheme.scaled(8, uiScale);
            int y = (getHeight() - size) / 2 + UiTheme.scaled(6, uiScale);
            eyeButton.setBounds(x, y, size, size);
        }
        layoutFloatingLabel();
    }

    private void updateActiveState() {
        boolean shouldFloat = input.hasFocus() || !input.getText().isEmpty();
        if (active == shouldFloat) return;
        active = shouldFloat;
        if (!animation.isRunning()) animation.start();
    }

    private void animateLabel() {
        float target = active ? 1f : 0f;
        if (Math.abs(progress - target) < 0.01f) {
            progress = target;
            animation.stop();
        } else {
            progress += progress < target ? 0.15f : -0.15f;
            progress = Math.max(0f, Math.min(1f, progress));
        }
        layoutFloatingLabel();
        repaint();
    }

    private void layoutFloatingLabel() {
        int y = Math.round((RESTING_Y + ((FLOATING_Y - RESTING_Y) * progress)) * uiScale);
        float size = (RESTING_SIZE + ((FLOATING_SIZE - RESTING_SIZE) * progress)) * uiScale;
        label.setFont(UiTheme.BODY_FONT.deriveFont(size));
        int horizontalPadding = UiTheme.scaled(12, uiScale);
        label.setBounds(horizontalPadding, y, getWidth() - (horizontalPadding * 2), UiTheme.scaled(18, uiScale));
        label.setForeground(blend(UiTheme.TEXT_GRAY, UiTheme.BLUE, progress));
    }

    private Color blend(Color from, Color to, float amount) {
        int r = (int) (from.getRed() + (to.getRed() - from.getRed()) * amount);
        int g = (int) (from.getGreen() + (to.getGreen() - from.getGreen()) * amount);
        int b = (int) (from.getBlue() + (to.getBlue() - from.getBlue()) * amount);
        return new Color(r, g, b);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Darker field color for better contrast
        g2.setColor(new Color(235, 235, 235));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        
        // Border
        if (input.hasFocus()) {
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(new Color(UiTheme.BLUE.getRed(), UiTheme.BLUE.getGreen(), UiTheme.BLUE.getBlue(), 120));
        } else {
            g2.setStroke(new BasicStroke(1f));
            g2.setColor(new Color(210, 210, 210));
        }
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

        g2.dispose();
        super.paintComponent(g);
    }
}
