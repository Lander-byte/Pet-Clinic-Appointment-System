package ui.components;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Logo extends JPanel {
    private final int fontSize;

    public Logo(int fontSize) {
        this.fontSize = fontSize;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Font logoFont = UiTheme.LOGO_FONT.deriveFont((float) fontSize);
        g2.setFont(logoFont);
        
        String firstPart = "Care";
        String secondPart = "Haven";
        
        int careWidth = g2.getFontMetrics().stringWidth(firstPart);
        int havenWidth = g2.getFontMetrics().stringWidth(secondPart);
        int totalWidth = careWidth + havenWidth;
        
        int x = (getWidth() - totalWidth) / 2;
        int y = (getHeight() + g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2 - (fontSize / 4);

        // Draw "Care"
        g2.setColor(UiTheme.BLUE);
        g2.drawString(firstPart, x, y);
        
        // Draw "Haven"
        g2.setColor(UiTheme.ORANGE);
        g2.drawString(secondPart, x + careWidth, y);

        // Subtitle "Veterinary"
        Font vetFont = new Font("Segoe UI", Font.BOLD, Math.max(12, fontSize / 4));
        g2.setFont(vetFont);
        String subtitle = "VETERINARY CLINIC";
        int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);
        int sx = (getWidth() - subtitleWidth) / 2;
        int sy = y + (fontSize / 3) + 10;
        
        g2.setColor(UiTheme.TEXT_GRAY);
        g2.drawString(subtitle, sx, sy);

        // Decorative Lines
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(200, 200, 200));
        int lineWidth = (totalWidth - subtitleWidth) / 2 - 20;
        if (lineWidth > 0) {
            g2.drawLine(sx - lineWidth - 10, sy - 5, sx - 10, sy - 5);
            g2.drawLine(sx + subtitleWidth + 10, sy - 5, sx + subtitleWidth + 10 + lineWidth, sy - 5);
        }

        g2.dispose();
    }
}
