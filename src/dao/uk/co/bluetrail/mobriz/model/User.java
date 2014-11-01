package uk.co.bluetrail.mobriz.model;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import uk.co.bluetrail.mobriz.Constants;

import java.io.Serializable;
import java.util.*;

/**
 * This class is used to generate Spring Validation rules
 * as well as the Hibernate mapping file.
 *
 * <p><a href="User.java.html"><i>View Source</i></a>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *         Updated by Dan Kibler (dan@getrolling.com)
 *  Extended to implement Acegi UserDetails interface
 *      by David Carter david@carter.net
 *
 * @hibernate.class table="app_user"
 */
public class User extends BaseObject implements SurveyElement, Serializable, UserDetails {
    private static final long serialVersionUID = 3832626162173359411L;

    protected Long id;
    protected Long project_id = new Long(0);
    protected Long account_id;
    private Account account ;
    protected String username;                    // required
    protected String password;                    // required
    protected String confirmPassword;
    protected String firstName;                   // required
    protected String lastName;                    // required   
    protected String email;                       // required; unique
    protected String phoneNumber;
    protected String passwordHint;
    protected Integer version;
    protected Set roles = new HashSet();
    protected boolean enabled;
    protected boolean accountExpired;
    protected boolean accountLocked;
    protected boolean credentialsExpired;
    protected String pinNumber ;
    protected String employeeRef ;
    boolean deleted ;
    protected Long response_id = new Long(0);
    protected int creditBalance = 0 ;
    
    
    //address stuff
    private String address1;
    private String address2;
    private String city ;
    private String county;
    private String postcode;
    private String webaddress;
    private String company;
    private String department;
  
    private Date created_on = new Date();
    private boolean oldreport = false; 
    private Long createdBy_id = null;
    private Long lastUpdatedBy_id = null;
	private Date updated_at = null;
	private Long checkPoint = new Long(0L);
	
    
	public static final int INVITE_NOT_SENT = 0;
	public static final int INVITE_SENT  = 10 ; 
	public static final int ASSESSMENT_COMPLETE = 20;
	public static final int PURCHASE_REPORT = 30;
	public static final int DOWNLOAD_REPORT = 40;

	public static final int INVALID_REPORT = 5;
	public static final int OK =1;
	public static final int ZERO_BALANCE = 4;
    
	private int status = User.INVITE_NOT_SENT ;
	private Long miroTeam_id ;
    private Date miroTeamAdded_on ;

    /**
     * @hibernate.property
     * @return the default survey id for assessments for this practitioner
     */
    public String getDefault_survey_id() {
        if(default_survey_id==null) {
            return Constants.Survey_id_Mirov11.toString();
        } else {
            return default_survey_id;
        }

    }

    public void setDefault_survey_id(String default_survey_id) {
        this.default_survey_id = default_survey_id;
    }

    private String default_survey_id;


    /**
     * @hibernate.property
     * @return the MiroTeamAdded_on
     */
    public Date getMiroTeamAdded_on() {
        return miroTeamAdded_on;
    }

    public void setMiroTeamAdded_on(Date miroTeamAdded_on) {
        this.miroTeamAdded_on = miroTeamAdded_on;
    }


	/**
	 * @return Returns the pinNumber.
	 * @hibernate.property
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * @spring.validator type="required"
	 * @param department to set.
	 */
	public void setDepartment(String department) {
	
		
		this.department = department ; 
		
	}
	
	
	/**
	 * @hibernate.property 
	 * @return the miroTeam_id
	 */
	public Long getMiroTeam_id() {
		return miroTeam_id;
	}

	/**
	 * @param miroTeamId the miroTeam_id to set
	 */
	public void setMiroTeam_id(Long miroTeamId) {
		miroTeam_id = miroTeamId;
	}

	/**
	 * @hibernate.property 
	 * @return the lastUpdatedBy_id
	 */
	public Long getLastUpdatedBy_id() {
		return lastUpdatedBy_id;
	}

	/**
	 * @param lastUpdatedBy_id the lastUpdatedBy_id to set
	 */
	public void setLastUpdatedBy_id(Long lastUpdatedBy_id) {
		this.lastUpdatedBy_id = lastUpdatedBy_id;
	}

	/**
	 * @hibernate.property 
	 * @return the checkPoint
	 */
	public Long getCheckPoint() {
		return checkPoint;
	}

	/**
	 * @param checkPoint the checkPoint to set
	 */
	public void setCheckPoint(Long checkPoint) {
		this.checkPoint = checkPoint;
	}

	

	/**
	 * @hibernate.property 
	 * @return the updated_at
	 */
	public Date getUpdated_at() {
		return updated_at;
	}

	/**
	 * @param updated_at the updated_at to set
	 */
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	/**
	 * @hibernate.property 
	 * @return the createdBy_id
	 */
	public Long getCreatedBy_id() {
		return createdBy_id;
	}

	/**
	 * @param createdBy_id the createdBy_id to set
	 */
	public void setCreatedBy_id(Long createdBy_id) {
		this.createdBy_id = createdBy_id;
	}

