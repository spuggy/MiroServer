package uk.co.bluetrail.mobriz.webapp.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import uk.co.bluetrail.mobriz.webapp.action.BaseFormController;
import uk.co.bluetrail.mobriz.model.Account;
import uk.co.bluetrail.mobriz.service.AccountManager;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class AccountFormController extends BaseFormController {
    private AccountManager accountManager = null;

    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }
    public AccountFormController() {
        setCommandName("account");
        setCommandClass(Account.class);
    }

    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
        String id = request.getParameter("id");
        Account account = null;

        if (!StringUtils.isEmpty(id)) {
            account = accountManager.getAccount(id);
        } else {
            account = new Account();
        }

        return account;
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        Account account = (Account) command;
        boolean isNew = (account.getId() == null);
        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            accountManager.removeAccount(account.getId().toString());

            saveMessage(request, getText("account.deleted", locale));
        } else {
            accountManager.saveAccount(account);

            String key = (isNew) ? "account.added" : "account.updated";
            saveMessage(request, getText(key, locale));

            if (!isNew) {
                return new ModelAndView("redirect:editAccount.html", "id", account.getId());
            }
        }

        return new ModelAndView(getSuccessView());
    }
}
