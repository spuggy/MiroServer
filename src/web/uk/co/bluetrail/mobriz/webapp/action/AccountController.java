package uk.co.bluetrail.mobriz.webapp.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Account;
import uk.co.bluetrail.mobriz.service.AccountManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class AccountController implements Controller {
    private final Log log = LogFactory.getLog(AccountController.class);
    private AccountManager accountManager = null;

    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        Account account = new Account();
        // populate object with request parameters
        BeanUtils.populate(account, request.getParameterMap());

        List accounts = accountManager.getAccounts(account);

        return new ModelAndView("accountList", Constants.ACCOUNT_LIST, accounts);
    }
}
