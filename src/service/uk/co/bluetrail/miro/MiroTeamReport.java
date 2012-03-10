package uk.co.bluetrail.miro;

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
	private MiroReportFileGenerator miroReportFileGenerator;
	private Map teamTotals;
	private ArrayList<MiroTeamResult> sortedTeamTotals;
	private List teamResults;

	private Map<String,Integer> leadingTotals ;
	private Map<String,Integer> secondaryTotals ;
	private MiroReportLevel miroLevels;

	
	
	
	

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

	public MiroTeamReport(File baseDirectory,Setting miroLetters,Setting miroLevelsSetting) {
		
		if(baseDirectory == null) {
			throw new RuntimeException("baseDirectory constructor vars is null");
		} 
			
		if(miroLetters == null) {
			throw new RuntimeException("miroLetter constructor var is null");
		}
		
		if(miroLevelsSetting == null) {
			throw new RuntimeException("miroLevels constructor var is null");
		}
		
		this.miroLetters = miroLetters;
		this.baseDirectory = baseDirectory;
		this.miroLevels = new MiroReportLevel(miroLevelsSetting.getSettingValues());
	}
	
	public void generateReport(MiroTeam miroTeam) throws Exception {
		this.miroTeam = miroTeam;
		
		if(miroTeam == null) {
			log.error("The miroTeamReportis null");
		}

		//this.generateTeamChart();
		//this.generateTeamPies();
		//this.generateTeamBarChart();
		//this.generateTeamResultsGraphic();
		//this.generateTeamStarChart();
		this.generateXMLReportFile();   
		MiroReportPDFGenerator.generatePDF(this.baseDirectory, this.miroTeam.getMiroTeamName());
		
		
	}
	
	
	
	
	private void generateXMLReportFile() throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		
		log.info("generateXMLReportFile");
		
		List<MiroPage> pages = new ArrayList<MiroPage>();
		Map<String, String> variables = new HashMap<String, String>();
		// Image Map
		Map<String, String> imgNames = new HashMap<String, String>();
		
		buildReportPageList(pages,variables,imgNames);
		
		//Image Map
		
		variables.put("id", miroTeam.getId().toString());
		variables.put("prac", miroTeam.getPractitionerName());
		
		variables.put("team_report_name" , miroTeam.getMiroTeamName());
		variables.put("reportFileName" , miroTeam.getMiroTeamName());
		
		

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
		
		imgNames.put("home_page_banner_img", this.baseDirectory.getAbsolutePath() + "/images/miroteamreport/team_report_banner_image.png");
		imgNames.put("team_chart_img", this.baseDirectory.getAbsolutePath() + "/images/miroteamreport/team_chart_place_holder.png");
		imgNames.put("team_chart_table_img", this.baseDirectory.getAbsolutePath() + "/images/miroteamreport/team_chart_table_place_holder.png");
		imgNames.put("team_radar_chart_img", this.baseDirectory.getAbsolutePath() + "/images/miroteamreport/team_radar_chart_holder.png");
		imgNames.put("team_bar_chart_img", this.baseDirectory.getAbsolutePath() + "/images/miroteamreport/team_bar_chart_place_holder.png");
		
		
		
		
		
		imgNames.put("imgU1", this.baseDirectory.getAbsolutePath() + "/images/" + "U1.png");
		imgNames.put("imgU3", this.baseDirectory.getAbsolutePath() + "/images/" + "U3.png");
		imgNames.put("imgU2", this.baseDirectory.getAbsolutePath() + "/images/"  + "U2.png");
		imgNames.put("imgU6", this.baseDirectory.getAbsolutePath() + "/images/"  + "U6.png");
	   
		
		
		
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
		
		MiroPage teamRadarPage = new MiroPage();
		teamRadarPage.add(new MiroPageElement("team_radar_chart"));
		pages.add(teamRadarPage);
		
		
//		practitiioner stuff
		pages.add(MiroPage.create("practitioner_details"));
		
		
		
	}
	
	
	

	private String getPieImage(MiroResponse mr) {
			
			return this.baseDirectory.getAbsolutePath() + "/out/"  + mr.getMiroReportName()+".jpg";
		
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

