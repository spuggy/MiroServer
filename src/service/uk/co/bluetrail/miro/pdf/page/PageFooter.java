package uk.co.bluetrail.miro.pdf.page;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.*;
import uk.co.bluetrail.miro.pdf.util.Context;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;


/**
 * Created by richard on 24/03/15.
 */
public class PageFooter {

    private final ArrayList<PdfTemplate> pageNumPlaceholder = new ArrayList<PdfTemplate>();

    public PageFooter() {


    }

    public void addTotalPageNumbers(Context context,int totalPageNumber) {

        try {

            Font f = context.getFont("FOOTERFONT");

            Iterator<PdfTemplate> itr = pageNumPlaceholder.iterator();
            while (itr.hasNext()) {
                PdfTemplate template = itr.next();
                template.beginText();
                template.setFontAndSize(BaseFont.createFont(), 8);
                template.showText(String.valueOf(totalPageNumber));
                template.endText();
            }

        }catch(Exception e) {
            //TODO handler exceptions
        }

    }


    public void draw(Context context,PdfWriter writer, com.lowagie.text.Document pdfDocument, int pagenumber) {

        float pageHeight = pdfDocument.getPageSize().getHeight();
        float pageWidth = pdfDocument.getPageSize().getWidth();
        float footerStart_y = pageHeight - ((280f / 300f) * pageHeight);


        float x = (20f / 210f) * pageWidth ;  //rough proportions from original doc

        PdfContentByte canvas = writer.getDirectContent();
        canvas.saveState();
        canvas.setLineWidth(1f);
        canvas.setColorStroke(Color.gray);
        canvas.moveTo(x-6f, footerStart_y);
        canvas.lineTo(pageWidth-x, footerStart_y);
        canvas.stroke();
        canvas.restoreState();

        String pageNumText = String.format("Page %d of ", pagenumber);
        Font f = context.getFont("FOOTERFONT") ;
        Phrase pageNumTextPhrase = new Phrase(pageNumText,f);


        float pageNum_y = footerStart_y - f.getSize() - 2f;
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, pageNumTextPhrase, x + 12f, pageNum_y, 0);


        PdfTemplate totalPageTemplate = canvas.createTemplate(50, 50);
        pageNumPlaceholder.add(totalPageTemplate);

        float offset = 31f ;
        if(pagenumber>9) {
            offset = 34f;
        }
        canvas.addTemplate(totalPageTemplate, x + offset, pageNum_y);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        String miroFooterText = context.miroFooterTxt + year  ;
        Phrase miroFooterPhrase = new Phrase(miroFooterText,f);

        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, miroFooterPhrase, x + 425f, pageNum_y, 0);



    }

}
