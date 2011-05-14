package uk.co.bluetrail.mobriz.model;

import java.util.Date;

/**
 * stores project meta data like recording time and name. 
 *
 * <p><a href="Setting.java.html"><i>View Source</i></a>
 *
 *
 * @hibernate.class table="mr.setting"
 */
public class Setting extends BaseObject {

	private static final String SPLIT_CHAR = ";";
	private String settingName ; //short name of the setting
	private Long id ;  //internal non editable setting id
	private String settingValue ; // editable setting number
	private String settingDescription ; 
	protected Long account_id;
	private Account account ;
	
	
	

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

	/**
     * @hibernate.property column="account_id" 
     */
	public Long getAccount_id() {
		return account_id;
	}

	public void setAccount_id(Long account_id) {
		this.account_id = account_id;
	}

	public String toString() {
		
		return settingName ;
	}

	public boolean equals(Object o) {
		
		Setting p = (Setting) o ;
		
	
		if(id.longValue() == p.getId().longValue()) {
			return true ; 
		} else {
			return false;	
		}
		
	}

	public int hashCode() {
		
		return id.hashCode();
	}

	/**
     * @hibernate.id column="id" generator-class="native" unsaved-value="null"
     */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
     * @hibernate.property length="255" not-null="true"
     */
	public String getSettingDescription() {
		return settingDescription;
	}

	/**
     * @spring.validator type="required"
     */
	public void setSettingDescription(String settingDescription) {
		this.settingDescription = settingDescription;
	}

	/**
     * @hibernate.property length="30" not-null="true"
     */
	public String getSettingName() {
		return settingName;
	}

	/**
     * @spring.validator type="required"
     */
	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	/**
     * @hibernate.property length="255" not-null="true"
     */
	public String getSettingValue() {
		return settingValue;
	}
	
	/**
     * @spring.validator type="required"
     */
	public void setSettingValue(String settingValue) {
		this.settingValue = settingValue;
	}

	public String[] getSettingValues() {
		
		if(this.settingValue==null) {
			return null;
		} else {
			return this.settingValue.split(Setting.SPLIT_CHAR);
		}
				
		
	}


}
