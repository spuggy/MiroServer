package uk.co.bluetrail.mobriz.webapp.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractView;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Constraint;
import uk.co.bluetrail.mobriz.model.LookupDef;
import uk.co.bluetrail.mobriz.model.LookupDefItem;
import uk.co.bluetrail.mobriz.model.Option;
import uk.co.bluetrail.mobriz.model.Question;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyElement;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.LookupDefItemManager;
import uk.co.bluetrail.mobriz.service.LookupDefManager;
import uk.co.bluetrail.mobriz.service.SurveyManager;
import uk.co.bluetrail.mobriz.service.SurveyResponseManager;
import uk.co.bluetrail.mobriz.service.UserManager;
import uk.co.bluetrail.mobriz.util.SurveyElementUtil;
import uk.co.bluetrail.mobriz.webapp.form.WebServiceForm;
import uk.co.bluetrail.mobriz.webapp.util.DeviceSurvey;
import uk.co.bluetrail.mobriz.webservice.QuestionAttributes;

public class SurveyWebServiceView extends AbstractView {

	protected final transient Log log = LogFactory.getLog(getClass());
	private SurveyResponseManager surveyResponseManager ;
	private LookupDefItemManager lookupDefItemManager;
	private LookupDefManager lookupDefManager;
	
	private int maxSearchLimit  ;  // the limit to how many records the search can select from the db.
	private int maxSearchDownload  ; //the maximum number of records that can be downloaed
	
	
	private SurveyManager surveyManager ;
	private UserManager userManager ;
	private static final char SEP = '\t';
	private static final char START_RECORD = '[';
	private static final char END_RECORD = ']';
	public static final String MCCMSVERSION = "mobriz-version";
	private ArrayList mobrizVersion ;
	private String firstPhotoVersion  ;
	private String fileDirectory ;
	
	
	private User user;

	
	public void setFirstPhotoVersion(String firstPhotoVersion) {
		this.firstPhotoVersion = firstPhotoVersion;
	}






	@Override
	protected void renderMergedOutputModel(Map args, HttpServletRequest request,HttpServletResponse response) throws Exception {
		  if (log.isDebugEnabled()) {
	            log.debug("entering 'onSubmit' method...");
	        }

	        WebServiceForm wsForm = null;

			String opCode;

			try {

				wsForm = (WebServiceForm) args.get("wsForm");

				opCode = wsForm.getOc();

				user = getUser(wsForm.getUp());

				if (user == null) {
					handleBadUserPin(wsForm, request, response);
					return ;
				}

				
				if (!isValidVersion(wsForm)) {
					handleBadVersion(wsForm, request, response);
					return ;
				}

				if (opCode.equals(Constants.WS_GETSURVEYUPDATES)) {
					getSurveyUpdates(wsForm, request, response);
				} else if (opCode.equals(Constants.WS_GETSURVEY)) {
					getSurvey(wsForm, request, response);
				} else if (opCode.equals(Constants.WS_POST_RESPONSE)) {
					postResponse(wsForm, request, response);
				} else if (opCode.equals(Constants.WS_POST_IMAGE)) {
					postImage(wsForm, request, response);
				} else if (opCode.equals(Constants.WS_LOOKUP)) {
					performLookup(wsForm, request, response);					
				} else{
					handleException("Unknown operation " + opCode, wsForm, request,response);
					return ;
				}

			} catch (Exception e) {
				handleException(e.toString(), wsForm, request, response);

			}

	        
	        return ;

	}

	
	
	
    
    
    private void performLookup(WebServiceForm wsForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
    	LookupDef lookupDef = lookupDefManager.getLookupDef(wsForm.getLid());     	
    	LookupDefItem searchItem = new LookupDefItem();
    	
    	searchItem.setLookupDef_id(new Long(wsForm.getLid()));
    	searchItem.add(wsForm.getLkv(),lookupDef);
    	  
    	    	
    	List searchResults = lookupDefItemManager.getLookupDefItemsQBE(searchItem,this.maxSearchLimit);
    	
    	
    	PrintWriter pw = response.getWriter();
    	
    	if(searchResults == null ||searchResults.size()==0) {
    		printError(pw,"No Results found, please try again!") ;
    	} else if(searchResults.size() > this.maxSearchDownload   ) {
    		printError(pw,"Too many results found, please make your search more specific") ;
    	} else {
    		printSearchResults(pw,lookupDef,searchResults);
    	}
    	
    	
    	logRecord(wsForm,"");
    	response.setStatus(HttpServletResponse.SC_OK);
		
	}






