package uk.co.bluetrail.mobriz.service.impl;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import uk.co.bluetrail.miro.MiroException;
import uk.co.bluetrail.miro.MiroReportLevel;
import uk.co.bluetrail.miro.MiroTeamReport;
import uk.co.bluetrail.mobriz.dao.MiroTeamDao;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.Manager;
import uk.co.bluetrail.mobriz.service.MiroResponseManager;
import uk.co.bluetrail.mobriz.service.MiroTeamManager;
import uk.co.bluetrail.mobriz.service.MiroTeamReportManager;
import uk.co.bluetrail.mobriz.service.SettingManager;
import uk.co.bluetrail.mobriz.service.SurveyManager;
import uk.co.bluetrail.mobriz.service.UserManager;


public class MiroTeamReportManagerImpl extends BaseManager implements MiroTeamReportManager {
 
	private MiroTeamDao miroTeamDao = null;
	private MiroResponseManager miroResponseManager = null;
	private Setting miroLevels = null;
	private Survey survey = null;
	protected SettingManager settingManager ;
	private UserManager userManager;
	private SurveyManager surveyManager;
	private MiroTeamManager miroTeamManager =null;
	
	
	

	/**
	 * @hibernate.property 
	 * @return the miroTeamMangaer
	 */
	public MiroTeamManager getMiroTeamMangaer() {
		return miroTeamManager;
	}

	/**
	 * @param miroTeamMangaer the miroTeamMangaer to set
	 */
	//public void setMiroTeamMangaer(MiroTeamManager miroTeamMangaer) {
//		this.miroTeamMangaer = miroTeamMangaer;
	//}
	
	public void setMiroTeamManager(MiroTeamManager miroTeamManager) {
        this.miroTeamManager = miroTeamManager;  
    }

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.service.impl.MiroTeamReportManager#setSettingManager(uk.co.bluetrail.mobriz.service.SettingManager)
	 */
	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.service.impl.MiroTeamReportManager#setUserManager(uk.co.bluetrail.mobriz.service.UserManager)
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.service.impl.MiroTeamReportManager#setSurveyManager(uk.co.bluetrail.mobriz.service.SurveyManager)
	 */
	public void setSurveyManager(SurveyManager surveyManager) {
		this.surveyManager = surveyManager;
	}

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.service.impl.MiroTeamReportManager#setSurvey(uk.co.bluetrail.mobriz.model.Survey)
	 */
	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
	
    protected void setup() {
		
	    
		miroLevels = settingManager.getSettingByName("MIRO_LEVELS");
			
		if(miroLevels ==null) {
			log.error("MIRO_LEVELS not Found!!");
			throw new RuntimeException("MIRO_LEVELS not Found!!");
			
		}
	       
    }
	
	
	
	
	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.service.impl.MiroTeamReportManager#setMiroLevels(uk.co.bluetrail.mobriz.model.Setting)
	 */
	public void setMiroLevels(Setting miroLevels) {
		this.miroLevels = miroLevels;
	}

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.service.impl.MiroTeamReportManager#setMiroResponseManager(uk.co.bluetrail.mobriz.service.MiroResponseManager)
	 */
	public void setMiroResponseManager(MiroResponseManager miroResponseManager) {
		this.miroResponseManager = miroResponseManager;
	}

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.service.impl.MiroTeamReportManager#getMiroTeamDao()
	 */
	public MiroTeamDao getMiroTeamDao() {
		return miroTeamDao;
	}

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.service.impl.MiroTeamReportManager#setMiroTeamDao(uk.co.bluetrail.mobriz.dao.MiroTeamDao)
	 */
	public void setMiroTeamDao(MiroTeamDao miroTeamDao) {
		this.miroTeamDao = miroTeamDao;
	}

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.service.impl.MiroTeamReportManager#createPDF(uk.co.bluetrail.mobriz.model.MiroTeam, java.lang.String)
	 */
	public boolean createPDF(MiroTeam mt, String filePath) {
		
		if(this.miroLevels==null) {
			this.setup();
		}
		
		User prac = userManager.getUser(mt.getCreatedBy_id().toString());
		
		if(prac == null) {
			throw new MiroException("Prac " + mt.getCreatedBy_id()+ " not found" );
		}
		
		mt.setPractitioner(prac);
		
		File baseDir = new File(filePath);
		
		MiroTeamReport mtr = new MiroTeamReport(baseDir,miroResponseManager.getMiroLetters(),this.miroLevels);
		
		try {
			List teamMapData = this.miroResponseManager.getTeamMap(filePath, mt.getMembers());
		
			mtr.generateReport(mt,teamMapData);
			mt.setTeamReportStatus(40);
			mt.updateMembers();
			this.miroTeamManager.saveMiroTeam(mt,mt.getPractitioner());
			
			return true;
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
		
		
		
		
	}

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.service.impl.MiroTeamReportManager#getUnprocessedTeams(int)
	 */
	public List getUnprocessedTeams(int miroDocLimit) {
		
		MiroTeam example = new MiroTeam();
		example.setTeamReportStatus(MiroTeam.REPORT_REQUESTED);
		return miroTeamDao.getMiroTeamsByExample(example, miroDocLimit);

		
	}
	
}
