package uk.co.bluetrail.miro;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import uk.co.bluetrail.mobriz.serviceDTO.TeamMapDTO;
import junit.framework.Assert;
import junit.framework.TestCase;
import static org.mockito.Mockito.*;

public class MiroTeamMapChartTest extends TestMiroTeamReport {

	
	public void testCreate() {
		
		
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
