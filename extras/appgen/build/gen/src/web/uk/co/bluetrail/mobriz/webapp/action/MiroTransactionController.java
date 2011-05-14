package uk.co.bluetrail.mobriz.webapp.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.MiroTransaction;
import uk.co.bluetrail.mobriz.service.MiroTransactionManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class MiroTransactionController implements Controller {
    private final Log log = LogFactory.getLog(MiroTransactionController.class);
    private MiroTransactionManager miroTransactionManager = null;

    public void setMiroTransactionManager(MiroTransactionManager miroTransactionManager) {
        this.miroTransactionManager = miroTransactionManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        MiroTransaction miroTransaction = new MiroTransaction();
        // populate object with request parameters
        BeanUtils.populate(miroTransaction, request.getParameterMap());

        List miroTransactions = miroTransactionManager.getMiroTransactions(miroTransaction);

        return new ModelAndView("miroTransactionList", Constants.MIROTRANSACTION_LIST, miroTransactions);
    }
}
