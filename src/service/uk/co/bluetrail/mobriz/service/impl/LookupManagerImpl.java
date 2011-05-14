package uk.co.bluetrail.mobriz.service.impl;

import java.util.ArrayList;
import java.util.List;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.dao.LookupDao;
import uk.co.bluetrail.mobriz.model.LabelValue;
import uk.co.bluetrail.mobriz.model.Role;
import uk.co.bluetrail.mobriz.service.LookupManager;


/**
 * Implementation of LookupManager interface to talk to the persistence layer.
 *
 * <p><a href="LookupManagerImpl.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class LookupManagerImpl extends BaseManager implements LookupManager {
    //~ Instance fields ========================================================

    private LookupDao dao;

    //~ Methods ================================================================

    public void setLookupDao(LookupDao dao) {
        super.dao = dao;
        this.dao = dao;
    }
    /**
     * @see uk.co.bluetrail.mobriz.service.LookupManager#getAllRoles()
     */
    public List getAllRoles() {
        List roles = dao.getRoles();
        List list = new ArrayList();
        Role role = null;

        for (int i = 0; i < roles.size(); i++) {
            role = (Role) roles.get(i);
            list.add(new LabelValue(role.getName(), role.getName()));
        }

        return list;
    }
    
    /**
     * @see uk.co.bluetrail.mobriz.service.LookupManager#getAllRoles()
     * 
     * all roles apart from the sysadmin role which should not be selectable
     */
    public List getAllUserRoles() {
        List roles = dao.getRoles();
        List list = new ArrayList();
        Role role = null;

        for (int i = 0; i < roles.size(); i++) {
            role = (Role) roles.get(i);
            if(!role.getName().equalsIgnoreCase(Constants.SYSADMIN_ROLE)) {
            	list.add(new LabelValue(role.getName(), role.getName()));
            }
        }

        return list;
    }
    
}
