/*
 * Created on 23-Jan-2006
 *
 */
package uk.co.bluetrail.mobriz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * Question class
 * 
 * Defines a question for a survey
 * 
 * <p><a href="Question.java.html"><i>View Source</i></a></p>
 * 
 * @author <a href="mailto:rspence@bluetrail.co.uk">Richard Spence</a>
 *
 * @struts.form include-all="false" extends="BaseForm"
 * @hibernate.class table="mr.questions"
 */
public class Question extends BaseObject implements SurveyElement {

	private Long id ;	
	private String qTxt ; //mando
	private int qType ; //mando
	private Long jQuestion_id ; 
	private Long survey_id ; //mando
	private Set constraints = new HashSet();
	private Set options = new HashSet();
	private Survey survey ;
	private boolean required = true ;   
	private boolean sticky = true ; 
	
	private Long branch_jquestion_id   ;   
	
	
	private Long checkPoint = null; 
	private Long lastUpdatedBy_id = null; 
	private Date updated_at = null ;	
	private Long createdBy_id = null;
	private Date created_on = null;
	protected Integer version;
	protected boolean deleted = false ; 
	
	private String shortname ;
	private String qMeta ; //extra string field for storing meta data like the name of the lookup 
	private LookupDef lookupDef;

	
	
	//constants
	public static final String QTYPE_CHECKBOX = "1"  ;
	public static final String QTYPE_DATE = "2"  ;
	public static final String QTYPE_MESSAGE = "3"  ;
	public static final String QTYPE_NUMBER = "4"  ;
	public static final String QTYPE_RADIO = "5"  ;
	public static final String QTYPE_TEXT = "6"  ;
	public static final String QTYPE_DECIMAL = "7"  ;
	public static final String QTYPE_PERCENT = "8"  ;
	public static final String QTYPE_SCORE = "9"  ;
	public static final String QTYPE_PICTURE = "10"  ;
	public static final String QTYPE_LOOKUP = "11"  ;
	
	
	public static final char QUESTION_TYPE = 'q';	
		
	public static final String[] QTYPES ;
	
	static {		
		QTYPES = new String[11] ;
		 
		QTYPES[0] = "multi-choice" ; 
		QTYPES[1] = "date" ;  
		QTYPES[2] = "message" ;
		QTYPES[3]  = "number"  ; 
		QTYPES[4] = "single-choice"  ; 
		QTYPES[5] = "text" ; 
		QTYPES[6] = "decimal" ;
		QTYPES[7] = "percent" ;
		QTYPES[8] = "score"  ;
		QTYPES[9] = "picture"  ;
		QTYPES[10] = "lookup"  ;
	}
	
	public boolean isText() {
		
		if(this.getQTypeStr().equals(QTYPE_DATE) || 
				this.getQTypeStr().equals(QTYPE_NUMBER) || 
				this.getQTypeStr().equals(QTYPE_DECIMAL) ||
				this.getQTypeStr().equals(QTYPE_SCORE) )
		{
			return false;
		} else {
			return true;  
		}
		
	
	}
	
	public String export() {
		StringBuffer lop = new StringBuffer() ;
		
		//q	what is the name	text	TRUE	FALSE	text
		
		
		lop.append(Question.QUESTION_TYPE) ;
		lop.append('\t') ;
		lop.append(this.getQTxt());
		lop.append('\t') ;
		lop.append(this.getQTypeExportString());
		lop.append('\t') ;
		lop.append(this.isRequired());
		lop.append('\t') ;
		lop.append(this.isSticky());
		lop.append('\t') ;
		lop.append(this.getShortname());
		
		return lop.toString();
		
	}
	
	
	/**
	 * @return
	 */
	private String getQTypeExportString() {
		
		int index = this.getQType() - 1 ;
		return QTYPES[index];
		
		
	}


	//quick function to choose between a branch and a jump	
	public Long getQuestionBranch_id() {
		
		Long branch = this.getBranch_jquestion_id() ; 
		Long jump = this.getJQuestion_id();
		
		//if they have a branch use it!!
		if(branch == null ||  branch.compareTo(new Long(0))==0){			
			return jump ;
		} else {
			return branch ;			 
		}
		
		
		
	}
	
	//this is just here in case any of the bean stuff objects to not having a setter
	public Long setQuestionBranch_id() {
		return this.branch_jquestion_id;
	}
	
	
	
