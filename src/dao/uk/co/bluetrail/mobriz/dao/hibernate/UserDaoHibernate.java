package uk.co.bluetrail.mobriz.dao.hibernate;

import java.util.List;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import uk.co.bluetrail.mobriz.dao.UserDao;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.User;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve User objects.
 *
 * <p><a href="UserDaoHibernate.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *   Modified by <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 *   Extended to implement Acegi UserDetailsService interface by David Carter david@carter.net
*/
public class UserDaoHibernate extends BaseDaoHibernate implements UserDao, UserDetailsService {
    
	
	 
	
	 public List getUserQBE(final User user) {
	    	
			//this just works on pinNumber but I need to do some more work to figure out how to make it work with others 
	    	
	    	if (user == null || user.getPinNumber()== null) {
	            return getHibernateTemplate().find("from User");
	        } else {
	            // filter on properties set in the activity
	            HibernateCallback callback = new HibernateCallback() {
	                public Object doInHibernate(Session session) throws HibernateException {
	                	 Criteria crit = session.createCriteria(User.class);
	    	             	             
	    	             //crit.add(Expression.like("accCity",account.getAccCity(),MatchMode.START).ignoreCase());
	    	             crit.add(Expression.eq("pinNumber", user.getPinNumber()));
	    	             crit.add(Expression.eq("deleted", false));
	    	             System.out.println(crit.toString());
	    	             
	    	             return  crit.list();
	                }
	            };
	            return (List) getHibernateTemplate().execute(callback);
	        } 
	    }
	
	/**
     * @see uk.co.bluetrail.mobriz.dao.UserDao#getUser(Long)
     */
    public User getUser(Long userId) {
    	 User user = (User) getHibernateTemplate().get(User.class, userId);

         if (user == null) {
             log.warn("uh oh, user '" + userId + "' not found...");
             throw new ObjectRetrievalFailureException(User.class, userId);
         }

         
        
         if (user.isDeleted() ) {
             log.warn("uh oh, user '" + userId + "' deleted...");
             throw new ObjectRetrievalFailureException(User.class, userId);
         }
         
         return user;
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.UserDao#getUsers(uk.co.bluetrail.mobriz.model.User)
     */
    public List getUsers(User user) {
    	    	
    	return getHibernateTemplate().find("from User where deleted = false",getCurrentUser().getAccount_id());
    	
    	
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.UserDao#saveUser(uk.co.bluetrail.mobriz.model.User)
     */
    public void saveUser(final User user) {
        if (log.isDebugEnabled()) {
            log.debug("user's id: " + user.getId());
        }
        
             
        getHibernateTemplate().saveOrUpdate(user);
        // necessary to throw a DataIntegrityViolation and catch it in UserManager
        getHibernateTemplate().flush();
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.UserDao#removeUser(Long)
     */
    public void removeUser(Long userId) {
        //getHibernateTemplate().delete(getUser(userId));
    	User user = getUser(userId) ; 
    	user.setDeleted(true) ;
    	saveUser(user);
    }

    /** 
    * @see org.acegisecurity.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
    */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
    	   
    	User u = getCurrentUser() ;
    	
    	List users = null;
    	
    	if(u ==null) {
    		//we must be loggin in so there will be no current user at the mo so don't search for account_id
    		users = getHibernateTemplate().find("from User where username=? and deleted = false", username);		
    	} else {
    		Object[] params = new Object[2] ;
        	params[0] = username ; 
        	params[1] = u.getAccount_id() ;        	
    		users = getHibernateTemplate().find("from User where username=? and account_id = ? and deleted = false", params);
    	}
    	
    	   	
    
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("user '" + username + "' not found...");
        } else {
            return (UserDetails) users.get(0);
        }
    }

    public List getAdminUsers() {
    //return getHibernateTemplate().find( );
    	HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
            	// List l = session.createQuery("select concat(a.user.lastName,concat('~',a.user.firstName)), count(a) from Activity a group by concat(a.user.lastName,concat('~',a.user.firstName)) order by concat(a.user.lastName,concat('~',a.user.firstName)))").list();	             
            	 SQLQuery q = session.createSQLQuery("select * from app_user where deleted = false and id in (select user_id from user_role where role_id = 1) order by lower(last_Name)"); 
            	 q.addEntity(User.class);
            	  
            	return  q.list();                                                                                                              
            }
        };
        return (List) getHibernateTemplate().execute(callback);
    
    
    }
    
	public List getEnabledUsers() {
		return getHibernateTemplate().find("from User where account_id = ? and enabled = true and deleted = false order by lower(lastName)",getCurrentUser().getAccount_id());
		
	}

	public List getProjectUsers(Long projectId) {
		
	    return getHibernateTemplate().find("from User where project_Id = ? and deleted = false order by id desc",projectId);
	   
	}

	public List getUsers(final User user, final int status) {
		  // filter on properties set in the activity
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
            	 Criteria crit = session.createCriteria(User.class);
	             	             
	             //crit.add(Expression.like("accCity",account.getAccCity(),MatchMode.START).ignoreCase());
	             crit.add(Expression.eq("createdBy_id", user.getId()));
	             crit.add(Expression.eq("oldreport", false));
	             crit.add(Expression.eq("status", status));
		            
	             crit.addOrder(Order.desc("created_on"));
	             
	             return  crit.list();
            }
        };
        return (List) getHibernateTemplate().execute(callback);
	}

	public List getUsersByProjects(final Long[] selectedProjectIds, final int status) {
		  // filter on properties set in the activity
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
            	 Criteria crit = session.createCriteria(User.class);
	             	             
	             //crit.add(Expression.like("accCity",account.getAccCity(),MatchMode.START).ignoreCase());
	             crit.add(Restrictions.in("project_id",selectedProjectIds));
	             crit.add(Expression.eq("deleted", false));
	             crit.add(Expression.eq("status", status));
	             crit.add(Expression.eq("oldreport", false));
	             
	             crit.addOrder(Order.asc("lastName"));
	            
	             return  crit.list();
            }
        };
        return (List) getHibernateTemplate().execute(callback);
	}

	public List getUsers(final Long[] userIds) {
		 HibernateCallback callback = new HibernateCallback() {
	            public Object doInHibernate(Session session) throws HibernateException {
	            	 Criteria crit = session.createCriteria(User.class);
		             	             
		             //crit.add(Expression.like("accCity",account.getAccCity(),MatchMode.START).ignoreCase());
		             crit.add(Restrictions.in("id",userIds));
		             crit.add(Expression.eq("deleted", false));
		             crit.addOrder(Order.asc("lastName"));
		             return  crit.list();
	            }
	        };
	        return (List) getHibernateTemplate().execute(callback);
	}
    	
    	
}
