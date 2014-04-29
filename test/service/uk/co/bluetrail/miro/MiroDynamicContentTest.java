package uk.co.bluetrail.miro;

import junit.framework.TestCase;

import java.io.File;

public class MiroDynamicContentTest extends TestCase {

	private File baseDir;

	protected void setUp() throws Exception {
		super.setUp();
		this.baseDir = new File("web/miro");
	}
	
	public void testAdd() {
		
		MiroDynamicContentFileGenerator f = new MiroDynamicContentFileGenerator(this.baseDir,"test.xhtml");
		
		f.addContent("d1", "hello I am \n text");
		
		String c = f.getContent();
		
		
		String expected = "<div id=\"d1\"><p>hello I am <br/> text</p></div>";

		assertEquals(c,expected);
		
	}
	
	public void testGenerate() {
		
		String filename = "test.xhtml";
		
		MiroDynamicContentFileGenerator f = new MiroDynamicContentFileGenerator(this.baseDir,filename);
		
		f.addContent("d1", "hello I am \n text");
		
		String c = f.getContent();
		
		
		String expected = "<div id=\"d1\"><p>hello I am <br/> text</p></div>";

		assertEquals(c,expected);
		
		try {
			f.genertate();
		} catch(Exception e) {
			fail("Exception creating file " + e.getMessage());
		}
			
		String filepath = this.baseDir.getAbsoluteFile() + File.separator + "out" + File.separator+ filename;

		File g = new File(filepath);
		
		assertTrue(g.exists());
		
	}
	
	
}
