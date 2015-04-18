package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.bluetrail.miro.pdf.util.Context;

/**
 * Created by richard on 20/03/15.
 */
public class Ul extends Handler {


    public Ul(Node node) {
        super(node);
    }

    @Override
    public Element getContent(Context context) {

        Font f = context.getFont("P") ;

        Paragraph p = new Paragraph();
        if (node != null) {

            p.setFirstLineIndent(0);
            p.setIndentationLeft(0);
            p.setIndentationRight(context.listIndentationRight);
            p.setSpacingAfter(context.spacingAfter);


            List list = new List();
            list.setListSymbol(context.listSymbol + " ");
            list.setAutoindent(true);


            NodeList childList = node.getChildNodes();
            if (childList != null && childList.getLength() > 0) {
                for (int c = 0; c < childList.getLength(); c++) {
                    Node childNode = childList.item(c);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                        if(childNode.getNodeName()=="li") {
                            ListItem item = new ListItem(strip(childNode.getTextContent()),f);
                            item.setSpacingAfter(0f);
                            item.setLeading(f.getSize() + context.leading);
                            list.add(item);
                        }
                    }
                }
                p.add(list);
                return p;
            }


        }
        return p;
    }

}
