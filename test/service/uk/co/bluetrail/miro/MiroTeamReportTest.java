package uk.co.bluetrail.miro;

import junit.framework.Assert;
import junit.framework.TestCase;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.serviceDTO.TeamMapDTO;

import java.io.File;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class MiroTeamReportTest extends TestCase {

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
		this.dynamicTensionDefaults.setSettingValue("10.00;0.35;0.34");

		miroLevels = new Setting();
		
		miroLevels.setSettingValue("h;100;37;m;38;17;l;18;0;a;1;-1");
		
		miroLetters = new Setting();
		
		miroLetters.setSettingValue("D;E;A;O");
		
		miroTeam = new MiroTeam();
		miroTeam.setId(new Long(100));
		miroTeam.setMiroTeamName("Testing02");
		
		
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
		
		this.baseDir = new File("/miro-reports");
		
		
	}
	
	public void testGenerateWithCommentary() {
		defaultTeam();
		
		
		StringBuffer sb = new StringBuffer();	
				
		sb.append("<p>My money's in that office, right? If she start giving me some bullshit about it ain't there, and we got to go someplace else and get it, I'm gonna shoot you in the head then and there. Then I'm gonna shoot that bitch in the kneecaps, find out where my goddamn money is. She gonna tell me too. Hey, look at me when I'm talking to you, motherfucker. You listen: we go in there, and that nigga Winston or anybody else is in there, you the first motherfucker to get shot. You understand? </p>");

		sb.append("<p>Well, the way they make shows is, they make one show. That show's called a pilot. Then they show that show to the people who make shows, and on the strength of that one show they decide if they're going to make more shows. Some pilots get picked and become television programs. Some don't, become nothing. She starred in one of the ones that became nothing. </p>");

		sb.append("<p>Now that there is the Tec-9, a crappy spray gun from South Miami. This gun is advertised as the most popular gun in American crime. Do you believe that shit? It actually says that in the little book that comes with it: the most popular gun in American crime. Like they're actually proud of that shit.  </p>");

		sb.append("<p>The path of the righteous man is beset on all sides by the iniquities of the selfish and the tyranny of evil men. Blessed is he who, in the name of charity and good will, shepherds the weak through the valley of darkness, for he is truly his brother's keeper and the finder of lost children. And I will strike down upon thee with great vengeance and furious anger those who would attempt to poison and destroy My brothers. And you will know My name is the Lord when I lay My vengeance upon thee. </p>");

		sb.append("<p>Your bones don't break, mine do. That's clear. Your cells react to bacteria and viruses differently than mine. You don't get sick, I do. That's also clear. But for some reason, you and I react the exact same way to water. We swallow it too fast, we choke. We get some in our lungs, we drown. However unreal it may seem, we are connected, you and I. We're on the same curve, just on opposite ends. </p>");

		this.miroTeam.setCommentary(sb.toString());
		
		
		MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
				
		mtr.setResults(this.teamResults);
		
		try {
			mtr.generateReport(miroTeam);
		} catch (Exception e) {
			Assert.fail("generate threw exception " + e.getMessage());
		} 
		
		
	}


    public void testGenerateWithBigCommentary() {
        defaultTeam();

        this.miroTeam.setMiroTeamName("big commentary");

        StringBuffer sb = new StringBuffer();

        sb.append("<p>My money's in that office, right? If she start giving me some bullshit about it ain't there, and we got to go someplace else and get it, I'm gonna shoot you in the head then and there. Then I'm gonna shoot that bitch in the kneecaps, find out where my goddamn money is. She gonna tell me too. Hey, look at me when I'm talking to you, motherfucker. You listen: we go in there, and that nigga Winston or anybody else is in there, you the first motherfucker to get shot. You understand? </p>");

        sb.append("<p>Well, the way they make shows is, they make one show. That show's called a pilot. Then they show that show to the people who make shows, and on the strength of that one show they decide if they're going to make more shows. Some pilots get picked and become television programs. Some don't, become nothing. She starred in one of the ones that became nothing. </p>");

        sb.append("<p>Now that there is the Tec-9, a crappy spray gun from South Miami. This gun is advertised as the most popular gun in American crime. Do you believe that shit? It actually says that in the little book that comes with it: the most popular gun in American crime. Like they're actually proud of that shit.  </p>");

        sb.append("<p>The path of the righteous man is beset on all sides by the iniquities of the selfish and the tyranny of evil men. Blessed is he who, in the name of charity and good will, shepherds the weak through the valley of darkness, for he is truly his brother's keeper and the finder of lost children. And I will strike down upon thee with great vengeance and furious anger those who would attempt to poison and destroy My brothers. And you will know My name is the Lord when I lay My vengeance upon thee. </p>");

        sb.append("<p>Your bones don't break, mine do. That's clear. Your cells react to bacteria and viruses differently than mine. You don't get sick, I do. That's also clear. But for some reason, you and I react the exact same way to water. We swallow it too fast, we choke. We get some in our lungs, we drown. However unreal it may seem, we are connected, you and I. We're on the same curve, just on opposite ends. </p>");

        sb.append("<p>My money's in that office, right? If she start giving me some bullshit about it ain't there, and we got to go someplace else and get it, I'm gonna shoot you in the head then and there. Then I'm gonna shoot that bitch in the kneecaps, find out where my goddamn money is. She gonna tell me too. Hey, look at me when I'm talking to you, motherfucker. You listen: we go in there, and that nigga Winston or anybody else is in there, you the first motherfucker to get shot. You understand? </p>");

        sb.append("<p>My money's in that office, right? If she start giving me some bullshit about it ain't there, and we got to go someplace else and get it, I'm gonna shoot you in the head then and there. </p>");


        this.miroTeam.setCommentary(sb.toString());


        MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);

        mtr.setResults(this.teamResults);

        try {
            mtr.generateReport(miroTeam);
        } catch (Exception e) {
            Assert.fail("generate threw exception " + e.getMessage());
        }


    }
	
	protected void defaultTeam() {


		this.members = new HashSet(); ;   //a piule of mrs
		this.teamResults = new ArrayList(); // a pile of uerDTOS
		addTeamMember("Roger","Test","O","A",1);
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
			
			Assert.assertEquals("Number of pages is 12", 12,plist.size());
			
			
			
			//get the first page
			MiroPage pe = plist.get(0);
			
			
			Assert.assertEquals("Page 1 has one element", 1,pe.getLength());
			
			pe = plist.get(1);
			
			Assert.assertEquals("Page 2 has one element", 1,pe.getLength());
			
			pe = plist.get(2);
			
			Assert.assertEquals("Page 6 has one elements", 1,pe.getLength());
			
			pe = plist.get(3);
			
			Assert.assertEquals("Page 4 has two elements", 7,pe.getLength());
			
			
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
            e.printStackTrace();
		}
	}
	
	public void testGetLevel() {
        defaultTeam();

		MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);

		mtr.setResults(this.teamResults);
		
		mtr.setTeam(miroTeam);
		
