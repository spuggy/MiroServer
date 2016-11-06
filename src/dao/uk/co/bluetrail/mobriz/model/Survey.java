/*
 * Created on 23-Jan-2006
 *
 */
package uk.co.bluetrail.mobriz.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Survey class
 * 
 * Defines a survey
 * 
 * <p><a href="Survey.java.html"><i>View Source</i></a></p>
 * 
 * @author <a href="mailto:rspence@bluetrail.co.uk">Richard Spence</a>
 * 
 * 
 * @struts.form include-all="false" extends="BaseForm"
 * @hibernate.class table="mr.surveys"
 * 
 */
public class Survey extends BaseObject implements SurveyElement {

	public static char LIVE = 'L' ;
	public static char DELETED = 'X' ;
	public static char RETIRED = 'R' ;
	public static char DRAFT = 'D' ;
	
	public static final int MAXLINES = 300;
	public static final String TITLE_LINE = "type\ttext\tqtype\trequired\tsticky\tshortname";
	private static final char SURVEY_TYPE = 's';
	public static int ALL = 1 ;
	public static int USERS = 2 ; 
	public static int HIDDEN = 0 ; 
	
	protected final Log log = LogFactory.getLog(getClass());
	private Long id ;
	private String title ;
	private Set questions ; 
	private HashMap questionMap ;
	
	private Long lastZipResponse_id  ;
		
	private Long checkPoint = null; 
	private Long lastUpdatedBy_id = null; 
	private Date updated_at = null ;	
	private Long createdBy_id = null;
	private Date created_on = null;
	protected Integer version;
	
	private File[] files = null ; // placeholder for associated files like photos 
	
	private Long firstQuestion_id ; 
	
	private Set users = new HashSet();
	private boolean common = true  ; //a public survey
	private Set surveyResponses ;
	
	private char surveyStatus = Survey.DRAFT  ; // the status of the survey life cycle D=draft , L = publish, R = retired, X=Dleted 
	private int visibility ; //show can see the survey 0 = public 1 = specifed users 
	private String surveyVersion ; //a string that identifies the survey version number. e.g 1.0 or sains01 

	private Long responseCount ;
	
	protected Long account_id;
    private Account account ;
	
    private String photoFile = null ;
	private Long lastPhotoStampResponse_id;
	private HashMap questionMapByShortName;
    
    /**
	 * @hibernate.property	 
	 */
    public String getPhotoFile() {
		return photoFile;
	}

	public void setPhotoFile(String photoFile) {
		this.photoFile = photoFile;
	}

	/**
	 * @hibernate.many-to-one column="account_id"
	 *                        class="uk.co.bluetrail.mobriz.model.Account"
	 * 
	 * insert="false" update="false" not-null="true"
	 * 
	 * @return Account - the account associated with this setting
	 */
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

    
	public String export() {
		StringBuffer lop = new StringBuffer() ;
		
		lop.append(Survey.SURVEY_TYPE) ;
		lop.append('\t');
		lop.append(getTitle()) ;
		
	
		
		
		return lop.toString();
		
	}
	
	
	
	/**
	 * 
	 * @param desc
	 * required to stop struts grumbling
	 * 
	 */
	public void setSurveyStatusDesc(String desc) {
		
	}

	
	
	/**
	 * @return Returns the surveyStatus.
	 * @hibernate.property
	 * @struts.form-field
	 *  
	 */
	public char getSurveyStatus() {
		return this.surveyStatus ; 
	}

	/**
	 * @param surveyStatus The surveyStatus to set.
	 */
	public void setSurveyStatus(char surveyStatus) {
		this.surveyStatus = surveyStatus;
	}

	/**
	 * @return Returns the surveyVersion.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public String getSurveyVersion() {
		return this.surveyVersion;
	}

	/**
	 * @param surveyVersion The surveyVersion to set.
	 */
	public void setSurveyVersion(String surveyVersion) {
		this.surveyVersion = surveyVersion;
	}

	/**
	 * @hibernate.property
	 * @struts.form-field
	 * @return Returns the surveyVisibility.
	 */
	public int getVisibility() {
		return this.visibility;
	}

	/**
	 * @param surveyVisibility The surveyVisibility to set.
	 */
	public void setVisibility(int surveyVisibility) {
		this.visibility = surveyVisibility;
	}

	public int getResponseCount() {
		
		return getSurveyResponses().size();
	}
	
