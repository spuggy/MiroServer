
package uk.co.bluetrail.mobriz.dao;

import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.User;

import java.util.List;

public interface SurveyDAO extends Dao {

		
	
	public List getSurveyNamedQuery(String queryName) ;
	

    /**
     * Retrieves all of the surveys
     */
    public List getSurveys(Survey survey);

    /**
     * Gets survey's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the survey's id
     * @return survey populated survey object
     */
    public Survey getSurvey(final Long id);

    /**
     * Saves a survey's information
     * @param survey the object to be saved
     * @param updateCheckPoint
     */	
    public void saveSurvey(Survey survey, boolean updateCheckPoint);

	/**
     * Removes a survey from the database by id
     * @param id the survey's id
     */
    public void removeSurvey(final Long id);

	/**
	 * @param user
	 * @return
	 */
	public List getSurveysByDate(User user);
	
	public List getSurveysByExample(final Survey survey) ;


	public List getLiveSurveys();


    public List getSurveyNamedQuery(String queryName,String[] fieldNames, Object[] objects) ;


}

