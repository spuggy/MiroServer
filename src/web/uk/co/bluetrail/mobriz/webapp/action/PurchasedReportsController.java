package uk.co.bluetrail.mobriz.webapp.action;



import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.UserManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


/**
 * Simple class to retrieve a list of users from the database.
 *
 * <p>
 * <a href="UserController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class PurchasedReportsController extends BaseController {
    private transient final Log log = LogFactory.getLog(PurchasedReportsController.class);
    private UserManager mgr = null;

    public void setUserManager(UserManager userManager) {
        this.mgr = userManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        String user_id = request.getParameter("id");
        
        
        List reps = this.mgr.getUsers(this.getCurrentUser(), User.DOWNLOAD_REPORT);
        
        return new ModelAndView("purchasedReportsList", "purchasedReports", reps);
    }
}
