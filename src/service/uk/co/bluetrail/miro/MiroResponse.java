package uk.co.bluetrail.miro;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.co.bluetrail.mobriz.model.*;

import java.util.*;


/**
 * 
 * Wraps Mobriz Survey Response and calulates Results and pages
 * 
 * @author Richard
 *
 */
public class MiroResponse  {


     private final Log log = LogFactory.getLog(MiroResponse.class);
	 private Long testId = null;
	 private int[] results = null;
	 private String[] resultLetters = null;
     public  int extroIntraValue = 0 ;
     public String extroIntroStrata = null;
	 private MiroProject miroProject;
	 private String miroReportName ;

	 
	 /**
	 * 
	 */
	private boolean resultsAttached[] = null ;
	
	 private String firstName = null ;
	 private String lastName = null ;
	 private String practitionerName;
	 private String practitionerEmail;
	 private String practitionerTelNo;
	 private String[] practitionerAddress = null;
	 
	 
	private int engagedScore;

	private int excessScore;
	private int latentScore ;
	private SurveyResponse surveyResponse;
	private List <Survey> surveys;
    private Survey survey ;
	private LinkedHashMap mostMiroTotals;
	private LinkedHashMap leastMiroTotals;
	private Setting miroLetters;

	private int testOffset;

	

	private String company;
	private String webaddress;




    public int getTestVersion() {

        if(this.surveyResponse.getAnswer_trail().length() > 62) {
            return 11   ;
        }  else {
            return 10  ;
        }


    }
	

	/**
	 * @hibernate.property 
	 * @return the webaddress
	 */
	public String getWebaddress() {
		
		if(webaddress == null) {
			return "" ;
		}
		
		return webaddress;
	}

	/**
	 * @param webaddress the webaddress to set
	 */
	public void setWebaddress(String webaddress) {
		this.webaddress = webaddress;
	}

	/**
	 * @hibernate.property 
	 * @return the company
	 */
	public String getCompany() {
		if(company==null) {
			return "";
		} 
		
		return company;
	}

	private void resetMiroTotals() {
		
		mostMiroTotals = new LinkedHashMap() ;
		leastMiroTotals = new LinkedHashMap() ;
		
		String[] miroLettersArray = miroLetters.getSettingValue().split(";");
		for(int i = 0 ; i < miroLettersArray.length;i++) {
			leastMiroTotals.put(miroLettersArray[i], new Integer(0));
			mostMiroTotals.put(miroLettersArray[i], new Integer(0));
		}
			
		
	}

