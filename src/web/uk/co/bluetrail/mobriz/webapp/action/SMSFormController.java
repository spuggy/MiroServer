package uk.co.bluetrail.mobriz.webapp.action;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;


import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.webapp.action.BaseFormController;
import uk.co.bluetrail.mobriz.webapp.form.SmsMessageForm;  
import uk.co.bluetrail.mobriz.webapp.util.NotFoundException;
import uk.co.bluetrail.mobriz.model.Account;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.AccountManager;
import uk.co.bluetrail.mobriz.service.MailEngine;
import uk.co.bluetrail.mobriz.service.SettingManager;
import uk.co.bluetrail.mobriz.service.UserManager;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class SMSFormController extends BaseFormController {
    private UserManager userManager = null;
    private SettingManager settingManager = null ;
    	
    
    public SettingManager getSettingManager() {
		return settingManager;
	}
	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}
	public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    public SMSFormController() {
    	
        setCommandName("smsMessageForm");
        setCommandClass(SmsMessageForm.class);
        
    }

    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
        
    	
    	
    	String username = request.getParameter("username");
        
    	if(username != null ) {
    	
    	User user = userManager.getUserByUsername(username);
    	
        if(user == null) {
        	throw new NotFoundException();
        }
           
        Setting url = settingManager.getSettingByName(Constants.SETTING_DOWNLOADURL);
        
        SmsMessageForm smsMessage = new SmsMessageForm(user) ;  
                
        Locale locale = request.getLocale();
       
        Object[] params = new Object[2] ;
        params[0] = user.getPinNumber() ;
        params[1] = url.getSettingValue();
                                   
        smsMessage.setSmsMessage(getText("smsform.smsmessage",params, locale));
        
        return smsMessage;
    	}
        
        return super.formBackingObject(request);
        
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
       
    	
    	if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        SmsMessageForm smsMessageForm = (SmsMessageForm) command;
        
        sendSmsMessage(smsMessageForm.getPhoneNumber(),smsMessageForm.getSmsMessage());
        
        
        return new ModelAndView(getSuccessView());
    }
    
    
    
}