//		try {
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
			
			
//		} catch (Exception e) {
//			Assert.fail("generate threw exception " + e.getMessage());
//		}
	}
	
	public void testDeBalance() {
		
		this.dynamicTensionDefaults  = new Setting();
		this.dynamicTensionDefaults.setSettingValue("10.00;0.35;0.34");
		
		try {
		
		this.members = new HashSet(); ;   //a piule of mrs
		this.teamResults = new ArrayList(); // a pile of uerDTOS
		this.miroTeam.setMiroTeamName("De Ballance");
		addTeamMember("Dave","Edmonds","D","E",1000);			
		addTeamMember("Doris ","Engles","D","E",1010);			
		addTeamMember("Eddy","Davis","E","D",1020);			
		addTeamMember("Enid","Dobson","E","D",1030);			
		
		MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
		
		mtr.setResults(this.teamResults);
		
		mtr.generateReport(miroTeam);
		

	} catch (Exception e) {
		Assert.fail("generate threw exception " + e.getMessage());
	} 
		
		
	}
	
	
	public void testAnotherTeam() {
		
		this.dynamicTensionDefaults  = new Setting();
		this.dynamicTensionDefaults.setSettingValue("10.00;0.35;0.34");
		
		
		try {
		
		this.members = new HashSet(); ;   //a piule of mrs
		this.teamResults = new ArrayList(); // a pile of uerDTOS
		this.miroTeam.setMiroTeamName("Another Team");
		
		addTeamMember("dave","eccles","d","e",1200);
		addTeamMember("deadre","eccles","d","e",1210);
		addTeamMember("don","eccles","d","e",1220);
		addTeamMember("dan","eccles","d","e",1230);
		addTeamMember("dolly","eccles","d","e",1240);
		addTeamMember("danny ","eccles","d","e",1250);
		addTeamMember("dupre","eccles","d","e",1260);
		addTeamMember("drue","eccles","d","e",1270);
		addTeamMember("dom","eccles","d","e",1280);
		addTeamMember("del","eccles","d","e",1290);
		addTeamMember("andy","eccles","a","e",1300);
		
		MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
		
		mtr.setResults(this.teamResults);
		
		
		mtr.generateReport(miroTeam);
		

		} catch (Exception e) {
			Assert.fail("generate threw exception " + e.getMessage());
		} 
	}
	
	
	public void testYetAnotherRobTeams() {
		
		this.dynamicTensionDefaults  = new Setting();
		this.dynamicTensionDefaults.setSettingValue("10.00;0.35;0.34");
		
		
		try {
		
			this.members = new HashSet(); ;   //a piule of mrs
			this.teamResults = new ArrayList(); // a pile of uerDTOS
			this.miroTeam.setMiroTeamName("Yet Another Team");
			addTeamMember("dave","Oddie","d","o",1400);
			addTeamMember("deadre","Oddie","d","o",1410);
			addTeamMember("don","Oddie","d","o",1420);
			addTeamMember("dan","Oddie","d","o",1430);
			addTeamMember("dolly","Oddie","d","o",1440);
			addTeamMember("danny ","Oddie","d","o",1450);
			addTeamMember("dupre","Oddie","d","o",1460);
			addTeamMember("drue","Oddie","d","o",1470);
			addTeamMember("dom","Oddie","d","o",1480);
			addTeamMember("del","Oddie","d","o",1490);
			addTeamMember("andy","eccles","a","e",1500);			
			
			MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
			
			mtr.setResults(this.teamResults);
			
			
			mtr.generateReport(miroTeam);
			
			
			//////
			this.members = new HashSet(); ;   //a piule of mrs
			this.teamResults = new ArrayList(); // a pile of uerDTOS
			this.miroTeam.setMiroTeamName("and another");
		
			addTeamMember("dave","Oddie","d","o",1510);
			addTeamMember("deadre","Oddie","d","o",1520);		
			
			mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
			mtr.setResults(this.teamResults);
			mtr.generateReport(miroTeam);
		
		

		} catch (Exception e) {
			Assert.fail("generate threw exception " + e.getMessage());
		} 
		
		///
	}
	
