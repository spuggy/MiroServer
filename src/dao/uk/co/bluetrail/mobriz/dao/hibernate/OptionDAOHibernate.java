
package uk.co.bluetrail.mobriz.dao.hibernate;

import java.util.List;

import uk.co.bluetrail.mobriz.model.Option;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.OptionDAO;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;

public class OptionDAOHibernate extends BaseDaoHibernate implements OptionDAO {

	public List getUpdated(Long ckp,boolean deleted, User newParam) {
		if(!deleted){
			return getHibernateTemplate().find("from Option o where o.deleted = false and o.checkPoint > " + ckp.toString() );
		} else {
			return getHibernateTemplate().find("from Option o where o.deleted = true and o.checkPoint > " + ckp.toString() + " and o.question.deleted = false");
		}
	}
	
	public List getOptions(Long sid){

		return getHibernateTemplate().find("from Option o where o.deleted = false and o.question_id in (select id from Question where survey_id = " + sid.toString()+ ") "  );

	}


	public List getOptionsForQuestion(Long qid){

		return getHibernateTemplate().find("from Option o where o.question_id = " + qid.toString()+ " and o.deleted = false order by  oText") ;

	}

	
	
	
    /**
     * @see uk.co.bluetrail.mobriz.dao.OptionDAO#getOptions(uk.co.bluetrail.mobriz.model.Option)
     */
    public List getOptions(final Option option) {
        return getHibernateTemplate().find("from Option o where o.deleted = false");

        /* Remove the line above and uncomment this code block if you want 
           to use Hibernate's Query by Example API.
        if (option == null) {
            return getHibernateTemplate().find("from Option");
        } else {
            // filter on properties set in the option
            HibernateCallback callback = new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Example ex = Example.create(option).ignoreCase().enableLike(MatchMode.ANYWHERE);
                    return session.createCriteria(Option.class).add(ex).list();
                }
            };
            return (List) getHibernateTemplate().execute(callback);
        }*/
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.OptionDAO#getOption(Long id)
     */
    public Option getOption(final Long id) {
        Option option = (Option) getHibernateTemplate().get(Option.class, id);
        if (option == null) {
            log.warn("uh oh, option with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(Option.class, id);
        }

        return option;
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.OptionDAO#saveOption(Option option)
     */    
    public void saveOption(final Option option) {
    	option.setCheckPoint(getNextCheckPoint());
        getHibernateTemplate().saveOrUpdate(option);
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.OptionDAO#removeOption(Long id)
     */
    public void removeOption(final Long id) {
        //getHibernateTemplate().delete(getOption(id));
    	Option o = getOption(id) ; 
    	o.setDeleted(true) ;
    	saveOption(o);
    	
    }
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.bluetrail.mobriz.dao.QuestionDAO#removeBranchesToQuestion(java.lang.Long)
	 */
	public void removeBranchesToQuestion(final Long qid) {

		final Long nextCheckPoint = getNextCheckPoint() ; 
		
		
		HibernateCallback callback = new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				
				String updateSql = "update Option set jquestion_id = 0 , checkPoint = " + nextCheckPoint.toString() + " where jquestion_id = " + qid.toString(); 
				session.createQuery( updateSql ).executeUpdate();
				return null;
			}

		};
		
		getHibernateTemplate().execute(callback);
		return ;

	}

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.dao.OptionDAO#getOptionsWithOText(java.lang.String)
	 */
	public List getOptionsWithOText(String oldFullName) {
		return getHibernateTemplate().find("from Option o where o.deleted = false and otext = '" + oldFullName + "'"); 

	}
    
}
