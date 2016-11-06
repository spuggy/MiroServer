/**
 * 
 */
package uk.co.bluetrail.mobriz.webapp.ajaxImpl;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.ValidatorResources;
import org.springframework.context.MessageSource;
import org.springmodules.validation.commons.DefaultBeanValidator;
import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.*;
import uk.co.bluetrail.mobriz.service.*;
import uk.co.bluetrail.mobriz.serviceDTO.QuestionServiceDTO;
import uk.co.bluetrail.mobriz.util.*;
import uk.co.bluetrail.mobriz.webapp.ajax.AjaxSurveyEditManager;
import uk.co.bluetrail.mobriz.webapp.ajaxDTO.QuestionAjaxDTO;
import uk.co.bluetrail.mobriz.webapp.form.ConstraintForm;
import uk.co.bluetrail.mobriz.webapp.form.OptionForm;
import uk.co.bluetrail.mobriz.webapp.form.QuestionForm;
import uk.co.bluetrail.mobriz.webapp.form.SurveyForm;
import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;
//
  

/**
 * @author Richard Spence
 *
 */
public class AjaxSurveyEditManagerImpl implements AjaxSurveyEditManager{
	
	protected final Log log = LogFactory.getLog(getClass());	   
	private DefaultBeanValidator validator ; 
	private ValidatorResources resources = null ;
	private ConstraintManager constraintManager ;
	private OptionManager optionManager ;
	private QuestionManager questionManager ;
	private SurveyManager surveyManager ;
	private UserManager userManager ;
	private SurveyResponseManager surveyResponseManager;
	private User currentUser ;

	
	public static final int STATUS_VALIDATION_ERROR = 2 ;
	public static final int STATUS_OTHER_ERROR = 3 ;
	public static final int STATUS_OK = 1 ;
	
	private static final String NONESELECTED = "0";
	private static final int SAVE = 0;
	private static final int PUBLISH = 1;
	
	private int maxSurveyLength ;
	
		
    private MessageSource messageSource = null;
	private int maxQuestionLength;
	private int maxQuestionShortNameLength;
	private int maxOptionLength;
	private MiroResponseManager miroResponseManager;
	
	private static Long defaultLong = null;

	
	static {
        ConvertUtils.register(new CurrencyConverter(), Double.class);
        ConvertUtils.register(new DateConverter(), Date.class);
        ConvertUtils.register(new DateConverter(), String.class);
        ConvertUtils.register(new LongConverter(defaultLong), Long.class);
        ConvertUtils.register(new IntegerConverter(defaultLong), Integer.class);
        ConvertUtils.register(new TimestampConverter(), Timestamp.class);
    }

	
	
	 public void setSurveyResponseManager(SurveyResponseManager surveyResponseManager) {
		this.surveyResponseManager = surveyResponseManager;
	}

	public void setMessageSource(MessageSource messageSource) {
	        this.messageSource = messageSource;
	}

	 public User getCurrentUser() {
	    	
	    	
	    	SecurityContext sc = SecurityContextHolder.getContext() ;
	  	  
	    	
	    	try { 
	    	
	    		currentUser = (User)  sc.getAuthentication().getPrincipal() ;
	    		return currentUser ;
	    	} catch (Exception e) {
	    		return null;
	    	}
	    	
	    	
	    	
	 } 
	 
	
	ValidatorResources getResources() {
		
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
	
		
		//get the struts validator resources from the servlet context 
		ServletContext servletContext = ctx.getServletContext() ;		
		ValidatorResources resources = (ValidatorResources) servletContext.getAttribute("org.apache.commons.validator.VALIDATOR_RESOURCES") ;
	
		return resources ;
	}
	
	public void setUserManager(UserManager userManager){
		this.userManager = userManager;		
	}

	public void setSurveyManager(SurveyManager surveyManager){
		this.surveyManager = surveyManager;		
	}
	
	public void setQuestionManager(QuestionManager questionManager){
		this.questionManager = questionManager;		
	}
	
	
	public void setOptionManager(OptionManager optionManager){
		this.optionManager = optionManager;
	}

	public void setConstraintManager(ConstraintManager constraintManager){
		this.constraintManager = constraintManager;
	}
	
