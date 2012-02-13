package uk.co.bluetrail.miro;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	private OrderedMap teamTotals;
	
	
	private Map<String,Integer> leadingTotals ;
	private Map<String,Integer> secondaryTotals ;
	private MiroReportLevel miroLevels;
	
	
	
	
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

	//	this.generateChart();
		this.generateXMLReportFile();   
		MiroReportPDFGenerator.generatePDF(this.baseDirectory, this.miroTeam.getMiroTeamReportName());
		
		
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
		
		variables.put("team_report_name" , miroTeam.getMiroTeamReportName());
		variables.put("reportFileName" , miroTeam.getMiroTeamReportName());
		
		

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
		
		MiroPage teamPiesPage = new MiroPage();
		List members = this.miroTeam.getMembers();
		Iterator itr = members.iterator();
		while(itr.hasNext()) {
			MiroResponse mr = (MiroResponse) itr.next();
	
			String resultLetters[] = mr.getResultLetters();
			
			String idKey = null;
			String pieKey = null;
			String idSuffix = "pie_and_bullets_";
			MiroPageElement mpe = null;
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
			teamPiesPage.add(mpe);
			
		}
		pages.add(teamPiesPage);
		
		
		//loop over users and add them 
		//set the varables for their images
		
		/*
		MiroPageElement[] pe = new MiroPageElement[3];
		pe[0] = new  MiroPageElement("homepage","100");
		pe[1] = new  MiroPageElement("homepage","101");
		pe[2] = new  MiroPageElement("homepage","102");
		pages.add(pe);
		*/
		
		
		//add team map - genrerate this to file?
		
		//add coloured reporitng table 
		
		//add paras
		
		//add a bar char - how to we do this?
		
		//ad more paras
		
		//more shit after this ... not sure how it worls speak to pob
		
		
		
			
		
		
		
		
		/*
		if(this.isExcess(results[0])) {
			pages.add(new String[] { resultLetters[0]+"1.1"});
		} else {
			pages.add(new String[] { resultLetters[0]+"1"});
		}
		
	
//		page5
		if(this.isEngaged(results[1])) {
			pages.add(new String[] { resultLetters[1]+"2" });
		} else {
			pages.add(new String[] { resultLetters[0] });
			
		}
//		page6
		if(this.isEngaged(results[1])) {
			pages.add(new String[] { resultLetters[0]+"-"+resultLetters[1] });
		} else {
			pages.add(new String[] { resultLetters[1]+"3" });
		}
//		page7
		pageItems = new String[2];
		
		if(this.isEngaged(results[2])) {
			pageItems[0] = resultLetters[2]+"4" ;
		} else {
			pageItems[0] =  resultLetters[2]+"5" ;

		}
		
		if(this.isLatent(results[3])) {
			pageItems[1] = resultLetters[3]+"6.1" ;
		} else {
			pageItems[1] = resultLetters[3]+"6";
			
		}
		
		pages.add(pageItems);
		
		//page8
		//added by rob 26/08
		if(this.isEngaged(results[0]) && this.isEngaged(results[1])) {
			pages.add(new String[] { resultLetters[0]+"-"+resultLetters[1]+"7"});
		} else {
			pages.add(new String[] { resultLetters[0]+"7"});
		}
		
			
		//page9
		pages.add(new String[] { "U5" });
		
//		page10
		pages.add(new String[] { "U6" });
		
		
//		page11
		pages.add(new String[] { "U7" });
		*/
		
		
		
		
		
		
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
		return "";
	}
	
	/**
	 * gets the modelevel for the specified index postion will return things like H M L A  .. 
	 * 
	 * @param index
	 * @return
	 */
	public String getModeLevel(int index)	{
		calculateResultsTable();
		return "";
	}
	
	/**
	 * goes thru the table of results and sums up the totals etc
	 */
	private void calculateResultsTable() {
		
		if(this.leadingTotals==null) {
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
		for(int i = 0 ; i < this.miroTeam.getTeamResults().size();i++) {
			TeamMapDTO dt = (TeamMapDTO) this.miroTeam.getTeamResults().get(i);
			Integer lm = this.leadingTotals.get(dt.getLeadingMode()) ; 
			this.leadingTotals.put(dt.getLeadingMode(), lm++);
			Integer sm = this.secondaryTotals.get(dt.getSecondaryMode()) ; 
			this.secondaryTotals.put(dt.getSecondaryMode(), sm++);
		}

		//loop over map, calculate percentages 
		this.teamTotals = new LinkedMap();
		int teamTotalTotals = 0 ;
	
		for(int i = 0 ; i < miroLetters.length;i++) {
			Integer lm = this.leadingTotals.get(miroLetters[i]) ;
			Integer sm = this.secondaryTotals.get(miroLetters[i]);
			int teamTotal = (lm*2) + sm ;
			this.teamTotals.put(miroLetters[i], lm);
			teamTotalTotals = teamTotalTotals + teamTotal;
		}
		
		//finally make the values in the map %
		for(int i = 0 ; i < miroLetters.length;i++) {
			
			Integer teamTotal = (Integer) this.teamTotals.get(miroLetters[i]) ;
			if(teamTotal>0) {
				this.teamTotals.put(miroLetters[1], (teamTotal/teamTotalTotals)*100);
			}
			
		}
		
		
	
		
		
		
	}

	public void buildReportPageList(MiroTeam miroTeam,List<MiroPage> pages,Map<String, String> variables,Map<String, String> imgNames) {
		
		this.miroTeam = miroTeam;
		
		buildReportPageList(pages,variables,imgNames);
		
	}
	
	

}
