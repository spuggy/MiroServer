package uk.co.bluetrail.mobriz.webapp.view;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import uk.co.bluetrail.mobriz.service.LookupDefItemManager;
import uk.co.bluetrail.mobriz.service.LookupDefManager;
import uk.co.bluetrail.mobriz.service.QuestionManager;
import uk.co.bluetrail.mobriz.webapp.form.WebServiceForm;

public class SurveyResponseExportView extends AbstractView {

	protected QuestionManager questionManager = null ;

	protected HashMap questionMap;

	protected LookupDefManager lookupDefManager; 
	protected LookupDefItemManager lookupDefItemManager ;
	
	
	public QuestionManager getQuestionManager() {
		return questionManager;
	}

	public void setQuestionManager(QuestionManager questionManager) {
		this.questionManager = questionManager;
	}


	protected void renderMergedOutputModel(Map args, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		

	
	Survey survey = (Survey) args.get("survey");

	
	List responses = (List) args.get("surveyResponses");

	// response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-Disposition", "attachment; filename=\"" + survey.getTitle() + ".csv\"");

	PrintWriter pw = response.getWriter();

	StringBuffer recordString = null;

	// export the title
	pw.println(survey.getTitle());
	pw.println(CR);

	Iterator itr = responses.iterator();
	SurveyResponse resp = null;
	
	questionMap = survey.getQuestionMap();

	
	pw.println(getHeader(survey));

	while (itr.hasNext()) {

		resp = (SurveyResponse) itr.next();
		recordString = new StringBuffer();

		recordString.append(getResponseLine(survey, resp));

		String str = recordString.toString();

		pw.println(str);

	}


	}

	
	public static final String CR = "\n";

	public static final String COMMA = ",";

	public static final String QUOTE = "\"";


	
	private String getHeader(Survey survey) {
		StringBuffer lop = new StringBuffer();

		Question question = null;

		lop.append(csvWrite("ID",true));
		lop.append(COMMA);
		lop.append(csvWrite("Name",true));
		lop.append(COMMA);
		lop.append(csvWrite("Emp Ref",true));
		lop.append(COMMA);
		lop.append(csvWrite("Date",true));
		lop.append(COMMA);
		Long qid = survey.getFirstQuestion_id();

		question = (Question) questionMap.get(qid);

		while (question != null) {

			if (question.getQTypeStr().equals(Question.QTYPE_CHECKBOX)
					|| question.getQTypeStr().equals(Question.QTYPE_SCORE)
					|| question.getQTypeStr().equals(Question.QTYPE_PERCENT)) {

				explodeOptionsToHeader(question, lop);

			} else if (question.getQTypeStr().equals(Question.QTYPE_LOOKUP)) {
			
				explodeLookupToHeader(question,lop);
				
			} else {
				lop.append(csvWrite(question.getQTxt(),true));
				lop.append(COMMA);
			}

			question = (Question) questionMap.get(question.getJQuestion_id());
		}

		return lop.toString();

	
	}

	private StringBuffer csvWrite(String string, boolean isString) {
		StringBuffer sb = new StringBuffer();
		if(isString) {
			sb.append(QUOTE);
			sb.append(string.replaceAll("\r\n|\n|\"", " "));
			sb.append(QUOTE);
			return sb;
		}
		return sb.append(string.replaceAll("\r\n|\n|\"", " "));
	}

	protected void explodeLookupToHeader(Question question, StringBuffer lop) {


		LookupDef lookupDef = lookupDefManager.getLookupDef(question.getQMeta());
		question.setLookupDef(lookupDef);			

		List fields = lookupDef.reportFields() ;

		Iterator fldItr = fields.iterator();

		String hdr = null;
		;

		while (fldItr.hasNext()) {

			String fldName = (String) fldItr.next();

			hdr = question.getQTxt() + ":" + fldName;

			lop.append(csvWrite(hdr,true));

			lop.append(COMMA);

		}

		
		
		
	}

	protected void explodeOptionsToHeader(Question question, StringBuffer lop) {

		Set options = question.getOptions();

		Iterator optItr = options.iterator();

		String hdr = null;
		;

		while (optItr.hasNext()) {

			Option option = (Option) optItr.next();

			hdr = question.getQTxt() + ":" + option.getOText();

			lop.append(csvWrite(hdr,true));

			lop.append(COMMA);

		}

	}