	/**
     * @see uk.co.bluetrail.mobriz.util.ConvertUtil#convert(java.lang.Object)
     */
    public Object convert(Object o) throws Exception {
       	 	
    	return ConvertUtil.convert(o);
    }
    // does the convert but stamps the record too.
   

//gets all the questions for a survey as an array
	public HashMap getQuestions(String surveyId) throws Exception{
		
		BTLAssert(!surveyId.equals(""),"Survey Id is blank  ");
		
		List questionList = questionManager.getQuestions(new Long(surveyId));

		Iterator itr = questionList.iterator() ;
		//QuestionForm qForm = null ;
		QuestionAjaxDTO questionAjaxDTO =  null ; 
		Question question ;
		HashMap qHash = new HashMap() ;
		int i=0;
		//QuestionForm[] qForms = new QuestionForm[questionList.size()];
		while(itr.hasNext()){
			
			question = (Question) itr.next();
			//qForm = (QuestionForm) convert(question);
			questionAjaxDTO = buildQuestionDTO(null,question,null,null);
		    qHash.put(question.getId().toString(),  questionAjaxDTO)  ;					
		}
				
		return qHash;
	}
	
	//builds a question DTO from the 3 potential questions
	private QuestionAjaxDTO buildQuestionDTO(Survey survey, Question question,Question prevQuestion,Question nextQuestion) throws Exception {
		
		QuestionForm qForm = null ;						
		qForm = (QuestionForm) convert(question);
		
		
		OptionForm[] oForms = null ; 
		if(question != null) {
			SortedSet options = question.getSortedOptions(true) ;
			
			if(options ==null || options.size() ==0) {
				//do nowt
			} else {
				oForms = new OptionForm[options.size()] ;
				Iterator itr = options.iterator() ;
				int i = 0;
				while(itr.hasNext()) {
					Option option = (Option) itr.next() ; 
					oForms[i++] = (OptionForm) convert(option);
				}			
			}
		}
		
			
		ConstraintForm cForm = null ; 
		if(question !=null){
			Set constraints = question.getConstraints(true) ;
			if(constraints ==null || constraints.size() ==0 ) {
				//do nowt
			} else {
				cForm = (ConstraintForm) convert(constraints.iterator().next());				
			}
		}
		
		//copy over survey stuff
		SurveyForm sForm = (SurveyForm) convert(survey) ;
		
		
		//create the DTO and copy over remaining fields		
		QuestionAjaxDTO questionAjaxDTO = new QuestionAjaxDTO(sForm, qForm,oForms,cForm);
		
		
		//finally the status
		questionAjaxDTO.setStatus(STATUS_OK);
		
		
		
		
		return questionAjaxDTO;
	}
	
	//grabs an individual question using a qid
	public QuestionAjaxDTO getQuestion(String qid) throws Exception{
		
		BTLAssert(!qid.equals(""),"qid is blank");
		
		Question question = questionManager.getQuestion(qid);
		
		return buildQuestionDTO(null,question,null,null);
	}
	