	private void printSearchResults(PrintWriter pw, LookupDef lookupDef, List searchResults) {
		
		
		LookupDefItem searchResult = null; 
		
		Iterator itr = searchResults.iterator() ;
		
		while(itr.hasNext()) { 
			searchResult = (LookupDefItem) itr.next();
			
			pw.print(START_RECORD);
			pw.print('l');
			pw.print(SEP);
			pw.print(searchResult.getValue());  
			pw.print(SEP);
			pw.print(searchResult.getDisplayValue(lookupDef));
			pw.print(END_RECORD);
			
		}
		
	}






	private void printError(PrintWriter pw ,String errMess) {
		log.warn("WSLookup: " + errMess);
		
		pw.print(START_RECORD);
		pw.print("e");
		pw.print(SEP);
		pw.println(errMess);
		pw.print(END_RECORD);
		
	}






	private void postImage(WebServiceForm wsForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String SR_SEP = "~";
		String[] sid = wsForm.getSid();
		String[] qids = wsForm.getQid();
		String[] answers = wsForm.getA();
		String surveyVersion = wsForm.getVer();
		String pid = wsForm.getPid() ;
		String ext = wsForm.getExt() ;
		
		StringBuffer  fileName = new StringBuffer() ;
		fileName.append(user.getId().toString()) ;
		fileName.append("_") ;		
		fileName.append(pid) ;
		fileName.append(".") ;
		fileName.append(ext) ;
		
		
		
		 // validate a file was entered
        if (wsForm.getFile().length == 0) {
           
        }
        
        MultipartHttpServletRequest multipartRequest =   (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");

  		
        //the directory to upload to
        String uploadDir = getServletContext().getRealPath("/" + Constants.PHOTODIR + "/"+ sid[0] )  ; 

        // Create the directory if it doesn't exist
        File dirPath = new File(uploadDir);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        //retrieve the file data
        InputStream stream = file.getInputStream();

        //write the file to the file specified
        OutputStream bos =  new FileOutputStream(uploadDir + "/" + fileName.toString());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];

        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        bos.close();

        //close the stream
        stream.close();
        
        
		PrintWriter pw = response.getWriter();

		pw.print(START_RECORD);
		pw.print('i');
		pw.print(SEP);
		pw.print(wsForm.getPid());
		pw.print(END_RECORD);


		//logRecord(wsForm,wsForm.getUp() + answerTrail.toString());

    	
		
	}






	public void setSurveyResponseManager(SurveyResponseManager surveyResponseManager) {
		this.surveyResponseManager = surveyResponseManager;
	}






	public void setSurveyManager(SurveyManager mgr) {
    	this.surveyManager =  mgr;

    }
    
    public void setUserManager(UserManager userManager) {
    	this.userManager =  userManager;

    }
    
