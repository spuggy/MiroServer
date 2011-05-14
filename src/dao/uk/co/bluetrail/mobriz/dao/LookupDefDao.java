
package uk.co.bluetrail.mobriz.dao;

import java.util.List;

import uk.co.bluetrail.mobriz.dao.Dao;
import uk.co.bluetrail.mobriz.model.LookupDef;

public interface LookupDefDao extends Dao {

    /**
     * Retrieves all of the lookupDefs
     */
    public List getLookupDefs(LookupDef lookupDef);

    /**
     * Gets lookupDef's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the lookupDef's id
     * @return lookupDef populated lookupDef object
     */
    public LookupDef getLookupDef(final Long id);

    /**
     * Saves a lookupDef's information
     * @param lookupDef the object to be saved
     */    
    public void saveLookupDef(LookupDef lookupDef);

    /**
     * Removes a lookupDef from the database by id
     * @param id the lookupDef's id
     */
    public void removeLookupDef(final Long id);
}

