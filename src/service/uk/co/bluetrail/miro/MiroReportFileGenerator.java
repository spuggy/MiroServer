package uk.co.bluetrail.miro;

/**
 *
 * Generates Miro Report XML File
 * 
 * @author Richard Spence June 2008 
 *
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MiroReportFileGenerator {

	private final static Log log = LogFactory
			.getLog(MiroReportFileGenerator.class);

	private Document sourceDocument = null;
	private Document dynamicDocument = null;
	private Document destDocument = null;
	private Element htmlElement = null;
	private Element bodyElement = null;
	private File baseDirectory = null;
	private String srcFilename = "mirosource11.xhtml";
	private String srcFolder = "miro2/xhtml";
	private String outputFolder = "out";

	private String dynamicFileName;

	/**
	 * @param baseDirectory
	 *            Base directory where two folders exists "xhtml" (for source)
	 *            and "out" (where the resulting file is generated)
	 */
	public MiroReportFileGenerator(File baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

	/**
	 * @param baseDirectory
	 *            Base directory where two folders exists "xhtml" (for source)
	 *            and "out" (where the resulting file is generated)
	 * @param srcFilename
	 *            To specify a different source filename. By defualt the source
	 *            filename is "mirosource.xhtml"
	 */
	public MiroReportFileGenerator(File baseDirectory, String srcFilename) {
		this.baseDirectory = baseDirectory;
		this.srcFilename = srcFilename;
	}

	public MiroReportFileGenerator(File baseDirectory, String srcFilename,String dynamicFileName) {
			
		this.baseDirectory = baseDirectory;
		this.srcFilename = srcFilename;
		this.dynamicFileName = dynamicFileName;
	}

	private void replaceVariables(Map<String, String> variables) {
		Iterator<String> mapIter = variables.keySet().iterator();
		while (mapIter.hasNext()) {
			String tagId = mapIter.next();
			// Get the Element by imageId
			Element element = this.sourceDocument.getElementById(tagId);
			if (element != null) {
				element.setTextContent((variables.get(tagId)));
			}
		}
	}

	private void replaceImagesPaths(Map<String, String> imageMap) {
		Iterator<String> mapIter = imageMap.keySet().iterator();
		while (mapIter.hasNext()) {
			String imageId = mapIter.next();
			// Get the Element by imageId
			Element element = this.sourceDocument.getElementById(imageId);
			if (element != null)
				element.setAttribute("src", imageMap.get(imageId));
		}
	}

	private void cloneHtmlTag() {
		// Clone HTML tag
		htmlElement = destDocument.createElement("html");
		Node srcHtmlNode = sourceDocument.getElementsByTagName("html").item(0);
		/*
		 * for(int i = 0; i < srcHtmlNode.getAttributes().getLength(); i++) {
		 * htmlElement
		 * .setAttribute(srcHtmlNode.getAttributes().item(i).getNodeName(),
		 * srcHtmlNode.getAttributes().item(i).getNodeValue()); }
		 */
		destDocument.appendChild(htmlElement);

		bodyElement = destDocument.createElement("body");

		htmlElement.appendChild(bodyElement);

	}

	private void addPagesToReport(List<MiroPage> sourcePages, Map<String, String> imgNames,Map<String, String> variables)
			throws ParserConfigurationException, SAXException, IOException {

		log.debug("[XML Manipulation] Starting..0");

		boolean addDivFlag = false;
		for (int srcPgIdx = 0; srcPgIdx < sourcePages.size(); srcPgIdx++) {
			addDivFlag = false;
			MiroPage tempPage = sourcePages.get(srcPgIdx);
			Element divEle = destDocument.createElement("div");
			divEle.setAttribute("id", srcPgIdx + "");
			log.debug("[XML Manipulation] Starting..1");
			for (int tmpPgIdx = 0; tmpPgIdx < tempPage.getLength(); tmpPgIdx++) {
				MiroPageElement pe = tempPage.get(tmpPgIdx);
				Element element = getElement(pe.getId());
				
				log.debug("[XML Manipulation] Starting..2");
				if (element != null) {
					addDivFlag = true;
					for (int i = 0; i < element.getChildNodes().getLength(); i++) {
						Node nodeTemp = destDocument.importNode(element.getChildNodes().item(i), true);
						adjustIdOfNode(nodeTemp, pe,imgNames,variables);
						divEle.appendChild(nodeTemp);
					}
					
				} else {
					element = sourceDocument.getElementById("missing_element");
				}
			}
			if (addDivFlag) {
				bodyElement.appendChild(divEle);
			}
		}

		log.debug("[XML Manipulation] Complete!");

	}
	
	

	

	private Element getElement(String id) {
		Element e = sourceDocument.getElementById(id);
		if(e==null && this.dynamicDocument!=null) {
			e = dynamicDocument.getElementById(id);
		}
		return e;
	}

	private void adjustIdOfNode(Node nodeTemp, MiroPageElement pe, Map<String, String> imgNames, Map<String, String> vars) {
		
		if(nodeTemp == null) {
			return;
		}
		
		short nt = nodeTemp.getNodeType();
		
		if(nt!=Node.ELEMENT_NODE) {
			return;
		}
		
		
		Element e = (Element) nodeTemp;
		String id = e.getAttribute("id");
		if (id != null && !id.equals("")) {
			e.setAttribute("id", pe.addSuffix(id));
			
			//change the image source
			String imgSrc = imgNames.get(pe.addSuffix(id));
			if(imgSrc!=null) {
				e.setAttribute("src", imgSrc);
			} 
			
			//if this is a variable then flip out the text
			String var = vars.get(pe.addSuffix(id));
			if(var !=null) {
				e.setTextContent(var);
			}
			
			
			
		}
		
		
		
		
		//check to chamhe image src
		//String id = e.getAttribute("src");
		
		
		
		for (int i = 0; i < e.getChildNodes().getLength(); i++) {
			Node nextNode = e.getChildNodes().item(i);
			adjustIdOfNode(nextNode, pe,imgNames,vars);
		}

	}

	private void writeReportToFile(String filePath, Document document)
			throws TransformerFactoryConfigurationError, TransformerException {
		Transformer tFormer;
		document.normalize();
		tFormer = TransformerFactory.newInstance().newTransformer();
		tFormer.setOutputProperty(OutputKeys.METHOD, "xml");
		tFormer.setParameter(OutputKeys.ENCODING, "UTF-8");
		Source source = new DOMSource(document);
		Result result = new StreamResult(new File(filePath).toURI().getPath());
		tFormer.transform(source, result);
	}

	private String makeReportFilename(Map<String, String> variables) {
		return variables.get("reportFileName") + ".xhtml";
	}

	private String getFilePathToWrite(String folder, String filename) {
		String ptw = baseDirectory.toString() + File.separatorChar + folder
				+ File.separatorChar + filename;
		log.debug("pathtowrite=" + ptw);
		return ptw;
	}

	/**
	 * @param pages
	 *            The list of pages to pick up from the source file
	 * @param variables
	 *            The variables to generate destination report name
	 * @param imgNames
	 *            Map that maps the image to its relative image path
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public void generate(List<MiroPage> pages, Map<String, String> variables,
			Map<String, String> imgNames) throws ParserConfigurationException,
			SAXException, IOException, TransformerFactoryConfigurationError,
			TransformerException {

		long start = System.currentTimeMillis();
		log.debug("[Generating Report]");
		DocumentBuilder sourceParser = null;

		log.debug("Creating Parser..");
		// sourceParser =
		// DocumentBuilderFactory.newInstance().newDocumentBuilder();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		
		// specifies that the builder produced by this factory will
		// validate documents as they are parsed.
		factory.setValidating(false);
		
	
		

		log.debug("Creating Parser..22");
		// this loader will validate XML-input:
		sourceParser = factory.newDocumentBuilder();
		
		log.debug("Creating Parser..setting resolver to null");
		sourceParser.setEntityResolver(null);

		// this loader will validate XML-input:
		DocumentBuilder loader = factory.newDocumentBuilder();

		// Clean non utf-8 Characters
		// System.out.println("Clean non utf-8 characters from source file..");
		// FileEncoder.textToUtf8(getFilePathToWrite(srcFolder, srcFilename),
		// getFilePathToWrite(srcFolder, srcFilename));

		log.debug("Parsing XML Document.. " + srcFolder + " " + srcFilename);
		if (sourceDocument == null) {
			sourceDocument = sourceParser.parse(getFilePathToWrite(srcFolder,srcFilename));
		}
		
		if (dynamicDocument == null && this.dynamicFileName !=null) {
			dynamicDocument = sourceParser.parse(getFilePathToWrite(outputFolder,dynamicFileName));
		}


		destDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		
		
		

		

		// Clone Html and Head Tags to destinatin XHTML
		cloneHtmlTag();

		// Add pages to final xhtml report
		log.debug("Adding pages to report..");
		addPagesToReport(pages,imgNames,variables);
		
		log.debug("Manipulating Variables..");
		replaceVariables(variables);
		
		log.debug("Replacing image paths..");
		replaceImagesPaths(imgNames);

		String destFilePath = getFilePathToWrite(outputFolder,makeReportFilename(variables));

		log.debug("Writing Report to file.. [path] " + destFilePath);
		writeReportToFile(destFilePath, destDocument);
		log.debug("[Time Taken] "
				+ ((System.currentTimeMillis() - start) / 1000) + "s");

	}

}