
package uk.co.bluetrail.mobriz.service;

import java.util.List;
import java.util.ArrayList;

import uk.co.bluetrail.mobriz.service.BaseManagerTestCase;
import uk.co.bluetrail.mobriz.dao.AccountDao;
import uk.co.bluetrail.mobriz.model.Account;
import uk.co.bluetrail.mobriz.service.impl.AccountManagerImpl;

import org.jmock.Mock;
import org.springframework.orm.ObjectRetrievalFailureException;

public class AccountManagerTest extends BaseManagerTestCase {
    private final String accountId = "1";
    private AccountManagerImpl accountManager = new AccountManagerImpl();
    private Mock accountDao = null;

    protected void setUp() throws Exception {
        super.setUp();
        accountDao = new Mock(AccountDao.class);
        accountManager.setAccountDao((AccountDao) accountDao.proxy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        accountManager = null;
    }

    public void testGetAccounts() throws Exception {
        List results = new ArrayList();
        Account account = new Account();
        results.add(account);

        // set expected behavior on dao
        accountDao.expects(once()).method("getAccounts")
            .will(returnValue(results));

        List accounts = accountManager.getAccounts(null);
        assertTrue(accounts.size() == 1);
        accountDao.verify();
    }

    public void testGetAccount() throws Exception {
        // set expected behavior on dao
        accountDao.expects(once()).method("getAccount")
            .will(returnValue(new Account()));
        Account account = accountManager.getAccount(accountId);
        assertTrue(account != null);
        accountDao.verify();
    }

    public void testSaveAccount() throws Exception {
        Account account = new Account();

        // set expected behavior on dao
        accountDao.expects(once()).method("saveAccount")
            .with(same(account)).isVoid();

        accountManager.saveAccount(account);
        accountDao.verify();
    }

    public void testAddAndRemoveAccount() throws Exception {
        Account account = new Account();

        // set required fields
        account.setCompanyName("CjNjVlOySsWuGhQjByXbVwUeSrKjQqHzCjTtNoJzZbUrUvQkRc");

        // set expected behavior on dao
        accountDao.expects(once()).method("saveAccount")
            .with(same(account)).isVoid();
        accountManager.saveAccount(account);
        accountDao.verify();

        // reset expectations
        accountDao.reset();

        accountDao.expects(once()).method("removeAccount").with(eq(new Long(accountId)));
        accountManager.removeAccount(accountId);
        accountDao.verify();

        // reset expectations
        accountDao.reset();
        // remove
        Exception ex = new ObjectRetrievalFailureException(Account.class, account.getId());
        accountDao.expects(once()).method("removeAccount").isVoid();
        accountDao.expects(once()).method("getAccount").will(throwException(ex));
        accountManager.removeAccount(accountId);
        try {
            accountManager.getAccount(accountId);
            fail("Account with identifier '" + accountId + "' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }
        accountDao.verify();
    }
}
