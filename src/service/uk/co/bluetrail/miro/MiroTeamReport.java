package uk.co.bluetrail.miro;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.serviceDTO.TeamMapDTO;

import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.map.LinkedMap;

public class MiroTeamReport {

	private final Log log = LogFactory.getLog(MiroTeamReport.class);
	private MiroTeam miroTeam;
	private File baseDirectory;
	private Setting miroLetters;
	private String[] dynamicTensionDefaults;  
	private MiroReportFileGenerator miroReportFileGenerator;
	private Map teamTotals;
	private ArrayList<MiroTeamResult> sortedTeamTotals;
	private List teamResults;

	private Map<String,Integer> leadingTotals ;
	private Map<String,Integer> secondaryTotals ;
	private MiroReportLevel miroLevels;

	private DynamicTension[] dynamicTensions ;
	private HashMap<String,Double> startChartTotals;
	
	
	

	public void setResults(List teamResults) {
		this.teamResults = teamResults;
		
	}
	
	public List getTeamResults() {
		return this.teamResults;
	}
	
	
	/**
	 * @return the miroLetters
	 */
	public Setting getMiroLetters() {
		
		return miroLetters;
		
		
	}

	/**
	 * @param miroLetters the miroLetters to set
	 */
	public void setMiroLetters(Setting miroLetters) {
		this.miroLetters = miroLetters;
	}

	public MiroTeamReport(File baseDirectory,Setting miroLetters,Setting miroLevelsSetting, Setting dynamicTensionDefaults) {
		
		if(baseDirectory == null) {
			throw new RuntimeException("baseDirectory constructor vars is null");
		} 
			
		if(miroLetters == null) {
			throw new RuntimeException("miroLetter constructor var is null");
		}
		
		if(miroLevelsSetting == null) {
			throw new RuntimeException("miroLevels constructor var is null");
		}
		if(dynamicTensionDefaults == null) {
			throw new RuntimeException("dynamicTensionDefaults constructor var is null");
		}
		
		
		
		this.miroLetters = miroLetters;
		this.baseDirectory = baseDirectory;
		this.miroLevels = new MiroReportLevel(miroLevelsSetting.getSettingValues());
		
		this.dynamicTensionDefaults  = dynamicTensionDefaults.getSettingValues();
		
			
		
		this.dynamicTensions = new DynamicTension[4];
		dynamicTensions[0] = new DynamicTension('N','S');
		dynamicTensions[1] = new DynamicTension('T','F');
		dynamicTensions[2] = new DynamicTension('D','O');
		dynamicTensions[3] = new DynamicTension('E','A');
		
		
	}
	
	public void generateReport(MiroTeam miroTeam) throws Exception {
		this.miroTeam = miroTeam;
		
		if(miroTeam == null) {
			log.error("The miroTeamReportis null");
		}
		
		calculateResultsTable();
		
	
		

		//this.generateTeamBarChart();
		//this.generateTeamResultsGraphic();
		this.generateTeamStarChart();
		this.generateXMLReportFile();   
		MiroReportPDFGenerator.generatePDF(this.baseDirectory, this.miroTeam.getMiroTeamNameFileName(""));
		
	}
	

	private void generateTeamStarChart() throws Exception {
		
		MiroStarChart msc = new MiroStarChart(this.baseDirectory, Color.BLACK);
		
		int[] v = new int[8];
		
		v[0] = (int) this.startChartTotals.get("N").doubleValue();
		v[1] = (int) this.startChartTotals.get("E").doubleValue();
		v[2] = (int) this.startChartTotals.get("F").doubleValue();
		v[3] = (int) this.startChartTotals.get("O").doubleValue();
		v[4] = (int) this.startChartTotals.get("S").doubleValue();
		v[5] = (int) this.startChartTotals.get("A").doubleValue();
		v[6] = (int) this.startChartTotals.get("T").doubleValue();
		v[7] = (int) this.startChartTotals.get("D").doubleValue();
		
		msc.createChart(this.miroTeam.getMiroTeamNameFileName("")+"_star_chart.png", v );
		
	}

	private void generateXMLReportFile() throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		
		log.info("generateXMLReportFile");
		
