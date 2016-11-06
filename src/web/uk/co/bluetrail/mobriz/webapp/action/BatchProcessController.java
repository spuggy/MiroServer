package uk.co.bluetrail.mobriz.webapp.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import uk.co.bluetrail.miro.MiroResponse;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.service.*;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class BatchProcessController implements Controller {
	 private final Log log = LogFactory.getLog(BatchProcessController.class);
	
	 
	 protected int miroDocLimit;
	 
	 
	 protected SurveyManager surveyManager = null;
	 protected SurveyResponseManager surveyResponseManager = null;
	 protected MiroProjectManager miroProjectManager = null;
	 protected MobrizAlertManager mobrizAlertManager = null;
	 protected UserManager userManager = null;
	 protected MailEngine mailEngine = null;
	 protected String fromEmail ;
	 protected MiroResponseManager miroResponseManager ;
	 protected String reportCreatedTemplateName ;
	 protected MiroTeamReportManager miroTeamReportManager =null;
   
     
	 /**
	 * @param miroProjectManager the miroProjectManager to set
	 */
	public void setMiroProjectManager(MiroProjectManager miroProjectManager) {
		this.miroProjectManager = miroProjectManager;
	}

	private HttpServletRequest request ;
     private HttpServletResponse response ; 

     
	

	/**
	 * @return the miroTeamManager
	 */
	public MiroTeamReportManager getMiroTeamReportManager() {
		return miroTeamReportManager;
	}

	/**
	 */
	public void setMiroTeamReportManager(MiroTeamReportManager miroTeamReportManager) {
		this.miroTeamReportManager = miroTeamReportManager;
	}

	/**
	 * @param reportCreatedTemplateName the reportCreatedTemplateName to set
	 */
	public void setReportCreatedTemplateName(String reportCreatedTemplateName) {
		this.reportCreatedTemplateName = reportCreatedTemplateName;
	}

	public void setSurveyManager(SurveyManager surveyManager) {
	       this.surveyManager = surveyManager;
	 }

	 public void setSurveyResponseManager(SurveyResponseManager surveyResponseManager) {
			this.surveyResponseManager = surveyResponseManager;
	 }

	
	 
	 
    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) 
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }
      
  
        ArrayList batchProcessResults = new ArrayList();   
        
        this.request = request;
        this.response = response ;

			try {
				processMiroReports(batchProcessResults, miroResponseManager.getMiroReportPath(RequestUtil.getAppURL(request)));
				
			} catch (Exception e) {
				log.error("Exception processing individual pdfs: " + e.toString());
				batchProcessResults.add("Exception processing individual pdfs: " + e.toString());
			}
			
			
			try {
				processMiroTeamReports(batchProcessResults, miroResponseManager.getMiroReportPath(RequestUtil.getAppURL(request)));
					
			} catch (Exception e) {
				log.error("Exception processing team pdfs: " + e.toString());
				batchProcessResults.add("Exception processing team pdfs: " + e.toString());
				
				
			}
			
			
			
			
			
		
		
		
		HashMap model = new HashMap();
		model.put("batchProcessResults", batchProcessResults);
		
        return new ModelAndView("batchProcessResults",model);
    }

    private void processMiroTeamReports(ArrayList batchProcessResults,String filePath) {
	
    	
    	log.debug("processing miro teams reports ");
    	List unprocessedTeams = miroTeamReportManager.getUnprocessedTeams(miroDocLimit);
            	    	
        
    	MiroTeam mt = null;
    	
    	Iterator itr = unprocessedTeams.iterator();
    	if(!itr.hasNext()) {
    		log.debug("No miro reports to process");
    	}
    	
    	MiroResponse mr = null;
    	
    	
    	while(itr.hasNext()){
    		mt = (MiroTeam) itr.next();
    		
    		try {
    			if(miroTeamReportManager.createPDF(mt,filePath)){  
    					sendMiroTeamEmails(mt);
    			}
    			batchProcessResults.add("Miro Team report created" + mt.getMiroTeamName() + "id=" + mt.getId() );
    			
    		    
    		} catch (Exception e) {
    			batchProcessResults.add("ERROR Miro Team processing " + mt.getMiroTeamName() + "id=" + mt.getId()+ ": " + e.getMessage());
    		}
    		
    				
    	}
    	
    	log.debug("end processing miro team reports ");
    	
    	
		
	}

	private void sendMiroTeamEmails(MiroTeam mt) {
		log.debug("sending miro emails");
		
		try{
			log.debug("sending email to " + mt.getPractitionerEmail() + " for miro team " + mt.getId());
			sendAlertEmail(mt);
			log.debug("sending alert emails");
		} catch(Exception e) {
			log.error("Error sending email to " +  mt.getPractitionerEmail() + " for surveyResponse " + mt.getId() + " - "+ e.toString());
		}
	
		log.debug("finished sending alert emails");
		
	}

	private void processMiroReports(ArrayList batchProcessResults, String filePath) {
	
    	log.debug("processing miro reports ");
    	List unprocessedResponses = miroResponseManager.getUnprocessedMiroResponses(miroDocLimit);
            	    	
        
    	SurveyResponse sr = null;
    	
    	Iterator itr = unprocessedResponses.iterator();
    	if(!itr.hasNext()) {
    		log.debug("No miro reports to process");
    	}
    	
    	MiroResponse mr = null;
    	
    	
    	while(itr.hasNext()){
    		sr = (SurveyResponse) itr.next();
    		mr = new MiroResponse();
    	try {
    		
    		if(miroResponseManager.createPDF(sr,mr,filePath)){  
    			sendMiroEmails(mr);
				//sendFreeMiroAssessmentEmails(mr);
    		} 
    		batchProcessResults.add("Individual Report created for " + sr.getUser().getFullName() + "id=" + sr.getUser().getId());
			
		    
		} catch (Exception e) {
			batchProcessResults.add("ERROR Individual Report "  + sr.getUser().getFullName() + "id=" + sr.getUser().getId() + ": " + e.getMessage());
		}
		
    				
    	}
    	
    	log.debug("end processing miro reports ");
		
	}

	private void sendMiroEmails(MiroResponse mr) {
		log.debug("sending miro emails");
		
			try{
				log.debug("sending email to " + mr.getPractitionerEmail() + " for surveyResponse " + mr.getTestId());
				sendAlertEmail(mr);
				log.debug("sending alert emails");
			} catch(Exception e) {
				log.error("Error sending email to " +  mr.getPractitionerEmail() + " for surveyResponse " + mr.getTestId() + " - "+ e.toString());
			}
		
		log.debug("finished sending alert emails");
		
	}

	
	/**
	 * @param mobrizAlertManager the mobrizAlertManager to set
	 */
	public void setMobrizAlertManager(MobrizAlertManager mobrizAlertManager) {
		this.mobrizAlertManager = mobrizAlertManager;
	}

	/**
	 * @param userManager the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * @param mailEngine the mailEngine to set
	 */
	public void setMailEngine(MailEngine mailEngine) {
		this.mailEngine = mailEngine;
	}

	
    
	/**
     * Convenience message to send messages to users, includes app URL as footer.
	s
	 * @throws MessagingException 
	 * @throws MessagingException 
     */
    protected void sendAlertEmail(MiroResponse mr) throws MessagingException  {
    	
    	String emailBody = "Miro Test Results available for " + mr.getFirstName() + " " + mr.getLastName() + "\n\nLogin at:  " + RequestUtil.getAppURL(request) ;
    	String emailSubject = "Miro Test Results available for " + mr.getFirstName() + " " + mr.getLastName();
    	    	
    	ArrayList attachmentFiles = new ArrayList();
    	
    	
    	mailEngine.sendMessage(fromEmail, mr.getPractitionerEmail(),emailBody, emailSubject, attachmentFiles);
    	
       
       
    }

    
    /**
     * Convenience message to send messages to users, includes app URL as footer.l
	 * @throws MessagingException 
	 * @throws MessagingException 
     */
    protected void sendAlertEmail(MiroTeam mt) throws MessagingException  {
    	
    	String emailBody = "Miro Team Report available for " + mt.getMiroTeamName()  + "\n\nLogin at:  " + RequestUtil.getAppURL(request) ;
    	String emailSubject = "Miro Test Results available for " + mt.getMiroTeamName() ;
    	    	
    	ArrayList attachmentFiles = new ArrayList();
    	
    	
    	mailEngine.sendMessage(fromEmail, mt.getPractitionerEmail(),emailBody, emailSubject, attachmentFiles);
    	
       
       
    }
    
    
    
   
	/**
	 * @hibernate.property 
	 * @return the alertDocLimit
	 */
	public int getAlertDocLimit() {
		return miroDocLimit;
	}

	/**
	 */
	public void setMiroDocLimit(int miroDocLimit) {
		this.miroDocLimit = miroDocLimit;
	}

	/**
	 * @param fromEmail the fromEmail to set
	 */
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	/**
	 * @param miroResponseManager the miroResponseManager to set
	 */
	public void setMiroResponseManager(MiroResponseManager miroResponseManager) {
		this.miroResponseManager = miroResponseManager;
	}

	

	
	
}