	/**
	 * @hibernate.property 
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

    
    
    /**
	 * @hibernate.property 
	 * @return the creditBalance
	 */
	public int getCreditBalance() {
		return creditBalance;
	}

	/**
	 * @param creditBalance the creditBalance to set
	 */
	public void setCreditBalance(int creditBalance) {
		this.creditBalance = creditBalance;
	}

	/**
	 * @hibernate.property 
	 * @return the response_id
	 */
	public Long getResponse_id() {
		return response_id;
	}

	/**
	 * @param response_id the response_id to set
	 */
	public void setResponse_id(Long response_id) {
		this.response_id = response_id;
	}

	/**
	 * @hibernate.property 
	 * @return the project_id
	 */
	public Long getProject_id() {
		return project_id;
	}

	/**
	 * @param project_id the project_id to set
	 */
	public void setProject_id(Long project_id) {
		this.project_id = project_id;
	}

	/**
     * @hibernate.property 
     */public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return Returns the employss ref.
	 * @hibernate.property
	 */    
    public String getEmployeeRef() {
		return employeeRef;
	}
    
    /**
     * @param employeeRef
     */
	public void setEmployeeRef(String employeeRef) {
		this.employeeRef = employeeRef;
	}
	/**
	 * @return Returns the pinNumber.
	 * @hibernate.property
	 */
	public String getPinNumber() {
		return pinNumber;
	}
	/**
	 * @spring.validator type="required"
	 * @param pinNumber The pinNumber to set.
	 */
	public void setPinNumber(String pinNumber) {
		//force pin numbers to be lower case
		
		if (pinNumber != null) {
			this.pinNumber = pinNumber.toLowerCase();
		} else {
			this.pinNumber = pinNumber ; 
		}
	}
    
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

	public User() {}

    public User(String username) {
        this.username = username;
    }

    /**
     * @hibernate.id column="id" generator-class="increment" unsaved-value="null"
     */
    public Long getId() {
        return id;
    }

    /**
     * @hibernate.property length="50" not-null="true" unique="true"
     */
    public String getUsername() {
        return username;
    }

    /**
     * @hibernate.property column="password" not-null="true"
     */
    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * @hibernate.property column="first_name" not-null="true" length="50"
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @hibernate.property column="last_name" not-null="true" length="50"
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the full name.
     */
    public String getFullName() {
        return firstName + ' ' + lastName;
    }

    

    /**
     * @hibernate.property name="email" not-null="true" unique="true"
     */
    public String getEmail() {
        return email;
    }

    

    /**
     * @hibernate.property column="password_hint" not-null="false"
     */
    public String getPasswordHint() {
        return passwordHint;
    }

    /**
     * @hibernate.set table="user_role" cascade="save-update" lazy="false"
     * @hibernate.collection-key column="user_id"
     * @hibernate.collection-many-to-many class="uk.co.bluetrail.mobriz.model.Role" column="role_id"
     */
    public Set getRoles() {
        return roles;
    }

    /**
     * Adds a role for the user
     * @param role
     */
    public void addRole(Role role) {
        getRoles().add(role);
    }

    /**
     * @see org.acegisecurity.userdetails.UserDetails#getAuthorities()
     */
    public GrantedAuthority[] getAuthorities() {
        return (GrantedAuthority[]) roles.toArray(new GrantedAuthority[0]);
    }

    /**
     * @hibernate.version
     */
    public Integer getVersion() {
        return version;
    }
    
    /**
     * @hibernate.property column="enabled" type="yes_no"
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * @hibernate.property column="account_expired" not-null="true" type="yes_no"
     */
    public boolean isAccountExpired() {
        return accountExpired;
    }
    
    /**
     * @see org.acegisecurity.userdetails.UserDetails#isAccountNonExpired()
     */
    public boolean isAccountNonExpired() {
        return !isAccountExpired();
    }

    /**
     * @hibernate.property column="account_locked" not-null="true" type="yes_no"
     */
    public boolean isAccountLocked() {
        return accountLocked;
    }
    
    /**
     * @see org.acegisecurity.userdetails.UserDetails#isAccountNonLocked()
     */
    public boolean isAccountNonLocked() {
        return !isAccountLocked();
    }