	/**
	 * @param wsForm
	 * @param request
	 * @param response
	 */
	private void postResponse(WebServiceForm wsForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		
		SurveyResponse surveyResponse = new SurveyResponse();
		String SR_SEP = "~";
		String[] sid = wsForm.getSid();
		String[] qids = wsForm.getQid();
		String[] answers = wsForm.getA();
		String surveyVersion = wsForm.getVer();
				
		
		StringBuffer questionTrail = new StringBuffer();
		StringBuffer answerTrail = new StringBuffer();
		
		

		surveyResponse.setSurvey_id(new Long(sid[0]));

		

		for (int i = 0; i < qids.length - 1; i++) {
			
		
			questionTrail.append(qids[i]);

			questionTrail.append(SR_SEP);
			answerTrail.append(answers[i]);

			answerTrail.append(SR_SEP);
		}
		
		
		
		questionTrail.append(qids[qids.length - 1]);
		
		
		
		answerTrail.append(answers[qids.length - 1]);
		
		
		surveyResponse.setAnswer_trail(answerTrail.toString());
		surveyResponse.setQuestion_trail(questionTrail.toString());

		
		
		SurveyElementUtil.timeStamp((SurveyElement) surveyResponse, user);
		surveyResponseManager.saveSurveyResponse(surveyResponse);

		
		PrintWriter pw = response.getWriter();

		pw.print(START_RECORD);
		pw.print('r');
		pw.print(SEP);
		pw.print(surveyResponse.getId());
		pw.print(END_RECORD);

		logRecord(wsForm,surveyResponse.getId().toString()+":"+questionTrail.toString()+":"+answerTrail.toString());
		
		
	}

	
	/**
	 * @param string
	 */
	private void logRecord(WebServiceForm wsForm,String data) {
		
		StringBuffer logLine = new StringBuffer() ;
		
		char sep = ':' ;
		
		logLine.append(user.getUsername());
		logLine.append(sep);
		logLine.append(wsForm.getOc());
		logLine.append(sep);
		logLine.append(wsForm.getBt()) ;
		logLine.append(sep);
		logLine.append(data);
				
		log.info(logLine.toString());
	}

	

	protected boolean isValidVersion(WebServiceForm wsForm) {
		
		
		String version = wsForm.getMv();
		
		List validVersions = this.getMobrizVersion() ;
		
		Iterator itr = validVersions.iterator() ;
		
		String validVersion = null ;
		while(itr.hasNext()) {
			validVersion = (String) itr.next() ; 
			if(version.toLowerCase().equals(validVersion.toLowerCase())) {
				return true ; 
			}
		}
		
		
		//if its a palm and it is older than the new first phohot version then 
		//let it thru otherwise warn of invalid version		
		if(!hasPhotoSupport(wsForm) && isPalm(wsForm)) {
			return true ;
		} 
		
		return false;
	}

	private boolean isPalm(WebServiceForm wsForm) {
		
		String build = wsForm.getBt().toLowerCase() ;
		
		if(build.indexOf("palm") == -1 ) {
			return false ;
		} else {
			return true ;
		}
		
		
	}






	/**
	 * 
	 */
	private void handleBadUserPin(WebServiceForm wsForm,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		log.warn("WSError: bad user pin " + wsForm.getUp());

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

	}

