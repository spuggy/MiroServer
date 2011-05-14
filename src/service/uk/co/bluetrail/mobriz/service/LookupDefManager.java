
package uk.co.bluetrail.mobriz.service;

import java.util.List;

import uk.co.bluetrail.mobriz.service.Manager;
import uk.co.bluetrail.mobriz.model.LookupDef;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.LookupDefDao;

public interface LookupDefManager extends Manager {
    /**
     * Retrieves all of the lookupDefs
     */
    public List getLookupDefs(LookupDef lookupDef);

    /**
     * Gets lookupDef's information based on id.
     * @param id the lookupDef's id
     * @return lookupDef populated lookupDef object
     */
    public LookupDef getLookupDef(final String id);

    /**
     * Saves a lookupDef's information
     * @param lookupDef the object to be saved
     * @param user TODO
     */
    public void saveLookupDef(LookupDef lookupDef, User user);

    /**
     * Removes a lookupDef from the database by id
     * @param id the lookupDef's id
     */
    public void removeLookupDef(final String id);
}