	private void validateDTO(QuestionAjaxDTO questionDTO){
		
		//validates the DTO usiing stuts validation plus
		//some custom bits to build a list of errors
		
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
			
		
		//create list for the errors
		ArrayList errorMessages = new ArrayList();
		
		
		
		
		
		
		QuestionForm qForm = questionDTO.getQForm() ; 
		ConstraintForm cForm = questionDTO.getCForm() ; 
		
		//set up a list of questions to validate the branches.
		List liveQuestions = null ;
		
		
		//create an empty form if we didn't get one
		
		if(qForm == null) {
			qForm = new QuestionForm() ; 
		} else {
			//if we have aform then go get alits of questions to validate the branches 
			 liveQuestions = questionManager.getQuestions(new Long(qForm.getSurvey_id()));
		}
		
		Object[] params = new Object[1];
		params[0] = qForm.getQTxt();
		
		//check the branch points to a live question
		if(!validBranch(qForm.getBranch_jquestion_id(),liveQuestions)){
			errorMessages.add(messageSource.getMessage("errors.badquestionbranch",params, request.getLocale()));
								
			
		}
				
		
		//validate the constraint form 
		if(cForm == null) {		
			validateForm("constraintForm", questionDTO.getCForm() , errorMessages) ; 
		}
			
		//validate the question form
		validateForm("questionForm", questionDTO.getQForm() , errorMessages) ; 		
		
		
		
		//validate all the optios 
		OptionForm[] oForms = questionDTO.getOForms() ; 
		 
		if(oForms !=null) {
			for(int i = 0 ; i < oForms.length  ; i++) {
				//check all fields
				validateForm("optionForm",oForms[i],errorMessages);
				//check the branch points to a valid questions
				if(!validBranch(oForms[i].getJQuestion_id(),liveQuestions)){					
					params = new Object[1] ;
					params[0] = oForms[i].getOText() ;						
					errorMessages.add(messageSource.getMessage("errors.badoptionbranch",params, request.getLocale()));					
				}
				
			}			
		}
	
		
		
		
		
		//other non struts validation
		//make sure radios and checkboxes have options on em.	
		boolean hasAtLeastOneOption = false ; 
		if(questionDTO.getLiveOptionCount() == 0 ) {			
			hasAtLeastOneOption = false ;
		} else {
			hasAtLeastOneOption = true ;
		}
		
		
		
		if( qForm.getQType() != null && 
			(qForm.getQType().equals(Question.QTYPE_RADIO) ||	qForm.getQType().equals(Question.QTYPE_CHECKBOX)) && 
			hasAtLeastOneOption ==false	){			
									
			errorMessages.add(messageSource.getMessage("errors.nooptions",null,request.getLocale()));
			
		}
	
		
		
		
		
		if(errorMessages.size() > 0 ) {
			questionDTO.setErrorMessages(errorMessages) ;
			questionDTO.setStatus(STATUS_VALIDATION_ERROR) ; 
		} else {
			questionDTO.setErrorMessages(null) ;
			questionDTO.setStatus(STATUS_OK) ;
		}
		
		
		
		
		 
		
	}
	
	
	/**
	 * @param branch_id
	 * @param liveQuestions
	 * @return 
	 * function to skim thru the question to check f branches are ok.
	 */
	private boolean validBranch(String branch_id,List liveQuestions){
		
		//if it doint point anywhere then it must be true.
		if(branch_id == null || branch_id.equals("") || branch_id.equals("0") || branch_id.equals("null")) {
			return true ;
		}
		
		if(branch_id != null && liveQuestions == null){
			//has to be bougs
			return true ;
		}
		
		Iterator itr = liveQuestions.iterator() ;
		
		
		Question question = null ; 
		while(itr.hasNext()) {
			question = (Question) itr.next() ;
			
			if(branch_id.equals(question.getId().toString())){
				return true ; 
			}
			
		}
		
		
		return false ; 
	}
	
	
	
	/**
	 * @param formName
	 * @param bean
	 * @param errorMessages
	 * runs validator on a form.
	 */
	private void validateForm(String formName, Object bean , ArrayList errorMessages){
				
	
		if(formName.equals("surveyForm")) {
			SurveyForm surveyForm = (SurveyForm) bean ;
			if(surveyForm.getTitle() ==null || surveyForm.getTitle().equals("")){
				errorMessages.add("Survey title is required") ;
			} else {				
				if(surveyForm.getTitle().length() > this.maxSurveyLength ) {
					errorMessages.add("Survey title cannot be longer than " + this.maxSurveyLength + " characters") ;
				}
			}
				
			return ;
			
		}
		if(formName.equals("questionForm")) {
			
			QuestionForm questionForm = (QuestionForm) bean ;
			
			if(questionForm.getQTxt().trim().equals("")) {
				errorMessages.add("The question text is required") ;
			}
			
			
			if(questionForm.getQTxt().length() > this.maxQuestionLength) {
				errorMessages.add("The question text is required and cannot be longer than " + this.maxQuestionLength + " characters") ;
			}
			
			if(questionForm.getShortname().trim().equals("")) {
				errorMessages.add("The shortname is required") ;
			}
			
			if(questionForm.getShortname().length() > this.maxQuestionShortNameLength) {
				errorMessages.add("The short name cannot be longer than " + this.maxQuestionShortNameLength + " characters") ;
			}
			
			if(questionForm.getQType().equals("")){					
				errorMessages.add("The question type is required") ;
			}
			
			if(questionForm.getQType().equals(Question.QTYPE_LOOKUP) && questionForm.getQMeta().equals("0")){					
				errorMessages.add("You must select a lookup type") ;
			}
			
			
			return;
	
		}
		if(formName.equals("optionForm")) {
	
			OptionForm optionForm = (OptionForm) bean ;
			
			if(optionForm.getOText().trim().equals("")) {
				errorMessages.add("The option text is required") ;
			}
			
			
			if(optionForm.getOText().length() > this.maxOptionLength) {
				errorMessages.add("Option cannot be longer than " + this.maxOptionLength + " characters") ;
			}
			
			
			
		}
		if(formName.equals("constraintForm")) {
			
			ConstraintForm constraintForm = (ConstraintForm) bean ;
			
			if(constraintForm.getCType().equals("")){					
				errorMessages.add("The constraint type is required") ;
			}
			
			
		}
		
		
		
		
		
	}
	
	
    	
	
	
	
	/* 
	 * 
	 * Save a new question
	 *  
	 * @see uk.co.bluetrail.mobriz.webapp.ajax.AjaxSurveyEditManager#saveQuestion(uk.co.bluetrail.mobriz.webapp.ajaxDTO.QuestionAjaxDTO)
	 */
	