    /**
     * @hibernate.property column="credentials_expired" not-null="true"  type="yes_no"
     */
    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }
    
    /**
     * @see org.acegisecurity.userdetails.UserDetails#isCredentialsNonExpired()
     */
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @spring.validator type="required"
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @spring.validator type="required"
     * @spring.validator type="twofields" msgkey="errors.twofields"
     * @spring.validator-args arg1resource="user.password"
     * @spring.validator-args arg1resource="user.confirmPassword"
     * @spring.validator-var name="secondProperty" value="confirmPassword"
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @spring.validator type="required"
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * @spring.validator type="required"
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @spring.validator type="required"
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    

    /**
     * @spring.validator type="required"
     * @spring.validator type="email"
     */
    public void setEmail(String email) {
        this.email = email;
    }

   /**
     * @spring.validator type="required"  
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @hibernate.property column="phone_number" not-null="false"
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    
    /**
     * @spring.validator type="required"
     */
    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public void setRoles(Set roles) {
        this.roles = roles;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * Convert user roles to LabelValue objects for convenience.  
     */
    public List getRoleList() {
        List userRoles = new ArrayList();

        if (this.roles != null) {
            for (Iterator it = roles.iterator(); it.hasNext();) {
                Role role = (Role) it.next();

                // convert the user's roles to LabelValue Objects
                userRoles.add(new LabelValue(role.getName(),
                                             role.getName()));
            }
        }

        return userRoles;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }
    
    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        final User user = (User) o;

        if (username != null ? !username.equals(user.getUsername()) : user.getUsername() != null) return false;

        return true;
    }

    public int hashCode() {
        return (username != null ? username.hashCode() : 0);
    }

    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this,
                ToStringStyle.DEFAULT_STYLE).append("username", this.username)
                .append("enabled", this.enabled)
                .append("accountExpired",this.accountExpired)
                .append("credentialsExpired",this.credentialsExpired)
                .append("accountLocked",this.accountLocked);

        GrantedAuthority[] auths = this.getAuthorities();
        if (auths != null) {
            sb.append("Granted Authorities: ");

            for (int i = 0; i < auths.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(auths[i].toString());
            }
        } else {
            sb.append("No Granted Authorities");
        }
        return sb.toString();
    }

	public boolean isSysAdmin() {
		
		List roles = this.getRoleList() ; 
		if(roles == null) {
			return false;
		}
		 
		LabelValue role = null ; 
		Iterator itr = roles.iterator() ;
		while(itr.hasNext()) {
        	role = (LabelValue) itr.next() ;
        	
        
        	if(role.getValue().equals(Constants.SYSADMIN_ROLE)) {
        		return true;
        	}        		
        }
		
		return false;
		
	}

	
	public boolean isTeamReportCreator() {
		
		List roles = this.getRoleList() ; 
		if(roles == null) {
			return false;
		}
		 
		LabelValue role = null ; 
		Iterator itr = roles.iterator() ;
		while(itr.hasNext()) {
        	role = (LabelValue) itr.next() ;
        	
        
        	if(role.getValue().equals(Constants.TEAMREPORT_ROLE)) {
        		return true;
        	}        		
        }
		
		return false;
		
	}

	
	
	public boolean isComplete() {
		
		if(this.response_id == null || this.response_id.longValue() == 0L) {
			return false ;
		} else {
			return true;
		}
		
	}

	public String getReportFileName() {
		
		if(this.response_id == null) {
			throw new SurveyException("response id is null in getReportFileName");
		}
		
		String fName = this.getFirstName().replaceAll("\\p{Punct}+", "");
		String lName = this.getLastName().replaceAll("\\p{Punct}+", "");
		return fName + "_" + lName + "_" + this.getResponse_id();
		
	}
	
	public String getOldReportFileName() {
		
		if(this.response_id == null) {
			throw new SurveyException("response id is null in getReportFileName");
		}
		
		
		return this.getLastName() + "_" + this.getResponse_id();
	}

	/**
	 * @hibernate.property 
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @hibernate.property 
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @hibernate.property 
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @hibernate.property 
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @hibernate.property 
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}

	/**
	 * @param county the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	/**
	 * @hibernate.property 
	 * @return the postcode
	 */
	public String getPostcode() {
		return postcode;
	}

	/**
	 * @param postcode the postcode to set
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	/**
	 * @hibernate.property 
	 * @return the webadddress
	 */
	public String getWebaddress() {
		return webaddress;
	}

	/**
	 * @param webadddress the webadddress to set
	 */
	public void setWebaddress(String webadddress) {
		this.webaddress = webadddress;
	}

	/**
	 * @hibernate.property 
	 * @return the created_on
	 */
	public Date getCreated_on() {
		return created_on;
	}

	/**
	 * @param created_on the created_on to set
	 */
	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}

	/**
	 * @hibernate.property 
	 * @return the oldreport
	 */
	public boolean isOldreport() {
		return oldreport;
	}

	/**
	 * @param oldreport the oldreport to set
	 */
	public void setOldreport(boolean oldreport) {
		this.oldreport = oldreport;
	}

	public boolean isNew() {
		if(this.createdBy_id==null) {
			return true;
		} else {
			return false;
		}
		
		
	}

	public String getInitials(HashMap existingInitials) {
		
		String initials = this.getInitials();
		
		if(existingInitials.get(initials)==null) {
			existingInitials.put(initials,initials);
			return initials;
		} 
		String newInitials = null;
		for(int i = 1 ; i < 10; i++) {
			newInitials = initials + i;
			if(existingInitials.get(newInitials)==null) {
				existingInitials.put(newInitials,newInitials);
				return newInitials;
			} 
		}
		
		return initials;
	}

	private String getInitials() {
		
		StringBuffer i = new StringBuffer();
		if(this.firstName!=null) {
			i.append(this.firstName.charAt(0));
		}
		if(this.lastName!=null) {
			i.append(this.lastName.charAt(0));
		}
		
		return i.toString().toUpperCase();
		
		
	}



}
