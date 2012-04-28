package uk.co.bluetrail.miro;

import java.awt.Color;
import java.io.File;

import junit.framework.TestCase;

public class TestMiroTeamBarChart extends TestCase {

	public void testCreate() {
		double[] barValues = { 180.0, 44.0, 30.0, 26.0 };
		  
		double[] barLevels = { 90.0, 80.0, 38.0,0.0 };    
		
		String[] levelLabels = {"", "High","Medium", "Low" };
		
		
		Color[] barColors = { Color.YELLOW, Color.GREEN, Color.BLUE, Color.RED };
		
		String[] barLabels = {"Energising","Organising","Analysing","Driving"};
		
		
		
		File filePath = new File("/Users/Richard/Documents/workspace-copy/MiroServer3/web/miro");
		
		
		uk.co.bluetrail.miro.MiroTeamBarChart barChart = new MiroTeamBarChart(filePath);
		
		
		//the first param is the directory where the chart goes
		
		String fileName = "barchart-bar-better.png" ;
		
		barChart.createBarChart("",fileName,barValues,barLabels,barColors,barLevels,levelLabels);
		
		
		
		File f = new File(filePath+"/out/"+fileName);
		
		if(!f.exists()) {
			fail("file no found");
		}
		
	}
	
}
