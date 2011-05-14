
package uk.co.bluetrail.mobriz.dao.hibernate;

import java.util.List;

import uk.co.bluetrail.mobriz.dao.hibernate.BaseDaoHibernate;
import uk.co.bluetrail.mobriz.model.MobrizAlert;
import uk.co.bluetrail.mobriz.dao.MobrizAlertDao;

import org.springframework.orm.ObjectRetrievalFailureException;

public class MobrizAlertDaoHibernate extends BaseDaoHibernate implements MobrizAlertDao {

	
	public List getByNamedQuery(String queryName, Object[] params){
		return getHibernateTemplate().findByNamedQuery(queryName, params);
	}
	
    
    /**
     * @see uk.co.bluetrail.mobriz.dao.MobrizAlertDao#getMobrizAlert(Long id)
     */
    public MobrizAlert getMobrizAlert(final Long id) {
        MobrizAlert mobrizAlert = (MobrizAlert) getHibernateTemplate().get(MobrizAlert.class, id);
        if (mobrizAlert == null) {
            log.warn("uh oh, mobrizAlert with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(MobrizAlert.class, id);
        }

        return mobrizAlert;
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.MobrizAlertDao#saveMobrizAlert(MobrizAlert mobrizAlert)
     */    
    public void saveMobrizAlert(final MobrizAlert mobrizAlert) {
        getHibernateTemplate().saveOrUpdate(mobrizAlert);
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.MobrizAlertDao#removeMobrizAlert(Long id)
     */
    public void removeMobrizAlert(final Long id) {
        getHibernateTemplate().delete(getMobrizAlert(id));
    }
}
