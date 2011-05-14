
package uk.co.bluetrail.mobriz.service;

import java.util.List;
import java.util.ArrayList;

import uk.co.bluetrail.mobriz.service.BaseManagerTestCase;
import uk.co.bluetrail.mobriz.dao.LookupDefDao;
import uk.co.bluetrail.mobriz.model.LookupDef;
import uk.co.bluetrail.mobriz.service.impl.LookupDefManagerImpl;

import org.jmock.Mock;
import org.springframework.orm.ObjectRetrievalFailureException;

public class LookupDefManagerTest extends BaseManagerTestCase {
    private final String lookupDefId = "1";
    private LookupDefManagerImpl lookupDefManager = new LookupDefManagerImpl();
    private Mock lookupDefDao = null;

    protected void setUp() throws Exception {
        super.setUp();
        lookupDefDao = new Mock(LookupDefDao.class);
        lookupDefManager.setLookupDefDao((LookupDefDao) lookupDefDao.proxy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        lookupDefManager = null;
    }

    public void testGetLookupDefs() throws Exception {
        List results = new ArrayList();
        LookupDef lookupDef = new LookupDef();
        results.add(lookupDef);

        // set expected behavior on dao
        lookupDefDao.expects(once()).method("getLookupDefs")
            .will(returnValue(results));

        List lookupDefs = lookupDefManager.getLookupDefs(null);
        assertTrue(lookupDefs.size() == 1);
        lookupDefDao.verify();
    }

    public void testGetLookupDef() throws Exception {
        // set expected behavior on dao
        lookupDefDao.expects(once()).method("getLookupDef")
            .will(returnValue(new LookupDef()));
        LookupDef lookupDef = lookupDefManager.getLookupDef(lookupDefId);
        assertTrue(lookupDef != null);
        lookupDefDao.verify();
    }

    public void testSaveLookupDef() throws Exception {
        LookupDef lookupDef = new LookupDef();

        // set expected behavior on dao
        lookupDefDao.expects(once()).method("saveLookupDef")
            .with(same(lookupDef)).isVoid();

        lookupDefManager.saveLookupDef(lookupDef,null);
        lookupDefDao.verify();
    }

    public void testAddAndRemoveLookupDef() throws Exception {
        LookupDef lookupDef = new LookupDef();

        // set required fields
        lookupDef.setName("PsUnRiPiRaYvRgMiApGbGfLrVdYcNr");
        lookupDef.setDesc("XhRsOsArVuRfHjQhOeAdJcIgAcSgOtNuEmDcBnBxPeGxKfHsYxAtRyFyClAyYpVlYjJhYbWdDmPzFaRcPcBjBnWnEgVzPwYxAnEsGmJjGrOyQzGkRnHjNvWbYsVvRlFnDmRvTeAwCjEhXnJwMiZfHkZqRdJtCeRgEtEvUaPrMwKhCxZqHgKwJsGiBlSaSbPxRnGbJfGpHlZaEhMfSjLuAeBvIgVvQwTeXeIhGbZfDcBqTwVsDyTrTrWgKaEyFcM");
        lookupDef.setLookupDesc1("NvQkRrEdHeEfWsIbNmMz");
        lookupDef.setLookupDesc2("GlQpYqLvYrWlOnEtFaUf");
        lookupDef.setLookupDesc3("JoKqToQxIdFmIsRzTkFz");
        lookupDef.setLookupDesc4("QqLcPtJjPwPcNoJwRnYr");
        lookupDef.setLookupDesc5("GgRbDpEiQjYjQiSeIwIj");

        // set expected behavior on dao
        lookupDefDao.expects(once()).method("saveLookupDef")
            .with(same(lookupDef)).isVoid();
        lookupDefManager.saveLookupDef(lookupDef,null);
        lookupDefDao.verify();

        // reset expectations
        lookupDefDao.reset();

        lookupDefDao.expects(once()).method("removeLookupDef").with(eq(new Long(lookupDefId)));
        lookupDefManager.removeLookupDef(lookupDefId);
        lookupDefDao.verify();

        // reset expectations
        lookupDefDao.reset();
        // remove
        Exception ex = new ObjectRetrievalFailureException(LookupDef.class, lookupDef.getId());
        lookupDefDao.expects(once()).method("removeLookupDef").isVoid();
        lookupDefDao.expects(once()).method("getLookupDef").will(throwException(ex));
        lookupDefManager.removeLookupDef(lookupDefId);
        try {
            lookupDefManager.getLookupDef(lookupDefId);
            fail("LookupDef with identifier '" + lookupDefId + "' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }
        lookupDefDao.verify();
    }
}
