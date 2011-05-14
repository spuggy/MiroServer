
package uk.co.bluetrail.mobriz.dao;

import java.util.List;

import uk.co.bluetrail.mobriz.dao.Dao;
import uk.co.bluetrail.mobriz.model.Setting;

public interface SettingDao extends Dao {

    /**
     * Retrieves all of the settings
     */
    public List getSettings(Setting setting);

    /**
     * Gets setting's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the setting's id
     * @return setting populated setting object
     */
    public Setting getSetting(final Long id);

    /**
     * Saves a setting's information
     * @param setting the object to be saved
     */    
    public void saveSetting(Setting setting);

    /**
     * Removes a setting from the database by id
     * @param id the setting's id
     */
    public void removeSetting(final Long id);

	public Setting getSettingByName(String settingName);
}

