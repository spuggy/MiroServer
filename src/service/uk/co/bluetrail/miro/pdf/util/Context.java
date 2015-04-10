package uk.co.bluetrail.miro.pdf.util;

import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by richard on 20/03/15.
 */
public class Context {

    Map<String,Font> fontMap = new HashMap<String,Font>();
    Map<String,Color> colourMap = new HashMap<String,Color>();

    public float spacingAfter = 10;
    public String filePath = ""  ;
    private Font defaultFont = null;
    private Color defaultColor = Color.gray;
    public String listSymbol = "\u2022";
    public float listIndentation = 5f;
    public String listSpaceAfterSymbol = " ";
    public Rectangle pageSize = PageSize.A4 ;
    public float marginLeft = 56f, marginRight = 56f, marginTop = 84f, marginBottom = 56f;
    public float leading = 3f;
    public float imageConstant = 0.48f;


    public Context(String filePath) {
        if(filePath!=null) {
           this.filePath = filePath;
        }

        try {
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1257, BaseFont.NOT_EMBEDDED);
            this.defaultFont = new Font(bf, 10f, Font.NORMAL);
        } catch (Exception e) {
            throw new MiroException(e.getMessage());
        }
    }

    public void addColor(String name,String hex) {

        this.colourMap.put(name,hex2Rgb(hex));

    }

    public Color getColor(String name) {

        Color c = colourMap.get(name) ;
        if(c == null) {
            return defaultColor;
        }  else {
            return c;
        }

    }



    public Font getFont(String name) {

        Font f = fontMap.get(name) ;
        if(f == null) {
            return defaultFont;
        } else {
            return f;
        }


    }

    public void addFont(String name, Font font) {
        this.fontMap.put(name, font);
    }

    private static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
    }
}
