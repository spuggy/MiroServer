package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.bluetrail.miro.pdf.util.Context;

import java.util.HashMap;

/**
 * Created by richard on 20/03/15.
 */
public class PieChart extends Handler {


    int cellCount = 1;

    public PieChart(Node node) {
        super(node);
    }

    @Override
    public Element getContent(Context context) {

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        try {


            HashMap<String,Node> legendMap = new HashMap<String,Node>();

            NodeList childList = node.getChildNodes();
            if (childList != null && childList.getLength() > 0) {
                for (int c = 0; c < childList.getLength(); c++) {
                    Node childNode = childList.item(c);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                        NamedNodeMap attr = childNode.getAttributes();
                        Node id = attr.getNamedItem("id");
                        if(node!=null) {
                            legendMap.put(id.getNodeValue(),childNode) ;
                        }
                    }
                }
            }




            //build the table
            table.setWidths(new int[]{2, 1});

            Node chartNode = legendMap.get("graph");
            if(chartNode!=null) {
                Img chartImgHandler = new Img(chartNode);
                Image chartImage = (Image) chartImgHandler .getContent(context) ;
                PdfPCell cell = new PdfPCell(chartImage);
                cell.setBorder(2);
                cell.setVerticalAlignment(Cell.ALIGN_TOP);
                table.addCell(cell);
            }


            Font legTextFont = context.getFont("LEGTEXTFONT") ;
            Font legSubTextFont = context.getFont("LEGSUBTEXTFONT");

            PdfPTable legTable = new PdfPTable(2);
            legTable.setWidths(new int[]{1, 3});
            for(int i = 1 ; i < 5;i++) {
                Node legImgNode = legendMap.get("miropie_img_leg"+i);
                Node legTextNode = legendMap.get("miropie_txt_leg"+i);
                Node legSubTextNode = legendMap.get("miropie_subtxt_leg"+i);
                if(legImgNode!=null) {
                    Img legImg = new Img(legImgNode);
                    PdfPCell cell = new PdfPCell((Image) legImg.getContent(context));
                    cell.setPadding(4f);
                    cell.setRowspan(2);
                    cell.setBorder(0);
                    legTable.addCell(cell);
                }

                if(legTextNode!=null) {
                    PdfPCell cell = new PdfPCell(new Phrase(legTextNode.getTextContent(),legTextFont));
                    cell.setPaddingTop(4f);
                    cell.setBorder(0);
                    legTable.addCell(cell);
                }

                if(legSubTextNode!=null) {
                    PdfPCell cell = new PdfPCell(new Phrase(legSubTextNode.getTextContent(),legSubTextFont));
                    cell.setBorder(0);
                    legTable.addCell(cell);
                }
            }

            PdfPCell cell = new PdfPCell(legTable);
            cell.setVerticalAlignment(Cell.ALIGN_CENTER);
            cell.setBorder(0);
            table.addCell(cell);


        } catch (Exception e) {
            return new Paragraph("");
        }
        return table;
    }



}
