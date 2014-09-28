package uk.co.bluetrail.miro;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.title.TextTitle;
import org.xml.sax.SAXException;
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
 * 
 * generates miro report
 * 
 * @author Richard
 *
 */
public class MiroReport {

    private final Log log = LogFactory.getLog(MiroReport.class);

    File baseDirectory;
	MiroResponse mr ;
	HashMap modes = null;
	String[] labels2 = null;
	String engagedText = null;
	String disEngagedText = null;
	HashMap colors = null;
	MiroReportFileGenerator miroReportFileGenerator ;

	private int engagedScore;
	private int excessScore;
	private int latentScore;

	private String latentText;
	private String excessText;

	private double miroGraphAdjustment;
	private HashMap<String,String> subTitles;


    public void setSubTitles(HashMap subTitles) {
        this.subTitles = subTitles;
    }
	
	/**
	 * @hibernate.property 
	 * @return the excessText
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
	 * @hibernate.property 
	 * @return the latentText
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

	public MiroReport(File baseDirectory, int engagedScore,int excessScore, int latentScore){
		this.baseDirectory = baseDirectory;
		this.engagedScore = engagedScore;
		this.excessScore = excessScore;
		this.latentScore = latentScore ;
	}
	
	public boolean generateReport(MiroResponse mr,Long reportVersion) throws Exception{
	
		if(mr == null) {
			log.error("The Miro response is null");
            return false;
		}


        if(reportVersion==MiroResponse.Survey_id_Mirov10) {
           return this.generateReportV10(mr);
        }

        if(reportVersion==MiroResponse.Survey_id_Mirov11) {
            return this.generateReportV11(mr);
        }

        throw new MiroException("Cannot produces a report for version " + reportVersion);

	}


    private boolean generateReportV11(MiroResponse mr) throws Exception{

        if(mr == null) {
            log.error("The Miro response is null");
        }

        setupMr(mr);

        if(!mr.supportsVersion(MiroResponse.Survey_id_Mirov11)) {
            throw new MiroException(mr.getMiroReportName() + "does not support version v11");
        }

        log.debug("Before Chart " + mr.toString());
        this.generateChart(MiroResponse.Survey_id_Mirov11);
        log.debug("Before XMLReportFile " + mr.toString());
        this.generateXMLReportFile();
        log.debug("Before PDF " + mr.toString());
        this.generateXSLReportFile(mr.getMiroReportName());
        MiroReportPDFGenerator.generatePDF(this.baseDirectory,mr,MiroResponse.Survey_id_Mirov11);

        return true;

    }

    private boolean generateReportV10(MiroResponse mr) throws Exception{

        if(mr == null) {
            log.error("The Miro response is null");
            return false ;
        }

        setupMr(mr);

        if(!mr.supportsVersion(MiroResponse.Survey_id_Mirov10)) {
            throw new MiroException(mr.getMiroReportName() + "does not support version v10");
        }

        log.debug("Before Chart " + mr.toString());
        this.generateChart(MiroResponse.Survey_id_Mirov10);
        log.debug("Before XMLReportFile " + mr.toString());
        this.generateXMLReportFile();
        log.debug("Before PDF " + mr.toString());
        this.generateXSLReportFile(mr.getMiroReportName());
        MiroReportPDFGenerator.generatePDF(this.baseDirectory, mr, MiroResponse.Survey_id_Mirov10);

        return true;

    }




    private void generateXSLReportFile(String reportName) throws Exception{

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};

        HashMap<String,String> strings = new HashMap<String, String>();
        strings.put("#PAGENUMBER",mr.getVariable("PAGENUMBER"));
        strings.put("#DATEOFREPORT",months[month] + " " + year);

        MiroXSLFileGenerator.generate(this.baseDirectory,"miro2fo.xsl",reportName,strings);

    }

    private void setupMr(MiroResponse mr) {
		this.mr = mr;
		this.mr.setEngagedScore(this.engagedScore);
		this.mr.setExcessScore(this.excessScore);
		this.mr.setLatentScore(this.latentScore);
		
	}

	private void generateChart(long reportVersion) {

        this.mr = mr;

        String subTitle = "";
        if(this.subTitles != null && reportVersion >= MiroResponse.Survey_id_Mirov11)  {

            String subTitleKey = mr.getExtraIntroMappingKey() ;
            subTitle = subTitles.get(subTitleKey) ;

            if(subTitle == null) {
                throw new MiroException("Could not find subTitle for " + subTitleKey);
            }

        }

		MiroPieChartGenerator pieChart = this.getMiroPieChart("Your MiRo Results Chart",subTitle,false, true,false)  ;
		pieChart.createPie(this.baseDirectory.getAbsolutePath()+"/out/",this.getChartName());

	}
	
	public void generatePieChart(MiroResponse mr, String title,boolean hideLegend, boolean hideTitle, boolean hideSubTitle) {

		this.mr = mr;

        String subTitle = "";
        if(this.subTitles != null && mr.extroIntroStrata != null)  {
            if(this.subTitles.get(mr.extroIntroStrata)!=null) {
                subTitle  = (String) this.subTitles.get(mr.extroIntroStrata) ;
            }
        }



		MiroPieChartGenerator pieChart = this.getMiroPieChart(title, subTitle,hideLegend,  hideTitle,hideSubTitle);
		pieChart.createPie(this.baseDirectory.getAbsolutePath()+"/out/",this.getChartName());
	}

	private MiroPieChartGenerator getMiroPieChart(String pieTitle, String pieSubtitle,boolean hideLegend, boolean hideTitle,boolean hideSubTitle) {
		
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
		
		for(int i = 0 ; i < resultsLen ; i++) {
		
			pieLabels1[i] = (String) modes.get(resultLetters[i]);   
			
			
			if(attached[i]){
				pieExplode[i] = false;
				if(mr.isExcess(results[i])){
					pieLabels3[i] = this.getExcessText();	
				} else {
					pieLabels3[i] = this.getEngagedText();	
				}
			} else {
				pieExplode[i] = true;
				if(mr.isLatent(results[i])) {
					pieLabels3[i] = this.getLatentText();
				} else {
					pieLabels3[i] = this.getDisEngagedText();
				}
				
			}
			
			
			pieColors[i] = (Color) colors.get(resultLetters[i]);   
			
		}
		
		
		return new MiroPieChartGenerator(pieTitle, pieSubtitle,mr.getResults(this.miroGraphAdjustment),pieLabels1, pieLabels2, pieLabels3, pieExplode, pieColors, hideLegend, hideTitle,hideSubTitle );
		
		
		
	}

	/**
	 * @throws TransformerException 
	 * @throws TransformerFactoryConfigurationError 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * 
	 */
	private void generateXMLReportFile() throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		
			log.info("Generating " + mr.getFullName() + "_" + mr.getTestId());
		
