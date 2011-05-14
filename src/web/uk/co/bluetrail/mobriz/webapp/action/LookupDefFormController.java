package uk.co.bluetrail.mobriz.webapp.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import uk.co.bluetrail.mobriz.webapp.action.BaseFormController;
import uk.co.bluetrail.mobriz.model.LookupDef;
import uk.co.bluetrail.mobriz.service.LookupDefManager;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class LookupDefFormController extends BaseFormController {
    private LookupDefManager lookupDefManager = null;

    public void setLookupDefManager(LookupDefManager lookupDefManager) {
        this.lookupDefManager = lookupDefManager;
    }
    public LookupDefFormController() {
        setCommandName("lookupDef");
        setCommandClass(LookupDef.class);
    }

    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
        String id = request.getParameter("id");
        LookupDef lookupDef = null;

        if (!StringUtils.isEmpty(id)) {
            lookupDef = lookupDefManager.getLookupDef(id);
        } else {
            lookupDef = new LookupDef();            
            lookupDef.setLookupPrompt(getText("lookupDef.defPromptTxt", request.getLocale()));
        }

        return lookupDef;
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        LookupDef lookupDef = (LookupDef) command;
        boolean isNew = (lookupDef.getId() == null);
        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            lookupDefManager.removeLookupDef(lookupDef.getId().toString());

            saveMessage(request, getText("lookupDef.deleted", locale));
        } else {
            
        	if(lookupDef.getLookupFields().size()==0 ||lookupDef.getLookupFields().size()>3 ) {
        		errors.reject("lookupDef.minmaxFieldsError");
        		return showForm(request, response, errors);
        	}
        	
        	
        	lookupDefManager.saveLookupDef(lookupDef, this.getCurrentUser());

            
            String key = (isNew) ? "lookupDef.added" : "lookupDef.updated";
            saveMessage(request, getText(key, locale));

           
            return new ModelAndView("redirect:lookupItemsUpload.html", "id", lookupDef.getId());
            
        }

        return new ModelAndView(getSuccessView());
    }
}
