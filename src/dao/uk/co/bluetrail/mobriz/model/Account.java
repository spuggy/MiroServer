package uk.co.bluetrail.mobriz.model;

/**
 * Account information for each client 
 *
 * <p><a href="Account.java.html"><i>View Source</i></a>
 *
 *
 * @hibernate.class table="mr.account"
 */
public class Account extends BaseObject {

	private Long id ;
	private String companyName ; 
	  
	
	@Override
	public boolean equals(Object o) {
		
		return ((Account) o).getId().longValue() == getId().longValue() ;
	}

	
	/**
     * @hibernate.property length="50" not-null="true"
     */
	public String getCompanyName() {
		return companyName;
	}

	/**
     * @spring.validator type="required"
     */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public int hashCode() {
		
		return id.hashCode();
	}

	@Override
	public String toString() {
		
		return getCompanyName();
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
	
	
}
