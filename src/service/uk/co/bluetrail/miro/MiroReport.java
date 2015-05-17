package uk.co.bluetrail.miro;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.title.TextTitle;
import org.xml.sax.SAXException;
import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.serviceDTO.TeamMapDTO;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


/**
 * generates miro report
 *
 * @author Richard
 */
public class MiroReport {

    private final Log log = LogFactory.getLog(MiroReport.class);

    private static int LEFT = 0;
    private static int RIGHT = 1;


    File baseDirectory;
    MiroResponse mr;
    HashMap modes = null;
    String[] labels2 = null;
    String engagedText = null;
    String disEngagedText = null;
    HashMap colors = null;
    MiroReportFileGenerator miroReportFileGenerator;
    HashMap<String, String> legendMap;
    HashMap<String, int[]> jpValueMap;
    HashMap<String, String> mbtimap ;

    private int engagedScore;
    private int excessScore;
    private int latentScore;

    private String latentText;
    private String excessText;

    private double miroGraphAdjustment;
    private HashMap<String, String> subTitles;


    public void setSubTitles(HashMap subTitles) {
        this.subTitles = subTitles;
    }

    /**
     * @return the excessText
     * @hibernate.property
     */
    public String getExcessText() {
        return excessText;
    }

    /**
     * @param excessText the excessText to set
     */
    public void setExcessText(String excessText) {
        this.excessText = excessText;
    }

    /**
     * @return the latentText
     * @hibernate.property
     */
    public String getLatentText() {
        return latentText;
    }

    public String getDisEngagedText() {
        return disEngagedText;
    }

    public void setDisEngagedText(String disEngagedText) {
        this.disEngagedText = disEngagedText;
    }

    public HashMap getColors() {
        return colors;
    }

    public void setColors(HashMap colors) {
        this.colors = colors;
    }

    public String getEngagedText() {
        return engagedText;
    }

    public void setEngagedText(String engagedText) {
        this.engagedText = engagedText;
    }

    public String[] getLabels2() {
        return labels2;
    }

    public void setLabels2(String[] labels2) {
        this.labels2 = labels2;
    }

    public HashMap getModes() {
        return modes;
    }

    public void setModes(HashMap modes) {
        this.modes = modes;
    }

    public MiroReport(File baseDirectory, int engagedScore, int excessScore, int latentScore) {
        this.baseDirectory = baseDirectory;
        this.engagedScore = engagedScore;
        this.excessScore = excessScore;
        this.latentScore = latentScore;
    }

    public boolean generateReport(MiroResponse mr, Long reportVersion) throws Exception {

        if (mr == null) {
            log.error("The Miro response is null");
            return false;
        }


        if (reportVersion == Constants.Survey_id_Mirov10) {
            return this.generateReportV10(mr);
        }

        if (reportVersion == Constants.Survey_id_Mirov11) {
            return this.generateReportV11(mr);
        }

        throw new MiroException("Cannot produces a report for version " + reportVersion);

    }


    private boolean generateReportV11(MiroResponse mr) throws Exception {

        if (mr == null) {
            log.error("The Miro response is null");
        }

        setupMr(mr);

        if (!mr.supportsVersion(Constants.Survey_id_Mirov11)) {
            throw new MiroException(mr.getMiroReportName() + "does not support version v11");
        }

        log.debug("Before Chart " + mr.toString());
        this.generateChart(Constants.Survey_id_Mirov11);
        log.debug("Before XMLReportFile " + mr.toString());
        this.generateXMLReportFile(Constants.Survey_id_Mirov11);
        MiroReportPDFGenerator.generatePDF(this.baseDirectory, mr, Constants.Survey_id_Mirov11);

        return true;

    }

