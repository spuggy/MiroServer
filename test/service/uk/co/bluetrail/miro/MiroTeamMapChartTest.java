package uk.co.bluetrail.miro;

import junit.framework.Assert;

import java.io.File;
import java.util.ArrayList;

public class MiroTeamMapChartTest extends MiroTeamReportTest {

	
	public void testCreate() {

        defaultTeam();
		
		ArrayList teamMapData = this.teamResults;
		
		boolean teamMapImageEngaged = true;
		
		MiroTeamMapPlotter teamMapPlotter = new MiroTeamMapPlotter(teamMapData,teamMapImageEngaged);
		MiroTeamMapChart miroTeamMapChart = new MiroTeamMapChart();
		
		String fileName = "teampagtest.jpg";
		
	
		
		try {
			miroTeamMapChart.createTeamChartFile(fileName,this.baseDir.getAbsolutePath(), teamMapPlotter);
		
		} catch (Exception e) {
			Assert.fail(" could not create image " + e.getMessage());
		}
	
		File jpg = new File(this.baseDir + "/out/" + fileName);
		
		assertTrue(fileName +" Exists" , jpg.exists());
		
		
		
		
	}	
	
	
}
