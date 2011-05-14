/**
 * 
 */
package uk.co.bluetrail.mobriz.serviceDTO;

import java.util.List;

import uk.co.bluetrail.mobriz.model.Question;
import uk.co.bluetrail.mobriz.model.User;

/**
 * @author Richard Spence
 *
 */
public class QuestionServiceDTO {

 	private Question question ;
	private String targetId ;
	private List optionList ;
	private List constraintList ;
	private User user ;
	
	
	public QuestionServiceDTO() {
		
	}
	
	public QuestionServiceDTO(Question question, String targetId , List optionList , List constraintList ,  User user ) {
		super();
		
		this.question = question ; 
		this.targetId = targetId ; 
		this.optionList  =optionList; 
		this.constraintList = constraintList ; 
		this.user = user;
		
		
		
	}
	
	
	
	
	/**
	 * @return Returns the question.
	 */
	public Question getQuestion() {
		return this.question;
	}

	/**
	 * @param question The question to set.
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}

	/**
	 * @return Returns the constraintList.
	 */
	public List getConstraintList() {
		return this.constraintList;
	}
	/**
	 * @param constraintList The constraintList to set.
	 */
	public void setConstraintList(List constraintList) {
		this.constraintList = constraintList;
	}
	/**
	 * @return Returns the optionList.
	 */
	public List getOptionList() {
		return this.optionList;
	}
	/**
	 * @param optionList The optionList to set.
	 */
	public void setOptionList(List optionList) {
		this.optionList = optionList;
	}
	/**
	 * @return Returns the targetId.
	 */
	public String getTargetId() {
		return this.targetId;
	}
	/**
	 * @param targetId The targetId to set.
	 */
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	/**
	 * @return Returns the user.
	 */
	public User getUser() {
		return this.user;
	}
	/**
	 * @param user The user to set.
	 */
	public void setUser(User user) {
		this.user = user;
	} 
	 
	
	
	
	
}
