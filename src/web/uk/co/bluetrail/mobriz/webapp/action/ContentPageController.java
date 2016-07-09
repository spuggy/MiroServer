package uk.co.bluetrail.mobriz.webapp.action;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.WebPage;
import uk.co.bluetrail.mobriz.service.WebPageManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ContentPageController implements Controller {
    private final Log log = LogFactory.getLog(ContentPageController.class);
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
        String pageTypeStr = request.getParameter("pageType");
        String listView = request.getParameter("view");

        if(listView==null) {
           listView = "webPageList";
        }


        if (name != null) {
            WebPage webPage = webPageManager.getWebPageByName(name);

            if(webPage==null) {
                return new ModelAndView("404") ;
            }

            return new ModelAndView("webPageShow", "webPage", webPage);

        }

        if (pageTypeStr != null) {

            try {
                int pageType = Integer.parseInt(pageTypeStr);
                List webPages = webPageManager.getWebPagesByTypeByDate(pageType, 10);
                return new ModelAndView(listView, Constants.WEBPAGE_LIST, webPages);
            } catch (Exception e) {
                log.debug("exception getting web pages by type " + e.getMessage());
                return new ModelAndView("404") ;
            }


        }

        return new ModelAndView("404") ;


    }
}
