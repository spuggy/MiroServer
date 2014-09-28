package uk.co.bluetrail.miro;

import com.lowagie.text.pdf.PdfReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.OutputStream;

public class MiroReportPDFGenerator {

    private final static Log log = LogFactory.getLog(MiroReportPDFGenerator.class);


    public static void generatePDF(File baseDir, String miroReportName, String xslFileName) throws Exception {


        MiroReportPDFGenerator.PDFCreator(baseDir, miroReportName, xslFileName);

    }


    public static void generatePDF(File baseDir, String miroReportName) throws Exception {

        String xslFileName = "miro2fo.xsl";

        MiroReportPDFGenerator.PDFCreator(baseDir, miroReportName, xslFileName);

    }

    public static void PDFCreator(File baseDir, String miroReportName, String xslFileName) {


        log.debug("Preparing..." + miroReportName);

        OutputStream out = null;

        try {


            // Setup input and output files
            File xmlfile = new File(baseDir, "out/" + miroReportName + ".xhtml");
            File xsltfile = new File(baseDir, "out/" + miroReportName + ".xsl");

            if (!xsltfile.exists()) {
                log.debug("Cannot find user xslt file for using global");
                xsltfile = new File(baseDir, "xhtml/" + xslFileName);
            }


            File pdffile = new File(baseDir, "out/" + miroReportName + ".pdf");

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
            out = new java.io.FileOutputStream(pdffile);
            out = new java.io.BufferedOutputStream(out);


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

            out.close();

            String path = pdffile.getAbsolutePath();
            PdfReader pdfReader = new PdfReader(path);
            int numOfPages = pdfReader.getNumberOfPages();
            if (numOfPages > 1) {
                log.debug("No of Pages:" + miroReportName + " " + numOfPages);
            } else {
                throw new MiroException("invalid pdf for " + miroReportName);
            }


        } catch (Exception e) {
            try {
                out.close();
            } catch (Exception ex) {
                log.error(ex);
            }
            log.error("Exception creating pdf " + miroReportName + " " + e.getMessage());
            throw new MiroException("Exception creating pdf " + miroReportName + " " + e.getMessage());
        }

        log.debug("Success for ! " + miroReportName);

    }
}
