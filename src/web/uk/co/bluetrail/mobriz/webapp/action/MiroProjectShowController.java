package uk.co.bluetrail.mobriz.webapp.action;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import uk.co.bluetrail.mobriz.model.MiroProject;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.MiroProjectManager;
import uk.co.bluetrail.mobriz.service.UserManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class MiroProjectShowController extends BaseController {
    private final Log log = LogFactory.getLog(MiroProjectShowController.class);
    private MiroProjectManager miroProjectManager = null;
	private UserManager userManager;


    //show.html?id=12313&version=v10

	
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

        MiroProject miroProject = null;

        if (!StringUtils.isEmpty(id)) {
            miroProject = miroProjectManager.getMiroProject(id, getCurrentUser());
        } else {
        	//TODO redirect somewhere sensible
        }
        
        User pracUser = userManager.getUser(miroProject.getCreatedBy_id().toString());

        HashMap model = new HashMap() ;
        model.put("miroProject",miroProject);
        model.put("pracUser",pracUser);
        
        
        return new ModelAndView("miroProjectShow", model);
    }
}
