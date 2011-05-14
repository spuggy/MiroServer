package uk.co.bluetrail.mobriz.service;

import java.util.ArrayList;
import java.util.List;

import uk.co.bluetrail.mobriz.dao.LookupDao;
import uk.co.bluetrail.mobriz.model.Role;
import uk.co.bluetrail.mobriz.service.impl.LookupManagerImpl;
import org.jmock.Mock;


public class LookupManagerTest extends BaseManagerTestCase {
    private LookupManager mgr = new LookupManagerImpl();
    private Mock lookupDao = null;

    protected void setUp() throws Exception {
        super.setUp();
        lookupDao = new Mock(LookupDao.class);
        mgr.setLookupDao((LookupDao) lookupDao.proxy());
    }

    public void testGetAllRoles() {
        if (log.isDebugEnabled()) {
            log.debug("entered 'testGetAllRoles' method");
        }

        // set expected behavior on dao
        Role role = new Role("admin");
        List testData = new ArrayList();
        testData.add(role);
        lookupDao.expects(once()).method("getRoles")
                 .withNoArguments().will(returnValue(testData));

        List roles = mgr.getAllRoles();
        assertTrue(roles.size() > 0);
        // verify expectations
        lookupDao.verify();
    }
}
