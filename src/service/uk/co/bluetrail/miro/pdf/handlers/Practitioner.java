package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.bluetrail.miro.pdf.util.Context;

/**
 * Created by richard on 20/03/15.
 */
public class Practitioner extends Handler {

    public Practitioner(Node node) {
        super(node);
    }


    @Override
    public Element getContent(Context context) {


        Paragraph p = new Paragraph();

        try {


            Font f = context.getFont("P");

            if (node != null) {
                p.setFirstLineIndent(0);
                p.setIndentationLeft(0);
                p.setSpacingAfter(context.spacingAfter);
                p.setLeading(f.getSize() + context.leading);


                NodeList childList = node.getChildNodes();
                if (childList != null && childList.getLength() > 0) {
                    for (int c = 0; c < childList.getLength(); c++) {
                        Node childNode = childList.item(c);
                        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                            String text = childNode.getTextContent();
                            if (text != null && text.trim().length() > 0 ) {
                                text = strip(text);
                                Phrase ph = new Phrase(text, f);
                                p.add(ph);
                                p.add(Chunk.NEWLINE);
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