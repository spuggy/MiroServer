package uk.co.bluetrail.mobriz.webapp.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.MiroProject;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.MiroProjectManager;
import uk.co.bluetrail.mobriz.service.SurveyManager;
import uk.co.bluetrail.mobriz.service.SurveyResponseManager;
import uk.co.bluetrail.mobriz.service.UserManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiroSurveyController extends BaseController {
    private final Log log = LogFactory.getLog(MiroSurveyController.class);
    private SurveyManager surveyManager = null;
    private SurveyResponseManager surveyResponseManager= null;
    private UserManager userManager;
	private MiroProjectManager miroProjectManager;
       
    
    
    /**
	 * @param miroProjectManager the miroProjectManager to set
	 */
	public void setMiroProjectManager(MiroProjectManager miroProjectManager) {
		this.miroProjectManager = miroProjectManager;
	}

	public void setSurveyResponseManager(SurveyResponseManager surveyResponseManager) {
		this.surveyResponseManager = surveyResponseManager;
	}

	public void setSurveyManager(SurveyManager surveyManager) {
        this.surveyManager = surveyManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method for MiroSurveyController...");
        }

        User candidate = getCurrentUser();
        MiroProject miroProject = miroProjectManager.getMiroProject(candidate.getProject_id().toString());
		User practitioner = userManager.getUser(miroProject.getCreatedBy_id().toString());

        String survey_id = practitioner.getDefault_survey_id() ;

        Survey survey = surveyManager.getSurvey(survey_id);


        if(survey==null) {
            return new ModelAndView("miroNoTestsToComplete");
        }

        List surveyResponses = surveyResponseManager.getSurveyResponses(survey,this.getCurrentUser()) ;
        
        
        Map model = new HashMap() ;
      	model.put("survey" , survey) ;
      	model.put("candidate" , candidate) ;
      	model.put("practitioner" , practitioner) ;
        model.put("isVersion11",(survey.getId().longValue() == Constants.Survey_id_Mirov11) ? true:false );
          	

        
        if(surveyResponses == null || surveyResponses.size() ==0 ) {
        	return new ModelAndView("miroSurveyForm", model);
        } else {
        	return new ModelAndView("miroTestComplete", model);
        }
    }

	/**
	 * @param userManager the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
}
