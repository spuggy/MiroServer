package uk.co.bluetrail.mobriz.webapp.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.LookupDef;
import uk.co.bluetrail.mobriz.service.LookupDefManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class LookupDefController implements Controller {
    private final Log log = LogFactory.getLog(LookupDefController.class);
    private LookupDefManager lookupDefManager = null;

    public void setLookupDefManager(LookupDefManager lookupDefManager) {
        this.lookupDefManager = lookupDefManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

      
        
        
        LookupDef lookupDef = new LookupDef();
        // populate object with request parameters
        BeanUtils.populate(lookupDef, request.getParameterMap());

        List lookupDefs = lookupDefManager.getLookupDefs(lookupDef);

        
        
        return new ModelAndView("lookupDefList", Constants.LOOKUPDEF_LIST, lookupDefs);
    }
}
