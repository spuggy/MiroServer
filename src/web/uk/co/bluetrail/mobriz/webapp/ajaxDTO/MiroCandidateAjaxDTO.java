package uk.co.bluetrail.mobriz.webapp.ajaxDTO;

public class MiroCandidateAjaxDTO {

	private String id;
	private String firstName ;
	private String lastName ;
	private String emailAddress;
	private String project_id;
	private int status   ;
	private String reportStatus;
	private String errorMessage;
	private boolean testComplete;
	private String creditBalance;
	
	
	
	/**
	 * @hibernate.property 
	 * @return the creditBalance
	 */
	public String getCreditBalance() {
		return creditBalance;
	}

	/**
	 * @param creditBalance the creditBalance to set
	 */
	public void setCreditBalance(String creditBalance) {
		this.creditBalance = creditBalance;
	}

	/**
	 * @hibernate.property 
	 * @return the reportStatus
	 */
	public String getReportStatus() {
		return reportStatus;
	}

	/**
	 * @param reportStatus the reportStatus to set
	 */
	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @hibernate.property 
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	

	/**
	 * @hibernate.property 
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	
	/**
	 * @hibernate.property 
	 * @return the project_id
	 */
	public String getProject_id() {
		return project_id;
	}

	/**
	 * @param project_id the project_id to set
	 */
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public MiroCandidateAjaxDTO() {
		
	}
	
	public MiroCandidateAjaxDTO(Long id, Long project_id,String firstName, String lastName, String emailAddress, int status) {
		this.id = id.toString();
		this.project_id = project_id.toString();
		this.firstName = firstName.toString();
		this.lastName = lastName.toString();
		this.emailAddress = emailAddress.toString();
		this.reportStatus = status+"";
		
	}
	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	

	/**
	 * @hibernate.property 
	 * @return the testComplete
	 */
	public boolean isTestComplete() {
		return testComplete;
	}

	/**
	 * @param testComplete the testComplete to set
	 */
	public void setTestComplete(boolean testComplete) {
		this.testComplete = testComplete;
	}

	
	
	
	
	
}