    private boolean generateReportV10(MiroResponse mr) throws Exception {

        if (mr == null) {
            log.error("The Miro response is null");
            return false;
        }

        setupMr(mr);

        if (!mr.supportsVersion(Constants.Survey_id_Mirov10)) {
            throw new MiroException(mr.getMiroReportName() + "does not support version v10");
        }

        log.debug("Before Chart " + mr.toString());
        this.generateChart(Constants.Survey_id_Mirov10);
        log.debug("Before XMLReportFile " + mr.toString());
        this.generateXMLReportFile(Constants.Survey_id_Mirov10);
        MiroReportPDFGenerator.generatePDF(this.baseDirectory, mr, Constants.Survey_id_Mirov10);

        return true;

    }



    private void setupMr(MiroResponse mr) {
        this.mr = mr;
        this.mr.setEngagedScore(this.engagedScore);
        this.mr.setExcessScore(this.excessScore);
        this.mr.setLatentScore(this.latentScore);

    }

    private void generateChart(long reportVersion) {

        this.mr = mr;



        MiroPieChartGenerator pieChart = this.getMiroPieChart("Your MiRo Results Chart", "", true, true, true);
        pieChart.createPie(this.baseDirectory.getAbsolutePath() + "/out/", this.getChartName());

    }

    public void generatePieChart(MiroResponse mr, String title, boolean hideLegend, boolean hideTitle, boolean hideSubTitle) {

        this.mr = mr;


        MiroPieChartGenerator pieChart = this.getMiroPieChart(title, "", hideLegend, hideTitle, hideSubTitle);
        pieChart.createPie(this.baseDirectory.getAbsolutePath() + "/out/", this.getChartName());
    }

    private MiroPieChartGenerator getMiroPieChart(String pieTitle, String pieSubtitle, boolean hideLegend, boolean hideTitle, boolean hideSubTitle) {

        int resultsLen = mr.getResults().length;
        String[] pieLabels1 = new String[resultsLen];
        String[] pieLabels2 = new String[resultsLen];
        String[] pieLabels3 = new String[resultsLen];
        boolean[] pieExplode = new boolean[resultsLen];
        Color[] pieColors = new Color[resultsLen];

        boolean[] attached = mr.getResultsAttached();
        pieLabels2 = this.getLabels2();

        String[] resultLetters = mr.getResultLetters();
        int[] results = mr.getResults();

        for (int i = 0; i < resultsLen; i++) {

            pieLabels1[i] = (String) modes.get(resultLetters[i]);


            if (attached[i]) {
                pieExplode[i] = false;
                if (mr.isExcess(results[i])) {
                    pieLabels3[i] = this.getExcessText();
                } else {
                    pieLabels3[i] = this.getEngagedText();
                }
            } else {
                pieExplode[i] = true;
                if (mr.isLatent(results[i])) {
                    pieLabels3[i] = this.getLatentText();
                } else {
                    pieLabels3[i] = this.getDisEngagedText();
                }

            }


            pieColors[i] = (Color) colors.get(resultLetters[i]);

        }


        return new MiroPieChartGenerator(pieTitle, pieSubtitle, mr.getResults(this.miroGraphAdjustment), pieLabels1, pieLabels2, pieLabels3, pieExplode, pieColors, hideLegend, hideTitle, hideSubTitle);


    }

