package uk.co.bluetrail.mobriz.webapp.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import uk.co.bluetrail.mobriz.webapp.action.BaseFormController;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.service.SettingManager;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class SettingFormController extends BaseFormController {
    private SettingManager settingManager = null;

    public void setSettingManager(SettingManager settingManager) {
        this.settingManager = settingManager;
    }
    public SettingFormController() {
        setCommandName("setting");
        setCommandClass(Setting.class);
    }

    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
        String id = request.getParameter("id");
        Setting setting = null;

        if (!StringUtils.isEmpty(id)) {
            setting = settingManager.getSetting(id);
        } else {
            setting = new Setting();
        }

        return setting;
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        Setting setting = (Setting) command;
        boolean isNew = (setting.getId() == null);
        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            settingManager.removeSetting(setting.getId().toString());

            saveMessage(request, getText("setting.deleted", locale));
        } else {
            settingManager.saveSetting(setting);

            String key = (isNew) ? "setting.added" : "setting.updated";
            saveMessage(request, getText(key, locale));

            if (!isNew) {
                return new ModelAndView("redirect:editSetting.html", "id", setting.getId());
            }
        }

        return new ModelAndView(getSuccessView());
    }
}
