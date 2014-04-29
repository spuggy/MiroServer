package uk.co.bluetrail.miro;

import junit.framework.TestCase;

import java.awt.*;
import java.io.File;

public class MiroTeamBarChartTest extends TestCase {

	public void testCreate() {
		double[] barValues = { 50.0, 50.0, 0.0, 0.0 };
		  
		double[] barLevels = { 100.0, 38.0, 18.0, 0.0 };    
		
		String[] levelLabels = {"", "High","Medium", "Low" };
		
		
		Color[] barColors = { Color.YELLOW, Color.GREEN, Color.BLUE, Color.RED };
		
		String[] barLabels = {"Energising","Organising","Analysing","Driving"};
		
		
		
		File filePath = new File("web/miro");
		
		
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
