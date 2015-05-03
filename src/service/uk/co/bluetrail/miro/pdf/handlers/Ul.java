package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.bluetrail.miro.pdf.util.Context;

/**
 * Created by richard on 20/03/15.
 */
public class Ul extends Handler {

    private Font f = null;

    public Ul(Node node, Font f) {

        super(node);
        this.f = f;

    }

    public Ul(Node node) {
        super(node);
    }

    public List getList(Context context) {

        if (f == null) {
            f = context.getFont("P");
        }

        if (node != null) {

            List list = new List();
            list.setListSymbol(context.listSymbol + " ");
            list.setAutoindent(true);


            NodeList childList = node.getChildNodes();
            if (childList != null && childList.getLength() > 0) {
                for (int c = 0; c < childList.getLength(); c++) {
                    Node childNode = childList.item(c);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                        if (childNode.getNodeName() == "li") {
                            ListItem item = new ListItem(strip(childNode.getTextContent()), f);
                            item.setSpacingAfter(0f);
                            item.setLeading(f.getSize() + context.leading);
                            list.add(item);
                        }
                    }
                }

            }
            return list;
        }

        return null;

    }

    @Override
    public Element getContent(Context context) {

        List list = getList(context) ;
        Paragraph p = new Paragraph();

        if(list!=null) {
            p.setFirstLineIndent(0);
            p.setIndentationLeft(0);
            p.setIndentationRight(context.listIndentationRight);
            p.setSpacingAfter(context.spacingAfter);
            p.add(list);
        }

        return p;

    }

}
