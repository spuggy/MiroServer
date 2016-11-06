package uk.co.bluetrail.mobriz.service.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.zip.ZipException;

import javax.imageio.IIOException;

import org.apache.commons.beanutils.PropertyUtils;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Option;
import uk.co.bluetrail.mobriz.model.Question;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyElement;
import uk.co.bluetrail.mobriz.model.SurveyException;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.ConstraintDAO;
import uk.co.bluetrail.mobriz.dao.OptionDAO;
import uk.co.bluetrail.mobriz.dao.QuestionDAO;
import uk.co.bluetrail.mobriz.dao.SurveyDAO;
import uk.co.bluetrail.mobriz.dao.UserDao;
import uk.co.bluetrail.mobriz.service.SettingManager;
import uk.co.bluetrail.mobriz.service.SurveyManager;
import uk.co.bluetrail.mobriz.service.SurveyResponseManager;
import uk.co.bluetrail.mobriz.service.UserManager;
import uk.co.bluetrail.mobriz.util.BluetrailZipFile;
import uk.co.bluetrail.mobriz.util.ImageWriter;
import uk.co.bluetrail.mobriz.util.SurveyElementUtil;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.time.FastDateFormat;

public class SurveyManagerImpl extends BaseManager implements SurveyManager {
	private static final String SEP = "\t";

	public static final String TAB = "\t";

	private SettingManager settingManager;

	private SurveyDAO dao;

	private QuestionDAO questionDAO;

	private OptionDAO optionDAO;

	private ConstraintDAO constraintDAO;

	private UserManager userManager;

	private SurveyResponseManager surveyResponseManager;

