package uk.co.bluetrail.miro.pdf;

import junit.framework.Assert;
import junit.framework.TestCase;
import uk.co.bluetrail.miro.pdf.util.Style;

public class StyleTest extends TestCase {

    public void testParse() {

        Style style = new Style("color:blue;margin-top:30px; margin-bottom:10");

        Assert.assertEquals("color not valid element so null",null,style.getStringValue("color"));
        Assert.assertEquals("finds a valid element","30px",style.getStringValue("margin-top"));
        Assert.assertEquals("null as cannot convert",null,style.getIntegerValue("margin-top"));
        Assert.assertEquals("10 a Integer",new Integer(10),style.getIntegerValue("margin-bottom"));

    }

    public void testBadParse() {

        Style style = new Style("fuck this");

        Assert.assertEquals("color not valid element so null",null,style.getStringValue("color"));

    }

    public void testSingleElementParse() {

        Style style = new Style("font-family:small_italics");

        Assert.assertEquals("color not valid element so null","small_italics",style.getStringValue("font-family"));

    }

}
