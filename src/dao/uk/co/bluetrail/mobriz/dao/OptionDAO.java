
package uk.co.bluetrail.mobriz.dao;

import java.util.List;

import uk.co.bluetrail.mobriz.model.Option;
import uk.co.bluetrail.mobriz.model.User;

public interface OptionDAO extends Dao {

	public List getUpdated(Long ckp, boolean deleted, User newParam);	
	/**
     * Retrieves all of the for a sid
     */
	public List getOptions(Long sid);

	public List getOptionsForQuestion(Long qid);
	
    /**
     * Retrieves all of the options
     */
    public List getOptions(Option option);

    /**
     * Gets option's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the option's id
     * @return option populated option object
     */
    public Option getOption(final Long id);

    /**
     * Saves a option's information
     * @param option the object to be saved
     */	
    public void saveOption(Option option);

	/**
     * Removes a option from the database by id
     * @param id the option's id
     */
    public void removeOption(final Long id);
    
    public void removeBranchesToQuestion(Long id);
	/**
	 * @param oldFullName
	 * @return
	 */
	public List getOptionsWithOText(String oldFullName);
    
}

