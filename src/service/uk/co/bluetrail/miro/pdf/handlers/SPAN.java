package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.bluetrail.miro.pdf.util.Context;

/**
 * Created by richard on 20/03/15.
 */
public class SPAN extends Handler {

    public static String DEFAULT_FONT = "P" ;
    public static String DEFAULT_BOLDFONT = "PBOLD";

    private String fontName = SPAN.DEFAULT_FONT;

    public SPAN(Node node, String fontName) {
        super(node);
        this.fontName = fontName;
    }

    public SPAN(Node node) {

        super(node);
    }

    @Override
    public Element getContent(Context context) {


        Chunk chunk = new Chunk();

        try {

            Font f = context.getFont(fontName);

            if(f==null) {
                context.getFont(SPAN.DEFAULT_FONT) ;
            }

            if (node != null) {

                NodeList childList = node.getChildNodes();
                if (childList != null && childList.getLength() > 0) {
                    for (int c = 0; c < childList.getLength(); c++) {
                        Node childNode = childList.item(c);
                        String text = childNode.getTextContent();
                        if (text != null) {
                            text = strip(text);
                            chunk.append(text);
                        }
                    }
                }
            }
        } catch(Exception e) {
            //ignore
        }
        return chunk;
    }
}