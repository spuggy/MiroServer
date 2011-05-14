package uk.co.bluetrail.mobriz.webapp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import uk.co.bluetrail.mobriz.webapp.action.BaseFormController;
import uk.co.bluetrail.mobriz.webapp.form.SurveyForm;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.service.SurveyManager;
import uk.co.bluetrail.mobriz.service.UserManager;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class SurveyNewFormController extends BaseFormController {
    private SurveyManager surveyManager = null;
    private UserManager userManager = null;

    public void setSurveyManager(SurveyManager surveyManager) {
        this.surveyManager = surveyManager;
    }
    public SurveyNewFormController() {
        setCommandName("survey");
        setCommandClass(Survey.class);
    }

    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {

    
    	
    	Survey survey=new Survey();
        
        
        
        return survey;
    }
    
    
    
    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        Survey survey = (Survey) command;
        
        surveyManager.saveSurvey(survey,getCurrentUser());        
        
        return new ModelAndView("redirect:editSurvey.html", "id", survey.getId());
        
        
    }
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
}
