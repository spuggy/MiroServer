package uk.co.bluetrail.miro.pdf.handlers;


import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.bluetrail.miro.pdf.util.Context;

/**
 * Created by richard on 20/03/15.
 */
public class PieChartFlexiBullet extends PieChart {

    private Font f = null;

    public PieChartFlexiBullet(Node node, Font f) {
        super(node);
        this.f = f;
    }

    public PieChartFlexiBullet(Node node) {
        this(node,null);
    }

    @Override
    public Element getContent(Context context) {

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        if (f == null) {
            f = context.getFont("P");
        }

        try {

            buildNodeMap();

            //build the table
            table.setWidths(new int[]{3 ,3, 2});

            PdfPCell bulletsCell = getBulletsCell(context);
            if (bulletsCell != null) {
                table.addCell(bulletsCell);
            }

            PdfPCell graphCell = getGraphCell(context);
            if (graphCell != null) {
                table.addCell(graphCell);
            }

            Font legTextFontSmall = context.getFont("LEGTEXTFONTSMALL") ;
            Font legSubTextFontSmall = context.getFont("LEGSUBTEXTFONTSMALL");

            PdfPCell cell = getGraphLegendCell(context,legTextFontSmall,legSubTextFontSmall);
            if (cell != null) {
                cell.setVerticalAlignment(Cell.ALIGN_CENTER);
                cell.setBorder(0);
                table.addCell(cell);
            }


        } catch (Exception e) {
            System.out.print(e.toString());
            return new Paragraph("");
        }
        
        table.setSpacingBefore(context.spacingAfter);
        table.setSpacingAfter(context.spacingAfter);

        return table;
    }

    private PdfPCell getBulletsCell(Context context) {

        Node listNode = nodeMap.get("bullets");
        if(listNode !=null) {
            Ul ul = new Ul(listNode, context.getFont("PSMALL"));
            PdfPCell cell = new PdfPCell();
            cell.addElement(ul.getList(context));
            cell.setBorder(0);
            cell.setPaddingTop(5f);
            cell.setPaddingLeft(5f);
            return cell;
        } else {
            return null;
        }
    }
}