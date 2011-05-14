
package uk.co.bluetrail.mobriz.service;

import java.util.List;

import uk.co.bluetrail.mobriz.service.Manager;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.dao.SettingDao;

public interface SettingManager extends Manager {
    /**
     * Retrieves all of the settings
     */
    public List getSettings(Setting setting);

    /**
     * Gets setting's information based on id.
     * @param id the setting's id
     * @param user TODO
     * @return setting populated setting object
     */
    public Setting getSetting(final String id);

    /**
     * Saves a setting's information
     * @param setting the object to be saved
     */
    public void saveSetting(Setting setting);

    /**
     * Removes a setting from the database by id
     * @param id the setting's id
     */
    public void removeSetting(final String id);
    
    
    public Setting getSettingByName(String settingName);
}

