
package uk.co.bluetrail.mobriz.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import uk.co.bluetrail.mobriz.dao.hibernate.BaseDaoHibernate;
import uk.co.bluetrail.mobriz.model.MiroProject;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.MiroProjectDao;

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

public class MiroProjectDaoHibernate extends BaseDaoHibernate implements MiroProjectDao {

    /**
     * @see uk.co.bluetrail.mobriz.dao.MiroProjectDao#getMiroProjects(uk.co.bluetrail.mobriz.model.MiroProject, User)
     */
    public List getMiroProjects(final MiroProject miroProject, final User user) {

    	HibernateCallback callback = new HibernateCallback() {
 	     	public Object doInHibernate(Session session) throws HibernateException, SQLException {
 	         	 
 	             Criteria crit = session.createCriteria(MiroProject.class);
 	            
 	            
 	            crit.add(Expression.eq("createdBy_id",user.getId()));
 	         
 	            crit.add(Expression.ne("projectStatus",3));
 	             
 	           crit.addOrder(Order.desc("updated_at"));
 	             
 	             
 	             return  crit.list();
 	             
 	         }
 	     };
 		
 	     return (List) getHibernateTemplate().execute(callback);
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.MiroProjectDao#getMiroProject(Long id)
     */
    public MiroProject getMiroProject(final Long id) {
        MiroProject miroProject = (MiroProject) getHibernateTemplate().get(MiroProject.class, id);
        if (miroProject == null) {
            log.warn("uh oh, miroProject with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(MiroProject.class, id);
        }

        return miroProject;
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.MiroProjectDao#saveMiroProject(MiroProject miroProject)
     */    
    public void saveMiroProject(final MiroProject miroProject) {
        getHibernateTemplate().saveOrUpdate(miroProject);
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.MiroProjectDao#removeMiroProject(Long id)
     */
    public void removeMiroProject(final Long id) {
        getHibernateTemplate().delete(getMiroProject(id));
    }
}
