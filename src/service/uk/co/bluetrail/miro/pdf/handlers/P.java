package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.bluetrail.miro.pdf.util.Context;

/**
 * Created by richard on 20/03/15.
 */
public class P extends Handler {

    public static String DEFAULT_FONT = "P" ;
    public static String DEFAULT_BOLDFONT = "PBOLD";

    private String fontName = P.DEFAULT_FONT;

    public P(Node node,String fontName) {
        super(node);
        this.fontName = fontName;
    }

    public P(Node node) {

        super(node);
    }

    @Override
    public Element getContent(Context context) {


        Paragraph p = new Paragraph();

        try {

            Font f = context.getFont(fontName);

            if(f==null) {
                context.getFont(P.DEFAULT_FONT) ;
            }

            if (node != null) {
                p.setFirstLineIndent(0);
                p.setIndentationLeft(0);
                p.setSpacingAfter(context.spacingAfter);
//                p.setKeepTogether(true);
                p.setLeading(f.getSize() + context.leading);

                NodeList childList = node.getChildNodes();
                if (childList != null && childList.getLength() > 0) {
                    for (int c = 0; c < childList.getLength(); c++) {
                        Node childNode = childList.item(c);
                        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                            Handler handler = HandlerFactory.instance().getHandler(childNode,null);
                            Element e = handler.getContent(context);
                            p.add(e);
                        } else {
                            String text = childNode.getTextContent();
                            if (text != null) {
                                text = strip(text);
                                Phrase ph = new Phrase(text, f);
                                p.add(ph);
                            }
                        }
                    }
                }
            }
        } catch(Exception e) {
            //ignore
        }
        return p;
    }
}