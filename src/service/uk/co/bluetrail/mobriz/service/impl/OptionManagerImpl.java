
package uk.co.bluetrail.mobriz.service.impl;

import java.util.List;

import uk.co.bluetrail.mobriz.model.Option;
import uk.co.bluetrail.mobriz.model.SurveyElement;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.OptionDAO;
import uk.co.bluetrail.mobriz.service.OptionManager;
import uk.co.bluetrail.mobriz.util.SurveyElementUtil;

public class OptionManagerImpl extends BaseManager implements OptionManager {
    private OptionDAO dao;

    
    public List getUpdated(Long ckp, boolean deleted, User newParam){
		return dao.getUpdated(ckp, deleted, null);
	}
    
    
    public List getOptions(Long sid){
    	return dao.getOptions(sid);
    }

    
    
    /**
     * Set the DAO for communication with the data layer.
     * @param dao
     */
    public void setOptionDAO(OptionDAO dao) {
        this.dao = dao;
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.OptionManager#getOptions(uk.co.bluetrail.mobriz.model.Option)
     */
    public List getOptions(final Option option) {
    	return dao.getOptions(option);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.OptionManager#getOption(String id)
     */
    public Option getOption(final String id) {
        return dao.getOption(new Long(id));
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.OptionManager#saveOption(Option option)
     */
    public void saveOption(Option option, User user) {
    	SurveyElementUtil.timeStamp((SurveyElement) option, user);    	

    	dao.saveOption(option);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.OptionManager#removeOption(String id)
     */
    public void removeOption(final String id) {
        dao.removeOption(new Long(id));
    }
}
