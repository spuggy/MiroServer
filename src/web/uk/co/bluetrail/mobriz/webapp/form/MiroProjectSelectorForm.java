package uk.co.bluetrail.mobriz.webapp.form;

public class MiroProjectSelectorForm {

	String[] selectedProjects;
	String[] teamUsers;
	
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
