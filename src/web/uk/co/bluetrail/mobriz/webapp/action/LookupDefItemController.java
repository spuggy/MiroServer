package uk.co.bluetrail.mobriz.webapp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Account;
import uk.co.bluetrail.mobriz.model.LookupDef;
import uk.co.bluetrail.mobriz.model.LookupDefItem;
import uk.co.bluetrail.mobriz.service.LookupDefItemManager;
import uk.co.bluetrail.mobriz.service.LookupDefManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class LookupDefItemController implements Controller {
    private final Log log = LogFactory.getLog(LookupDefItemController.class);
    private LookupDefItemManager lookupDefItemManager = null;
    private LookupDefManager lookupDefManager = null;
    protected final String MESSAGES_KEY = "successMessages";
    private String importStr = "import" ;
    private String exportStr = "export" ;

    
    public void setLookupDefItemManager(LookupDefItemManager lookupDefItemManager) {
        this.lookupDefItemManager = lookupDefItemManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method... ");
        }
       
       
        
        String uri =   request.getRequestURI() ;
        
        if(uri.toLowerCase().indexOf(exportStr) != -1) {
        	return doExport(request, response) ;  
    	} 
    	
        if(uri.toLowerCase().indexOf("deletelookupitem") != -1) {
        	return doDelete(request, response) ;  
    	} 
    	
        
        throw new Exception("Unknown action " + uri);
        
        
    }

	private ModelAndView doDelete(HttpServletRequest request,
			HttpServletResponse response) {

		LookupDefItem lookupDefItem = new LookupDefItem();
		// populate object with request parameters
		String id = request.getParameter("id");
		
		LookupDefItem lookupDefItemToDel = lookupDefItemManager.getLookupDefItem(id);
		
		Long lookupDefId = lookupDefItemToDel.getLookupDef_id();
					
	    lookupDefItemManager.removeLookupDefItem(lookupDefItemToDel.getId()+"");

	    request.getSession().setAttribute(MESSAGES_KEY, "Item Deleted");
           
         return new ModelAndView("redirect:lookupItemsUpload.html", "id", lookupDefId);
		
		
		
		
	}
	
	

	private ModelAndView doExport(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		LookupDefItem lookupDefItem = new LookupDefItem();
		// populate object with request parameters
		BeanUtils.populate(lookupDefItem, request.getParameterMap());

		LookupDef lookupDef = lookupDefManager.getLookupDef(lookupDefItem.getLookupDef_id().toString());
		List lookupDefItems = lookupDefItemManager.getLookupDefItemsQBE(lookupDefItem);

		
		Map model = new HashMap() ;
		model.put("lookupDef" , lookupDef) ;
		model.put("lookupDefItems" , lookupDefItems) ;
		
		return new ModelAndView("lookupDefExportView", model);
	  
		
		
		
		
	}

	

	public void setLookupDefManager(LookupDefManager lookupDefManager) {
		this.lookupDefManager = lookupDefManager;
	}
}