	private String getResponseLine(Survey survey, SurveyResponse surveyResponse) {
		
		StringBuffer lop = new StringBuffer();

		Question question = null;

		lop.append(csvWrite(surveyResponse.getId().toString(),false));
		lop.append(COMMA);
		
		
		User user = surveyResponse.getUser() ; 
		
		if(user != null) {
			lop.append(csvWrite(user.getUsername(),true));
			lop.append(COMMA);
			lop.append(csvWrite(user.getEmployeeRef(),true));
		} else {
			lop.append(csvWrite(surveyResponse.getCreatedBy_id().toString(),true));
			lop.append(COMMA);
			lop.append(csvWrite("Unknown",true));
		}
		
		
		lop.append(COMMA);
				
		lop.append(surveyResponse.getCreated_on());
		lop.append(COMMA);
		Long qid = survey.getFirstQuestion_id();

		question = (Question) questionMap.get(qid);

		String rawAnswer = null;

		while (question != null) {

			rawAnswer = surveyResponse.getAnswer(question);

			if (question.getQTypeStr().equals(Question.QTYPE_CHECKBOX)
					|| question.getQTypeStr().equals(Question.QTYPE_SCORE)
					|| question.getQTypeStr().equals(Question.QTYPE_PERCENT)) {

				if (question.getQTypeStr().equals(Question.QTYPE_CHECKBOX)) {

					unpackCheckBoxAnswers(rawAnswer, question, lop);

				} else {
					unpackAnswers(rawAnswer, question, lop);
				}

			} else if ( question.getQTypeStr().equals(Question.QTYPE_LOOKUP)) {
			
				unpackLookupAnswers(rawAnswer,question,lop);
			
			} else {
				lop.append(csvWrite(rawAnswer,question.isText()));  
				lop.append(COMMA);

			}

			question = (Question) questionMap.get(question.getJQuestion_id());
		}

		return lop.toString();


	}

	protected void unpackLookupAnswers(String rawAnswer, Question question,StringBuffer lop) {

		String id = null;
		
		try { 
		
		id = LookupDefItem.parseId(rawAnswer.trim()) ;
			
		LookupDefItem lookupDefItem = lookupDefItemManager.getLookupDefItem(id);
		
		List fields = lookupDefItem.getReportFields(question.getLookupDef());

			
		Iterator fldItr = fields.iterator();

		String v = null;

		while (fldItr.hasNext()) {

			String fldName = (String) fldItr.next();

			v = fldName;

			lop.append(csvWrite(v,true));

			lop.append(COMMA);

		}
		
		} catch (Exception e) {			
			lop.append(csvWrite("Could not find lookupItemDef id=" + id,true) );
		}
		
		
	}

	/**
	 * @param rawAnswer
	 * @param question
	 * @param lop
	 */
	protected void displayDate(String rawAnswer, Question question, StringBuffer lop) {

		
		
		
		
		Calendar calendar = Calendar.getInstance();
		
		long dtLong = 0 ;
		
		try {
			dtLong = Long.parseLong(rawAnswer) ;
		} catch (Exception e) {
			//assume errror parsing means not a number 
			lop.append(rawAnswer);
			lop.append(COMMA);
			return ;
		}
		Date dt = new Date(dtLong);
		
		calendar.setTime(dt);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int date = calendar.get(Calendar.DATE);

		lop.append(date);
		lop.append('/') ;
		lop.append(month) ;
		lop.append('/') ;
		lop.append(year);

		lop.append(COMMA);

		
		
	}

	/**
	 * 
	 * Unpacks the results from a score/percent field into columns. These are
	 * numbers separated by semi colons
	 * 
	 * @param rawAnswer
	 * @param question
	 */
	protected void unpackAnswers(String rawAnswer, Question question,
			StringBuffer lop) {

		Set options = question.getOptions();

		Iterator optItr = options.iterator();

		int index = 0;
		String[] values = rawAnswer.split(";");

		while (optItr.hasNext()) {

			Option option = (Option) optItr.next();
			lop.append(getValueAt(values, index++));

			lop.append(COMMA);

		}

	}

	protected String getValueAt(String[] values, int index) {

		try {
			return values[index];
		} catch (RuntimeException e) {
			return ("");
		}

	}

	/**
	 * 
	 * Unpacks the results from a checkbox field into columns. These are numbers
	 * separated by semi colons, but not every option is returned so need to
	 * check the option list
	 * 
	 * @param rawAnswer
	 * @param question
	 */
	protected void unpackCheckBoxAnswers(String rawAnswer, Question question,
			StringBuffer lop) {

		Set options = question.getOptions();

		Iterator itr = options.iterator();

		String[] values = rawAnswer.split(SurveyResponse.SEP);
		Option option = null;
		while (itr.hasNext()) {

			option = (Option) itr.next();

			lop.append(isMember(values, option.getOText()));
			lop.append(COMMA);

		}

	}

	/**
	 * 
	 * Checks an array for a value and returns 0 if not found and 1 if found
	 * 
	 * @param values
	 * @param i
	 * @return
	 */
	protected int isMember(String[] values, String value) {

		if (values == null) {
			return 0;
		}

		for (int i = 0; i < values.length; i++) {
			if (values[i].equals(value)) {
				return 1;
			}
		}

		return 0;
	}

	public void setLookupDefManager(LookupDefManager lookupDefManager) {
		this.lookupDefManager = lookupDefManager;
	}

	public void setLookupDefItemManager(LookupDefItemManager lookupDefItemManager) {
		this.lookupDefItemManager = lookupDefItemManager;
	}

	
	
}
