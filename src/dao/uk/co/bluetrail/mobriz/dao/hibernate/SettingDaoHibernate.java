
package uk.co.bluetrail.mobriz.dao.hibernate;

import java.util.List;

import uk.co.bluetrail.mobriz.dao.hibernate.BaseDaoHibernate;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.dao.SettingDao;

import org.springframework.orm.ObjectRetrievalFailureException;

public class SettingDaoHibernate extends BaseDaoHibernate implements SettingDao {

    /**
     * @see uk.co.bluetrail.mobriz.dao.SettingDao#getSettings(uk.co.bluetrail.mobriz.model.Setting)
     */
    public List getSettings(final Setting setting) {
        
       	return getHibernateTemplate().find("from Setting where account_id = ?",getCurrentUser().getAccount_id());
    	
    	        
        /* Remove the line above and uncomment this code block if you want 
           to use Hibernate's Query by Example API.
        if (setting == null) {
            return getHibernateTemplate().find("from Setting");
        } else {
            // filter on properties set in the setting
            HibernateCallback callback = new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Example ex = Example.create(setting).ignoreCase().enableLike(MatchMode.ANYWHERE);
                    return session.createCriteria(Setting.class).add(ex).list();
                }
            };
            return (List) getHibernateTemplate().execute(callback);
        }*/
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.SettingDao#getSetting(Long id)
     */
    public Setting getSetting(final Long id) {
        Setting setting = (Setting) getHibernateTemplate().get(Setting.class, id);
        
                
        
        if (setting == null) {
            log.warn("uh oh, setting with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(Setting.class, id);
        }

        if(setting.getAccount_id().longValue() != getCurrentUser().getAccount_id().longValue()){
            log.warn("uh oh, setting with id '" + id + "' not visible..");
        	throw new ObjectRetrievalFailureException(Setting.class, id);
        } 
        
        return setting;
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.SettingDao#saveSetting(Setting setting)
     */    
    public void saveSetting(final Setting setting) {

    	getHibernateTemplate().saveOrUpdate(setting);
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.SettingDao#removeSetting(Long id)
     */
    public void removeSetting(final Long id) {
        getHibernateTemplate().delete(getSetting(id));
    }

	public Setting getSettingByName(String settingName) {
		
		Object[] params = new Object[1] ;
		params[0] = settingName ;
		
		
 		
		List settings = getHibernateTemplate().find("from Setting where settingName = ? ",params);
		

		return (Setting) settings.get(0);
		
		
	}
}
