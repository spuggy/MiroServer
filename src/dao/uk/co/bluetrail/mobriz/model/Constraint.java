/*
 * Created on 23-Jan-2006
 *
 */
package uk.co.bluetrail.mobriz.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Constraint class
 * 
 * Defines a contstraint for a question e.g mandatory or greqater than 10
 * 
 * <p><a href="Constraint.java.html"><i>View Source</i></a></p>
 * 
 * @author <a href="mailto:rspence@bluetrail.co.uk">Richard Spence</a>
 * 
 * @struts.form include-all="false" extends="BaseForm"
 * @hibernate.class table="mr.constraints"
 */
public class Constraint extends BaseObject implements SurveyElement  {

	Long id ;	
	Long question_id ; //mando
	int cType  ; //mando 
	Long jQuestion_id ;
	int i1 ; 
	int i2 ; 
	Date d1;
	Date d2;
		
	private Question question ; 
	
	
	private Long checkPoint = null; 
	private Long lastUpdatedBy_id = null; 
	private Date updated_at = null ;	
	private Long createdBy_id = null;
	private Date created_on = null;
	protected Integer version;
	protected boolean deleted = false ; 
	
	public static final String CTYPE_EQUALS = "1"  ;
	public static final String CTYPE_NOTEQUALS = "2"  ;
	public static final String CTYPE_GT = "3"  ;
	public static final String CTYPE_LT = "4"  ;
	public static final String CTYPE_GT_EQUALS = "5"  ;
	public static final String CTYPE_LT_EQUALS = "6"  ;
	public static final String CTYPE_BETWEEN = "7"  ;
	
	
	public boolean isNew() {
	
		
		
		if(version == null) {
			return true;
		}
		if (version.compareTo(new Integer(0))==0 ) {
			return true ; 
		} else {
			return false ; 
		}
	}
	
	
	/**
	 * 
	 * remmed out added to merge file in dao so we can make it not 
	 * 
	 * hibernate.many-to-one column="question_id" class="uk.co.bluetrail.mobriz.model.Question"
	 * lazy="false"   
	 *  insert="false" 
	 *  update="false"
     * 
	 * @return Question
	 */	
	public Question getQuestion(){
		return question;
	}
	
	/**
	 * @param question The question to set.
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}
	
	/**
	 * @return Returns the deleted.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public boolean isDeleted() {
		return deleted;
	}
	/**
	 * @param deleted The deleted to set.
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
	/**
     * @return Returns the updated version.     
     * @hibernate.version
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
	
	
	
	/**
	 * @return Returns the cType.
	 * @hibernate.property
	 * @struts.validator type="required"
	 * @struts.form-field
	 */
	public int getCType() {
		return cType;
	}
	/**
	 * @param type The cType to set.
	 */
	public void setCType(int type) {
		cType = type;
	}
	/**
	 * @return Returns the d1.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public Date getD1() {
		return d1;
	}
	/**
	 * @param d1 The d1 to set.
	 */
	public void setD1(Date d1) {
		this.d1 = d1;
	}
	/**
	 * @return Returns the d2.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public Date getD2() {
		return d2;
	}
	/**
	 * @param d2 The d2 to set.
	 */
	public void setD2(Date d2) {
		this.d2 = d2;
	}
	/**
	 * @return Returns the i1.
	 * @hibernate.property
	 * @struts.form-field
*/
	public int getI1() {
		return i1;
	}
	/**
	 * @param i1 The i1 to set.
	 */
	public void setI1(int i1) {
		this.i1 = i1;
	}
	/**
	 * @return Returns the i2.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public int getI2() {
		return i2;
	}
	/**
	 * @param i2 The i2 to set.
	 */
	public void setI2(int i2) {
		this.i2 = i2;
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
	 * @return Returns the jQuestion_id.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public Long getJQuestion_id() {
		return jQuestion_id;
	}
	/**
	 * @param question_id The jQuestion_id to set.
	 */
	public void setJQuestion_id(Long question_id) {
		jQuestion_id = question_id;
	}
	/**
	 * @return Returns the question_id.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public Long getQuestion_id() {
		return question_id;
	}
	/**
	 * @param question_id The question_id to set.
	 */
	public void setQuestion_id(Long question_id) {
		this.question_id = question_id;
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
	 * @struts.form-field
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
	 * @struts.form-field
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
	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.model.BaseObject#toString()
	 */
	public String toString() {
		if(id == null ){
			return "" ; 
		} else {
			return id.toString();
		}
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
		
		if (id != null) {
			return id.hashCode();
		} else {
			return 0;
		}
	}
	
	
	
	
}
