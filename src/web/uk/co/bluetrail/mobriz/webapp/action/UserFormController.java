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

/**
 * Implementation of <strong>SimpleFormController</strong> that interacts with
 * the {@link UserManager} to retrieve/persist values to the database.
 *
 * <p><a href="UserFormController.java.html"><i>View Source</i></a>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UserFormController extends BaseFormController {
    private RoleManager roleManager;
	private SettingManager settingManager;

    /**
     * @param roleManager The roleManager to set.
     */
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    public UserFormController() {
        setCommandName("user");
        setCommandClass(User.class);
    }

    public ModelAndView processFormSubmission(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Object command,
                                              BindException errors)
    throws Exception {
        if (request.getParameter("cancel") != null) {
            if (!StringUtils.equals(request.getParameter("from"), "list")) {
                return new ModelAndView(getCancelView());
            } else {
                return new ModelAndView(getSuccessView());
            }
        }

        return super.processFormSubmission(request, response, command, errors);
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
       
    	
    	if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        User user = (User) command;
        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
 
            getUserManager().removeUser(user.getId().toString());
            saveMessage(request, getText("user.deleted", user.getFullName(), locale));

            return new ModelAndView(getSuccessView());
        } else {
            Boolean encrypt = (Boolean) getConfiguration().get(Constants.ENCRYPT_PASSWORD);

            if (StringUtils.equals(request.getParameter("encryptPass"), "true") 
                    && (encrypt != null && encrypt.booleanValue())) {

                String algorithm = (String) getConfiguration().get(Constants.ENC_ALGORITHM);

                if (algorithm == null) { // should only happen for test case

                    if (log.isDebugEnabled()) {
                        log.debug("assuming testcase, setting algorithm to 'SHA'");
                    }

                    algorithm = "SHA";
                }

                user.setPassword(StringUtil.encodePassword(user.getPassword(), algorithm));
            }

           
            
            
           Role userRole = roleManager.getRole(Constants.USER_ROLE);
           Role adminRole = roleManager.getRole(Constants.ADMIN_ROLE);
           Role teamReportRole = roleManager.getRole(Constants.TEAMREPORT_ROLE);
           Role departmentAdminRole = roleManager.getRole(Constants.DEPARTMENT_ADMIN_ROLE);
           Role sysAdminRole = roleManager.getRole(Constants.SYSADMIN_ROLE);



            String[] roleNames = (String[]) request.getParameterValues("userRoles");
       	   user.getRoles().clear();
           user.addRole(userRole) ;
           user.addRole(adminRole);
           if(isMember(roleNames,Constants.TEAMREPORT_ROLE)) {
        	   user.addRole(teamReportRole);
           }
            if(isMember(roleNames,Constants.DEPARTMENT_ADMIN_ROLE)) {
                user.addRole(departmentAdminRole);
            }

            if(isMember(roleNames,Constants.SYSADMIN_ROLE)) {
                user.addRole(sysAdminRole);
            }



            try {
            if (user.getPinNumber().equals(Constants.BLANKPIN)) {
    			String newPin = Integer.toHexString(new String(user.getUsername())
    					.hashCode());
    			user.setPinNumber(newPin);

    		}
            } catch (Exception e) {
            	//ignore odd exception
            }
            
            //set the account number to the current editing user
            if(user.getAccount_id() ==null) {
            	user.setAccount_id(getCurrentUser().getAccount_id());
            }

            Integer originalVersion = user.getVersion();
            
            try {
                getUserManager().saveUser(user);
            } catch (UserExistsException e) {
                log.warn(e.getMessage());

                
                errors.rejectValue("username", "errors.existing.user",
                                   new Object[] {
                                       user.getUsername(), user.getEmail()
                                   }, "duplicate user");

                // redisplay the unencrypted passwords
                user.setPassword(user.getConfirmPassword());
                // reset the version # to what was passed in
                user.setVersion(originalVersion);
                
                return showForm(request, response, errors);
            }

            if (!StringUtils.equals(request.getParameter("from"), "list")) {
                saveMessage(request, getText("user.saved", user.getFullName(), locale));

                // return to main Menu
                return new ModelAndView(new RedirectView("mainMenu.html"));
            } else {
                if (StringUtils.isBlank(request.getParameter("version"))) {
                    saveMessage(request, getText("user.added", user.getFullName(), locale));

                    Setting newUserEmailSubject  = settingManager.getSettingByName("NEWUSER_EMAILSUBJECT");
                    Setting newUserEmailMessage  = settingManager.getSettingByName("NEWUSER_EMAILMESSAGE");
                                  
                    
                    // Send an account information e-mail
                    message.setSubject(newUserEmailSubject.getSettingValue());
                    sendUserMessage(user, newUserEmailMessage.getSettingValue(), RequestUtil.getAppURL(request));

                    return new ModelAndView(getSuccessView());
                } else {
                    saveMessage(request, getText("user.updated.byAdmin", user.getFullName(), locale));
                }
            }
        }

        return showForm(request, response, errors);
    }

    private boolean isMember(String[] roleNames, String roleName) {
		if(roleNames == null) {
			return false;
		}
		
		for(int i = 0 ; i < roleNames.length ; i++) {
			if(roleNames[i].equals(roleName)) {
				return true;
			}
		}
	
		return false;
	}

	protected ModelAndView showForm(HttpServletRequest request,
                                    HttpServletResponse response,
                                    BindException errors)
    throws Exception {
        if (request.getRequestURI().indexOf("editProfile") > -1) {
            // if URL is "editProfile" - make sure it's the current user
            // reject if username passed in or "list" parameter passed in
            // someone that is trying this probably knows the AppFuse code
            // but it's a legitimate bug, so I'll fix it. ;-)
            if ((request.getParameter("username") != null) || (request.getParameter("from") != null)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                log.warn("User '" + request.getRemoteUser() + "' is trying to edit user '" +
                         request.getParameter("username") + "'");

                return null;
            }
        }

        // prevent ordinary users from calling a GET on editUser.html
        // unless a bind error exists.
        if ((request.getRequestURI().indexOf("editUser") > -1) && (!request.isUserInRole(Constants.ADMIN_ROLE) &&
                (errors.getErrorCount() == 0) && // be nice to server-side validation for editProfile
                (request.getRemoteUser() != null))) { // be nice to unit tests
            response.sendError(HttpServletResponse.SC_FORBIDDEN);

            return null;
        }

        return super.showForm(request, response, errors);
    }

    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
        if (!isFormSubmission(request)) {
            String username = request.getParameter("username");

            // if user logged in with remember me, display a warning that they can't change passwords
            log.debug("checking for remember me login...");

            AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
            SecurityContext ctx = SecurityContextHolder.getContext();

            if (ctx.getAuthentication() != null) {
                Authentication auth = ctx.getAuthentication();

                if (resolver.isRememberMe(auth)) {
                    request.getSession().setAttribute("cookieLogin", "true");

                    // add warning message
                    saveMessage(request, getText("userProfile.cookieLogin", request.getLocale()));
                }
            }

            User user = null;

            if (request.getRequestURI().indexOf("editProfile") > -1) {
                user = getUserManager().getUserByUsername(request.getRemoteUser());
            } else if (!StringUtils.isBlank(username) && !"".equals(request.getParameter("version"))) {
                user = getUserManager().getUserByUsername(username);
            } else {
            	
            	
            	Setting creditBalance = settingManager.getSettingByName("NEWUSER_CREDITS");
                user = new User();
               
                user.addRole(roleManager.getRole(Constants.USER_ROLE));
                user.addRole(roleManager.getRole(Constants.ADMIN_ROLE));  
                user.setEnabled(true);
                user.setCreditBalance(Integer.parseInt(creditBalance.getSettingValue()));
            }

            user.setConfirmPassword(user.getPassword());

            if(user.getPinNumber() == null || user.getPinNumber().equals("")) {
            	user.setPinNumber(Constants.BLANKPIN);
            }
            
            
            return user;
        }
        return super.formBackingObject(request);
    }

    protected void onBind(HttpServletRequest request, Object command)
    throws Exception {
        // if the user is being deleted, turn off validation
        if (request.getParameter("delete") != null) {
            super.setValidateOnBinding(false);
        } else {
            super.setValidateOnBinding(true);
        }
    }

	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}
}
