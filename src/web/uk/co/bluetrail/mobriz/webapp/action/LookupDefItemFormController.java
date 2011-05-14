package uk.co.bluetrail.mobriz.webapp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import uk.co.bluetrail.mobriz.webapp.action.BaseFormController;
import uk.co.bluetrail.mobriz.model.LookupDef;
import uk.co.bluetrail.mobriz.model.LookupDefItem;
import uk.co.bluetrail.mobriz.service.LookupDefItemManager;
import uk.co.bluetrail.mobriz.service.LookupDefManager;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class LookupDefItemFormController extends BaseFormController {
    private LookupDefItemManager lookupDefItemManager = null;
    private LookupDefManager lookupDefManager = null;

   
    
    public void setLookupDefManager(LookupDefManager lookupDefManager) {
        this.lookupDefManager = lookupDefManager;
    }

    public void setLookupDefItemManager(LookupDefItemManager lookupDefItemManager) {
        this.lookupDefItemManager = lookupDefItemManager;
    }
    
  
    
    public LookupDefItemFormController() {
        setCommandName("lookupDefItem");
        setCommandClass(LookupDefItem.class);
    }

    protected Map referenceData(HttpServletRequest request)  throws Exception {

		String lookupDef_id = request.getParameter("lookupDef_id");
		String id = request.getParameter("id");
        LookupDef lookupDef = null;
        List lookupDefItemList = null; 
        LookupDefItem lookupDefItem = null;
        
        if (!StringUtils.isEmpty(lookupDef_id)) {
        	lookupDef = lookupDefManager.getLookupDef(lookupDef_id);        	
        } else {
        	lookupDefItem = lookupDefItemManager.getLookupDefItem(id);
        	lookupDef = lookupDefManager.getLookupDef(lookupDefItem.getLookupDef_id().toString());
        }
                
    	Map refData = new HashMap() ;
    	
    	refData.put("lookupDef" , lookupDef) ;
    	
    	
    	return refData;
    }
    
    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
    	String id = request.getParameter("id");
        LookupDefItem lookupDefItem = null;

        if (!StringUtils.isEmpty(id)) {
            lookupDefItem = lookupDefItemManager.getLookupDefItem(id);
        } else {
            lookupDefItem = new LookupDefItem();
        }

        return lookupDefItem;
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        LookupDefItem lookupDefItem = (LookupDefItem) command;
        boolean isNew = (lookupDefItem.getId() == null);
        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            lookupDefItemManager.removeLookupDefItem(lookupDefItem.getId().toString());

            saveMessage(request, getText("lookupDefItem.deleted", locale));
            
            return new ModelAndView("redirect:lookupItemsUpload.html", "id", lookupDefItem.getLookupDef_id());
        } else {
        	
        		        	
        		if(lookupDefItem.isEmpty() ) {
            		errors.reject("lookupDefItem.emptyFieldsError");
            		return showForm(request, response, errors);
            	}
            	
            	
        	
        	
        	
            lookupDefItemManager.saveLookupDefItem(lookupDefItem, this.getCurrentUser());

            String key = (isNew) ? "lookupDefItem.added" : "lookupDefItem.updated";
            saveMessage(request, getText(key, locale));

          
            return new ModelAndView("redirect:lookupItemsUpload.html", "id", lookupDefItem.getLookupDef_id());
            
            
          
        }

       
    }
}