	public void setSurveyResponseManager(
			SurveyResponseManager surveyResponseManager) {
		this.surveyResponseManager = surveyResponseManager;
	}

	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.bluetrail.mobriz.service.SurveyManager#saveSurvey(java.util.Vector,
	 *      uk.co.bluetrail.mobriz.model.User)
	 */
	public void saveSurvey(Survey survey, Vector fileLines, User user) {

		int lineCount = 1;
		String surveyLine = null;

		try {

			Iterator itr = fileLines.iterator();

			// ignore the header line
			surveyLine = (String) itr.next(); // ignore

			lineCount++;

			surveyLine = (String) itr.next();

			lineCount++;
			survey.init(surveyLine.split(SEP));
			saveSurvey(survey, user);

			surveyLine = (String) itr.next();

			Question question = null;
			Question previousQuestion = null;
			Option option = null;

			while (itr.hasNext()) {

				lineCount++;

				switch (surveyLine.charAt(0)) {

				case Question.QUESTION_TYPE:
					question = new Question();
					question.init(surveyLine.split(SEP));

					SurveyElementUtil.timeStamp((SurveyElement) question, user);
					question.setSurvey_id(survey.getId());
					this.questionDAO.saveQuestion(question);

					System.out.println("Question! 1");

					// point the first question if it is null;
					if (survey.getFirstQuestion_id() == null) {
						survey.setFirstQuestion_id(question.getId());
						saveSurvey(survey, user);
					}

					System.out.println("Question! 2");

					if (previousQuestion != null) {
						System.out.println("Question! 3");
						previousQuestion.setJQuestion_id(question.getId());
						SurveyElementUtil.timeStamp(
								(SurveyElement) previousQuestion, user);
						this.questionDAO.saveQuestion(previousQuestion);
					}

					System.out.println("Question! 4");

					previousQuestion = question;

					break;
				case Option.OPTION_TYPE:

					option = new Option();
					option.init(surveyLine.split(SEP));

					option.setQuestion_id(previousQuestion.getId());
					SurveyElementUtil.timeStamp((SurveyElement) option, user);
					this.optionDAO.saveOption(option);

					break;
				default:
					throw new SurveyException("Unknown type"
							+ surveyLine.charAt(0));

				}

				surveyLine = (String) itr.next();
			}

		} catch (Exception e) {

			throw new SurveyException("Error processing line " + lineCount
					+ ". Line text = " + surveyLine);

		}

	}

	public List getVisibleSurveys(User user) {

		List allSurveys = dao.getSurveys(null);
		return surveysForUser(allSurveys, user);

	}

	private List surveysForUser(List allSurveys, User user) {
		// filters out anthing the user shoudl not see.
		ArrayList visibleSurveys = new ArrayList();

		Iterator itr = allSurveys.iterator();
		Survey survey;
		Object obj;

		while (itr.hasNext()) {
			obj = itr.next();
			survey = (Survey) obj;

			if (survey.visibleToUser(user)) {
				visibleSurveys.add(survey);
			}
		}

		return (List) visibleSurveys;

	}

	/**
	 * Set the DAO for communication with the data layer.
	 * 
	 * @param dao
	 */
	public void setSurveyDAO(SurveyDAO dao) {
		this.dao = dao;
	}

	/**
	 * @see uk.co.bluetrail.mobriz.service.SurveyManager#getSurveys(uk.co.bluetrail.mobriz.model.Survey)
	 */
	public List getSurveys(final Survey survey) {
		return dao.getSurveys(survey);
	}

	/**
	 * @see uk.co.bluetrail.mobriz.service.SurveyManager#getSurvey(String id)
	 */
	public Survey getSurvey(final String id) {
		return (Survey) dao.getSurvey(new Long(id));
	}

	public Survey getSurveyAndFiles(final String id, String path) {
		Survey survey = (Survey) dao.getSurvey(new Long(id));

		if (survey == null) {
			return survey;
		}

		File directory = getSurveyPhotoDirectory(survey, path);

		File[] files = directory.listFiles(new PhotoFileFilter());

		survey.setFiles(files);

		return survey;

	}

	private File getSurveyPhotoDirectory(Survey survey, String path) {

		return new File(path + "/" + survey.getId());

	}

	/**
	 * @see uk.co.bluetrail.mobriz.service.SurveyManager#saveSurvey(Survey
	 *      survey)
	 */
	public void saveSurvey(Survey survey, User user) {
		SurveyElementUtil.timeStamp((SurveyElement) survey, user);

		if (survey.getAccount_id() == null) {
			survey.setAccount_id(user.getAccount_id());

		}

		dao.saveSurvey(survey, true);
	}

	/**
	 * @see uk.co.bluetrail.mobriz.service.SurveyManager#saveSurvey(Survey
	 *      survey)
	 */
	public void saveSurvey(Survey survey, String[] users, User user) {
		SurveyElementUtil.timeStamp((SurveyElement) survey, user);

		if (survey.getAccount_id() == null) {
			survey.setAccount_id(user.getAccount_id());

		}

		for (int i = 0; users != null && i < users.length; i++) {

			String userId = new String(users[i]);

			survey.addUser(userManager.getUser(userId));

		}

		dao.saveSurvey(survey, true);
	}

	/**
	 * @see uk.co.bluetrail.mobriz.service.SurveyManager#removeSurvey(String id)
	 */
	public void removeSurvey(final String id) {
		dao.removeSurvey(new Long(id));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.bluetrail.mobriz.service.SurveyManager#publishSurvey(java.lang.String)
	 */
	public Long publishSurvey(Survey survey, User user) {

		saveSurvey(survey, user);

		return survey.getId();
	}

	private Survey copySurvey(Survey curSurvey) {

		Survey newSurvey = new Survey();

		try {
			PropertyUtils.copyProperties(newSurvey, curSurvey);
		} catch (Exception e) {
			log.error("[Could not copy survey]" + e.toString());
		}

		// null out the version and id to make this a *new* survey
		newSurvey.setId(null);
		newSurvey.setVersion(null);

		return newSurvey;

	}

	/**
	 * Set the DAO for communication with the data layer.
	 * 
	 * @param dao
	 */
	public void setQuestionDAO(QuestionDAO dao) {
		this.questionDAO = dao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.bluetrail.mobriz.service.SurveyManager#getSurveysByDate(uk.co.bluetrail.mobriz.model.User)
	 */
	public List getSurveysByDate(User user) {
		return dao.getSurveysByDate(user);

	}

	public OptionDAO getOptionDAO() {
		return this.optionDAO;
	}

	public void setOptionDAO(OptionDAO optionDAO) {
		this.optionDAO = optionDAO;
	}

	public List getSurveyNamedQuery(String queryName) {
		return dao.getSurveyNamedQuery(queryName);
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/*
	 * internal class to act as filefilter ;
	 */
	class PhotoFileFilter implements FileFilter {
		public boolean accept(File f) {
			return true;
		}

	}

	public void saveSurvey(Survey survey) {
		dao.saveSurvey(survey, false);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.bluetrail.mobriz.service.SurveyManager#processPhotos(uk.co.bluetrail.mobriz.model.Survey)
	 */


	private boolean hasPhotoTypeQuestions(HashMap questionMap) {

		Iterator itr = questionMap.keySet().iterator();
		Object key = null;
		Question question = null;
		while (itr.hasNext()) {
			key = itr.next();
			question = (Question) questionMap.get(key);
			if (Question.QTYPE_PICTURE.equals(question.getQTypeStr())) {
				return true;
			}

		}

		return false;
	}



	public List getLiveSurveys() {

		return dao.getLiveSurveys();

	}

}