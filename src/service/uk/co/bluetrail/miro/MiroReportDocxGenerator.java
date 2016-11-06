package uk.co.bluetrail.miro;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.AltChunkType;
import org.docx4j.wml.Br;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.STBrType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class MiroReportDocxGenerator {
	
	private final static Log log = LogFactory.getLog(MiroReportDocxGenerator.class);
	

	
	public static void generateDocx(File baseDir,String miroReportName) throws Exception {
	
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

	
			Document dom = db.parse(baseDir.getAbsolutePath()+ "/out/" + miroReportName + ".xhtml");
			
			Element docEle = dom.getDocumentElement();

			//get a nodelist of elements
			NodeList l = docEle.getElementsByTagName("div");	
			
			File template = new File(baseDir.getAbsolutePath()+ "/xhtml/reptemplate.docx");
			
			
			WordprocessingMLPackage wordMLPackage = wordMLPackage = WordprocessingMLPackage.load(template);
			
			
			ObjectFactory factory = Context.getWmlObjectFactory();
			
			for(int i = 2 ; i < l.getLength();i++){
			
				Node n  = l.item(i);
			String html =  getHtml(nodeToString(n)); 
			
			System.out.println(html);
			
			
			
	
			wordMLPackage.getMainDocumentPart().addAltChunk(AltChunkType.Html, html.getBytes()); 

			
			
			Br objBr = new Br();
			objBr.setType(STBrType.PAGE);
			
			wordMLPackage.getMainDocumentPart().addObject(objBr);
			
			
			
			}
			
			File outFile = new File(baseDir.getAbsolutePath()+ "/out/" + miroReportName + ".docx");
			
			wordMLPackage.save(outFile);		
		

	}	
	
	private static String getHtml(String s) {
		
		return "<html><head><title>Import me</title></head><body>" +  s + "</body></html>"; 
		
	}
	
	
	
	private static String nodeToString(Node node) {
		  StringWriter sw = new StringWriter();
		  try {
		    Transformer t = TransformerFactory.newInstance().newTransformer();
		    t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		    t.transform(new DOMSource(node), new StreamResult(sw));
		  } catch (TransformerException te) {
		    System.out.println("nodeToString Transformer Exception");
		  }
		  return sw.toString();
		}
	
	
	
}
