
package uk.co.bluetrail.mobriz.service.impl;

import java.util.List;

import uk.co.bluetrail.mobriz.service.impl.BaseManager;
import uk.co.bluetrail.mobriz.model.LookupDef;
import uk.co.bluetrail.mobriz.model.SurveyElement;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.LookupDefDao;
import uk.co.bluetrail.mobriz.service.LookupDefManager;
import uk.co.bluetrail.mobriz.util.SurveyElementUtil;

public class LookupDefManagerImpl extends BaseManager implements LookupDefManager {
    private LookupDefDao dao;

    /**
     * Set the Dao for communication with the data layer.
     * @param dao
     */
    public void setLookupDefDao(LookupDefDao dao) {
        this.dao = dao;
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.LookupDefManager#getLookupDefs(uk.co.bluetrail.mobriz.model.LookupDef)
     */
    public List getLookupDefs(final LookupDef lookupDef) {
        return dao.getLookupDefs(lookupDef);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.LookupDefManager#getLookupDef(String id)
     */
    public LookupDef getLookupDef(final String id) {
        return dao.getLookupDef(new Long(id));
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.LookupDefManager#saveLookupDef(LookupDef lookupDef, User user)
     */
    public void saveLookupDef(LookupDef lookupDef, User user) {
      
    	SurveyElementUtil.timeStamp((SurveyElement) lookupDef, user);    	
    	
    	if(lookupDef.getAccount_id() ==null) {
    		lookupDef.setAccount_id(user.getAccount_id());
    		
    	}
    	
    	dao.saveLookupDef(lookupDef);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.LookupDefManager#removeLookupDef(String id)
     */
    public void removeLookupDef(final String id) {
        dao.removeLookupDef(new Long(id));
    }
}
