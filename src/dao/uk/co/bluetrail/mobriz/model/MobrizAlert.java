package uk.co.bluetrail.mobriz.model;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * stores an alert record used to send email alerts etc
 * 
 * <p>
 * <a href="MobrizAlert.java.html"><i>View Source</i></a>
 * 
 * 
 * 
 */
public class MobrizAlert extends BaseObject implements SurveyElement {

	private final Log log = LogFactory.getLog(MobrizAlert.class);

	protected Long account_id;

	private Long id;

	private String alertTitle;

	private boolean stopProcessing;

	private int executionOrder;

	private String alertRules;

	private boolean enabled;

	private Long checkPoint = null;

	private Long lastUpdatedBy_id = null;

	private Date updated_at = null;

	private Long createdBy_id = null;

	private Date created_on = null;

	private boolean deleted = false;

	private String emailAdress;

	private String emailSubject;

	private String emailBody;

	private ArrayList attachments ;
	
	private Integer version;

	private AlertRule[] alertRuleTable;

	private SurveyResponse sr;

	/**
	 * @hibernate.property
	 * @return the emailAdress
	 */
	public String getEmailAdress() {
		return emailAdress;
	}

	/**
	 * @spring.validator type="required"
	 * @param emailAdress
	 *            the emailAdress to set
	 * @spring.validator type="email"
	 */
	public void setEmailAdress(String emailAdress) {
		this.emailAdress = emailAdress;
	}

