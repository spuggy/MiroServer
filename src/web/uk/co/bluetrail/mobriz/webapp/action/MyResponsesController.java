package uk.co.bluetrail.mobriz.webapp.action;

//TODO make surveys add account ids

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.service.SurveyManager;
import uk.co.bluetrail.mobriz.service.SurveyResponseManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class MyResponsesController extends BaseController {
    private final Log log = LogFactory.getLog(MyResponsesController.class);
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

        Survey survey = new Survey();
        // populate object with request parameters
        BeanUtils.populate(survey, request.getParameterMap());

        List responses = surveyResponseManager.getSurveyResponses(getCurrentUser());
        
        ModelAndView mv = new ModelAndView("myResponsesList", "responseList", responses); 
        
        return mv ;
    }
}
