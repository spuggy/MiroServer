
package uk.co.bluetrail.mobriz.service;

import java.util.List;
import java.util.ArrayList;

import uk.co.bluetrail.mobriz.service.BaseManagerTestCase;
import uk.co.bluetrail.mobriz.dao.MiroTeamDao;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.impl.MiroTeamManagerImpl;

import org.jmock.Mock;
import org.springframework.orm.ObjectRetrievalFailureException;

public class MiroTeamManagerTest extends BaseManagerTestCase {
    private final String miroTeamId = "1";
    private MiroTeamManagerImpl miroTeamManager = new MiroTeamManagerImpl();
    private Mock miroTeamDao = null;

    protected void setUp() throws Exception {
        super.setUp();
        miroTeamDao = new Mock(MiroTeamDao.class);
        miroTeamManager.setMiroTeamDao((MiroTeamDao) miroTeamDao.proxy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        miroTeamManager = null;
    }

    public void testGetMiroTeams() throws Exception {
        List results = new ArrayList();
        MiroTeam miroTeam = new MiroTeam();
        results.add(miroTeam);

        // set expected behavior on dao
        miroTeamDao.expects(once()).method("getMiroTeams")
            .will(returnValue(results));
        
        User u = new User();

        List miroTeams = miroTeamManager.getMiroTeams(null,u);
        assertTrue(miroTeams.size() == 1);
        miroTeamDao.verify();
    }

    public void testGetMiroTeam() throws Exception {
        // set expected behavior on dao
        miroTeamDao.expects(once()).method("getMiroTeam")
            .will(returnValue(new MiroTeam()));
        MiroTeam miroTeam = miroTeamManager.getMiroTeam(miroTeamId);
        assertTrue(miroTeam != null);
        miroTeamDao.verify();
    }

    public void testSaveMiroTeam() throws Exception {
        MiroTeam miroTeam = new MiroTeam();

        // set expected behavior on dao
        miroTeamDao.expects(once()).method("saveMiroTeam")
            .with(same(miroTeam)).isVoid();

        User u = new User();
        
        miroTeamManager.saveMiroTeam(miroTeam,u);
        miroTeamDao.verify();
    }

    public void testAddAndRemoveMiroTeam() throws Exception {
        MiroTeam miroTeam = new MiroTeam();

        User u = new User();
        
        // set required fields
         miroTeam.setMiroTeamReportName("UyWxIpGzBuOyKbGoBfQgAdDpHiUfHp");
     
        // set expected behavior on dao
        miroTeamDao.expects(once()).method("saveMiroTeam")
            .with(same(miroTeam)).isVoid();
        miroTeamManager.saveMiroTeam(miroTeam,u);
        miroTeamDao.verify();

        // reset expectations
        miroTeamDao.reset();

        miroTeamDao.expects(once()).method("removeMiroTeam").with(eq(new Long(miroTeamId)));
        miroTeamManager.removeMiroTeam(miroTeamId);
        miroTeamDao.verify();

        // reset expectations
        miroTeamDao.reset();
        // remove
        Exception ex = new ObjectRetrievalFailureException(MiroTeam.class, miroTeam.getId());
        miroTeamDao.expects(once()).method("removeMiroTeam").isVoid();
        miroTeamDao.expects(once()).method("getMiroTeam").will(throwException(ex));
        miroTeamManager.removeMiroTeam(miroTeamId);
        try {
            miroTeamManager.getMiroTeam(miroTeamId);
            fail("MiroTeam with identifier '" + miroTeamId + "' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }
        miroTeamDao.verify();
    }
}
