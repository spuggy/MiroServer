package uk.co.bluetrail.miro;

import junit.framework.TestCase;

import java.awt.*;
import java.io.File;

public class MiroSpiderWebChartTest extends TestCase {

	

	public void testGenerate() {
		
		int[] values = {10,20,30,40,50,60,70,80};
		String[] labels = {"Intuition","Energising","Feeling","Organising","Sensing","Analysing","Thinking","Driving"};
		
		
		
		
		
		File filePath = new File("web/miro");
		
		MiroSpiderWebChart msc = new MiroSpiderWebChart(filePath, Color.BLUE);
		
		String fileName = "spiderchart.png" ;
		
		try {
			msc.createChart(fileName, values,labels);
		} catch (Exception e) {
			fail("Exception createTeamChart " + e.toString());
		}
		
		File f = new File(filePath+"/out/"+fileName);
		
		if(!f.exists()) {
			fail("file no found");
		}
		
		
	}

	
	
	
}
