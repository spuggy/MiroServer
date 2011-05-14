package uk.co.bluetrail.mobriz.webapp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.service.SettingManager;
import uk.co.bluetrail.mobriz.service.SurveyManager;
import uk.co.bluetrail.mobriz.service.SurveyResponseManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class ResponsesPhotoController implements Controller {
    private final Log log = LogFactory.getLog(ResponsesPhotoController.class);

    private SurveyManager  surveyManager = null;
    
    
    public void setSurveyManager(SurveyManager surveyManager) {
		this.surveyManager = surveyManager;
	}
    
    
    

    

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        String survey_id = request.getParameter("id");
        
        
        //the directory to upload to
        String photoPath = request.getSession().getServletContext().getRealPath(Constants.PHOTODIR )  ; 

        
        Survey survey = surveyManager.getSurveyAndFiles(survey_id,photoPath);
        
        
    	Map model = new HashMap() ;
    	model.put("survey" , survey) ;
    	
		
		
        return new ModelAndView("responsesPhotoList", model);
    }



	


	
}
