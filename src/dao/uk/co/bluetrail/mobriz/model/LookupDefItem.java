package uk.co.bluetrail.mobriz.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Stores a lookupdefItems used in lookup questions
 * 
 * <p><a href="LookupDefItem.java.html"><i>View Source</i></a>
 *
 *
 * @hibernate.class table="mr.lookupdefitems"
 */
public class LookupDefItem  extends BaseObject { 

	private static final String COMMA = ", ";
	private Long id ;
	private Long lookupDef_id;
	private String lookup1 ; //the value   
	private String lookup2 ;
	private String lookup3 ;
	private String lookup4 ;
	private String lookup5 ;
	private String lookup6 ;
	private String lookup7 ;
	protected Long account_id;
	protected boolean deleted = false ;
	private int idx = 0 ;
	
	
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
		
		return id.toString();
	}

	public String getValue() {
		return id.toString();
	}
	
	public String getDisplayValue(LookupDef lookupDef){
		
		if(lookupDef_id==null) {
			return "No Lookup Def" ;			
		}
		
		StringBuffer dVal = new StringBuffer() ;
		
		if(lookupDef.isLookupSearch1()) {
			dVal.append(this.getLookup1());
			dVal.append(COMMA);
		}
		if(lookupDef.isLookupSearch2()) {
			dVal.append(this.getLookup2());
			dVal.append(COMMA);
		}
		if(lookupDef.isLookupSearch3()) {
			dVal.append(this.getLookup3());
			dVal.append(COMMA);
		}
		if(lookupDef.isLookupSearch4()) {
			dVal.append(this.getLookup4());
			dVal.append(COMMA);
		}
		if(lookupDef.isLookupSearch5()) {
			dVal.append(this.getLookup5());
			dVal.append(COMMA);
		}
		if(lookupDef.isLookupSearch6()) {
			dVal.append(this.getLookup6());
			dVal.append(COMMA);
		}
		if(lookupDef.isLookupSearch7()) {
			dVal.append(this.getLookup7());
			dVal.append(COMMA);
		}
		
		return dVal.toString();
	}
	
	public ArrayList getReportFields(LookupDef lookupDef){

		ArrayList fields = new ArrayList() ;
		
				
		if(lookupDef.isLookupReport1()) {
			fields.add(this.getLookup1());
			
		}
		
		if(lookupDef.isLookupReport2()) {
			fields.add(this.getLookup2());
			
		}
		
		if(lookupDef.isLookupReport3()) {
			fields.add(this.getLookup3());
			
		}
		
		if(lookupDef.isLookupReport4()) {
			fields.add(this.getLookup4());
			
		}
		
		if(lookupDef.isLookupReport5()) {
			fields.add(this.getLookup5());
			
		}
		
		if(lookupDef.isLookupReport6()) {
			fields.add(this.getLookup6());
			
		}
		
		if(lookupDef.isLookupReport7()) {
			fields.add(this.getLookup7());
			
		}
		
		
		
		return fields;
	}
	
	public ArrayList getFields(LookupDef lookupDef){

		ArrayList fields = new ArrayList() ;
		
				
		if(!this.isBlank(lookupDef.getLookupDesc1())) {
			fields.add(this.getLookup1());
			
		}
		
		if(!this.isBlank(lookupDef.getLookupDesc2())) {
			fields.add(this.getLookup2());
			
		}
		
		if(!this.isBlank(lookupDef.getLookupDesc3())) {
			fields.add(this.getLookup3());
			
		}
		
		if(!this.isBlank(lookupDef.getLookupDesc4())) {
			fields.add(this.getLookup4());
			
		}
		
		if(!this.isBlank(lookupDef.getLookupDesc5())) {
			fields.add(this.getLookup5());
			
		}
		
		if(!this.isBlank(lookupDef.getLookupDesc6())) {
			fields.add(this.getLookup6());
			
		}
		
		if(!this.isBlank(lookupDef.getLookupDesc7())) {
			fields.add(this.getLookup7());
			
		}
		
		
		
		return fields;
	}
	
	
	private boolean isBlank(String str) {
		
		if(str == null || str.equals("")) {
			return true ; 
		} else {
			return false ;
		}
		
		
	}


	/**
	 * @hibernate.property
	 *  
	 */
	public Long getLookupDef_id() {
		return lookupDef_id;
	}


	public void setLookupDef_id(Long lookupDef_id) {
		this.lookupDef_id = lookupDef_id;
	}


	/**
	 * @return Returns the id.
	 * @hibernate.id column="id"
     *  generator-class="increment" unsaved-value="null"	 
	 **/
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
		
	/**
	 * @hibernate.property
	 *  
	 */
	public String getLookup1() {
		if(lookup1==null) {
			return "";
		} else {
			return lookup1;
		}
		
	}
	
	
	public void setLookup1(String lookup1) {
		this.lookup1 = lookup1;
	}
	
	/**
	 * @hibernate.property
	 *  
	 */	
	public String getLookup2() {
		if(lookup2==null) {
			return "";
		} else {
			return lookup2;
		}
		
	}
	public void setLookup2(String lookup2) {
		this.lookup2 = lookup2;
	}
	
	/**
	 * @hibernate.property
	 *  
	 */	
	public String getLookup3() {
		if(lookup3==null) {
			return "";
		} else {
			return lookup3;
		}
		
	}
	public void setLookup3(String lookup3) {
		this.lookup3 = lookup3;
	}
	
	/**
	 * @hibernate.property
	 *  
	 */	
	public String getLookup4() {
		if(lookup4==null) {
			return "";
		} else {
			return lookup4;
		}
		
	}
	public void setLookup4(String lookup4) {
		this.lookup4 = lookup4;
	}
	
	
	/**
	 * @hibernate.property
	 *  
	 */	
	public String getLookup5() {
		if(lookup5==null) {
			return "";
		} else {
			return lookup5;
		}
		
	}
	public void setLookup5(String lookup5) {
		this.lookup5 = lookup5;
	}
	
	/**
	 * @hibernate.property
	 *  
	 */	
	public Long getAccount_id() {
		return account_id;
	}
	public void setAccount_id(Long account_id) {
		this.account_id = account_id;
	}
	
	/**
	 * @hibernate.property
	 *  
	 */	
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}


	public void add(String[] str, LookupDef lookupDef) {

		ArrayList fIdx = lookupDef.getSearchFieldIndex();

		for (int i = 0; i < str.length; i++) {

			switch (((Integer) fIdx.get(i)).intValue()) {

			case 1:
				this.setLookup1(str[i]);
				break;
			case 2:
				this.setLookup2(str[i]);
				break;
			case 3:
				this.setLookup3(str[i]);
				break;
			case 4:
				this.setLookup4(str[i]);
				break;
			case 5:
				this.setLookup5(str[i]);
				break;
			case 6:
				this.setLookup6(str[i]);
				break;
			case 7:
				this.setLookup7(str[i]);
				break;
			default:
				throw new SurveyException(
						"Unexpected fieldIndex in lookupDefItem.add: ");
			}

		}

	}

	/**
	 * @hibernate.property
	 * 
	 */	
	public String getLookup6() {
		if(lookup6==null) {
			return "";
		} else {
			return lookup6;
		}
	}


	public void setLookup6(String lookup6) {
		this.lookup6 = lookup6;
	}

	/**
	 * @hibernate.property
	 *  
	 */	
	public String getLookup7() {
		if(lookup7==null) {
			return "";
		} else {
			return lookup7;
		}
	}


	public void setLookup7(String lookup7) {
		this.lookup7 = lookup7;
	}


	public boolean isEmpty() {

		StringBuffer fields = new StringBuffer() ;
		fields.append(this.getLookup1());
		fields.append(this.getLookup2());
		fields.append(this.getLookup3());
		fields.append(this.getLookup4());
		fields.append(this.getLookup5());
		fields.append(this.getLookup6());
		fields.append(this.getLookup7());
		
		return fields.toString().trim().equals("");
		
	}


	public void copy(int[] fieldIndex, String[] split) {
	
		try {

			for (int i = 0; i < fieldIndex.length; i++) {

				switch (fieldIndex[i]) {

				case 1:

					this.setLookup1(split[i]);

					break;
				case 2:

					this.setLookup2(split[i]);

					break;
				case 3:

					this.setLookup3(split[i]);

					break;
				case 4:

					this.setLookup4(split[i]);

					break;
				case 5:

					this.setLookup5(split[i]);

					break;
				case 6:

					this.setLookup6(split[i]);

					break;
				case 7:

					this.setLookup7(split[i]);

					break;
				default:
					throw new SurveyException("Invalid index value .. ");
				}

			}

		} catch (Exception e) {
			throw new SurveyException(e.toString());
		}
			
			
		
		
		
		
	}


	public static String parseId(String str) {
		try {
			if (str.indexOf(SurveyResponse.SEP) == 0) {
				return str.trim();
			} else {
				String[] parts = str.split(SurveyResponse.SEP);
				return parts[1];
			}
		} catch (Exception e) {
			return str;
		}
		
		
	} 
	
}
