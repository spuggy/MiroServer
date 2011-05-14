
package uk.co.bluetrail.mobriz.service.impl;

import java.util.List;

import uk.co.bluetrail.mobriz.service.impl.BaseManager;
import uk.co.bluetrail.mobriz.model.MiroTransaction;
import uk.co.bluetrail.mobriz.dao.MiroTransactionDao;
import uk.co.bluetrail.mobriz.service.MiroTransactionManager;

public class MiroTransactionManagerImpl extends BaseManager implements MiroTransactionManager {
    private MiroTransactionDao dao;

    /**
     * Set the Dao for communication with the data layer.
     * @param dao
     */
    public void setMiroTransactionDao(MiroTransactionDao dao) {
        this.dao = dao;
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MiroTransactionManager#getMiroTransactions(uk.co.bluetrail.mobriz.model.MiroTransaction)
     */
    public List getMiroTransactions(final MiroTransaction miroTransaction) {
        return dao.getMiroTransactions(miroTransaction);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MiroTransactionManager#getMiroTransaction(String id)
     */
    public MiroTransaction getMiroTransaction(final String id) {
        return dao.getMiroTransaction(new Long(id));
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MiroTransactionManager#saveMiroTransaction(MiroTransaction miroTransaction)
     */
    public void saveMiroTransaction(MiroTransaction miroTransaction) {
        dao.saveMiroTransaction(miroTransaction);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MiroTransactionManager#removeMiroTransaction(String id)
     */
    public void removeMiroTransaction(final String id) {
        dao.removeMiroTransaction(new Long(id));
    }
}