	@Override
	public boolean equals(Object o) {

		if (id.compareTo((Long) o) == 0) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public int hashCode() {

		return id.hashCode();
	}

	@Override
	public String toString() {

		return id + " - " + this.alertTitle;
	}

	/**
	 * @hibernate.property
	 */
	public Long getCheckPoint() {
		return this.checkPoint;
	}

	/**
	 * @hibernate.property
	 */
	public Long getCreatedBy_id() {
		return this.createdBy_id;
	}

	/**
	 * @hibernate.property
	 */
	public Date getCreated_on() {
		return this.created_on;
	}

	/**
	 * @hibernate.property
	 */
	public Long getLastUpdatedBy_id() {

		return this.lastUpdatedBy_id;
	}

	/**
	 * @hibernate.property
	 */
	public Date getUpdated_at() {
		return this.updated_at;
	}

	/**
	 * @hibernate.property
	 */
	public Integer getVersion() {
		return this.version;
	}

	/**
	 * @hibernate.property
	 */
	public boolean isDeleted() {

		return this.deleted;

	}

	public boolean isNew() {
		if (version == null) {
			return true;
		}
		if (version.compareTo(new Integer(0)) == 0) {
			return true;
		} else {
			return false;
		}
	}

	public void setCheckPoint(Long checkPoint) {
		this.checkPoint = checkPoint;
	}

	public void setCreatedBy_id(Long createdBy_id) {
		this.createdBy_id = createdBy_id;

	}

	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void setLastUpdatedBy_id(Long lastUpdatedBy_id) {
		this.lastUpdatedBy_id = lastUpdatedBy_id;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;

	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @hibernate.property
	 * @return the account_id
	 */
	public Long getAccount_id() {
		return account_id;
	}

	/**
	 * @param account_id
	 *            the account_id to set
	 */
	public void setAccount_id(Long account_id) {
		this.account_id = account_id;
	}

	/**
	 * @hibernate.property
	 * @return the alertRules
	 */
	public String getAlertRules() {
		return alertRules;
	}

	/**
	 * @spring.validator type="required"
	 * @param alertRules
	 *            the alertRules to set
	 */
	public void setAlertRules(String alertRules) {
		this.alertRules = alertRules;
	}

	/**
	 * @hibernate.property length="100" not-null="true"
	 * @return the alertTitle
	 */
	public String getAlertTitle() {
		return alertTitle;
	}

	/**
	 * @spring.validator type="required"
	 * @param alertTitle
	 *            the alertTitle to set
	 */
	public void setAlertTitle(String alertTitle) {
		this.alertTitle = alertTitle;
	}

	/**
	 * @hibernate.property
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @spring.validator type="required"
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @hibernate.property
	 * @return the executionOrder
	 */
	public int getExecutionOrder() {
		return executionOrder;
	}

	/**
	 * @spring.validator type="required"
	 * @param executionOrder
	 *            the executionOrder to set
	 */
	public void setExecutionOrder(int executionOrder) {
		this.executionOrder = executionOrder;
	}

	/**
	 * @return Returns the id.
	 * @hibernate.id column="id" generator-class="increment"
	 *               unsaved-value="null"
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @hibernate.property
	 * @return the stopProcessing
	 */
	public boolean isStopProcessing() {
		return stopProcessing;
	}

	/**
	 * @spring.validator type="required"
	 * @param stopProcessing
	 *            the stopProcessing to set
	 */
	public void setStopProcessing(boolean stopProcessing) {
		this.stopProcessing = stopProcessing;
	}

	/**
	 * @hibernate.property
	 * @return the emailBody
	 */
	public String getEmailBody() {
		return emailBody;
	}

	/**
	 * @spring.validator type="required"
	 * @param emailBody
	 *            the emailBody to set
	 */
	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	/**
	 * @hibernate.property
	 * @return the emailSubject
	 */
	public String getEmailSubject() {
		return emailSubject;
	}

	/**
	 * @spring.validator type="required"
	 * @param emailSubject
	 *            the emailSubject to set
	 */
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public boolean isMatch() throws Exception {

		getAlertRuleTable();

		for (int i = 0; i < this.alertRuleTable.length; i++) {
			if (!this.alertRuleTable[i].isMatch(this.sr)) {
				return false;
			}
		}

		return true;
	}

	private AlertRule[] getAlertRuleTable() {

		if (this.alertRuleTable != null) {
			return this.alertRuleTable;
		}

		String[] alertRuleRaw = this.getAlertRules().split("#");
		this.alertRuleTable = new AlertRule[alertRuleRaw.length];

		for (int i = 0; i < alertRuleRaw.length; i++) {
			alertRuleTable[i] = new AlertRule(alertRuleRaw[i]);
		}

		return this.alertRuleTable;
	}

	public void setSurveyResponse(SurveyResponse sr) {
		this.attachments = null;
		this.sr = sr;

	}

	public String getFullTitle() {
		return this.getId() + ": " + this.getAlertTitle();
	}

	public SurveyResponse getSurveyResponse() {
		return this.sr;
	}

	public String getEmailBodyParsed() {

		return parseAndReplaceVariables(this.getEmailBody());

	}
	
	public String getEmailSubjectParsed() {

		return parseAndReplaceVariables(this.getEmailSubject());

	}

	private String parseAndReplaceVariables(String str) {

		int pos = 0;
		StringBuffer newStr = new StringBuffer();
		StringBuffer fName = null;
		String fValue;
		Question question  = null;
		while (pos < str.length()) {
			if (str.charAt(pos) == '$') {
				if (pos + 1 < str.length() && str.charAt(pos + 1) == '{') {
					fName = new StringBuffer();
					pos = pos + 2;
					while (pos < str.length() && str.charAt(pos) != '}') {
						fName.append(str.charAt(pos));
						pos++;
					}
					try {
						question = sr.getSurvey().getQuestionByShortName(fName.toString()) ;
						if(question != null && question.getQTypeStr().equals(Question.QTYPE_PICTURE)){
							fValue = sr.getFieldImageName(fName.toString());
							addAttachment(fValue);
						} else {
							fValue = sr.getFieldValue(fName.toString());
							newStr.append(fValue);
						}
					
						
					} catch (Exception e) {
						fValue = "????";
					}
					
				}

			} else {
				newStr.append(str.charAt(pos));
			}
			pos++;
		}
		return newStr.toString();

	}

	private void addAttachment(String value) {
		if(this.attachments == null) {
			attachments = new ArrayList();
		}
		this.attachments.add(value);
		
	}

	/**
	 * @hibernate.property 
	 * @return the attachments
	 */
	public ArrayList getAttachments() {
		if(attachments==null) {
			attachments = new ArrayList();
		}
		return attachments;
	}

}

class AlertRule {

	String fieldName;

	String op;

	String[] values;

	public AlertRule(String alertText) {

		String[] fields = alertText.split("~");

		fieldName = fields[0];
		op = fields[1];
		values = fields[2].split(";");

	}

	public boolean isMatch(SurveyResponse sr) throws Exception {

		String fVal = sr.getFieldValue(fieldName);

		if (fVal == null) {
			return false;
		}

		for (int i = 0; i < values.length; i++) {
			if (fVal.toString().trim().equals(values[i].trim())) {
				return true;
			}
		}

		return false;
	}

}