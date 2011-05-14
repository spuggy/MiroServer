
package uk.co.bluetrail.mobriz.service;

import java.util.List;

import uk.co.bluetrail.mobriz.service.Manager;
import uk.co.bluetrail.mobriz.model.Account;
import uk.co.bluetrail.mobriz.dao.AccountDao;

public interface AccountManager extends Manager {
    /**
     * Retrieves all of the accounts
     */
    public List getAccounts(Account account);

    /**
     * Gets account's information based on id.
     * @param id the account's id
     * @return account populated account object
     */
    public Account getAccount(final String id);

    /**
     * Saves a account's information
     * @param account the object to be saved
     */
    public void saveAccount(Account account);

    /**
     * Removes a account from the database by id
     * @param id the account's id
     */
    public void removeAccount(final String id);
}

