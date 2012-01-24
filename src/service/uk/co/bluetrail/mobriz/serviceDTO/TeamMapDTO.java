package uk.co.bluetrail.mobriz.serviceDTO;

import java.io.Serializable;

public class TeamMapDTO implements Serializable {

	Long   id;
	String fullName ;
	String initials ;
	String leadingModeText;
	String leadingMode;
	String secondaryMode;
	String secondaryModeText;
	
	
	
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @hibernate.property 
	 * @return the leadingMode
	 */
	public String getLeadingMode() {
		return leadingMode;
	}
	/**
	 * @param leadingMode the leadingMode to set
	 */
	public void setLeadingMode(String leadingMode) {
		this.leadingMode = leadingMode;
	}
	/**
	 * @hibernate.property 
	 * @return the leadingModeText
	 */
	public String getLeadingModeText() {
		return leadingModeText;
	}
	/**
	 * @param leadingModeText the leadingModeText to set
	 */
	public void setLeadingModeText(String leadingModeText) {
		this.leadingModeText = leadingModeText;
	}
	/**
	 * @hibernate.property 
	 * @return the secondaryMode
	 */
	public String getSecondaryMode() {
		return secondaryMode;
	}
	/**
	 * @param secondaryMode the secondaryMode to set
	 */
	public void setSecondaryMode(String secondaryMode) {
		this.secondaryMode = secondaryMode;
	}
	/**
	 * @hibernate.property 
	 * @return the secondaryModeTest
	 */
	public String getSecondaryModeText() {
		return secondaryModeText;
	}
	/**
	 * @param secondaryModeTest the secondaryModeTest to set
	 */
	public void setSecondaryModeText(String secondaryModeText) {
		this.secondaryModeText = secondaryModeText;
	}
	/**
	 * @hibernate.property 
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @hibernate.property 
	 * @return the initials
	 */
	public String getInitials() {
		return initials;
	}
	/**
	 * @param initials the initials to set
	 */
	public void setInitials(String initials) {
		this.initials = initials;
	}
	
	
	
	
}
