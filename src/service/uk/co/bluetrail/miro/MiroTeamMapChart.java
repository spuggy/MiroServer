package uk.co.bluetrail.miro;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MiroTeamMapChart  {
	
	private static int BIG_BLOB_RADIUS = 30;
	private static int SMALL_BLOB_RADIUS = 20;
	private static int BIG_BLOB_FONT = 20 ;
	private static int SMALL_BLOB_FONT = 15 ;
	
	
    private int circleRadius = BIG_BLOB_RADIUS, borderWidth = 2;
    private int fontSize = BIG_BLOB_FONT;
    Color circleColor = Color.black, borderColor = Color.white;

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
    
    
    public void createTeamChartFile(String fileName,String filePath,MiroTeamMapPlotter plotter  ) throws Exception {
    
    	java.awt.image.BufferedImage teamMap = createTeamChart(filePath, plotter);
		
		char seperator = File.separatorChar;
		
		String file = new String(filePath+seperator+"out"+seperator+fileName);
		

		
		FileOutputStream out = new FileOutputStream(file);
		
        ImageIO.write(teamMap, "jpg", out);
        out.close();
    	
    }
    
    public BufferedImage createTeamChart(String filePath,MiroTeamMapPlotter plotter ) throws Exception {
       
    	
    	
    	String[] initials = plotter.getInitialsArray();
    	int[] x = plotter.getX();
    	int[] y = plotter.getY();
    	
    	if(!plotter.isBigBlobs()) {
    		circleRadius = SMALL_BLOB_RADIUS ;
    	    fontSize = SMALL_BLOB_FONT;
    	}
    	
        BufferedImage image = null;
        File file = new File(filePath + plotter.getBacgroundImage());
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
