package uk.co.bluetrail.mobriz.dao.ibatis;

import java.util.List;

import uk.co.bluetrail.mobriz.dao.LookupDao;

/**
 * iBatis implementation of LookupDao.
 *
 * <p><a href="LookupDaoiBatis.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class LookupDaoiBatis extends BaseDaoiBATIS implements LookupDao {

    /**
     * @see uk.co.bluetrail.mobriz.dao.LookupDao#getRoles()
     */
    public List getRoles() {
        logger.debug("retrieving all role names...");

        return getSqlMapClientTemplate().queryForList("getRoles", null);
    }
}
