package uk.co.bluetrail.miro;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants; 
import org.xml.sax.SAXException;

import com.lowagie.text.pdf.PdfReader;

public class MiroReportPDFGenerator {
	
	private final static Log log = LogFactory.getLog(MiroReportPDFGenerator.class);
	
	public static void generatePDF(File baseDir,String miroReportName) throws Exception {
      
            
            log.debug("Preparing..." + miroReportName);

               
     
     
            // Setup input and output files            
            File xmlfile = new File(baseDir, "out/" + miroReportName+".xhtml");
            File xsltfile = new File(baseDir, "xhtml/miro2fo.xsl");
            File pdffile = new File(baseDir, "out/" + miroReportName+".pdf");

            log.debug("Input: XML (" + xmlfile + ")");
            log.debug("Stylesheet: " + xsltfile);
            log.debug("Output: PDF (" + pdffile + ")");
            
            log.debug("Transforming...");
            
            // configure fopFactory as desired
            FopFactory fopFactory = FopFactory.newInstance();
            
            fopFactory.setUserConfig(new File(baseDir, "fop/fopconfig.xml"));

          
           
            
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // configure foUserAgent as desired

            // Setup output
            OutputStream out = new java.io.FileOutputStream(pdffile);
            out = new java.io.BufferedOutputStream(out);
            
            try {
                // Construct fop with desired output format
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
                      
               
                
                
                // Setup XSLT
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));
                
                // Set the value of a <param> in the stylesheet
                transformer.setParameter("versionParam", "2.0");
            
                // Setup input for XSLT transformation
                Source src = new StreamSource(xmlfile);
                
            
                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());
    
                // Start XSLT transformation and FOP processing
                transformer.transform(src, res);
            } finally {
                out.close();
            }
            log.debug("Checking report pages !!!");
            //try and read the file to make sure it is ok. this will throw and exception
        	PdfReader pdfReader = new PdfReader(pdffile.getPath());
        	log.debug("No of Pages:"+pdfReader.getNumberOfPages());
		    
            
        	log.debug("Success for ! " + miroReportName);
       
    }
}
