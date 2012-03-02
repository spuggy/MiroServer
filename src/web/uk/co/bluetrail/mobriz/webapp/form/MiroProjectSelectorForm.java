package uk.co.bluetrail.mobriz.webapp.form;

public class MiroProjectSelectorForm {

	String[] selectedProjects;
	String[] teamUsers;
	String version;
    String id;
    String title;
    String miroTeamName;
    
    
    
	
	/**
	 * @hibernate.property 
	 * @return the miroTeamName
	 */
	public String getMiroTeamName() {
		return miroTeamName;
	}
	/**
	 * @param miroTeamName the miroTeamName to set
	 */
	public void setMiroTeamName(String miroTeamName) {
		this.miroTeamName = miroTeamName;
	}
	/**
	 * @hibernate.property 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @hibernate.property 
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		
		if(id == null) {
			this.id = id;
			return;
		}
		
		if(id.indexOf(',') !=-1) {
			String[] bits = id.split(",");
			this.id = bits[0];
		} else {
			this.id = id;
		}
	}
	/**
	 * @hibernate.property 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @hibernate.property 
	 * @return the selectedProjects
	 */
	public String[] getSelectedProjects() {
		return selectedProjects;
	}
	/**
	 * @param selectedProjects the selectedProjects to set
	 */
	public void setSelectedProjects(String[] selectedProjects) {
		this.selectedProjects = selectedProjects;
	}
	/**
	 * @hibernate.property 
	 * @return the teamUsers
	 */
	public String[] getTeamUsers() {
		return teamUsers;
	}
	/**
	 * @param teamUsers the teamUsers to set
	 */
	public void setTeamUsers(String[] teamUsers) {
		this.teamUsers = teamUsers;
	}
	/**
	 * @hibernate.property 
	 * @return the allUsers
	 */
	public String[] getAllUsers() {
		return allUsers;
	}
	/**
	 * @param allUsers the allUsers to set
	 */
	public void setAllUsers(String[] allUsers) {
		this.allUsers = allUsers;
	}
	/**
	 * @hibernate.property 
	 * @return the selectType
	 */
	public String getSelectType() {
		return selectType;
	}
	/**
	 * @param selectType the selectType to set
	 */
	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}
	String[] allUsers;
	String selectType;

		
	
	
}
