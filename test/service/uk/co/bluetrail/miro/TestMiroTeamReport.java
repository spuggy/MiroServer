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
		miroTeam.setMiroTeamReportName("Test Report2");
		
		MiroResponse mr1 = getMockMiroResponse(new Long(27647),"Richard","Spence");
		MiroResponse mr2 = getMockMiroResponse(new Long(227650),"Johnny","Doodah");

		
		
		ArrayList members = new ArrayList() ;
		
		members.add(mr1);
		members.add(mr2);
		miroTeam.setTeamMembers(members) ;
		
		
		HashMap initials = new HashMap();
		
		TeamMapDTO tm1 = new TeamMapDTO();
		tm1.setId(mr1.getTestId());
		tm1.setFullName(mr1.getFullName());
		tm1.setLeadingModeText("Organising Mode");
		tm1.setLeadingMode("O");
		tm1.setSecondaryModeText("Energising Mode");
		tm1.setSecondaryMode("E");
		
		TeamMapDTO tm2 = new TeamMapDTO();
		tm2.setId(mr2.getTestId());
		tm2.setFullName(mr1.getFullName());
		tm2.setLeadingModeText("Driving Mode");
		tm2.setLeadingMode("D");
		tm2.setSecondaryModeText("Organising Mode");
		tm2.setSecondaryMode("O");
		
		ArrayList teamResults = new ArrayList();
		
		teamResults.add(tm1);
		teamResults.add(tm2);
		
		miroTeam.setResults(teamResults);
		
		User prac = new User();
		
		prac.setFirstName("Richard");
		prac.setLastName("Spence");
		prac.setId(new Long(99));
		
		miroTeam.setPractitioner(prac);
		
		this.baseDir = new File("/Users/RSpence1/git/MiroServer/web/miro");  
		
		
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
			
			
			//get the first page
			MiroPage pe = plist.get(0);
			
			Assert.assertEquals("Page 1 has one element", 1,pe.getLength());
			
			pe = plist.get(1);
			
			Assert.assertEquals("Page 2 has one element", 1,pe.getLength());
			
			pe = plist.get(2);
			
			Assert.assertEquals("Page 3 has two elements", 2,pe.getLength());
			
			
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
	
	
}
