
package uk.co.bluetrail.mobriz.dao;

import java.util.List;

import uk.co.bluetrail.mobriz.model.Question;
import uk.co.bluetrail.mobriz.model.User;

public interface QuestionDAO extends Dao {

	public List getUpdated(Long ckp, boolean deleted, User newParam);
	/**
     * Retrieves all of the for a sid
     */
	public List getQuestions(Long sid);
	
	public Long getNextCheckPoint() ;
		
	
    /**
     * Retrieves all of the questions
     */
    public List getQuestions(Question question);

    /**
     * Gets question's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the question's id
     * @return question populated question object
     */
    public Question getQuestion(final Long id);

    /**
     * Saves a question's information
     * @param question the object to be saved
     */	
    public void saveQuestion(Question question);

	/**
     * Removes a question from the database by id
     * @param id the question's id
     */
    public void removeQuestion(final Long id);
	/**
	 * @param id
	 * @return
	 */
	public Question getPrevQuestion(Long id);
	public Question getNextQuestion(Long id);
	
	public void removeBranchesToQuestion(Long id);
	/**
	 * @param sid
	 * @return
	 */
	public List getQuestions_by_date(Long sid);
	
}

