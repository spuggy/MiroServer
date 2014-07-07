package uk.co.bluetrail.miro;

import junit.framework.TestCase;
import uk.co.bluetrail.mobriz.model.*;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;


public class MiroReport10Test extends TestCase {


    protected File baseDir ;
    protected Setting miroLetters;
    protected String baseDirPath ;
    protected User prac;
    protected HashMap modeNames;
    protected int testOffset;
    protected int engagedScore ;
    protected int latentScore ;
    protected double miroGraphAdjustment  ;
    protected int excessScore;


	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		

        this.testOffset = 32;
        this.engagedScore = 31;
        this.latentScore =  8 ;
        this.miroGraphAdjustment= 0.75 ;
		this.excessScore = 55;

		miroLetters = new Setting();
		
		miroLetters.setSettingValue("D;E;A;O");


		
		prac = new User();
		
		prac.setFirstName("Kenny");
		prac.setLastName("Practitioner");
		prac.setId(new Long(99));
		prac.setAddress1("80 Sandringham Road");
		prac.setCity("Swindon");
        prac.setPhoneNumber("07961236235");
        prac.setEmail("rspence@pac.com");
		
        this.baseDirPath =  "/miro-reports";

		this.baseDir = new File(this.baseDirPath);
		
		
	}
	
	public void testGenerate() {

        MiroReport miroReport = getMiroReport(this.baseDirPath);

        MiroResponse miroResponse = new MiroResponse();

        SurveyResponse surveyResponse  = new SurveyResponse();
        surveyResponse.setId(new Long(99));
        surveyResponse.setAnswer_trail("Charming#E;Tolerant#O~Self-assured#D;Impulsive#E~Empathic#O;Competitive#D~Charismatic#E;Methodical#A ~Positive#E;Pioneering#D~Amiable#O;Sceptical#D~Good-natured#O;Unwavering#D~Affable#E;Adventurous#D~Playful#E;Demanding#D~Admirable#E;Forceful#D~Companionable#E;Self-sufficient#D~Kind-hearted#O;Orderly#A~Unconventional#E;Conventional#A~Gregarious#E;Level-headed#A~Open #E;No-nonsense #D~Friendly#E;Forthright#D~Big-hearted#O;Well-disciplined#A~Relaxed#O;Exacting#A~Gentle#O;Modest#A~Sophisticated#A;Compassionate#O~Popular#E;Eager#D~Optimistic#E;Risk taking#D~Open-minded#A;Self-confident#D~Respectful  #O;Definite #D~Unpredictable #E;Stable#O~Self-reliant#D;Restrained#A~Attentive#O;Diplomatic#A~Helpful#O;Determined#D~Contented#O;Restless#D~Perfectionist#A ;Impatient#D ");
        surveyResponse.setQuestion_trail("33~34~35~36~37~38~39~40~41~42~43~44~45~46~47~48~49~50~51~52~53~54~55~56~57~58~59~60~61~62");

        User candidate = new User();

        candidate.setFirstName("Roger");
        candidate.setLastName("Test");
        candidate.setId(new Long(1000));

        surveyResponse.setUser(candidate);


        Survey survey = new Survey();
        survey.setId(0L);
        survey.setFirstQuestion_id(33L);
        initQuestions(survey);

        miroResponse.init(survey,surveyResponse, this.miroLetters,this.testOffset);

        MiroProject miroProject = new MiroProject();

        User practitioner = this.prac;

        candidate.setResponse_id(surveyResponse.getId());
        populateMiroResponse(miroResponse,practitioner,candidate) ;


        miroResponse.setMiroProject(miroProject);

        try {
            miroReport.generateReport(miroResponse,MiroReport.V10);
        } catch (Exception e) {
            fail("failed with " + e.getMessage());
        }


		
		
	}

    protected MiroReport getMiroReport(String baseDirectory){


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


    private void initQuestions(Survey survey) {

        Question q = null;
        HashSet m = new HashSet();


        q = new Question() ; q.setId(1L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(2L); m.add(q);
        q = new Question() ; q.setId(2L); q.setShortname("10"); q.setQTxt("Q"); q.setJQuestion_id(3L); m.add(q);
        q = new Question() ; q.setId(3L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(4L); m.add(q);
        q = new Question() ; q.setId(5L); q.setShortname("2"); q.setQTxt("Q"); q.setJQuestion_id(6L); m.add(q);
        q = new Question() ; q.setId(6L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(7L); m.add(q);
        q = new Question() ; q.setId(7L); q.setShortname("6"); q.setQTxt("Q"); q.setJQuestion_id(8L); m.add(q);
        q = new Question() ; q.setId(10L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(11L); m.add(q);
        q = new Question() ; q.setId(11L); q.setShortname("9"); q.setQTxt("Q"); q.setJQuestion_id(12L); m.add(q);
        q = new Question() ; q.setId(13L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(14L); m.add(q);
        q = new Question() ; q.setId(14L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(15L); m.add(q);
        q = new Question() ; q.setId(15L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(16L); m.add(q);
        q = new Question() ; q.setId(16L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(17L); m.add(q);
        q = new Question() ; q.setId(17L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(18L); m.add(q);
        q = new Question() ; q.setId(18L); q.setShortname("5"); q.setQTxt("Q"); q.setJQuestion_id(19L); m.add(q);
        q = new Question() ; q.setId(19L); q.setShortname("1"); q.setQTxt("Q"); q.setJQuestion_id(20L); m.add(q);
        q = new Question() ; q.setId(20L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(21L); m.add(q);
        q = new Question() ; q.setId(21L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(22L); m.add(q);
        q = new Question() ; q.setId(22L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(23L); m.add(q);
        q = new Question() ; q.setId(23L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(24L); m.add(q);
        q = new Question() ; q.setId(24L); q.setShortname("7"); q.setQTxt("Q"); q.setJQuestion_id(25L); m.add(q);
        q = new Question() ; q.setId(26L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(27L); m.add(q);
        q = new Question() ; q.setId(27L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(28L); m.add(q);
        q = new Question() ; q.setId(28L); q.setShortname("4"); q.setQTxt("Q"); q.setJQuestion_id(29L); m.add(q);
        q = new Question() ; q.setId(29L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(30L); m.add(q);
        q = new Question() ; q.setId(31L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(0L); m.add(q);
        q = new Question() ; q.setId(8L); q.setShortname("D-O"); q.setQTxt("tie"); q.setJQuestion_id(9L); m.add(q);
        q = new Question() ; q.setId(4L); q.setShortname("E-A"); q.setQTxt("tie"); q.setJQuestion_id(5L); m.add(q);
        q = new Question() ; q.setId(9L); q.setShortname("E-D"); q.setQTxt("tie"); q.setJQuestion_id(10L); m.add(q);
        q = new Question() ; q.setId(12L); q.setShortname("O-A"); q.setQTxt("tie"); q.setJQuestion_id(13L); m.add(q);
        q = new Question() ; q.setId(25L); q.setShortname("E-O"); q.setQTxt("tie"); q.setJQuestion_id(26L); m.add(q);
        q = new Question() ; q.setId(30L); q.setShortname("D-A"); q.setQTxt("tie"); q.setJQuestion_id(0L); m.add(q);
        q = new Question() ; q.setId(32L); q.setShortname("123"); q.setQTxt("23"); q.setJQuestion_id(0L); m.add(q);
        q = new Question() ; q.setId(62L); q.setShortname("D-A"); q.setQTxt("tie"); q.setJQuestion_id(0L); m.add(q);
        q = new Question() ; q.setId(33L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(34L); m.add(q);
        q = new Question() ; q.setId(34L); q.setShortname("6"); q.setQTxt("Q"); q.setJQuestion_id(35L); m.add(q);
        q = new Question() ; q.setId(35L); q.setShortname("D-O"); q.setQTxt("tie"); q.setJQuestion_id(36L); m.add(q);
        q = new Question() ; q.setId(36L); q.setShortname("E-A"); q.setQTxt("tie"); q.setJQuestion_id(37L); m.add(q);
        q = new Question() ; q.setId(37L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(38L); m.add(q);
        q = new Question() ; q.setId(38L); q.setShortname("2"); q.setQTxt("Q"); q.setJQuestion_id(39L); m.add(q);
        q = new Question() ; q.setId(39L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(40L); m.add(q);
        q = new Question() ; q.setId(40L); q.setShortname("10"); q.setQTxt("Q"); q.setJQuestion_id(41L); m.add(q);
        q = new Question() ; q.setId(41L); q.setShortname("E-D"); q.setQTxt("tie"); q.setJQuestion_id(42L); m.add(q);
        q = new Question() ; q.setId(42L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(43L); m.add(q);
        q = new Question() ; q.setId(43L); q.setShortname("9"); q.setQTxt("Q"); q.setJQuestion_id(44L); m.add(q);
        q = new Question() ; q.setId(44L); q.setShortname("O-A"); q.setQTxt("tie"); q.setJQuestion_id(45L); m.add(q);
        q = new Question() ; q.setId(45L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(46L); m.add(q);
        q = new Question() ; q.setId(46L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(47L); m.add(q);
        q = new Question() ; q.setId(47L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(48L); m.add(q);
        q = new Question() ; q.setId(48L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(49L); m.add(q);
        q = new Question() ; q.setId(49L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(50L); m.add(q);
        q = new Question() ; q.setId(50L); q.setShortname("5"); q.setQTxt("Q"); q.setJQuestion_id(51L); m.add(q);
        q = new Question() ; q.setId(51L); q.setShortname("1"); q.setQTxt("Q"); q.setJQuestion_id(52L); m.add(q);
        q = new Question() ; q.setId(52L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(53L); m.add(q);
        q = new Question() ; q.setId(53L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(54L); m.add(q);
        q = new Question() ; q.setId(54L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(55L); m.add(q);
        q = new Question() ; q.setId(55L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(56L); m.add(q);
        q = new Question() ; q.setId(56L); q.setShortname("7"); q.setQTxt("Q"); q.setJQuestion_id(57L); m.add(q);
        q = new Question() ; q.setId(57L); q.setShortname("E-O"); q.setQTxt("tie"); q.setJQuestion_id(58L); m.add(q);
        q = new Question() ; q.setId(58L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(59L); m.add(q);
        q = new Question() ; q.setId(59L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(60L); m.add(q);
        q = new Question() ; q.setId(60L); q.setShortname("4"); q.setQTxt("Q"); q.setJQuestion_id(61L); m.add(q);
        q = new Question() ; q.setId(61L); q.setShortname("11"); q.setQTxt("Q"); q.setJQuestion_id(62L); m.add(q);
        q = new Question() ; q.setId(62L); q.setShortname("D-A"); q.setQTxt("tie"); q.setJQuestion_id(0L); m.add(q);


        survey.setQuestions(m);

    }


    protected void populateMiroResponse(MiroResponse mr, User practitioner,
                                        User candidate) {
        mr.setPractitionerEmail(practitioner.getEmail());
        mr.setPractitionerName(practitioner.getFirstName() +" " + practitioner.getLastName());
        mr.setPractitionerTelNo(practitioner.getPhoneNumber());
        mr.setPractitionerAddress(new String[] {practitioner.getAddress1(),practitioner.getAddress2(),practitioner.getCity(),practitioner.getCounty(),practitioner.getPostcode()});
        mr.setFirstName(candidate.getFirstName());
        mr.setLastName(candidate.getLastName());
        mr.setWebaddress(practitioner.getWebaddress());
        mr.setCompany(practitioner.getCompany());
        mr.setMiroReportName(candidate.getReportFileName());

    }


}
