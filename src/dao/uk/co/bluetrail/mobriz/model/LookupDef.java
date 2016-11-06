package uk.co.bluetrail.mobriz.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import antlr.collections.impl.Vector;

/**
 * The Deinition or header for a lookup.  Defines the fields and the name of the lookup
 * A lookup is used from the client to select itrems that have too many items to download with a survey
 *
 * <p><a href="LookupDef.java.html"><i>View Source</i></a>
 *
 *
 * @hibernate.class table="mr.lookupdefs"
 */
public class LookupDef  extends BaseObject implements SurveyElement{

	private static final int MAX_DESC_LEN = 29;
	private Long id ;
	private String name ; 
	private String desc ; 
	private String lookupDesc1 ; //the names of the lookup columns will be diplayed in client  
	private String lookupDesc2 ;
	private String lookupDesc3 ;
	private String lookupDesc4 ;
	private String lookupDesc5 ;
	private String lookupDesc6 ;
	private String lookupDesc7 ;
	private boolean lookupSearch1 ; //whether this field is the search on the device
	private boolean lookupSearch2 ;
	private boolean lookupSearch3 ;
	private boolean lookupSearch4 ;
	private boolean lookupSearch5 ;
	private boolean lookupSearch6 ;
	private boolean lookupSearch7 ;
	private boolean lookupReport1 ; //wether this field is included in the csv download
	private boolean lookupReport2 ;
	private boolean lookupReport3 ;
	private boolean lookupReport4 ;
	private boolean lookupReport5 ;
	private boolean lookupReport6 ;
	private boolean lookupReport7 ;
	
	
	private ArrayList reportFields;
	
	protected Long account_id;
	private Long checkPoint = null; 
	private Long lastUpdatedBy_id = null; 
	private Date updated_at = null ;	
	private Long createdBy_id = null;
	private Date created_on = null;
	protected Integer version;
	protected boolean deleted = false ; 
	private Set lookupDefItems = null; 

	private String lookupPrompt ; 
	
	
	private HashSet lookupFields = null; ;
	
