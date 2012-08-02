package uk.co.bluetrail.miro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class MiroDynamicContentFileGenerator {

	private File baseDirectory;
	private String srcFilename;
	private StringBuffer content;

	public MiroDynamicContentFileGenerator(File baseDirectory, String srcFilename) {
		
		this.baseDirectory = baseDirectory;
		this.srcFilename = srcFilename;
		this.content = new StringBuffer();
		
	}
	
	public String getContent() {
		return this.content.toString();
	}
	
	public void addContent(String id,String content) {
		
		this.content.append("<div id=\"" );
		this.content.append(id);
		this.content.append("\">" );
		this.content.append("<p>");
		if(content!=null) {
            String parsed =  content.replaceAll("(\r\n\r\n|\n\r\n\r|\r\r|\n\n)", "</p><p>");
            parsed =  parsed.replaceAll("(\r\n|\n\r|\r|\n)", "<br/>");

            this.content.append(parsed);
		} 
		this.content.append("</p>");
		this.content.append("</div>");
		
		
	}
	
	public void genertate() throws IOException {
	 
		String filepath = this.baseDirectory.getAbsoluteFile() + File.separator + "out" + File.separator+ this.srcFilename;

		
		FileOutputStream fos = null;
		OutputStreamWriter out = null;
		
		//use buffering
	    try {
			fos = new FileOutputStream(filepath); 
			out = new OutputStreamWriter(fos, "UTF-8");
		    out.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		   
		    out.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.miro-assessment.com/dtds/xhtml1-transitional.dtd\">");
		    out.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
		    out.append("<head></head>");
		    out.append("<body>");
		    out.append( this.content);
	        out.append("</body></html>");
	    } 
	    finally {
	      try {
	    	  out.close();
	      } catch(Exception e) {}
	    }
		
	}
	
	
	
}
