package uk.co.bluetrail.mobriz.webapp.action;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;

import uk.co.bluetrail.miro.MiroReport;
import uk.co.bluetrail.miro.MiroResponse;
import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Account;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.MobrizAlert;
import uk.co.bluetrail.mobriz.model.Question;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.AccountManager;
import uk.co.bluetrail.mobriz.service.MailEngine;
import uk.co.bluetrail.mobriz.service.MiroResponseManager;
import uk.co.bluetrail.mobriz.service.MiroTeamManager;
import uk.co.bluetrail.mobriz.service.MiroTeamReportManager;
import uk.co.bluetrail.mobriz.service.MobrizAlertManager;
import uk.co.bluetrail.mobriz.service.SurveyManager;
import uk.co.bluetrail.mobriz.service.SurveyResponseManager;
import uk.co.bluetrail.mobriz.service.UserManager;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class BatchProcessController implements Controller {
	 private final Log log = LogFactory.getLog(BatchProcessController.class);
	
	 
	 private int miroDocLimit;
	 
	 
	 private SurveyManager surveyManager = null;
	 private SurveyResponseManager surveyResponseManager = null;
	 private MobrizAlertManager mobrizAlertManager = null;
	 private UserManager userManager = null;
	 private MailEngine mailEngine = null;
	 private String fromEmail ;
	 private MiroResponseManager miroResponseManager ;
	 private String reportCreatedTemplateName ;
	 private MiroTeamReportManager miroTeamReportManager =null;
   
     
	 private HttpServletRequest request ;
     private HttpServletResponse response ; 

     
	

	/**
	 * @return the miroTeamManager
	 */
	public MiroTeamReportManager getMiroTeamReportManager() {
		return miroTeamReportManager;
	}

	/**
	 * @param miroTeamManager the miroTeamManager to set
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
	 * @param filePath 
     * @param user
     * @param msg
     * @param url
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
     * Convenience message to send messages to users, includes app URL as footer.
	 * @param filePath 
     * @param user
     * @param msg
     * @param url
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
	 * @param alertDocLimit the alertDocLimit to set
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