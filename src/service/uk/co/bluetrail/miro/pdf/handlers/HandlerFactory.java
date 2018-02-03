package uk.co.bluetrail.miro.pdf.handlers;

import com.lowagie.text.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import uk.co.bluetrail.miro.pdf.util.Style;

/**
 * Created by richard on 20/03/15.
 */
public class HandlerFactory {

    private static HandlerFactory handlerFactory = null;


    public static HandlerFactory instance() {

        if (HandlerFactory.handlerFactory == null) {
            HandlerFactory.handlerFactory = new HandlerFactory();
        }

        return HandlerFactory.handlerFactory;
    }

    private HandlerFactory() {

    }

    public Handler getHandler(Node node,Document document) {

        if(node==null) {
            return new DefaultHandler(node);
        }

        String name = node.getNodeName();
        NamedNodeMap attr = node.getAttributes();

        String className = "";
        Style style = null;
        if (attr != null) {
            Node clazz = attr.getNamedItem("class");
            if(clazz !=null) {
                className = clazz.getNodeValue();
            }
            Node styleAttr = attr.getNamedItem("style");
            if(styleAttr != null) {
                style = new Style(styleAttr.getNodeValue());
            }


        }
        if (name.equals("ul") && className.equals("population_bar_chart")) {
            return new PopulationBarChart(node,style);
        } else if (name.equals("ul")) {
            return new Ul(node);
        } else if (name.equals("b") && className.equals("firstpage")) {
            //so as not to fuck with this just now
            Style localStyle = new Style("font-family:firstpagefontbold");
            return new B(node, localStyle);
        } else if (name.equals("b")) {
            return new B(node,style);
        } else if (name.equals("b")) {
            return new B(node);
        } else if (name.equals("img")) {
            return new Img(node);
        } else if (name.equals("p") && className.equals("firstpage")) {
            //so as not to fuck with this just now
            Style localStyle = new Style("font-family:firstpagefont");
            return new P(node, localStyle);
        }  else if (name.equals("p") && className.equals("fouricons")) {
            return new FourIcons(node);
        } else if (name.equals("p") && className.equals("practitioner")) {
            return new Practitioner(node);
        } else if (name.equals("p") && className.equals("miropie")) {
            return new PieChart(node,style);
        } else if (name.equals("p") && className.equals("miropieflexi")) {
            return new PieChartFlexiBullet(node);
        } else if (name.equals("p")) {
            return new P(node,style);
        } else if (name.equals("h1")) {
            return new H(node, 1);
        } else if (name.equals("h3")) {
            return new H(node,3);
        } else if (name.equals("h4")) {
            return new H(node, 4);
        } else if (name.equals("h2")) {
            return new H(node, 2);
        } else if (name.equals("table") && className.equals("pie_and_bullets")) {
            return new PieAndBullets(node);
        }  else {
            return new DefaultHandler(node);
        }

    }


}
