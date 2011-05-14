package uk.co.bluetrail.mobriz.webapp.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.MobrizAlert;
import uk.co.bluetrail.mobriz.service.MobrizAlertManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class MobrizAlertController extends BaseController implements Controller {
    private final Log log = LogFactory.getLog(MobrizAlertController.class);
    private MobrizAlertManager mobrizAlertManager = null;

    public void setMobrizAlertManager(MobrizAlertManager mobrizAlertManager) {
        this.mobrizAlertManager = mobrizAlertManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        MobrizAlert mobrizAlert = new MobrizAlert();
        // populate object with request parameters
        BeanUtils.populate(mobrizAlert, request.getParameterMap());

        List mobrizAlerts = mobrizAlertManager.getMobrizAlerts(getCurrentUser());

        return new ModelAndView("mobrizAlertList", Constants.MOBRIZALERT_LIST, mobrizAlerts);
    }
}
