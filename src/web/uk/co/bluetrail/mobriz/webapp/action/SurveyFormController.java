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
import uk.co.bluetrail.mobriz.model.LookupDef;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.service.LookupDefManager;
import uk.co.bluetrail.mobriz.service.SurveyManager;
import uk.co.bluetrail.mobriz.service.UserManager;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class SurveyFormController extends BaseFormController {
    private SurveyManager surveyManager = null;
    private UserManager userManager = null;
    private LookupDefManager lookupDefManager = null;
        
    public void setLookupDefManager(LookupDefManager lookupDefManager) {
		this.lookupDefManager = lookupDefManager;
	}
	public void setSurveyManager(SurveyManager surveyManager) {
        this.surveyManager = surveyManager;
    }
    public SurveyFormController() {
        setCommandName("survey");
        setCommandClass(Survey.class);
    }

    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
    	
    	String id = request.getParameter("id");
        Survey survey = null; 
                
        
        if (!StringUtils.isEmpty(id)) {
            survey = surveyManager.getSurvey(id);           
        } else {
        	survey=new Survey();
        }
        
        
        return survey;
    }
    
    
    protected Map referenceData(HttpServletRequest request)  throws Exception {
    	
    	Map refData = new HashMap() ;
    	
    	List users = userManager.getEnabledUsers() ;    	
    	refData.put("userList" , users) ;
		
		LookupDef lookupDef = new LookupDef();
		List lookupDefs = lookupDefManager.getLookupDefs(lookupDef);
		refData.put("lookupDefs" , lookupDefs) ;
    	
    	return refData;
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        Survey survey = (Survey) command;
        boolean isNew = (survey.getId() == null);
        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            surveyManager.removeSurvey(survey.getId().toString());

            saveMessage(request, getText("survey.deleted", locale));
        } else {
            surveyManager.saveSurvey(survey,getCurrentUser());

            String key = (isNew) ? "survey.added" : "survey.updated";
            saveMessage(request, getText(key, locale));

            if (!isNew) {
                return new ModelAndView("redirect:editSurvey.html", "id", survey.getId());
            }
        }

        return new ModelAndView(getSuccessView());
    }
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
}