    /**
     * @throws TransformerException
     * @throws TransformerFactoryConfigurationError
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    private void generateXMLReportFile(long reportVersion) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {

        log.info("Generating " + mr.getFullName() + "_" + mr.getTestId());

        List<MiroPage> pages = mr.getReportPageList(reportVersion);

        //Image Map
        Map<String, String> variables = new HashMap<String, String>();
        variables.put("id", mr.getTestId().toString());
        variables.put("firstname", mr.getFirstName());
        variables.put("lastname", mr.getLastName());
        variables.put("name", mr.getFullName());
        variables.put("name2", mr.getFullName());
        variables.put("v1", mr.getPractitionerName());
        variables.put("reportFileName", mr.getMiroReportName());
        variables.put("toc1", mr.getVariable("toc1",reportVersion));
        variables.put("toc1", mr.getVariable("toc1",reportVersion));
        variables.put("toc2", mr.getVariable("toc2",reportVersion));
        variables.put("toc3", mr.getVariable("toc3",reportVersion));
        variables.put("toc4", mr.getVariable("toc4",reportVersion));
        variables.put("toc5", mr.getVariable("toc5",reportVersion));





        StringBuffer sb = new StringBuffer();

        int vName = 2;

        if (!mr.getCompany().trim().equals("")) {
            variables.put("v" + vName++, mr.getCompany());
        }

        String[] alines = mr.getPractitionerAddress();


        for (int i = 0; i < alines.length; i++) {
            if (alines[i] != null && !alines[i].trim().equals("")) {
                variables.put("v" + vName++, alines[i]);
            }
            variables.put("v" + vName++, " ");

        }


        if (!mr.getPractitionerTelNo().trim().equals("")) {
            variables.put("v" + vName++, "Tel: " + mr.getPractitionerTelNo());
        }

        if (!mr.getPractitionerEmail().trim().equals("")) {

            variables.put("v" + vName++, "Email: " + mr.getPractitionerEmail());

        }

        if (!mr.getWebaddress().trim().equals("")) {
            variables.put("v" + vName++, "Web: " + mr.getWebaddress());
        }


        // Image Map
        Map<String, String> imgNames = new HashMap<String, String>();
        imgNames.put("graph", this.baseDirectory.getAbsolutePath() + "/out/" + this.getChartName());
        imgNames.put("imgU1", this.baseDirectory.getAbsolutePath() + "/miro2/images/" + "U1.png");
        imgNames.put("imgU3", this.baseDirectory.getAbsolutePath() + "/miro2/images/" + "U3.png");
        imgNames.put("imgU2", this.baseDirectory.getAbsolutePath() + "/miro2/images/" + "U2.png");
        imgNames.put("imgU6", this.baseDirectory.getAbsolutePath() + "/miro2/images/" + "U6.png");


        //add pie images
        String[] resultLetters = mr.getResultLetters();
        for (int l = 0; l < resultLetters.length; l++) {
            String resultLetter = resultLetters[l];
            String id = "miropie_img_leg" + (l + 1);

            String legImageName = this.getMiroPieChartLegendValue(resultLetter + "img");
            if (legImageName != null) {
                imgNames.put(id, this.baseDirectory.getAbsolutePath() + "/miro2/images/" + legImageName);
            }

            id = "miropie_txt_leg" + (l + 1);
            String miropieTxt = this.getMiroPieChartLegendValue(resultLetter + "text");
            if (miropieTxt != null) {
                variables.put(id, miropieTxt);
            }

            id = "miropie_subtxt_leg" + (l + 1);
            String miropieSubTxt = this.getMiroPieChartLegendValue(resultLetter + "subText");
            if (miropieTxt != null) {
                variables.put(id, miropieSubTxt);
            }


        }

        variables.put("mbti", this.getMBTIValue());

        //add miro population chart value
        if(reportVersion == Constants.Survey_id_Mirov11) {
            addMiroPopulationChartValues(variables);
            variables.put("report_type", "YOUR MIRO COACHING REPORT");
            variables.put("report_type_colour", "MIRORED");

            //lexmex

            if (this.subTitles != null) {
                String subTitleKey = mr.getExtraIntroMappingKey();
                if(subTitleKey!=null) {
                    String subTitle = subTitles.get(subTitleKey);
                    if (subTitle != null) {
                        variables.put("lexmexpiesubtitle", subTitle);
                    }
                }

            }

        } else {
            variables.put("report_type", "YOUR MIRO REPORT");
            variables.put("report_type_colour", "MIROBLUE");
        }


        if (miroReportFileGenerator == null) {
            miroReportFileGenerator = new MiroReportFileGenerator(this.baseDirectory);
        }
        miroReportFileGenerator.generate(pages, variables, imgNames);


    }

    private int adjustToPercent(int val,int total) {

        if(val == 0 || total ==0) {
            return 0;
        }


        double x = ((double)val/(double) total) * 100;

        return  (int)x;

    }

    private String str(int v) {
         return String.valueOf(v);
    }

    private int safeDiv(int a,int b) {

        if(a == 0 || b ==0) {
            return 0;
        }

        double v = (double)a/(double) b ;

        return  (int) v;
    }

    private void addMiroPopulationChartValues(Map<String, String> variables) {

        int miroAdjustment = 66;
        int intExAdjustment = 20;

        variables.put("leadershipBar_1lv", str(adjustToPercent(getLetterScore("D"), miroAdjustment)));
        variables.put("leadershipBar_1rv", str(adjustToPercent(getLetterScore("O"), miroAdjustment)));
        variables.put("leadershipBar_2lv", str(adjustToPercent(getLetterScore("E"), miroAdjustment)));
        variables.put("leadershipBar_2rv", str(adjustToPercent(getLetterScore("A"), miroAdjustment)));
        variables.put("leadershipBar_3lv", str(adjustToPercent(mr.intraValue, intExAdjustment)));
        variables.put("leadershipBar_3rv", str(adjustToPercent(mr.extroValue, intExAdjustment)));

        variables.put("pmBar_1lv", str(adjustToPercent(getJPValues(LEFT),miroAdjustment)));
        variables.put("pmBar_1rv", str(adjustToPercent(getJPValues(RIGHT),miroAdjustment)));
        variables.put("pmBar_2lv",str(adjustToPercent(getLetterScore("A") + safeDiv(getLetterScore("O"), 2), miroAdjustment)));
        variables.put("pmBar_2rv", str(adjustToPercent(getLetterScore("D") + safeDiv(getLetterScore("E"),2),miroAdjustment)));
        variables.put("pmBar_3lv", str(adjustToPercent(getLetterScore("E") + safeDiv(getLetterScore("O"),2),miroAdjustment)));
        variables.put("pmBar_3rv", str(adjustToPercent(getLetterScore("D") + safeDiv(getLetterScore("A"),2),miroAdjustment)));


        variables.put("negInfluBar_1lv", str(adjustToPercent(mr.intraValue, intExAdjustment)));
        variables.put("negInfluBar_1rv", str(adjustToPercent(mr.extroValue, intExAdjustment)));
        variables.put("negInfluBar_2lv", str(adjustToPercent(getLetterScore("E"), miroAdjustment)));
        variables.put("negInfluBar_2rv", str(adjustToPercent(getLetterScore("A"), miroAdjustment)));
        variables.put("negInfluBar_3lv", str(adjustToPercent(getLetterScore("O"), miroAdjustment)));
        variables.put("negInfluBar_3rv", str(adjustToPercent(getLetterScore("D"), miroAdjustment)));


        variables.put("manChangeBar_1lv", str(adjustToPercent(getLetterScore("D") + safeDiv(getLetterScore("E"),2),miroAdjustment)));
        variables.put("manChangeBar_1rv", str(adjustToPercent(getLetterScore("A") + safeDiv(getLetterScore("O"),2),miroAdjustment)));
        variables.put("manChangeBar_2lv", str(adjustToPercent(getLetterScore("D") + safeDiv(getLetterScore("A"),2),miroAdjustment)));
        variables.put("manChangeBar_2rv", str(adjustToPercent(getLetterScore("E") + safeDiv(getLetterScore("O"),2),miroAdjustment)));
        variables.put("manChangeBar_3lv", str(adjustToPercent(mr.extroValue, intExAdjustment)));
        variables.put("manChangeBar_3rv", str(adjustToPercent(mr.intraValue, intExAdjustment)));

    }

    private String getMBTIValue() {

        if(this.mbtimap ==null) {
            this.mbtimap = new HashMap<String,String>();

            mbtimap.put("DEAOEX","ENTP");
            mbtimap.put("DEAOIN","INTJ");
            mbtimap.put("DEOAEX","ENTP");
            mbtimap.put("DEOAIN","INTJ");
            mbtimap.put("DOAEEX","ENTJ");
            mbtimap.put("DOAEIN","INTP");
            mbtimap.put("DOEAEX","ENTP");
            mbtimap.put("DOEAIN","INTJ");
            mbtimap.put("DAOEEX","ENTJ");
            mbtimap.put("DAOEIN","INTP");
            mbtimap.put("DAEOEX","ENTJ");
            mbtimap.put("DAEOIN","INTP");
            mbtimap.put("EDOAEX","ENFP");
            mbtimap.put("EDOAIN","INFJ");
            mbtimap.put("EDAOEX","ENFP");
            mbtimap.put("EDAOIN","INFJ");
            mbtimap.put("EODAEX","ENFJ");
            mbtimap.put("EODAIN","INFP");
            mbtimap.put("EOADEX","ENFJ");
            mbtimap.put("EOADIN","INFP");
            mbtimap.put("EADOEX","ENFP");
            mbtimap.put("EADOIN","INFJ");
            mbtimap.put("EAODEX","ENFJ");
            mbtimap.put("EAODIN","INFP");
            mbtimap.put("ODEAEX","ESFJ");
            mbtimap.put("ODEAIN","ISFP");
            mbtimap.put("ODAEEX","ESFP");
            mbtimap.put("ODAEIN","ISFJ");
            mbtimap.put("OEODEX","ESFJ");
            mbtimap.put("OEODIN","ISFP");
            mbtimap.put("OEADEX","ESFJ");
            mbtimap.put("OEADIN","ISFP");
            mbtimap.put("OADEEX","ESFP");
            mbtimap.put("OADEIN","ISFJ");
            mbtimap.put("OAEDEX","ESFP");
            mbtimap.put("OAEDIN","ISFJ");
            mbtimap.put("ADEOEX","ESTJ");
            mbtimap.put("ADEOIN","ISTP");
            mbtimap.put("ADOEEX","ESTJ");
            mbtimap.put("ADOEIN","ISTP");
            mbtimap.put("AEDOEX","ESTJ");
            mbtimap.put("AEDOIN","ISTP");
            mbtimap.put("AEODEX","ESTP");
            mbtimap.put("AEODIN","ISTJ");
            mbtimap.put("AODEEX","ESTP");
            mbtimap.put("AODEIN","ISTJ");
            mbtimap.put("AOEDEX","ESTP");
            mbtimap.put("AOEDIN","ISTJ");
            
        }

        try {

            String[] results = mr.getResultLetters();
            StringBuffer keyBuf = new StringBuffer();
            for (int i = 0; i < results.length; i++) {
                keyBuf.append(results[i]);
            }

            String key = keyBuf.toString();


            if (mr.extroValue > mr.intraValue) {
                key = key + "EX";
            } else {
                key = key + "IN";
            }

            String mbtiValue = mbtimap.get(key);    // 0 and 1 are left values 2,3 are right values

            return mbtiValue;

        } catch (Exception ex) {
            System.out.println("getJPValues: " + ex.getMessage());
        }

        return "";

    }

    private int getJPValues(int position) {

        if(this.jpValueMap == null) {
            this.jpValueMap = new HashMap<String, int[]>();
            jpValueMap.put("DEAOEX",new int[]{3,4,1,2});
            jpValueMap.put("DEAOIN",new int[]{1,3,2,4});
            jpValueMap.put("DEOAEX",new int[]{3,4,1,2});
            jpValueMap.put("DEOAIN",new int[]{1,3,2,4});
            jpValueMap.put("DOAEEX",new int[]{1,3,2,4});
            jpValueMap.put("DOAEIN",new int[]{2,4,1,3});
            jpValueMap.put("DOEAEX",new int[]{2,4,1,3});
            jpValueMap.put("DOEAIN",new int[]{1,3,2,4});
            jpValueMap.put("DAOEEX",new int[]{1,2,3,4});
            jpValueMap.put("DAOEIN",new int[]{2,4,1,3});
            jpValueMap.put("DAEOEX",new int[]{1,2,3,4});
            jpValueMap.put("DAEOIN",new int[]{2,4,1,3});
            jpValueMap.put("EDOAEX",new int[]{3,4,1,2});
            jpValueMap.put("EDOAIN",new int[]{1,3,2,4});
            jpValueMap.put("EDAOEX",new int[]{3,4,1,2});
            jpValueMap.put("EDAOIN",new int[]{1,3,2,4});
            jpValueMap.put("EODAEX",new int[]{1,2,3,4});
            jpValueMap.put("EODAIN",new int[]{2,4,1,3});
            jpValueMap.put("EOADEX",new int[]{1,2,3,4});
            jpValueMap.put("EOADIN",new int[]{2,4,1,3});
            jpValueMap.put("EADOEX",new int[]{2,4,1,3});
            jpValueMap.put("EADOIN",new int[]{1,3,2,4});
            jpValueMap.put("EAODEX",new int[]{1,3,2,4});
            jpValueMap.put("EAODIN",new int[]{2,4,1,3});
            jpValueMap.put("ODEAEX",new int[]{1,3,2,4});
            jpValueMap.put("ODEAIN",new int[]{2,4,1,3});
            jpValueMap.put("ODAEEX",new int[]{2,4,1,3});
            jpValueMap.put("ODAEIN",new int[]{1,3,2,4});
            jpValueMap.put("OEODEX",new int[]{1,2,3,4});
            jpValueMap.put("OEODIN",new int[]{2,4,1,3});
            jpValueMap.put("OEADEX",new int[]{1,3,2,4});
            jpValueMap.put("OEADIN",new int[]{2,4,1,3});
            jpValueMap.put("OADEEX",new int[]{3,4,1,2});
            jpValueMap.put("OADEIN",new int[]{1,3,2,4});
            jpValueMap.put("OAEDEX",new int[]{3,4,1,2});
            jpValueMap.put("OAEDIN",new int[]{1,3,2,4});
            jpValueMap.put("ADEOEX",new int[]{1,2,3,4});
            jpValueMap.put("ADEOIN",new int[]{2,4,1,3});
            jpValueMap.put("ADOEEX",new int[]{1,2,3,4});
            jpValueMap.put("ADOEIN",new int[]{2,4,1,3});
            jpValueMap.put("AEDOEX",new int[]{1,3,2,4});
            jpValueMap.put("AEDOIN",new int[]{2,4,1,3});
            jpValueMap.put("AEODEX",new int[]{2,4,1,3});
            jpValueMap.put("AEODIN",new int[]{1,3,2,4});
            jpValueMap.put("AODEEX",new int[]{3,4,1,2});
            jpValueMap.put("AODEIN",new int[]{1,3,2,4});
            jpValueMap.put("AOEDEX",new int[]{3,4,1,2});
            jpValueMap.put("AOEDIN",new int[]{1,2,3,4});
        }

        try {

            String[] results = mr.getResultLetters();
            StringBuffer keyBuf = new StringBuffer();
            for (int i = 0; i < results.length; i++) {
                keyBuf.append(results[i]);
            }

            String key = keyBuf.toString();


            if (mr.extroValue > mr.intraValue) {
                key = key + "EX";
            } else {
                key = key + "IN";
            }

            int[] values = jpValueMap.get(key);    // 0 and 1 are left values 2,3 are right values
            int[] resultValues = mr.getResults();
            if (values == null) {
                return 0;
            } else {
                if (position == RIGHT) {
                    int v1 = resultValues[values[2] - 1];
                    int v2 = resultValues[values[3] - 1];
                    double v = (v1 + v2) * 0.5;
                    return ((int) v) ;
                } else {
                    int v1 = resultValues[values[0] - 1];
                    int v2 = resultValues[values[1] - 1];
                    double v = (v1 + v2) * 0.5;
                    return ((int) v) ;
                }
            }

        } catch (Exception ex) {
            System.out.println("getJPValues: " + ex.getMessage());
        }

        return 0;

    }

    private int getLetterScore(String letter) {

        try {
            int[] results = mr.getResults();
            String[] letters = mr.getResultLetters();

            for (int i = 0; i < results.length; i++) {

                if(letters[i].equalsIgnoreCase(letter)) {
                    return results[i];
                }

            }

        } catch(Exception e) {
           System.out.println("getLetterScore:" + e.getMessage());
        }

        return 0;

    }

    private String getMiroPieChartLegendValue(String key) {

        if (this.legendMap == null) {

            //Images for the pie chart legend
            this.legendMap = new HashMap<String, String>();
            legendMap.put("Aimg", "analysing_mode_leg.png");
            legendMap.put("Eimg", "energising_mode_leg.png");
            legendMap.put("Dimg", "driving_mode_leg.png");
            legendMap.put("Oimg", "organising_mode_leg.png");


            int resultsLen = mr.getResults().length;
            String[] pieLabels1 = new String[resultsLen];
            String[] pieLabels2 = new String[resultsLen];
            String[] pieLabels3 = new String[resultsLen];
            boolean[] pieExplode = new boolean[resultsLen];

            boolean[] attached = mr.getResultsAttached();
            pieLabels2 = this.getLabels2();

            String[] resultLetters = mr.getResultLetters();
            int[] results = mr.getResults();

            for (int i = 0; i < resultsLen; i++) {

                String letter = resultLetters[i];

                pieLabels1[i] = (String) modes.get(resultLetters[i]);


                if (attached[i]) {
                    pieExplode[i] = false;
                    if (mr.isExcess(results[i])) {
                        pieLabels3[i] = this.getExcessText();
                    } else {
                        pieLabels3[i] = this.getEngagedText();
                    }
                } else {
                    pieExplode[i] = true;
                    if (mr.isLatent(results[i])) {
                        pieLabels3[i] = this.getLatentText();
                    } else {
                        pieLabels3[i] = this.getDisEngagedText();
                    }

                }

                legendMap.put(letter + "text", pieLabels1[i]);
                legendMap.put(letter + "subText", pieLabels2[i] + " " + pieLabels3[i]);

            }


        }

        return legendMap.get(key);
    }

    /*
     * genrates name for the miro chart ;
     *
     */
    private String getChartName() {

        return mr.getMiroReportName() + ".jpg";
    }

