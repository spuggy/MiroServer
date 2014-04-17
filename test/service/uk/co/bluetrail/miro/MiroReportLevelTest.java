package uk.co.bluetrail.miro;

import junit.framework.*;

public class MiroReportLevelTest extends junit.framework.TestCase {
	
	//h;100;40;m;40;20;l;20;1;a;0;0

	String[] levels = {"h","100","40","m","40","20","l","20","1","a","1","-1"};  
	
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	
	public void testCreate() {
		
		
		MiroReportLevel mtl = new MiroReportLevel(levels);
		
	}
	
	public void testGetLevelHigh() {
		
		
		MiroReportLevel mtl = new MiroReportLevel(levels);
		
		String level = mtl.getLevel(42);
		   
		Assert.assertEquals("Should be high", "H", level);
		
	}
	
	public void testGetLevelLow() {
		
		
		MiroReportLevel mtl = new MiroReportLevel(levels);
		
		String level = mtl.getLevel(14);
		
		Assert.assertEquals("Should be low", "L", level);
		
	}
	
	public void testGetLevelAbsent() {
		
		
		MiroReportLevel mtl = new MiroReportLevel(levels);
		
		String level = mtl.getLevel(0.00);
		
		Assert.assertEquals("Should be absent", "A", level);
		
	}
	
	public void testGetLevelMedium() {
		
		
		MiroReportLevel mtl = new MiroReportLevel(levels);
		
		String level = mtl.getLevel(25);
		
		Assert.assertEquals("Should be medium", "M", level);
		
	}
	
	
	public void testGetLevelShouldThrowException() {
		
		
		MiroReportLevel mtl = new MiroReportLevel(levels);
		
		try {
			String level = mtl.getLevel(-100);
			Assert.fail("should have thrown an exception v=-100");
		} catch(Exception e) {
			Assert.assertTrue(true);
		}
		
		try {
			String level = mtl.getLevel(101);
			Assert.fail("should have thrown an exception v=101");
		} catch(Exception e) {
			Assert.assertTrue(true);
		}
		
		
	}
	
	public void testCrazyRobError() {
		
		
		
		MiroReportLevel mtl = new MiroReportLevel(levels);
		
		try {
			String level = mtl.getLevel(19.047619047619047);
			Assert.assertTrue(true);
		} catch(Exception e) {
			Assert.fail(" exception " + e.getMessage());
			
		}	
		
	
		
	}
	 
	
	
}
