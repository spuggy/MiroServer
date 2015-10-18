package uk.co.bluetrail.mobriz.model;

import java.util.Date;

/**
 * MiroProject class
 * 
 * Defines a question for a survey
 * 
 * <p><a href="MiroProject.java.html"><i>View Source</i></a></p>
 * 
 * @author <a href="mailto:rspence@bluetrail.co.uk">Richard Spence</a>
 * 
 * @hibernate.class table="mr.miroprojects"
 */
public class MiroProject extends BaseObject implements SurveyElement {

	private Long id;
	private String projectTitle ;
	private String projectDescription;
	private String emailInviteSubject ;
	private String emailInviteText ;
	private Integer projectStatus = OPEN_PROJECT ;
	private String costcode ;
	
	static public Integer OPEN_PROJECT = 1 ;
	static public Integer CLOSED_PROJECT = 2 ;
	static public Integer DELETED_PROJECT = 3 ;
	
		
	private Long checkPoint = null;
	private Long lastUpdatedBy_id = null;
	private Date updated_at = null;
	private Long createdBy_id = null;
	private Date created_on = null;
	protected boolean bccPractitioner; //send a bcc of invites email to the practitioner
	private Integer version;
	private User practitioner;

	/**
	 * @hibernate.property column="bcc_practitioner" type="yes_no"
	 */
	public boolean isBccPractitioner() {
		return bccPractitioner;
	}

	public void setBccPractitioner(boolean bccPractitioner) {
		this.bccPractitioner = bccPractitioner;
	}


	/**
	 * @return Returns the practitioner. mapped in external matadata/dao file
	 */
	public User getPractitioner() {
		return this.practitioner;
	}

	/**
	 * @param practitioner
	 *   The practitioner to set.
	 */
	public void setPractitioner(User practitioner) {
		this.practitioner = practitioner;
	}
	
	/**
	 * @hibernate.property length="50" not-null="true"
	 * @return the costcode
	 */
	public String getCostcode() {
		if(costcode==null) {
			return "";
		}
		return costcode;
	}

	/**
	 * @param costcode the costcode to set
	 * @spring.validator type="required"
	 */
	public void setCostcode(String costcode) {
		if(costcode !=null && costcode.length()> 21) {
			this.costcode = costcode.substring(0,20);
		} else {
			this.costcode = costcode;
		}
		
	}

	/**
	 * @hibernate.property length="50" not-null="true"
	 * @return the emailInviteSubject
	 */
	public String getEmailInviteSubject() {
		return emailInviteSubject;
	}

	/**
	 * @param emailInviteSubject the emailInviteSubject to set
	 */
	public void setEmailInviteSubject(String emailInviteSubject) {
		this.emailInviteSubject = emailInviteSubject;
	}

	/**
	 * @hibernate.property length="100" not-null="true"
	 * @return the emailInviteText
	 */
	public String getEmailInviteText() {
		return emailInviteText;
	}

	/**
	 * @param emailInviteText the emailInviteText to set
	 * @spring.validator type="required"
	 * 
	 */
	public void setEmailInviteText(String emailInviteText) {
		this.emailInviteText = emailInviteText;
	}

	/**
	 * @hibernate.property length="254" not-null="true"
	 * @return the projectDescription
	 */
	public String getProjectDescription() {
		return projectDescription;
	}

	/**
	 * @param projectDescription the projectDescription to set
	 */
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	/**
	 * @hibernate.property 
	 * @return the projectStatus
	 */
	public Integer getProjectStatus() {
		return projectStatus;
	}

	/**
	 * @param projectStatus the projectStatus to set
	 */
	public void setProjectStatus(Integer projectStatus) {
		this.projectStatus = projectStatus;
	}

	/**
	 * @hibernate.property length="100" not-null="true"
	 * @return the projectTitle
	 */
	public String getProjectTitle() {
		return projectTitle;
	}

	/**
	 * @param projectTitle the projectTitle to set
	 * @spring.validator type="required"
	 */
	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	/**
	 * @return Returns the id.
	 * @hibernate.id column="id" generator-class="increment"
	 *               unsaved-value="null"
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object o) {

		if (id.compareTo((Long) o) == 0) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public int hashCode() {

		return id.hashCode();
	}

	@Override
	public String toString() {

		return id + " - " + this.projectTitle;
	}

	/**
	 * @hibernate.property
	 */
	public Long getCheckPoint() {
		return this.checkPoint;
	}

	/**
	 * @hibernate.property
	 */
	public Long getCreatedBy_id() {
		return this.createdBy_id;
	}

	/**
	 * @hibernate.property
	 */
	public Date getCreated_on() {
		return this.created_on;
	}

	/**
	 * @hibernate.property
	 */
	public Long getLastUpdatedBy_id() {

		return this.lastUpdatedBy_id;
	}

	/**
	 * @hibernate.property
	 */
	public Date getUpdated_at() {
		return this.updated_at;
	}

	/**
	 * @hibernate.property
	 */
	public Integer getVersion() {
		return this.version;
	}

	/**
	 */
	public boolean isDeleted() {

		return this.projectStatus.intValue() == MiroProject.DELETED_PROJECT.intValue();

	}

	public boolean isNew() {
		if (version == null) {
			return true;
		}
		if (version.compareTo(new Integer(0)) == 0) {
			return true;
		} else {
			return false;
		}
	}

	public void setCheckPoint(Long checkPoint) {
		this.checkPoint = checkPoint;
	}

	public void setCreatedBy_id(Long createdBy_id) {
		this.createdBy_id = createdBy_id;

	}

	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}

	public void setDeleted(boolean deleted) {
		this.projectStatus = MiroProject.DELETED_PROJECT;
	}

	public void setLastUpdatedBy_id(Long lastUpdatedBy_id) {
		this.lastUpdatedBy_id = lastUpdatedBy_id;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;

	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	
}
