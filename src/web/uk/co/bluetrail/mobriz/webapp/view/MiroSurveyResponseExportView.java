package uk.co.bluetrail.mobriz.webapp.view;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.co.bluetrail.mobriz.model.Question;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.SettingManager;

public class MiroSurveyResponseExportView extends SurveyResponseExportView {

	private SettingManager settingManager ;
	private Setting miroLetters;
	private LinkedHashMap mostMiroTotals;
	private LinkedHashMap leastMiroTotals;
	
	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}

	@Override
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

	
	public void resetMiroTotals() {
			
		mostMiroTotals = new LinkedHashMap() ;
		leastMiroTotals = new LinkedHashMap() ;
		
		String[] miroLettersArray = miroLetters.getSettingValue().split(";");
		for(int i = 0 ; i < miroLettersArray.length;i++) {
			leastMiroTotals.put(miroLettersArray[i], new Integer(0));
			mostMiroTotals.put(miroLettersArray[i], new Integer(0));
		}
			
		
	}



	private String getResponseLine(Survey survey, SurveyResponse surveyResponse) {
		
		resetMiroTotals();
		
		StringBuffer lop = new StringBuffer();
	
		Question question = null;
	
		lop.append(surveyResponse.getId());
		lop.append(COMMA);
		
		
		User user = surveyResponse.getUser() ; 
		
		if(user != null) {
			lop.append(user.getUsername());
			lop.append(COMMA);
			lop.append(user.getEmployeeRef());
		} else {
			lop.append(surveyResponse.getCreatedBy_id());
			lop.append(COMMA);
			lop.append("Unknown");
		}
		
		
		lop.append(COMMA);
				
		lop.append(surveyResponse.getCreated_on());
		lop.append(COMMA);
		
		Long qid = survey.getFirstQuestion_id();
	
		question = (Question) questionMap.get(qid); 
	
		String rawAnswer = null;
	
		String mostAnswer = null ;
		String leastAnswer = null ;
		Integer totalLeast = null ;
		Integer totalMost = null ;
		
		while (question != null) {
	
			rawAnswer = surveyResponse.getAnswer(question);
	
			String[] rawAnswers = rawAnswer.split(";");
			String[] mostAnswers = rawAnswers[0].split("#");
			String[] leastAnswers = rawAnswers[1].split("#");
			
			mostAnswer = mostAnswers[1].trim();
			leastAnswer = leastAnswers[1].trim();
			
			totalMost = (Integer) mostMiroTotals.get(mostAnswer) ;
			totalMost++;
			mostMiroTotals.put(mostAnswer,totalMost);
			totalLeast = (Integer) leastMiroTotals.get(leastAnswer) ;
			totalLeast++;
			leastMiroTotals.put(leastAnswer,totalLeast);
			
			
	
			question = (Question) questionMap.get(question.getJQuestion_id());
		}
	
		String[] miroLettersArray = miroLetters.getSettingValue().split(";");
		
		for(int i = 0 ; i < miroLettersArray.length-1;i++) {
			totalMost = (Integer) mostMiroTotals.get(miroLettersArray[i]) ;
			totalLeast = (Integer) leastMiroTotals.get(miroLettersArray[i]) ;
			lop.append(totalMost.intValue()-totalLeast.intValue());
			lop.append(COMMA);						
		}
		totalMost = (Integer) mostMiroTotals.get(miroLettersArray[miroLettersArray.length-1]) ;
		totalLeast = (Integer) leastMiroTotals.get(miroLettersArray[miroLettersArray.length-1]) ;
		lop.append(totalMost.intValue()-totalLeast.intValue());
		
		
		return lop.toString();
	
	
	}

	private String getHeader(Survey survey) {
		StringBuffer lop = new StringBuffer();
	
		Question question = null;
	
		lop.append("ID");
		lop.append(COMMA);
		lop.append("Name");
		lop.append(COMMA);
		lop.append("Emp Ref");
		lop.append(COMMA);
		lop.append("Date");
		lop.append(COMMA);
		
		miroLetters = settingManager.getSettingByName("MIRO_LETTERS");
		
		if(miroLetters ==null) {
			lop.append("MIRO_LETTERS not Found!!");
		}
		
		String[] miroLettersArray = miroLetters.getSettingValue().split(";");
		
		for(int i = 0 ; i < miroLettersArray.length-1;i++) {
			lop.append(miroLettersArray[i]);
			lop.append(COMMA);			
		}
		lop.append(miroLettersArray[miroLettersArray.length-1]);
		
				
		return lop.toString();
	
	
	}

	
	
	
}
