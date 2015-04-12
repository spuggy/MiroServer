package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import org.w3c.dom.Node;
import uk.co.bluetrail.miro.pdf.util.Context;

/**
 * Created by richard on 20/03/15.
 */
public class B extends Handler {


    public B(Node node) {
        super(node);
    }

    @Override
    public Element getContent(Context context) {
        Font f = context.getFont("PBOLD");

        if (node != null) {

            String text = node.getTextContent();

            if(text != null) {
                Phrase p =new Phrase(strip(text),f) ;
                return p;
            }

        }
        return new Phrase("");
    }
}
