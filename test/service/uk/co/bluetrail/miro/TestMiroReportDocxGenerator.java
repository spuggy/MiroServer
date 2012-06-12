package uk.co.bluetrail.miro;

import java.io.File;

import uk.co.bluetrail.mobriz.model.MiroTeam;

import junit.framework.TestCase;

public class TestMiroReportDocxGenerator extends TestCase {


	protected MiroTeam miroTeam;
	protected File baseDir ;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	
		this.baseDir = new File("/Users/Richard/Documents/workspace-copy/MiroServer3/web/miro");
	}
	
	public void testDoesItRun() {
		
		try {
			MiroReportDocxGenerator.generateDocx(baseDir, "Testing02_map_100");
		} catch (Exception e) {
			fail("no it doesn't" + e.toString());
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
}
