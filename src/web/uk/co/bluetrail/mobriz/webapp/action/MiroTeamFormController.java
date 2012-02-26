package uk.co.bluetrail.mobriz.webapp.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import uk.co.bluetrail.mobriz.webapp.action.BaseFormController;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.service.MiroTeamManager;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class MiroTeamFormController extends BaseFormController {
    private MiroTeamManager miroTeamManager = null;

    public void setMiroTeamManager(MiroTeamManager miroTeamManager) {
        this.miroTeamManager = miroTeamManager;
    }
    public MiroTeamFormController() {
        setCommandName("miroTeam");
        setCommandClass(MiroTeam.class);
    }

    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
        String id = request.getParameter("id");
        MiroTeam miroTeam = null;

        if (!StringUtils.isEmpty(id)) {
            miroTeam = miroTeamManager.getMiroTeam(id);
        } else {
            miroTeam = new MiroTeam();
        }

        return miroTeam;
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        MiroTeam miroTeam = (MiroTeam) command;
        boolean isNew = (miroTeam.getId() == null);
        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            miroTeamManager.removeMiroTeam(miroTeam.getId().toString());

            saveMessage(request, getText("miroTeam.deleted", locale));
        } else {
            miroTeamManager.saveMiroTeam(miroTeam,this.getCurrentUser());

            String key = (isNew) ? "miroTeam.added" : "miroTeam.updated";
            saveMessage(request, getText(key, locale));

            if (!isNew) {
                return new ModelAndView("redirect:editMiroTeam.html", "id", miroTeam.getId());
            }
        }

        return new ModelAndView(getSuccessView());
    }
}
