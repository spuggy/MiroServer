package uk.co.bluetrail.miro;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.VerticalPositionMark;
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
import uk.co.bluetrail.miro.pdf.page.TOC;
import uk.co.bluetrail.miro.pdf.page.TitlePage;
import uk.co.bluetrail.miro.pdf.util.Context;
import uk.co.bluetrail.miro.pdf.util.TOCItem;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by richard on 20/03/15.
 */
public class MiroReportPDFGenerator extends PdfPageEventHelper {

    private static MiroReportPDFGenerator miroReportPDFGenerator = null;
    private int pagenumber;
    private boolean isFreeReport = false;
    private Context context;
    private PageFooter pageFooter = new PageFooter();
    private PageHeader pageHeader = new PageHeader();
    private TOC toc ;


    private final static Log log = LogFactory.getLog(MiroReportPDFGenerator.class);




    public static void generatePDF(File baseDir, MiroResponse mr, Long miroVersion, boolean isFreeReport) {

        if (MiroReportPDFGenerator.miroReportPDFGenerator == null) {
            MiroReportPDFGenerator.miroReportPDFGenerator = new MiroReportPDFGenerator();
        }


        MiroReportPDFGenerator.miroReportPDFGenerator.privateGeneratePDF(baseDir, mr.getMiroReportName(), mr.getMiroReportName(miroVersion),isFreeReport);

    }

    public static void generatePDF(File baseDir, String miroReportName,boolean isFreeReport) throws Exception {

        if (MiroReportPDFGenerator.miroReportPDFGenerator == null) {
            MiroReportPDFGenerator.miroReportPDFGenerator = new MiroReportPDFGenerator();
        }

        MiroReportPDFGenerator.miroReportPDFGenerator.privateGeneratePDF(baseDir, miroReportName, null,isFreeReport);


    }

    public static void PDFCreator(File baseDir, String miroReportName, String xslFileName, String miroReportNameVersion,boolean isFreeReport) {

        if (MiroReportPDFGenerator.miroReportPDFGenerator == null) {
            MiroReportPDFGenerator.miroReportPDFGenerator = new MiroReportPDFGenerator();
        }

        MiroReportPDFGenerator.miroReportPDFGenerator.privateGeneratePDF(baseDir, miroReportName, miroReportNameVersion,isFreeReport);

    }


