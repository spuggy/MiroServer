package uk.co.bluetrail.mobriz.service;



import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import antlr.collections.List;

import static org.mockito.Mockito.*;

import uk.co.bluetrail.miro.MiroResponse;
import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.dao.MiroTeamDao;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.impl.MiroTeamReportManagerImpl;
import uk.co.bluetrail.mobriz.service.impl.SurveyManagerImpl;
import uk.co.bluetrail.mobriz.serviceDTO.TeamMapDTO;

import junit.framework.Assert;
import junit.framework.TestCase;

public class MiroTeamReportManagerTest extends TestCase {

	private MiroTeamReportManagerImpl miroTeamReportManager = new MiroTeamReportManagerImpl();
	String baseDir ;
	Setting miroLetters;
	Setting miroLevels;
	private HashSet members;
	private ArrayList teamResults;
	private HashMap<String, String> modeNames;
	MiroTeam miroTeam;
	MiroTeamDao  miroTeamDAO = null;


	protected void setUp() throws Exception {
		super.setUp();
	
		/* setup survey and survey manager */
		Survey survey = mock(Survey.class);
		
		this.miroTeamReportManager.setSurvey(survey);
		
		
		
		miroTeamDAO = mock(MiroTeamDao.class);
		miroTeamReportManager.setMiroTeamDao(miroTeamDAO);
		
		miroLevels = new Setting();
		miroLevels.setSettingValue("h;100;40;m;39;20;l;19;1;a;0;0");
		miroLetters = new Setting();
		miroLetters.setSettingValue("D;E;A;O");
		
		this.miroTeamReportManager.setMiroLevels(miroLevels);
		
		
		
		
		miroTeam = new MiroTeam();
		miroTeam.setId(new Long(100));
		miroTeam.setMiroTeamName("Testing Team 103");

		this.members = new HashSet();
		this.teamResults = new ArrayList(); // a pile of uerDTOS

		modeNames = new HashMap<String, String>();
		modeNames.put("A", "Analysing Mode");
		modeNames.put("E", "Energising Mode");
		modeNames.put("D", "Driving Mode");
		modeNames.put("O", "Organising Mode");

		addTeamMember("Richard", "Spence", "O", "A", 2000);
		addTeamMember("Johnny", "Doodah", "O", "E", 3000);
		addTeamMember("Edward", "Spence", "O", "A", 4000);
		addTeamMember("Tracy", "Harding", "O", "A", 5000);
		addTeamMember("Betty", "Harding", "O", "D", 6000);
		addTeamMember("George", "Spence", "E", "A", 7000);
		addTeamMember("Leigh", "Chappell", "A", "D", 8000);
		addTeamMember("Scarlett", "Chappell", "A", "D", 9000);

		this.miroTeam.setMembers(members);
		
		MiroResponseManager mrm = mock(MiroResponseManager.class);
		when(mrm.getMiroLetters()).thenReturn(miroLetters);
		when(mrm.getTeamMap(anyString(), anyList())).thenReturn(teamResults);
		
		this.miroTeamReportManager.setMiroResponseManager(mrm);
		
		
		User prac = new User();

		prac.setFirstName("Kenny");
		prac.setLastName("Practitioner");
		prac.setId(new Long(99));
		prac.setAddress1("80 Sandringham Road");
		prac.setCity("Swindon");

		miroTeam.setPractitioner(prac);

		
		this.baseDir = "/Users/Richard/Documents/workspace-copy/MiroServer3/web/miro";
		
		File pdf = new File(this.baseDir + "/out/" + miroTeam.getMiroTeamNameFileName(Constants.PDF));
		
		pdf.delete();
		
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		miroTeamReportManager = null;

	

	}

	private void addTeamMember(String firstName, String secondName,
			String leading, String secondary, int id) {

		User u = mock(User.class);
		when(u.getFirstName()).thenReturn(firstName);
		when(u.getLastName()).thenReturn(secondName);
		when(u.getId()).thenReturn(new Long(id));
		when(u.getFullName()).thenReturn(firstName + " " + secondName);
	
		members.add(u);
		
		
		MiroResponse mr1 = mock(MiroResponse.class);
		when(mr1.getFirstName()).thenReturn(firstName);
		when(mr1.getLastName()).thenReturn(secondName);
		when(mr1.getTestId()).thenReturn(new Long(id));
		when(mr1.getFullName()).thenReturn(firstName+ " " + secondName);
		when(mr1.getResultLetters()).thenReturn(new String[] {leading,secondary});
		when(mr1.is2ndEngaged()).thenReturn(true);
		when(mr1.getMiroReportName()).thenReturn(firstName+"_"+secondName+"_"+id);
		

		
		TeamMapDTO tm1 = new TeamMapDTO();
		tm1.setId(mr1.getTestId());
		tm1.setFullName(mr1.getFullName());
		tm1.setLeadingModeText(modeNames.get(leading));
		tm1.setLeadingMode(leading);
		tm1.setSecondaryModeText(modeNames.get(secondary));
		tm1.setSecondaryMode(secondary);
		tm1.setMiroResponse(mr1);
		
		teamResults.add(tm1);


	}

	public void testCreatePDF() {

		
		
		try {
			this.miroTeamReportManager.createPDF(miroTeam, this.baseDir);
		} catch (Exception e) {
			fail("Exception " + e.getMessage());
		}
		
		File pdf = new File(this.baseDir + "/out/" + miroTeam.getMiroTeamNameFileName(Constants.PDF));
		
		assertTrue("PFD Exists" , pdf.exists());

	}

}
