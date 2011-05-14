package uk.co.bluetrail.mobriz.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Date;

/**
* MiroTransaction class
* 
* Defines a MiroTransaction - for buying credits
* 
* <p><a href="MiroTransaction.java.html"><i>View Source</i></a></p>
* 
* @author <a href="mailto:rspence@bluetrail.co.uk">Richard Spence</a>
* 
* @hibernate.class table="mr.mirotransactions"
*/
public class MiroTransaction extends BaseObject implements SurveyElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int OK = 2;
	public static final int FAILURE = 3;
	public static final int OK_PROCESSED = 4;
	public static final int FAILURE_PROCESSED = 5;
	public static final int OK_BUY_REPORT = 6;
	Long id ;	
	Long user_id ;
	int status ; 
	int errorCode ;
	int paymentMethod ;
	int credits ;
	double transValue;
	private Long checkPoint = null; 
	private Long lastUpdatedBy_id = null; 
	private Date updated_at = null ;	
	private Long createdBy_id = null;
	private Date created_on = null;
	protected Integer version;
	protected boolean deleted = false ; 
	private String paymentStatus = null;
	private String paymentStatusDetail = null;
	private String paymentTransId = null;
	
	
	
	
	
	
	
		
		
		
	
	/**
	 * @hibernate.property 
	 * @return the paymentStatus
	 */
	public String getPaymentStatus() {
		return paymentStatus;
	}
	/**
	 * @param paymentStatus the paymentStatus to set
	 */
	public void setPaymentStatus(String paymentStatus) {
		try{
		this.paymentStatus = paymentStatus.substring(0, 200);
		} catch(RuntimeException e) {
			this.paymentStatus = paymentStatus;
		}
	}
	/**
	 * @hibernate.property 
	 * @return the paymentStatusDetail
	 */
	public String getPaymentStatusDetail() {
		
		
		
		return paymentStatusDetail;
	}
	/**
	 * @param paymentStatusDetail the paymentStatusDetail to set
	 */
	public void setPaymentStatusDetail(String paymentStatusDetail) {
		try {
			this.paymentStatusDetail = paymentStatusDetail.substring(0, 200); 
		} catch(RuntimeException e) {
			this.paymentStatusDetail = paymentStatusDetail;
		}
		
	}
	/**
	 * @hibernate.property 
	 * @return the paymentTransId
	 */
	public String getPaymentTransId() {
		return paymentTransId;
	}
	/**
	 * @param paymentTransId the paymentTransId to set
	 */
	public void setPaymentTransId(String paymentTransId) {
		
		try {
			this.paymentTransId = paymentTransId.substring(0, 200);
		} catch (RuntimeException e) {
			this.paymentTransId = paymentTransId;
		}
	
	}
	/**
	 * @hibernate.property 
	 * @return the transValue
	 */
	public double getTransValue() {
		return transValue;
	}
	/**
	 * @param transValue the transValue to set
	 */
	public void setTransValue(double transValue) {
		this.transValue = transValue;
	}
	/**
	 * @hibernate.property 
	 * @return the credits
	 */
	public int getCredits() {
		return credits;
	}
	/**
     * @spring.validator type="required"
     */
	public void setCredits(int credits) {
		this.credits = credits;
	}
	/**
	 * @hibernate.property 
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @return Returns the id.
	 * @hibernate.id column="id"
     *  generator-class="increment" unsaved-value="null"
	 * @struts.form-field
	 **/
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
	 * @return the paymentMethod
	 */
	public int getPaymentMethod() {
		return paymentMethod;
	}
	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(int paymentMethod) {
		this.paymentMethod = paymentMethod;
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
	 * @return the user_id
	 */
	public Long getUser_id() {
		return user_id;
	}
	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public boolean isNew() {
	
		
		
		if(version == null) {
			return true;
		}
		if (version.compareTo(new Integer(0))==0 ) {
			return true ; 
		} else {
			return false ; 
		}
	}
	/**
	 * @return Returns the deleted.
	 * @hibernate.property
	 * @struts.form-field
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
	 * @struts.form-field
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
	 * @struts.form-field
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
	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.model.BaseObject#toString()
	 */
	public String toString() {
		if(id == null ){
			return "" ; 
		} else {
			return id.toString();
		}
	}
	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.model.BaseObject#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if(id.compareTo((Long) o) == 0 ){
			return true; 
		} else {
			return false;
		}
	}
	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.model.BaseObject#hashCode()
	 */
	public int hashCode() {
		
		if (id != null) {
			return id.hashCode();
		} else {
			return 0;
		}
	}
	public String getTransValueStr() {
		DecimalFormat currency = new DecimalFormat("0.00");
		return ""+currency.format(this.getTransValue());
		
	}

}
