package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import org.w3c.dom.Node;
import uk.co.bluetrail.miro.pdf.util.Context;

/**
 * Created by richard on 20/03/15.
 */
abstract public class Handler {

    protected Node node = null;

    public Element getContent(Context context) {
        return new Paragraph("");
    }

    public Handler(Node node) {
         this.node = node;
    }

    protected String strip(String str) {

        if(str != null) {
            return str.replaceAll(System.getProperty("line.separator"), " ").replaceAll("\\s+", " ");
        } else {
            return str;
        }
    }


    public String toString() {
        if(node !=null) {
            return node.getNodeName() + "=" + node.getTextContent();
        }  else {
            return "null";
        }
    }

}
