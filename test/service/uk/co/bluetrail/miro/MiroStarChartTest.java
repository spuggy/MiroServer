package uk.co.bluetrail.miro;

import java.awt.Color;
import java.io.File;

import junit.framework.TestCase;

public class MiroStarChartTest extends TestCase {
	
	public void testGenerate() {
		
		int[] values = {10,20,30,40,50,60,70,80};
		
		File filePath = new File("/Users/RSpence1/documents/workspace-copy/MiroServer/web/miro");
		
		MiroStarChart msc = new MiroStarChart(filePath, Color.BLUE);
		
		String fileName = "arg.png" ;
		
		try {
			msc.createChart(fileName, values);
		} catch (Exception e) {
			fail("Exception createTeamChart " + e.toString());
		}
		
		File f = new File(filePath+"/out/"+fileName);
		
		if(!f.exists()) {
			fail("file no found");
		}
		
		
	}

}