			List<MiroPage> pages = mr.getReportPageList();
			
			//Image Map
			Map<String, String> variables = new HashMap<String, String>();
			variables.put("id", mr.getTestId().toString());
			variables.put("firstname", mr.getFirstName());
			variables.put("lastname", mr.getLastName());
			variables.put("name", mr.getFullName());
			variables.put("name2", mr.getFullName());
			variables.put("v1", mr.getPractitionerName());
			variables.put("reportFileName" , mr.getMiroReportName());
            variables.put("toc1",mr.getVariable("toc1"));
            variables.put("toc1",mr.getVariable("toc1"));
            variables.put("toc2",mr.getVariable("toc2"));
            variables.put("toc3",mr.getVariable("toc3"));
            variables.put("toc4",mr.getVariable("toc4"));
            variables.put("toc5",mr.getVariable("toc5"));

			StringBuffer sb = new StringBuffer();

			int vName = 2;
			
			if(!mr.getCompany().trim().equals("")){
				variables.put("v"+vName++, mr.getCompany());
			}
			
			String[] alines = mr.getPractitionerAddress();
			
			
			for(int i = 0 ; i < alines.length;i++){
				if(alines[i] != null && !alines[i].trim().equals("")){
					variables.put("v"+vName++, alines[i]);
				}
				variables.put("v"+vName++, " ");
				
			}
			
			
			
			
			if(!mr.getPractitionerTelNo().trim().equals("")){
				variables.put("v"+vName++, "Tel: "+  mr.getPractitionerTelNo());
			}
			
			if(!mr.getPractitionerEmail().trim().equals("")){
				
				variables.put("v"+vName++,"Email: "+ mr.getPractitionerEmail());
				
			}
			
			if(!mr.getWebaddress().trim().equals("")){
				variables.put("v"+vName++,"Web: "+mr.getWebaddress());
			}
			
			
			
			// Image Map
			Map<String, String> imgNames = new HashMap<String, String>();
			imgNames.put("graph", this.baseDirectory.getAbsolutePath() + "/out/" +this.getChartName());
			imgNames.put("imgU1", this.baseDirectory.getAbsolutePath() + "/images/" + "U1.png");
			imgNames.put("imgU3", this.baseDirectory.getAbsolutePath() + "/images/" + "U3.png");
			imgNames.put("imgU2", this.baseDirectory.getAbsolutePath() + "/images/"  + "U2.png");
			imgNames.put("imgU6", this.baseDirectory.getAbsolutePath()+ "/images/"  + "U6.png");
		
			
			if(miroReportFileGenerator==null){
				miroReportFileGenerator = new MiroReportFileGenerator(this.baseDirectory);
			}
			miroReportFileGenerator.generate(pages, variables, imgNames);
			
		
	}

	/*
	 * genrates name for the miro chart ;
	 * 
	 */	
	private String getChartName() {
		
		return mr.getMiroReportName()+".jpg";
	}

	public void setLatentText(String latentText) {
		this.latentText = latentText;
		
	}

	public void setMiroGraphAdjustment(double miroGraphAdjustment) {
		this.miroGraphAdjustment = miroGraphAdjustment;
		
	}

	public BufferedImage getThumbnailPie(String title,MiroResponse mr, int  thumbNailPieWidth, int thumbNailPieHeight)  throws Exception {
		  
		if(mr == null) {
			log.error("The Miro response is null");
		}

		setupMr(mr);
		
		
		MiroPieChartGenerator pieChart = this.getMiroPieChart(title,"",false,false,true)  ;
		Font defFont = TextTitle.DEFAULT_FONT;
		Font smallFont = new Font(defFont.getName(),defFont.getStyle(),10);
				
		pieChart.setTitleFont(smallFont);
		return pieChart.getThumbnailPie(thumbNailPieWidth, thumbNailPieHeight);
		
		
		
	}
	
	public void  setTeamMapData(TeamMapDTO teamMapDTO,MiroResponse mr) {
		
		if(mr == null) {
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
		
		if(attached[1]) {
			teamMapDTO.setSecondaryModeText(secondaryModeDesc);
			teamMapDTO.setSecondaryMode(resultLetters[1]);
		} 
		
		
		
	}


    public String getReportFilePath(MiroResponse miroResponse, Long version) {

        return this.baseDirectory.getAbsolutePath()+"/out/"+miroResponse.getMiroReportName(version)+".pdf";

    }
}
