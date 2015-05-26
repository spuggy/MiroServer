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


        try {

            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 2});
            table.setSpacingBefore(context.spacingAfter);

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
                    cell.setPaddingTop(5f);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    cell.setBorderWidthLeft(0f);
                    cell.setBorderWidthTop(1f);
                    cell.setBorderWidthBottom(0f);
                    table.addCell(cell);
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (node.getNodeName()=="ul") {
                try {
                    Ul ul = new Ul(node,context.getFont("PSMALL"));
                    PdfPCell cell = new PdfPCell();
                    cell.addElement(ul.getList(context));
                    cell.setBorder(0);
                    cell.setPaddingTop(5f);
                    cell.setPaddingLeft(40f);
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


