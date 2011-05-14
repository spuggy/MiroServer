
package uk.co.bluetrail.mobriz.dao;

import java.util.List;

import uk.co.bluetrail.mobriz.dao.Dao;
import uk.co.bluetrail.mobriz.model.MiroTransaction;

public interface MiroTransactionDao extends Dao {

    /**
     * Retrieves all of the miroTransactions
     */
    public List getMiroTransactions(MiroTransaction miroTransaction);

    /**
     * Gets miroTransaction's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the miroTransaction's id
     * @return miroTransaction populated miroTransaction object
     */
    public MiroTransaction getMiroTransaction(final Long id);

    /**
     * Saves a miroTransaction's information
     * @param miroTransaction the object to be saved
     */    
    public void saveMiroTransaction(MiroTransaction miroTransaction);

    /**
     * Removes a miroTransaction from the database by id
     * @param id the miroTransaction's id
     */
    public void removeMiroTransaction(final Long id);
}

