package uk.co.bluetrail.mobriz.webapp.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import uk.co.bluetrail.mobriz.webapp.action.BaseFormController;
import uk.co.bluetrail.mobriz.model.MiroTransaction;
import uk.co.bluetrail.mobriz.service.Manager;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class MiroTransactionFormController extends BaseFormController {
    private Manager manager = null;

    public void setManager(Manager manager) {
        this.manager = manager;
    }
    public MiroTransactionFormController() {
        setCommandName("miroTransaction");
        setCommandClass(MiroTransaction.class);
    }

    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
        String id = request.getParameter("id");
        MiroTransaction miroTransaction = null;

        if (!StringUtils.isEmpty(id)) {
            miroTransaction = (MiroTransaction) manager.getObject(MiroTransaction.class, new Long(id));
        } else {
            miroTransaction = new MiroTransaction();
        }

        return miroTransaction;
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        MiroTransaction miroTransaction = (MiroTransaction) command;
        boolean isNew = (miroTransaction.getId() == null);
        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            manager.removeObject(MiroTransaction.class, miroTransaction.getId());

            saveMessage(request, getText("miroTransaction.deleted", locale));
        } else {
            manager.saveObject(miroTransaction);

            String key = (isNew) ? "miroTransaction.added" : "miroTransaction.updated";
            saveMessage(request, getText(key, locale));

            if (!isNew) {
                return new ModelAndView("redirect:editMiroTransaction.html", "id", miroTransaction.getId());
            }
        }

        return new ModelAndView(getSuccessView());
    }
}
