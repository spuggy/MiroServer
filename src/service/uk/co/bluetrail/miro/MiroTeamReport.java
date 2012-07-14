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
	private HashMap<String,Double> starChartTotals;
	
	
	

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
		
	
		

		this.generateTeamBarChart();
		//this.generateTeamResultsGraphic();
		this.generateDynamicContent();
		this.generateTeamSpiderWebChart();
		this.generateXMLReportFile();   
		MiroReportPDFGenerator.generatePDF(this.baseDirectory, this.miroTeam.getMiroTeamNameFileName(""),"miro2fo-team.xsl");
		//rem out .docx generation for now.
		//MiroReportDocxGenerator.generateDocx(this.baseDirectory, this.miroTeam.getMiroTeamNameFileName(""));
	}
	

	private void generateDynamicContent() throws IOException {
		
		
		
		MiroDynamicContentFileGenerator f = new MiroDynamicContentFileGenerator(this.baseDirectory,this.miroTeam.getMiroTeamNameFileName("_dynamic.xhtml"));
		
		if(this.miroTeam.getCommentary()==null || this.miroTeam.getCommentary().equals("")) {
			//dont add blank stuff it freaks the pdf generator out.
		} else {
			
			f.addContent("commentary","<h3>Practitioner Commentary</h3>" + this.miroTeam.getCommentary());
		}
		
		f.addContent("dummy","dummy");
		
		f.genertate();
		
	}

	private void generateTeamSpiderWebChart() throws Exception {
		
		MiroSpiderWebChart mswc = new MiroSpiderWebChart(this.baseDirectory, Color.BLACK);
		
		int[] v = new int[8];
		
		v[0] = (int) this.starChartTotals.get("N").doubleValue();
		v[1] = (int) this.starChartTotals.get("E").doubleValue();
		v[2] = (int) this.starChartTotals.get("F").doubleValue();
		v[3] = (int) this.starChartTotals.get("O").doubleValue();
		v[4] = (int) this.starChartTotals.get("S").doubleValue();
		v[5] = (int) this.starChartTotals.get("A").doubleValue();
		v[6] = (int) this.starChartTotals.get("T").doubleValue();
		v[7] = (int) this.starChartTotals.get("D").doubleValue();
		
		//TODO should facotor these labels out some where
		String[] labels = {"Intuition","Energising","Feeling","Organising","Sensing","Analysing","Thinking","Driving"};
		
		
		mswc.createChart(this.miroTeam.getMiroTeamNameFileName("")+"_star_chart.png", v , labels);
		
	}

	private void generateTeamBarChart() throws Exception {
		
		MiroTeamBarChart mtbc = new MiroTeamBarChart(this.baseDirectory);
		
		double[] v = new double[4];
		HashMap<String,String> desc = new HashMap<String,String>();
		HashMap<String,Color> colors = new HashMap<String,Color>();
		
		//TODO should facotor these labels out some where
		String[] l = new String[4];
		desc.put("E","Energising");
		desc.put("O", "Organising");
		desc.put("A", "Analysing");
		desc.put("D","Driving");
		
		Color[] c = new Color[4];
		colors.put("E",Color.YELLOW);
		colors.put("O", Color.GREEN);
		colors.put("A", Color.BLUE);
		colors.put("D",Color.RED);
		
		String[] levelLabels = new String[4] ;
		levelLabels[0] = "" ;
		levelLabels[1] = "High";
		levelLabels[2] = "Medium";
		levelLabels[3] = "Low";   //ignore absent cos it gets in the way
		
		double[] barLevels = new double[4];
		barLevels[0] = this.miroLevels.getHigher("h");
		barLevels[1] = this.miroLevels.getHigher("m");
		barLevels[2] = this.miroLevels.getHigher("l");
		barLevels[3] = 0.00;
		
		String[] order = {"D","E","O","A"};
		

		
		
		//driving, energising, organising, analysing
		
		//his.starChartTotals.put(mtr.key, mtr.value);
		
		MiroTeamResult mtr = null;
		
		for(int i = 0 ; i < 4 ; i++) {
			
			mtr = this.sortedTeamTotals.get(i);
			
			v[i] = this.starChartTotals.get(order[i]);
			l[i] = desc.get(order[i]);
			c[i] = colors.get(order[i]);
			
		}
		
		mtbc.createBarChart("",this.miroTeam.getMiroTeamNameFileName("")+"_bar_chart.png", v , l,c,barLevels,levelLabels);
		
	}

	
	
	private void generateXMLReportFile() throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		
		log.info("generateXMLReportFile");
		
		List<MiroPage> pages = new ArrayList<MiroPage>();
		Map<String, String> variables = new HashMap<String, String>();
		// Image Map
		Map<String, String> imgNames = new HashMap<String, String>();
		
		
		imgNames.put("home_page_banner_img", this.baseDirectory.getAbsolutePath() + "/images/miroteamreport/homepage.png");
		imgNames.put("team_chart_table_img", this.baseDirectory.getAbsolutePath() + "/images/miroteamreport/team_chart_table_place_holder.png");
		imgNames.put("team_radar_chart_img", this.baseDirectory.getAbsolutePath() + "/out/"+ this.miroTeam.getMiroTeamNameFileName("")+"_star_chart.png");
		imgNames.put("team_bar_chart_img", this.baseDirectory.getAbsolutePath() + "/out/"+ this.miroTeam.getMiroTeamNameFileName("")+"_bar_chart.png");
		
		
		
		
		imgNames.put("4dperformer", this.baseDirectory.getAbsolutePath() + "/images/miroteamreport/4DPerformer.jpg");
		imgNames.put("miromodel", this.baseDirectory.getAbsolutePath() + "/images/miroteamreport/miro-model.png" );
		imgNames.put("communications", this.baseDirectory.getAbsolutePath() + "/images/miroteamreport/communication.jpg" );
		imgNames.put("decisionmaking", this.baseDirectory.getAbsolutePath() + "/images/miroteamreport/decisionmaking.jpg" );
		imgNames.put("relationships", this.baseDirectory.getAbsolutePath() + "/images/miroteamreport/relationships.jpg" );
		imgNames.put("change", this.baseDirectory.getAbsolutePath() + "/images/miroteamreport/change.jpg" );
		
	
		imgNames.put("imgU2", this.baseDirectory.getAbsolutePath() + "/images/"  + "U2.png");
		imgNames.put("imgU6", this.baseDirectory.getAbsolutePath() + "/images/"  + "U6.png");
	   
		
		
		
		buildReportPageList(pages,variables,imgNames);
		
		//Image Map
		
		variables.put("id", miroTeam.getId().toString());
		variables.put("v1", miroTeam.getPractitionerName());
		
		variables.put("team_report_name" , miroTeam.getMiroTeamName());
		variables.put("reportFileName" , miroTeam.getMiroTeamNameFileName(""));
		
		
		System.out.println(variables.get("commentary"));
		

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
			miroReportFileGenerator = new MiroReportFileGenerator(this.baseDirectory,"miroteamreportsource.xhtml",this.miroTeam.getMiroTeamNameFileName("_dynamic.xhtml"));
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
		
		
		int maxPieCount = 3;
		int pieCount = 0;
		
		MiroPage individualPiePage = new MiroPage();
		individualPiePage.add(new MiroPageElement("pie_and_bullets_page_title"));
		
		
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
			
			pieCount++;
			
			
			
			individualPiePage.add(mpe);
			
			if(pieCount==maxPieCount) {
				pages.add(individualPiePage);
				individualPiePage = new MiroPage();
				pieCount=0;
			}
			
			
		}
		
		if(pieCount!=0) {
			pages.add(individualPiePage);
		}
		
		MiroPage teamMapPage = new MiroPage();
		
		//add the team pie 
		teamMapPage.add(new MiroPageElement("team_chart"));
		
		//add the team results coloured box below
		//teamPiePage.add(new MiroPageElement("team_chart_table"));
		imgNames.put("team_chart_img",getTeamMapImage(this.miroTeam));
		
		//add Team descriptors.
		for(int i = 0 ; i < 4 ; i++) {
		
			String mode = this.getMode(i);
			String modeLevel = this.getModeLevel(i);
			String divKey = "M" + mode + (i+1) + modeLevel ;
			teamMapPage.add(new MiroPageElement(divKey));
			
		}
		
		pages.add(teamMapPage);
		
		MiroPage teamBulletsPage = new MiroPage();
		teamBulletsPage.add(new MiroPageElement("team_bar_chart"));
	
		teamBulletsPage.add(new MiroPageElement("startlist"));
		//Team descriptor Bullet points.
		for(int i = 0 ; i < 4 ; i++) {
			
			String mode = this.getMode(i);
			String modeLevel = this.getModeLevel(i);
			String divKey = "B" + mode + (i+1) + modeLevel ;
		
			teamBulletsPage.add(new MiroPageElement(divKey));
			
			
		}
		teamBulletsPage.add(new MiroPageElement("endlist"));
		
		pages.add(teamBulletsPage);
		
		//dynamicTensions
		MiroPage dynamicTensionPage = new MiroPage();
		
		dynamicTensionPage.add(new MiroPageElement("team_radar_chart"));
		
		
		for(int i = 0 ; i < this.dynamicTensions.length; i ++) {
			
			String divHeading = this.dynamicTensions[i].toString()+"_heading";
			String divKey = this.dynamicTensions[i].toString() + getBalanceValue(this.dynamicTensions[i]);
			dynamicTensionPage.add(new MiroPageElement(divHeading));
			dynamicTensionPage.add(new MiroPageElement(divKey));
			
		}
		
		

		
		pages.add(dynamicTensionPage);
		
		
		
		//comms bit 
		MiroPage contextBulletsPage1 = new MiroPage();
		
		contextBulletsPage1.add(new MiroPageElement("contextBulletPageTitle"));
		
		contextBulletsPage1.add(new MiroPageElement("commsBulletTitle"));
		
		//Team descriptor Bullet points.
		for(int i = 0 ; i < 4 ; i++) {
			
			String mode = this.getMode(i);
			String modeLevel = this.getModeLevel(i);
			String divKey = "C" + mode + (i+1) + modeLevel ;
			contextBulletsPage1.add(new MiroPageElement(divKey));
		}
	
		contextBulletsPage1.add(new MiroPageElement("communication_footer"));
		
		
		
		contextBulletsPage1.add(new MiroPageElement("decisonBulletTitle"));
		
		//Team descriptor Bullet points.
		for(int i = 0 ; i < 4 ; i++) {
			
			String mode = this.getMode(i);
			String modeLevel = this.getModeLevel(i);
			String divKey = "D" + mode + (i+1) + modeLevel ;
			contextBulletsPage1.add(new MiroPageElement(divKey));
		}
		
		contextBulletsPage1.add(new MiroPageElement("decision_footer"));
		
		
		pages.add(contextBulletsPage1);
		
		MiroPage contextBulletsPage2 = new MiroPage();
		
		
		contextBulletsPage2.add(new MiroPageElement("relationshipsBulletTitle"));
		
		//Team descriptor Bullet points.
		for(int i = 0 ; i < 4 ; i++) {
			
			String mode = this.getMode(i);
			String modeLevel = this.getModeLevel(i);
			String divKey = "R" + mode + (i+1) + modeLevel ;
			contextBulletsPage2.add(new MiroPageElement(divKey));
		}
		
		contextBulletsPage2.add(new MiroPageElement("relationships_footer"));
		
		
		contextBulletsPage2.add(new MiroPageElement("dealingChangeBulletTitle"));
		
		//Team descriptor Bullet points.
		for(int i = 0 ; i < 4 ; i++) {
			
			String mode = this.getMode(i);
			String modeLevel = this.getModeLevel(i);
			String divKey = "H" + mode + (i+1) + modeLevel ;
			contextBulletsPage2.add(new MiroPageElement(divKey));
		}
		
		contextBulletsPage2.add(new MiroPageElement("change_footer"));
		
		
		pages.add(contextBulletsPage2);
		
		
		