    public void setLatentText(String latentText) {
        this.latentText = latentText;

    }

    public void setMiroGraphAdjustment(double miroGraphAdjustment) {
        this.miroGraphAdjustment = miroGraphAdjustment;

    }

    public BufferedImage getThumbnailPie(String title, MiroResponse mr, int thumbNailPieWidth, int thumbNailPieHeight) throws Exception {

        if (mr == null) {
            log.error("The Miro response is null");
        }

        setupMr(mr);


        MiroPieChartGenerator pieChart = this.getMiroPieChart(title, "", false, false, true);
        Font defFont = TextTitle.DEFAULT_FONT;
        Font smallFont = new Font(defFont.getName(), defFont.getStyle(), 10);

        pieChart.setTitleFont(smallFont);
        return pieChart.getThumbnailPie(thumbNailPieWidth, thumbNailPieHeight);


    }

    public void setTeamMapData(TeamMapDTO teamMapDTO, MiroResponse mr) {

        if (mr == null) {
            log.error("The Miro response is null");
        }

        this.setupMr(mr);

        String[] resultLetters = mr.getResultLetters();
        int[] results = mr.getResults();
        boolean[] attached = mr.getResultsAttached();

        String leadingModeDesc = (String) modes.get(resultLetters[0]);
        String secondaryModeDesc = (String) modes.get(resultLetters[1]);

        teamMapDTO.setLeadingModeText(leadingModeDesc);
        teamMapDTO.setLeadingMode(resultLetters[0]);

        if (attached[1]) {
            teamMapDTO.setSecondaryModeText(secondaryModeDesc);
            teamMapDTO.setSecondaryMode(resultLetters[1]);
        }


    }


    public String getReportFilePath(MiroResponse miroResponse, Long version) {

        return this.baseDirectory.getAbsolutePath() + "/out/" + miroResponse.getMiroReportName(version) + ".pdf";

    }
}
