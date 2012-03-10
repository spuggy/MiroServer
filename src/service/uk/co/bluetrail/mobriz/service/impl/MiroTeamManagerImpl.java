
package uk.co.bluetrail.mobriz.service.impl;

import java.util.List;

import uk.co.bluetrail.mobriz.service.impl.BaseManager;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.SurveyElement;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.MiroTeamDao;
import uk.co.bluetrail.mobriz.service.MiroTeamManager;
import uk.co.bluetrail.mobriz.util.SurveyElementUtil;

public class MiroTeamManagerImpl extends BaseManager implements MiroTeamManager {
   
	private MiroTeamDao dao;

    
    /**
     * Set the Dao for communication with the data layer.
     * @param dao
     */
    public void setMiroTeamDao(MiroTeamDao dao) {
        this.dao = dao;
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MiroTeamManager#getMiroTeams(uk.co.bluetrail.mobriz.model.MiroTeam)
     */
    public List getMiroTeams(final MiroTeam miroTeam, User user) {
        return dao.getMiroTeams(miroTeam, user);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MiroTeamManager#getMiroTeam(String id)
     */
    public MiroTeam getMiroTeam(final String id) {
        return dao.getMiroTeam(new Long(id));
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MiroTeamManager#saveMiroTeam(MiroTeam miroTeam, User user)
     */
    public void saveMiroTeam(MiroTeam miroTeam, User user) {
    	
    	SurveyElementUtil.timeStamp((SurveyElement) miroTeam, user);

    	miroTeam.setCheckPoint(new Long(0));
    	  
        dao.saveMiroTeam(miroTeam);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MiroTeamManager#removeMiroTeam(String id)
     */
    public void removeMiroTeam(final String id) {
        dao.removeMiroTeam(new Long(id));
    }

	

	
}
