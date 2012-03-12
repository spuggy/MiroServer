package uk.co.bluetrail.miro;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
	private HashSet members;
	private ArrayList teamResults;
	private HashMap<String, String> modeNames;
	
	
	
	
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
		miroTeam.setMiroTeamName("Testing Team 102");
		
		this.members = new HashSet(); ;   //a piule of mrs
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
		
		
		
		
		
		
		
		
		
		User prac = new User();
		
		prac.setFirstName("Kenny");
		prac.setLastName("Practitioner");
		prac.setId(new Long(99));
		prac.setAddress1("80 Sandringham Road");
		prac.setCity("Swindon");
		
		miroTeam.setPractitioner(prac);
		
		this.baseDir = new File("/Users/Richard/Documents/workspace-copy/MiroServer3/web/miro");
		
		
	}
	
	
	

	private void addTeamMember(String firstName, String secondName, String leading, String secondary, int id) {
		
		 
		
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


	public void testCreate() {
		
		MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels);
		
	}
	
	public void testBuildReportPageList() {
		
		try {
			
			MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels);
			
			mtr.setResults(this.teamResults);
			
			List<MiroPage> plist = new ArrayList<MiroPage>();
			Map<String, String> variables = new HashMap<String, String>();
			Map<String, String> imgNames = new HashMap<String, String>();
			
			mtr.buildReportPageList(miroTeam, plist, variables,imgNames);
			
			Assert.assertEquals("Number of pages is 7", 7,plist.size());
			
			
			
			//get the first page
			MiroPage pe = plist.get(0);
			
			
			Assert.assertEquals("Page 1 has one element", 1,pe.getLength());
			
			pe = plist.get(1);
			
			Assert.assertEquals("Page 2 has one element", 1,pe.getLength());
			
			pe = plist.get(2);
			
			Assert.assertEquals("Page 16 has two elements", 16,pe.getLength());
			
			pe = plist.get(3);
			
			Assert.assertEquals("Page 4 has two elements", 6,pe.getLength());
			
			
		} catch(Exception e) {
			fail(e.toString());
		}
		   
		
	}
	
	public void testGenerate() {
		MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels);
		
		mtr.setResults(this.teamResults);
		
		try {
			mtr.generateReport(miroTeam);
		} catch (Exception e) {
			Assert.fail("generate threw exception " + e.getMessage());
		} 
	}
	
	public void testGetLevel() {
		MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels);
		
		mtr.setResults(this.teamResults);
		
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