	/**
	 * @return // jquestion_id is the default jump the branch is a question level branch.  the branch is downloded to the device
	 * @hibernate.property
	 * @struts.form-field
	 * @struts.validator type="required"
	 */
	public Long getBranch_jquestion_id() {
		return this.branch_jquestion_id;
	}
	/**
	 * @param branch_jquestion_id The branch_jquestion_id to set.
	 */
	public void setBranch_jquestion_id(Long branch_jquestion_id) {
		this.branch_jquestion_id = branch_jquestion_id;
	}
	/**
	 * @return Returns the shortname.
	 * @hibernate.property
	 * @struts.form-field
	 * @struts.validator type="required"
	 * @struts.validator type="maxlength" arg1value="20"
     * @struts.validator-var name="maxlength" value="20" arg1value="20"

	 */
	public String getShortname() {
		return shortname;
	}
	/**
	 * @param shortname The shortname to set.
	 */
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	/**
	 * @return Returns the sticky.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public boolean isSticky() {
		return sticky;
	}
	/**
	 * @param sticky The sticky to set.
	 */
	public void setSticky(boolean sticky) {
		this.sticky = sticky;
	}
	/**
	 * @return Returns the required.
	 * @hibernate.property
	 * @struts.form-field	 
	 */
	public boolean isRequired() {
		return required;
	}
	/**
	 * @param required The required to set.
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	
	public int getOptionCount(){
		
		options = getOptions();
		
		
		
		
		if(options == null){
			return 0 ;
		}
	
		
		
		Iterator itr = options.iterator() ;
		int oCount = 0 ;
		Option option ; 
		
		
		while(itr.hasNext()){
			option = (Option) itr.next() ;
			
			if(!option.isDeleted()) {
				
				oCount++;
			} 
		}
		
				
		
		return oCount;
	}
	
	
	public int getConstraintCount(){
		constraints = getConstraints();
		
		
		
		if(constraints == null){
			
			return 0 ;
			
		} 
		
		Iterator itr = constraints.iterator() ;
		int cCount = 0 ;
		Constraint constraint ; 
		while(itr.hasNext()){
			constraint = (Constraint) itr.next() ; 
			if(!constraint.deleted) {
				cCount++;
			}
		}
		
				
		
		return cCount;
	}
	
	
	/**
	 * 
	 * remmed out added to merge file in dao so we can make it not 
	 * 
	 * hibernate.many-to-one column="survey_id" class="uk.co.bluetrail.mobriz.model.Survey"
	 * lazy="false"   
	 *  insert="false" 
	 *  update="false"
     * 
	 * @return Question
	 */	
	public Survey getSurvey() {
		return survey;
	}
	/**
	 * @param survey The survey to set.
	 */
	public void setSurvey(Survey survey) {
		this.survey = survey;
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
	 * @return Returns the constraints
	 * @hibernate.set table="mr.constraints" cascade="save-update" lazy="false"  inverse="true"
     * @hibernate.collection-key column="question_id"
     * @hibernate.collection-one-to-many class="uk.co.bluetrail.mobriz.model.Constraint"
	 */
	public Set getConstraints() {
		return constraints;
	}
	
	public Set getConstraints(boolean deleted){
		
		Set constraints = getConstraints() ;
		Iterator itr = constraints.iterator() ;
		
		HashSet liveConstraints = new HashSet() ;
		Constraint constraint = null ; 
		while(itr.hasNext()){
			constraint = (Constraint) itr.next() ;			
			if(!constraint.isDeleted() == deleted) {
				liveConstraints.add(constraint) ;
			}
			
		}
		
		if(liveConstraints ==null || liveConstraints.size()==0){
			return null ;
		} else {
			return liveConstraints ;
		}
		
	}
	
	
	/**
	 * @param constraints The constraints to set.
	 */
	public void setConstraints(Set constraints) {
		this.constraints = constraints;
	}
	/**
	 * @param options The options to set.
	 */
	public void setOptions(Set options) {
		this.options = options;
	}
	/**
	 * @return Returns the options
	 * @hibernate.set table="mr.options" cascade="save-update" lazy="false"  inverse="true"  order-by="id"
     * @hibernate.collection-key column="question_id" not-null="true"
     * @hibernate.collection-one-to-many class="uk.co.bluetrail.mobriz.model.Option"
	 */
	public Set getOptions() {
		
		//for lookup questions return the number of lookup fields
		if(this.getLookupDef() != null && this.getQTypeStr().equals(Question.QTYPE_LOOKUP)) {
			Option opt;
			long id = 0 ;
			Iterator itr = this.getLookupDef().getLookupFields().iterator();
			String optText ;
			options = new LinkedHashSet() ;
			
			//make the first two options the lookup id and the lookup instrucxtions
			options.add(getLookupOption(id++, this.getLookupDef().getId().toString()));
			options.add(getLookupOption(id++,this.getLookupDef().getLookupPrompt()));
			
			
			while(itr.hasNext()) {
				optText = (String) itr.next();
				options.add(getLookupOption(id++,optText));
				
			}
			
			return options;
			
		} else {
			return options;
		}
	}
	
	
	public Option getLookupOption(long id, String optText) {
		
		Option opt = new Option();
		opt.setId(new Long(id));
		opt.setOText(optText);
		opt.setJQuestion_id(new Long(0));
		opt.setQuestion(this);
	
		return opt;
	}
	
	
	public SortedSet getSortedOptions(boolean deleted) {
		
		Set unsortedOptions = getOptions(deleted) ;
		
		if ( unsortedOptions == null ) {
			return null;
		}
		SortedSet sortedOptions = new TreeSet() ;
		
		Iterator itr = unsortedOptions.iterator();
		Option option = null; 
		while(itr.hasNext()){
			Object o = itr.next() ; 
			option = (Option) o ; 
			sortedOptions.add(option);
		}
		
		
		return sortedOptions; 	
	}
	
	
	public Set getOptions(boolean deleted){
		
		Set options = getOptions() ;
		Iterator itr = options.iterator() ;
		
		HashSet liveOptions = new HashSet() ;
		Option option = null ; 
		while(itr.hasNext()){
			option = (Option) itr.next() ;
			if(!option.isDeleted() == deleted) {
				liveOptions.add(option) ;
			}
			
		}
		
		if(liveOptions ==null || liveOptions.size()==0){
			return null ;
		} else {
			return liveOptions ;
		}
		
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
	 * @struts.validator type="required"
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
	 * @return Returns the qTxt.
	 * @hibernate.property
	 * @struts.form-field
	 * @struts.validator type="required"	
	 * @struts.validator type="maxlength" arg1value="100"
     * @struts.validator-var name="maxlength" value="100" arg1value="100"

	*/
	public String getQTxt() {
		return qTxt;
	}
	/**
	 * @param txt The qTxt to set.
	 */
	public void setQTxt(String txt) {
		qTxt = txt;
	}
	/**
	 * @return Returns the qType.
	 * @hibernate.property
	 * @struts.form-field
	 * @struts.validator type="required"
	 */
	public int getQType() {
		return qType;
	}
	
	public String getQTypeStr() {
		return "" + qType;
	} 
	
	/**
	 * @param type The qType to set.
	 */
	public void setQType(int type) {
		qType = type;
	}
	/**
	 * @return Returns the survey_id.
	 * @hibernate.property
	 * @struts.form-field
	 * @struts.validator type="required"
	 */
	public Long getSurvey_id() {
		return survey_id;
	}
	/**
	 * @param survey_id The survey_id to set.
	 */
	public void setSurvey_id(Long survey_id) {
		this.survey_id = survey_id;
	}
	/**
	 * @return Returns the created_on.
	 * @hibernate.property
	 *  
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
		
		return id.hashCode();
	}
	
	/**
     * Adds an option the list
     *
     * @param parentco
     */
     public void addOption(Option option) {
    	 getOptions().add(option);
     }
     
     /**
      * Adds a constraint 
      *
      * @param parentco
      */
      public void addConstraint(Constraint constraint) {
     	  getConstraints().add(constraint);
      }

	/**
	 * @param strings
	 */
	public void init(String[] dataLine) {
		//q	what is the name	text	TRUE	FALSE	text		
		
		this.setQTxt(dataLine[1]) ;		
		this.setQType(dataLine[2].toLowerCase());		
		this.setRequired(Boolean.parseBoolean(dataLine[3].toLowerCase()));
		this.setSticky(Boolean.parseBoolean(dataLine[4].toLowerCase()));	
		this.setShortname(dataLine[5]);		
		this.setBranch_jquestion_id(new Long(0));
		this.setJQuestion_id(new Long(0));
		
	}


	/**
	 * @param string
	 */
	private void setQType(String string) {
	
		for(int i = 0 ; i < QTYPES.length ; i++) {
			if(QTYPES[i].equals(string)) {
				setQType(i+1) ;
				return ;
			}
		}
		
		throw new SurveyException("Unknown question type " + string);
		
	}

	/**
	 * @return Returns the sticky.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public String getQMeta() {
		return qMeta;
	}


	public void setQMeta(String meta) {
		qMeta = meta;
	}


	public void setLookupDef(LookupDef lookupDef) {
		this.lookupDef = lookupDef;
		
	}


	public LookupDef getLookupDef() {
		return lookupDef;
	}
     
	
}
