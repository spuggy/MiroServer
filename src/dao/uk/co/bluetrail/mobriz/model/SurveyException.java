/**
 * 
 */
package uk.co.bluetrail.mobriz.model;

/**
 * @author Richard Spence
 *
 */
public class SurveyException extends RuntimeException {

	
	String s  ;
	
	public SurveyException(String s) { 
		super(s) ;		
		this.s= s;
	}
	
	
	
}
