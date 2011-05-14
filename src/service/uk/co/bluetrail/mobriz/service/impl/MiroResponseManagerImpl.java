package uk.co.bluetrail.mobriz.service.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import uk.co.bluetrail.miro.MiroReport;
import uk.co.bluetrail.miro.MiroResponse;
import uk.co.bluetrail.miro.MiroTeamPieChart;
import uk.co.bluetrail.mobriz.dao.Dao;
import uk.co.bluetrail.mobriz.dao.SurveyResponseDAO;
import uk.co.bluetrail.mobriz.model.MiroProject;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.MiroProjectManager;
import uk.co.bluetrail.mobriz.service.MiroResponseManager;
import uk.co.bluetrail.mobriz.service.SettingManager;
import uk.co.bluetrail.mobriz.service.SurveyManager;
import uk.co.bluetrail.mobriz.service.UserManager;
import uk.co.bluetrail.mobriz.serviceDTO.TeamMapDTO;

public class MiroResponseManagerImpl extends BaseManager implements MiroResponseManager {

	private Survey survey;
	private Setting miroLetters;
	private SettingManager settingManager ;
	private UserManager userManager;
	private SurveyManager surveyManager;
	private int testOffset ;
	private SurveyResponseDAO surveyResponseDAO;
	private MiroProjectManager miroProjectManager;
	private String pdfLocation_dev ;
	private String pdfLocation_live ;
	private String pdfLocation_test ;
	
	private double miroGraphAdjustment ;
	
    private int excessScore ;
    private int engagedScore ;
    private int latentScore;
	private int teamPieWidth;
	private int teamPieHeight;
	private int thumbNailPieHeight;
	private int thumbNailPieWidth;
	
	
	/**
	 * @param teamPieHeight the teamPieHeight to set
	 */
	public void setTeamPieHeight(int teamPieHeight) {
		this.teamPieHeight = teamPieHeight;
	}

	/**
	 * @param teamPieWidth the teamPieWidth to set
	 */
	public void setTeamPieWidth(int teamPieWidth) {
		this.teamPieWidth = teamPieWidth;
	}

	/**
	 * @hibernate.property 
	 * @return the pdfLocation_test
	 */
	public String getPdfLocation_test() {
		return pdfLocation_test;
	}

	/**
	 * @param pdfLocation_test the pdfLocation_test to set
	 */
	public void setPdfLocation_test(String pdfLocation_test) {
		this.pdfLocation_test = pdfLocation_test;
	}

	/**
	 * @hibernate.property 
	 * @return the pdfLocation_dev
	 */
	public String getPdfLocation_dev() {
		return pdfLocation_dev;
	}

	/**
	 * @param pdfLocation_dev the pdfLocation_dev to set
	 */
	public void setPdfLocation_dev(String pdfLocation_dev) {
		this.pdfLocation_dev = pdfLocation_dev;
	}

	/**
	 * @hibernate.property 
	 * @return the pdfLocation_live
	 */
	public String getPdfLocation_live() {
		return pdfLocation_live;
	}

	/**
	 * @param pdfLocation_live the pdfLocation_live to set
	 */
	public void setPdfLocation_live(String pdfLocation_live) {
		this.pdfLocation_live = pdfLocation_live;
	}

	/**
	 * @param testOffset the testOffset to set
	 */
	public void setTestOffset(int testOffset) {
		this.testOffset = testOffset;
	}

	
	public boolean isValid(SurveyResponse sr) {
		
		setup();
		
		MiroResponse mr = new MiroResponse(survey,sr, this.miroLetters,this.testOffset);
		
		return mr.isValid();
		
	}

	
	private void setup() {
		
		if(survey!=null) {
			return; //setou must be done!!
		}
		
		List surveys = surveyManager.getLiveSurveys();
	        
	    if(surveys.size()==0) {
	    	log.error("Cannot find a survey!!!!!");
	        	throw new RuntimeException("Cannot find a survey!!!!");
	    } 
	        
	        
	    survey = (Survey) surveys.get(0);
	        
	    
	    miroLetters = settingManager.getSettingByName("MIRO_LETTERS");
		
		if(miroLetters ==null) {
			log.error("MIRO_LETTERS not Found!!");
			throw new RuntimeException("MIRO_LETTERS not Found!!");
			
		}
	       
	}

