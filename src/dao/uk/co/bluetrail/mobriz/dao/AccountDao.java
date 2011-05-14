package uk.co.bluetrail.mobriz.dao;

import java.util.List;

import uk.co.bluetrail.mobriz.dao.Dao;
import uk.co.bluetrail.mobriz.model.Account;

public interface AccountDao extends Dao {

    /**
     * Retrieves all of the accounts
     */
    public List getAccounts(Account account);

    /**
     * Gets account's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the account's id
     * @return account populated account object
     */
    public Account getAccount(final Long id);

    /**
     * Saves a account's information
     * @param account the object to be saved
     */    
    public void saveAccount(Account account);

    /**
     * Removes a account from the database by id
     * @param id the account's id
     */
    public void removeAccount(final Long id);
}

