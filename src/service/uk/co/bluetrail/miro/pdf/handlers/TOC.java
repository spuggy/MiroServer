package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.bluetrail.miro.pdf.util.Context;

/**
 * Created by richard on 20/03/15.
 */
public class TOC extends Handler {

    /**
     * <table class="toc">
     <tr>
     <td colspan="7">
     What does this report tell me?
     </td>
     <td>
     <span id="toc1">3</span>
     </td>
     </tr>
     <tr>
     * @param node
     */
    int cellCount = 1 ;

    public TOC(Node node) {
        super(node);
    }

    @Override
    public Element getContent(Context context) {

        PdfPTable table = new PdfPTable(2);

        try {


           table.setWidths(new int[]{5, 1});
           table.setWidthPercentage(100);


            NodeList childList = node.getChildNodes();
            if (childList != null && childList.getLength() > 0) {
                for (int c = 0; c < childList.getLength(); c++) {
                    Node childNode = childList.item(c);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                        handleTableNode(context,table,childNode);
                    }
                }
            }

        } catch (Exception e) {
          return new Paragraph("");
        }
        return table;
    }

    private void handleTableNode(Context context,PdfPTable table, Node node) {

        if(node == null) {
            return;
        }

        if (node.getNodeType() != Node.ELEMENT_NODE) {
            handleTableNode(context,table,node.getNextSibling());
        }

        if(node.getNodeName()=="tr") {
            handleTableNode(context,table,node.getFirstChild());
        } else {
            if(node.getNodeName()=="td") {
                PdfPCell cell;
                cell = new PdfPCell(new Phrase(node.getTextContent(),context.getFont("TOCCOMPANYFONT")));

                if(cellCount % 2 ==0) {
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT | Element.ALIGN_TOP);
                } else {
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT | Element.ALIGN_TOP);
                }
                cell.setBorder(0);
                table.addCell(cell);
                cellCount++;
                handleTableNode(context,table,node.getNextSibling());
            }
        }

    }


}
