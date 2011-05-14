package uk.co.bluetrail.mobriz.webapp.action;

import java.util.ArrayList;
import java.util.HashMap;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailSendException;
import org.springframework.web.servlet.ModelAndView;

import uk.co.bluetrail.miro.MiroResponse;
import uk.co.bluetrail.mobriz.model.MiroTransaction;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.MailEngine;
import uk.co.bluetrail.mobriz.service.MiroTransactionManager;
import uk.co.bluetrail.mobriz.service.UserManager;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;
import uk.co.bluetrail.mobriz.webapp.util.StringEncoder;

public class AddCreditsSuccessController extends BaseController {

	private String SEP="&";
	
	  private final Log log = LogFactory.getLog(AddCreditsSuccessController.class);
	
	private String encrypPassword;
	private MiroTransactionManager miroTransactionManager = null;
	private UserManager userManager = null;
	 private MailEngine mailEngine = null;
	 private String fromEmail ;
	
	 
	
	/**
	 * @param fromEmail the fromEmail to set
	 */
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}


	/**
	 * @param mailEngine the mailEngine to set
	 */
	public void setMailEngine(MailEngine mailEngine) {
		this.mailEngine = mailEngine;
	}


	/**
	 * @param userManager the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}


	public void setMiroTransactionManager(
			MiroTransactionManager miroTransactionManager) {
		this.miroTransactionManager = miroTransactionManager;
	}

	
	/**
	 * @hibernate.property 
	 * @return the encrypPassword
	 */
	public String getEncrypPassword() {
		return encrypPassword;
	}

	/**
	 * @param encrypPassword the encrypPassword to set
	 */
	public void setEncrypPassword(String encrypPassword) {
		this.encrypPassword = encrypPassword;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String crypt = null;
		
		
		
		String cryptNoBase64 = null;
		
		String cryptRaw = null;
		
		try {
		
		crypt = (String) request.getParameter("crypt");
				
		cryptNoBase64 =  StringEncoder.decode64(crypt);
		
		cryptRaw = StringEncoder.simpleXor(cryptNoBase64.toString(),this.getEncrypPassword());
		
		} catch(Exception e) {
			log.error("Error decoding " + e);
		}
		
		HashMap params = new HashMap();
		String[] paramRaw = cryptRaw.split(SEP);
		String[] paramLabelValue = null;
		for(int i = 0 ; i < paramRaw.length;i++){
			paramLabelValue = paramRaw[i].split("=") ;
			params.put(paramLabelValue[0], paramLabelValue[1]);
		}
		
		String projectId = request.getParameter("pid");
		
		if(projectId !=null) {
			params.put("projectId", projectId);
		}
		
		String id = (String) params.get("VendorTxCode");
		String status = (String) params.get("Status");
		String statusDetail = (String) params.get("StatusDetail");
		params.put("StatusDetail", statusDetail);
		
		MiroTransaction miroTransaction = miroTransactionManager.getMiroTransaction(id);
		
		if(miroTransaction.getStatus()==MiroTransaction.OK_PROCESSED || miroTransaction.getStatus()==MiroTransaction.FAILURE_PROCESSED){
			log.error("Transaction already processed id=" + miroTransaction.getId());
			params.put("StatusDetail", "Transaction already processed id=" + miroTransaction.getId() );
			return new ModelAndView("addCreditsFailure",params);
		}
		miroTransaction.setPaymentStatus((String) params.get("Status"));
		miroTransaction.setPaymentStatusDetail((String) params.get("StatusDetail"));
		miroTransaction.setPaymentTransId((String) params.get("VPSTxId"));
		
		
		if(status.equals("OK")) {
			miroTransaction.setStatus(MiroTransaction.OK);
			miroTransactionManager.saveMiroTransaction(miroTransaction);
			User user = userManager.getUser(miroTransaction.getUser_id().toString());
			sendConfirmaton(miroTransaction,true,user);
			
			params.put("user", user);
			log.debug("mirotransaction processed ok" + miroTransaction.getId());
			return new ModelAndView("addCreditsSuccess",params);
		} else {
			miroTransaction.setStatus(MiroTransaction.FAILURE);
			miroTransactionManager.saveMiroTransaction(miroTransaction);
			User user = userManager.getUser(miroTransaction.getUser_id().toString());
			sendConfirmaton(miroTransaction,false,user);
			log.debug("mirotransaction processing failed!" + miroTransaction.getId());
			return new ModelAndView("addCreditsFailure",params);
		}
		
		//TODO add the two views and error view
	
		//TODO add error emails
		//TODO add logging etc
		//TODO add new fields to trans.
		//TODO add admin interface userid=x and check for sysadmin on save.
		//TODO add log earings on error
		//TODO add failure page.
		//TODO do we need security on the submit pages?
		
		
	}


	private void sendConfirmaton(MiroTransaction miroTransaction, boolean success, User user) throws Exception{
	  	
		String emailBody = null ;
    	String emailSubject = null ;
		
		try {
		
		

		
		if(success) {
	    	emailBody = "Your MiRo credit  transaction was successful. Reference:" + miroTransaction.getId() ;
	    	emailSubject = "You have successfully added " + miroTransaction.getCredits() + ".  Your balance is " + user.getCreditBalance();
		} else {
			emailBody = "Your MiRo credit transaction failed. Reference:" + miroTransaction.getId()  ;
	    	emailSubject = "Your MiRo credit transaction failed.\n\nStatus = " + miroTransaction.getPaymentStatusDetail() ;
		}
	    
		ArrayList attachmentFiles = new ArrayList();
	    	
	    	
	    mailEngine.sendMessage(fromEmail, user.getEmail(),emailBody, emailSubject, attachmentFiles);
	    
	    mailEngine.sendMessage(fromEmail, "rspence@bluetrail.co.uk",emailBody, emailSubject + " " + user.getFullName(), attachmentFiles);
	    	
		} catch(MailSendException e) {
			log.error("Exception sending email  " + emailSubject + " " + user.getFullName() +user.getEmail() );
		}
	       
	   
		
	}


	/**
	 * @hibernate.property 
	 * @return the fromEmail
	 */
	public String getFromEmail() {
		return fromEmail;
	}


	

}
