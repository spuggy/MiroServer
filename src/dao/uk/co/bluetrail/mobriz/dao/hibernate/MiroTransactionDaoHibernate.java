
package uk.co.bluetrail.mobriz.dao.hibernate;

import java.util.List;

import uk.co.bluetrail.mobriz.dao.hibernate.BaseDaoHibernate;
import uk.co.bluetrail.mobriz.model.MiroTransaction;
import uk.co.bluetrail.mobriz.dao.MiroTransactionDao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;

public class MiroTransactionDaoHibernate extends BaseDaoHibernate implements MiroTransactionDao {

    /**
     * @see uk.co.bluetrail.mobriz.dao.MiroTransactionDao#getMiroTransactions(uk.co.bluetrail.mobriz.model.MiroTransaction)
     */
    public List getMiroTransactions(final MiroTransaction miroTransaction) {
       
        if (miroTransaction == null) {
            return getHibernateTemplate().find("from MiroTransaction");
        } else {
            // filter on properties set in the miroTransaction
            HibernateCallback callback = new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Example ex = Example.create(miroTransaction).ignoreCase().enableLike(MatchMode.ANYWHERE);
                    return session.createCriteria(MiroTransaction.class).add(ex).list();
                }
            };
            return (List) getHibernateTemplate().execute(callback);
        }
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.MiroTransactionDao#getMiroTransaction(Long id)
     */
    public MiroTransaction getMiroTransaction(final Long id) {
        MiroTransaction miroTransaction = (MiroTransaction) getHibernateTemplate().get(MiroTransaction.class, id);
        if (miroTransaction == null) {
            log.warn("uh oh, miroTransaction with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(MiroTransaction.class, id);
        }

        return miroTransaction;
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.MiroTransactionDao#saveMiroTransaction(MiroTransaction miroTransaction)
     */    
    public void saveMiroTransaction(final MiroTransaction miroTransaction) {
        getHibernateTemplate().saveOrUpdate(miroTransaction);
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.MiroTransactionDao#removeMiroTransaction(Long id)
     */
    public void removeMiroTransaction(final Long id) {
        getHibernateTemplate().delete(getMiroTransaction(id));
    }
}
