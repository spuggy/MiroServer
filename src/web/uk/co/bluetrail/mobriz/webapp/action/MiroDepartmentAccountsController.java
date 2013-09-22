package uk.co.bluetrail.mobriz.webapp.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import uk.co.bluetrail.mobriz.service.MiroDepartmentAccountsReportManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class MiroDepartmentAccountsController implements Controller {
    private final Log log = LogFactory.getLog(MiroDepartmentAccountsController.class);
    private MiroDepartmentAccountsReportManager miroDepartmentAccountsReportManager = null;

    public void setMiroDepartmentAccountsManager(MiroDepartmentAccountsReportManager MiroDepartmentAccountsReportManager) {
        this.miroDepartmentAccountsReportManager = MiroDepartmentAccountsReportManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }



        List reportLines = miroDepartmentAccountsReportManager.getMonthDetails(3,2013) ;

        return new ModelAndView("dd", "reportLines",reportLines);
    }
}
