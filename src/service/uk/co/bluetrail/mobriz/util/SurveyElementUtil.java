package uk.co.bluetrail.mobriz.util;



import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.co.bluetrail.mobriz.model.SurveyElement;
import uk.co.bluetrail.mobriz.model.User;


/**
 * Utility class s * 
 * <p>
 * <a href="ConvertUtil.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public final class SurveyElementUtil {
    //~ Static fields/initializers =============================================

    private static Log log = LogFactory.getLog(SurveyElementUtil.class);

    //~ Methods ================================================================

    /**
     * Method to convert a ResourceBundle to a Map object.
     * @param rb a given resource bundle
     * @return Map a populated map
     */
    public static void timeStamp(SurveyElement surveyElement, User user){
    	
    	
    	
    	Calendar cal = Calendar.getInstance();
    	    	     	
    	if(surveyElement.isNew()){
    	
    		surveyElement.setCreated_on(cal.getTime());
    		surveyElement.setCreatedBy_id(user.getId());
    		surveyElement.setUpdated_at(cal.getTime());
    		surveyElement.setLastUpdatedBy_id(user.getId());
    		surveyElement.setVersion(new Integer(1)); //version no longer used so just incremented it here code left in for now just incase we need to turn it back on
        	
    		
    	} else {
    		
    		
    		surveyElement.setUpdated_at(cal.getTime());
    		surveyElement.setLastUpdatedBy_id(user.getId());
    		
    	}
    	
    	
    	
    }
}