		List<MiroPage> pages = new ArrayList<MiroPage>();
		Map<String, String> variables = new HashMap<String, String>();
		// Image Map
		Map<String, String> imgNames = new HashMap<String, String>();
		
		
		imgNames.put("home_page_banner_img", this.baseDirectory.getAbsolutePath() + "/images/miroteamreport/team_report_banner_image.png");
		imgNames.put("team_chart_table_img", this.baseDirectory.getAbsolutePath() + "/images/miroteamreport/team_chart_table_place_holder.png");
		imgNames.put("team_radar_chart_img", this.baseDirectory.getAbsolutePath() + "/out/"+ this.miroTeam.getMiroTeamNameFileName("")+"_star_chart.png");
		imgNames.put("team_bar_chart_img", this.baseDirectory.getAbsolutePath() + "/images/miroteamreport/team_bar_chart_place_holder.png");
		
		
		
		
		
		imgNames.put("imgU1", this.baseDirectory.getAbsolutePath() + "/images/" + "U1.png");
		imgNames.put("imgU3", this.baseDirectory.getAbsolutePath() + "/images/" + "U3.png");
		imgNames.put("imgU2", this.baseDirectory.getAbsolutePath() + "/images/"  + "U2.png");
		imgNames.put("imgU6", this.baseDirectory.getAbsolutePath() + "/images/"  + "U6.png");
	   
		
		
		
		buildReportPageList(pages,variables,imgNames);
		
		//Image Map
		
		variables.put("id", miroTeam.getId().toString());
		variables.put("prac", miroTeam.getPractitionerName());
		
		variables.put("team_report_name" , miroTeam.getMiroTeamName());
		variables.put("reportFileName" , miroTeam.getMiroTeamNameFileName(""));
		
		

		int vName = 2;
		
		if(!miroTeam.getCompany().trim().equals("")){
			variables.put("v"+vName++, miroTeam.getCompany());
		}
		
		
		String[] alines = miroTeam.getPractitionerAddress();
		for(int i = 0 ; i < alines.length;i++){
			if(alines[i] != null && !alines[i].trim().equals("")){
				variables.put("v"+vName++, alines[i]);
			}
			variables.put("v"+vName++, " ");
		}
		
		
		if(!miroTeam.getPractitionerTelNo().trim().equals("")){
			variables.put("v"+vName++, "Tel: "+  miroTeam.getPractitionerTelNo());
		}
		
		if(!miroTeam.getPractitionerEmail().trim().equals("")){
			
			variables.put("v"+vName++,"Email: "+ miroTeam.getPractitionerEmail());
			
		}
		
		if(!miroTeam.getWebaddress().trim().equals("")){
			variables.put("v"+vName++,"Web: "+miroTeam.getWebaddress());
		}
		
		
		
		
		//imgNames.put("graph", this.baseDirectory.getAbsolutePath() + "/out/" +this.getChartName());
		
		
		
		
		
