
package uk.co.bluetrail.mobriz.service;

import java.util.List;

import uk.co.bluetrail.mobriz.service.Manager;
import uk.co.bluetrail.mobriz.model.MiroTransaction;
import uk.co.bluetrail.mobriz.dao.MiroTransactionDao;

public interface MiroTransactionManager extends Manager {
    /**
     * Retrieves all of the miroTransactions
     */
    public List getMiroTransactions(MiroTransaction miroTransaction);

    /**
     * Gets miroTransaction's information based on id.
     * @param id the miroTransaction's id
     * @return miroTransaction populated miroTransaction object
     */
    public MiroTransaction getMiroTransaction(final String id);

    /**
     * Saves a miroTransaction's information
     * @param miroTransaction the object to be saved
     */
    public void saveMiroTransaction(MiroTransaction miroTransaction);

    /**
     * Removes a miroTransaction from the database by id
     * @param id the miroTransaction's id
     */
    public void removeMiroTransaction(final String id);
}

