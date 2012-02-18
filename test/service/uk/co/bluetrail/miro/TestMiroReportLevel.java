package uk.co.bluetrail.miro;

import junit.framework.*;

public class TestMiroReportLevel extends junit.framework.TestCase {

	public void testCreate() {
		
		String[] levels = {"h","100","40","m","39","20","l","19","10","a","0","0"};
		
		MiroReportLevel mtl = new MiroReportLevel(levels);
		
	}
	
	public void testGetLevelHigh() {
		
		String[] levels = {"h","100","40","m","39","20","l","19","10","a","0","0"};
		
		MiroReportLevel mtl = new MiroReportLevel(levels);
		
		String level = mtl.getLevel(42);
		
		Assert.assertEquals("Should be high", "H", level);
		
	}
	
	public void testGetLevelLow() {
		
		String[] levels = {"h","100","40","m","39","20","l","19","10","a","0","0"};
		
		MiroReportLevel mtl = new MiroReportLevel(levels);
		
		String level = mtl.getLevel(14);
		
		Assert.assertEquals("Should be low", "L", level);
		
	}
	
	public void testGetLevelAbsent() {
		
		String[] levels = {"h","100","40","m","39","20","l","19","10","a","0","0"};
		
		MiroReportLevel mtl = new MiroReportLevel(levels);
		
		String level = mtl.getLevel(0);
		
		Assert.assertEquals("Should be absent", "A", level);
		
	}
	
	public void testGetLevelMedium() {
		
		String[] levels = {"h","100","40","m","39","20","l","19","10","a","0","0"};
		
		MiroReportLevel mtl = new MiroReportLevel(levels);
		
		String level = mtl.getLevel(25);
		
		Assert.assertEquals("Should be medium", "M", level);
		
	}
	
	
	public void testGetLevelShouldThrowException() {
		
		String[] levels = {"h","100","40","m","39","20","l","19","10","a","0","0"};
		
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
	
	
	 
	
	
}
