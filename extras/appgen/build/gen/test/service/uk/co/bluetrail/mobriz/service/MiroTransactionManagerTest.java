
package uk.co.bluetrail.mobriz.service;

import java.util.List;
import java.util.ArrayList;

import uk.co.bluetrail.mobriz.service.BaseManagerTestCase;
import uk.co.bluetrail.mobriz.dao.MiroTransactionDao;
import uk.co.bluetrail.mobriz.model.MiroTransaction;
import uk.co.bluetrail.mobriz.service.impl.MiroTransactionManagerImpl;

import org.jmock.Mock;
import org.springframework.orm.ObjectRetrievalFailureException;

public class MiroTransactionManagerTest extends BaseManagerTestCase {
    private final String miroTransactionId = "1";
    private MiroTransactionManagerImpl miroTransactionManager = new MiroTransactionManagerImpl();
    private Mock miroTransactionDao = null;

    protected void setUp() throws Exception {
        super.setUp();
        miroTransactionDao = new Mock(MiroTransactionDao.class);
        miroTransactionManager.setMiroTransactionDao((MiroTransactionDao) miroTransactionDao.proxy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        miroTransactionManager = null;
    }

    public void testGetMiroTransactions() throws Exception {
        List results = new ArrayList();
        MiroTransaction miroTransaction = new MiroTransaction();
        results.add(miroTransaction);

        // set expected behavior on dao
        miroTransactionDao.expects(once()).method("getMiroTransactions")
            .will(returnValue(results));

        List miroTransactions = miroTransactionManager.getMiroTransactions(null);
        assertTrue(miroTransactions.size() == 1);
        miroTransactionDao.verify();
    }

    public void testGetMiroTransaction() throws Exception {
        // set expected behavior on dao
        miroTransactionDao.expects(once()).method("getMiroTransaction")
            .will(returnValue(new MiroTransaction()));
        MiroTransaction miroTransaction = miroTransactionManager.getMiroTransaction(miroTransactionId);
        assertTrue(miroTransaction != null);
        miroTransactionDao.verify();
    }

    public void testSaveMiroTransaction() throws Exception {
        MiroTransaction miroTransaction = new MiroTransaction();

        // set expected behavior on dao
        miroTransactionDao.expects(once()).method("saveMiroTransaction")
            .with(same(miroTransaction)).isVoid();

        miroTransactionManager.saveMiroTransaction(miroTransaction);
        miroTransactionDao.verify();
    }

    public void testAddAndRemoveMiroTransaction() throws Exception {
        MiroTransaction miroTransaction = new MiroTransaction();

        // set required fields

        // set expected behavior on dao
        miroTransactionDao.expects(once()).method("saveMiroTransaction")
            .with(same(miroTransaction)).isVoid();
        miroTransactionManager.saveMiroTransaction(miroTransaction);
        miroTransactionDao.verify();

        // reset expectations
        miroTransactionDao.reset();

        miroTransactionDao.expects(once()).method("removeMiroTransaction").with(eq(new Long(miroTransactionId)));
        miroTransactionManager.removeMiroTransaction(miroTransactionId);
        miroTransactionDao.verify();

        // reset expectations
        miroTransactionDao.reset();
        // remove
        Exception ex = new ObjectRetrievalFailureException(MiroTransaction.class, miroTransaction.getId());
        miroTransactionDao.expects(once()).method("removeMiroTransaction").isVoid();
        miroTransactionDao.expects(once()).method("getMiroTransaction").will(throwException(ex));
        miroTransactionManager.removeMiroTransaction(miroTransactionId);
        try {
            miroTransactionManager.getMiroTransaction(miroTransactionId);
            fail("MiroTransaction with identifier '" + miroTransactionId + "' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }
        miroTransactionDao.verify();
    }
}
