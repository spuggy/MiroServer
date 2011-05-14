
package uk.co.bluetrail.mobriz.dao.hibernate;

import java.util.List;

import uk.co.bluetrail.mobriz.dao.hibernate.BaseDaoHibernate;
import uk.co.bluetrail.mobriz.model.Account;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.AccountDao;


import org.springframework.orm.ObjectRetrievalFailureException;

public class AccountDaoHibernate extends BaseDaoHibernate implements AccountDao {

    /**
     * @see uk.co.bluetrail.mobriz.dao.AccountDao#getAccounts(uk.co.bluetrail.mobriz.model.Account)
     */
    public List getAccounts(final Account account) {
            	
    	
    	return getHibernateTemplate().find("from Account");

        /* Remove the line above and uncomment this code block if you want 
           to use Hibernate's Query by Example API.
        if (account == null) {
            return getHibernateTemplate().find("from Account");
        } else {
            // filter on properties set in the account
            HibernateCallback callback = new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Example ex = Example.create(account).ignoreCase().enableLike(MatchMode.ANYWHERE);
                    return session.createCriteria(Account.class).add(ex).list();
                }
            };
            return (List) getHibernateTemplate().execute(callback);
        }*/
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.AccountDao#getAccount(Long id)
     */
    public Account getAccount(final Long id) {
        
    	//getHibernateTemplate().setFilterName(null);
    	
    	Account account = (Account) getHibernateTemplate().get(Account.class, id);
        if (account == null) {
            log.warn("uh oh, account with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(Account.class, id);
        }

        return account;
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.AccountDao#saveAccount(Account account)
     */    
    public void saveAccount(final Account account) {
        getHibernateTemplate().saveOrUpdate(account);
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.AccountDao#removeAccount(Long id)
     */
    public void removeAccount(final Long id) {
        getHibernateTemplate().delete(getAccount(id));
    }
}