	public HashMap getQuestionMap() {
		
		if(questionMap != null){
			return questionMap ;
		}
		
		questionMap = new HashMap();
		
		Iterator itr = questions.iterator() ; 
		
		Question q = null ; 
		while(itr.hasNext()){
			q = (Question) itr.next() ; 
			questionMap.put(q.getId(),q);
			
		}
		return questionMap;
	}
	
	
	/**
	 * @return Returns the responses
	 * @hibernate.set table="mr.responses" cascade="none" lazy="true"  inverse="true"
     * @hibernate.collection-key column="survey_id"
     * @hibernate.collection-one-to-many class="uk.co.bluetrail.mobriz.model.SurveyResponse"
	 */
	public Set getSurveyResponses() {
		return surveyResponses;
	}
	/**
	 * @param surveyResponses The surveyResponses to set.
	 */
	public void setSurveyResponses(Set surveyResponses) {
		this.surveyResponses = surveyResponses;
	}
	public boolean visibleToUser(User user){
		
		if(visibility == Survey.ALL){
			log.info("Visible to all" + getId());
			return true ; 
		}
		
		if(visibility == Survey.USERS){
		
			Iterator itr = getUsers().iterator() ;
			User userFound ; 
			while(itr.hasNext()){
				userFound = (User) itr.next();
				if(userFound.equals(user)){
					log.info("found user!!" + getId() + userFound.getUsername());
					return true;
				}
			}
		
			log.info("not found user!!" + getId() + user.getUsername());
			
		}
		
		
		//must be hidden so return false 		
		return false; 
	}
			
	/**
	 * @return Returns the common.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public boolean isCommon() {
		return common;
	}
	/**
	 * @param common The common to set.
	 */
	public void setCommon(boolean common) {
		this.common = common;
	}
	
	/**
     * Returns the user's roles.
     * 
     * @return Set
     * 
     * @hibernate.set table="mr.survey_user" cascade="all" lazy="false"
     * @hibernate.collection-key column="survey_id"
     * @hibernate.collection-many-to-many class="uk.co.bluetrail.mobriz.model.User"   column="user_id"
     * @struts.form-field
     */
	public Set getUsers() {
		return users;
	}
	
	/**
     * Adds a user for the survey
     *
     * @param role
     */
    public void addUser(User user) {
    	
    	users = getUsers();
    	
    	if(users==null) {
    		users = new HashSet();
    	}
    	
        users.add(user);
    }
	
	
	/**
	 * @param users The users to set.
	 */
	public void setUsers(Set users) {
		this.users = users;
	}
	/**
	 * @return Returns the firstQuestion_id.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public Long getFirstQuestion_id() {
		return firstQuestion_id;
	}
	/**
	 * @param firstQuestion_id The firstQuestion_id to set.
	 */
	public void setFirstQuestion_id(Long firstQuestion_id) {
		this.firstQuestion_id = firstQuestion_id;
	}
	public boolean isNew() {
		
		if(version == null) {
			return true;
		}
		
		if ( version.compareTo(new Integer(0))==0 ) {
			return true ; 
		} else {
			return false ; 
		}
	}
	
	
	/**
     * @return Returns the updated version.
     * this is not a version disabled for now
     * @hibernate.property
     * @struts.form-field
     */
	public Integer getVersion() {
		return version;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	
	
	
	
	public int getConstraintCount(){
		
		
		questions = getQuestions();
		
		if(questions == null){
			return 0 ;
		}
		
		
		Iterator itr = questions.iterator() ; 
		int constraintCount = 0;		
		Question question = null ; 
		
		while(itr.hasNext()){
			question = (Question) itr.next();
			
			if(!question.isDeleted()){
		
					constraintCount = constraintCount + question.getConstraintCount();
			} 			
				
		}
		
		
		return constraintCount;
	}
	public int getOptionCount(){
		questions = getQuestions();
		
		if(questions == null){
			return 0 ;
		}
		
		Iterator itr = questions.iterator() ; 
		int optionCount = 0;
		int optionCountForQuestion = 0 ;
		Question question = null ; 
		
		while(itr.hasNext()){
			question = (Question) itr.next();
			if(!question.isDeleted()){				
				optionCount = optionCount + question.getOptionCount();
			}
			
		}
		
		return optionCount;
		
	}
	
	public int getQuestionCount(){
		questions = getQuestions();
		
		
		
		if(questions == null){
			return 0 ;
		}
	
		Iterator itr = questions.iterator() ;
		int qCount = 0 ;
		Question question ; 
		while(itr.hasNext()){
			question = (Question) itr.next() ; 
			if(!question.deleted) {
				qCount++;
			}
		}
		
				
		
		return qCount;
	}
	
	
	
	
	/**
	 * @return Returns the questions
	 * 
	 * @hibernate.set table="mr.questions" cascade="save-update" lazy="false" inverse="true"    
     * @hibernate.collection-key column="survey_id"
     * @hibernate.collection-one-to-many class="uk.co.bluetrail.mobriz.model.Question"
	 */
	public Set getQuestions() {	
		
		return questions;
	}
	
	
	/**
	 * @return Returns the id.
	 * @hibernate.id column="id"
     *  generator-class="increment" unsaved-value="null"
	  * @struts.form-field
	 **/
	public Long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return Returns the title.
	 * @hibernate.property
	 * @struts.validator type="required"
	 * @struts.validator type="maxlength" arg1value="30"
     * @struts.validator-var name="maxlength" value="30" arg1value="30"

	 * @struts.form-field

	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
		
	
	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.model.BaseObject#toString()
	 */
	public String toString() {
		
		return title;
	}
	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.model.BaseObject#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {

		if(id.compareTo((Long) o) == 0 ){
			return true; 
		} else {
			return false;
		}
	}
	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.model.BaseObject#hashCode()
	 */
	public int hashCode() {
		
		return title.hashCode();
	}
	
