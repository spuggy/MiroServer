
package uk.co.bluetrail.mobriz.service;

import java.util.List;

import uk.co.bluetrail.mobriz.model.Question;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.serviceDTO.QuestionServiceDTO;
import uk.co.bluetrail.mobriz.dao.*;

public interface QuestionManager extends Manager {

	
	public void saveNewQuestion(QuestionServiceDTO questionDTO);
	
	public void saveInsertQuestion(QuestionServiceDTO questionDTO);
	
	public Long getNextCheckPoint() ;
	
	public List getUpdated(Long ckp, boolean deleted, User newParam);
	
	public List getQuestions(Long sid);     
	
	public List getQuestions_by_date(Long sid) ;
		
    /**
     * Setter for DAO, convenient for unit testing
     */
    public void setQuestionDAO(QuestionDAO questionDAO);
    public void setConstraintDAO(ConstraintDAO constraintDAO);
    public void setOptionDAO(OptionDAO optionDAO);
    public void setSurveyDAO(SurveyDAO surveyDAO);

    /**
     * Retrieves all of the questions
     */
    public List getQuestions(Question question);

    /**
     * Gets question's information based on id.
     * @param id the question's id
     * @return question populated question object
     */
    public Question getQuestion(final String id);

    /**
     * Saves a question's information
     * @param question the object to be saved
     */
    public void saveQuestion(Question question, User user);

    
    public void updateQuestion(QuestionServiceDTO questionDTO) throws Exception;
    
    /**
     * Removes a question from the database by id
     * @param id the question's id
     */
    public void removeQuestion(QuestionServiceDTO questionDTO);
    public void removeQuestion(final String id); //old one not used
    
   
    
}

