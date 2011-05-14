package uk.co.bluetrail.mobriz.webapp.action;



import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.service.SurveyManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class SurveyAdminController implements Controller {
    private final Log log = LogFactory.getLog(SurveyAdminController.class);
    private SurveyManager surveyManager = null;
  
    public void setSurveyManager(SurveyManager surveyManager) {
        this.surveyManager = surveyManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        Survey survey = new Survey();
        // populate object with request parameters
        BeanUtils.populate(survey, request.getParameterMap());

        List surveys = surveyManager.getSurveys(survey);
        
        
        return new ModelAndView("surveyAdminList", Constants.SURVEY_LIST, surveys);
    }
}
