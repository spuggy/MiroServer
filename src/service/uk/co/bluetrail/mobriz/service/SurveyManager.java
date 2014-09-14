
package uk.co.bluetrail.mobriz.service;

import uk.co.bluetrail.mobriz.dao.OptionDAO;
import uk.co.bluetrail.mobriz.dao.QuestionDAO;
import uk.co.bluetrail.mobriz.dao.SurveyDAO;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.User;

import java.util.List;
import java.util.Vector;


public interface SurveyManager extends Manager {
	public Survey getSurveyAndFiles(final String id, String path) ;
	
	public List getVisibleSurveys(User user) ; 
	
	public List getSurveyNamedQuery(String queryName) ;
	
	
    /**
     * Setter for DAO, convenient for unit testing
     */
    public void setSurveyDAO(SurveyDAO surveyDAO);

    /**
     * Retrieves all of the surveys
     */
    public List getSurveys(Survey survey);

    /**
     * Gets survey's information based on id.
     * @param id the survey's id
     * @return survey populated survey object
     */
    public Survey getSurvey(final String id);

    /**
     * Saves a survey's information
     * @param survey the object to be saved
     */
    public void saveSurvey(Survey survey, User user);

    
    public void saveSurvey(Survey survey, String[] usersIds , User  user);

    
    
    /**
     * Removes a survey from the database by id
     * @param id the survey's id
     */
    public void removeSurvey(final String id);
    
    
    /**
     * 
     * takes a survey that is in draft and sets it to published.
     * note - in version 2.0 this will copy the survey!
     *
     * @return
     */
    public Long publishSurvey(Survey survey, User user) ; 
    
    public void setQuestionDAO(QuestionDAO questionDAO);

    public void setOptionDAO(OptionDAO optionDAO);

	/**
	 * @param user
	 * @return
	 */
	public List getSurveysByDate(User user);

	/**
	 * @param uploadedSurvey 
	 * @param fileLines
	 * @param user
	 */
	public void saveSurvey(Survey uploadedSurvey, Vector fileLines, User user);
	
	/*
	 * just save the survey - used for admin routines and batc processing
	 */
	public void saveSurvey(Survey survey) ;

	
	public List getLiveSurveys();
}

