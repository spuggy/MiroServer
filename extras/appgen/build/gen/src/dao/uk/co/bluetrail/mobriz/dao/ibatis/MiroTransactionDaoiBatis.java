
package uk.co.bluetrail.mobriz.dao.ibatis;

import java.util.List;

import uk.co.bluetrail.mobriz.dao.ibatis.BaseDaoiBATIS;
import uk.co.bluetrail.mobriz.model.MiroTransaction;
import uk.co.bluetrail.mobriz.dao.MiroTransactionDao;

import org.springframework.orm.ObjectRetrievalFailureException;

public class MiroTransactionDaoiBatis extends BaseDaoiBATIS implements MiroTransactionDao {

    /**
     * @see uk.co.bluetrail.mobriz.dao.MiroTransactionDao#getMiroTransactions(uk.co.bluetrail.mobriz.model.MiroTransaction)
     */
    public List getMiroTransactions(final MiroTransaction miroTransaction) {
          return getSqlMapClientTemplate().queryForList("getMiroTransactions", null);
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.MiroTransactionDao#getMiroTransaction(Long id)
     */
    public MiroTransaction getMiroTransaction(Long id) {
        MiroTransaction miroTransaction = (MiroTransaction) getSqlMapClientTemplate().queryForObject("getMiroTransaction", id);

        if (miroTransaction == null) {
            throw new ObjectRetrievalFailureException(MiroTransaction.class, id);
        }

        return miroTransaction;
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.MiroTransactionDao#saveMiroTransaction(MiroTransaction miroTransaction)
     */    
    public void saveMiroTransaction(final MiroTransaction miroTransaction) {
        Long id = miroTransaction.getId();
        // check for new record
        if (id == null) {
            id = (Long) getSqlMapClientTemplate().insert("addMiroTransaction", miroTransaction);
        } else {
            getSqlMapClientTemplate().update("updateMiroTransaction", miroTransaction);
        }
        if( id == null ) {
            throw new ObjectRetrievalFailureException(MiroTransaction.class, id);
        }
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.MiroTransactionDao#removeMiroTransaction(Long id)
     */
    public void removeMiroTransaction(Long id) {
        getSqlMapClientTemplate().update("deleteMiroTransaction", id);
    }
}
