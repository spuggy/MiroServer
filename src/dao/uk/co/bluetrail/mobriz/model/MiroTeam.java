package uk.co.bluetrail.mobriz.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.BaseObject;

/**
 * MiroTeam  information 
 *
 * <p><a href="MiroTeam.java.html"><i>View Source</i></a>
 *
 *
 * @hibernate.class table="mr.miroteam"
 */
public class MiroTeam  extends BaseObject implements SurveyElement  {

	
	private static final long serialVersionUID = -3533292341403024042L;
	private Long id;
	private String miroReportName;
	private User practitioner;
	private Set members;
	private List teamResults;
	private Long checkPoint = null; 
	private Long lastUpdatedBy_id = null; 
	private Date updated_at = null ;	
	private Long createdBy_id = null;
	private Date created_on = null;
	protected Integer version;
	boolean deleted ;
	
	
	/**
     * Returns the user's roles.
     * 
     * @return Set
     * 
     * @hibernate.set table="mr.miroteam_user" cascade="all" lazy="false"
     * @hibernate.collection-key column="miro_team_id"
     * @hibernate.collection-many-to-many class="uk.co.bluetrail.mobriz.model.User"   column="user_id"
     */
	public Set getMembers() {
		return members;
	}

	
	
	public void setMembers(Set members) {
		this.members = members;
	}
	
	

	


	@Override
	public boolean equals(Object o) {
		
		return ((MiroTeam) o).getId().longValue() == this.id.longValue();
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return this.miroReportName;
	}

	/**
	 * @return Returns the id.
	 * @hibernate.id column="id"
     *  generator-class="increment" unsaved-value="null"	 
	 **/
	public Long getId() {
		return id;
	}

	public String getPractitionerName() {
		return practitioner.getFullName();
	}

	/**
     * @hibernate.property length="100" not-null="true"
     */
	public String getMiroTeamReportName() {
		return this.miroReportName;
	}

	public String getCompany() {
		String comp = practitioner.getCompany();
		if(comp==null) {
			return "";
		} else {
			return comp;
		}
	}

	public String[] getPractitionerAddress() {
		String[] add = {"22 Rail way Cuttings", "East Cheam"};
		return add;
	}

	public String getPractitionerTelNo() {
		return s(this.practitioner.getPhoneNumber());
	}

	public String getPractitionerEmail() {
		return s(this.practitioner.getEmail());
	}

	public String getWebaddress() {
		return s(this.practitioner.getWebaddress());
	}
	
	public User getPractitioner(){
		return practitioner;
		
	}

	public void setId(Long id) {
		this.id = id;
		
	}

	public void setMiroTeamReportName(String miroReportName) {
		this.miroReportName = miroReportName;
		
	}

	public void setPractitioner(User practitioner) {
		this.practitioner = practitioner;
		
	}
	
	private String s(String str){
		if(str==null) {
			return ""; 
		} else {
			return str;
		}
	}

	
	

	public void setResults(List teamResults) {
		this.teamResults = teamResults;
		
	}
	
	public List getTeamResults() {
		return this.teamResults;
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

public boolean isDeleted() {
	// TODO Auto-generated method stub
	return false;
}

public void setDeleted(boolean deleted) {
	this.deleted = deleted;
}
}
