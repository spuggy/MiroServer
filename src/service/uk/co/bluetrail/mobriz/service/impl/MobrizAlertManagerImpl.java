
package uk.co.bluetrail.mobriz.service.impl;

import java.util.List;

import uk.co.bluetrail.mobriz.service.impl.BaseManager;
import uk.co.bluetrail.mobriz.model.MobrizAlert;
import uk.co.bluetrail.mobriz.model.SurveyElement;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.MobrizAlertDao;
import uk.co.bluetrail.mobriz.service.MobrizAlertManager;
import uk.co.bluetrail.mobriz.util.SurveyElementUtil;

public class MobrizAlertManagerImpl extends BaseManager implements MobrizAlertManager {
    private MobrizAlertDao dao;

    /**
     * Set the Dao for communication with the data layer.
     * @param dao
     */
    public void setMobrizAlertDao(MobrizAlertDao dao) {
        this.dao = dao;
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MobrizAlertManager#getMobrizAlerts(User)
     */
    public List getMobrizAlerts(User currentUser) {
        
    	Object[] params = new Object[1];
    	params[0] = currentUser.getAccount_id();
    	return dao.getByNamedQuery("mobrizAlert.getAllMobrizAlerts", params);
 
    	
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MobrizAlertManager#getMobrizAlert(String id)
     */
    public MobrizAlert getMobrizAlert(final String id) {
        return dao.getMobrizAlert(new Long(id));
    }

    

    /**
     * @see uk.co.bluetrail.mobriz.service.MobrizAlertManager#removeMobrizAlert(String id, User currentUser)
     */
    public void removeMobrizAlert(final String id, User currentUser) {
        MobrizAlert mobrizAlert = dao.getMobrizAlert(new Long(id));
        mobrizAlert.setDeleted(true);
    	this.saveMobrizAlert(mobrizAlert,currentUser);
    }

	public void saveMobrizAlert(MobrizAlert mobrizAlert, User currentUser) {
		
		
		if (mobrizAlert.getAccount_id() == null) {
			mobrizAlert.setAccount_id(currentUser.getAccount_id());

		}
		SurveyElementUtil.timeStamp(mobrizAlert,currentUser);
		
		mobrizAlert.setCheckPoint(dao.getNextCheckPoint());
		
		dao.saveMobrizAlert(mobrizAlert);
		
	}
}
