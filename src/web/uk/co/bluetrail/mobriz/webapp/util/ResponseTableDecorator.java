package uk.co.bluetrail.mobriz.webapp.util;

import org.displaytag.decorator.TableDecorator;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Survey;

public class ResponseTableDecorator extends TableDecorator {

	public static String LT = "<";
	public static String GT = "&gt;";
	
	public String getPhotoLink() {
		
		Survey survey = (Survey) getCurrentRowObject();

		if(survey.getPhotoFile()==null || survey.getPhotoFile() == null) {
			return Constants.NOTAPPLICABLE ;
		}
 		
		
		
		return "<a href=\"" + Constants.PHOTODIR + "/" + survey.getPhotoFile()  + "\"><img src=\"styles/images/images.gif\" /></a>"; 
		
		
	
		
	}
	
	
	
	
}