	/**
	 * @param settingManager the settingManager to set
	 */
	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}

	/**
	 * @param surveyManager the surveyManager to set
	 */
	public void setSurveyManager(SurveyManager surveyManager) {
		this.surveyManager = surveyManager;
	}

	/**
	 * @param userManager the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public List getUnprocessedMiroResponses(int miroDocLimit) {
		
		this.setup();
		
		SurveyResponse example = new SurveyResponse();
		example.setSurvey_id(survey.getId());
		example.setAlertsProcessed(false);
		return this.surveyResponseDAO.getSurveyResponsesByExample(example, miroDocLimit);
		
	}

	/**
	 * @param surveyReponseDAO the surveyReponseDAO to set
	 */
	public void setSurveyReponseDAO(SurveyResponseDAO surveyResponseDAO) {
		
		
		this.surveyResponseDAO = surveyResponseDAO;
	}

	 private MiroReport getMiroReport(String baseDirectory){
		 
		 	
		 
			MiroReport miroReport = new MiroReport(new File(baseDirectory),this.engagedScore,this.excessScore,this.latentScore);
					
			
			HashMap modes = new HashMap();
			modes.put("E", "Energising Mode");
			modes.put("D", "Driving Mode");
			modes.put("A", "Analysing Mode");
			modes.put("O", "Organising Mode");
			
			HashMap colors = new HashMap();
			colors.put("E", Color.YELLOW);
			colors.put("D", Color.RED);
			colors.put("A", Color.BLUE);
			colors.put("O", Color.GREEN );  
			
			miroReport.setModes(modes) ;
			miroReport.setColors(colors);
			miroReport.setEngagedText("Engaged");
			miroReport.setDisEngagedText("Disengaged");
			miroReport.setLatentText("Latent");
			miroReport.setExcessText("Excess");
			miroReport.setMiroGraphAdjustment(miroGraphAdjustment);
			miroReport.setLabels2(new String[] { "Leading", "Supporting", "Supplementary","Dormant" });
			

			return miroReport;
		 
		 
	 }
	
	public boolean createPDF(SurveyResponse sr, MiroResponse mr,String baseDirectory) {
		
		MiroReport miroReport = getMiroReport(baseDirectory);
		
		this.setup();
		
		mr.init(survey,sr, this.miroLetters,this.testOffset);
		
				
		try{ 
		
		
		User candidate  = sr.getUser();
		
		MiroProject miroProject = miroProjectManager.getMiroProject(candidate.getProject_id().toString());
		
		User practitioner = userManager.getUser(miroProject.getCreatedBy_id().toString());
			
		mr.setPractitionerEmail(practitioner.getEmail());
		mr.setPractitionerName(practitioner.getFirstName() +" " + practitioner.getLastName());
		mr.setPractitionerTelNo(practitioner.getPhoneNumber());
		mr.setPractitionerAddress(new String[] {practitioner.getAddress1(),practitioner.getAddress2(),practitioner.getCity(),practitioner.getCounty(),practitioner.getPostcode()});
		mr.setFirstName(candidate.getFirstName());
		mr.setLastName(candidate.getLastName());
		mr.setWebaddress(practitioner.getWebaddress());  
		mr.setCompany(practitioner.getCompany());
		
		
		candidate.setResponse_id(sr.getId());
		mr.setMiroReportName(candidate.getReportFileName());
		mr.setMiroProject(miroProject);  
			
		miroReport.generateReport(mr);
		
	
		sr.setAlertsProcessed(true);
		candidate.setStatus(User.PURCHASE_REPORT);
		userManager.saveUser(candidate);
		this.surveyResponseDAO.saveSurveyResponse(sr);
		
		} catch(Exception e) {
			log.error("Error creating pdf for response " + sr.getId() + " = " + e.toString());
			return false ;
		}
	
		return true;

		
	}

	/**
	 * @param miroProjectManager the miroProjectManager to set
	 */
	public void setMiroProjectManager(MiroProjectManager miroProjectManager) {
		this.miroProjectManager = miroProjectManager;
	}

	/**
	 * @param surveyResponseDAO the surveyResponseDAO to set
	 */
	public void setSurveyResponseDAO(SurveyResponseDAO surveyResponseDAO) {
		this.surveyResponseDAO = surveyResponseDAO;
	}

	public String getMiroReportPath(String appURL) {
		
		if(appURL.toLowerCase().indexOf("localhost") > 0 ) {
			return this.getPdfLocation_dev();
			
		} 
		
		if(appURL.toLowerCase().indexOf("dev.bluetrail.co.uk") > 0 ) {
			return this.getPdfLocation_test();
			
		} 
		
		
		return this.getPdfLocation_live();
			
		}

	/**
	 * @param engagedScore the engagedScore to set
	 */
	public void setEngagedScore(int engagedScore) {
		this.engagedScore = engagedScore;
	}

	/**
	 * @param excessScore the excessScore to set
	 */
	public void setExcessScore(int excessScore) {
		this.excessScore = excessScore;
	}

	/**
	 * @param latentScore the latentScore to set
	 */
	public void setLatentScore(int latentScore) {
		this.latentScore = latentScore;
	}

	/**
	 * @param miroGraphAdjustment the miroGraphAdjustment to set
	 */
	public void setMiroGraphAdjustment(double miroGraphAdjustment) {
		this.miroGraphAdjustment = miroGraphAdjustment;
	}
	
	

	public BufferedImage getMiroTeamPie(String baseDirectory, String[] userIds) throws Exception {
		
		
		MiroReport miroReport = getMiroReport(baseDirectory);

		this.setup();
		
		List users = userManager.getUsers(userIds);

		Iterator itr = users.iterator();
		
		User user = null;
		SurveyResponse sr = null;
		MiroResponse mr = null;
	
		MiroTeamPieChart miroTeamPieChart = new MiroTeamPieChart(teamPieWidth,teamPieHeight,this.thumbNailPieWidth,this.thumbNailPieHeight);
		
		while(itr.hasNext()) {
			user = (User) itr.next();
			sr = surveyResponseDAO.getSurveyResponse(user.getResponse_id());
			mr = new MiroResponse();
			mr.init(survey,sr, this.miroLetters,this.testOffset);
			try {
				BufferedImage bi = miroReport.getThumbnailPie(user.getFullName(),mr,this.thumbNailPieWidth,this.thumbNailPieHeight);
				miroTeamPieChart.add(bi);
			} catch(Exception e) {
				log.error("Error trying to create thumbnail for userid = " + user.getId());
			}
		}
		
		return miroTeamPieChart.getBufferedImage();
		
		
	}

	/**
	 * @param thumbNailPieHeight the thumbNailPieHeight to set
	 */
	public void setThumbNailPieHeight(int thumbNailPieHeight) {
		this.thumbNailPieHeight = thumbNailPieHeight;
	}

	/**
	 * @param thumbNailPieWidth the thumbNailPieWidth to set
	 */
	public void setThumbNailPieWidth(int thumbNailPieWidth) {
		this.thumbNailPieWidth = thumbNailPieWidth;
	}

	public List getTeamMap(String baseDirectory,List users) {
		
		MiroReport miroReport = getMiroReport(baseDirectory);

		this.setup();
		
		
		Iterator itr = users.iterator();
		
		User user = null;
		SurveyResponse sr = null;
		MiroResponse mr = null;
	
		HashMap initials = new HashMap();
		TeamMapDTO teamMapDTO = null;
		ArrayList teamMapData = new ArrayList();
		
		while(itr.hasNext()) {
			user = (User) itr.next();
			sr = surveyResponseDAO.getSurveyResponse(user.getResponse_id());
			mr = new MiroResponse();
			mr.init(survey,sr, this.miroLetters,this.testOffset);
			teamMapDTO = new TeamMapDTO();		
			try {
				teamMapDTO = new TeamMapDTO();
				teamMapDTO.setFullName(user.getFullName());
				teamMapDTO.setInitials(user.getInitials(initials));
				miroReport.setTeamMapData(teamMapDTO, mr);
				teamMapData.add(teamMapDTO);
			} catch(Exception e) {
				log.error("Error trying to create teamMap for userid = " + user.getId());
			}
		}
		
		return teamMapData;
		
	}
	
	
}
