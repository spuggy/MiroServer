package uk.co.bluetrail.miro.pdf.page;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import uk.co.bluetrail.miro.pdf.util.Context;

import java.awt.*;

/**
 * Created by richard on 24/03/15.
 */
public class PageHeader {



    public void draw(Context context,PdfWriter writer, com.lowagie.text.Document pdfDocument)  {
         try {

             float pageHeight = pdfDocument.getPageSize().getHeight();
             float pageWidth = pdfDocument.getPageSize().getWidth();
             float miroImage_y = pageHeight - ((18f / 300f) * pageHeight);


             /**
              * Ignoring what's actually happening, to get the image to be what you think of as 150 DPI,
              * take the source image's width and height in pixels and multiple those by 0.48 (72 divided by 150)
              * and pass those new numbers to scaleAbsolute()
              */



             Image img1 = Image.getInstance(context.filePath + "/miro2/images/mirologo_header.png");
             img1.scaleAbsolute(153f * 0.48f, 66f * 0.48f);
             float x = (20f / 210f) * pageWidth;  //rough proportions from original doc

             img1.setAbsolutePosition(x, miroImage_y);
             pdfDocument.add(img1);

             PdfContentByte canvas = writer.getDirectContent();
             float line_y = miroImage_y - 5f;
             canvas.saveState();
             canvas.setLineWidth(1f);
             canvas.setColorStroke(Color.gray);
             canvas.moveTo(x-6f, line_y);
             canvas.lineTo(pageWidth-x, line_y);
             canvas.stroke();
             canvas.restoreState();


         } catch (Exception e) {
             //ignore
         }
    }
}