	public QuestionAjaxDTO saveNewQuestion(QuestionAjaxDTO questionDTO) throws Exception{
	
		    
		
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		User user = getUser(request.getSession());
		
		log.info(user.getUsername()+": saveQuestion");
		
		validateDTO(questionDTO) ;
		
		//quit out if bad validation problems
		if (questionDTO.getStatus() != STATUS_OK) {
			return questionDTO;   
		}
		
		
		
		Question newQuestion = null ;
		newQuestion = (Question) convert(questionDTO.getQForm());		
			
		
			
		List constraintList = convertConstraints(questionDTO.getCForm());		
		List optionList = convertOptions(questionDTO.getOForms()) ;
	
		
		
		//do the save
		questionManager.saveNewQuestion(new QuestionServiceDTO(newQuestion,questionDTO.getTargetId(), optionList,constraintList,getUser(request.getSession())));
		
		
		//reload it again to get the options loaded
		newQuestion = questionManager.getQuestion(newQuestion.getId().toString());
		
		//grab the latest survey and send it back- there is proabbaly soem crafy hibernate mapping thing
		//to get this but its foxed me i can tell you.
		Survey survey = surveyManager.getSurvey(newQuestion.getSurvey_id().toString());
		return  buildQuestionDTO(survey,newQuestion,null,null); 
		
		
	}
	
	 /**
	 * @param b
	 */
	private void BTLAssert(boolean b,String message) {
		
		if(!b) { 
			StringBuffer sb = new StringBuffer() ;
			sb.append("*******************************************\n");
			sb.append("* ASSERT FIALED: " +  message +"           \n");
			sb.append("*******************************************\n");
			log.fatal(sb.toString());
		}
		
	}


	
	
	
	
	 /**
     * Convenience method to get the userForm from the session
     *
     * @param session the current user's session
     * @return the user's populated form from the session
     */
    public User getUser(HttpSession session) {
        return getCurrentUser();
    }


	
	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.webapp.ajax.AjaxSurveyEditManager#deleteQuestion(uk.co.bluetrail.mobriz.webapp.ajaxDTO.QuestionAjaxDTO)
	 */
	public String  deleteQuestion(QuestionAjaxDTO questionDTO) throws Exception {
		
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		User user = getUser(request.getSession());
		
		log.info(user.getUsername()+": deleteQuestion" + questionDTO.getQForm().getId());
				
		BTLAssert(questionDTO.getQForm() != null,"deleteQuestion questionDTO.QFrom is null");
		
		//grab the question first as we want to send back stuff about this
//		//grab the latest survey and send it back- there is proabbaly soem crafy hibernate mapping thing
		//to get this but its foxed me i can tell you.
		Question question = questionManager.getQuestion(questionDTO.getQForm().getId());
		
		QuestionServiceDTO questionServiceDTO = new QuestionServiceDTO(question,null, null,null,getUser(request.getSession())); 
		questionManager.removeQuestion(questionServiceDTO) ;
		
		
		//whats happeing here is we are passing back thru the DTOs a question and a target id.
		//the targetid is the id of the question we just deleted and question form going down is the
		//last question - whcih could null if we have dleeted everyting.
		Survey survey = surveyManager.getSurvey(question.getSurvey_id().toString());
		
		QuestionAjaxDTO questionAjaxDTO =  buildQuestionDTO(survey,questionServiceDTO.getQuestion(),null,null); 
		
		questionAjaxDTO.setTargetId(questionServiceDTO.getTargetId());
		
		//return questionAjaxDTO ;
		return survey.getFirstQuestion_id().toString() ;
	}

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.webapp.ajax.AjaxSurveyEditManager#updateQuestion(uk.co.bluetrail.mobriz.webapp.ajaxDTO.QuestionAjaxDTO)
	 */
	public QuestionAjaxDTO updateQuestion(QuestionAjaxDTO questionDTO) throws Exception {

		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		User user = getUser(request.getSession());
		
		log.info(user.getUsername()+": updateQuestion-" + questionDTO.getQForm().getId());
				
		validateDTO(questionDTO) ;
		
		//quit out if bad validation problems
		if (questionDTO.getStatus() != STATUS_OK) {
			return questionDTO;   
		}
		
				
		Question question = null ;
		question = (Question) convert(questionDTO.getQForm());		
			
			
		List constraintList = convertConstraints(questionDTO.getCForm());		
		List optionList = convertOptions(questionDTO.getOForms()) ;
	
		
		
		//do the save
		questionManager.updateQuestion(new QuestionServiceDTO(question,questionDTO.getTargetId(), optionList,constraintList,getUser(request.getSession())));
		
		
		//reload it again to get the options loaded
		//also in the next line we probably don't have to reload the survey 
		//as we have it here in the question ..
		question = questionManager.getQuestion(question.getId().toString());
	
		
		//grab the latest survey and send it back- there is proabbaly soem crafy hibernate mapping thing
		//to get this but its foxed me i can tell you.
		
		Survey survey = surveyManager.getSurvey(question.getSurvey_id().toString());
		return  buildQuestionDTO(survey,question,null,null); 
		
	}

	
	//converts the constraintForms into constraints
	private List convertConstraints(ConstraintForm cForm) throws Exception{
		List constraintList = null ; 
		if(cForm !=null && !cForm.getCType().equals(NONESELECTED)) {
			constraintList = new ArrayList();
			Constraint constraint = (Constraint) convert(cForm);
			System.out.println("convertConstraints vvfffff" + constraint.getVersion());
			
			constraintList.add(constraint);
		}
		
		
		return constraintList ;
	}
	
