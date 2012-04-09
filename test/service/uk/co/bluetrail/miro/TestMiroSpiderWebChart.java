package uk.co.bluetrail.miro;

import java.awt.Color;
import java.io.File;

import junit.framework.TestCase;

public class TestMiroSpiderWebChart extends TestCase {

	

	public void testGenerate() {
		
		int[] values = {10,20,30,40,50,60,70,80};
		String[] labels = {"Intuition","Energising","Feeling","Organising","Sensing","Analysing","Thinking","Driving"};
		
		
		
		
		
		File filePath = new File("/Users/Richard/Documents/workspace-copy/MiroServer3/web/miro");
		
		MiroSpiderWebChart msc = new MiroSpiderWebChart(filePath, Color.BLUE);
		
		String fileName = "barchart.png" ;
		
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
