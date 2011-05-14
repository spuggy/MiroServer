package uk.co.bluetrail.miro;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class MiroTeamPieChart {

	private int teamPieWidth;
	private int teamPieHeight;
	private BufferedImage backgroundImage;
	private Graphics2D graphics2D;
	int x = 0 ;
	int y = 0 ;
	int imgCount = 0 ;
	int imagesWide = 0;
	int thumbWidth;
	int thumbHeight;
	
	public MiroTeamPieChart(int teamPieWidth, int teamPieHeight, int thumbWidth, int thumbHeight) {
		this.teamPieWidth = teamPieWidth;
		this.teamPieHeight = teamPieHeight;
		this.thumbWidth = thumbWidth;
		this.thumbHeight = thumbHeight;
		this.imagesWide = this.teamPieWidth / this.thumbWidth;
		int x = 0 ;
		int y = 0 ;
		int imgCount = 0 ;
	}

	public BufferedImage getBufferedImage() {
		
		graphics2D.dispose();
		
		return backgroundImage ;
		
	}
	
	

	public void add(BufferedImage thumbnailPie) {

		if(backgroundImage==null || graphics2D ==null) {
			backgroundImage = new BufferedImage(teamPieWidth, teamPieHeight,BufferedImage.TYPE_INT_RGB);
			graphics2D = backgroundImage.createGraphics();
			graphics2D.setBackground(Color.WHITE);
			graphics2D.setColor(Color.WHITE);
			graphics2D.fillRect(0,0,  teamPieWidth,teamPieHeight);
		}
		
		graphics2D.drawImage(thumbnailPie, x, y, thumbWidth, thumbHeight, null);

		imgCount++ ;
		x = x + thumbWidth;
		
		if(imgCount >= imagesWide) {
			y = y + thumbHeight;
			x=0;
			imgCount = 0 ;
		}
		
		
	}

}
