
package uk.co.bluetrail.mobriz.dao.hibernate;

import java.util.List;

import uk.co.bluetrail.mobriz.dao.hibernate.BaseDaoHibernate;
import uk.co.bluetrail.mobriz.model.LookupDef;
import uk.co.bluetrail.mobriz.model.LookupDefItem;
import uk.co.bluetrail.mobriz.dao.LookupDefDao;

import org.springframework.orm.ObjectRetrievalFailureException;

public class LookupDefDaoHibernate extends BaseDaoHibernate implements LookupDefDao {
  
    /**
     * @see uk.co.bluetrail.mobriz.dao.LookupDefDao#getLookupDefs(uk.co.bluetrail.mobriz.model.LookupDef)
     */
    public List getLookupDefs(final LookupDef lookupDef) {
        
       	return getHibernateTemplate().find("from LookupDef where deleted=false and account_id = ?",getCurrentUser().getAccount_id());

        /* Remove the line above and uncomment this code block if you want 
           to use Hibernate's Query by Example API.
        if (lookupDef == null) {
            return getHibernateTemplate().find("from LookupDef");
        } else {
            // filter on properties set in the lookupDef
            HibernateCallback callback = new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Example ex = Example.create(lookupDef).ignoreCase().enableLike(MatchMode.ANYWHERE);
                    return session.createCriteria(LookupDef.class).add(ex).list();
                }
            };
            return (List) getHibernateTemplate().execute(callback);
        }*/
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.LookupDefDao#getLookupDef(Long id)
     */
    public LookupDef getLookupDef(final Long id) {
        LookupDef lookupDef = (LookupDef) getHibernateTemplate().get(LookupDef.class, id);
        if (lookupDef == null) {
            log.warn("uh oh, lookupDef with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(LookupDef.class, id);
        }

        return lookupDef;
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.LookupDefDao#saveLookupDef(LookupDef lookupDef, User user)
     */    
    public void saveLookupDef(final LookupDef lookupDef) {
    	
    	lookupDef.setCheckPoint(getNextCheckPoint());
    	
        getHibernateTemplate().saveOrUpdate(lookupDef);
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.LookupDefDao#removeLookupDef(Long id)
     */
    public void removeLookupDef(final Long id) {
        //getHibernateTemplate().delete(getLookupDef(id));
    	//soft delete
        LookupDef lookupDef = getLookupDef(id) ; 
    	lookupDef.setDeleted(true) ;
    	saveLookupDef(lookupDef);
    }
}
