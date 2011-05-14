package uk.co.bluetrail.mobriz.webapp.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.service.SettingManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class SettingController implements Controller {
    private final Log log = LogFactory.getLog(SettingController.class);
    private SettingManager settingManager = null;

    public void setSettingManager(SettingManager settingManager) {
        this.settingManager = settingManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        Setting setting = new Setting();
        // populate object with request parameters
        BeanUtils.populate(setting, request.getParameterMap());

        List settings = settingManager.getSettings(setting);

        return new ModelAndView("settingList", Constants.SETTING_LIST, settings);
    }
}
