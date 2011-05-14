/**
 * 
 */
package uk.co.bluetrail.mobriz.webapp.ajax;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.springframework.context.MessageSource;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Question;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.ConstraintManager;
import uk.co.bluetrail.mobriz.service.OptionManager;
import uk.co.bluetrail.mobriz.service.QuestionManager;
import uk.co.bluetrail.mobriz.service.SurveyManager;
import uk.co.bluetrail.mobriz.util.ConvertUtil;
import uk.co.bluetrail.mobriz.util.CurrencyConverter;
import uk.co.bluetrail.mobriz.util.DateConverter;
import uk.co.bluetrail.mobriz.util.TimestampConverter;
import uk.co.bluetrail.mobriz.webapp.ajaxDTO.QuestionAjaxDTO;
import uk.co.bluetrail.mobriz.webapp.form.QuestionForm;

import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;
//


/**
 * @author Richard Spence
 *
 */
public interface AjaxSurveyEditManager {
	 public void setMessageSource(MessageSource messageSource) ;
	  
	
	public QuestionAjaxDTO saveInsertQuestion(QuestionAjaxDTO questionDTO) throws Exception ; 
	
	public void setConstraintManager(ConstraintManager constraintManager) ;		
	public void setOptionManager(OptionManager optionManager);	
	public void setQuestionManager(QuestionManager questionManager) ;		
	public void setSurveyManager(SurveyManager surveyManager);
	
	
	public QuestionAjaxDTO updateQuestion(QuestionAjaxDTO questionDTO) throws Exception;
	
	public QuestionAjaxDTO updateSurvey(QuestionAjaxDTO questionDTO) throws Exception;
	
	public String deleteQuestion(QuestionAjaxDTO questionDTO) throws Exception;
	
	/**
     * @see uk.co.bluetrail.mobriz.util.ConvertUtil#convert(java.lang.Object)
     */
   
    //gets all the questions for a survey as an array
	public HashMap getQuestions(String surveyId) throws Exception ;	
	//grabs an individual question using a qid
	public QuestionAjaxDTO getQuestion(String qid) throws Exception ;	
	
	
	/**
     * Convenience method to get the userForm from the session
     *
     * @param session the current user's session
     * @return the user's populated form from the session
     */
    public User getUser(HttpSession session) ;
	public QuestionAjaxDTO saveNewQuestion(QuestionAjaxDTO questionDTO) throws Exception ;
}
