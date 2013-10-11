
package uk.co.bluetrail.mobriz.dao.hibernate;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import uk.co.bluetrail.mobriz.model.Constraint;
import uk.co.bluetrail.mobriz.model.Question;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.SurveyDAO;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;

public class SurveyDAOHibernate extends BaseDaoHibernate implements SurveyDAO {

	
	

	
	
    /**
     * @see uk.co.bluetrail.mobriz.dao.SurveyDAO#getSurveys(uk.co.bluetrail.mobriz.model.Survey)
     */
    public List getSurveys(final Survey survey) {
    	 return getHibernateTemplate().find("from Survey s where s.surveyStatus !='X' order by updated_at desc ");

        /* Remove the line above and uncomment this code block if you want 
           to use Hibernate's Query by Example API.
        if (survey == null) {
            return getHibernateTemplate().find("from Survey");
        } else {
            // filter on properties set in the survey
            HibernateCallback callback = new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Example ex = Example.create(survey).ignoreCase().enableLike(MatchMode.ANYWHERE);
                    return session.createCriteria(Survey.class).add(ex).list();
                }
            };
            return (List) getHibernateTemplate().execute(callback);
        }*/
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.SurveyDAO#getSurvey(Long id)
     */
    public Survey getSurvey(final Long id) {
        Survey survey = (Survey) getHibernateTemplate().get(Survey.class, id);
        
        
        
        if (survey == null) {
            log.warn("uh oh, survey with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(Survey.class, id);
        }

        return survey;
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.SurveyDAO#saveSurvey(Survey survey, boolean updateCheckPoint)
     */    
    public void saveSurvey(final Survey survey, boolean updateCheckPoint) {
    	
    	if(survey == null) {
    		System.out.println("Survey is null!");
    	}
    	
    	
    	if(updateCheckPoint) {
    		//add a checkpoint ; 
    		survey.setCheckPoint(getNextCheckPoint());
    	}
    	
    	
    	//if it is deleted then delete the questions
    	if(survey.isDeleted() && survey.getQuestions() != null){
    		Iterator itr = survey.getQuestions().iterator() ;
    		Question question = null ; 
    		while(itr.hasNext()){
    			question = (Question) itr.next() ;
    			question.setDeleted(true);
    			
    		}
    		
    		
    	}
    	
    	
    	
    	getHibernateTemplate().saveOrUpdate(survey);
        
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.SurveyDAO#removeSurvey(Long id)
     */
    public void removeSurvey(final Long id) {
        //getHibernateTemplate().delete(getSurvey(id));
    	//soft delete!
    	Survey s = getSurvey(id) ; 
    	s.setDeleted(true) ;
    	saveSurvey(s, true);
    }


	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.dao.SurveyDAO#getSurveysByDate(uk.co.bluetrail.mobriz.model.User)
	 */
	public List getSurveysByDate(User user) {
		 
	    	 return getHibernateTemplate().find("from Survey s where s.surveyStatus !='X' order by created_on desc ");

	        /* Remove the line above and uncomment this code block if you want 
	           to use Hibernate's Query by Example API.
	        if (survey == null) {
	            return getHibernateTemplate().find("from Survey");
	        } else {
	            // filter on properties set in the survey
	            HibernateCallback callback = new HibernateCallback() {
	                public Object doInHibernate(Session session) throws HibernateException {
	                    Example ex = Example.create(survey).ignoreCase().enableLike(MatchMode.ANYWHERE);
	                    return session.createCriteria(Survey.class).add(ex).list();
	                }
	            };
	            return (List) getHibernateTemplate().execute(callback);
	        }*/
	    }


	public List getSurveyNamedQuery(String queryName) {
	
		User user = getCurrentUser() ;
		
		
		
		List  namedQueryResults = getHibernateTemplate().findByNamedQueryAndNamedParam(queryName, "account_id" ,user.getAccount_id());
		
		return namedQueryResults;
		
	}

	public List getSurveysByExample(final Survey survey) {
	
		
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Example ex = Example.create(survey).ignoreCase().enableLike(MatchMode.ANYWHERE);
                return session.createCriteria(Survey.class).add(ex).addOrder(Order.desc("created_on")).list();
            }
        };
       return (List) getHibernateTemplate().execute(callback);
    
		
	}

	public List getLiveSurveys() {
		 return getHibernateTemplate().find("from Survey s where s.surveyStatus !='X' and s.visibility != 0  ");
		
	}

    public List getSurveyNamedQuery(String queryName,String[] fieldNames, Object[] objects) {




        List  namedQueryResults = getHibernateTemplate().findByNamedQueryAndNamedParam(queryName,  fieldNames, objects);

        return namedQueryResults;

    }




}
