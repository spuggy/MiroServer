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

public class NewsPageController implements Controller {
    private final Log log = LogFactory.getLog(NewsPageController.class);
    private WebPageManager webPageManager = null;
    protected int maxItems ;

    public int getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }

    public void setWebPageManager(WebPageManager webPageManager) {
        this.webPageManager = webPageManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        List webPages = webPageManager.getWebPagesByTypeByDate(WebPage.NEWS,maxItems);

        return new ModelAndView("newsPageList", "newsPageList", webPages);


    }
}
