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

    
    public PieChart(Node node) {
        super(node);
    }

    HashMap<String,Node> nodeMap = new HashMap<String,Node>();
    
    @Override
    public Element getContent(Context context) {

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        try {

            buildNodeMap();
            
            //build the table
            table.setWidths(new int[]{3, 1});

            PdfPCell graphCell = getGraphCell(context);
            if(graphCell !=null ) {
                table.addCell(graphCell);
            }
            PdfPCell cell = getGraphLegendCell(context);
            if(cell !=null) {
                cell.setVerticalAlignment(Cell.ALIGN_CENTER);
                cell.setBorder(0);
                table.addCell(cell);
            }



        } catch (Exception e) {
            System.out.print(e.toString());
            return new Paragraph("");
        }
        return table;
    }


    protected  PdfPCell getGraphLegendCell(Context context) throws DocumentException {
        Font legTextFont = context.getFont("LEGTEXTFONT") ;
        Font legSubTextFont = context.getFont("LEGSUBTEXTFONT");

        return getGraphLegendCell(context,legTextFont,legSubTextFont);
    }


    protected PdfPCell getGraphLegendCell(Context context, Font legTextFont, Font legSubTextFont) throws DocumentException {
        
        PdfPTable legTable = new PdfPTable(2);
        legTable.setWidths(new int[]{1, 3});
        for (int i = 1; i < 9; i++) {
            Node legImgNode = nodeMap.get("miropie_img_leg" + i);
            Node legTextNode = nodeMap.get("miropie_txt_leg" + i);
            Node legSubTextNode = nodeMap.get("miropie_subtxt_leg" + i);
            if (legImgNode != null) {
                Img legImg = new Img(legImgNode);
                if (legImg.isValid()) {
                    PdfPCell cell = new PdfPCell((Image) legImg.getContent(context));
                    cell.setPadding(4f);
                    cell.setRowspan(2);
                    cell.setBorder(0);
                    legTable.addCell(cell);
                }
            }

            if (legTextNode != null && !legTextNode.getTextContent().trim().equals("")) {
                PdfPCell cell = new PdfPCell(new Phrase(legTextNode.getTextContent(), legTextFont));
                cell.setPaddingTop(4f);
                cell.setBorder(0);
                legTable.addCell(cell);
            }

            if (legSubTextNode != null && !legSubTextNode.getTextContent().trim().equals("")) {
                PdfPCell cell = new PdfPCell(new Phrase(legSubTextNode.getTextContent(), legSubTextFont));
                cell.setBorder(0);
                legTable.addCell(cell);
            }

        }

        return new PdfPCell(legTable);

    }

    protected PdfPCell getGraphCell(Context context) {

        Node chartNode = nodeMap.get("graph");
        if(chartNode!=null) {
            Img chartImgHandler = new Img(chartNode);
            Image chartImage = (Image) chartImgHandler.getContent(context) ;
            PdfPCell cell = new PdfPCell(chartImage);
            cell.setBorder(0);
            cell.setVerticalAlignment(Cell.ALIGN_TOP);
            return cell;
        } else {
            return null;
        }

    }

    protected void buildNodeMap() {

        NodeList childList = node.getChildNodes();
        if (childList != null && childList.getLength() > 0) {
            for (int c = 0; c < childList.getLength(); c++) {
                Node childNode = childList.item(c);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    NamedNodeMap attr = childNode.getAttributes();
                    Node id = attr.getNamedItem("id");
                    if(node!=null) {
                        nodeMap.put(id.getNodeValue(),childNode) ;
                    }
                }
            }
        }

    }


}
