package uk.co.bluetrail.miro.pdf.page;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfTemplate;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.bluetrail.miro.pdf.handlers.H;
import uk.co.bluetrail.miro.pdf.handlers.Handler;
import uk.co.bluetrail.miro.pdf.handlers.P;
import uk.co.bluetrail.miro.pdf.handlers.SPAN;
import uk.co.bluetrail.miro.pdf.util.Context;
import uk.co.bluetrail.miro.pdf.util.TOCItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by richard on 20/03/15.
 */
public class TOC {

    private Node node;
    public ArrayList<TOCItem> tocItems = new ArrayList<TOCItem>();
    public Map<String,Integer> tocPageNumbers = new HashMap<String, Integer>();
    public String title = "";
    private Element header = null;

    public TOC(Node node, Context context) {

        this.node = node ;

        if(header == null) {
            processNode(node,context) ;
        }

    }


    private void processNode(Node node, Context context)  {

        context.getFont(P.DEFAULT_FONT) ;


        if (node != null) {

            NodeList childList = node.getChildNodes();
            if (childList != null && childList.getLength() > 0) {
                for (int c = 0; c < childList.getLength(); c++) {
                    Node childNode = childList.item(c);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                        if (childNode.getNodeName() == "p") {
                            String v = childNode.getTextContent();
                            NamedNodeMap attr = childNode.getAttributes();
                            final Node id = attr.getNamedItem("id");

                            if(id.getNodeValue().equals("header")) {
                                Handler h = new H(childNode,3);
                                this.header = h.getContent(context);
                            } else {
                                Handler span = new SPAN(childNode) ;
                                tocItems.add(new TOCItem(v,id.getNodeValue(),span.getContent(context)));
                            }
                        }
                    }
                }

            }

        }


    }


    public void addPage(Node node, int pagenumber) {

        if (node != null) {
            NamedNodeMap attr = node.getAttributes();
            final Node id = attr.getNamedItem("class");
            if(id!=null)  {
                this.tocPageNumbers.put(id.getNodeValue(), new Integer(pagenumber));
            }
        }

    }


    public void populateToc(Context context, PdfTemplate template, Integer pageNumber) {

        Font font =  context.getFont(P.DEFAULT_FONT);
        BaseFont baseFont =  font.getBaseFont();
        float fontSize = font.getSize();

        template.beginText();
        template.setFontAndSize(baseFont, fontSize);
        template.setTextMatrix(50 - baseFont.getWidthPoint(pageNumber.toString(), fontSize), 0);
        template.showText(pageNumber.toString());
        template.endText();

    }

    public Element getHeader() {
        return header;
    }

    public ArrayList<TOCItem> getTocItems() {
        return tocItems;

    }
}
