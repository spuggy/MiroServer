package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.bluetrail.miro.pdf.util.Context;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by richard on 20/03/15.
 */
public class PopulationBarChart extends Handler {


    public PopulationBarChart(Node node) {
        super(node);
    }

    @Override
    public Element getContent(Context context) {

        Font f = context.getFont("P");


        Paragraph p = new Paragraph();
        p.setSpacingAfter(context.spacingAfter*4);
        p.setSpacingBefore(context.spacingAfter);

        try {

            HashMap<String,String> variables = new HashMap<String, String>();

            //get the id of the chart
            NamedNodeMap attrMap = node.getAttributes();
            if (attrMap != null) {
              Node id = attrMap.getNamedItem("id");
              if(id !=null) {
                  variables.put("id",id.getNodeValue());
              }
            }


            NodeList childList = node.getChildNodes();


            //loop over html and build map of nodes and text values
            for (int c = 0; c < childList.getLength(); c++) {
                Node childNode = childList.item(c);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    attrMap = childNode.getAttributes();
                    if (attrMap != null) {
                        int len = attrMap.getLength();
                        for(int a = 0 ; a< len;a++) {
                            Node attr = attrMap.item(a);
                            if(attr!=null) {
                                variables.put(attr.getNodeValue(), childNode.getTextContent());
                            }
                        }
                    }
                }
            }

            PdfPTable barTable = new PdfPTable(4);
            barTable.setWidthPercentage(100);

            for(int l=1; l < 4;l++) {
                addBarLeftText(l, barTable, variables,context);
                addBarLeftImage(l, barTable, variables, context);
                addBarRightImage(l, barTable, variables,context);
                addBarRightText(l, barTable, variables,context);
            }

            p.add(barTable);

        } catch (Exception e) {
            return new Paragraph(e.toString());
        }
        return p;
    }

    private void addBarLeftText(int barNum, PdfPTable barTable, HashMap<String, String> variables, Context context) {



        String id = variables.get("id");
        String text = variables.get(id + "_" + barNum + "tl");
        String debug = "  [" + variables.get(id + "_" + barNum + "lv") + "]";

        Font f = context.getFont("FOOTERFONT") ;

        if(text !=null) {
            PdfPCell cell;
            cell = new PdfPCell(new Phrase(text + debug,f));
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(4f);
            cell.setBorderWidthLeft(0f);
            cell.setBorderWidthTop(0f);
            cell.setBorderWidthBottom(1f);
            cell.setBorderColorBottom(Color.lightGray);
            //cell.setBorderWidthRight(2f);
            cell.setBorderColorRight(Color.lightGray);
            barTable.addCell(cell);
        }


    }

    private void addBarRightText(int barNum, PdfPTable barTable, HashMap<String, String> variables,Context context) {

        String id = variables.get("id");
        String text = variables.get(id + "_" + barNum + "tr");
        String debug = "  [" + variables.get(id + "_" + barNum + "rv") + "]";

        Font f = context.getFont("FOOTERFONT") ;
        if(text !=null) {
            PdfPCell cell;
            cell = new PdfPCell(new Phrase(text + debug,f));
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPadding(4f);
            //cell.setBorderWidthLeft(2f);
            cell.setBorderWidthTop(0f);
            cell.setBorderWidthBottom(1f);
            cell.setBorderColorBottom(Color.lightGray);
            cell.setBorderWidthRight(0f);
            cell.setBorderColorLeft(Color.lightGray);
            barTable.addCell(cell);
        }

    }

    private void addBarRightImage(int barNum, PdfPTable barTable, HashMap<String, String> variables,Context context) {

        String id = variables.get("id");
        String valStr = variables.get(id + "_" + barNum + "rv");

        try {

            if (valStr != null) {
                Image img1 = Image.getInstance(context.filePath + "/miro2/images/right_bar.png");
                float val = Float.parseFloat(valStr);
                img1.scaleAbsolute(val, 40f);
                PdfPCell cell;
                cell = new PdfPCell(img1);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                cell.setPaddingLeft(1f);
                cell.setPaddingTop(5f);
                cell.setBorderWidthLeft(1f);
                cell.setBorderWidthTop(0f);
                cell.setBorderWidthBottom(1f);
                cell.setBorderWidthRight(0f);
                cell.setBorderColorBottom(Color.lightGray);
                cell.setBorderColorLeft(Color.lightGray);
                barTable.addCell(cell);
            }

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }


    }

    private void addBarLeftImage(int barNum, PdfPTable barTable, HashMap<String, String> variables,Context context) {

        String id = variables.get("id");
        String valStr = variables.get(id + "_" + barNum + "lv");

        try {

            if (valStr != null) {
                Image img1 = Image.getInstance(context.filePath + "/miro2/images/left_bar.png");
                float val = Float.parseFloat(valStr);
                img1.scaleAbsolute(val, 40f);
                PdfPCell cell;
                cell = new PdfPCell(img1);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                cell.setPaddingRight(1f);
                cell.setPaddingTop(5f);
                cell.setBorderWidthLeft(0);
                cell.setBorderWidthTop(0);
                cell.setBorderWidthBottom(1f);
                cell.setBorderWidthRight(1f);
                cell.setBorderColorBottom(Color.lightGray);
                cell.setBorderColorRight(Color.lightGray);
                barTable.addCell(cell);
            }

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
