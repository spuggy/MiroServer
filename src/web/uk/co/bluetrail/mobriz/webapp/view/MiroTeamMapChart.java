package uk.co.bluetrail.mobriz.webapp.view;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class MiroTeamMapChart  {
    private int circleRadius = 20, borderWidth = 2;
    Color circleColor = Color.black, borderColor = Color.white;
    private int fontSize = 15;
    
    public void setFontSize(int f) {
    	this.fontSize = f;
    }
    

    public void setCircleRadius(int w) {
        circleRadius = w;
    }

    public void setCircleBorder(int b) {
        borderWidth = b;
     }

    public void setCircleColor(Color c) {
        circleColor = c;
    }

    public void setCircleBorderColor(Color bc) {
        borderColor = bc;
    }

    public BufferedImage createTeamChart(String backGroundFileName, String[] initials, int[] x, int[] y) throws Exception {

        BufferedImage image = null;
        File file = new File(backGroundFileName);
        image = ImageIO.read(file);
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        for(int i = 0;i < initials.length;i++) {
            g.setColor(borderColor);
            g.fillOval(x[i]-circleRadius, y[i]-circleRadius, circleRadius*2, circleRadius*2);
            g.setColor(circleColor);
            g.fillOval(x[i]-circleRadius+borderWidth, y[i]-circleRadius+borderWidth, 2*(circleRadius-borderWidth), 2*(circleRadius-borderWidth));
            g.setFont(new Font("Calibri", Font.BOLD, fontSize));
            g.setColor(Color.WHITE);
            FontMetrics fm = g.getFontMetrics(g.getFont());
            int strW = fm.stringWidth(initials[i]);
            int strH = fm.getAscent();
            g.drawString(initials[i], x[i] - strW/2, y[i] + strH/2);
        }
        
        g.dispose();
        return image;
       
    }
}
