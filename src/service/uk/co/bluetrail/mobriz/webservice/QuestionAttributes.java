/*
 * Created on 03-Mar-2006
 *
 */
package uk.co.bluetrail.mobriz.webservice;

/**
 * QuestionAttributes class
 * 
 * a class that encapsulates the attribute string for a question.
 * this si used to mung a few attributes together into a 101010110
 * king od string so that we don'w have to send loads of fields down
 * 
 * 
 * <p><a href="QuestionAttributes.java.html"><i>View Source</i></a></p>
 * 
 * @author <a href="mailto:rspence@bluetrail.co.uk">Richard Spence</a>
 * 
 */
public class QuestionAttributes {

	String attr ;
	public final static String  ON = "1" ;
	public final static String  OFF = "0" ;
	boolean required = false ; 
	boolean sticky = false ; 
	
	public String getAttr() {
		StringBuffer sb = new StringBuffer() ; 
		
		if(required){
			sb.append(ON) ; 
		} else { 
			sb.append(OFF) ;
		}
	
		if(sticky){
			sb.append(ON) ; 
		} else { 
			sb.append(OFF) ;
		}	
		
		return sb.toString();
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	public void setSticky(boolean sticky) {
		this.sticky = sticky;
	}
	
	
	
}
