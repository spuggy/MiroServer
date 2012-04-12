package uk.co.bluetrail.miro;

import java.awt.Color;
import java.io.File;

import junit.framework.TestCase;

public class TestMiroPieChartGenerator extends TestCase {
	
	
	public void testCreatePie() {

		int[] pieValues = { 30, 20, 15, 13 };
		String[] pieLabels1 = { "Energising Mode", "Organising Mode",
				"Analysing Mode", "Driving Mode" };

		String[] pieLabels2 = { "Leading", "Supporting", "Supplementary",
				"Dormant" };
		String[] pieLabels3 = { "Engaged", "Engaged", "Disengaged", "Disengaed" };

		boolean[] pieExplode = { false, false, true, true };
		// String[] pieColors = { "yellow", "green", "blue", "red" };
		Color[] pieColors = { Color.YELLOW, Color.GREEN, Color.BLUE, Color.RED };

		MiroPieChartGenerator pieChart = new MiroPieChartGenerator("Your MiRo Results Chart", pieValues,
				pieLabels1, pieLabels2, pieLabels3, pieExplode, pieColors,true,true);  

		File filePath = new File("/Users/Richard/Documents/workspace-copy/MiroServer3/web/miro/out");
		
		
		String fileName = "test_piechart.jpg" ;
		
		try {
			pieChart.createPie(filePath.getAbsolutePath(), fileName);
			
		} catch (Exception e) {
			fail("Exception createPie " + e.toString());
		}
		
		File f = new File(filePath+"/"+fileName);
		
		if(!f.exists()) {
			fail("file no found");
		}
		
		
		
	}
	

}
