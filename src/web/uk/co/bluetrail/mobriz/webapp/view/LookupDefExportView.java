package uk.co.bluetrail.mobriz.webapp.view;

import java.io.PrintWriter;
import java.util.AbstractCollection;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import uk.co.bluetrail.mobriz.model.LookupDef;
import uk.co.bluetrail.mobriz.model.LookupDefItem;
import uk.co.bluetrail.mobriz.model.Option;
import uk.co.bluetrail.mobriz.model.Question;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.QuestionManager;
import uk.co.bluetrail.mobriz.webapp.form.WebServiceForm;

public class LookupDefExportView extends AbstractView {

	
	@Override
	protected void renderMergedOutputModel(Map args, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		

	
	LookupDef lookupDef = (LookupDef) args.get("lookupDef");
	List lookupDefItems = (List) args.get("lookupDefItems");

	// response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-Disposition", "attachment; filename=\"" + lookupDef.getName() + ".txt\"");

	PrintWriter pw = response.getWriter();
	
	Iterator itr = lookupDefItems.iterator();
	LookupDefItem lookupDefItem  = null;
		
	pw.println(getCSVList(lookupDef.getFields(lookupDef)));

	while (itr.hasNext()) {

		lookupDefItem = (LookupDefItem) itr.next();		
		
		pw.println(getCSVList(lookupDefItem.getFields(lookupDef)));

	}


	}

	
	private String getCSVList(AbstractCollection col) {
		
		Iterator itr = col.iterator();
		String v = null ; 
		StringBuffer line = new StringBuffer();
		
		while(itr.hasNext()){
			v = (String) itr.next();
			line.append(v);
			line.append(TAB);			
			
		}		
	
		
		return line.toString();
	}


	public static final String CR = "\n";

	public static final String TAB = "\t";

	private List questionList ;

	


	
		
}