		if(miroReportFileGenerator==null){
			miroReportFileGenerator = new MiroReportFileGenerator(this.baseDirectory,"miroteamreportsource.xhtml");
		}
		miroReportFileGenerator.generate(pages, variables, imgNames);
		
		
	}
	
	/*
	 * builds a list of pages to be added and creates a list of variables to replace values in the final document
	 * this is a change to getpagelist in the original report as we needed to build the variables list as we went.
	 */
	public void  buildReportPageList(List<MiroPage> pages,Map<String, String> variables, Map<String, String> imgNames) {
		
		MiroPageElement[]pageItems = null;
		
	
		
		//add home page 
		pages.add(MiroPage.create("homepage"));
		//add intro text
		pages.add(MiroPage.create("intro_text_page"));
		
		MiroPage individualPiePage = new MiroPage();
		List members = this.getTeamResults();
		Iterator itr = members.iterator();
		while(itr.hasNext()) {
			TeamMapDTO dto = (TeamMapDTO) itr.next();
	
			
			MiroResponse mr = dto.getMiroResponse();
			
			String resultLetters[] = mr.getResultLetters();
			
			String idKey = null;
			String pieKey = null;
			String nameKey = null;
			String idSuffix = "pie_and_bullets_";
			MiroPageElement mpe = null;
			
			//addname
			nameKey = idSuffix + "title";
			mpe = new MiroPageElement(nameKey,mr.getTestId().toString());
			individualPiePage.add(mpe );
			variables.put(idSuffix + "name_" + mpe.getSuffix(), mr.getFullName());
			
			
			//add the pie
			if(mr.is2ndEngaged()) {
				idKey = idSuffix+resultLetters[0]+"-"+resultLetters[1];
				pieKey = idKey + "_pie_" ;
		
				mpe = new MiroPageElement(idKey,mr.getTestId().toString());
				imgNames.put(pieKey+mpe.getSuffix(), getPieImage(mr));
				
			} else {
				idKey = idSuffix+resultLetters[1];
				pieKey = idKey + "_pie_" ;
				
				mpe = new MiroPageElement(idKey,mr.getTestId().toString());
				imgNames.put(pieKey+mpe.getSuffix(), getPieImage(mr));
				
			}
			individualPiePage.add(mpe);
			
		}
		pages.add(individualPiePage);
		
		MiroPage teamPiePage = new MiroPage();
		
		//add the team pie 
		teamPiePage.add(new MiroPageElement("team_chart"));
		
		//add the team results coloured box below
		teamPiePage.add(new MiroPageElement("team_chart_table"));
		imgNames.put("team_chart_img",getTeamMapImage(this.miroTeam));
		
		//add Team descriptors.
		for(int i = 0 ; i < 4 ; i++) {
		
			String mode = this.getMode(i);
			String modeLevel = this.getModeLevel(i);
			String divKey = "M" + mode + (i+1) + modeLevel ;
			teamPiePage.add(new MiroPageElement(divKey));
			
		}
		
		pages.add(teamPiePage);
		
		MiroPage teamBulletsPage = new MiroPage();
		teamBulletsPage.add(new MiroPageElement("team_bar_chart"));
	
		
		//Team descriptor Bullet points.
		for(int i = 0 ; i < 4 ; i++) {
			
			String mode = this.getMode(i);
			String modeLevel = this.getModeLevel(i);
			String divKey = "B" + mode + (i+1) + modeLevel ;
		
			teamBulletsPage.add(new MiroPageElement(divKey));
			
			
		}
		
		pages.add(teamBulletsPage);
		
		//dynamicTensions
		MiroPage dynamicTensionPage = new MiroPage();
		dynamicTensionPage.add(new MiroPageElement("team_radar_chart"));
		

		for(int i = 0 ; i < this.dynamicTensions.length; i ++) {
			
			
			String divKey = this.dynamicTensions[i].toString() + getBalanceValue(this.dynamicTensions[i]);
			dynamicTensionPage.add(new MiroPageElement(divKey));
			
		}
		
		
		
		pages.add(dynamicTensionPage);
		
		
		
		//comms bit 
		MiroPage contextBulletsPage = new MiroPage();
		
		contextBulletsPage.add(new MiroPageElement("contextBulletPageTitle"));
		
		contextBulletsPage.add(new MiroPageElement("commsBulletTitle"));
		
		//Team descriptor Bullet points.
		for(int i = 0 ; i < 4 ; i++) {
			
			String mode = this.getMode(i);
			String modeLevel = this.getModeLevel(i);
			String divKey = "C" + mode + (i+1) + modeLevel ;
			contextBulletsPage.add(new MiroPageElement(divKey));
		}
	
		contextBulletsPage.add(new MiroPageElement("decisonBulletTitle"));
		
		//Team descriptor Bullet points.
		for(int i = 0 ; i < 4 ; i++) {
			
			String mode = this.getMode(i);
			String modeLevel = this.getModeLevel(i);
			String divKey = "D" + mode + (i+1) + modeLevel ;
			contextBulletsPage.add(new MiroPageElement(divKey));
		}
		
		contextBulletsPage.add(new MiroPageElement("relationshipsBulletTitle"));
		
		//Team descriptor Bullet points.
		for(int i = 0 ; i < 4 ; i++) {
			
			String mode = this.getMode(i);
			String modeLevel = this.getModeLevel(i);
			String divKey = "R" + mode + (i+1) + modeLevel ;
			contextBulletsPage.add(new MiroPageElement(divKey));
		}
		
		contextBulletsPage.add(new MiroPageElement("dealingChangeBulletTitle"));
		
		//Team descriptor Bullet points.
		for(int i = 0 ; i < 4 ; i++) {
			
			String mode = this.getMode(i);
			String modeLevel = this.getModeLevel(i);
			String divKey = "H" + mode + (i+1) + modeLevel ;
			contextBulletsPage.add(new MiroPageElement(divKey));
		}
		
		pages.add(contextBulletsPage);
		
		
		
//		practitiioner stuff
		pages.add(MiroPage.create("practitioner_details"));
		
		
		
	}
	
	
	

	private String getBalanceValue(DynamicTension dt) {
		
		
		double larger = 0.00 ;
		double smaller = 0.00 ;
		char larger_mode = ' ';
		
		if(this.startChartTotals.get(dt.getf()) > this.startChartTotals.get(dt.gets())) {
			larger = this.startChartTotals.get(dt.getf());
			larger_mode = dt.f;
			smaller = this.startChartTotals.get(dt.gets());
		} else {
			larger = this.startChartTotals.get(dt.gets());
			larger_mode = dt.s;
			smaller = this.startChartTotals.get(dt.getf());
		}
		
		/*
		If larger plus smaller = 0 then Z   (both = zero)  

		If lager plus smaller = less than 20 then V   

		If larger minus smaller = less than 25% of larger then B

		If larger minus smaller = more than 24% of larger then U-larger  

		If larger minus smaller = 100 then C larger
*/

		if(larger==0.00 && smaller ==0.00) {
			return "Z";
		}
		
		if(larger+smaller < Double.parseDouble(this.dynamicTensionDefaults[0])) {
			return "V" ;
		}
		
		if(larger-smaller < (larger*Double.parseDouble(this.dynamicTensionDefaults[1]))) {
			return "B";
		}
		
		if(larger-smaller > (larger*Double.parseDouble(this.dynamicTensionDefaults[2]))) {
			return "U" + larger_mode;
		}
		
		if(larger-smaller == 100.00) {
			return "C" + larger;
		}

		throw new RuntimeException("getBalanceValue() Exception - should not be here");
		
	}
	
	private MiroTeamResult getResultForLetter(String letter) {
		
		MiroTeamResult result = null;
		
		for(int i = 0 ; i <this.sortedTeamTotals.size(); i++) {
			result = this.sortedTeamTotals.get(i);
			
			if(result.key.equals(letter)) {
				return result;
			}
		}
		
		throw new RuntimeException("Could not find letter " + letter);
	}

	private String getPieImage(MiroResponse mr) {
			
			return this.baseDirectory.getAbsolutePath() + "/out/"  + mr.getMiroReportName()+".jpg";
		
	}
	
	private String getTeamMapImage(MiroTeam mt) {
		return this.baseDirectory.getAbsolutePath() + "/out/"  + mt.getMiroTeamNameFileName(Constants.JPEG);
	
		
	}

	/**
	 * gets the mode for the specified index position will return things like O D E A  .. 
	 * 
	 * @param index
	 * @return
	 */
	public String getMode(int index)	{
		calculateResultsTable();
		MiroTeamResult mtr =  this.sortedTeamTotals.get(index);
		return mtr.key;
	
	}
	
	
	/**
	 * gets the modelevel for the specified index postion will return things like H M L A  .. 
	 * 
	 * @param index
	 * @return
	 */
	public String getModeLevel(int index)	{
		calculateResultsTable();
		MiroTeamResult mtr = this.sortedTeamTotals.get(index);
		return this.miroLevels.getLevel(mtr.value);
		
		
	}
	
	/**
	 * goes thru the table of results and sums up the totals etc
	 */
	private void calculateResultsTable() {
		
		if(this.leadingTotals!=null) {
			//we have claculated em.  so quit
			return ;
		}
		
		this.leadingTotals = new HashMap<String,Integer>();
		this.secondaryTotals = new HashMap<String,Integer>();
		
		String[] miroLetters = this.miroLetters.getSettingValues();
		
		//initialise the maps so we have a zero is there are no results for a letter
		for(int i = 0 ; i < miroLetters.length;i++) {
			this.leadingTotals.put(miroLetters[i], new Integer(0));
			this.secondaryTotals.put(miroLetters[i], new Integer(0));
		}
		
		
		
		//count up each letter to get basic totals
		for(int i = 0 ; i < this.getTeamResults().size();i++) {
			TeamMapDTO dt = (TeamMapDTO) this.getTeamResults().get(i);
			Integer lm = this.leadingTotals.get(dt.getLeadingMode()) ; 
			this.leadingTotals.put(dt.getLeadingMode(), ++lm);
			Integer sm = this.secondaryTotals.get(dt.getSecondaryMode()) ; 
			this.secondaryTotals.put(dt.getSecondaryMode(), ++sm);
		}

		//loop over map, calculate percentages 
		this.teamTotals = new LinkedMap();
		int teamTotalTotals = 0 ;
	
		for(int i = 0 ; i < miroLetters.length;i++) {
			Integer lm = this.leadingTotals.get(miroLetters[i]) ;
			Integer sm = this.secondaryTotals.get(miroLetters[i]);
			int teamTotal = (lm*2) + sm ;
			this.teamTotals.put(miroLetters[i], teamTotal);
			teamTotalTotals = teamTotalTotals + teamTotal;
		}
		
		ArrayList<MiroTeamResult> tempSort = new ArrayList<MiroTeamResult>();
		
		//finally make the values in the map %
		for(int i = 0 ; i < miroLetters.length;i++) {
			
			String ml = miroLetters[i];
			
			Integer teamTotal = (Integer) this.teamTotals.get(ml) ;
			
			double pc = 0.0;
			
			if(teamTotal.intValue()>0) {
				pc = (teamTotal.doubleValue()/teamTotalTotals)*100;
				tempSort.add(new MiroTeamResult(ml, pc));
			} else {
				tempSort.add(new MiroTeamResult(ml, 0.00));
			}
			
		
			
		}
		
		int count = tempSort.size();
		
		Collections.sort(tempSort);
		this.sortedTeamTotals = tempSort;
		
	this.startChartTotals = new HashMap<String,Double>();
	Iterator itr =  this.sortedTeamTotals.iterator();
	while(itr.hasNext()) {
		MiroTeamResult mtr = (MiroTeamResult) itr.next();
		this.startChartTotals.put(mtr.key, mtr.value);
	}
	
	//N	d+e
	//S	a+o
	//T	d+a
	//F	e+o
	
	//now make sure the values from the star chart are there;
	//hard wired letters - these are not likely to change from now on so fuck it.
	this.startChartTotals.put("N",this.startChartTotals.get("D") + this.startChartTotals.get("E"));
	this.startChartTotals.put("S",this.startChartTotals.get("A") + this.startChartTotals.get("O"));
	this.startChartTotals.put("T",this.startChartTotals.get("D") + this.startChartTotals.get("A"));
	this.startChartTotals.put("F",this.startChartTotals.get("E") + this.startChartTotals.get("O"));
		
	
		
		
		
	}

	public void buildReportPageList(MiroTeam miroTeam,List<MiroPage> pages,Map<String, String> variables,Map<String, String> imgNames) {
		
		this.miroTeam = miroTeam;
		
		buildReportPageList(pages,variables,imgNames);
		
	}

	public void setTeam(MiroTeam mt) {
		this.miroTeam = mt;
		
	}

	public void generateReport(MiroTeam mt, List teamMapData) throws Exception {
		
		this.setResults(teamMapData);
		this.generateReport(mt);
		
	}

	
	
	

}

class DynamicTension {
	
	public char f;
	public char s;

	public DynamicTension(char f, char s) {
		this.f = f;
		this.s = s;
	}
	
	public String gets() {
		return this.s+"";
	}

	public String getf() {
		return this.f+"";
	}
	
	public String toString() {
		return f+""+s;
	}
	
}





class MiroTeamResult implements Comparable<MiroTeamResult> {

	
	public String key;
	public double value;


	public MiroTeamResult(String key, double v) {
		this.key = key ;
		this.value = v;
	}
	
	
	public int compareTo(MiroTeamResult mtr) {
		final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
		
		if(mtr.value==this.value) {
	
			return mtr.key.compareTo(this.key);
			
		}
		
		if(mtr.value>this.value) {
			return AFTER;
		}
		
		return BEFORE;
		
	}

	
	
	
	
	
}

