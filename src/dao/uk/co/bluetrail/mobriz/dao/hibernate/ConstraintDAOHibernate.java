
package uk.co.bluetrail.mobriz.dao.hibernate;

import java.util.List;

import uk.co.bluetrail.mobriz.model.Constraint;
import uk.co.bluetrail.mobriz.model.Option;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.ConstraintDAO;

import org.springframework.orm.ObjectRetrievalFailureException;

public class ConstraintDAOHibernate extends BaseDaoHibernate implements ConstraintDAO {

	public List getUpdated(Long ckp,boolean deleted, User newParam) {
		if(!deleted){
			return getHibernateTemplate().find("from Constraint c where c.deleted = false and c.checkPoint >" + ckp.toString() );
		} else {
			return getHibernateTemplate().find("from Constraint c where c.deleted = true and c.checkPoint >" + ckp.toString() + " and c.question.deleted = false");
		}
	}
	
	public List getConstraints(Long sid){

		return getHibernateTemplate().find("from Constraint c where c.deleted = false and c.question_id in (select id from Question where survey_id = " + sid.toString()+ ")"  );

	}
	
	
    /**
     * @see uk.co.bluetrail.mobriz.dao.ConstraintDAO#getConstraints(uk.co.bluetrail.mobriz.model.Constraint)
     */
    public List getConstraints(final Constraint constraint) {
    	return getHibernateTemplate().find("from Constraint c where c.deleted = false");

        /* Remove the line above and uncomment this code block if you want 
           to use Hibernate's Query by Example API.
        if (constraint == null) {
            return getHibernateTemplate().find("from Constraint");
        } else {
            // filter on properties set in the constraint
            HibernateCallback callback = new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Example ex = Example.create(constraint).ignoreCase().enableLike(MatchMode.ANYWHERE);
                    return session.createCriteria(Constraint.class).add(ex).list();
                }
            };
            return (List) getHibernateTemplate().execute(callback);
        }*/
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.ConstraintDAO#getConstraint(Long id)
     */
    public Constraint getConstraint(final Long id) {
        Constraint constraint = (Constraint) getHibernateTemplate().get(Constraint.class, id);
        if (constraint == null) {
            log.warn("uh oh, constraint with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(Constraint.class, id);
        }

        return constraint;
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.ConstraintDAO#saveConstraint(Constraint constraint)
     */    
    public void saveConstraint(final Constraint constraint) {
    	constraint.setCheckPoint(getNextCheckPoint());
        getHibernateTemplate().saveOrUpdate(constraint);
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.ConstraintDAO#removeConstraint(Long id)
     */
    public void removeConstraint(final Long id) {
        //getHibernateTemplate().delete(getConstraint(id));
    	//soft delete!
    	Constraint c = getConstraint(id) ; 
    	c.setDeleted(true) ;
    	saveConstraint(c);
    }
}
