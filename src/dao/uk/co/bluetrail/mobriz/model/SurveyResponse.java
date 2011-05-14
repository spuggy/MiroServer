/*
 * Created on 23-Jan-2006
 *
 */
package uk.co.bluetrail.mobriz.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtilsBean;

import uk.co.bluetrail.mobriz.Constants;

/**
 * Response class
 * 
 * <p>
 * <a href="Answer.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:rspence@bluetrail.co.uk">Richard Spence</a>
 * 
 * @struts.form include-all="false" extends="BaseForm"
 * @hibernate.class table="mr.responses"
 */
public class SurveyResponse extends BaseObject implements SurveyElement {

	public static final String SEP = ";";



	

	private Long id;

	private Long survey_id;

	private int total;

	private String question_trail;

	private String answer_trail;

	private Survey survey;

	private Long checkPoint = null;

	private Long lastUpdateBy_id = null;

	private Date updated_at = null;

	private Long createdBy_id = null;

	private Date created_on = null;

	protected Integer version;

	protected boolean deleted = false;

	private boolean photosStamped = false;

	private HashMap map = null;

	private User user;

	private boolean alertsProcessed = false;

	
	/**
	 * @return Returns the user. mapped in external matadata/dao file
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * @param user
	 *            The user to set.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	public String getAnswer(Question question) {

		if (map == null) {
			buildHashMap();
		}

		String ans = (String) map.get(question.getId().toString());

		if (ans == null) {
			return ("n/a");
		} else {
			if (Question.QTYPE_DATE.equals(question.getQTypeStr())) {
				return this.getDateString(ans);
			}

			return ans;
		}

	}

	private String getDateString(String rawAnswer) {

		Calendar calendar = Calendar.getInstance();

		long dtLong = 0;

		try {
			dtLong = Long.parseLong(rawAnswer);
		} catch (Exception e) {
			// assume errror parsing means not a number
			return rawAnswer;
		}
		Date dt = new Date(dtLong);

		calendar.setTime(dt);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int date = calendar.get(Calendar.DATE);

		StringBuffer lop = new StringBuffer();

		lop.append(date);
		lop.append('/');
		lop.append(month);
		lop.append('/');
		lop.append(year);

		return lop.toString();

	}

	private void buildHashMap() {

		map = new HashMap();
		String[] questions = question_trail.split("~");
		String[] answers = answer_trail.split("~");

		for (int i = 0; i < answers.length; i++) {
			map.put(questions[i], answers[i]);
		}

	}

	public String[] getQuestionsAnswered() {
		if (question_trail == null) {
			return null;
		} else {
			return question_trail.split("~");
		}

	}

	/**
	 * @return Returns the answer_trail.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public String getAnswer_trail() {
		return answer_trail;
	}

	/**
	 * @param answer_trail
	 *            The answer_trail to set.
	 */
	public void setAnswer_trail(String answer_trail) {
		this.answer_trail = answer_trail;
	}

	/**
	 * @return Returns the question_trail.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public String getQuestion_trail() {
		return question_trail;
	}

	/**
	 * @param question_trail
	 *            The question_trail to set.
	 */
	public void setQuestion_trail(String question_trail) {
		this.question_trail = question_trail;
	}

	/**
	 * mapped in merge file as we need stuff that
	 * 
	 * @hibernat stuff cannot do
	 * @return
	 */
	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
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

	/**
	 * @return Returns the deleted.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param deleted
	 *            The deleted to set.
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
	 * @param version
	 *            The version to set.
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
	 * @param checkPoint
	 *            The checkPoint to set.
	 */
	public void setCheckPoint(Long checkPoint) {
		this.checkPoint = checkPoint;
	}

	/**
	 * @return Returns the createdBy.
	 * @hibernate.property
	 */
	public Long getCreatedBy_id() {
		return createdBy_id;
	}

	/**
	 * @param createdBy_id
	 *            The createdBy to set.
	 */
	public void setCreatedBy_id(Long createdBy_id) {
		this.createdBy_id = createdBy_id;
	}

	/**
	 * @return Returns the lastUpdateBy.
	 * @hibernate.property
	 */
	public Long getLastUpdatedBy_id() {
		return lastUpdateBy_id;
	}

