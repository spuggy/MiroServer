package uk.co.bluetrail.mobriz.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import uk.co.bluetrail.mobriz.dao.Dao;
import uk.co.bluetrail.mobriz.model.User;

import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * This class serves as the Base class for all other Daos - namely to hold
 * common methods that they might all use. Can be used for standard CRUD
 * operations.</p>
 *
 * <p><a href="BaseDaoHibernate.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class BaseDaoHibernate extends HibernateDaoSupport implements Dao {

	protected final Log log = LogFactory.getLog(getClass());
	protected User currentUser ; 
	
	public Long getNextCheckPoint() {
		HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
            	// List l = session.createQuery("select concat(a.user.lastName,concat('~',a.user.firstName)), count(a) from Activity a group by concat(a.user.lastName,concat('~',a.user.firstName)) order by concat(a.user.lastName,concat('~',a.user.firstName)))").list();	             
            	 SQLQuery q = session.createSQLQuery("select nextval('mr.checkpoint') as chkp"); 
            	q.addScalar("chkp",Hibernate.DOUBLE);
               	return  q.uniqueResult();
				
	        }
        };
        
        Double newCheckPoint = (Double) getHibernateTemplate().execute(callback);
        
  
    	
        return new Long(newCheckPoint.longValue());
	} 
	
    public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	/**
     * @see uk.co.bluetrail.mobriz.dao.Dao#saveObject(java.lang.Object)
     */
    public void saveObject(Object o) {
        getHibernateTemplate().saveOrUpdate(o);
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.Dao#getObject(java.lang.Class, java.io.Serializable)
     */
    public Object getObject(Class clazz, Serializable id) {
        Object o = getHibernateTemplate().get(clazz, id);

        if (o == null) {
            throw new ObjectRetrievalFailureException(clazz, id);
        }

        return o;
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.Dao#getObjects(java.lang.Class)
     */
    public List getObjects(Class clazz) {
        return getHibernateTemplate().loadAll(clazz);
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.Dao#removeObject(java.lang.Class, java.io.Serializable)
     */
    public void removeObject(Class clazz, Serializable id) {
        getHibernateTemplate().delete(getObject(clazz, id));
    }
    
    public User getCurrentUser() {
    
    	
    	SecurityContext sc = SecurityContextHolder.getContext() ;
  	  
    	
    	try { 
    	
    		currentUser = (User)  sc.getAuthentication().getPrincipal() ;
    		return currentUser ;
    	} catch (Exception e) {
    		return null;
    	}
    	
    	
    	
    }
}
