/*
 * Created on 23-Jan-2006
 *
 */
package uk.co.bluetrail.mobriz.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Option class
 * 
 * An option for a question like red blue green
 * 
 * <p><a href="Option.java.html"><i>View Source</i></a></p>
 * 
 * @author <a href="mailto:rspence@bluetrail.co.uk">Richard Spence</a>
 * 
 * @struts.form include-all="false" extends="BaseForm"
 * @hibernate.class table="mr.options"
 */
public class Option extends BaseObject implements SurveyElement,Comparable {

	public static final char OPTION_TYPE = 'o';
	
	private Long id ;
	private Long question_id ;  //mando
	private String oText ; //mando
	private Long jQuestion_id;	 
	private String oType ;
 
	private Long checkPoint = null; 
	private Long lastUpdatedBy_id = null; 
	private Date updated_at = null ;	
	private Long createdBy = null;
	private Date created_on = null;
	
	private Question question ;
	protected Integer version;
	protected boolean deleted = false ; 
	
	public boolean hasSameId(Option option) {
		
		if(option == null) {
			return false ;			
		}
		
		if(getId()==null) {
			return false ;  
		}
		
		if(option.getId() == null ) {
			return false ;
		}
			
		Long optionId = option.getId() ; 
		Long thisId = getId() ;
		
		if( thisId.compareTo(optionId) == 0 ) {
			return true ; 
		} else {
			return false ; 
		}
		
		
	}
	
	
	public boolean hasChanged(Option option) throws Exception {
		
		System.out.println("AAAAAAAAAAAA");
		
		if(!this.hasSameId(option)) {
			throw new Exception("hasChanged expects options with the same id");
		}
		
		if(option.isDeleted() != isDeleted()) {
			System.out.println("BBBBBBBAAAAAAAAAAAA");
			return true ; 
		}
		
		if(areEqual(option.getOText(),getOText())) {
			System.out.println("CBBBBBBBAAAAAAAAAAAA");
			return true ;
		}
		
		if(areEqual(option.getJQuestion_id(),getJQuestion_id())){
			System.out.println("ddBBBBBBBAAAAAAAAAAAA");
			return true ;
		}
		
		System.out.println("end BBBBBBBAAAAAAAAAAAA");
		
		return false ;
	}
	
	private boolean areEqual(Object o1, Object o2){
		
		if(o1 == null && o2 ==null) {
			return true ; 
		}
		
		if(o1 == null && o2 != null) {
			return false ; 
		}
		
		if(o1 != null && o2 == null) {
			return false ; 
		}
		
		if(o1.toString().equals(o2.toString())){
			return true ;
		}
		
		return false ;
		
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
     * 
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
	 * 
	 * remmed out added to merge fiel; in dao
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
	 * @return Returns the oText.
	 * @hibernate.property
	 * @struts.form-field
	 * @struts.validator type="required"
	 * @struts.validator type="maxlength" arg1value="20"
     * @struts.validator-var name="maxlength" value="20" arg1value="20"

	 */
	public String getOText() {
		return oText;
	}
	/**
	 * @param text The oText to set.
	 */
	public void setOText(String text) {
		oText = text;
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
		return createdBy;
	}
	/**
	 * @param createdBy_id The createdBy to set.
	 */
	public void setCreatedBy_id(Long createdBy_id) {
		this.createdBy = createdBy_id;
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


	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		
		if(o==null) {
			return 1; 
		}
		
		Option option = (Option) o ; 
		
		return this.getId().compareTo(option.getId());
		
	}


	/**
	 * @return Returns the oType.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public String getOType() {
		return this.oType;
	}


	/**
	 * @param type The oType to set.
	 */
	public void setOType(String type) {
		this.oType = type;
	}


	/**
	 * @param strings
	 */
	public void init(String[] dataLine) {
		
		this.setJQuestion_id(new Long(0));
		this.setOText(dataLine[1]);
		
		
	}


	/**
	 * @return
	 */
	public String export() {
		
		StringBuffer lop = new StringBuffer();
		
		lop.append(Option.OPTION_TYPE) ;
		lop.append('\t') ;
		lop.append(getOText());
		
		return lop.toString();
	}


	
	
	
	
	
}