    public void calculateResults() {

        if(this.results!=null) {
           return;
        }  else {
           this.forceCalculateResults();
        }

    }


	
	public void forceCalculateResults() {
		

		resetMiroTotals();
		
		Question question = null;
	
		
		User user = surveyResponse.getUser() ; 
		
				
		Long qid = survey.getFirstQuestion_id();
	
		question = (Question) survey.getQuestionMap().get(qid); 
	
		String rawAnswer = null;
	
		String mostAnswer = null ;
		String leastAnswer = null ;
		Integer totalLeast = null ;
		Integer totalMost = null ;
		
		HashMap tieBreakers = new HashMap();
		HashMap resultMap = new HashMap();
		HashMap resultMapWorker = new HashMap();
		
		log.debug(this.surveyResponse.getId() + ": Starting processing id = " );
		
		boolean isTieBreaker = false;
		
		while (question != null && !question.getQMeta().equalsIgnoreCase("Q11Inst")) {
	
			rawAnswer = surveyResponse.getAnswer(question);
	
			String[] rawAnswers = rawAnswer.split(";");
			String[] mostAnswers = rawAnswers[0].split("#");
			String[] leastAnswers = rawAnswers[1].split("#");
			
			mostAnswer = mostAnswers[1].trim();
			leastAnswer = leastAnswers[1].trim();
			
			if(question.getQMeta().trim().toLowerCase().equals("tie")) {
				log.debug(this.surveyResponse.getId() + ": Ignoring tie breaker qid= " + question.getId() );
				tieBreakers.put(question.getShortname(), mostAnswer);
				isTieBreaker = true;
			} else {
				isTieBreaker = false;
			}
			
			log.debug(this.surveyResponse.getId() + ": Adding most " + mostAnswer + " for qid= " + question.getId() );
			totalMost = (Integer) mostMiroTotals.get(mostAnswer) ;
			if(isTieBreaker) {
				totalMost = totalMost + 2 ;
			} else {
				totalMost++;
			}
			
			mostMiroTotals.put(mostAnswer,totalMost);
			log.debug(this.surveyResponse.getId() + ": Adding least " + leastAnswer + " for qid= " + question.getId() );
			totalLeast = (Integer) leastMiroTotals.get(leastAnswer) ;
			if(isTieBreaker) {
				totalLeast = totalLeast + 2;
			} else {
				totalLeast++;	
			}
			
			leastMiroTotals.put(leastAnswer,totalLeast);
			
			
				
			question = (Question) survey.getQuestionMap().get(question.getJQuestion_id());
		}
	
		String[] miroLettersArray = miroLetters.getSettingValue().split(";");
	
		int totalScore = 0 ;
		for(int i = 0 ; i < miroLettersArray.length;i++) {
			totalMost = (Integer) mostMiroTotals.get(miroLettersArray[i]) ;
			totalLeast = (Integer) leastMiroTotals.get(miroLettersArray[i]) ;
			totalScore = (totalMost.intValue()-totalLeast.intValue())+ this.getTestOffset();
			resultMap.put(miroLettersArray[i], new Integer(totalScore));
			resultMapWorker.put(miroLettersArray[i], new Integer(totalScore));
		}

		int loopBreaker = 0 ;
		while(removeEqualScores(resultMapWorker,tieBreakers) && loopBreaker < 100){
			loopBreaker++;
		}
		
		if(loopBreaker >=100) {
			log.error(this.surveyResponse.getId() + ": loop = "  +  loopBreaker );
			throw new RuntimeException("Loopbreaker over 100!!!");
		}
		
		log.debug(this.surveyResponse.getId() + "creating sorted letters");
		
		populateResultArrays(resultMap,resultMapWorker);


        log.debug("calculating miro 11 results ..");
        if(question != null) {
            //jump over the intersticial page
            question = (Question) survey.getQuestionMap().get(question.getJQuestion_id());
        }

        //now calculate miro11 results
        while (question != null ) {

            rawAnswer = surveyResponse.getAnswer(question);

            String[] rawAnswers = rawAnswer.split("#");
            String answer = rawAnswers[1];

            if(answer.equalsIgnoreCase("plus")) {
                extroIntraValue++ ;
            } else {
                extroIntraValue--;
            }


            question = (Question) survey.getQuestionMap().get(question.getJQuestion_id());
        }

        this.extroIntroStrata = getExtraIntroStr(this.extroIntraValue) ;

        log.debug("Finished calculating results ..");
	}

    public String getExtraIntroStr(int extroIntro) throws MiroException {


        if(extroIntro >= 0  &&  extroIntro  <= 5 )  {
            return "LEX"  ;
        }

        if(extroIntro >= 6  &&  extroIntro  <= 14 )  {
            return "MEX" ;
        }

        if(extroIntro >= 15  &&  extroIntro  <= 19 )  {
            return "HEX";
        }


        if(extroIntro <= -1  &&  extroIntro   >=-5 )  {
            return "LIN";
        }

        if(extroIntro <= -6  &&  extroIntro   >=-14 )  {
            return "MIN";
        }

        if(extroIntro <= -15  &&  extroIntro   >=-19 )  {
            return "HIN";
        }


        throw new MiroException("Could not find ExtraInto for value " + extroIntro);

    }


    private int getTestOffset() {
		return testOffset;
	}

