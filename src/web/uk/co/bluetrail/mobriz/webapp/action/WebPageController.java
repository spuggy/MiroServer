package uk.co.bluetrail.mobriz.webapp.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.WebPage;
import uk.co.bluetrail.mobriz.service.WebPageManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class WebPageController implements Controller {
    private final Log log = LogFactory.getLog(WebPageController.class);
    private WebPageManager webPageManager = null;

    public void setWebPageManager(WebPageManager webPageManager) {
        this.webPageManager = webPageManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }
        
        String name = request.getParameter("name");
        
        if(name==null) {

        WebPage webPage = new WebPage();
        // populate object with request parameters
        BeanUtils.populate(webPage, request.getParameterMap());

        List webPages = webPageManager.getWebPages(webPage);

        return new ModelAndView("webPageList", Constants.WEBPAGE_LIST, webPages);
        } else {
        	
        	WebPage webPage = webPageManager.getWebPageByName(name) ;
        	
        	return new ModelAndView("webPageShow", "webPage", webPage);
        	
        	
        }
        
        
        
    }
}
