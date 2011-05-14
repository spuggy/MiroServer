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

public class SurveyResponseExportController implements Controller {
    private final Log log = LogFactory.getLog(SurveyResponseExportController.class);

    private SurveyManager  surveyManager = null;
    private SurveyResponseManager surveyResponseManager = null;
    
    
    public void setSurveyManager(SurveyManager surveyManager) {
		this.surveyManager = surveyManager;
	}
    
    
    public void setSurveyResponseManager(SurveyResponseManager surveyResponseManager) {
		this.surveyResponseManager = surveyResponseManager;
	}

    

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        String survey_id = request.getParameter("id");
        
        Survey survey = surveyManager.getSurvey(survey_id);
        
        SurveyResponse surveyResponse = new SurveyResponse() ;
        surveyResponse.setSurvey_id(new Long(survey_id));
        
        List surveyResponses = surveyResponseManager.getSurveyResponses(surveyResponse);
        
    	Map model = new HashMap() ;
		model.put("survey" , survey) ;
		model.put("surveyResponses" , surveyResponses);
        
        return new ModelAndView("surveyResponseExportView", model);
    }



	


	
}
