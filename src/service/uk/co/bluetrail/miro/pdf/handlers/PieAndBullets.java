package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.bluetrail.miro.pdf.util.Context;

public class PieAndBullets extends Handler{

    int cellCount = 1 ;

    public PieAndBullets(Node node) {
        super(node);
    }

    @Override
    public Element getContent(Context context) {

        PdfPTable table = new PdfPTable(2);
        table.setSpacingAfter(context.spacingAfter);
        table.setSpacingBefore(context.spacingAfter);

        try {

            table.setWidthPercentage(100);

            NodeList childList = node.getChildNodes();
            Node trNode = null;
            int i = 0 ;
            while(trNode==null && i < childList.getLength() ) {
                Node curNode = childList.item(i);
                if (curNode.getNodeType() == Node.ELEMENT_NODE && curNode.getNodeName()=="tr") {
                    trNode=curNode;
                }
                i++;
            }

            if(trNode!=null) {
                childList = trNode.getChildNodes();
                if (childList != null && childList.getLength() > 0) {
                    for (int c = 0; c < childList.getLength(); c++) {
                        Node childNode = childList.item(c);
                        System.out.println("childNode " + childNode.getNodeName());
                        if (childNode.getNodeType() == Node.ELEMENT_NODE ) {
                            handleTableNode(context,table,childNode);
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Paragraph("");
        }
        return table;
    }

    private void handleTableNode(Context context,PdfPTable table, Node node) {


        if(node == null) {
            return;
        }

        System.out.println(node.getNodeName());


        if (node.getNodeType() != Node.ELEMENT_NODE) {
            handleTableNode(context,table,node.getNextSibling());
        }

        if(node.getNodeName()=="tr") {
            handleTableNode(context,table,node.getFirstChild());
        } else {
            if(node.getNodeName()=="td") {
                handleTableNode(context,table,node.getFirstChild());
            } else if(node.getNodeName()=="img") {
                try {
                    Img img = new Img(node);
                    PdfPCell cell = new PdfPCell((Image) img.getContent(context));
                    cell.setBorder(0);
                    cell.setPadding(4f);
                    cell.setBorderWidthLeft(0f);
                    cell.setBorderWidthTop(1f);
                    cell.setBorderWidthBottom(0f);
                    table.addCell(cell);
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (node.getNodeName()=="ul") {
                try {
                    Ul ul = new Ul(node);
                    PdfPCell cell = new PdfPCell();
                    cell.addElement(ul.getList(context));
                    cell.setBorder(0);
                    cell.setPadding(4f);
                    cell.setBorderWidthLeft(0f);
                    cell.setBorderWidthTop(1f);
                    cell.setBorderWidthBottom(0f);
                    table.addCell(cell);

                } catch(Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    }

}


/**
 PdfPCell cell;
 String text = strip(node.getTextContent());
 cell = new PdfPCell(new Phrase(text,context.getFont("TOCCOMPANYFONT")));

 if(cellCount % 2 ==0) {
 cell.setHorizontalAlignment(Element.ALIGN_RIGHT | Element.ALIGN_TOP);
 } else {
 cell.setHorizontalAlignment(Element.ALIGN_LEFT | Element.ALIGN_TOP);
 }
 cell.setBorder(0);
 table.addCell(cell);
 cellCount++;
 handleTableNode(context,table,node.getNextSibling());
 */