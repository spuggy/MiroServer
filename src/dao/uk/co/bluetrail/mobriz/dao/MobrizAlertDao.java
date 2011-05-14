
package uk.co.bluetrail.mobriz.dao;

import java.util.List;

import uk.co.bluetrail.mobriz.dao.Dao;
import uk.co.bluetrail.mobriz.model.MobrizAlert;

public interface MobrizAlertDao extends Dao {

	 public List getByNamedQuery(String queryName, Object[] params);
	
    

    /**
     * Gets mobrizAlert's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the mobrizAlert's id
     * @return mobrizAlert populated mobrizAlert object
     */
    public MobrizAlert getMobrizAlert(final Long id);

    /**
     * Saves a mobrizAlert's information
     * @param mobrizAlert the object to be saved
     */    
    public void saveMobrizAlert(MobrizAlert mobrizAlert);

    /**
     * Removes a mobrizAlert from the database by id
     * @param id the mobrizAlert's id
     */
    public void removeMobrizAlert(final Long id);
}

