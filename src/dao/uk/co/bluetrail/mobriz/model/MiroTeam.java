package uk.co.bluetrail.mobriz.model;

import java.util.ArrayList;
import java.util.List;
import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.BaseObject;


public class MiroTeam  extends BaseObject {

	
	private static final long serialVersionUID = -3533292341403024042L;
	private Long id;
	private String miroReportName;
	private User practitioner;
	private List members;
	private List teamResults;

	@Override
	public boolean equals(Object o) {
		
		return ((MiroTeam) o).getId().longValue() == this.id.longValue();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return id.hashCode();
	}

	@Override
	public String toString() {
		return this.miroReportName;
	}

	public Long getId() {
		return id;
	}

	public String getPractitionerName() {
		return practitioner.getFullName();
	}

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

	public void setTeamMembers(List members) {
		this.members = members;
		
	}
	
	public List getMembers() {
		 return members;
	}

	public void setResults(List teamResults) {
		this.teamResults = teamResults;
		
	}
	
	public List getTeamResults() {
		return this.teamResults;
	}

}
