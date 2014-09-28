package uk.co.bluetrail.miro;

import junit.framework.Assert;
import uk.co.bluetrail.mobriz.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class MiroReport11Test extends MiroReport10Test  {


    public void initTestData(MiroResponse miroResponse) {


        SurveyResponse surveyResponse  = new SurveyResponse();
        surveyResponse.setId(new Long(1));
        surveyResponse.setSurvey_id(MiroResponse.Survey_id_Mirov11);

        surveyResponse.setAnswer_trail("Charming#E;Tolerant#O~Self-assured#D;Impulsive#E~Empathic#O;Competitive#D~Charismatic#E;Methodical#A ~Positive#E;Pioneering#D~Amiable#O;Sceptical#D~Good-natured#O;Unwavering#D~Affable#E;Adventurous#D~Playful#E;Demanding#D~Admirable#E;Forceful#D~Companionable#E;Self-sufficient#D~Kind-hearted#O;Orderly#A~Unconventional#E;Conventional#A~Gregarious#E;Level-headed#A~Open #E;No-nonsense #D~Friendly#E;Forthright#D~Big-hearted#O;Well-disciplined#A~Relaxed#O;Exacting#A~Gentle#O;Modest#A~Sophisticated#A;Compassionate#O~Popular#E;Eager#D~Optimistic#E;Risk taking#D~Open-minded#A;Self-confident#D~Respectful  #O;Definite #D~Unpredictable #E;Stable#O~Self-reliant#D;Restrained#A~Attentive#O;Diplomatic#A~Helpful#O;Determined#D~Contented#O;Restless#D~Perfectionist#A;Impatient#D~~true#plus~true#plus~true#minus~true#minus~true#plus~false#plus~false#plus~true#minus~false#plus~true#plus~false#plus~true#plus~false#plus~true#plus~false#plus~false#plus~false#plus~true#plus~false#plus");
        
        surveyResponse.setQuestion_trail("33~34~35~36~37~38~39~40~41~42~43~44~45~46~47~48~49~50~51~52~53~54~55~56~57~58~59~60~61~62~63~64~65~66~67~68~69~70~71~72~73~74~75~76~77~78~79~80~81~82~83");

        User candidate = new User();

        candidate.setFirstName("Roger");
        candidate.setLastName("Test");
        candidate.setId(new Long(1000));

        surveyResponse.setUser(candidate);


        Survey survey = new Survey();


        survey.setId(MiroResponse.Survey_id_Mirov11);
        survey.setFirstQuestion_id(33L);
        initQuestions(survey);

        List<Survey> surveys = new ArrayList<Survey>() ;
        surveys.add(survey);
        miroResponse.init(surveys,surveyResponse, this.miroLetters,this.testOffset);


        System.out.println("arse");


        User practitioner = this.prac;

        candidate.setResponse_id(surveyResponse.getId());
        populateMiroResponse(miroResponse,practitioner,candidate) ;

        MiroProject miroProject = new MiroProject();
        miroResponse.setMiroProject(miroProject);
    }

    public void testGetExtraIntroStr () {

        MiroResponse mr = new MiroResponse() ;


        Assert.assertEquals(mr.getExtraIntroStr(0),"LEX") ;
        Assert.assertEquals(mr.getExtraIntroStr(5),"LEX") ;
        Assert.assertEquals(mr.getExtraIntroStr(6),"MEX") ;
        Assert.assertEquals(mr.getExtraIntroStr(14),"MEX") ;
        Assert.assertEquals(mr.getExtraIntroStr(15),"HEX") ;
        Assert.assertEquals(mr.getExtraIntroStr(19),"HEX") ;
        Assert.assertEquals(mr.getExtraIntroStr(-1),"LIN") ;
        Assert.assertEquals(mr.getExtraIntroStr(-5),"LIN") ;
        Assert.assertEquals(mr.getExtraIntroStr(-6),"MIN") ;
        Assert.assertEquals(mr.getExtraIntroStr(-14),"MIN") ;
        Assert.assertEquals(mr.getExtraIntroStr(-15),"HIN") ;
        Assert.assertEquals(mr.getExtraIntroStr(-19),"HIN") ;

    }

    public void testGenerate() {
        MiroReport miroReport = getMiroReport(this.baseDirPath);

        MiroResponse miroResponse = new MiroResponse();

        initTestData(miroResponse);

        try {
            miroReport.generateReport(miroResponse,MiroResponse.Survey_id_Mirov11);

            String reportFile = miroReport.getReportFilePath(miroResponse,MiroResponse.Survey_id_Mirov11) ;

            File f = new File(reportFile);

            if(!f.exists()) {
                fail("pdf not created for " + reportFile);
            }

        } catch (Exception e) {
            fail("failed with " + e.getMessage());
        }




    }






    public void testCalculateResults() {
        MiroReport MiroReport = getMiroReport(this.baseDirPath);

        MiroResponse miroResponse = new MiroResponse();

        this.initTestData(miroResponse);



        try {

            miroResponse.forceCalculateResults();
            Assert.assertEquals("extroIntraValue should be 13", 13, miroResponse.extroIntraValue);


        } catch (Exception e) {
            fail("failed with " + e.getMessage());
        }




    }



    protected void initQuestions(Survey survey) {

        Question q = null;
        HashSet m = new HashSet();


        q = new Question() ; q.setId(1L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(2L); m.add(q);
        q = new Question() ; q.setId(2L); q.setShortname("10"); q.setQMeta("Q"); q.setJQuestion_id(3L); m.add(q);
        q = new Question() ; q.setId(3L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(4L); m.add(q);
        q = new Question() ; q.setId(5L); q.setShortname("2"); q.setQMeta("Q"); q.setJQuestion_id(6L); m.add(q);
        q = new Question() ; q.setId(6L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(7L); m.add(q);
        q = new Question() ; q.setId(7L); q.setShortname("6"); q.setQMeta("Q"); q.setJQuestion_id(8L); m.add(q);
        q = new Question() ; q.setId(10L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(11L); m.add(q);
        q = new Question() ; q.setId(11L); q.setShortname("9"); q.setQMeta("Q"); q.setJQuestion_id(12L); m.add(q);
        q = new Question() ; q.setId(13L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(14L); m.add(q);
        q = new Question() ; q.setId(14L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(15L); m.add(q);
        q = new Question() ; q.setId(15L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(16L); m.add(q);
        q = new Question() ; q.setId(16L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(17L); m.add(q);
        q = new Question() ; q.setId(17L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(18L); m.add(q);
        q = new Question() ; q.setId(18L); q.setShortname("5"); q.setQMeta("Q"); q.setJQuestion_id(19L); m.add(q);
        q = new Question() ; q.setId(19L); q.setShortname("1"); q.setQMeta("Q"); q.setJQuestion_id(20L); m.add(q);
        q = new Question() ; q.setId(20L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(21L); m.add(q);
        q = new Question() ; q.setId(21L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(22L); m.add(q);
        q = new Question() ; q.setId(22L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(23L); m.add(q);
        q = new Question() ; q.setId(23L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(24L); m.add(q);
        q = new Question() ; q.setId(24L); q.setShortname("7"); q.setQMeta("Q"); q.setJQuestion_id(25L); m.add(q);
        q = new Question() ; q.setId(26L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(27L); m.add(q);
        q = new Question() ; q.setId(27L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(28L); m.add(q);
        q = new Question() ; q.setId(28L); q.setShortname("4"); q.setQMeta("Q"); q.setJQuestion_id(29L); m.add(q);
        q = new Question() ; q.setId(29L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(30L); m.add(q);
        q = new Question() ; q.setId(31L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(0L); m.add(q);
        q = new Question() ; q.setId(8L); q.setShortname("D-O"); q.setQMeta("tie"); q.setJQuestion_id(9L); m.add(q);
        q = new Question() ; q.setId(4L); q.setShortname("E-A"); q.setQMeta("tie"); q.setJQuestion_id(5L); m.add(q);
        q = new Question() ; q.setId(9L); q.setShortname("E-D"); q.setQMeta("tie"); q.setJQuestion_id(10L); m.add(q);
        q = new Question() ; q.setId(12L); q.setShortname("O-A"); q.setQMeta("tie"); q.setJQuestion_id(13L); m.add(q);
        q = new Question() ; q.setId(25L); q.setShortname("E-O"); q.setQMeta("tie"); q.setJQuestion_id(26L); m.add(q);
        q = new Question() ; q.setId(30L); q.setShortname("D-A"); q.setQMeta("tie"); q.setJQuestion_id(0L); m.add(q);
        q = new Question() ; q.setId(32L); q.setShortname("123"); q.setQMeta("23"); q.setJQuestion_id(0L); m.add(q);
        q = new Question() ; q.setId(33L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(34L); m.add(q);
        q = new Question() ; q.setId(34L); q.setShortname("6"); q.setQMeta("Q"); q.setJQuestion_id(35L); m.add(q);
        q = new Question() ; q.setId(35L); q.setShortname("D-O"); q.setQMeta("tie"); q.setJQuestion_id(36L); m.add(q);
        q = new Question() ; q.setId(36L); q.setShortname("E-A"); q.setQMeta("tie"); q.setJQuestion_id(37L); m.add(q);
        q = new Question() ; q.setId(37L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(38L); m.add(q);
        q = new Question() ; q.setId(38L); q.setShortname("2"); q.setQMeta("Q"); q.setJQuestion_id(39L); m.add(q);
        q = new Question() ; q.setId(39L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(40L); m.add(q);
        q = new Question() ; q.setId(40L); q.setShortname("10"); q.setQMeta("Q"); q.setJQuestion_id(41L); m.add(q);
        q = new Question() ; q.setId(41L); q.setShortname("E-D"); q.setQMeta("tie"); q.setJQuestion_id(42L); m.add(q);
        q = new Question() ; q.setId(42L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(43L); m.add(q);
        q = new Question() ; q.setId(43L); q.setShortname("9"); q.setQMeta("Q"); q.setJQuestion_id(44L); m.add(q);
        q = new Question() ; q.setId(44L); q.setShortname("O-A"); q.setQMeta("tie"); q.setJQuestion_id(45L); m.add(q);
        q = new Question() ; q.setId(45L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(46L); m.add(q);
        q = new Question() ; q.setId(46L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(47L); m.add(q);
        q = new Question() ; q.setId(47L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(48L); m.add(q);
        q = new Question() ; q.setId(48L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(49L); m.add(q);
        q = new Question() ; q.setId(49L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(50L); m.add(q);
        q = new Question() ; q.setId(50L); q.setShortname("5"); q.setQMeta("Q"); q.setJQuestion_id(51L); m.add(q);
        q = new Question() ; q.setId(51L); q.setShortname("1"); q.setQMeta("Q"); q.setJQuestion_id(52L); m.add(q);
        q = new Question() ; q.setId(52L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(53L); m.add(q);
        q = new Question() ; q.setId(53L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(54L); m.add(q);
        q = new Question() ; q.setId(54L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(55L); m.add(q);
        q = new Question() ; q.setId(55L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(56L); m.add(q);
        q = new Question() ; q.setId(56L); q.setShortname("7"); q.setQMeta("Q"); q.setJQuestion_id(57L); m.add(q);
        q = new Question() ; q.setId(57L); q.setShortname("E-O"); q.setQMeta("tie"); q.setJQuestion_id(58L); m.add(q);
        q = new Question() ; q.setId(58L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(59L); m.add(q);
        q = new Question() ; q.setId(59L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(60L); m.add(q);
        q = new Question() ; q.setId(60L); q.setShortname("4"); q.setQMeta("Q"); q.setJQuestion_id(61L); m.add(q);
        q = new Question() ; q.setId(61L); q.setShortname("11"); q.setQMeta("Q"); q.setJQuestion_id(62L); m.add(q);
        q = new Question() ; q.setId(62L); q.setShortname("D-A"); q.setQMeta("tie"); q.setJQuestion_id(63L); m.add(q);
        q = new Question() ; q.setId(63L); q.setShortname(""); q.setQMeta("Q11Inst"); q.setJQuestion_id(64L); m.add(q);
        q = new Question() ; q.setId(64L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(65L); m.add(q);
        q = new Question() ; q.setId(65L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(66L); m.add(q);
        q = new Question() ; q.setId(66L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(67L); m.add(q);
        q = new Question() ; q.setId(67L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(68L); m.add(q);
        q = new Question() ; q.setId(68L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(69L); m.add(q);
        q = new Question() ; q.setId(69L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(70L); m.add(q);
        q = new Question() ; q.setId(70L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(71L); m.add(q);
        q = new Question() ; q.setId(71L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(72L); m.add(q);
        q = new Question() ; q.setId(72L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(73L); m.add(q);
        q = new Question() ; q.setId(73L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(74L); m.add(q);
        q = new Question() ; q.setId(74L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(75L); m.add(q);
        q = new Question() ; q.setId(75L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(76L); m.add(q);
        q = new Question() ; q.setId(76L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(77L); m.add(q);
        q = new Question() ; q.setId(77L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(78L); m.add(q);
        q = new Question() ; q.setId(78L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(79L); m.add(q);
        q = new Question() ; q.setId(79L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(80L); m.add(q);
        q = new Question() ; q.setId(80L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(81L); m.add(q);
        q = new Question() ; q.setId(81L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(82L); m.add(q);
        q = new Question() ; q.setId(82L); q.setShortname(""); q.setQMeta("Q11"); q.setJQuestion_id(0L); m.add(q);


        survey.setQuestions(m);

    }





}