	/**
	 * @return Returns the created_on.
	 * @hibernate.property
	 */
	public Long getCheckPoint() {
		return checkPoint;
	}
	/**
	 * @param checkPoint The checkPoint to set.
	 */
	public void setCheckPoint(Long checkPoint) {
		this.checkPoint = checkPoint;
	}
	/**
	 * @return Returns the created_on.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public Date getCreated_on() {
		return created_on;
	}
	/**
	 * @param created_on The created_on to set.
	 */
	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}
	/**
	 * @return Returns the createdBy.
	 * @hibernate.property	 
	 */
	public Long getCreatedBy_id() {
		return createdBy_id;
	}
	/**
	 * @param createdBy_id The createdBy to set.
	 */
	public void setCreatedBy_id(Long createdBy_id) {
		this.createdBy_id = createdBy_id;
	}
	/**
	 * @return Returns the lastUpdateBy.
	 * @hibernate.property	 
	 */
	public Long getLastUpdatedBy_id() {
		return lastUpdatedBy_id;
	}
	/**
	 * @param lastUpdateBy_id The lastUpdateBy to set.
	 */
	public void setLastUpdatedBy_id(Long lastUpdatedBy_id) {
		this.lastUpdatedBy_id = lastUpdatedBy_id;
	}
	/**
	 * @return Returns the updated_at.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public Date getUpdated_at() {
		return updated_at;
	}
	/**
	 * @param updated_at The updated_at to set.
	 */
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	/**
	 * @param questions The questions to set.
	 */
	public void setQuestions(Set questions) {
		
		
		
		this.questions = questions;
	}

	/**
	 * @param strings
	 */
	public void init(String[] strings) {
		
	
		this.setTitle(strings[1]); 
		this.setVersion(new Integer(0));
		this.setCommon(false) ;
		this.setSurveyStatus(Survey.DRAFT);
		this.setVisibility(0);
		
	}

	public void setResponseCount(Long responseCount) {
		this.responseCount = responseCount;
	}

	public boolean isDeleted() {
		return false;
	}

	public void setDeleted(boolean deleted) {
		this.surveyStatus = Survey.DELETED;
		
	}
  
	
	/**
	 * @return Returns the account Id.
	 * @hibernate.property
	 */
	public Long getAccount_id() {
		return account_id;
	}

	public void setAccount_id(Long account_id) {
		this.account_id = account_id;
	}

	public File[] getFiles() {
		return files;
	}

	public void setFiles(File[] files) {
		this.files = files;
	}

	public void setLastZipResponse_id(Long lastZipResponse_id) {
		if(this.lastZipResponse_id==null || this.lastZipResponse_id.longValue() < lastZipResponse_id.longValue() ) {
			this.lastZipResponse_id = lastZipResponse_id; 
		} 
		
		
	}

	/**
	 * @return Returns the LastZipResponse_id which is the id of the last response to be zipped.
	 * @hibernate.property	 
	 */public Long getLastZipResponse_id() {
		
		if(lastZipResponse_id==null) {
			return new Long(0);
		} else {
			return lastZipResponse_id;
		}
		
	}

	public void setLastPhotoStampResponse_id(Long lastPhotoStampResponse_id) {
		if(this.lastPhotoStampResponse_id==null || this.lastPhotoStampResponse_id.longValue() < lastPhotoStampResponse_id.longValue() ) {
			this.lastPhotoStampResponse_id = lastPhotoStampResponse_id; 
		} 
		
	}

	/**
	 * @return Returns the LastPhotoResponse_id which is the id of the last response to be stamped for this survey.
	 * @hibernate.property	 
	 */
	public Long getLastPhotoStampResponse_id() {
		if(lastPhotoStampResponse_id==null) {
			return new Long(0);
		} else {
			return lastPhotoStampResponse_id;
		}
	}
	
	public Question getQuestionByShortName(String shortName) {
		
		if(this.questionMapByShortName==null) {
			this.getQuestionMapByShortName() ;
		}
		
		Question question = (Question) this.questionMapByShortName.get(shortName.toLowerCase());
		
		return question ;
		
		
	}
	
public HashMap getQuestionMapByShortName() {
		
		if(questionMapByShortName != null){
			return questionMapByShortName ;
		}
		
		questionMapByShortName = new HashMap();  
		
		Iterator itr = questions.iterator() ; 
		
		Question q = null ; 
		while(itr.hasNext()){
			q = (Question) itr.next() ; 
			questionMapByShortName.put(q.getShortname().toLowerCase(),q);
			
		}
		return questionMapByShortName;
	}
	
}
