
package uk.co.bluetrail.mobriz.service.impl;

import java.util.List;

import uk.co.bluetrail.mobriz.service.impl.BaseManager;
import uk.co.bluetrail.mobriz.model.Account;
import uk.co.bluetrail.mobriz.dao.AccountDao;
import uk.co.bluetrail.mobriz.service.AccountManager;

public class AccountManagerImpl extends BaseManager implements AccountManager {
    private AccountDao dao;

    /**
     * Set the Dao for communication with the data layer.
     * @param dao
     */
    public void setAccountDao(AccountDao dao) {
        this.dao = dao;
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.AccountManager#getAccounts(uk.co.bluetrail.mobriz.model.Account)
     */
    public List getAccounts(final Account account) {
        return dao.getAccounts(account);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.AccountManager#getAccount(String id)
     */
    public Account getAccount(final String id) {
        return dao.getAccount(new Long(id));
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.AccountManager#saveAccount(Account account)
     */
    public void saveAccount(Account account) {
        dao.saveAccount(account);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.AccountManager#removeAccount(String id)
     */
    public void removeAccount(final String id) {
        dao.removeAccount(new Long(id));
    }
}
