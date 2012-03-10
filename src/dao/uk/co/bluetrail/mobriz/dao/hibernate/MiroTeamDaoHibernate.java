
package uk.co.bluetrail.mobriz.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import uk.co.bluetrail.mobriz.dao.hibernate.BaseDaoHibernate;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.MiroTeamDao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;

public class MiroTeamDaoHibernate extends BaseDaoHibernate implements MiroTeamDao {

    /**
     * @see uk.co.bluetrail.mobriz.dao.MiroTeamDao#getMiroTeams(uk.co.bluetrail.mobriz.model.MiroTeam, User)
     */
    public List getMiroTeams(final MiroTeam miroTeam, final User user) {

    	HibernateCallback callback = new HibernateCallback() {
 	     	public Object doInHibernate(Session session) throws HibernateException, SQLException {
 	         	 
 	             Criteria crit = session.createCriteria(MiroTeam.class);
 	            
 	            
 	            crit.add(Expression.eq("createdBy_id",user.getId()));
 	             
 	            crit.addOrder(Order.desc("updated_at"));
 	             
 	             
 	             return  crit.list();
 	             
 	         }
 	     };
 		
 	     return (List) getHibernateTemplate().execute(callback);
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.MiroTeamDao#getMiroTeam(Long id)
     */
    public MiroTeam getMiroTeam(final Long id) {
        MiroTeam miroTeam = (MiroTeam) getHibernateTemplate().get(MiroTeam.class, id);
        if (miroTeam == null) {
            log.warn("uh oh, miroTeam with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(MiroTeam.class, id);
        }

        return miroTeam;
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.MiroTeamDao#saveMiroTeam(MiroTeam miroTeam)
     */    
    public void saveMiroTeam(final MiroTeam miroTeam) {
        getHibernateTemplate().saveOrUpdate(miroTeam);
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.MiroTeamDao#removeMiroTeam(Long id)
     */
    public void removeMiroTeam(final Long id) {
        getHibernateTemplate().delete(getMiroTeam(id));
    }
    
	public List getMiroTeamsByExample(final MiroTeam miroTeam, final int limit) {
//		 filter on properties set in the surveyResponse
       HibernateCallback callback = new HibernateCallback() {
           public Object doInHibernate(Session session) throws HibernateException {
               Example ex = Example.create(miroTeam).ignoreCase().enableLike(MatchMode.ANYWHERE);
               Criteria crit = session.createCriteria(MiroTeam.class).add(ex);
               crit.setFetchSize(limit);
                       return crit.list();
           }
       };
      return (List) getHibernateTemplate().execute(callback);
   
	}
	
	
    
    
    
}
