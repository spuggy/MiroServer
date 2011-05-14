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
 * ShortDateDecorator class
 * 
 * Used in the decorator tag libs to format a date english styleee
 * 
 * <p><a href="ShortDateDecorator.java.html"><i>View Source</i></a></p>
 * 
 * @author <a href="mailto:rspence@bluetrail.co.uk">Richard Spence</a>
 * 
 */
public class ShortDateDecorator implements ColumnDecorator {

	/**
     * FastDateFormat used to format the date object.
     */
    private FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy"); //$NON-NLS-1$

	
	/* (non-Javadoc)
	 * @see org.displaytag.decorator.ColumnDecorator#decorate(java.lang.Object)
	 */
	public String decorate(Object columnValue) throws DecoratorException {
		if(columnValue != null) {
		Date date = (Date) columnValue;
        return this.dateFormat.format(date);
		} else {
			return null ; 
		}
	}

}
