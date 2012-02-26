package uk.co.bluetrail.mobriz.webapp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.LabelValue;
import uk.co.bluetrail.mobriz.model.MiroProject;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.service.MiroProjectManager;
import uk.co.bluetrail.mobriz.service.MiroResponseManager;
import uk.co.bluetrail.mobriz.service.MiroTeamManager;
import uk.co.bluetrail.mobriz.service.UserManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class MiroTeamController extends BaseController {
    private final Log log = LogFactory.getLog(MiroTeamController.class);
    private MiroTeamManager miroTeamManager = null;
    private MiroProjectManager miroProjectManager = null;
	private MiroResponseManager miroResponseManager = null;
	private UserManager userManager = null;
 
    
    

    public void setMiroProjectManager(MiroProjectManager miroProjectManager) {
		this.miroProjectManager = miroProjectManager;
	}

	public void setMiroResponseManager(MiroResponseManager miroResponseManager) {
		this.miroResponseManager = miroResponseManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setMiroTeamManager(MiroTeamManager miroTeamManager) {
        this.miroTeamManager = miroTeamManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        MiroTeam miroTeam = new MiroTeam();
        // populate object with request parameters
        BeanUtils.populate(miroTeam, request.getParameterMap());

        //get the project list for the selector
      	List miroProjects = null;
    	miroProjects = miroProjectManager.getMiroProjects(null, getCurrentUser());
        Iterator itr = miroProjects.iterator();
        List miroProjectLabels = new ArrayList();
        MiroProject mp = null ; 
        while(itr.hasNext()){
        	mp = (MiroProject) itr.next();
        	miroProjectLabels.add(new LabelValue(mp.getProjectTitle(),mp.getId().toString()));
        }
    	
        Map refData = new HashMap() ;
    
        refData.put("myProjects", miroProjectLabels);
        
  
        /// get the list o teams for this user
        List miroTeams = miroTeamManager.getMiroTeams(miroTeam, this.getCurrentUser());
        refData.put(Constants.MIROTEAM_LIST, miroTeams);
        
        return new ModelAndView("miroTeamList", refData);
    }
}
