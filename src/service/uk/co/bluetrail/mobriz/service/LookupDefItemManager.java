
package uk.co.bluetrail.mobriz.service;

import java.util.List;

import uk.co.bluetrail.mobriz.service.Manager;
import uk.co.bluetrail.mobriz.model.LookupDef;
import uk.co.bluetrail.mobriz.model.LookupDefItem;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.LookupDefItemDao;

public interface LookupDefItemManager extends Manager {
    
	/**
     * Retrieves all of the lookupDefItems that match an example
     */	
	public List getLookupDefItemsQBE(final LookupDefItem lookupDefItem,int limit);
	
	/**
     * Retrieves all of the lookupDefItems
     */
    public List getLookupDefItems(LookupDefItem lookupDefItem);

    /**
     * Gets lookupDefItem's information based on id.
     * @param id the lookupDefItem's id
     * @return lookupDefItem populated lookupDefItem object
     */
    public LookupDefItem getLookupDefItem(final String id);

    /**
     * Saves a lookupDefItem's information
     * @param lookupDefItem the object to be saved
     * @param user TODO
     */
    public void saveLookupDefItem(LookupDefItem lookupDefItem, User user);

    /**
     * Removes a lookupDefItem from the database by id
     * @param id the lookupDefItem's id
     */
    public void removeLookupDefItem(final String id);

	/**
	 * Retrieves all of the lookupDefItems that match an example
	 */	
	public List getLookupDefItemsQBE(final LookupDefItem lookupDefItem);

	
}

