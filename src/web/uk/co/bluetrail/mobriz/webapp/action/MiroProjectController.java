package uk.co.bluetrail.mobriz.webapp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.MiroProject;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.model.WebPage;
import uk.co.bluetrail.mobriz.service.MiroProjectManager;
import uk.co.bluetrail.mobriz.service.UserManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import uk.co.bluetrail.mobriz.service.WebPageManager;

public class MiroProjectController extends BaseController {
    private final Log log = LogFactory.getLog(MiroProjectController.class);
    private MiroProjectManager miroProjectManager = null;
	private UserManager userManager;
    private WebPageManager webPageManager = null;

    public void setWebPageManager(WebPageManager webPageManager) {
        this.webPageManager = webPageManager;
    }

    protected int maxItems ;

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }

    /**
	 * @param userManager the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setMiroProjectManager(MiroProjectManager miroProjectManager) {
        this.miroProjectManager = miroProjectManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        String id = request.getParameter("id");

        List miroProjects = null;

        List newsPages = webPageManager.getWebPagesByTypeByDate(WebPage.NEWS,maxItems);

        Map refData = new HashMap() ;

		miroProjects = miroProjectManager.getMiroProjects(null, this.getCurrentUser());

        refData.put(Constants.MIROPROJECT_LIST, miroProjects);
        refData.put("newsPageList", newsPages);

        return new ModelAndView("miroProjectList", refData);


      }
}
