package uk.co.bluetrail.mobriz.dao;

import java.util.List;

import uk.co.bluetrail.mobriz.dao.BaseDaoTestCase;
import uk.co.bluetrail.mobriz.model.MiroTransaction;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.ObjectRetrievalFailureException;

public class MiroTransactionDaoTest extends BaseDaoTestCase {
    private Long miroTransactionId = new Long("1");
    private MiroTransactionDao dao = null;

    public void setMiroTransactionDao(MiroTransactionDao dao) {
        this.dao = dao;
    }

    public void testAddMiroTransaction() throws Exception {
        MiroTransaction miroTransaction = new MiroTransaction();

        // set required fields

        dao.saveMiroTransaction(miroTransaction);

        // verify a primary key was assigned
        assertNotNull(miroTransaction.getId());

        // verify set fields are same after save
    }

    public void testGetMiroTransaction() throws Exception {
        MiroTransaction miroTransaction = dao.getMiroTransaction(miroTransactionId);
        assertNotNull(miroTransaction);
    }

    public void testGetMiroTransactions() throws Exception {
        MiroTransaction miroTransaction = new MiroTransaction();

        List results = dao.getMiroTransactions(miroTransaction);
        assertTrue(results.size() > 0);
    }

    public void testSaveMiroTransaction() throws Exception {
        MiroTransaction miroTransaction = dao.getMiroTransaction(miroTransactionId);

        // update required fields

        dao.saveMiroTransaction(miroTransaction);

    }

    public void testRemoveMiroTransaction() throws Exception {
        Long removeId = new Long("3");
        dao.removeMiroTransaction(removeId);
        try {
            dao.getMiroTransaction(removeId);
            fail("miroTransaction found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        } catch (InvalidDataAccessApiUsageException e) { // Spring 2.0 throws this one
            assertNotNull(e.getMessage());        	
        }
    }
}
