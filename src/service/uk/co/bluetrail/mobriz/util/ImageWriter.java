package uk.co.bluetrail.mobriz.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;


import javax.imageio.ImageIO;
import javax.swing.*;



public class ImageWriter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		
		 
		try {
			ImageWriter.writeString("/Users/Richard/documents/workspace copy/ImageWrite/22221.jpg", "Helooo");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
public static void writeString(String fileName,String text) throws IOException{
		
		BufferedImage image = null;
   
        // Read from a file
        File file = new File(fileName);
        image = ImageIO.read(file);
        
        int h = image.getHeight() ;
        int w = image.getWidth();
        
        Graphics2D g = image.createGraphics();
      
        Font font = new Font("Courier", Font.PLAIN, 8);
        
        
        FontMetrics currentMetrics = g.getFontMetrics(font); 
        
        int stringHeight = currentMetrics.getHeight();
        
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0,  w,stringHeight+4);
  
        g.setColor(Color.GREEN);
    
        g.drawString(text, 3,3+stringHeight);
        
        ImageIO.write(image, "jpeg", file);  
        
    
        
    
	}
    
}
