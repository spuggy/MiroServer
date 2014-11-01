
package uk.co.bluetrail.mobriz.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.SurveyResponseDAO;

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

public class SurveyResponseDAOHibernate extends BaseDaoHibernate implements SurveyResponseDAO {

    /**
     * @see uk.co.bluetrail.mobriz.dao.SurveyResponseDAO#getSurveyResponses(uk.co.bluetrail.mobriz.model.SurveyResponse)
     */
    public List getSurveyResponses(final SurveyResponse surveyResponse) {
        //return getHibernateTemplate().find("from SurveyResponse");

    	return getHibernateTemplate().find("from SurveyResponse sr where sr.survey_id = " + surveyResponse.getSurvey_id().toString() + " and sr.deleted = false  order by created_on desc");
        /*
    	
    	 if (surveyResponse == null) {
    		 return getHibernateTemplate().find("from SurveyResponse sr where sr.survey_id = " + surveyResponse.getSurvey_id().toString() + " order by created_on desc");
    	  } else {
            // filter on properties set in the surveyResponse
            HibernateCallback callback = new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Example ex = Example.create(surveyResponse).ignoreCase().enableLike(MatchMode.ANYWHERE);
                            return session.createCriteria(SurveyResponse.class).add(ex).list();
                }
            };
            return (List) getHibernateTemplate().execute(callback);
        }
        */
    }

    public List getSurveyResponsesByExample(final SurveyResponse surveyResponse) {
        
    	
    	 
            // filter on properties set in the surveyResponse
            HibernateCallback callback = new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Example ex = Example.create(surveyResponse).ignoreCase().enableLike(MatchMode.ANYWHERE);
                            return session.createCriteria(SurveyResponse.class).add(ex).list();
                }
            };
           return (List) getHibernateTemplate().execute(callback);
        
       
    }
    
    
    
    
    /**
     * @see uk.co.bluetrail.mobriz.dao.SurveyResponseDAO#getSurveyResponse(Long id)
     */
    public SurveyResponse getSurveyResponse(final Long id) {
        SurveyResponse surveyResponse = (SurveyResponse) getHibernateTemplate().get(SurveyResponse.class, id);
        
        
        if (surveyResponse == null) {
            log.warn("uh oh, surveyResponse with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(SurveyResponse.class, id);
        }

        return surveyResponse;
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.SurveyResponseDAO#saveSurveyResponse(SurveyResponse surveyResponse)
     */    
    public void saveSurveyResponse(final SurveyResponse surveyResponse) {
        getHibernateTemplate().saveOrUpdate(surveyResponse);
    }

    /**
     * @see uk.co.bluetrail.mobriz.dao.SurveyResponseDAO#removeSurveyResponse(Long id)
     */
    public void removeSurveyResponse(final Long id) {
        getHibernateTemplate().delete(getSurveyResponse(id));
    }

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.dao.SurveyResponseDAO#removeSurveyResponses(java.lang.Long)
	 */
	public void removeSurveyResponses(final Long survey_id) {
		
        String q = "update SurveyResponse set deleted = true where survey_id = " + survey_id.toString();		             

        Session s = getSession() ; 
        
        if(s ==null) {
        	return ;
        }
        
        Query query = s.createQuery(q) ; 
        
        if(query ==null) {        	
        	return ;
        }
        
                
        query.executeUpdate() ;
        
		
	    
	}

	public List getSurveyResponses(User currentUser) {
		
    	return getHibernateTemplate().find("from SurveyResponse sr where sr.createdBy_id = " + currentUser.getId().toString() + " and sr.deleted = false  order by created_on desc");
        
	}

    public List getUnprocessedResponses(final int limit) {

        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {

                Criteria crit = session.createCriteria(SurveyResponse.class);

                crit.add(Expression.eq("alertsProcessed",false));

                crit.addOrder(Order.asc("id"));

                crit.setFetchSize(limit);

                return  crit.list();

            }
        };

        return (List) getHibernateTemplate().execute(callback);
    }


	public List getSurveyResponsesGreaterThanId(final Long id,final int limit) {
		 HibernateCallback callback = new HibernateCallback() {
	 	     	public Object doInHibernate(Session session) throws HibernateException, SQLException {
	 	         	 
	 	             Criteria crit = session.createCriteria(SurveyResponse.class);
	 	            

	 	            crit.add(Expression.eq("deleted",false));
	 	            	          
	 	            crit.add(Expression.gt("id", id));
	 	             
	 	           crit.addOrder(Order.asc("id"));

                    crit.setFetchSize(limit);

	 	             return  crit.list();
	 	             
	 	         }
	 	     };
	 		
	 	     return (List) getHibernateTemplate().execute(callback);
	}

	public List getSurveyResponsesByExample(final SurveyResponse surveyResponse, final int limit) {
//		 filter on properties set in the surveyResponse
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Example ex = Example.create(surveyResponse).ignoreCase().enableLike(MatchMode.ANYWHERE);
                Criteria crit = session.createCriteria(SurveyResponse.class).add(ex);
                crit.setFetchSize(limit);
                        return crit.list();
            }
        };
       return (List) getHibernateTemplate().execute(callback);
    
	}
	
	

	public List getSurveyResponses(Survey survey, User currentUser) {
		
    	return getHibernateTemplate().find("from SurveyResponse sr where sr.createdBy_id = " + currentUser.getId().toString() + " and sr.deleted = false and sr.survey_id = " + survey.getId() + "  order by created_on desc");
      
	}
	
	
	
}
