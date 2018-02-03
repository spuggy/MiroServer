package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import org.w3c.dom.Node;
import uk.co.bluetrail.miro.pdf.util.Context;
import uk.co.bluetrail.miro.pdf.util.Style;

/**
 * Created by richard on 20/03/15.
 */
public class B extends Handler {
    
    private Style style = null;

    public B(Node node) {
        super(node);
    }

    public B(Node node, Style style) {
        super(node);
        this.style = style;
    }

    @Override
    public Element getContent(Context context) {

        try {

            Font f  = null;

            if(style!=null && style.getStringValue("font-family") !=null) {
                String fontName = style.getStringValue("font-family").toUpperCase();
                f = context.getFont(fontName);
            }

            if(f==null) {
                f = context.getFont(P.DEFAULT_BOLDFONT) ;
            }


            if (node != null) {

                String text = node.getTextContent();

                if (text != null) {
                    Phrase p = new Phrase(strip(text), f);
                    return p;
                }

            }
        } catch(Exception e) {
            //ignore
        }
        return new Phrase("");
    }
}
