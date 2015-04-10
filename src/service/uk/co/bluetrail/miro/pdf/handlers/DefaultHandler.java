package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import org.w3c.dom.Node;
import uk.co.bluetrail.miro.pdf.util.Context;

/**
 * Created by richard on 20/03/15.
 */
public class DefaultHandler extends Handler {

    public DefaultHandler(Node node) {
        super(node);
    }

    @Override
    public Element getContent(Context context) {
        return new Paragraph("");
    }


}