//		practitiioner stuff and whats next
		MiroPage whatsNextPage = new MiroPage();
		
		whatsNextPage.add(new MiroPageElement("whatsnext"));
		
		if(!this.miroTeam.getCommentary().equals("")) {
			whatsNextPage.add(new MiroPageElement("commentary"));
		}
			
		whatsNextPage.add(new MiroPageElement("practitioner_details"));
		
		pages.add(whatsNextPage);
		
	}
	
	
	

	private String getBalanceValue(DynamicTension dt) {
		
		
		double larger = 0.00 ;
		double smaller = 0.00 ;
		char larger_mode = ' ';
		
		if(this.starChartTotals.get(dt.getf()) > this.starChartTotals.get(dt.gets())) {
			larger = this.starChartTotals.get(dt.getf());
			larger_mode = dt.f;
			smaller = this.starChartTotals.get(dt.gets());
		} else {
			larger = this.starChartTotals.get(dt.gets());
			larger_mode = dt.s;
			smaller = this.starChartTotals.get(dt.getf());
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
		
		if(larger > 0 && smaller == 0.00) {
			return "C" + larger_mode;
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
			try{
				Integer lm = this.leadingTotals.get(dt.getLeadingMode()) ; 
				this.leadingTotals.put(dt.getLeadingMode(), ++lm);
			} catch(Exception e) {
				//catch null modes
			}
			
			try {
				Integer sm = this.secondaryTotals.get(dt.getSecondaryMode()) ; 
				this.secondaryTotals.put(dt.getSecondaryMode(), ++sm);
			} catch(Exception e) {
				//catch null modes  that keith bloke
			}
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
		
	this.starChartTotals = new HashMap<String,Double>();
	Iterator itr =  this.sortedTeamTotals.iterator();
	while(itr.hasNext()) {
		MiroTeamResult mtr = (MiroTeamResult) itr.next();
		this.starChartTotals.put(mtr.key, mtr.value);
	}
	
	//N	d+e
	//S	a+o
	//T	d+a
	//F	e+o
	
	//now make sure the values from the star chart are there;
	//hard wired letters - these are not likely to change from now on so fuck it.
	this.starChartTotals.put("N",this.starChartTotals.get("D") + this.starChartTotals.get("E"));
	this.starChartTotals.put("S",this.starChartTotals.get("A") + this.starChartTotals.get("O"));
	this.starChartTotals.put("T",this.starChartTotals.get("D") + this.starChartTotals.get("A"));
	this.starChartTotals.put("F",this.starChartTotals.get("E") + this.starChartTotals.get("O"));
		
	
		
		
		
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