	private void populateResultArrays(HashMap resultMap, HashMap resultMapWorker) {
	
		this.results = new int[4] ;
		this.resultLetters = new String[4];
		
		
		int maxValue = 0 ;
		String maxletter = "";
		Iterator itr = null;
		Integer v = null;
		String key = null;
		for(int i = 0 ; i < results.length ; i++) {
			itr = resultMapWorker.keySet().iterator();
			maxValue = 0 ;
			maxletter = "";
			while(itr.hasNext()){
				key = (String)  itr.next();
				v = (Integer) resultMapWorker.get(key);
				if(v.intValue()>maxValue){
					maxletter = key;
					maxValue = v.intValue();
				}
				
			}
			this.results[i] = ((Integer) resultMap.get(maxletter)).intValue();
			this.resultLetters[i] = maxletter;
			resultMapWorker.put(maxletter, new Integer(-1000));
		}		
		
		
		
	}

	private boolean removeEqualScores(HashMap resultMapWorker, HashMap tieBreakers) {
		
		Iterator itr = resultMapWorker.keySet().iterator();
		String letter = null;
		String key = null;
		while(itr.hasNext()) {
			letter = (String) itr.next();
			Iterator itr2 = resultMapWorker.keySet().iterator();
			while(itr2.hasNext()){
				key = (String) itr2.next();
				if(!key.equals(letter)) {
					if(isEqual(resultMapWorker.get(letter),resultMapWorker.get(key))) {
						
						addTieBreaker(key,letter,resultMapWorker, tieBreakers);
						log.debug(this.surveyResponse.getId() + ": found tie for " + key + " and " + letter );
						return true;
						
						
					}
					
				} 
				
				
			}
		}
		
		log.debug(this.surveyResponse.getId() + ": No Ties found " );
		return false;
	}

	private void addTieBreaker(String key, String letter, HashMap resultMapWorker, HashMap tieBreakers) {
		
		String tieBreaker = (String) tieBreakers.get(key+"-"+letter);
		if(tieBreaker == null) {
			tieBreaker = (String) tieBreakers.get(letter+"-"+key);
		}
		
		if(tieBreaker == null) {
			log.error(this.surveyResponse.getId() + ": tie breaker missing for " + key+"-"+letter);
			return;
		}
		
		Integer total = null;
		String totalKey = null;
		if(tieBreaker.equals(key)){
			totalKey = key ;
		} else {
			totalKey = letter;
		}
		
		total = (Integer) resultMapWorker.get(totalKey) ;
		total++;
		resultMapWorker.put(totalKey,total);
		
		
	}

	private boolean isEqual(Object o1, Object o2) {
		
		Integer a = (Integer) o1 ;
		Integer b = (Integer) o2 ;
		return a.intValue()==b.intValue();
		
		
	}

	
	
	
	/**
	 * @hibernate.property 
	 * @return the latentScore
	 */
	public int getLatentScore() {
		return latentScore;
	}

	/**
	 * @param latentScore the latentScore to set
	 */
	public void setLatentScore(int latentScore) {
		this.latentScore = latentScore;
	}

	/**
	 * @hibernate.property 
	 * @return the engagedScore
	 */
	public int getEngagedScore() {
		return engagedScore;
	}

	/**
	 * @hibernate.property 
	 * @return the excessScore
	 */
	public int getExcessScore() {
		return excessScore;
	}

	/**
	 * @param excessScore the excessScore to set
	 */
	public void setExcessScore(int excessScore) {
		this.excessScore = excessScore;
	}

	public boolean[] getResultsAttached() {
		this.calculateResults();
		if(resultsAttached == null) {
			
			resultsAttached = new boolean[results.length];
			for(int i = 0 ; i < results.length;i++){
				if(isEngaged(results[i]) || isExcess(results[i])) {
					resultsAttached[i] = true;
				} else {
					resultsAttached[i] = false;
				}
			}
		}
		
		return resultsAttached;
		
	}

