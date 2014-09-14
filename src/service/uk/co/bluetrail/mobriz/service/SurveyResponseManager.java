
package uk.co.bluetrail.mobriz.service;

import uk.co.bluetrail.mobriz.dao.SurveyResponseDAO;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;

import java.util.List;

public interface SurveyResponseManager extends Manager {

    /**
     * Setter for DAO, convenient for unit testing
     */
    public void setSurveyResponseDAO(SurveyResponseDAO surveyResponseDAO);

    /**
     * Retrieves all of the surveyResponses
     */
    public List getSurveyResponses(SurveyResponse surveyResponse);

    /**
     * Gets surveyResponse's information based on id.
     * @param id the surveyResponse's id
     * @return surveyResponse populated surveyResponse object
     */
    public SurveyResponse getSurveyResponse(final String id);

    /**
     * Saves a surveyResponse's information
     * @param surveyResponse the object to be saved
     */
    public void saveSurveyResponse(SurveyResponse surveyResponse);

    /**
     * Removes a surveyResponse from the database by id
     * @param id the surveyResponse's id
     */
    public void removeSurveyResponse(final String id);
    
    /**
     * Soft Deletes surveyResponses from the database by survey id
     * @param id the surveyResponse's id
     */
    public void removeSurveyResponses(final String id);

	public List getSurveyResponses(User currentUser);

	public List getResponsesByExample(SurveyResponse example);

	



	
	public boolean processAlerts(SurveyResponse sr, List alerts, List trueAlerts);


	public List getSurveyResponses(Survey survey, User currentUser);


	
}