public void testLastTwoRobTeams() {
		
		this.dynamicTensionDefaults  = new Setting();
		this.dynamicTensionDefaults.setSettingValue("10.00;0.35;0.34");
		
		
		try {
		
			this.members = new HashSet(); ;   //a piule of mrs
			this.teamResults = new ArrayList(); // a pile of uerDTOS
			this.miroTeam.setMiroTeamName("One More Team");
			addTeamMember("dave","Oddie","d","o",1600);
			addTeamMember("deadre","Oddie","d","o",1610);
			addTeamMember("don","Oddie","d","o",1620);
			addTeamMember("dan","Oddie","d","o",1630);
			addTeamMember("dolly","Oddie","d","o",1640);
			addTeamMember("danny ","Oddie","d","o",1650);
			addTeamMember("dupre","Oddie","d","o",1660);
			addTeamMember("drue","Oddie","d","o",1670);
			addTeamMember("dom","Oddie","d","o",1680);
			addTeamMember("del","Oddie","d","o",1690);
			addTeamMember("andy","eccles","a","e",1700);	
			
			MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
			
			mtr.setResults(this.teamResults);
			
			
			mtr.generateReport(miroTeam);
			
			
			//////
			this.members = new HashSet(); ;   //a piule of mrs
			this.teamResults = new ArrayList(); // a pile of uerDTOS
			this.miroTeam.setMiroTeamName("well two more");
		
			addTeamMember("dave","Oddie","d","o",1710);
			addTeamMember("deadre","Oddie","d","o",1720);
			
			mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
			mtr.setResults(this.teamResults);
			mtr.generateReport(miroTeam);
		
		

		} catch (Exception e) {
			Assert.fail("generate threw exception " + e.getMessage());
		} 
		
		///
	}
	
	
	
	public void testRobTeams() {
		
		this.dynamicTensionDefaults  = new Setting();
		this.dynamicTensionDefaults.setSettingValue("10.00;0.35;0.34");
		
		
		try {
		
		this.members = new HashSet(); ;   //a piule of mrs
		this.teamResults = new ArrayList(); // a pile of uerDTOS
		this.miroTeam.setMiroTeamName("De Ballance");
		addTeamMember("Dave","Edmonds","D","E",1000);			
		addTeamMember("Doris ","Engles","D","E",1010);			
		addTeamMember("Eddy","Davis","E","D",1020);			
		addTeamMember("Enid","Dobson","E","D",1030);			
		
		MiroTeamReport mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
		
		mtr.setResults(this.teamResults);
		
		
		mtr.generateReport(miroTeam);
		
		
		//////
		this.members = new HashSet(); ;   //a piule of mrs
		this.teamResults = new ArrayList(); // a pile of uerDTOS
		this.miroTeam.setMiroTeamName("All square");
	
		addTeamMember("Dave","Edmonds","D","E",1040);			
		addTeamMember("Angela","O'leary","A","O",1050);			
		addTeamMember("Oscar","Delaney","O","D",1060);			
		addTeamMember("Ernest","Atlee","E","A",1070);			
		
		mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
		mtr.setResults(this.teamResults);
		mtr.generateReport(miroTeam);
		
		///
		
		this.members = new HashSet(); ;   //a piule of mrs
		this.teamResults = new ArrayList(); // a pile of uerDTOS
		this.miroTeam.setMiroTeamName("A little off");
		addTeamMember("Dave","Edmonds","D","E",1080);
		addTeamMember("Danny","Engles","D","E",1090);
		addTeamMember("Deadre","Eastman","D","E",1100);
		addTeamMember("Oscar","Atlee","O","A",1110);
		
		mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
		mtr.setResults(this.teamResults);
		mtr.generateReport(miroTeam);
	
		///
		this.members = new HashSet(); ;   //a piule of mrs
		this.teamResults = new ArrayList(); // a pile of uerDTOS
		this.miroTeam.setMiroTeamName("Eccentric");
		addTeamMember("Dave","Edmonds","D","E",1120);
		addTeamMember("Danny","Engles","D","E",1130);
		addTeamMember("Dilbert","Eccles","D","E",1140);
		addTeamMember("Deadre","Eastman","D","E",1150);
		addTeamMember("Oscar","Atlee","O","A",1160);
		addTeamMember("Otto","Atlee","O","A",1170);
		mtr = new MiroTeamReport(baseDir,miroLetters,miroLevels,dynamicTensionDefaults);
		mtr.setResults(this.teamResults);
		mtr.generateReport(miroTeam);
	
		
	
		
		} catch (Exception e) {
			Assert.fail("generate threw exception " + e.getMessage());
		} 
		
		
		
	}
	
	
}
