
package uk.co.bluetrail.mobriz.dao.hibernate;

import java.sql.SQLException;
import java.util.List;


import uk.co.bluetrail.mobriz.dao.hibernate.BaseDaoHibernate;
import uk.co.bluetrail.mobriz.model.LookupDefItem;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.dao.LookupDefItemDao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;

public class LookupDefItemDaoHibernate extends BaseDaoHibernate implements LookupDefItemDao {

	/**
	 * @see uk.co.bluetrail.mccms.dao.AccountDAO#getAccountsQBE(uk.co.bluetrail.mccms.model.Account)
	 */
	public List getLookupDefItemsQBE(final LookupDefItem lookupDefItem, final int limit) {
	
	   	 
		
		
	     // filter on properties set in the account
	     HibernateCallback callback = new HibernateCallback() {
	     	public Object doInHibernate(Session session) throws HibernateException, SQLException {
	         	 
	             Criteria crit = session.createCriteria(LookupDefItem.class);
	             if(limit>0) {
	            	 crit.setMaxResults(limit);
	             }
	          
	             
	             
	             crit.add(Expression.eq("lookupDef_id", lookupDefItem.getLookupDef_id()));	            		 
	             
	             if(lookupDefItem.getLookup1() != null && !lookupDefItem.getLookup1().equals("")){
	             	crit.add(Expression.like("lookup1",lookupDefItem.getLookup1(),MatchMode.START).ignoreCase());
	             }
	             
	             if(lookupDefItem.getLookup2() != null && !lookupDefItem.getLookup2().equals("")){
		           	crit.add(Expression.like("lookup2",lookupDefItem.getLookup2(),MatchMode.START).ignoreCase());
		         }
	             
	             if(lookupDefItem.getLookup3() != null && !lookupDefItem.getLookup3().equals("")){
			           	crit.add(Expression.like("lookup3",lookupDefItem.getLookup3(),MatchMode.START).ignoreCase());
			     }

	             if(lookupDefItem.getLookup4() != null && !lookupDefItem.getLookup4().equals("")){
			           	crit.add(Expression.like("lookup4",lookupDefItem.getLookup4(),MatchMode.START).ignoreCase());
			     }
		             
	             if(lookupDefItem.getLookup5() != null && !lookupDefItem.getLookup5().equals("")){
			           	crit.add(Expression.like("lookup5",lookupDefItem.getLookup5(),MatchMode.START).ignoreCase());
			     }

	             crit.add(Expression.eq("deleted",false));
	             
	             return  crit.list();
	             
	         }
	     };
	     return (List) getHibernateTemplate().execute(callback);
	}
	
	
		
    /**
     * @see uk.co.bluetrail.mobriz.dao.LookupDefItemDao#getLookupDefItems(uk.co.bluetrail.mobriz.model.LookupDefItem)
     */
    public List getLookupDefItems(final LookupDefItem lookupDefItem) {
        
    	
    	return getHibernateTemplate().find("from LookupDefItem where deleted = false order by value");

        /* Remove the line above and uncomment this code block if you want 
           to use Hibernate's Query by Example API.
        if (lookupDefItem == null) {
            return getHibernateTemplate().find("from LookupDefItem");
        } else {
            // filter on properties set in the lookupDefItem
            HibernateCallback callback = new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Example ex = Example.create(lookupDefItem).ignoreCase().enableLike(MatchMode.ANYWHERE);
                    return session.createCriteria(LookupDefItem.class).add(ex).list();
                }
            };
            return (List) getHibernateTemplate().execute(callback);
        }*/
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.LookupDefItemDao#getLookupDefItem(Long id)
     */
    public LookupDefItem getLookupDefItem(final Long id) {
        LookupDefItem lookupDefItem = (LookupDefItem) getHibernateTemplate().get(LookupDefItem.class, id);
        if (lookupDefItem == null) {
            log.warn("uh oh, lookupDefItem with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(LookupDefItem.class, id);
        }

        return lookupDefItem;
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.LookupDefItemDao#saveLookupDefItem(LookupDefItem lookupDefItem, User user)
     */    
    public void saveLookupDefItem(final LookupDefItem lookupDefItem) {
        getHibernateTemplate().saveOrUpdate(lookupDefItem);
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.LookupDefItemDao#removeLookupDefItem(Long id)
     */
    public void removeLookupDefItem(final Long id) {
    	//getHibernateTemplate().delete(getLookupDefItem(id));
    	//soft delete!
    	LookupDefItem lookupDefItem = getLookupDefItem(id) ; 
    	lookupDefItem.setDeleted(true) ;
    	saveLookupDefItem(lookupDefItem);
        
    }
}
