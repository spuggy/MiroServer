package uk.co.bluetrail.mobriz.webapp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.util.SurveyElementUtil;
import uk.co.bluetrail.mobriz.webapp.action.BaseFormController;
import uk.co.bluetrail.mobriz.webapp.form.WebServiceForm;
import uk.co.bluetrail.mobriz.webapp.util.DeviceSurvey;
import uk.co.bluetrail.mobriz.webservice.QuestionAttributes;
import uk.co.bluetrail.mobriz.model.Constraint;
import uk.co.bluetrail.mobriz.model.Option;
import uk.co.bluetrail.mobriz.model.Question;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyElement;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.SettingManager;
import uk.co.bluetrail.mobriz.service.SurveyManager;
import uk.co.bluetrail.mobriz.service.SurveyResponseManager;
import uk.co.bluetrail.mobriz.service.UserManager;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;



public class SurveyWebServiceController extends BaseFormController {
	private SurveyResponseManager srMgr ;
	private SurveyManager mgr ; 
	
	
	
	public SurveyWebServiceController(){
		setCommandName("webServiceForm");
        setCommandClass(WebServiceForm.class);
	}
	
	
    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
    	
        return new WebServiceForm();
    }

    public ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse response, Object command, BindException errors)   throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        
        
        WebServiceForm wsForm = null;

		String opCode;

		wsForm = (WebServiceForm) command;
	    
		Map model = new HashMap() ;
		model.put("wsForm" , wsForm) ;
		
		return new ModelAndView("surveyWebServiceView",model) ;
    }
}