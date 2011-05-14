package uk.co.bluetrail.mobriz.model;

import java.util.Date;

/**
 * SurveyElement Interface
 * 
 * Object that enforces some fields important for the survey elememnts
 * like surveys and questions
 * 
 * <p><a href="SurveyElement.java.html"><i>View Source</i></a></p>
 * 
 * @author <a href="mailto:rspence@bluetrail.co.uk">Richard Spence</a>
 * 
 */
public interface SurveyElement  {

	
	
	public Long getCheckPoint() ;
	public void setCheckPoint(Long checkPoint);
	public Date getCreated_on() ;
	public void setCreated_on(Date created_on);
	public Long getCreatedBy_id() ;
	public void setCreatedBy_id(Long createdBy_id);
	public Long getLastUpdatedBy_id();
	public void setLastUpdatedBy_id(Long lastUpdateBy_id) ;
	public Date getUpdated_at();
	public void setUpdated_at(Date updated_at);
	
	//force version onto survey element so we can check if adds or deletes
	public Integer getVersion();
	public void setVersion(Integer version);
	public boolean isNew() ;
	
	
	public boolean isDeleted() ;	
	public void setDeleted(boolean deleted) ;
	
	

}
