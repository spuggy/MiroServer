
package uk.co.bluetrail.mobriz.service;

import java.util.List;
import java.util.ArrayList;

import uk.co.bluetrail.mobriz.service.BaseManagerTestCase;
import uk.co.bluetrail.mobriz.dao.LookupDefItemDao;
import uk.co.bluetrail.mobriz.model.LookupDefItem;
import uk.co.bluetrail.mobriz.service.impl.LookupDefItemManagerImpl;

import org.jmock.Mock;
import org.springframework.orm.ObjectRetrievalFailureException;

public class LookupDefItemManagerTest extends BaseManagerTestCase {
    private final String lookupDefItemId = "1";
    private LookupDefItemManagerImpl lookupDefItemManager = new LookupDefItemManagerImpl();
    private Mock lookupDefItemDao = null;

    protected void setUp() throws Exception {
        super.setUp();
        lookupDefItemDao = new Mock(LookupDefItemDao.class);
        lookupDefItemManager.setLookupDefItemDao((LookupDefItemDao) lookupDefItemDao.proxy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        lookupDefItemManager = null;
    }

    public void testGetLookupDefItems() throws Exception {
        List results = new ArrayList();
        LookupDefItem lookupDefItem = new LookupDefItem();
        results.add(lookupDefItem);

        // set expected behavior on dao
        lookupDefItemDao.expects(once()).method("getLookupDefItems")
            .will(returnValue(results));

        List lookupDefItems = lookupDefItemManager.getLookupDefItems(null);
        assertTrue(lookupDefItems.size() == 1);
        lookupDefItemDao.verify();
    }

    public void testGetLookupDefItem() throws Exception {
        // set expected behavior on dao
        lookupDefItemDao.expects(once()).method("getLookupDefItem")
            .will(returnValue(new LookupDefItem()));
        LookupDefItem lookupDefItem = lookupDefItemManager.getLookupDefItem(lookupDefItemId);
        assertTrue(lookupDefItem != null);
        lookupDefItemDao.verify();
    }

    public void testSaveLookupDefItem() throws Exception {
        LookupDefItem lookupDefItem = new LookupDefItem();

        // set expected behavior on dao
        lookupDefItemDao.expects(once()).method("saveLookupDefItem")
            .with(same(lookupDefItem)).isVoid();

        lookupDefItemManager.saveLookupDefItem(lookupDefItem,null);
        lookupDefItemDao.verify();
    }

    public void testAddAndRemoveLookupDefItem() throws Exception {
        LookupDefItem lookupDefItem = new LookupDefItem();

        // set required fields

        // set expected behavior on dao
        lookupDefItemDao.expects(once()).method("saveLookupDefItem")
            .with(same(lookupDefItem)).isVoid();
        lookupDefItemManager.saveLookupDefItem(lookupDefItem,null);
        lookupDefItemDao.verify();

        // reset expectations
        lookupDefItemDao.reset();

        lookupDefItemDao.expects(once()).method("removeLookupDefItem").with(eq(new Long(lookupDefItemId)));
        lookupDefItemManager.removeLookupDefItem(lookupDefItemId);
        lookupDefItemDao.verify();

        // reset expectations
        lookupDefItemDao.reset();
        // remove
        Exception ex = new ObjectRetrievalFailureException(LookupDefItem.class, lookupDefItem.getId());
        lookupDefItemDao.expects(once()).method("removeLookupDefItem").isVoid();
        lookupDefItemDao.expects(once()).method("getLookupDefItem").will(throwException(ex));
        lookupDefItemManager.removeLookupDefItem(lookupDefItemId);
        try {
            lookupDefItemManager.getLookupDefItem(lookupDefItemId);
            fail("LookupDefItem with identifier '" + lookupDefItemId + "' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }
        lookupDefItemDao.verify();
    }
}
