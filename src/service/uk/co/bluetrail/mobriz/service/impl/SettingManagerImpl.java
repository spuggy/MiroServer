
package uk.co.bluetrail.mobriz.service.impl;

import java.util.List;

import uk.co.bluetrail.mobriz.service.impl.BaseManager;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.dao.SettingDao;
import uk.co.bluetrail.mobriz.service.SettingManager;

public class SettingManagerImpl extends BaseManager implements SettingManager {
    private SettingDao dao;

    /**
     * Set the Dao for communication with the data layer.
     * @param dao
     */
    public void setSettingDao(SettingDao dao) {
        this.dao = dao;
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.SettingManager#getSettings(uk.co.bluetrail.mobriz.model.Setting)
     */
    public List getSettings(final Setting setting) {
        return dao.getSettings(setting);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.SettingManager#getSetting(String id)
     */
    public Setting getSetting(final String id) {
        return dao.getSetting(new Long(id));
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.SettingManager#saveSetting(Setting setting)
     */
    public void saveSetting(Setting setting) {
        dao.saveSetting(setting);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.SettingManager#removeSetting(String id)
     */
    public void removeSetting(final String id) {
    	dao.removeSetting(new Long(id));
    }

	public  Setting getSettingByName(String settingName) {
		return dao.getSettingByName(settingName);
		
	}
}