	private void handleBadVersion(WebServiceForm wsForm,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		log.warn("WSError: bad version " + wsForm.getMv());

		PrintWriter pw = response.getWriter();
		pw.print(START_RECORD);
		pw.print("e");
		pw.print(SEP);
		pw.println(this.getMobrizVersion().toString());
		pw.print(END_RECORD);

		response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);

	}

	/**
	 * @param string
	 * @param wsForm
	 * @param request
	 * @param response
	 */
	private void handleException(String eMess, WebServiceForm wsForm,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		log.error("WSEXception: " + eMess);

		log.error(wsForm.toString());
		
			
				
		PrintWriter pw = response.getWriter();
		pw.print(START_RECORD);
		pw.print("e");
		pw.print(SEP);
		pw.println(eMess);
		pw.print(END_RECORD);

		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

	}

	/**
	 * @param wsForm
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void getSurveyUpdates(WebServiceForm wsForm,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		
		List surveys = surveyManager.getVisibleSurveys(user);

		// build a hashtable of the sids on the device
		Hashtable deviceSurveys = new Hashtable();

		String[] deviceSids = wsForm.getSid();
		String[] deviceChkps = wsForm.getCp();
		String[] deviceStatus = wsForm.getSt();

		PrintWriter pw = response.getWriter();
		DeviceSurvey deviceSurvey = null;
		if (deviceSids != null) {

			// build an in memory device table
			for (int i = 0; i < deviceSids.length; i++) {
				deviceSurvey = new DeviceSurvey(deviceSids[i], deviceChkps[i],
						deviceStatus[i]);
				deviceSurveys.put(deviceSurvey.getSid(), deviceSurvey);
			}

		}
		

		// spin thru the surveys on the
		Iterator itr = surveys.iterator();
		Survey survey = null;

		while (itr.hasNext()) {
			survey = (Survey) itr.next();
			// if the user already has the survey with the same chekcpoint then
			// send back the same status. Otherwise give em a new one.
			deviceSurvey = (DeviceSurvey) deviceSurveys.get(survey.getId());
			if (deviceSurvey == null || !deviceSurvey.equals(survey)) {				
				printSurvey(pw, survey, DeviceSurvey.SURVEY_TO_LOAD);
			} else {				
				printSurvey(pw, survey, deviceSurvey.getStatus());
			}
		}
		
		logRecord(wsForm,"");
				
		
		response.setStatus(HttpServletResponse.SC_OK);

	}

	/**
	 * @param pw
	 * @param survey
	 */
	private void printSurvey(PrintWriter pw, Survey survey, char status) {

		pw.print(START_RECORD);
		pw.print('s');
		pw.print(SEP);
		pw.print(status);
		pw.print(SEP);
		pw.print(survey.getId());
		pw.print(SEP);
		pw.print(survey.getCheckPoint());
		pw.print(SEP);
		pw.print("1");
		pw.print(SEP);
		pw.print(survey.getQuestionCount());
		pw.print(SEP);
		pw.print(survey.getFirstQuestion_id());
		pw.print(SEP);
		pw.print(survey.getTitle());
		pw.print(END_RECORD);

	}

	/**
	 * @param wsForm
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void getSurvey(WebServiceForm wsForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		
		Survey survey = null;

		String[] sids = wsForm.getSid();

		if (sids[0] == null) {
			log.error("sid table is null");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		survey = surveyManager.getSurvey(sids[0]);

		if (survey == null) {
			log.error("could not find sid = " + wsForm.getSid()[0]);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		Iterator itr = survey.getQuestions().iterator();
		Iterator itr2 = null;

		PrintWriter pw = response.getWriter();
		Question question = null;
		Option option = null;
		Constraint constraint = null;

		printSurvey(pw, survey, DeviceSurvey.SURVEY_TO_LOAD);

		while (itr.hasNext()) {
			question = (Question) itr.next();
			printQuestion(pw, question, wsForm);

			if (question.getOptions() != null) {
				itr2 = question.getOptions().iterator();
				while (itr2.hasNext()) {
					option = (Option) itr2.next();
					printOption(pw, option);
				}
			}

			if (question.getConstraints() != null) {
				itr2 = question.getConstraints().iterator();
				while (itr2.hasNext()) {
					constraint = (Constraint) itr2.next();
					printConstraint(pw, constraint);
				}
			}

		}

		logRecord(wsForm,survey.getId().toString());
		
		response.setStatus(HttpServletResponse.SC_OK);

	}

	/**
	 * @param pw
	 * @param constraint
	 */
	private void printConstraint(PrintWriter pw, Constraint constraint) {

		if (constraint.isDeleted()) {
			return;
		}

		pw.print(START_RECORD);
		pw.print('c');
		pw.print(SEP);
		pw.print(constraint.getCType());
		pw.print(SEP);
		pw.print(constraint.getI1());
		pw.print(SEP);
		pw.print(constraint.getI2());
		pw.print(SEP);
		if (constraint.getD1() == null) {
			pw.print(0);
		} else {
			pw.print(constraint.getD1().getTime());				
		}
		pw.print(SEP);
		if (constraint.getD2() == null) {
			pw.print(0);
		} else {
			pw.print(constraint.getD2().getTime());
		}
		pw.print(END_RECORD);

	}

	private void printOption(PrintWriter pw, Option option) {

		if (option.isDeleted()) {
			return;
		}

		pw.print(START_RECORD);
		pw.print('o');
		pw.print(SEP);
		if (option.getJQuestion_id() == null) {
			pw.print(0);
		} else {
			pw.print(option.getJQuestion_id());
		}
		pw.print(SEP);
		pw.print(option.getOText());
		pw.print(END_RECORD);

	}

	/**
	 * @param pw
	 * @param question
	 */
	private void printQuestion(PrintWriter pw, Question question, WebServiceForm wsForm) {

		if (question.isDeleted()) {
			return;
		}

		
		// sets the attr string which can be a combination of fields
		QuestionAttributes attrs = new QuestionAttributes();
		attrs.setRequired(question.isRequired());
		attrs.setSticky(question.isSticky());
			
		
		
		String questionType = null ; 
		String questionText = null ;
		questionType= question.getQType() +"" ;
		questionText = question.getQTxt() ;

		//patch for photo questions and old version.  Transforms photo types to message question and adds
		//not supported message to question.		
		if(isPictureQuestion(question.getQTypeStr()) && !hasPhotoSupport(wsForm)) {
			
			questionType= question.QTYPE_MESSAGE ;
			questionText = question.getQTxt() + " - photos are not supported on this device please ignore this question." ;
			attrs.setRequired(false);
			
		}
		
		
		
		if(isLookupQuestion(question)) {
			LookupDef lookupDef = lookupDefManager.getLookupDef(question.getQMeta());
			question.setLookupDef(lookupDef);			
		}
	
		pw.print(START_RECORD);
		pw.print('q');
		pw.print(SEP);
		pw.print(question.getSurvey_id());
		pw.print(SEP);
		pw.print(question.getId());
		pw.print(SEP);
		pw.print(question.getCheckPoint());
		pw.print(SEP);
		pw.print(question.getJQuestion_id());
		pw.print(SEP);
		pw.print(question.getBranch_jquestion_id());
		pw.print(SEP);
		pw.print(questionType);
		pw.print(SEP);
		pw.print(attrs.getAttr());
		pw.print(SEP);
		pw.print(0);
		pw.print(SEP);
		pw.print(0);
		pw.print(SEP);
		pw.print(question.getOptionCount());
		pw.print(SEP);
		pw.print(question.getConstraintCount());
		pw.print(SEP);
		pw.print(questionText);
		pw.print(END_RECORD);

	}

	






	private boolean isLookupQuestion(Question question) {
		
		return question.getQTypeStr().equals(Question.QTYPE_LOOKUP);
		
	}






	private boolean hasPhotoSupport(WebServiceForm wsForm) {
		
		String curVersion = getBuildVersion(wsForm.getBt());
		
		
		
		int i = curVersion.compareTo(this.firstPhotoVersion);            // 32; lowercase follows uppercase
	    		 
		if (i < 0) {
			return false;
	    } else if (i > 0) {
	        return true ;
	    } else {
	    	return true ;
	    }
		
		
	}






	private boolean isPictureQuestion(String type) {
		
		return type.equals(Question.QTYPE_PICTURE);
		
	}






	protected User getUser(String pinNumber) {

		

		log.warn("Searching for pinm=" + pinNumber);
		User userQBE = new User();
		// make sure it only works with lower case numbers
		userQBE.setPinNumber(pinNumber.toLowerCase().trim());

		List users = userManager.getUserQBE(userQBE);

		if (users == null) {
			log.warn("Could not find user with pin=" + pinNumber);
			return null;
		}

		if (users.size() > 1) {
			log.warn("Found more than one use with pin=" + pinNumber);
			return null;
		}

		if (users.size() == 0) {
			log.error("** Could not find user with pin=" + pinNumber);
			return null;
		}

		User user = (User) users.get(0);

		log.debug("found user:" + user.getUsername());

		return user;

	}



	public void setMobrizVersion(ArrayList mobrizVersion){
		this.mobrizVersion = mobrizVersion;
	}

	public ArrayList getMobrizVersion() {
		return mobrizVersion;
	}




	






	public void setFileDirectory(String fileDirectory) {
		this.fileDirectory = fileDirectory;
	}
	
	
	
	private String getBuildVersion(String buildString) {
		
		String[] parts = buildString.toLowerCase().split(".jar_") ;
		
		try {
			return parts[1];
		} catch(Exception e) {
			return "";
		}
	}






	public void setLookupDefItemManager(LookupDefItemManager lookupDefItemManager) {
		this.lookupDefItemManager = lookupDefItemManager;
	}






	public void setLookupDefManager(LookupDefManager lookupDefManager) {
		this.lookupDefManager = lookupDefManager;
	}






	public void setMaxSearchLimit(int maxSearchLimit) {
		this.maxSearchLimit = maxSearchLimit;
	}






	public void setMaxSearchDownload(int maxSearchDownload) {
		this.maxSearchDownload = maxSearchDownload;
	}
	
}
