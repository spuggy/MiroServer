package uk.co.bluetrail.mobriz.webapp.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import uk.co.bluetrail.mobriz.webapp.action.BaseFormController;
import uk.co.bluetrail.mobriz.model.MobrizAlert;
import uk.co.bluetrail.mobriz.service.MobrizAlertManager;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class MobrizAlertFormController extends BaseFormController {
    private MobrizAlertManager mobrizAlertManager = null;

    public void setMobrizAlertManager(MobrizAlertManager mobrizAlertManager) {
        this.mobrizAlertManager = mobrizAlertManager;
    }
    public MobrizAlertFormController() {
        setCommandName("mobrizAlert");
        setCommandClass(MobrizAlert.class);
    }

    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
        String id = request.getParameter("id");
        MobrizAlert mobrizAlert = null;

        if (!StringUtils.isEmpty(id)) {
            mobrizAlert = mobrizAlertManager.getMobrizAlert(id);
        } else {
            mobrizAlert = new MobrizAlert();
        }

        return mobrizAlert;
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        MobrizAlert mobrizAlert = (MobrizAlert) command;
        boolean isNew = (mobrizAlert.getId() == null);
        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            mobrizAlertManager.removeMobrizAlert(mobrizAlert.getId().toString(), getCurrentUser());

            saveMessage(request, getText("mobrizAlert.deleted", locale));
        } else {
            mobrizAlertManager.saveMobrizAlert(mobrizAlert, getCurrentUser());

            String key = (isNew) ? "mobrizAlert.added" : "mobrizAlert.updated";
            saveMessage(request, getText(key, locale));

            if (!isNew) {
                return new ModelAndView("redirect:editMobrizAlert.html", "id", mobrizAlert.getId());
            }
        }

        return new ModelAndView(getSuccessView());
    }
}
