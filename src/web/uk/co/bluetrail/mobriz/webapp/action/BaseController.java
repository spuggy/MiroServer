package uk.co.bluetrail.mobriz.webapp.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.Controller;

import uk.co.bluetrail.mobriz.model.User;

public abstract class BaseController implements Controller {

	   protected final transient Log log = LogFactory.getLog(getClass());
	   protected final String MESSAGES_KEY = "successMessages";
	   
	
public User getCurrentUser() {
    	
    	
    	SecurityContext sc = SecurityContextHolder.getContext() ;
  	  
    	
    	try { 
    	
    		return  (User)  sc.getAuthentication().getPrincipal() ;
    		
    	} catch (Exception e) {
    		return null;
    	}
    	
    	
    	
    }

	public void saveMessage(HttpServletRequest request, String msg) {
    List messages = (List) request.getSession().getAttribute(MESSAGES_KEY);

    if (messages == null) {
        messages = new ArrayList();
    }

    messages.add(msg);
    request.getSession().setAttribute(MESSAGES_KEY, messages);
}

}