	//converts the optionForms into options
	private List convertOptions(OptionForm[] options) throws Exception{
	
		//now add any options
		
		List optionList = null ; 
		Option option = null ; 
		if(options != null && options.length > 0 ) {
			optionList = new ArrayList();
			for(int i = 0 ; i < options.length ; i++) {
				option = (Option) convert(options[i]) ;
				optionList.add(option);
			}				
		} 
		System.out.println("9:optionList"+ optionList);
		return optionList ;
	}

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.webapp.ajax.AjaxSurveyEditManager#saveInsertQuestion(uk.co.bluetrail.mobriz.webapp.ajaxDTO.QuestionAjaxDTO)
	 */
	public QuestionAjaxDTO saveInsertQuestion(QuestionAjaxDTO questionDTO) throws Exception {

		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		User user = getUser(request.getSession());
		
		log.info(user.getUsername()+": saveInsertQuestion");
				
		validateDTO(questionDTO) ;
		
		//quit out if bad validation problems
		if (questionDTO.getStatus() != STATUS_OK) {
			return questionDTO;   
		}
		
		
		
		Question newQuestion = null ;
		newQuestion = (Question) convert(questionDTO.getQForm());		
			
			
		List constraintList = convertConstraints(questionDTO.getCForm());		
		List optionList = convertOptions(questionDTO.getOForms()) ;
	
		
		QuestionServiceDTO questionServiceDTO = new QuestionServiceDTO(newQuestion,questionDTO.getTargetId(), optionList,constraintList,getUser(request.getSession())); 
		
		//do the save
		questionManager.saveInsertQuestion(questionServiceDTO);

		//reload it again to get the options loaded
		//probably don't have to load the survey after this load 
		// as we have it in the question
		newQuestion = questionManager.getQuestion(newQuestion.getId().toString());
	
		
		
		//grab the latest survey and send it back- there is proabbaly soem crafy hibernate mapping thing
		//to get this but its foxed me i can tell you.
		Survey survey = surveyManager.getSurvey(newQuestion.getSurvey_id().toString());

		
		QuestionAjaxDTO questionAjaxDTO =  buildQuestionDTO(survey,newQuestion,null,null);
		
		questionAjaxDTO.setTargetId(questionDTO.getTargetId());
		
		return  questionAjaxDTO ; 
		
	}

