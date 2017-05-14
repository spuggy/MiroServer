package uk.co.bluetrail.mobriz.service.impl;

import uk.co.bluetrail.miro.MiroConstants;
import uk.co.bluetrail.miro.MiroReport;
import uk.co.bluetrail.miro.MiroResponse;
import uk.co.bluetrail.miro.MiroTeamPieChart;
import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.dao.SurveyResponseDAO;
import uk.co.bluetrail.mobriz.model.*;
import uk.co.bluetrail.mobriz.service.*;
import uk.co.bluetrail.mobriz.serviceDTO.TeamMapDTO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.util.*;

public class MiroResponseManagerImpl extends BaseManager implements MiroResponseManager {

    protected Setting miroLetters;
    protected SettingManager settingManager;
    private UserManager userManager;
    private SurveyManager surveyManager;
    private int testOffset;
    private SurveyResponseDAO surveyResponseDAO;
    private MiroProjectManager miroProjectManager;
    private String pdfLocation_dev;
    private String pdfLocation_live;
    private String pdfLocation_test;

    private double miroGraphAdjustment;

    private int excessScore;
    private int engagedScore;
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
     * @return the pdfLocation_test
     * @hibernate.property
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
     * @return the pdfLocation_dev
     * @hibernate.property
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
     * @return the pdfLocation_live
     * @hibernate.property
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

        Survey survey = surveyManager.getSurvey(sr.getSurvey_id().toString());

        MiroResponse mr = new MiroResponse(survey, sr, this.miroLetters, this.testOffset);

