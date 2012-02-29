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

public class MiroTeamFormController extends MiroProjectSelectorFormController {
    private MiroTeamManager miroTeamManager = null;

    public void setMiroTeamManager(MiroTeamManager miroTeamManager) {
        this.miroTeamManager = miroTeamManager;
    }
    

    
}
