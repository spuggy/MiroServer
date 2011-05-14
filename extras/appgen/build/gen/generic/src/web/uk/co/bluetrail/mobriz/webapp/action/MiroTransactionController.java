package uk.co.bluetrail.mobriz.webapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.MiroTransaction;
import uk.co.bluetrail.mobriz.service.Manager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class MiroTransactionController implements Controller {
    private final Log log = LogFactory.getLog(MiroTransactionController.class);
    private Manager manager = null;

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        return new ModelAndView("miroTransactionList", Constants.MIROTRANSACTION_LIST,
                                manager.getObjects(MiroTransaction.class));
    }
}