        return mr.isValid();

    }


    protected void setup() {


        miroLetters = settingManager.getSettingByName("MIRO_LETTERS");

        if (miroLetters == null) {
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




    public String getRawResults(SurveyResponse sr, MiroResponse mr, String baseDirectory) throws Exception {

        User candidate = sr.getUser();

        MiroReport miroReport = getMiroReport(baseDirectory,candidate);

        this.setup();

        Survey survey = surveyManager.getSurvey(sr.getSurvey_id().toString());


        mr.init(survey, sr, this.miroLetters, this.testOffset);

        MiroProject miroProject = miroProjectManager.getMiroProject(candidate.getProject_id().toString());

        User practitioner = userManager.getUser(miroProject.getCreatedBy_id().toString());

        populateMiroResponse(mr, practitioner, candidate);


        mr.setMiroProject(miroProject);


        StringBuffer sb = new StringBuffer();

        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);

        sb.append(sr.getId());
        sb.append(",");
        appendString(sb, candidate.getFirstName());
        appendString(sb, candidate.getLastName());
        sb.append(df.format(sr.getUpdated_at()));
        sb.append(",");
        sb.append(miroProject.getId());
        sb.append(",");
        appendString(sb, miroProject.getProjectTitle());
        appendString(sb, practitioner.getFullName());

        int x[] = mr.getResults();
        String c[] = mr.getResultLetters();

        for (int i = 0; i < x.length; i++) {
            sb.append(c[i]);
            sb.append(",");


        }

        for (int i = 0; i < x.length; i++) {

            sb.append(x[i]);
            sb.append(",");

        }

        return sb.toString();
    }

    public void appendString(StringBuffer sb, String str) {
        sb.append("\"");
        sb.append(str);
        sb.append("\"");
        sb.append(",");
    }


    public List getUnprocessedMiroResponses(int miroDocLimit) {

        return this.surveyResponseDAO.getUnprocessedResponses(miroDocLimit);

    }

    /**
     * @param surveyResponseDAO the surveyReponseDAO to set
     */
    public void setSurveyReponseDAO(SurveyResponseDAO surveyResponseDAO) {


        this.surveyResponseDAO = surveyResponseDAO;
    }

    private MiroReport getMiroReport(String baseDirectory) {
        return getMiroReport(baseDirectory,null);
    }

    private MiroReport getMiroReport(String baseDirectory,User candidate) {


        MiroReport miroReport = new MiroReport(new File(baseDirectory), this.engagedScore, this.excessScore, this.latentScore);

        HashMap modes = new HashMap();
        modes.put("E", "Energising Mode");
        modes.put("D", "Driving Mode");
        modes.put("A", "Analysing Mode");
        modes.put("O", "Organising Mode");

        String[] subPieTxtLeg  = new String[] {"Pivot Point (Dominant Function)", "Auxiliary Function",
        "Tertiary Function","Inferior Function"};

        HashMap<String, String> subPieSubTxtLeg =  new HashMap<String, String>();
        subPieSubTxtLeg.put("Ni",	"Introverted iNtuition");
        subPieSubTxtLeg.put("Ne",	"Extroverted iNtuition");
        subPieSubTxtLeg.put("Si",	"Introverted Sensing");
        subPieSubTxtLeg.put("Se",	"Extroverted Sensing");
        subPieSubTxtLeg.put("Ti",	"Introverted Thinking");
        subPieSubTxtLeg.put("Te",	"Extroverted Thinking");
        subPieSubTxtLeg.put("Fi",	"Introverted Feeling");
        subPieSubTxtLeg.put("Fe",	"Extroverted Feeling");


        HashMap<String, String[]> subPieOrdering = new HashMap<String, String[]>();
        subPieOrdering.put("ENTJ", new String[]{"Te", "Ni", "Si", "Fe"});
        subPieOrdering.put("ENTP", new String[]{"Ne", "Ti", "Fi", "Se"});
        subPieOrdering.put("INTJ", new String[]{"Ni", "Te	Fe	Si"});
        subPieOrdering.put("INTP", new String[]{"Ti", "Ne", "Se", "Fi"});
        subPieOrdering.put("ENFJ", new String[]{"Fe", "Ni", "Si", "Te"});
        subPieOrdering.put("ENFP", new String[]{"Ne", "Fi", "Ti", "Se"});
        subPieOrdering.put("INFJ", new String[]{"Ni", "Fe", "Te", "Si"});
        subPieOrdering.put("INFP", new String[]{"Fi", "Ne", "Se", "Ti"});
        subPieOrdering.put("ESFJ", new String[]{"Fe", "Si", "Ni", "Te"});
        subPieOrdering.put("ESFP", new String[]{"Se", "Fi", "Ti", "Ni"});
        subPieOrdering.put("ISFJ", new String[]{"Si", "Fe", "Te", "Ne"});
        subPieOrdering.put("ISFP", new String[]{"Fi", "Se", "Ne", "Ti"});
        subPieOrdering.put("ESTJ", new String[]{"Te", "Si", "Ni", "Fi"});
        subPieOrdering.put("ESTP", new String[]{"Se", "Ti", "Fi", "Ne"});
        subPieOrdering.put("ISTJ", new String[]{"Si", "Te", "Fe", "Ni"});
        subPieOrdering.put("ISTP", new String[]{"Ti", "Se", "Ne", "Fe"});

        HashMap<String, String> subPieAddtionalText = new HashMap<String, String>();

        subPieAddtionalText.put("SLEX", "Slightly");
        subPieAddtionalText.put("NLEX", "Slightly");
        subPieAddtionalText.put("TLEX", "Slightly");
        subPieAddtionalText.put("FLEX", "Slightly");
        subPieAddtionalText.put("SMEX", "Moderately");
        subPieAddtionalText.put("NMEX", "Moderately");
        subPieAddtionalText.put("TMEX", "Moderately");
        subPieAddtionalText.put("FMEX", "Moderately");
        subPieAddtionalText.put("SHEX", "Strongly");
        subPieAddtionalText.put("NHEX", "Strongly");
        subPieAddtionalText.put("THEX", "Strongly");
        subPieAddtionalText.put("FHEX", "Strongly");
        subPieAddtionalText.put("SLIN", "Slightly");
        subPieAddtionalText.put("NLIN", "Slightly");
        subPieAddtionalText.put("TLIN", "Slightly");
        subPieAddtionalText.put("FLIN", "Slightly");
        subPieAddtionalText.put("SMIN", "Moderately");
        subPieAddtionalText.put("NMIN", "Moderately");
        subPieAddtionalText.put("TMIN", "Moderately");
        subPieAddtionalText.put("FMIN", "Moderately");
        subPieAddtionalText.put("SHIN", "Strongly");
        subPieAddtionalText.put("NHIN", "Strongly");
        subPieAddtionalText.put("THIN", "Strongly");
        subPieAddtionalText.put("FHIN", "Strongly");


        MiroConstants constants = MiroConstants.getInstance();

        HashMap colors = new HashMap();
        colors.put("E", constants.miroYellow);
        colors.put("D", constants.miroRed);
        colors.put("A", constants.miroBlue);
        colors.put("O", constants.miroGreen);

        miroReport.setSubPieOrdering(subPieOrdering);
        miroReport.setSubPieSubTxtLeg(subPieSubTxtLeg);
        miroReport.setSubPieAddtionalText(subPieAddtionalText);
        miroReport.setSubPieTxtLeg(subPieTxtLeg);
        miroReport.setModes(modes);
        miroReport.setColors(colors);
        miroReport.setEngagedText("Engaged");
        miroReport.setDisEngagedText("Disengaged");
        miroReport.setLatentText("Latent");
        miroReport.setExcessText("Excess");
        miroReport.setMiroGraphAdjustment(miroGraphAdjustment);
        miroReport.setLabels2(new String[]{"Leading", "Supporting", "Supplementary", "Dormant"});
        miroReport.setSubPieAddtionalText(subPieAddtionalText);

        if(candidate!=null && candidate.getUserType().equals(User.USER_TYPE_FREE)) {
            miroReport.setIsFreeReport(true);
        }


        return miroReport;


    }

    public void createPie(MiroResponse mr, String baseDirectory, boolean plain) {


        MiroReport miroReport = getMiroReport(baseDirectory);

        miroReport.generatePieChart(mr, "", true, true,true);

    }


    public boolean createPDF(SurveyResponse sr, MiroResponse mr, String baseDirectory) throws Exception {

        User candidate = sr.getUser();

        MiroReport miroReport = getMiroReport(baseDirectory,candidate);

        this.setup();

        Survey survey = surveyManager.getSurvey(sr.getSurvey_id().toString());

        mr.init(survey, sr, this.miroLetters, this.testOffset);



        MiroProject miroProject = miroProjectManager.getMiroProject(candidate.getProject_id().toString());

        User practitioner = userManager.getUser(miroProject.getCreatedBy_id().toString());

        candidate.setResponse_id(sr.getId());
        populateMiroResponse(mr, practitioner, candidate);

        mr.setMiroProject(miroProject);

        miroReport.generateReport(mr, Constants.Survey_id_Mirov10)  ;

        if(mr.supportsVersion(Constants.Survey_id_Mirov11)) {
            miroReport.generateReport(mr, Constants.Survey_id_Mirov11);
        }

        sr.setAlertsProcessed(true);

        //push on to purchased if a free jobby
        if(candidate.getUserType().equals(User.USER_TYPE_FREE)) {
            userManager.buyReport(candidate.getId(), sr.getCreatedBy_id());
        } else {
            userManager.saveAsPurchased(candidate.getId(), sr.getId());
        }

        this.surveyResponseDAO.saveSurveyResponse(sr);


        return true;

    }


    private void populateMiroResponse(MiroResponse mr, User practitioner,
                                      User candidate) {
        mr.setPractitionerEmail(practitioner.getEmail());
        mr.setPractitionerName(practitioner.getFirstName() + " " + practitioner.getLastName());
        mr.setPractitionerTelNo(practitioner.getPhoneNumber());
        mr.setPractitionerAddress(new String[]{practitioner.getAddress1(), practitioner.getAddress2(), practitioner.getCity(), practitioner.getCounty(), practitioner.getPostcode()});
        mr.setFirstName(candidate.getFirstName());
        mr.setLastName(candidate.getLastName());
        mr.setWebaddress(practitioner.getWebaddress());
        mr.setCompany(practitioner.getCompany());
        mr.setMiroReportName(candidate.getReportFileName());

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

        if (appURL.toLowerCase().indexOf("localhost") > 0) {
            return this.getPdfLocation_dev();

        }

        if (appURL.toLowerCase().indexOf("dev.bluetrail.co.uk") > 0) {
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

        MiroTeamPieChart miroTeamPieChart = new MiroTeamPieChart(teamPieWidth, teamPieHeight, this.thumbNailPieWidth, this.thumbNailPieHeight);


        Survey survey = null;

        while (itr.hasNext()) {
            user = (User) itr.next();
            sr = surveyResponseDAO.getSurveyResponse(user.getResponse_id());

            survey = surveyManager.getSurvey(sr.getSurvey_id().toString());

            mr = new MiroResponse();
            mr.init(survey, sr, this.miroLetters, this.testOffset);
            try {
                BufferedImage bi = miroReport.getThumbnailPie(user.getFullName(), mr, this.thumbNailPieWidth, this.thumbNailPieHeight);
                miroTeamPieChart.add(bi);
            } catch (Exception e) {
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

    public List getTeamMap(String baseDirectory, Collection users) {

        MiroReport miroReport = getMiroReport(baseDirectory);

        this.setup();

        Iterator itr = users.iterator();

        User candidate = null;
        User practitioner = null;
        SurveyResponse sr = null;
        MiroResponse mr = null;

        HashMap initials = new HashMap();
        TeamMapDTO teamMapDTO = null;
        ArrayList teamMapData = new ArrayList();

        Survey survey = null ;

        while (itr.hasNext()) {
            candidate = (User) itr.next();

            practitioner = getPractitioner(candidate, practitioner);


            sr = surveyResponseDAO.getSurveyResponse(candidate.getResponse_id());

            survey = surveyManager.getSurvey(sr.getSurvey_id().toString());


            mr = new MiroResponse();
            mr.init(survey, sr, this.miroLetters, this.testOffset);
            teamMapDTO = new TeamMapDTO();
            try {
                teamMapDTO = new TeamMapDTO();
                teamMapDTO.setId(candidate.getId());
                teamMapDTO.setFullName(candidate.getFullName());
                teamMapDTO.setInitials(candidate.getInitials(initials));
                this.populateMiroResponse(mr, practitioner, candidate);
                miroReport.setTeamMapData(teamMapDTO, mr);
                teamMapDTO.setMiroResponse(mr);
                teamMapData.add(teamMapDTO);

            } catch (Exception e) {
                log.error("Error trying to create teamMap for userid = " + candidate.getId());
            }
        }

        return teamMapData;

    }

    private User getPractitioner(User candidate, User practitioner) {

        if (practitioner != null) {
            return practitioner;
        }

        User tmpPractitioner = userManager.getUser(candidate.getCreatedBy_id().toString());

        return tmpPractitioner;


    }

    public Setting getMiroLetters() {
        return miroLetters;
    }


}
