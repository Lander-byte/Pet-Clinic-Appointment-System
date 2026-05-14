package ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FloatingInput extends JPanel {
    private static final char PASSWORD_ECHO_CHAR = '•';
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
    private float uiScale = 1.0F;

    public FloatingInput(String labelText, boolean secure) {
        this.setLayout((LayoutManager)null);
        this.setOpaque(false);
        this.input = (JTextField)(secure ? new JPasswordField() : new JTextField());
        this.input.setBorder(BorderFactory.createEmptyBorder(15, 12, 0, 12));
        this.input.setBackground(new Color(0, 0, 0, 0));
        this.input.setForeground(UiTheme.TEXT_MAIN);
        this.input.setFont(UiTheme.BODY_FONT.deriveFont(14.0F));
        this.input.setOpaque(false);
        this.input.setCaretColor(UiTheme.BLUE);
        this.label = new JLabel(labelText);
        this.label.setFont(UiTheme.BODY_FONT.deriveFont(12.0F));
        this.label.setForeground(UiTheme.TEXT_GRAY);
        this.add(this.label);
        if (this.input instanceof JPasswordField) {
            ((JPasswordField)this.input).setEchoChar('•');
            this.eyeButton = new JButton() {
                {
                    Objects.requireNonNull(FloatingInput.this);
                }

                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(FloatingInput.this.passwordVisible ? UiTheme.BLUE : UiTheme.TEXT_GRAY);
                    int w = this.getWidth();
                    int h = this.getHeight();
                    int eyeW = 16;
                    int eyeH = 10;
                    int eyeX = (w - eyeW) / 2;
                    int eyeY = (h - eyeH) / 2;
                    g2.setStroke(new BasicStroke(1.5F));
                    g2.drawArc(eyeX, eyeY, eyeW, eyeH, 0, 180);
                    g2.drawArc(eyeX, eyeY, eyeW, eyeH, 180, 180);
                    g2.fillOval(w / 2 - 2, h / 2 - 2, 4, 4);
                    if (!FloatingInput.this.passwordVisible) {
                        g2.drawLine(eyeX + 2, eyeY + 2, eyeX + eyeW - 2, eyeY + eyeH - 2);
                    }

                    g2.dispose();
                }
            };
            this.eyeButton.setBorder((Border)null);
            this.eyeButton.setContentAreaFilled(false);
            this.eyeButton.setFocusPainted(false);
            this.eyeButton.setCursor(Cursor.getPredefinedCursor(12));
            this.eyeButton.addActionListener((e) -> this.togglePasswordVisibility());
            this.add(this.eyeButton);
            this.input.setBorder(BorderFactory.createEmptyBorder(15, 12, 0, 40));
        }

        this.add(this.input);
        this.animation = new Timer(10, (e) -> this.animateLabel());
        this.input.addFocusListener(new FocusAdapter() {
            {
                Objects.requireNonNull(FloatingInput.this);
            }

            public void focusGained(FocusEvent e) {
                FloatingInput.this.updateActiveState();
                FloatingInput.this.repaint();
            }

            public void focusLost(FocusEvent e) {
                FloatingInput.this.updateActiveState();
                FloatingInput.this.repaint();
            }
        });
        this.input.getDocument().addDocumentListener(new DocumentListener() {
            {
                Objects.requireNonNull(FloatingInput.this);
            }

            public void insertUpdate(DocumentEvent e) {
                FloatingInput.this.updateActiveState();
            }

            public void removeUpdate(DocumentEvent e) {
                FloatingInput.this.updateActiveState();
            }

            public void changedUpdate(DocumentEvent e) {
                FloatingInput.this.updateActiveState();
            }
        });
    }

    public void addActionListener(ActionListener l) {
        this.input.addActionListener(l);
    }

    public Dimension getPreferredSize() {
        return new Dimension(200, UiTheme.scaled(54, this.uiScale));
    }

    public Dimension getMinimumSize() {
        return this.getPreferredSize();
    }

    private void togglePasswordVisibility() {
        if (this.input instanceof JPasswordField) {
            this.passwordVisible = !this.passwordVisible;
            ((JPasswordField)this.input).setEchoChar((char)(this.passwordVisible ? '\u0000' : '•'));
            this.eyeButton.repaint();
        }

    }

    public String getText() {
        return this.input instanceof JPasswordField ? new String(((JPasswordField)this.input).getPassword()) : this.input.getText();
    }

    public void setText(String text) {
        this.input.setText(text);
        this.updateActiveState();
    }

    public void clear() {
        this.input.setText("");
        this.updateActiveState();
    }

    public void setUiScale(float uiScale) {
        this.uiScale = uiScale;
        int horizontalPadding = UiTheme.scaled(12, uiScale);
        int topPadding = UiTheme.scaled(15, uiScale);
        int rightPadding = this.input instanceof JPasswordField ? UiTheme.scaled(40, uiScale) : horizontalPadding;
        this.input.setBorder(BorderFactory.createEmptyBorder(topPadding, horizontalPadding, 0, rightPadding));
        this.input.setFont(UiTheme.scaledFont(UiTheme.BODY_FONT, 0, 14, uiScale));
        this.layoutFloatingLabel();
    }

    public void doLayout() {
        this.input.setBounds(0, 0, this.getWidth(), this.getHeight());
        if (this.eyeButton != null) {
            int size = UiTheme.scaled(24, this.uiScale);
            int x = this.getWidth() - size - UiTheme.scaled(8, this.uiScale);
            int y = (this.getHeight() - size) / 2;
            this.eyeButton.setBounds(x, y, size, size);
        }

        this.layoutFloatingLabel();
    }

    private void updateActiveState() {
        boolean shouldFloat = this.input.hasFocus() || !this.input.getText().isEmpty();
        if (this.active != shouldFloat) {
            this.active = shouldFloat;
            if (!this.animation.isRunning()) {
                this.animation.start();
            }

        }
    }

    private void animateLabel() {
        float target = this.active ? 1.0F : 0.0F;
        if (Math.abs(this.progress - target) < 0.01F) {
            this.progress = target;
            this.animation.stop();
        } else {
            this.progress += this.progress < target ? 0.15F : -0.15F;
            this.progress = Math.max(0.0F, Math.min(1.0F, this.progress));
        }

        this.layoutFloatingLabel();
        this.repaint();
    }

    private void layoutFloatingLabel() {
        int y = Math.round((12.0F + -8.0F * this.progress) * this.uiScale);
        float size = (12.0F + -2.0F * this.progress) * this.uiScale;
        this.label.setFont(UiTheme.BODY_FONT.deriveFont(size));
        int horizontalPadding = UiTheme.scaled(12, this.uiScale);
        this.label.setBounds(horizontalPadding, y, this.getWidth() - horizontalPadding * 2, UiTheme.scaled(18, this.uiScale));
        this.label.setForeground(this.blend(UiTheme.TEXT_GRAY, UiTheme.BLUE, this.progress));
    }

    private Color blend(Color from, Color to, float amount) {
        int r = (int)((float)from.getRed() + (float)(to.getRed() - from.getRed()) * amount);
        int g = (int)((float)from.getGreen() + (float)(to.getGreen() - from.getGreen()) * amount);
        int b = (int)((float)from.getBlue() + (float)(to.getBlue() - from.getBlue()) * amount);
        return new Color(r, g, b);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(235, 235, 235));
        g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 12, 12);
        if (this.input.hasFocus()) {
            g2.setStroke(new BasicStroke(1.5F));
            g2.setColor(new Color(UiTheme.BLUE.getRed(), UiTheme.BLUE.getGreen(), UiTheme.BLUE.getBlue(), 120));
        } else {
            g2.setStroke(new BasicStroke(1.0F));
            g2.setColor(new Color(210, 210, 210));
        }

        g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 12, 12);
        g2.dispose();
        super.paintComponent(g);
    }
}
