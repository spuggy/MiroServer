package uk.co.bluetrail.mobriz.webapp.action;



import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.service.SurveyManager;
import uk.co.bluetrail.mobriz.service.SurveyResponseManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class ShowResponseController implements Controller {
	private final Log log = LogFactory.getLog(SurveyResponseExportController.class);

    private SurveyResponseManager surveyResponseManager = null;
    
    
      
    
    public void setSurveyResponseManager(SurveyResponseManager surveyResponseManager) {
		this.surveyResponseManager = surveyResponseManager;
	}

    

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        String id = request.getParameter("id");
        
        if(id==null) {
        	id = request.getParameter("response_id");
        }
        
        SurveyResponse surveyResponse = surveyResponseManager.getSurveyResponse(id) ;
        
        return new ModelAndView("responseShow", "surveyResponse", surveyResponse);
    }


}
