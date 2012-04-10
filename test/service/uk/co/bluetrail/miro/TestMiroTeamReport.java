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

	protected MiroTeam miroTeam;
	protected File baseDir ;
	protected Setting miroLetters;
	protected Setting dynamicTensionDefaults;
	protected Setting miroLevels;
	protected HashSet members;
	protected ArrayList teamResults;
	protected HashMap<String, String> modeNames;
	HashMap initials;
	
	
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		this.dynamicTensionDefaults  = new Setting();
		this.dynamicTensionDefaults.setSettingValue("20.00;0.25;0.24");
				
		
		
		miroLevels = new Setting();
		
		miroLevels.setSettingValue("h;100;40;m;39;20;l;19;1;a;0;0");
		
		miroLetters = new Setting();
		
		miroLetters.setSettingValue("D;E;A;O");
		
		miroTeam = new MiroTeam();
		miroTeam.setId(new Long(100));
		miroTeam.setMiroTeamName("Testing Team all bullets");
		
		
		
		
		modeNames = new HashMap<String, String>();  
		modeNames.put("A", "Analysing Mode");  
		modeNames.put("E", "Energising Mode");
		modeNames.put("D", "Driving Mode");
		modeNames.put("O", "Organising Mode");  
		
		
		
		User prac = new User();
		
		prac.setFirstName("Kenny");
		prac.setLastName("Practitioner");
		prac.setId(new Long(99));
		prac.setAddress1("80 Sandringham Road");
		prac.setCity("Swindon");
		
		miroTeam.setPractitioner(prac);
		
		this.baseDir = new File("/Users/Richard/Documents/workspace-copy/MiroServer3/web/miro");
		
		
	}
	
	private void defaultTeam() {
		
		this.members = new HashSet(); ;   //a piule of mrs
		this.teamResults = new ArrayList(); // a pile of uerDTOS
		addTeamMember("Richard","Spence","O","A",2000);
		addTeamMember("Johnny","Doodah","O","E",3000);
		addTeamMember("Edward","Spence","O","A",4000);
		addTeamMember("Tracy","Harding","O","A",5000);
		addTeamMember("Betty",	"Harding","O","D",	6000);
		addTeamMember("George",	"Spence","E","A",7000);
		addTeamMember("Leigh",	"Chappell",	"A","D",	8000);
		addTeamMember("Scarlett",	"Chappell",	"A","D",9000);
	}
	
	

	private void addTeamMember(String firstName, String secondName, String leading, String secondary, int id) {
		
		if(initials == null) {
			initials = new HashMap();
			
		}
		 
		leading = leading.toUpperCase();
		secondary = secondary.toUpperCase();
		
		
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
		tm1.setInitials(getInitials(firstName,secondName));
		
		teamResults.add(tm1);
		
	}

	private String getInitials(String firstName, String lastName) {
		
		StringBuffer i = new StringBuffer();
		if(firstName!=null) {
			i.append(firstName.charAt(0));
		}
		if(lastName!=null) {
			i.append(lastName.charAt(0));
		}
		
		return i.toString().toUpperCase();
		
		
	}
	

	public void testCreate() {
		defaultTeam();
		MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
		
	}
	
	public void testBuildReportPageList() {
		defaultTeam();
		try {
			
			MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
			
			mtr.setResults(this.teamResults);
			
			List<MiroPage> plist = new ArrayList<MiroPage>();
			Map<String, String> variables = new HashMap<String, String>();
			Map<String, String> imgNames = new HashMap<String, String>();
			
			mtr.buildReportPageList(miroTeam, plist, variables,imgNames);
			
			Assert.assertEquals("Number of pages is 8", 8,plist.size());
			
			
			
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
		defaultTeam();
		
		MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
		
		mtr.setResults(this.teamResults);
		
		try {
			mtr.generateReport(miroTeam);
		} catch (Exception e) {
			Assert.fail("generate threw exception " + e.getMessage());
		} 
	}
	
	public void testGetLevel() {
		MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
		
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
	
	
	public void testRobTeams() {
		
		this.dynamicTensionDefaults  = new Setting();
		this.dynamicTensionDefaults.setSettingValue("20.00;0.30;0.29");
	
		
		try {
		
		this.members = new HashSet(); ;   //a piule of mrs
		this.teamResults = new ArrayList(); // a pile of uerDTOS
		this.miroTeam.setMiroTeamName("Wacky wankers");
		addTeamMember("Dave","Richards","e","a",100);
		addTeamMember("Dee","Jagger","e","a",101);
		addTeamMember("Dozy","Jones","e","a",102);
		addTeamMember("beaky","Wyman","e","a",103);
		addTeamMember("Mick","Watts","a","e",104);
		addTeamMember("Titch","McCartney","a","e",105);
		addTeamMember("Matthew ","Lennon","a","e",106);
		addTeamMember("Mark","Star","o","d",107);
		addTeamMember("Luke","Springsteen","o","d",108);
		addTeamMember("John","Clemons","d","o",109);
		
		MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
		
		mtr.setResults(this.teamResults);
		
		
		mtr.generateReport(miroTeam);
		
		
		//////
		this.members = new HashSet(); ;   //a piule of mrs
		this.teamResults = new ArrayList(); // a pile of uerDTOS
		this.miroTeam.setMiroTeamName("Bunch of cunts");
	
		addTeamMember("Dave","Richards","d","a",100);
		addTeamMember("Dee","Jagger","d","a",101);
		addTeamMember("Dozy","Jones","d","a",102);
		addTeamMember("beaky","Wyman","d","a",103);
		addTeamMember("Mick","Watts","a","d",104);
		addTeamMember("Titch","McCartney","a","d",105);
		addTeamMember("Matthew ","Lennon","a","d",106);
		addTeamMember("Mark","Star","a","d",107);
		addTeamMember("Luke","Springsteen","o","e",108);
		addTeamMember("John","Clemons","o","e",109);
		
		mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
		mtr.setResults(this.teamResults);
		mtr.generateReport(miroTeam);
		
		///
		
		this.members = new HashSet(); ;   //a piule of mrs
		this.teamResults = new ArrayList(); // a pile of uerDTOS
		this.miroTeam.setMiroTeamName("Knob headz");
		addTeamMember("Dave","Richards","o","e",100);
		addTeamMember("Dee","Jagger","o","e",101);
		addTeamMember("Dozy","Jones","o","e",102);
		addTeamMember("beaky","Wyman","o","e",103);
		addTeamMember("Mick","Watts","o","e",104);
		addTeamMember("Titch","McCartney","o","e",105);
		addTeamMember("Matthew ","Lennon","o","e",106);
		addTeamMember("Mark","Star","o","e",107);
		addTeamMember("Luke","Springsteen","o","e",108);
		addTeamMember("John","Clemons","o","e",109);
		
		mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
		mtr.setResults(this.teamResults);
		mtr.generateReport(miroTeam);
	
		///
		this.members = new HashSet(); ;   //a piule of mrs
		this.teamResults = new ArrayList(); // a pile of uerDTOS
		this.miroTeam.setMiroTeamName("Oily tossers");
		addTeamMember("Dave","Richards","e","o",100);
		addTeamMember("Dee","Jagger","e","o",101);
		addTeamMember("Dozy","Jones","e","o",102);
		addTeamMember("beaky","Wyman","e","o",103);
		addTeamMember("Mick","Watts","e","o",104);
		addTeamMember("Titch","McCartney","e","d",105);
		addTeamMember("Matthew ","Lennon","e","d",106);
		addTeamMember("Mark","Star","e","d",107);
		addTeamMember("Luke","Springsteen","e","d",108);
		addTeamMember("John","Clemons","e","d",109);
		mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
		mtr.setResults(this.teamResults);
		mtr.generateReport(miroTeam);
	
		//
		this.members = new HashSet(); ;   //a piule of mrs
		this.teamResults = new ArrayList(); // a pile of uerDTOS
		this.miroTeam.setMiroTeamName("Fuck whits");
		addTeamMember("Dave","Richards","o","a",100);
		addTeamMember("Dee","Jagger","o","a",101);
		addTeamMember("Dozy","Jones","a","o",102);
		addTeamMember("beaky","Wyman","a","o",103);
		addTeamMember("Mick","Watts","d","e",104);
		addTeamMember("Titch","McCartney","d","e",105);
		addTeamMember("Matthew ","Lennon","e","d",106);
		addTeamMember("Mark","Star","e","d",107);
		addTeamMember("Luke","Springsteen","d","e",108);
		addTeamMember("John","Clemons","e","d",109);
		mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
		mtr.setResults(this.teamResults);
		mtr.generateReport(miroTeam);
	
		
		} catch (Exception e) {
			Assert.fail("generate threw exception " + e.getMessage());
		} 
		
		
		
	}
	
	
}
