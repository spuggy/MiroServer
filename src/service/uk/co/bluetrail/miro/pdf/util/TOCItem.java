package uk.co.bluetrail.miro.pdf.util;

import com.lowagie.text.Element;

/**
 * Created by richard on 31/05/15.
 */
public class TOCItem
{

    public String text = "";
    public String id =  "" ;
    public Element element = null;

    public TOCItem(String text, String id, Element element) {
        this.text = text;
        this.id = id;
        this.element = element;
    }

}
