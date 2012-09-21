package uk.co.bluetrail.mobriz.service.impl;

import java.util.Iterator;
import java.util.List;

import uk.co.bluetrail.mobriz.model.MobrizAlert;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.SurveyResponseDAO;
import uk.co.bluetrail.mobriz.service.SurveyResponseManager;

public class SurveyResponseManagerImpl extends BaseManager implements
		SurveyResponseManager {
	private SurveyResponseDAO dao;

	/**
	 * Set the DAO for communication with the data layer.
	 * 
	 * @param dao
	 */
	public void setSurveyResponseDAO(SurveyResponseDAO dao) {
		this.dao = dao;
	}

	/**
	 * @see uk.co.bluetrail.mobriz.service.SurveyResponseManager#getSurveyResponses(uk.co.bluetrail.mobriz.model.SurveyResponse)
	 */
	public List getSurveyResponses(final SurveyResponse surveyResponse) {
		return dao.getSurveyResponses(surveyResponse);
	}

	/**
	 * @see uk.co.bluetrail.mobriz.service.SurveyResponseManager#getSurveyResponse(String
	 *      id)
	 */
	public SurveyResponse getSurveyResponse(final String id) {
		return dao.getSurveyResponse(new Long(id));
	}

	/**
	 * @see uk.co.bluetrail.mobriz.service.SurveyResponseManager#saveSurveyResponse(SurveyResponse
	 *      surveyResponse)
	 */
	public void saveSurveyResponse(SurveyResponse surveyResponse) {
		dao.saveSurveyResponse(surveyResponse);
	}

	/**
	 * @see uk.co.bluetrail.mobriz.service.SurveyResponseManager#removeSurveyResponse(String
	 *      id)
	 */
	public void removeSurveyResponse(final String id) {
		dao.removeSurveyResponse(new Long(id));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.bluetrail.mobriz.service.SurveyResponseManager#removeSurveyResponses(java.lang.String)
	 */
	public void removeSurveyResponses(String id) {
		dao.removeSurveyResponses(new Long(id));

	}

	public List getSurveyResponses(User currentUser) {
		return dao.getSurveyResponses(currentUser);
	}

	public List getResponsesByExample(SurveyResponse example) {
		return dao.getSurveyResponsesByExample(example);
	}

	public List getUnstampedPhotoResponses(Survey survey) {
		return dao.getSurveyResponsesGreaterThanId(survey, survey
				.getLastPhotoStampResponse_id(),100);
	}

	public List getUnZippedResponses(Survey survey) {
		return dao.getSurveyResponsesGreaterThanId(survey, survey
				.getLastZipResponse_id(),100);

	}

	public boolean processAlerts(SurveyResponse sr, List alerts, List trueAlerts) {

		log.debug("Processing Alerts for Survey Response " + sr.getId());
		try {

			Iterator itr = alerts.iterator();

			MobrizAlert ma = null;
			while (itr.hasNext()) {
				ma = (MobrizAlert) itr.next();
				ma.setSurveyResponse(sr);

				try {
					if (ma.isEnabled()) {

						if (ma.isMatch()) {
							log.debug("Survey Response " + sr.getId()
									+ " matches " + ma.getFullTitle());
							trueAlerts.add(ma);
						} else {
							log.debug("Survey Response " + sr.getId()
									+ " does NOT match " + ma.getFullTitle());
						}
					} else {
						log.debug(ma.getFullTitle() + " not enabled");

					}
				} catch (Exception e) {
					log.error("Exception processing: Survey Response "
							+ sr.getId() + " with " + ma.getFullTitle() + " "
							+ e.toString());
					return false;
				}

			}

			sr.setAlertsProcessed(true);
			this.saveSurveyResponse(sr);

		} catch (Exception e) {
			log.error("General Exception processing: Survey Response "
					+ sr.getId() + e.toString());
			return false;
		}

		log.debug("Completed Processing Alerts for Survey Response "
				+ sr.getId());

		return true;

	}

	public List getUnprocessedResponses(Survey survey, int alertDocLimit) {
		SurveyResponse example = new SurveyResponse();
		example.setSurvey_id(survey.getId());
		example.setAlertsProcessed(false);
		return dao.getSurveyResponsesByExample(example, alertDocLimit);
	}
	
	public List getSurveyResponses(Survey survey, User currentUser) {
		return dao.getSurveyResponses(survey, currentUser) ;
	
		
	}



}
