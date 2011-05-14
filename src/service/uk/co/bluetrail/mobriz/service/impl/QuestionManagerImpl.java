
package uk.co.bluetrail.mobriz.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import uk.co.bluetrail.mobriz.model.Constraint;
import uk.co.bluetrail.mobriz.model.Option;
import uk.co.bluetrail.mobriz.model.Question;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyElement;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.ConstraintDAO;
import uk.co.bluetrail.mobriz.dao.OptionDAO;
import uk.co.bluetrail.mobriz.dao.QuestionDAO;
import uk.co.bluetrail.mobriz.dao.SurveyDAO;
import uk.co.bluetrail.mobriz.service.ConstraintManager;
import uk.co.bluetrail.mobriz.service.OptionManager;
import uk.co.bluetrail.mobriz.service.QuestionManager;
import uk.co.bluetrail.mobriz.service.SurveyManager;
import uk.co.bluetrail.mobriz.serviceDTO.QuestionServiceDTO;
import uk.co.bluetrail.mobriz.util.SurveyElementUtil;

public class QuestionManagerImpl extends BaseManager implements QuestionManager {
    private QuestionDAO dao;
	private SurveyDAO surveyDAO;
	private OptionDAO optionDAO;
	private ConstraintDAO  constraintDAO;
    public List getUpdated(Long ckp, boolean deleted, User newParam){
		return dao.getUpdated(ckp, deleted, null);
	}
    
    public Long getNextCheckPoint() {
    	return dao.getNextCheckPoint();
    }
    
    
    public List getQuestions(Long sid){
    	return dao.getQuestions(sid);
    }
    
