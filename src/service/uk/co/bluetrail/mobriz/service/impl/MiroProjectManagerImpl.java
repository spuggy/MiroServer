
package uk.co.bluetrail.mobriz.service.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.orm.ObjectRetrievalFailureException;


import uk.co.bluetrail.mobriz.service.impl.BaseManager;
import uk.co.bluetrail.mobriz.model.MiroProject;
import uk.co.bluetrail.mobriz.model.SurveyElement;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.MiroProjectDao;
import uk.co.bluetrail.mobriz.dao.UserDao;
import uk.co.bluetrail.mobriz.service.MiroProjectManager;
import uk.co.bluetrail.mobriz.util.SurveyElementUtil;

public class MiroProjectManagerImpl extends BaseManager implements MiroProjectManager {
    private MiroProjectDao dao;
    private UserDao userDao;

    
    
    /**
	 * @param userDao the userDao to set
	 */
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
     * Set the Dao for communication with the data layer.
     * @param dao
     */
    public void setMiroProjectDao(MiroProjectDao dao) {
        this.dao = dao;
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MiroProjectManager#getMiroProjects(uk.co.bluetrail.mobriz.model.MiroProject, User)
     */
    public List getMiroProjects(final MiroProject miroProject, User user) {
        return dao.getMiroProjects(miroProject,user);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MiroProjectManager#getMiroProject(String id, User user)
     */
    public MiroProject getMiroProject(final String id, User user) {
    	
    	MiroProject mp = dao.getMiroProject(new Long(id));
    	
    	if(mp.getCreatedBy_id().longValue() != user.getId().longValue() & user.isSysAdmin()) {
    		new ObjectRetrievalFailureException(MiroProject.class, id);
    	}
    	
    	return mp;
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MiroProjectManager#getMiroProject(String id, User user)
     */
    public MiroProject getMiroProject(final String id) {
    	
    	MiroProject mp = dao.getMiroProject(new Long(id));  
    	
    	
    	
    	return mp;
    }
    
    
    /**
     * @see uk.co.bluetrail.mobriz.service.MiroProjectManager#saveMiroProject(MiroProject miroProject, User currentUser)
     */
    public void saveMiroProject(MiroProject miroProject, User currentUser) {
    	
    	SurveyElementUtil.timeStamp(miroProject,currentUser);
		
		miroProject.setCheckPoint(dao.getNextCheckPoint());
    	
        dao.saveMiroProject(miroProject);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MiroProjectManager#removeMiroProject(String id, User user)
     */
    public void removeMiroProject(final String id, User user) {
        
    	MiroProject mp = this.getMiroProject(id, user);
    	
    	if(mp != null) {
    		mp.setProjectStatus(MiroProject.DELETED_PROJECT);
    		List users = userDao.getProjectUsers(mp.getId());
    		Iterator itr = users.iterator() ; 
    		User user2Del = null;
    		while(itr.hasNext()) {
    			user2Del = (User) itr.next();
    			user2Del.setDeleted(true);
    			user2Del.setEnabled(false);
    			userDao.saveUser(user2Del);
    		}
    		
    		
    		
    		this.saveMiroProject(mp, user);
    	}
    	
    	
    }
}
