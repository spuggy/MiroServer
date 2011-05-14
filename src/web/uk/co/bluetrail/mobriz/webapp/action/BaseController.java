package uk.co.bluetrail.mobriz.webapp.action;

import java.util.Locale;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.Controller;

import uk.co.bluetrail.mobriz.model.User;

public abstract class BaseController implements Controller {

	   protected final transient Log log = LogFactory.getLog(getClass());
	   
	
public User getCurrentUser() {
    	
    	
    	SecurityContext sc = SecurityContextHolder.getContext() ;
  	  
    	
    	try { 
    	
    		return  (User)  sc.getAuthentication().getPrincipal() ;
    		
    	} catch (Exception e) {
    		return null;
    	}
    	
    	
    	
    }
	
}
