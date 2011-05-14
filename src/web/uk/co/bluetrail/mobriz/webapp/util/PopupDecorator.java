/*
 * Created on 31-Dec-2005
 *
 */
package uk.co.bluetrail.mobriz.webapp.util;

import org.displaytag.decorator.ColumnDecorator;
import org.displaytag.exception.DecoratorException;
import java.util.Date;
import org.apache.commons.lang.time.FastDateFormat;


/**
 * Popup Decorator class
 * 
 * Used in the decorator tag libs to create a popu based on the value for the SMS field
 * 
 * TODO make this more generic!
 * 
 * <p><a href="ShortDateDecorator.java.html"><i>View Source</i></a></p>
 * 
 * @author <a href="mailto:rspence@bluetrail.co.uk">Richard Spence</a>
 * 
 */
public class PopupDecorator implements ColumnDecorator {

	

	
	/* (non-Javadoc)
	 * @see org.displaytag.decorator.ColumnDecorator#decorate(java.lang.Object)
	 */
	public String decorate(Object columnValue) throws DecoratorException {
		if(columnValue != null) {
			String name = (String) columnValue ;
        return "<input class=\"qrowbutton\" type=\"button\" value=\"sms\" onclick=\"BTL_popUpWindow('sms.html?username=" + name +"')\">" ;
		} else {
			return null ; 
		}
	}

}
