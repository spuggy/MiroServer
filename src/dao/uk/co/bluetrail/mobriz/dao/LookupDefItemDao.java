
package uk.co.bluetrail.mobriz.dao;

import java.util.List;

import uk.co.bluetrail.mobriz.dao.Dao;
import uk.co.bluetrail.mobriz.model.LookupDefItem;

public interface LookupDefItemDao extends Dao {

	/**
     * Retrieves all of the lookupDefItems that match an example lookupDefItem
	 * @param limit
     */
	public List getLookupDefItemsQBE(final LookupDefItem lookupDefItem, int limit);
	
    /**
     * Retrieves all of the lookupDefItems
     */
    public List getLookupDefItems(LookupDefItem lookupDefItem);

    /**
     * Gets lookupDefItem's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the lookupDefItem's id
     * @return lookupDefItem populated lookupDefItem object
     */
    public LookupDefItem getLookupDefItem(final Long id);

    /**
     * Saves a lookupDefItem's information
     * @param lookupDefItem the object to be saved
     */    
    public void saveLookupDefItem(LookupDefItem lookupDefItem);

    /**
     * Removes a lookupDefItem from the database by id
     * @param id the lookupDefItem's id
     */
    public void removeLookupDefItem(final Long id);
}

