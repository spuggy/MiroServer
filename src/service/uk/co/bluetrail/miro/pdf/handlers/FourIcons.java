package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.w3c.dom.Node;
import uk.co.bluetrail.miro.pdf.util.Context;

import java.io.IOException;

/**
 * Created by richard on 20/03/15.
 */
public class FourIcons extends Handler {

    private Image getInlineImage(Context context,String name) throws BadElementException, IOException {
        Image img1 = Image.getInstance(name);
        img1.setAlignment(Image.RIGHT | Image.TEXTWRAP );
        img1.scaleToFit(248f*context.imageConstant,366f*context.imageConstant);

        return img1;
    }

    public FourIcons(Node node) {
        super(node);
    }

    @Override
    public Element getContent(Context context) {

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingAfter(context.spacingAfter*2);
        table.setSpacingBefore(context.spacingAfter*2);

        try {
            PdfPCell cell;
            cell = new PdfPCell(getInlineImage(context, context.filePath + "/miro2/images/driving_mode_icon_cover_page.png"));
            cell.setBorder(0);
            table.addCell(cell);
            cell = new PdfPCell(getInlineImage(context, context.filePath + "/miro2/images/energising_mode_icon_cover_page.png"));
            cell.setBorder(0);
            table.addCell(cell);
            cell = new PdfPCell(getInlineImage(context, context.filePath + "/miro2/images/analyser_mode_icon_cover_page.png"));
            cell.setBorder(0);
            table.addCell(cell);
            cell = new PdfPCell(getInlineImage(context, context.filePath + "/miro2/images/organising-four-logos_cover_page.png"));
            cell.setBorder(0);
            table.addCell(cell);


        } catch (Exception e) {
          return new Paragraph(e.toString());
        }
        return table;
    }



}
