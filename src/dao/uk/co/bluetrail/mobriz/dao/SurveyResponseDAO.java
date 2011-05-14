
package uk.co.bluetrail.mobriz.dao;

import java.util.List;

import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;

public interface SurveyResponseDAO extends Dao {

    /**
     * Retrieves all of the surveyResponses
     */
    public List getSurveyResponses(SurveyResponse surveyResponse);

    /**
     * Gets surveyResponse's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the surveyResponse's id
     * @return surveyResponse populated surveyResponse object
     */
    public SurveyResponse getSurveyResponse(final Long id);

    /**
     * Saves a surveyResponse's information
     * @param surveyResponse the object to be saved
     */	
    public void saveSurveyResponse(SurveyResponse surveyResponse);

	/**
     * Removes a surveyResponse from the database by id
     * @param id the surveyResponse's id
     */
    public void removeSurveyResponse(final Long id);
    
    
    /**
     * soft deletes surveyResponses by surveyid  
     * @param id the surveyResponse's id
     */
    public void removeSurveyResponses(final Long id);

	public List getSurveyResponses(User currentUser);
	
	public List getSurveyResponsesByExample(final SurveyResponse surveyResponse) ;

	public List getSurveyResponsesGreaterThanId(Survey survey, Long lastPhotoStampResponse_id);

	public List getSurveyResponsesByExample(final SurveyResponse surveyResponse, final int Limit) ;
	    
	public List getSurveyResponses(Survey survey, User currentUser);
}

