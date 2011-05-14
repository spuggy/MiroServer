package uk.co.bluetrail.mobriz.webapp.form;

import javax.servlet.http.HttpServletRequest;

import uk.co.bluetrail.mobriz.model.User;



/**
 * A from used to send an SMS message to a user.
 * 
 * @author <a href="mailto:rspence@bluetrail.co.uk">Richard Spence</a>
 * 
 * @struts.form name="SMSForm"
 */
public class SmsMessageForm  {
	
	private String username ; 
	private String user_id ; 
	private String phoneNumber ; 
	private String smsMessage ; 
	private String smsType;
	private String pinNumber ;
	private String downloadURL ;
	
	public String getDownloadURL() {
		return downloadURL;
	}

	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}

	public SmsMessageForm() {
		
	}
	
	public SmsMessageForm(User user) {
	
		this.username = user.getUsername();
		this.user_id = user.getId().toString();
		this.phoneNumber = user.getPhoneNumber() ;
		this.pinNumber = user.getPinNumber() ;
	}
	
    /**
	 * @return Returns the pinNumber.
	 */
	public String getPinNumber() {
		return this.pinNumber;
	}

	/**
	 * @param pinNumber The pinNumber to set.
	 */
	public void setPinNumber(String pinNumber) {
		this.pinNumber = pinNumber;
	}

	/**
	 * @return Returns the smsMessage.
	 */
	public String getSmsMessage() {
		return this.smsMessage;
	}

	/**
	 * @param smsMessage The smsMessage to set.
	 */
	public void setSmsMessage(String smsMessage) {
		this.smsMessage = smsMessage;
	}

	/**
	 * @return Returns the smsType.
	 */
	public String getSmsType() {
		return this.smsType;
	}

	/**
	 * @param smsType The smsType to set.
	 */
	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}

	/**
	 * @return Returns the phoneNumber.
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber The phoneNumber to set.
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	
	
}
