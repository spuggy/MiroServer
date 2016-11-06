package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.*;
import org.w3c.dom.NamedNodeMap;
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


            //this is a bit nesty ... but hey for now
            // could merge it in with P
            NodeList childList = node.getChildNodes();
            if (childList != null && childList.getLength() > 0) {
                for (int c = 0; c < childList.getLength(); c++) {
                    Node childNode = childList.item(c);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                        if (childNode.getNodeName() == "li") {
                            Phrase ph = new Phrase();
                            NodeList liChildList = childNode.getChildNodes();
                            if (liChildList != null && liChildList.getLength() > 0) {
                                for (int i = 0; i < liChildList.getLength(); i++) {
                                    Node liChildNode = liChildList.item(i);
                                    if (liChildNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Handler handler = HandlerFactory.instance().getHandler(liChildNode,null);
                                        Element e = handler.getContent(context);
                                        ph.add(e);
                                    } else {
                                        String text = liChildNode.getTextContent();
                                        if (text != null) {
                                            text = strip(text);
                                            Phrase phtxt = new Phrase(text, f);
                                            ph.add(phtxt);
                                        }
                                    }
                                }
                            }

                            ListItem item = new ListItem(ph);
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

        List list = getList(context);
        Paragraph p = new Paragraph();

        String className = null;
        NamedNodeMap attr = node.getAttributes();
        if (attr != null) {
            Node clazz = attr.getNamedItem("class");
            if (clazz != null) {
                className = clazz.getNodeValue();
            }
        }


        if (list != null) {
            p.setFirstLineIndent(0);
            p.setIndentationLeft(0);
            p.setIndentationRight(context.listIndentationRight);
            if(className !=null && className.toLowerCase().equals("compact")) {
                p.setSpacingAfter(0f);
            } else {
                p.setSpacingAfter(context.spacingAfter);
            }

            p.add(list);
        }

        return p;

    }

}