	public int getLookupFieldCount() {
	
		return 	getLookupFields().size();
		
	}
	
	
	
	
	/*
	 * returns the number of input fields for this lookup.
	 */
	public HashSet getLookupFields() {
		
		if(lookupFields !=null) {
			return lookupFields; 
		}
		
		lookupFields = new LinkedHashSet();
		
		
		
		int cnt = 0 ; 
		
		if(this.lookupSearch1) {
			lookupFields.add(this.lookupDesc1); 
		}
		if(this.lookupSearch2) {
			lookupFields.add(this.lookupDesc2); 
		}
		if(this.lookupSearch3) {
			lookupFields.add(this.lookupDesc3); 
		}
		if(this.lookupSearch4) {
			lookupFields.add(this.lookupDesc4); 
		}
		if(this.lookupSearch5) {
			lookupFields.add(this.lookupDesc5); 
		}
		if(this.lookupSearch6) {
			lookupFields.add(this.lookupDesc6); 
		}
		if(this.lookupSearch7) {
			lookupFields.add(this.lookupDesc7); 
		}
		
		return lookupFields;
		
	}
	
	
	private boolean notBlank(String v) {
		if(v != null && !v.equals("")) {
			return true ;
		} else {
			return false;
		}
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



	/**
     * @hibernate.property length="30" not-null="true"
     */
	public String getName() {
		return name;
	}



	/**
     * @spring.validator type="required"
     */
	public void setName(String name) {
		this.name = name;
	}


	/**
     * @hibernate.property length="255" not-null="true" column="description"
     */
	public String getDesc() {
		return desc;
	}



	/**
     * @spring.validator type="required"
     */
	public void setDesc(String desc) {
		this.desc = desc;
	}


	/**
     * @hibernate.property length="30" not-null="true"
     */
	public String getLookupDesc1() {
		if(lookupDesc1==null) {
			return "" ;
		} else {
			return lookupDesc1;
		}
	}



	/**
     * @spring.validator type="required"
     */
	public void setLookupDesc1(String lookupDesc1) {
		
		this.lookupDesc1 = truncate(lookupDesc1);
		
	}



	private String truncate(String str) {
		if(str !=null) {
			int l = 0 ;
			if(str.length() > MAX_DESC_LEN) {
				l = MAX_DESC_LEN ;
			} else {
				l= str.length();
			}
			return str.substring(0,l);
		} else {
			return str; 
		}
		
	}

	/**
     * @hibernate.property length="30" not-null="true"
     */
	public String getLookupDesc2() {
		if(lookupDesc2==null) {
			return "" ;
		} else {
			return lookupDesc2;
		}
	}



	
	public void setLookupDesc2(String lookupDesc2) {
		this.lookupDesc2 = truncate(lookupDesc2);
	}



	/**
     * @hibernate.property length="30" not-null="true"
     */
	public String getLookupDesc3() {
		if(lookupDesc3==null) {
			return "" ;
		} else {
			return lookupDesc3;
		}
	}



	
	public void setLookupDesc3(String lookupDesc3) {
		this.lookupDesc3 = truncate(lookupDesc3);
	}




	/**
     * @hibernate.property length="30" not-null="true"
     */
	public String getLookupDesc4() {
		if(lookupDesc4==null) {
			return "" ;
		} else {
			return lookupDesc4;
		}
	}




	public void setLookupDesc4(String lookupDesc4) {
		this.lookupDesc4 = truncate(lookupDesc4);
	}



	/**
     * @hibernate.property length="30" not-null="true"
     */
	public String getLookupDesc5() {
		if(lookupDesc5==null) {
			return "" ;
		} else {
			return lookupDesc5;
		}
	}




	public void setLookupDesc5(String lookupDesc5) {
		this.lookupDesc5 = truncate(lookupDesc5);
	}




	@Override
	public boolean equals(Object o) {
		
		return ((LookupDef) o).getId().longValue() == getId().longValue() ;
	}

	
	

	@Override
	public int hashCode() {
		
		return id.hashCode();
	}

	@Override
	public String toString() {
		
		return getName();
	}

	/**
	 * @return Returns the id.
	 * @hibernate.id column="id"
     *  generator-class="increment" unsaved-value="null"	 
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
	 * @return Returns the deleted.
	 * @hibernate.property
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



	public Set getLookupDefItems() {
		return lookupDefItems;
	}



	public void setLookupDefItems(Set lookupDefItems) {
		this.lookupDefItems = lookupDefItems;
	}

	/**
	 * @hibernate.property length="30" not-null="true"
	 */
	public String getLookupDesc6() {
		if(lookupDesc6==null) {
			return "" ;
		} else {
			return lookupDesc6;
		}
	}

	public void setLookupDesc6(String lookupDesc6) {
		this.lookupDesc6 = truncate(lookupDesc6);
	}

	/**
	 * @hibernate.property length="30" not-null="true"
	 */
	public String getLookupDesc7() {
		if(lookupDesc7==null) {
			return "" ;
		} else {
			return lookupDesc7;
		}
	}

	public void setLookupDesc7(String lookupDesc7) {
		this.lookupDesc7 = truncate(lookupDesc7);
	}

	/**
	 * @hibernate.property
	 */
	public boolean isLookupSearch1() {
		return lookupSearch1;
	}

	public void setLookupSearch1(boolean lookupSearch1) {
		this.lookupSearch1 = lookupSearch1;
	}

	/**
	 * @hibernate.property
	 */
	public boolean isLookupSearch2() {
		return lookupSearch2;
	}

	public void setLookupSearch2(boolean lookupSearch2) {
		this.lookupSearch2 = lookupSearch2;
	}

	/**
	 * @hibernate.property
	 */
	public boolean isLookupSearch3() {
		return lookupSearch3;
	}

	public void setLookupSearch3(boolean lookupSearch3) {
		this.lookupSearch3 = lookupSearch3;
	}

	/**
	 * @hibernate.property
	 */
	public boolean isLookupSearch4() {
		return lookupSearch4;
	}

	public void setLookupSearch4(boolean lookupSearch4) {
		this.lookupSearch4 = lookupSearch4;
	}

	/**
	 * @hibernate.property
	 */
	public boolean isLookupSearch5() {
		return lookupSearch5;
	}

	public void setLookupSearch5(boolean lookupSearch5) {
		this.lookupSearch5 = lookupSearch5;
	}

	/**
	 * @hibernate.property
	 */
	public boolean isLookupSearch6() {
		return lookupSearch6;
	}

	public void setLookupSearch6(boolean lookupSearch6) {
		this.lookupSearch6 = lookupSearch6;
	}

	/**
	 * @hibernate.property
	 */
	public boolean isLookupSearch7() {
		return lookupSearch7;
	}

	public void setLookupSearch7(boolean lookupSearch7) {
		this.lookupSearch7 = lookupSearch7;
	}

	/**
	 * @hibernate.property
	 */
	public boolean isLookupReport1() {
		return lookupReport1;
	}

	public void setLookupReport1(boolean lookupReport1) {
		this.lookupReport1 = lookupReport1;
	}

	/**
	 * @hibernate.property
	 */
	public boolean isLookupReport2() {
		return lookupReport2;
	}

	public void setLookupReport2(boolean lookupReport2) {
		this.lookupReport2 = lookupReport2;
	}

	/**
	 * @hibernate.property
	 */
	public boolean isLookupReport3() {
		return lookupReport3;
	}

	public void setLookupReport3(boolean lookupReport3) {
		this.lookupReport3 = lookupReport3;
	}

	/**
	 * @hibernate.property
	 */
	public boolean isLookupReport4() {
		return lookupReport4;
	}

	public void setLookupReport4(boolean lookupReport4) {
		this.lookupReport4 = lookupReport4;
	}

	/**
	 * @hibernate.property
	 */
	public boolean isLookupReport5() {
		return lookupReport5;
	}

	public void setLookupReport5(boolean lookupReport5) {
		this.lookupReport5 = lookupReport5;
	}

	/**
	 * @hibernate.property
	 */
	public boolean isLookupReport6() {
		return lookupReport6;
	}

	public void setLookupReport6(boolean lookupReport6) {
		this.lookupReport6 = lookupReport6;
	}

	/**
	 * @hibernate.property
	 */
	public boolean isLookupReport7() {
		return lookupReport7;
	}

	public void setLookupReport7(boolean lookupReport7) {
		this.lookupReport7 = lookupReport7;
	}

	public void setLookupFields(HashSet lookupFields) {
		this.lookupFields = lookupFields;
	}

	/**
	 * @hibernate.property length="100" not-null="true"
	 */
	public String getLookupPrompt() {
		return lookupPrompt;
	}

	/**
     * @spring.validator type="required"
     */
	public void setLookupPrompt(String lookupPrompt) {
		this.lookupPrompt = lookupPrompt;
	}

	public boolean isValid() {
		return false;
	}

	/**
	 * 
	 * Used in the import, takes a list of the field names and returns the number of the looupField
	 * 
	 * @param split
	 * @return
	 */
	public int[] getFieldIndex(String[] split) {

		int idx[] = new int[split.length];
		
		
		
		for(int i = 0; i < split.length; i++) {
			
			
			if(split[i].equalsIgnoreCase(this.lookupDesc1)){
				idx[i] = 1 ;
			} else if(split[i].equalsIgnoreCase(this.lookupDesc2)){
				idx[i] = 2 ;
			}else if(split[i].equalsIgnoreCase(this.lookupDesc3)){
				idx[i] = 3 ;
			}else if(split[i].equalsIgnoreCase(this.lookupDesc4)){
				idx[i] = 4 ;
			}else if(split[i].equalsIgnoreCase(this.lookupDesc5)){
				idx[i] = 5 ;
			}else if(split[i].equalsIgnoreCase(this.lookupDesc6)){
				idx[i] = 6 ;
			}else if(split[i].equalsIgnoreCase(this.lookupDesc7)){
				idx[i] = 7 ;
			} else {
				throw new SurveyException("Unknown field name " + split[i]);
			}
			
			
		}

		return idx;
	}

	
	/**
	 * 
	 * Used in the webserveice searc, returns the index of the fields used in searches 
	 * 
	 * @param split
	 * @return
	 */
	public ArrayList getSearchFieldIndex() {

		ArrayList idx = new ArrayList();
		
		
		if(this.isLookupSearch1()) {
			idx.add(new Integer(1)) ;
		}
		if(this.isLookupSearch2()) {
			idx.add(new Integer(2)) ;
		}
		if(this.isLookupSearch3()) {
			idx.add(new Integer(3)) ;
		}
		if(this.isLookupSearch4()) {
			idx.add(new Integer(4)) ;
		}
		if(this.isLookupSearch5()) {
			idx.add(new Integer(5)) ;
		}
		if(this.isLookupSearch6()) {
			idx.add(new Integer(6)) ;
		}
		
		if(this.isLookupSearch7()) {
			idx.add(new Integer(7)) ;
		}
				
		return idx;
	}
	
	
	public List reportFields() {
		
		if(reportFields !=null) {
			return reportFields; 
		}
		
		reportFields = new ArrayList();
		
		
		if(this.lookupReport1) {
			reportFields.add(this.lookupDesc1); 
		}
		if(this.lookupReport2) {
			reportFields.add(this.lookupDesc2); 
		}
		if(this.lookupReport3) {
			reportFields.add(this.lookupDesc3); 
		}
		if(this.lookupReport4) {
			reportFields.add(this.lookupDesc4); 
		}
		if(this.lookupReport5) {
			reportFields.add(this.lookupDesc5); 
		}
		if(this.lookupReport6) {
			reportFields.add(this.lookupDesc6); 
		}
		if(this.lookupReport7) {
			reportFields.add(this.lookupDesc6); 
		}
		
		return reportFields;
	}
	
	private boolean isBlank(String str) {
		
		if(str == null || str.equals("")) {
			return true ; 
		} else {
			return false ;
		}
		
		
	}

	
	
	
	
	public ArrayList getFields(LookupDef lookupDef){

		ArrayList fields = new ArrayList() ;
		
				
		if(!this.isBlank(getLookupDesc1())) {
			fields.add(getLookupDesc1());
			
		}
		
		if(!this.isBlank(getLookupDesc2())) {
			fields.add(this.getLookupDesc2());
			
		}
		
		if(!this.isBlank(getLookupDesc3())) {
			fields.add(this.getLookupDesc3());
			
		}
		
		if(!this.isBlank(getLookupDesc4())) {
			fields.add(this.getLookupDesc4());
			
		}
		
		if(!this.isBlank(lookupDef.getLookupDesc5())) {
			fields.add(this.getLookupDesc5());
			
		}
		
		if(!this.isBlank(lookupDef.getLookupDesc6())) {
			fields.add(this.getLookupDesc6());
			
		}
		
		if(!this.isBlank(lookupDef.getLookupDesc7())) {
			fields.add(this.getLookupDesc7());
			
		}
		
		
		
		return fields;
	}
	
}