		public String getFullName() {
			return firstName + " " + lastName ;
		}	
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getTestId() {
				
		return testId;   
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public String[] getPractitionerAddress() {
		return practitionerAddress;
	}

	public void setPractitionerAddress(String[] practitionerAddress) {
		this.practitionerAddress = practitionerAddress;
	}

	public String getPractitionerEmail() {
		return practitionerEmail;
	}

	public void setPractitionerEmail(String practitionerEmail) {
		this.practitionerEmail = practitionerEmail;
	}

	public String getPractitionerName() {
		return practitionerName;
	}

	public void setPractitionerName(String practitionerName) {
		this.practitionerName = practitionerName;
	}

	public String getPractitionerTelNo() {
		return practitionerTelNo;
	}

	public void setPractitionerTelNo(String practitionerTelNo) {
		this.practitionerTelNo = practitionerTelNo;
	}

	
	public void setResultLetters(String[] resultLetters) {
		this.resultLetters = resultLetters;
	}

	public void setResults(int[] results) {
		this.results = results;
	}

	public MiroResponse() {
		// TODO Auto-generated constructor stub
	}

	
	public MiroResponse(List<Survey> surveys, SurveyResponse sr,Setting miroLetters, int testOffset) {

        this.surveyResponse = sr ;
		this.miroLetters = miroLetters;
		this.testOffset = testOffset;
		this.surveys = surveys;
		this.testId = sr.getId();
	}

	
	public boolean isValid() {
		
		getResults();
		
		int lowerRange = testOffset - 2 ;
		int upperRange = testOffset + 2 ;
		int rangeResult = 0 ;
		int overOffsetResult = 0 ;
		
		for(int i = 0 ;i < results.length ; i++) {
			
			if(results[i] >= lowerRange && results[i] <= upperRange){
				rangeResult++ ;
			}
			
			if(results[i] >= testOffset) {
				overOffsetResult++;
			}
			
		}
		
		if(rangeResult==4 || overOffsetResult==4){
			return false;
		} else {
			return true;
		}
		
	}
		

	
	public int[] getResults() {
		this.calculateResults();
		
		return this.results;
		
	}

	public String[] getResultLetters() {
		this.calculateResults();
		return resultLetters;
		
	}
	
	

	public List<MiroPage> getReportPageList() {
	
		MiroPageElement[]pageItems = null;
		
		List<MiroPage> pages = new ArrayList<MiroPage>();
		
		//page0 
        pages.add(MiroPage.create("homepage"));

        pages.add(MiroPage.create("TOC"));


        //page1
		pages.add(MiroPage.create("U2"));
//		page2
		pages.add(MiroPage.create("U3"));
//		page3	
		pages.add(MiroPage.create("U4"));
//		page4

        if (this.isExcess(results[0])) {
            pages.add(MiroPage.create(resultLetters[0] + "1.1"));
        } else {
            pages.add(MiroPage.create(resultLetters[0] + "1"));
        }

       // woo its miro 1.1
        if(this.extroIntroStrata!=null) {
          String key = this.getExtraIntroMappingKey();
          if(key==null) {
              throw new MiroException("could not find ExtroIntraMapping mapping key for " + key);
          }
          pages.add(MiroPage.create(key));

        }

//		page5
		if(this.isEngaged(results[1])) {
			pages.add(MiroPage.create(resultLetters[1]+"2" ));
		} else {
			pages.add(MiroPage.create( resultLetters[0] ));
			
		}
//		page6
		if(this.isEngaged(results[1])) {
			pages.add(MiroPage.create(resultLetters[0]+"-"+resultLetters[1] ));
		} else {
			pages.add(MiroPage.create( resultLetters[1]+"3" ));
		}
//		page7
		MiroPage page = new MiroPage();
		
		if(this.isEngaged(results[2])) {
			page.add(resultLetters[2]+"4") ;
		} else {
			page.add(resultLetters[2]+"5");

		}
		
		if(this.isLatent(results[3])) {
			page.add(resultLetters[3]+"6.1") ;
		} else {
			page.add(resultLetters[3]+"6");
			
		}
		
		pages.add(page);
		
		//page8
		//added by rob 26/08
		if(this.isEngaged(results[0]) && this.isEngaged(results[1])) {
			pages.add(MiroPage.create( resultLetters[0]+"-"+resultLetters[1]+"7"));
		} else {
			pages.add(MiroPage.create( resultLetters[0]+"7"));
		}
		
			
		//page9
		pages.add(MiroPage.create( "U5" ));
		
		//page10
		pages.add(MiroPage.create( "U6" ));
		
		
		//page11
		pages.add(MiroPage.create("U7" ));
		
		
		return pages;
		
		
		
		
	}

    public String getExtraIntroMappingKey() {

        String comboKey = resultLetters[0] + resultLetters[1] + resultLetters[2] + this.extroIntroStrata  ;

        return ExtroIntraMapping.get(comboKey);

    }

    public boolean isExcess(int i) {
		if(i>=this.excessScore) {
			return true;	
		} else {
			return false;	
		}
	}

	public boolean isEngaged(int i) {
		if(i>this.engagedScore && i < this.excessScore) {
			return true;	
		} else {
			return false;	
		}
		
	}

	public boolean isLatent(int i) {
		if(i<=this.latentScore) {
			return true;	
		} else {
			return false;	
		}
		
	}
	
	

	public void setEngagedScore(int engagedScore) {
		this.engagedScore = engagedScore;
		
	}

	/**
	 * @param survey the survey to set
	 */
	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public void init(List<Survey> surveys, SurveyResponse sr, Setting miroLetters, int testOffset) {
		this.surveyResponse = sr ;
		this.miroLetters = miroLetters;
		this.testOffset = testOffset;
		this.surveys = surveys;
		this.testId = sr.getId();


        Iterator<Survey> itr = this.surveys.iterator();

        while(itr.hasNext()) {
            Survey s = itr.next() ;
            if(s.getId().longValue() == (this.surveyResponse.getSurvey_id().longValue())) {
                this.survey = s;
            }
        }

        if(this.survey==null) {
            throw new MiroException("Survey " + this.surveyResponse.getSurvey_id() + " not found");
        }

    }

	public void setMiroProject(MiroProject miroProject) {
		this.miroProject = miroProject;
		
	}

	/**
	 * @hibernate.property 
	 * @return the miroProject
	 */
	public MiroProject getMiroProject() {
		return miroProject;
	}

	public void setMiroReportName(String reportFileName) {
		this.miroReportName = reportFileName;
		
	}

	
	public String getMiroReportName() {
		return miroReportName;
	}

	

	public void setCompany(String company) {
		this.company = company;// TODO Auto-generated method stub
		
	}

	public String getAddressHtml() {
		
		String[] alines = this.getPractitionerAddress();
		
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0 ; i < alines.length;i++){
			if(alines[i] != null && !alines[i].trim().equals("")){
				sb.append(alines[i]);
				sb.append("<br/>");
			}
			
		}
		
		return sb.toString();
	}

	public int[] getResults(double miroGraphAdjustment) {
		
		
		int[] tmpResults = this.getResults().clone() ;
		
		int adjustment = (int) (tmpResults[3] * miroGraphAdjustment) ;
		
		for(int i = 0 ; i < tmpResults.length ; i ++) {
			tmpResults[i] = tmpResults[i] - adjustment;
		}
		
		return tmpResults;
		
	}

	public String toString() {
		
		int[] tmpResults = this.getResults();
		
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < this.results.length ; i ++) {
			sb.append(this.resultLetters[i] + "=" + tmpResults[i]);
		}
		
		return sb.toString();
				
	}

	/**
	 * is the second result engages - used by mirteam
	 */
	public boolean is2ndEngaged() {
		
		return isEngaged(results[1]);
	}



}
