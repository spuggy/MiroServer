
package uk.co.bluetrail.mobriz.service.impl;

import java.util.List;

import uk.co.bluetrail.mobriz.service.impl.BaseManager;
import uk.co.bluetrail.mobriz.model.LookupDefItem;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.LookupDefItemDao;
import uk.co.bluetrail.mobriz.service.LookupDefItemManager;

public class LookupDefItemManagerImpl extends BaseManager implements LookupDefItemManager {
    private LookupDefItemDao dao;

    
    
    /**
     * Set the Dao for communication with the data layer.
     * @param dao
     */
    public void setLookupDefItemDao(LookupDefItemDao dao) {
        this.dao = dao;
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.LookupDefItemManager#getLookupDefItems(uk.co.bluetrail.mobriz.model.LookupDefItem)
     */
    public List getLookupDefItems(final LookupDefItem lookupDefItem) {
        return dao.getLookupDefItems(lookupDefItem);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.LookupDefItemManager#getLookupDefItem(String id)
     */
    public LookupDefItem getLookupDefItem(final String id) {
        return dao.getLookupDefItem(new Long(id));
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.LookupDefItemManager#saveLookupDefItem(LookupDefItem lookupDefItem, User user)
     */
    public void saveLookupDefItem(LookupDefItem lookupDefItem, User user) {
    	
    	if(lookupDefItem.getAccount_id() ==null) {  
    		lookupDefItem.setAccount_id(user.getAccount_id());
    		
    	}
    	  
        dao.saveLookupDefItem(lookupDefItem);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.LookupDefItemManager#removeLookupDefItem(String id)
     */
    public void removeLookupDefItem(final String id) {
        dao.removeLookupDefItem(new Long(id));
    }

	public List getLookupDefItemsQBE(LookupDefItem lookupDefItem) {
	    return dao.getLookupDefItemsQBE(lookupDefItem, 0);
	}
	
	public List getLookupDefItemsQBE(LookupDefItem lookupDefItem,int limit) {
	    return dao.getLookupDefItemsQBE(lookupDefItem, limit);
	}
}
