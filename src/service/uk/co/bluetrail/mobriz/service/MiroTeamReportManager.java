package uk.co.bluetrail.mobriz.service;

import java.util.List;

import uk.co.bluetrail.mobriz.dao.MiroTeamDao;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.User;

public interface MiroTeamReportManager {

	/**
	 * @param settingManager the settingManager to set
	 */
	public void setSettingManager(SettingManager settingManager);

	/**
	 * @param userManager the userManager to set
	 */
	public void setUserManager(UserManager userManager);

	/**
	 * @param surveyManager the surveyManager to set
	 */
	public void setSurveyManager(SurveyManager surveyManager);

	public void setSurvey(Survey survey);

	/**
	 * @param miroLevels the miroLevels to set
	 */
	public void setMiroLevels(Setting miroLevels);

	/**
	 * @param miroResponseManager the miroResponseManager to set
	 */
	public void setMiroResponseManager(MiroResponseManager miroResponseManager);

	/**
	 * @hibernate.property 
	 */
	public MiroTeamDao getMiroTeamDao();

	/**
	 * @param miroTeamDao the miroTeamDao to set
	 */
	public void setMiroTeamDao(MiroTeamDao miroTeamDao);

	public boolean createPDF(MiroTeam mt, String filePath);

	public List getUnprocessedTeams(int miroDocLimit);
	
	/**
	 * @param miroTeamMangaer the miroTeamMangaer to set
	 */
	public void setMiroTeamManager(MiroTeamManager miroTeamManager) ;
	

}