    /**
     * Set the DAO for communication with the data layer.
     * @param dao
     */
    public void setQuestionDAO(QuestionDAO dao) {
        this.dao = dao;
    }
    
    
    /**
     * @see uk.co.bluetrail.mobriz.service.QuestionManager#getQuestions(uk.co.bluetrail.mobriz.model.Question)
     */
    public List getQuestions(final Question question) {
    	return dao.getQuestions(question);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.QuestionManager#getQuestion(String id)
     */
    public Question getQuestion(final String id) {
        return dao.getQuestion(new Long(id));
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.QuestionManager#saveQuestion(Question question)
     */
    public void saveQuestion(Question question, User user) {
    	SurveyElementUtil.timeStamp((SurveyElement) question, user);    	

        dao.saveQuestion(question);
             
        
    }

    private void saveOptions(QuestionServiceDTO questionDTO)  {
    //save the options
    	List options = questionDTO.getOptionList() ; 
    	if(options != null) {
    		Iterator itr = options.iterator() ;
    		while(itr.hasNext()){
    			Option option = (Option) itr.next() ; 
    			option.setQuestion_id(questionDTO.getQuestion().getId());
    			saveUpdateOption(option,questionDTO.getUser());
    		}
    	}
    }
    
    private void saveConstraints(QuestionServiceDTO questionDTO)  {
    	
    	//save constraints
    	List constraints = questionDTO.getConstraintList() ; 
    	if(constraints != null) {
    		Iterator itr = constraints.iterator() ;
    		while(itr.hasNext()){
    			Constraint constraint = (Constraint) itr.next() ; 
    			constraint.setQuestion_id(questionDTO.getQuestion().getId());
    			saveUpdateConstraint(constraint,questionDTO.getUser());
    		}   		
    	}
    	
    	
    }
    
    
    public void saveNewQuestion(QuestionServiceDTO questionDTO){
    	
    	
    	
    	//stamp the new question and save it
    	saveUpdateQuestion(questionDTO.getQuestion(),questionDTO.getUser());
        
    
    	
    	saveOptions(questionDTO);
    	saveConstraints(questionDTO);
    	
    
    	
    	Survey survey = null ; 
    	
    	
    	
    	//if we have a target question then point at that.  Otherwise we must have the first question 
    	//so update the first question link in that.
    	if(!questionDTO.getTargetId().equals("")){
    
	    	//get the previous question and point it at the new one.
    		Question prevQuestion = dao.getQuestion(new Long(questionDTO.getTargetId()));
	     	prevQuestion.setJQuestion_id(questionDTO.getQuestion().getId());
	     	survey = surveyDAO.getSurvey(new Long(questionDTO.getQuestion().getSurvey_id()+""));	    	
	     	saveUpdateQuestion(prevQuestion,questionDTO.getUser());
	     	saveUpdateSurvey(survey,questionDTO.getUser());
    	} else {
    	
    		survey = surveyDAO.getSurvey(new Long(questionDTO.getQuestion().getSurvey_id()+""));
    		survey.setFirstQuestion_id(questionDTO.getQuestion().getId());
    		saveUpdateSurvey(survey,questionDTO.getUser());
    	}
    	
    	
    	
    	
    }
    
    
//  saves and stamps an constraint
    private void saveUpdateConstraint(Constraint constraint,User user){
    	SurveyElementUtil.timeStamp((SurveyElement) constraint, user);    
    	constraintDAO.saveConstraint(constraint);
    }
    

    
    //saves and stamps an option
    private void saveUpdateOption(Option option,User user){
    	SurveyElementUtil.timeStamp((SurveyElement) option, user);    
    	optionDAO.saveOption(option);
    }
    
    
    
    //saves and stamps a question
    private void saveUpdateQuestion(Question question,User user){
    	SurveyElementUtil.timeStamp((SurveyElement) question, user);    
    	dao.saveQuestion(question);
    }
    
    //saves and stamps a survey
    private void saveUpdateSurvey(Survey survey, User user){
   		SurveyElementUtil.timeStamp((SurveyElement) survey, user);    	
    	surveyDAO.saveSurvey(survey, true);       	
    }
    
    /**
     * @see uk.co.bluetrail.mobriz.service.QuestionManager#removeQuestion(String id)
     */
    public void removeQuestion(QuestionServiceDTO questionDTO) {
    
    	 
		
    	
   
    	
    	Question question = questionDTO.getQuestion() ;
    	
    	Question prevQuestion = dao.getPrevQuestion(question.getId());
    	Question nextQuestion = dao.getNextQuestion(question.getJQuestion_id());
    	Survey survey = surveyDAO.getSurvey(question.getSurvey_id());
    	
    	questionDTO.setTargetId(question.getId().toString());
    	
    
    	
    	
    	//if prev question and next question are null we are deletein the first question 
    	// of a survey with only one question
    	if(prevQuestion == null && nextQuestion ==null) {
    		survey.setFirstQuestion_id(new Long(0));	
    		saveUpdateSurvey(survey,questionDTO.getUser());
    		questionDTO.setQuestion(null);
    		questionDTO.setQuestion(null); //we have deleted everything
    	} 
    	
    	
    	
    	//prev is null but there are more questions so we are at the first question again
    	//but this time there are more questions.
    	if(prevQuestion == null && nextQuestion !=null) {
    		survey.setFirstQuestion_id(nextQuestion.getId());	
    		saveUpdateSurvey(survey,questionDTO.getUser());
    		questionDTO.setQuestion(null); //we are using th question to update the last question in the UI
    	} 
    	
    	
    	
    	//we are deleting the last question!
    	if(prevQuestion != null && nextQuestion == null){
    		prevQuestion.setJQuestion_id(new Long(0));	
    		saveUpdateQuestion(prevQuestion,questionDTO.getUser());
    		questionDTO.setQuestion(prevQuestion); 
    	}
    	
    	
    	
    	//normal question in the middle
    	if(prevQuestion !=null && nextQuestion != null){
    		prevQuestion.setJQuestion_id(nextQuestion.getId());	
    		saveUpdateQuestion(prevQuestion,questionDTO.getUser());
    		questionDTO.setQuestion(null); //we are using th question to update the last question in the UI
    	}


    	//some questions and branches to this question - we need set those branches to null/0
    	removeBranchesToQuestion(question.getId());
    	
    	    	
    	//now we have re pointed everything delete the bloody question;
        dao.removeQuestion(question.getId());
        
        
        
        
        
        //finally got gett he survey again and update
        survey = surveyDAO.getSurvey(question.getSurvey_id());
    	saveUpdateSurvey(survey,questionDTO.getUser());
    	
    
    }

	
	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.service.QuestionManager#setSurveyDAO(uk.co.bluetrail.mobriz.dao.SurveyDAO)
	 */
	public void setSurveyDAO(SurveyDAO surveyDAO) {
		this.surveyDAO = surveyDAO;	
	}
	public void setConstraintDAO(ConstraintDAO constraintDAO) {
		this.constraintDAO = constraintDAO;	
	}
	public void setOptionDAO(OptionDAO optionDAO) {
		this.optionDAO = optionDAO;	
	}

	
	
	

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.service.QuestionManager#removeQuestion(java.lang.Long)
	 */
	public void removeQuestion(final String id) {
		// TODO Auto-generated method stub
		
	}

	
	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.service.QuestionManager#updateQuestion(uk.co.bluetrail.mobriz.serviceDTO.QuestionServiceDTO)
	 */
	public void updateQuestion(QuestionServiceDTO questionDTO) throws Exception {
		
	 	 
		
//		//stamp the new question and save it
    	saveUpdateQuestion(questionDTO.getQuestion(),questionDTO.getUser());     
    	
    	Question updatedQuesion = (Question) dao.getQuestion(questionDTO.getQuestion().getId());
    	
    	saveOptions(questionDTO) ;
    	saveConstraints(questionDTO);
    	
    	Survey survey = surveyDAO.getSurvey(new Long(questionDTO.getQuestion().getSurvey_id()+""));
 	   	saveUpdateSurvey(survey,questionDTO.getUser());
        
    			
	}

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.service.QuestionManager#saveInsertQuestion(uk.co.bluetrail.mobriz.serviceDTO.QuestionServiceDTO)
	 */
	public void saveInsertQuestion(QuestionServiceDTO questionDTO) {
		
		Question question = questionDTO.getQuestion() ;
    
//		stamp the new question and save it
    	saveUpdateQuestion(question,questionDTO.getUser());
        
    	saveOptions(questionDTO);
    	saveConstraints(questionDTO);
    	
		
		//now work out where to point it
    	    	
    	Long target= new Long(questionDTO.getTargetId());
    	
    	Question prevQuestion = dao.getPrevQuestion(target);
    	Question nextQuestion = dao.getQuestion(target);
    	Survey survey = surveyDAO.getSurvey(question.getSurvey_id());
    	
    	
    	
    	//prev is null but there are more questions so we are at the first question again
    	//but this time there are more questions.
    	if(prevQuestion == null && nextQuestion !=null) {
    		survey.setFirstQuestion_id(question.getId());	
    		saveUpdateSurvey(survey,questionDTO.getUser());
    		
    		question.setJQuestion_id(nextQuestion.getId());
    		saveUpdateQuestion(question,questionDTO.getUser());
    	} 
    	
    	
    	
    	
    	
    	
    	//normal question in the middle
    	if(prevQuestion !=null && nextQuestion != null){
    		prevQuestion.setJQuestion_id(question.getId());	
    		saveUpdateQuestion(prevQuestion,questionDTO.getUser());    		 
    		question.setJQuestion_id(nextQuestion.getId());
    		saveUpdateQuestion(question,questionDTO.getUser());
    	}
    	
    	//this may be repeating stuff but I want to make sure survey is the last thig to happen
    	survey = surveyDAO.getSurvey(new Long(questionDTO.getQuestion().getSurvey_id()+""));
    	saveUpdateSurvey(survey,questionDTO.getUser());
    	 
    	
		
	}

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.service.QuestionManager#removeBranchesToQuestion(java.lang.String)
	 * 
	 * sets any questions that are pointed at by the id to 0 - this is to remove branches top unsed questions * 
	 * 
	 */
	private void removeBranchesToQuestion(Long id ){
		
		dao.removeBranchesToQuestion(id) ;
		optionDAO.removeBranchesToQuestion(id);
		
	}

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.service.QuestionManager#getQuestions_by_date(java.lang.Long)
	 */
	public List getQuestions_by_date(Long sid) {
		return dao.getQuestions_by_date(sid);
	}
    
    
}
