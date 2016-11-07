package uk.co.bluetrail.mobriz.webapp.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationTrustResolver;
import org.acegisecurity.AuthenticationTrustResolverImpl;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContext;

import org.apache.commons.lang.StringUtils;
import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Role;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.RoleManager;
import uk.co.bluetrail.mobriz.service.SettingManager;
import uk.co.bluetrail.mobriz.service.UserManager;
import uk.co.bluetrail.mobriz.service.UserExistsException;
import uk.co.bluetrail.mobriz.util.StringUtil;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


public class UserConfirmFormController extends BaseFormController {
    private RoleManager roleManager;
	private SettingManager settingManager;

    /**
     * @param roleManager The roleManager to set.
     */
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    public UserConfirmFormController() {
        setCommandName("user");
        setCommandClass(User.class);
    }

   
    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
       
    	
    	
    	if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        User user = (User) command;
       
        if(user.getFirstName().equals("") || user.getLastName().equals("")){
            saveMessage(request, "First name and last name are required fields");
            return showForm(request, response, errors);

        }
        
        User user2Update = getUserManager().getUser(user.getId().toString());
        
        user2Update.setFirstName(user.getFirstName());
        user2Update.setLastName(user.getLastName());
        
        getUserManager().saveUser(user2Update);
        
        
        return new ModelAndView("redirect:miroSurvey.html");
        
    }

  
    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
       
    	
    	  
            return getCurrentUser();
        
    }

    

	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}
}
