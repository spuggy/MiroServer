package uk.co.bluetrail.miro;

import junit.framework.TestCase;

import java.io.File;
import java.util.HashMap;

public class TestMiroXSLFileGenerator extends TestCase {

	private File baseDir;

	protected void setUp() throws Exception {
		super.setUp();
		this.baseDir = new File("/miro-reports");
	}

	
	public void testGenerate() {
		
		String filename = "testmiro2fo.xsl";

        HashMap<String,String> strings =  new HashMap<String,String>();

        strings.put("#PAGECOUNT","10");
        strings.put("#DATEOFREPORT","April 2012");


		try {
            MiroXSLFileGenerator.generate(this.baseDir, filename, "replace_report", strings);

        } catch(Exception e) {
			fail("Exception creating file " + e.getMessage());
		}
			
		String filepath = this.baseDir.getAbsoluteFile() + File.separator + "out" + File.separator+ "replace_report.xsl" ;

		File g = new File(filepath);
		
		assertTrue("Does the file exist?",g.exists());
		
	}
	
	
}
