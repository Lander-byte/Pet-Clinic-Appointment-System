package PetClinic.ui.components;

import javax.swing.JPanel;
import java.awt.BasicStroke;
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
        Font logo = UiTheme.LOGO_FONT.deriveFont((float) fontSize);
        g2.setFont(logo);
        int careWidth = g2.getFontMetrics().stringWidth("Care");
        int totalWidth = careWidth + g2.getFontMetrics().stringWidth("Haven");
        int x = (getWidth() - totalWidth) / 2;
        int y = fontSize;
        g2.setColor(UiTheme.LOGO_BLUE);
        g2.drawString("Care", x, y);
        g2.setColor(UiTheme.ORANGE);
        g2.drawString("Haven", x + careWidth, y);

        Font vetFont = new Font("Lilita One", Font.BOLD, Math.max(12, fontSize / 5));
        g2.setFont(vetFont);
        String vet = "Veterinary";
        int vetWidth = g2.getFontMetrics().stringWidth(vet);
        int vetX = (getWidth() - vetWidth) / 2;
        int vetY = y + (fontSize / 3);
        g2.setColor(UiTheme.LABEL_GRAY);
        g2.drawString(vet, vetX, vetY);

        g2.setStroke(new BasicStroke(2));
        int lineY = vetY - 8;
        g2.drawLine(vetX - 115, lineY, vetX - 25, lineY);
        g2.drawLine(vetX + vetWidth + 25, lineY, vetX + vetWidth + 115, lineY);
        g2.dispose();
    }
}