    private void privateGeneratePDF(File baseDir, String miroReportName, String miroReportNameVersion, boolean isFreeReport) {


        this.pagenumber = 0;
        this.isFreeReport = isFreeReport;

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
            Font h4Font = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINBold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 13, Font.NORMAL);
            Font h2Font = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINBold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 12, Font.NORMAL);
            Font firstPageFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINRegular.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 14, Font.NORMAL);
            Font firstPageFontBold = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINRegular.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 14, Font.BOLD);
            Font pFontBold = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 75 Bold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 10, Font.NORMAL);
            Font pFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 55 Roman.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 10, Font.NORMAL);
            Font pFontSmall = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 55 Roman.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 8, Font.NORMAL);
            Font frontBannerFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINBold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 16, Font.NORMAL, Color.white);
            Font frontNameFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINBold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 34, Font.NORMAL, Color.black);
            Font frontCompanyFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINRegular.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 16, Font.NORMAL);
            Font footerFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 55 Roman.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 8, Font.NORMAL);
            Font tocCompanyFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 55 Roman.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 16, Font.NORMAL);
            Font legTextFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINBold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 9, Font.NORMAL);
            Font legSubTextFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINRegular.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 7, Font.NORMAL);
            Font barTextFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 55 Roman.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 10, Font.NORMAL);

            try {

                context.addFont("H1", h1Font);
                context.addFont("H3", h3Font);
                context.addFont("H4", h4Font);
                context.addFont("H2", h2Font);
                context.addFont("P", pFont);
                context.addFont("PSMALL", pFont);
                context.addFont("PBOLD", pFontBold);
                context.addFont("FRONTBANNER", frontBannerFont);
                context.addColor("MIROBLUE", MiroConstants.getInstance().miroBlue);
                context.addColor("MIRORED", MiroConstants.getInstance().miroRed);
                context.addColor("MIROGREEN", MiroConstants.getInstance().miroGreen);
                context.addColor("MIROYELLOW", MiroConstants.getInstance().miroYellow);
                context.addFont("FRONTNAMEFONT", frontNameFont);
                context.addFont("FRONTCOMPANYFONT", frontCompanyFont);
                context.addFont("FOOTERFONT", footerFont);
                context.addFont("BARTEXTFONT", barTextFont);
                context.addFont("TOCCOMPANYFONT", tocCompanyFont);
                context.addFont("FIRSTPAGEFONT", firstPageFont);
                context.addFont("FIRSTPAGEFONTBOLD", firstPageFontBold);
                context.addFont("LEGTEXTFONT", legTextFont);
                context.addFont("LEGSUBTEXTFONT", legSubTextFont);


            } catch (Exception e) {
                throw new uk.co.bluetrail.miro.pdf.util.MiroException(e.getMessage());
            }

           final Map<String, PdfTemplate> tocPlaceHolders = new HashMap<String, PdfTemplate>();


            NodeList nodeList = document.getElementsByTagName("div");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NamedNodeMap attributes = node.getAttributes();
                Node id = attributes.getNamedItem("id");

                if (id != null)  {

                    //0
                    if(id.getNodeValue().equals("0")) {
                        TitlePage.draw(context, writer, pdfDocument, node);
                    }  else if(id.getNodeValue().equals("1")) {

                        this.toc = new TOC(node,context);
                        pdfDocument.add(this.toc.getHeader());

                        ArrayList<TOCItem> tocItems = toc.getTocItems();
                        Iterator<TOCItem> itr = tocItems.iterator();
                        while(itr.hasNext()) {

                            final TOCItem tocItem =  itr.next();
                            pdfDocument.add(new Paragraph((Chunk)tocItem.element));

                            // Add a placeholder for the page reference
                            pdfDocument.add(new VerticalPositionMark() {
                                @Override
                                public void draw(final PdfContentByte canvas, final float llx, final float lly, final float urx, final float ury, final float y)
                                {
                                    final PdfTemplate createTemplate = canvas.createTemplate(50, 50);
                                    tocPlaceHolders.put(tocItem.id,createTemplate);
                                    canvas.addTemplate(createTemplate, urx - 200, y);
                                }
                            });
                        }

                    }  else {
                        processPageElements(context, pdfDocument, node);
                    }

                }

                pdfDocument.newPage();
            }

            pageFooter.addTotalPageNumbers(context, pagenumber - 1);
            populateToc(tocPlaceHolders) ;


            pdfDocument.close();
        } catch (Exception e) {
            throw new uk.co.bluetrail.miro.pdf.util.MiroException(e.toString());
        }
    }

    private void populateToc(Map<String, PdfTemplate> tocPlaceHolders) throws IOException, DocumentException {
        Set keys = tocPlaceHolders.keySet()  ;
        Iterator<String> itr = keys.iterator() ;
        while(itr.hasNext()) {
            String key = itr.next();
            Integer pageNumber = toc.tocPageNumbers.get(key);
            PdfTemplate template = tocPlaceHolders.get(key);
            if (pageNumber != null && template !=null) {
                toc.populateToc(context,template,pageNumber);
            }
        }
    }


    public void onStartPage(PdfWriter writer, com.lowagie.text.Document document) {
        pagenumber++;
    }

    private void writeWaterMark(PdfWriter writer, com.lowagie.text.Document pdfDocument) {
        try {
            PdfGState gState = new PdfGState();
            gState.setFillOpacity(0.1f);
            gState.setStrokeOpacity(0.1f);
            PdfContentByte contentUnder = writer.getDirectContentUnder();
            contentUnder.saveState();
            contentUnder.setGState(gState);
            contentUnder.setFontAndSize(BaseFont.createFont(), 96);
            contentUnder.beginText();
            contentUnder.showTextAligned(Element.ALIGN_CENTER,"SAMPLE",pdfDocument.getPageSize().getWidth()/2,pdfDocument.getPageSize().getHeight()/2,45);
            contentUnder.endText();
            contentUnder.restoreState();
        }catch (Exception e) {
            System.out.println("exception creating watermak " + e.getMessage());
        }
    }


    public void onEndPage(PdfWriter writer, com.lowagie.text.Document pdfDocument) {

        switch (pagenumber) {
            case 0:
            case 1:
                // no header
                if(this.isFreeReport) {
                    writeWaterMark(writer,pdfDocument);
                }
                break;
            default:
                if(this.isFreeReport) {
                    writeWaterMark(writer,pdfDocument);
                }
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
                if(this.toc !=null) {
                    toc.addPage(childNode,pagenumber) ;
                }
                Handler handler = HandlerFactory.instance().getHandler(childNode,pdfDocument) ;
                pdfDocument.add(handler.getContent(context));
            }

        }
    }



}
