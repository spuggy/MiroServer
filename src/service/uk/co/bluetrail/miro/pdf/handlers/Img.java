package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import uk.co.bluetrail.miro.pdf.util.Context;

/**
 * Created by richard on 20/03/15.
 */
public class Img extends Handler {

    public Img(Node node) {
        super(node);
    }

    @Override
    public Element getContent(Context context) {

        try {

            if (node != null) {
                NamedNodeMap attr = node.getAttributes();
                if (attr != null) {
                    Node src = attr.getNamedItem("src");
                    Node height = attr.getNamedItem("height");
                    Node width = attr.getNamedItem("width");
                    Node vspace = attr.getNamedItem("vspace");

                    if (src != null && height !=null && width !=null) {
                        String val = src.getNodeValue();
                        if (val != null) {
                            Image img1 = Image.getInstance(val);
                            img1.setAlignment(Element.ALIGN_CENTER);

                            if(vspace!=null) {
                                Float f = Float.parseFloat(vspace.getNodeValue());
                                img1.setSpacingBefore(f);
                                img1.setSpacingAfter(2000f);
                            } else {
                                img1.setSpacingBefore(context.spacingAfter);
                            }
                            img1.scaleToFit(Integer.parseInt(width.getNodeValue())*context.imageConstant,Integer.parseInt(height.getNodeValue())*context.imageConstant);
                            return img1;
                        }
                    }
                }
            }

        } catch(Exception e) {
            System.out.println(e.getMessage()) ;
            return new Phrase("");
        }



        return new Phrase("");
    }

}