	/**
	 * @param lastUpdateBy_id
	 *            The lastUpdateBy to set.
	 */
	public void setLastUpdatedBy_id(Long lastUpdatedBy_id) {
		this.lastUpdateBy_id = lastUpdatedBy_id;
	}

	/**
	 * @return Returns the updated_at.
	 * @hibernate.property
	 */
	public Date getUpdated_at() {
		return updated_at;
	}

	/**
	 * @param updated_at
	 *            The updated_at to set.
	 */
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns the id.
	 * @hibernate.id column="id" generator-class="increment"
	 *               unsaved-value="null"
	 * @struts.form-field
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return Returns the jQuestion_id.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public Long getSurvey_id() {
		return survey_id;
	}

	public void setSurvey_id(Long survey_id) {
		this.survey_id = survey_id;
	}

	/**
	 * @return Returns the jQuestion_id.
	 * @hibernate.property
	 * @struts.form-field
	 */
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.bluetrail.mobriz.model.BaseObject#toString()
	 */
	public String toString() {

		return id.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.bluetrail.mobriz.model.BaseObject#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {

		return id.compareTo((Long) o) == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.bluetrail.mobriz.model.BaseObject#hashCode()
	 */
	public int hashCode() {

		return id.hashCode();
	}

	/**
	 * @return Returns the created_on.
	 * @hibernate.property
	 */
	public Date getCreated_on() {

		return created_on;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.bluetrail.mobriz.model.SurveyElement#setCreated_on(java.util.Date)
	 */
	public void setCreated_on(Date created_on) {
		this.created_on = created_on;

	}

	public String getPhoto(Question question) {
		try {
			String imageData[] = null;
			StringBuffer imgName = new StringBuffer();
			imageData = getAnswer(question).split(SurveyResponse.SEP);
			imgName = new StringBuffer();
			imgName.append('/');
			imgName.append(Constants.PHOTODIR);
			imgName.append('/');
			imgName.append(getSurvey_id());
			imgName.append('/');
			imgName.append(getCreatedBy_id());
			imgName.append('_');
			imgName.append(imageData[1]);
			imgName.append('.');
			imgName.append(imageData[2]);

			return imgName.toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @return photosHaveBeenStamped
	 * @hibernate.property
	 */
	public boolean isPhotosStamped() {
		return photosStamped;
	}

	public void setPhotosStamped(boolean photosStamped) {
		this.photosStamped = photosStamped;
	}

	public String getFieldValue(String fieldName) throws Exception {
		BeanUtilsBean bu = BeanUtilsBean.getInstance();
	    Object fval = null;
		try{
			Object fVal =  bu.getProperty(this, fieldName) ;
			return fVal.toString();
		} catch(NoSuchMethodException nsm){
		
			Question question = this.getSurvey().getQuestionByShortName(fieldName);
			if(question == null) {
				return "??";
			}
			
			if(question.getQTypeStr().equals(Question.QTYPE_LOOKUP)) {
				String lookup  = this.getAnswer(question);
				if(lookup==null) {
					return "??" ;	
				}
				String[] lookupValues = lookup.split(SurveyResponse.SEP);
				lookup = lookupValues[0].trim();
				try{
					if(lookup.charAt(lookup.length()-1)==',') {
						lookup = lookup.substring(0,lookup.length()-1 );
					}
				} catch(Exception e) {
					
				}
				return lookup ;
				
				
			}
			
			return this.getAnswer(question);
			
		} 
		
		
	}

	public void setAlertsProcessed(boolean b) {
		this.alertsProcessed = b;

	}

	/**
	 * @hibernate.property
	 */
	public boolean getAlertsProcessed() {
		return this.alertsProcessed;

	}

	public String getFieldImageName(String fieldName) {

		try {
			String fValue = this.getFieldValue(fieldName);
			Question question = this.getSurvey().getQuestionByShortName(
					fieldName);
			if (question == null) {
				return "??";
			}
			if (Question.QTYPE_PICTURE.equals(question.getQTypeStr())) {
				return this.getPhoto(question);
			} else {
				return "";
			}

		} catch (Exception e) {
			return "??";
		}

	}

}
