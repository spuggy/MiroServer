package uk.co.bluetrail.miro.pdf.page;

import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.bluetrail.miro.pdf.util.Context;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by richard on 24/03/15.
 */
public class TitlePage {

    public static void draw(Context context, PdfWriter writer,com.lowagie.text.Document pdfDocument, Node node) throws Exception{

        Image img1 = Image.getInstance(context.filePath + "/miro2/images/mirologo-big.png");
        img1.setAlignment(Element.ALIGN_CENTER);
        img1.scaleToFit(365f*context.imageConstant,183f*context.imageConstant);



        NodeList childList = node.getChildNodes();
        ArrayList<String> variables = new ArrayList<String>();

        int idx = 0 ;
        for (int c = 0; c < childList.getLength(); c++) {
            Node childNode = childList.item(c);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                variables.add(childNode.getTextContent());
            }
        }

        float pageHeight = pdfDocument.getPageSize().getHeight();
        float pageWidth =  pdfDocument.getPageSize().getWidth();


        float x = (20f/210f)*pageWidth;  //rough proportions from original doc

        //miro logo
        float miroImage_y = pageHeight- ((60f/300f)*pageHeight);
        img1.setAbsolutePosition(x,miroImage_y);
        pdfDocument.add(img1);

        //your miro report with blue back ground
        float yourmiroreport_y = pageHeight- ((125f/300f)*pageHeight);
        float yourmiroreport_padding = 6f;
        Chunk c = new Chunk("YOUR MIRO REPORT", context.getFont("FRONTBANNER"));
        PdfContentByte canvas = writer.getDirectContent();
        canvas.saveState();
        canvas.setColorStroke(context.getColor("FRONTBANNERBG"));
        canvas.setColorFill(context.getColor("FRONTBANNERBG"));
        canvas.rectangle(x-yourmiroreport_padding, yourmiroreport_y-(yourmiroreport_padding+2f), c.getWidthPoint() + (2*yourmiroreport_padding) , c.getFont().getSize()+(2*yourmiroreport_padding));
        canvas.fillStroke();
        canvas.restoreState();
        c.setBackground(context.getColor("FRONTBANNERBG"));
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(c), x, yourmiroreport_y, 0);


        float name_y = pageHeight- ((150f/300f)*pageHeight);
        String name = variables.get(3) ;
        if(name != null) {
            Phrase namePhrase =  new Phrase(name, context.getFont("FRONTNAMEFONT"));
            ColumnText columnText = new ColumnText(canvas);
            columnText.setIndent(0f);
            columnText.showTextAligned(canvas, Element.ALIGN_LEFT, namePhrase,x-6f, name_y,0);
        }

        float company_y = pageHeight- ((260f/300f)*pageHeight);
        String company = variables.get(0) ;
        if(company != null) {
            Phrase companyPhrase =  new Phrase(company, context.getFont("FRONTCOMPANYFONT"));
            ColumnText columnText = new ColumnText(canvas);
            columnText.setIndent(0f);
            columnText.showTextAligned(canvas, Element.ALIGN_LEFT, companyPhrase,x-6f, company_y,0);
        }

        float title_y = company_y - (context.getFont("FRONTCOMPANYFONT").getSize());
        String title = variables.get(1) ;
        if(title != null) {
            Phrase titlePhrase =  new Phrase(title, context.getFont("H3"));
            ColumnText columnText = new ColumnText(canvas);
            columnText.setIndent(0f);
            columnText.showTextAligned(canvas, Element.ALIGN_LEFT, titlePhrase,x-6f, title_y,0);
        }

        float line_y = title_y - ((context.getFont("H3").getSize()));
        canvas.saveState();
        canvas.setLineWidth(1f);
        canvas.setColorStroke(Color.gray);
        canvas.moveTo(x-6f, line_y);
        canvas.lineTo(pageWidth*0.75f, line_y);
        canvas.stroke();
        canvas.restoreState();






    }


}
