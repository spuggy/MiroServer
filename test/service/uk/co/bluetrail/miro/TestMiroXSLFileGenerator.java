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
		
		String filename = "testreplace.xsl";

        HashMap<String,String> strings =  new HashMap<String,String>();

        strings.put("#FOOTER","<footer>");
        strings.put("#HEADER","<header>");


		try {
            MiroXSLFileGenerator.generate(this.baseDir, filename, strings);

        } catch(Exception e) {
			fail("Exception creating file " + e.getMessage());
		}
			
		String filepath = this.baseDir.getAbsoluteFile() + File.separator + "out" + File.separator+ filename;

		File g = new File(filepath);
		
		assertTrue("Does the file exist?",g.exists());
		
	}
	
	
}
