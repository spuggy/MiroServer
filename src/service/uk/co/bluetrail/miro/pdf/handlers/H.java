package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.*;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import uk.co.bluetrail.miro.pdf.util.Context;

import java.io.IOException;

/**
 * Created by richard on 20/03/15.
 */
public class H extends Handler {

    private int size = 3;

    public H(Node node,int size) {
        super(node);
        this.size = size;
    }

    private Image getInlineImage(Context context,String name) throws BadElementException, IOException {
        Image img1 = Image.getInstance(name);
        img1.setAlignment(Image.RIGHT | Image.TEXTWRAP );
        img1.setBorder(10);

        img1.scaleToFit(248f*context.imageConstant,248f*context.imageConstant);

        return img1;
    }

    @Override
    public Element getContent(Context context) {

        Font f = context.getFont("H"+size) ;

        try {

        if (node != null) {


            String text = node.getTextContent();

            if(text != null) {

                Paragraph p =new Paragraph(strip(text),f) ;
                p.setSpacingAfter((4-size)*context.spacingAfter);
                p.setLeading(f.getSize() + context.leading);

                NamedNodeMap attr = node.getAttributes();

                if (attr != null) {
                    Node clazz = attr.getNamedItem("class");
                    if (clazz != null) {
                        //TODO should we get hard wired images out of here
                        if (clazz.getNodeValue().equals("organising_mode")) {
                            p.add(getInlineImage(context, context.filePath + "/miro2/images/organising_mode_icon.jpg")) ;
                        } else if (clazz.getNodeValue().equals("energising_mode")) {
                            p.add(getInlineImage(context,context.filePath + "/miro2/images/energising_mode_icon.jpg")) ;
                        } else if (clazz.getNodeValue().equals("analysing_mode")) {
                            p.add(getInlineImage(context,context.filePath + "/miro2/images/analyser_mode_icon.jpg")) ;
                        } else if (clazz.getNodeValue().equals("driving_mode")) {
                            p.add(getInlineImage(context,context.filePath + "/miro2/images/driving_mode_icon.jpg")) ;
                        }
                    }
                }



                return p;
            }

        }
        } catch(Exception e) {
            //ignore
        }

        return new Paragraph("");

    }


}
