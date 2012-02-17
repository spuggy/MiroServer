package uk.co.bluetrail.miro;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.serviceDTO.TeamMapDTO;

import junit.framework.Assert;
import junit.framework.TestCase;
import static org.mockito.Mockito.*;


public class TestMiroTeamReport extends TestCase {

	MiroTeam miroTeam;
	File baseDir ;
	Setting miroLetters;
	Setting miroLevels;
	private ArrayList members;
	private ArrayList teamResults;
	private HashMap<String, String> modeNames;
	
	
	private MiroResponse getMockMiroResponse(Long id,String firstName, String lastName) {
	
		MiroResponse mr = mock(MiroResponse.class);
		when(mr.getFirstName()).thenReturn(firstName);
		when(mr.getLastName()).thenReturn(lastName);
		when(mr.getTestId()).thenReturn(id);
		when(mr.getFullName()).thenReturn(firstName+ " " + lastName);
		when(mr.getResultLetters()).thenReturn(new String[] {"A","E","O","D"});
		when(mr.is2ndEngaged()).thenReturn(true);
		when(mr.getMiroReportName()).thenReturn(firstName+"_"+lastName+"_"+id);
		
		return mr;
		
	}
	
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
				
		
		miroLevels = new Setting();
		
		miroLevels.setSettingValue("h;100;40;m;39;20;l;19;1;a;0;0");
		
		miroLetters = new Setting();
		
		miroLetters.setSettingValue("D;E;A;O");
		
		miroTeam = new MiroTeam();
		miroTeam.setId(new Long(100));
		miroTeam.setMiroTeamReportName("Testing Team");
		
		this.members = new ArrayList() ;   //a piule of mrs
		this.teamResults = new ArrayList(); // a pile of uerDTOS
		
		modeNames = new HashMap<String, String>();  
		modeNames.put("A", "Analysing Mode");  
		modeNames.put("E", "Energising Mode");
		modeNames.put("D", "Driving Mode");
		modeNames.put("O", "Organising Mode");  
		
		
		addTeamMember("Richard","Spence","O","A",2000);
		addTeamMember("Johnny","Doodah","O","E",3000);
		addTeamMember("Edward","Spence","O","A",4000);
		addTeamMember("Tracy","Harding","O","A",5000);
		addTeamMember("Betty",	"Harding","O","D",	6000);
		addTeamMember("George",	"Spence","E","A",7000);
		addTeamMember("Leigh",	"Chappell",	"A","D",	8000);
		addTeamMember("Scarlett",	"Chappell",	"A","D",9000);
		
		
		
		
		miroTeam.setTeamMembers(members) ;
		miroTeam.setResults(teamResults);
		
		
		
		
		User prac = new User();
		
		prac.setFirstName("Richard");
		prac.setLastName("Spence");
		prac.setId(new Long(99));
		
		miroTeam.setPractitioner(prac);
		
		this.baseDir = new File("/Users/RSpence1/git/MiroServer/web/miro");  
		
		
	}
	
	
	

	private void addTeamMember(String firstName, String secondName, String leading, String secondary, int id) {
		
		MiroResponse mr1 = getMockMiroResponse(new Long(id),firstName,secondName);
		members.add(mr1);
		
		TeamMapDTO tm1 = new TeamMapDTO();
		tm1.setId(mr1.getTestId());
		tm1.setFullName(mr1.getFullName());
		tm1.setLeadingModeText(modeNames.get(leading));
		tm1.setLeadingMode(leading);
		tm1.setSecondaryModeText(modeNames.get(secondary));
		tm1.setSecondaryMode(secondary);
		
		teamResults.add(tm1);
		
	}


	public void testCreate() {
		
		MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels);
		
	}
	
	public void testBuildReportPageList() {
		
		try {
			
			MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels);
			
			List<MiroPage> plist = new ArrayList<MiroPage>();
			Map<String, String> variables = new HashMap<String, String>();
			Map<String, String> imgNames = new HashMap<String, String>();
			
			mtr.buildReportPageList(miroTeam, plist, variables,imgNames);
			
			Assert.assertEquals("Number of pages is 4", 4,plist.size());
			
			
			
			//get the first page
			MiroPage pe = plist.get(0);
			
			
			Assert.assertEquals("Page 1 has one element", 1,pe.getLength());
			
			pe = plist.get(1);
			
			Assert.assertEquals("Page 2 has one element", 1,pe.getLength());
			
			pe = plist.get(2);
			
			Assert.assertEquals("Page 3 has two elements", 8,pe.getLength());
			
			
		} catch(Exception e) {
			fail(e.toString());
		}
		   
		
	}
	
	public void testGenerate() {
		MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels);
		
		try {
			mtr.generateReport(miroTeam);
		} catch (Exception e) {
			Assert.fail("generate threw exception " + e.getMessage());
		} 
	}
	
	public void testGetLevel() {
		MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels);
		
		mtr.setTeam(miroTeam);
		
		try {
			String m0 = mtr.getMode(0);
			String m1 = mtr.getMode(1);
			String m2 = mtr.getMode(2);
			String m3 = mtr.getMode(3);
				
			
			//levels	A	O	E	D
			//H	M	L	L
			Assert.assertEquals("m0 is O ","O",m0);
			Assert.assertEquals("m1 is A ","A",m1);
			Assert.assertEquals("m2 is E ","E",m2);
			Assert.assertEquals("m3 is D ","D",m3);
			
			
		} catch (Exception e) {
			Assert.fail("generate threw exception " + e.getMessage());
		} 
	}
	
	
}
