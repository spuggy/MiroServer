/**
 * A utility class to store a line from the devices survey table.
 * 
 *  Has utility functions to see is survey matchs the line
 *  
 *  used in the webservice action.
 */
package uk.co.bluetrail.mobriz.webapp.util;

import java.util.Hashtable;

import uk.co.bluetrail.mobriz.model.Survey;

/**
 * @author Richard Spence
 *
 */
public class DeviceSurvey {
	 	
	public static final char SURVEY_LOADED = 'L' ;
	public static final char SURVEY_TO_LOAD = 'l' ;
	
	private long sid ;
	private long checkPoint ; 
	private char status ; 
	
	
	public DeviceSurvey(String sidStr, String checkPointStr, String statusStr) {
 
		sid = Long.parseLong(sidStr) ;
		checkPoint = Long.parseLong(checkPointStr) ;
		status = statusStr.charAt(0);	
		
		if(status != DeviceSurvey.SURVEY_TO_LOAD && status != DeviceSurvey.SURVEY_LOADED) {
			throw new RuntimeException("Invalid DeviceSurvey status in " + status);
		}
		
	}
	
	public boolean equals(Survey survey) {
		
		if(survey.getId().longValue() == sid && survey.getCheckPoint().longValue() == checkPoint) {
			return true ;
		} else {
			return false;
		}
		
	}
	
	public char getStatus() {
		return status ;
	}
	
	public Long getSid() {
		return new Long(sid);
	}
}
