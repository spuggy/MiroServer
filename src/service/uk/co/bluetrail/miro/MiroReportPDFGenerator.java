package uk.co.bluetrail.miro;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.bluetrail.miro.pdf.handlers.Handler;
import uk.co.bluetrail.miro.pdf.handlers.HandlerFactory;
import uk.co.bluetrail.miro.pdf.page.PageFooter;
import uk.co.bluetrail.miro.pdf.page.PageHeader;
import uk.co.bluetrail.miro.pdf.page.TitlePage;
import uk.co.bluetrail.miro.pdf.util.Context;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by richard on 20/03/15.
 */
public class MiroReportPDFGenerator extends PdfPageEventHelper {

    private static MiroReportPDFGenerator miroReportPDFGenerator = null;
    private int pagenumber;
    private Context context;
    private PageFooter pageFooter = new PageFooter();
    private PageHeader pageHeader = new PageHeader();


    private final static Log log = LogFactory.getLog(MiroReportPDFGenerator.class);




    public static void generatePDF(File baseDir, MiroResponse mr, Long miroVersion) {

        if (MiroReportPDFGenerator.miroReportPDFGenerator == null) {
            MiroReportPDFGenerator.miroReportPDFGenerator = new MiroReportPDFGenerator();
        }


        MiroReportPDFGenerator.miroReportPDFGenerator.privateGeneratePDF(baseDir, mr.getMiroReportName(), mr.getMiroReportName(miroVersion));

    }

    public static void generatePDF(File baseDir, String miroReportName) throws Exception {

        if (MiroReportPDFGenerator.miroReportPDFGenerator == null) {
            MiroReportPDFGenerator.miroReportPDFGenerator = new MiroReportPDFGenerator();
        }

        MiroReportPDFGenerator.miroReportPDFGenerator.privateGeneratePDF(baseDir, miroReportName, null);


    }

    public static void PDFCreator(File baseDir, String miroReportName, String xslFileName, String miroReportNameVersion) {

        if (MiroReportPDFGenerator.miroReportPDFGenerator == null) {
            MiroReportPDFGenerator.miroReportPDFGenerator = new MiroReportPDFGenerator();
        }

        MiroReportPDFGenerator.miroReportPDFGenerator.privateGeneratePDF(baseDir, miroReportName, miroReportNameVersion);

    }


    private void privateGeneratePDF(File baseDir, String miroReportName, String miroReportNameVersion) {


        try {

            // Setup input and output files
            File sourceFile = new File(baseDir, "out/" + miroReportName + ".xhtml");


            // if the alternate name with version is supplied use that .. means v11 at the end.
            String miroReportFileName = miroReportName ;
            if(miroReportNameVersion!=null) {
                miroReportFileName = miroReportNameVersion;
            }

            File pdffile = new File(baseDir, "out/" + miroReportFileName + ".pdf");

            log.debug("Input: XML (" + sourceFile + ")");
            log.debug("Output: PDF (" + pdffile + ")");

            log.debug("Transforming...");


            context = new Context("/miro-reports");


            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document document = docBuilder.parse(sourceFile);

            com.lowagie.text.Document pdfDocument = new com.lowagie.text.Document(context.pageSize, context.marginLeft, context.marginRight, context.marginTop, context.marginBottom);

            PdfWriter writer = PdfWriter.getInstance(pdfDocument, new FileOutputStream(pdffile));
            writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
            writer.setPageEvent(this);


            pdfDocument.open();


            Font h1Font = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINBold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 44, Font.NORMAL);
            Font h3Font = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINBold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 16, Font.NORMAL);
            Font h4Font = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINRegular.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 13, Font.NORMAL);
            Font firstPageFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINRegular.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 10, Font.NORMAL);
            Font pFontBold = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 75 Bold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 10, Font.NORMAL);
            Font pFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 55 Roman.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 10, Font.NORMAL);
            Font frontBannerFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINBold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 16, Font.NORMAL, Color.white);
            Font frontNameFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINBold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 34, Font.NORMAL, Color.black);
            Font frontCompanyFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINRegular.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 16, Font.NORMAL);
            Font footerFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 55 Roman.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 8, Font.NORMAL);
            Font tocCompanyFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 55 Roman.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 16, Font.NORMAL);


            try {

                context.addFont("H1", h1Font);
                context.addFont("H3", h3Font);
                context.addFont("H4", h4Font);
                context.addFont("P", pFont);
                context.addFont("PBOLD", pFontBold);
                context.addFont("FRONTBANNER", frontBannerFont);
                context.addColor("FRONTBANNERBG", "#43ADD5");
                context.addFont("FRONTNAMEFONT", frontNameFont);
                context.addFont("FRONTCOMPANYFONT", frontCompanyFont);
                context.addFont("FOOTERFONT", footerFont);
                context.addFont("TOCCOMPANYFONT", tocCompanyFont);
                context.addFont("FIRSTPAGEFONT", firstPageFont);

            } catch (Exception e) {
                throw new uk.co.bluetrail.miro.pdf.util.MiroException(e.getMessage());
            }


            NodeList nodeList = document.getElementsByTagName("div");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NamedNodeMap attributes = node.getAttributes();
                Node id = attributes.getNamedItem("id");

                if (id != null && id.getNodeValue().equals("0")) {
                    TitlePage.draw(context, writer, pdfDocument, node);
                } else {
                    processPageElements(context, pdfDocument, node);
                }

                pdfDocument.newPage();
            }

            pageFooter.addTotalPageNumbers(context, pagenumber - 1);

            pdfDocument.close();
        } catch (Exception e) {
            throw new uk.co.bluetrail.miro.pdf.util.MiroException(e.toString());
        }
    }


    public void onStartPage(PdfWriter writer, com.lowagie.text.Document document) {
        pagenumber++;
    }


    public void onEndPage(PdfWriter writer, com.lowagie.text.Document pdfDocument) {
        Rectangle rect = writer.getBoxSize("art");
        switch (pagenumber) {
            case 0:
            case 1:
                // no header
                break;
            default:
                pageHeader.draw(context, writer, pdfDocument);
                pageFooter.draw(context, writer, pdfDocument, pagenumber);
                break;
        }

    }

    private void processPageElements(Context context, com.lowagie.text.Document pdfDocument, Node node) throws Exception {
        NodeList childList = node.getChildNodes();

        for (int c = 0; c < childList.getLength(); c++) {
            Node childNode = childList.item(c);

            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Handler handler = HandlerFactory.instance().getHandler(childNode);
                pdfDocument.add(handler.getContent(context));
            }

        }
    }



}
