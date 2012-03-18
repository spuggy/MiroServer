package uk.co.bluetrail.mobriz.webapp.util;

import org.displaytag.decorator.TableDecorator;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.Survey;

public class MiroTeamTableDecorator extends TableDecorator {

	
	public static String LT = "<";
	public static String GT = "&gt;";
	private MiroTeam mt = null;
	
	
	
	/**
	 * @hibernate.property 
	 * @return the mt
	 */
	public MiroTeam getMt() {
		return mt;
	}



	/**
	 * @param mt the mt to set
	 */
	public void setMt(MiroTeam mt) {
		this.mt = mt;
	}



	public String getTeamReportStatus() {

		
		
		mt = (MiroTeam) getCurrentRowObject();
		
		
		
		switch (mt.getTeamReportStatus()) {

		case MiroTeam.REPORT_CREATED:
			
			return "draft";


		case MiroTeam.REPORT_REQUESTED:

			return "report requested";

		case MiroTeam.REPORT_DOWNLOADED:

			return "<a href=\"miroTeamReportShow.html?id=" + mt.getId() + "\" /><span class=\"status40\">download report<img src=\"images/pdf.gif\" /></span></a>"; 
			

		default:

			return "unknown status";
			

		}

		//return "<a href=\"" + Constants.PHOTODIR + "/" + survey.getPhotoFile()
		//		+ "\"><img src=\"styles/images/images.gif\" /></a>";

	}
	
	

}
