package uk.co.bluetrail.mobriz.webapp.action;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import uk.co.bluetrail.mobriz.model.MiroProject;
import uk.co.bluetrail.mobriz.service.MiroProjectManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MiroProjectFormController extends BaseFormController {
    private MiroProjectManager miroProjectManager = null;

    protected Map referenceData(HttpServletRequest request)  throws Exception {

        Map refData = new HashMap() ;

        refData.put("currentUser",this.getCurrentUser());

        return refData;
    }

    public void setMiroProjectManager(MiroProjectManager miroProjectManager) {
        this.miroProjectManager = miroProjectManager;
    }



    public MiroProjectFormController() {
        setCommandName("miroProject");
        setCommandClass(MiroProject.class);
    }

    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {
        String id = request.getParameter("id");
        MiroProject miroProject = null;

        if (!StringUtils.isEmpty(id)) {
            miroProject = miroProjectManager.getMiroProject(id, getCurrentUser());
        } else {
            miroProject = new MiroProject();
            miroProject.setBccPractitioner(true);
        }

        return miroProject;
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
            throws Exception {


        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        MiroProject miroProject = (MiroProject) command;
        boolean isNew = (miroProject.getId() == null);
        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            miroProjectManager.removeMiroProject(miroProject.getId().toString(), getCurrentUser());

            saveMessage(request, getText("miroProject.deleted", locale));
            return new ModelAndView("redirect:miroProjects.html");
        } else {
            miroProjectManager.saveMiroProject(miroProject, getCurrentUser());

            String key = (isNew) ? "miroProject.added" : "miroProject.updated";
            saveMessage(request, getText(key, locale));

        }

        return new ModelAndView("redirect:showProject.html", "id", miroProject.getId());


    }
}