	public String submitMiroResponse(String sid, String[] qids, String first[], String[] second) throws Exception {
		
		try { 
		
			SurveyResponse surveyResponse = new SurveyResponse() ;
			
			
			
			surveyResponse.setSurvey_id(new Long(sid)) ;
			StringBuffer questionTrail = new StringBuffer() ;
			StringBuffer answerTrail = new StringBuffer() ;
			
			String SR_SEP = "~";
			
			for (int i = 0; i < qids.length - 1; i++) {
				
				
				questionTrail.append(qids[i]);
				questionTrail.append(SR_SEP);
				answerTrail.append(first[i]);
				answerTrail.append(";");
				answerTrail.append(second[i]);
				answerTrail.append(SR_SEP);
			}
			
			
			
			questionTrail.append(qids[qids.length - 1]);
						
			
			answerTrail.append(first[qids.length - 1]);
			answerTrail.append(";");
			answerTrail.append(second[qids.length - 1]);
			
			
			surveyResponse.setAnswer_trail(answerTrail.toString());
			surveyResponse.setQuestion_trail(questionTrail.toString());

			
			
			SurveyElementUtil.timeStamp((SurveyElement) surveyResponse, getCurrentUser());

			if(miroResponseManager.isValid(surveyResponse)) {
			
				surveyResponseManager.saveSurveyResponse(surveyResponse);

			} else {
				return Constants.MIRO_INVALID;
			}
			
		} catch (Exception e) {

            log.error(e);
			return Constants.MIRO_EXCEPTION;
		}
		
		return Constants.MIRO_OK; 

	}

	

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.webapp.ajax.AjaxSurveyEditManager#updateSurvey(uk.co.bluetrail.mobriz.webapp.ajaxDTO.SurveyAjaxDTO)
	 */
	public QuestionAjaxDTO updateSurvey(QuestionAjaxDTO surveyDTO) throws Exception {
	
	
			return updateSurvey(surveyDTO,SAVE) ; 

		
	}
	

	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.webapp.ajax.AjaxSurveyEditManager#updateSurvey(uk.co.bluetrail.mobriz.webapp.ajaxDTO.SurveyAjaxDTO)
	 */
	public QuestionAjaxDTO publishSurvey(QuestionAjaxDTO surveyDTO) throws Exception {
	
	
			return updateSurvey(surveyDTO,PUBLISH) ; 

		
	}

	
	private QuestionAjaxDTO updateSurvey(QuestionAjaxDTO surveyDTO, int action)throws Exception {
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
				
		validateSurveyDTO(surveyDTO) ;
		
		
		//quit out if bad validation problems
		if (surveyDTO.getStatus() != STATUS_OK) {
			return surveyDTO;   
		}
		
		SurveyForm surveyForm = surveyDTO.getSForm();
		
		Survey survey = new Survey();
		
		
		
		
		
		BeanUtils.copyProperties(survey, surveyForm);
		
		
		//copy over the survey details to the survey form
		String[] users = surveyForm.getUserNames() ; 
	    


		
		 		 
			
				 
		
		User user = getUser(request.getSession());
		//do the save or publish 
		
		
		
		switch(action) {
		
			case SAVE :   
				surveyManager.saveSurvey(survey,users,user);
				break ; 
			case PUBLISH : 
				surveyManager.publishSurvey(survey,user);
				break ;
		    default:		    	
		    	throw new Exception("Unkown action " + action);
		}
		
		surveyForm.setUserNames(null);
		
		//copy the survey details back
		BeanUtils.copyProperties(surveyForm, survey);
		
		return  surveyDTO ;
		
		
	}
	
	

	/**
	 * @param surveyDTO
	 */
	private void validateSurveyDTO(QuestionAjaxDTO surveyDTO) {
		surveyDTO.setStatus(STATUS_OK);
		
	}

	public void setValidator(DefaultBeanValidator validator) {
		this.validator = validator;
	}

	public void setMaxSurveyLength(int maxSurveyLength) {
		this.maxSurveyLength = maxSurveyLength;
	}

	public void setMaxQuestionLength(int maxQuestionLength) {
		this.maxQuestionLength = maxQuestionLength;
	}

	public void setMaxQuestionShortNameLength(int maxQuestionShortNameLength) {
		this.maxQuestionShortNameLength = maxQuestionShortNameLength;
	}

	public void setMaxOptionLength(int maxOptionLength) {
		this.maxOptionLength = maxOptionLength;
	}

	/**
	 * @param miroResponseManager the miroResponseManager to set
	 */
	public void setMiroResponseManager(MiroResponseManager miroResponseManager) {
		this.miroResponseManager = miroResponseManager;
	}
	
}
