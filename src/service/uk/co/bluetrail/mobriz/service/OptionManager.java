
package uk.co.bluetrail.mobriz.service;

import java.util.List;

import uk.co.bluetrail.mobriz.model.Option;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.OptionDAO;

public interface OptionManager extends Manager {
	
	public List getUpdated(Long ckp, boolean deleted, User newParam);	
	
	public List getOptions(Long sid);
	
	
    /**
     * Setter for DAO, convenient for unit testing
     */
    public void setOptionDAO(OptionDAO optionDAO);

    /**
     * Retrieves all of the options
     */
    public List getOptions(Option option);

    /**
     * Gets option's information based on id.
     * @param id the option's id
     * @return option populated option object
     */
    public Option getOption(final String id);

    /**
     * Saves a option's information
     * @param option the object to be saved
     */
    public void saveOption(Option option, User user);

    /**
     * Removes a option from the database by id
     * @param id the option's id
     */
    public void removeOption(final String id);
}

