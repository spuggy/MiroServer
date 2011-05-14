/**
 * 
 */
package uk.co.bluetrail.mobriz.webapp.ajaxDTO;

import java.util.ArrayList;

import uk.co.bluetrail.mobriz.webapp.form.*;

/**
 * @author Richard Spence
 *
 * Used to transfer questions, constraints and options up and down.
 * probably could have munged into one classic question object but 
 * did not trust the dwr stuff to tranlate everyting
 *
 */
public class QuestionAjaxDTO {

	
	private String targetId = null;
	private SurveyForm sForm = null;
	private QuestionForm qForm = null ; 
	private OptionForm[] oForms = null ;	
	private ConstraintForm cForm = null;
	private ArrayList errorMessages = null ;
	private int status = 0 ; //0 is error ;
	private int liveOptionCount = 0 ;

	
	
	/**
	 * @return Returns the optionCount.
	 */
	public int getLiveOptionCount() {
		return this.liveOptionCount;
	}


	/**
	 * @param optionCount The optionCount to set.
	 */
	public void setOptionCount(int optionCount) {
		this.liveOptionCount = optionCount;
	}


	public void addErrorMessage(String errormessage) {
		if(errorMessages ==null ) {
			errorMessages = new ArrayList();
		}
		
		errorMessages.add(errorMessages);	
	}
	
	
	/**
	 * @return Returns the errorMessages.
	 */
	public ArrayList getErrorMessages() {
		return this.errorMessages;
	}

	/**
	 * @param errorMessages The errorMessages to set.
	 */
	public void setErrorMessages(ArrayList errorMessages) {
		this.errorMessages = errorMessages;
	}

	
	//no params constructto for beans
	public	QuestionAjaxDTO( ){			
	}
	
public	QuestionAjaxDTO(SurveyForm sForm, QuestionForm qForm  ,OptionForm[] oForms, ConstraintForm cForm ){
		super();
		this.sForm = sForm;
		this.qForm = qForm ; 
		this.oForms = oForms ; 
		this.cForm = cForm ;
		
		
}

/**
 * @return Returns the cForm.
 */
public ConstraintForm getCForm() {
	return this.cForm;
}

/**
 * @param form The cForm to set.
 */
public void setCForm(ConstraintForm form) {
	this.cForm = form;
}


/**
 * @return Returns the oForms.
 */
public OptionForm[] getOForms() {
	return this.oForms;
}

/**
 * @param forms The oForms to set.
 */
public void setOForms(OptionForm[] forms) {
	this.oForms = forms;
}

/**
 * @return Returns the qForm.
 */
public QuestionForm getQForm() {
	return this.qForm;
}

/**
 * @param form The qForm to set.
 */
public void setQForm(QuestionForm form) {
	this.qForm = form;
}

/**
 * @return Returns the status.
 */
public int getStatus() {
	return this.status;
}

/**
 * @param status The status to set.
 */
public void setStatus(int status) {
	this.status = status;
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
 * @return Returns the sForm.
 */
public SurveyForm getSForm() {
	return this.sForm;
}


/**
 * @param form The sForm to set.
 */
public void setSForm(SurveyForm form) {
	this.sForm = form;
}


/**
 * @param liveOptionCount The liveOptionCount to set.
 */
public void setLiveOptionCount(int liveOptionCount) {
	this.liveOptionCount = liveOptionCount;
}







}
