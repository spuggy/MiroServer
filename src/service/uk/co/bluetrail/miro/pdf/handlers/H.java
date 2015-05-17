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

        img1.scaleToFit(167f*context.imageConstant,167f*context.imageConstant);

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

                        String className = clazz.getNodeValue();
                        if(className!=null && className.startsWith("icon_")) {
                            String[] bits = className.toLowerCase().split("_") ;
                            char[] chars = bits[1].toCharArray();

                            for(int i = chars.length-1 ; i > -1  ; i--) {
                                if (chars[i]=='o') {
                                    p.add(getInlineImage(context, context.filePath + "/miro2/images/organising_mode_icon.png")) ;
                                } else if (chars[i]=='e') {
                                    p.add(getInlineImage(context,context.filePath + "/miro2/images/energising_mode_icon.png")) ;
                                } else if (chars[i]=='a') {
                                    p.add(getInlineImage(context,context.filePath + "/miro2/images/analyser_mode_icon.png")) ;
                                } else if (chars[i]=='d') {
                                    p.add(getInlineImage(context, context.filePath + "/miro2/images/driving_mode_icon.png"));
                                }
                            }

                        } else if(className.toLowerCase().equals("center")) {
                            p.setAlignment(Element.ALIGN_CENTER);
                        }


                    }
                }



                return p;
            }

        }
        } catch(Exception e) {
            System.out.println(e.toString() + " H element");
        }

        return new Paragraph("");

    }


}
