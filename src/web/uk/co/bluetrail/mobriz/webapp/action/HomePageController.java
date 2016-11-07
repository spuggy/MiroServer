package uk.co.bluetrail.mobriz.webapp.action;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.LabelValue;
import uk.co.bluetrail.mobriz.model.Role;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.SettingManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class HomePageController implements Controller {
    private final Log log = LogFactory.getLog(HomePageController.class);
    
    
    Properties homePages = null;
      
    
	public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        List roles = getCurrentUser().getRoleList() ;
        
        Iterator itr = roles.iterator() ;
        
        LabelValue role = null ; 
        
        boolean isSysAdmin = false;
        boolean isAdmin = false;
        boolean isClient = false;
        boolean isDepartmentAdmin = false;




        while(itr.hasNext()) {
        	role = (LabelValue) itr.next() ;
        	
        	if(role.getValue().equals(Constants.SYSADMIN_ROLE)) {
        		isSysAdmin = true;
        	}

            if(role.getValue().equals(Constants.DEPARTMENT_ADMIN_ROLE)) {
                isDepartmentAdmin = true;
            }


        	if(role.getValue().equals(Constants.ADMIN_ROLE)) {
        		isAdmin = true;
        	}
        	if(role.getValue().equals(Constants.CLIENT_ROLE)) {
        		isClient = true;
        	}
      
        }


        
    	if(isSysAdmin) {
    		return new ModelAndView((String) homePages.get(Constants.SYSADMIN_ROLE));
    	}

        if(isDepartmentAdmin) {
            return new ModelAndView((String) homePages.get(Constants.DEPARTMENT_ADMIN_ROLE));
        }

        if(isAdmin) {
    		return new ModelAndView((String) homePages.get(Constants.ADMIN_ROLE));
    	} 
    	
    	if(isClient) {
    		return new ModelAndView((String) homePages.get(Constants.CLIENT_ROLE));
    	}

		//this is a candidate logging in
		//if they have not done a survey send them to confirm name else send them the the
		//complete or download page
		if(getCurrentUser()!=null && getCurrentUser().getStatus() < User.ASSESSMENT_COMPLETE)  {
			return new ModelAndView((String) homePages.get(Constants.USER_ROLE));
		} else {
			return new ModelAndView("redirect:miroSurvey.html");
		}


        
    }
    
    private User getCurrentUser() {
    	
    	
    	
    	SecurityContext sc = SecurityContextHolder.getContext() ;
  	  
    	
    	try { 
    	
    		return  (User)  sc.getAuthentication().getPrincipal() ;
    	} catch (Exception e) {
    		return null;
    	}
    	
    	
    	
    }

	public Properties getHomePages() {
		return homePages;
	}

	public void setHomePages(Properties homePages) {
		this.homePages = homePages;
	}
}
