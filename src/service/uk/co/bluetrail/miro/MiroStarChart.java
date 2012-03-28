package uk.co.bluetrail.miro;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

public class MiroStarChart {



	int[] x = {190,305,356,305,190, 66, 30,66};
	int[] y = {30, 67, 180,289,322,289,180,63};
	String IMG_NAME = "miro.jpg";
	File filePath = null;
	Color textColor = null;
	
	//images start from 12 oclock
	
	public MiroStarChart(File baseDirectory,  Color textColor) {
	
		this.filePath = baseDirectory;
		this.textColor = textColor;
	}
	
	public void createChart(String outputFileName, int[] values) throws Exception {

		if(values.length!=8) {
			throw new RuntimeException("You need 8 values");
		}
		
		
        BufferedImage image = null;
        String filePath = this.filePath + "/images/miroteamreport/miroStartChartBackground.png";
        File file = new File(filePath);
        image = ImageIO.read(file);
        Graphics2D g = (Graphics2D)image.getGraphics();

        for(int i = 0;i < values.length;i++) {
            
            g.setFont(new Font("Calibri", Font.BOLD, 25));
            g.setColor(this.textColor);
            FontMetrics fm = g.getFontMetrics(g.getFont());
            
            g.drawString(values[i]+"", x[i] , y[i] );
        }
        
        g.dispose();
        

		char seperator = File.separatorChar;

		String outputFile = new String(this.filePath.getAbsolutePath()+seperator+"out"+seperator+outputFileName);

		FileOutputStream out = new FileOutputStream(outputFile);

        ImageIO.write(image, "jpg", out);
        out.close();
        
       
    }
	
	
	

}
