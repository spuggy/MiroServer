package uk.co.bluetrail.mobriz.webapp.ajaxImpl;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.SimpleMailMessage;
import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.MiroProject;
import uk.co.bluetrail.mobriz.model.Role;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.*;
import uk.co.bluetrail.mobriz.webapp.ajax.AjaxMiroProjectManager;
import uk.co.bluetrail.mobriz.webapp.ajaxDTO.MiroCandidateAjaxDTO;
import uk.co.bluetrail.mobriz.webapp.ajaxDTO.ShoppingCartDTO;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;
import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

public class AjaxMiroProjectManagerImpl implements AjaxMiroProjectManager  {

	protected static final int STATUS_OK = 0 ;
	protected static final int STATUS_DUPE = 1;
	protected static final int STATUS_OK_BAD_EMAIL = 9 ;
	
	protected static final Log log = LogFactory.getLog(AjaxMiroProjectManagerImpl.class);
	
	UserManager userManager;
	RoleManager roleManager ;
	MiroProjectManager miroProjectManager ;
	protected MailEngine mailEngine = null;
	protected SimpleMailMessage message = null;
	protected String templateName = null;
	private SurveyResponseManager surveyResponseManager;

	
	
	/**
	 * @param sureyResponseManager the sureyResponseManager to set
	 */
	public void setSurveyResponseManager(SurveyResponseManager sureyResponseManager) {
		this.surveyResponseManager = sureyResponseManager;
	}

	/**
	 * @param mailEngine the mailEngine to set
	 */
	public void setMailEngine(MailEngine mailEngine) {
		this.mailEngine = mailEngine;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(SimpleMailMessage message) {
		this.message = message;
	}

	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @param miroProjectManager the miroProjectManager to set
	 */
	public void setMiroProjectManager(MiroProjectManager miroProjectManager) {
		this.miroProjectManager = miroProjectManager;
	}

	/**
	 * @param roleManager the roleManager to set
	 */
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public User getCurrentUser() {
    	
    	User currentUser = null;
    	
    	SecurityContext sc = SecurityContextHolder.getContext() ;
  	  
    	
    	try { 
    	
    		currentUser = (User)  sc.getAuthentication().getPrincipal() ;
    		return currentUser ;
    	} catch (Exception e) {
    		return null;
    	}
    	
    	
    	
 } 
	
	public MiroCandidateAjaxDTO[] getCandidates(String projectId) throws Exception{  
		
		List users = userManager.getCandidates(projectId) ;
		
		Iterator itr = users.iterator();
		
		MiroCandidateAjaxDTO miroCandidateAjaxDTO = null;
		
		List surveyResponses = null ;
		
		MiroCandidateAjaxDTO[] candidates = new MiroCandidateAjaxDTO[users.size()];

		User user = null;
		
		for(int i = 0 ; i < candidates.length;i++){
            Object[] obj = (Object[]) itr.next();

            candidates[i]= new MiroCandidateAjaxDTO(obj);
				
		}


		return candidates;
	}
	
	public MiroCandidateAjaxDTO buyReport(String id) {
		
		MiroCandidateAjaxDTO mr = new MiroCandidateAjaxDTO();
		
		try {
		
		
		
		User user = userManager.getUser(id) ;
		SurveyResponse surveyResponse = surveyResponseManager.getSurveyResponse(user.getResponse_id().toString());
		
		if(user==null ) {
			mr.setStatus(User.INVALID_REPORT);
			log.error("user is null buy report for id = " +id);
			return mr;
		}
		
		if( surveyResponse ==null) {
			mr.setStatus(User.INVALID_REPORT);
			log.error("survey response is null buy report for id = " +id);
			return mr;
		}
		
		if(user.getStatus()!=User.PURCHASE_REPORT ) {
			mr.setStatus(User.INVALID_REPORT);
			log.error("unexpected status " + user.getStatus() + " buy report for id = " +id);
			return mr;
		}
		
		User pracUser = userManager.getUser(this.getCurrentUser().getId().toString());
		
		//if(pracUser.getCreditBalance()==0) {
		//	mr.setCreditBalance(pracUser.getCreditBalance()+"0");
			//mr.setStatus(User.ZERO_BALANCE); 
		//	return mr;
		//}
		
		pracUser = userManager.buyReport(user.getId(),getCurrentUser().getId());
		
		
		mr.setCreditBalance(pracUser.getCreditBalance()+"");
		mr.setStatus(pracUser.OK);
		
		return mr;
		
		} catch(Exception e) {
			log.error("Exception in buyReport for id = " + id + " - " + e.toString());
			mr.setStatus(User.INVALID_REPORT);
			return mr;
		}
		
		
	}
	
	public Object[] sendInviteEmails(String[] ids ){
		
		MiroProject miroProject = null;
		
		
                  
        ArrayList report = null;
        

		
		User user = null;
		
		report = new ArrayList();
		
		for(int i = 0 ; i < ids.length; i++){
			user = userManager.getUser(ids[i]);
			sendUserEmail(user,report,miroProject);
		}
			
		
		return report.toArray();
	}
	
	private String getReportLine(User user, boolean status, String errMessage) {
		if(status) {
			return "Email to " + user.getFullName() + " - OK: mail sent" ;  
		} else {
			return "Email to " + user.getFullName() + " - Error: mail not sent, check address";
		}
	}
	
	
	private boolean sendUserEmail(User user, ArrayList report,MiroProject miroProject) {
	
		String newUserEmailSubject  = null;
        String newUserEmailMessage  = null;
        
        WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		
		if(user!=null && miroProject==null){
			miroProject = miroProjectManager.getMiroProject(user.getProject_id().toString());
			
			if(miroProject.getEmailInviteSubject()==null || miroProject.getEmailInviteSubject().equals("") ){
				newUserEmailSubject  = miroProject.getProjectTitle();
				
			} else {
				newUserEmailSubject  = miroProject.getEmailInviteSubject();

			}
	        newUserEmailMessage  = miroProject.getEmailInviteText() ;
	      
		}
		
		
		
		if(user!=null) {
			  // Send an account information e-mail
			message.setSubject(newUserEmailSubject);

			
			try{
	        	sendUserMessage(user, newUserEmailMessage, RequestUtil.getAppURL(request));
	        	
	        	
	        	
	        	if(user.getStatus()==User.INVITE_NOT_SENT) {
	        		user.setStatus(User.INVITE_SENT);
	        		userManager.saveUser(user);
	        	}
	        	report.add(getReportLine(user,true,""));
	        	
	        } catch(Exception e) {
	        	log.debug(getReportLine(user,false,e.toString()));
	        	report.add(getReportLine(user,false,""));
	        	return false;
	        }
		
	        return true;
		}
		return false;
	}
	
	
	public MiroCandidateAjaxDTO saveCandidateAndEmail(MiroCandidateAjaxDTO miroCandidateAjaxDTO) throws Exception{
		
		
		this.saveCandidate(miroCandidateAjaxDTO);
		
		if(miroCandidateAjaxDTO.getStatus()!=AjaxMiroProjectManagerImpl.STATUS_OK) {
			return miroCandidateAjaxDTO;
		}
		
		MiroProject miroProject = null;
		ArrayList report = null;
        
		User user = userManager.getUser(miroCandidateAjaxDTO.getId());
		report = new ArrayList();
		if(sendUserEmail(user,report,miroProject)){
			miroCandidateAjaxDTO.setStatus(AjaxMiroProjectManagerImpl.STATUS_OK);
			return miroCandidateAjaxDTO;
		} else {
			miroCandidateAjaxDTO.setStatus(AjaxMiroProjectManagerImpl.STATUS_OK_BAD_EMAIL);
			miroCandidateAjaxDTO.setErrorMessage("User saved but the email was not sent.  Please check address");
			return miroCandidateAjaxDTO;
		}
		
	}

	

	public MiroCandidateAjaxDTO saveCandidate(MiroCandidateAjaxDTO miroCandidateAjaxDTO) throws Exception{
		
		
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		String algorithm = (String)ctx.getServletContext().getAttribute(Constants.ENC_ALGORITHM)   ;     
	
        if (algorithm == null) { // should only happen for test case
              
                algorithm = "SHA";
        }

        Role userRole = roleManager.getRole(Constants.USER_ROLE);
        			
        User userDTO = null;
        
        if(miroCandidateAjaxDTO.getId().equals("new")) {
        	userDTO = new User();
        } else {
        	userDTO = userManager.getUser(miroCandidateAjaxDTO.getId());
        }
        
    	userDTO.setFirstName(miroCandidateAjaxDTO.getFirstName());
		userDTO.setLastName(miroCandidateAjaxDTO.getLastName());
		userDTO.setEmail(miroCandidateAjaxDTO.getEmailAddress());
		userDTO.setProject_id(Long.parseLong(miroCandidateAjaxDTO.getProject_id()));
	    
		try {
			User savedUser = userManager.saveUser(userDTO,algorithm,userRole);
			miroCandidateAjaxDTO.setId(userDTO.getId().toString());
			miroCandidateAjaxDTO.setStatus(AjaxMiroProjectManagerImpl.STATUS_OK);
			
			MiroProject miroProject = miroProjectManager.getMiroProject(userDTO.getProject_id().toString());
			
			String newUserEmailSubject  = miroProject.getProjectTitle();
            String newUserEmailMessage  = miroProject.getEmailInviteText() ;
                          
            
            // Send an account information e-mail
            message.setSubject(newUserEmailSubject);
            
            
            
           // sendUserMessage(savedUser, newUserEmailMessage, RequestUtil.getAppURL(request));
            
            
		} catch(UserExistsException uex) {
			miroCandidateAjaxDTO.setStatus(AjaxMiroProjectManagerImpl.STATUS_DUPE);
			miroCandidateAjaxDTO.setErrorMessage("A user with this name already exists for this project!");
			
		}
		
	
				
		return miroCandidateAjaxDTO ;
		
	}
	public MiroCandidateAjaxDTO deleteCandidate(String candidateId)  throws Exception{
		
		MiroCandidateAjaxDTO miroCandidateAjaxDTO = new MiroCandidateAjaxDTO();
		miroCandidateAjaxDTO.setStatus(0);
		try{		
		userManager.removeUser(candidateId);
		} catch(Exception e){
			miroCandidateAjaxDTO.setStatus(1);
			miroCandidateAjaxDTO.setErrorMessage("An error occurred deleting this candidate");
		}
		return miroCandidateAjaxDTO;  
		
		
	}
	/**
	 * @param userManagerl the userManagerl to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	/**
     * Convenience message to send messages to users, includes app URL as footer.
     * @param user
     * @param msg
     * @param url
	 * @throws Exception 
     */
    protected void sendUserMessage(User user, String msg, String url) throws Exception {
      

        message.setTo(user.getFullName() + "<" + user.getEmail() + ">");

        Map model = new HashMap();
        model.put("user", user);

        // TODO: once you figure out how to get the global resource bundle in
        // WebWork, then figure it out here too.  In the meantime, the Username
        // and Password labels are hard-coded into the template. 
        // model.put("bundle", getTexts());
        model.put("message", msg);
        model.put("applicationURL", url);
        mailEngine.sendMessageWithExceptions(message, templateName, model);
    }
    
    public ShoppingCartDTO delFromCart(String id) {
    	
    	ShoppingCartDTO shoppingCartDTO = getShoppingCart();
    	
    	shoppingCartDTO.delFromCart(id);
    	
    	this.saveShoppingCart(shoppingCartDTO);
    	
    	return shoppingCartDTO;
    	
    }

	public ShoppingCartDTO addToCart(String id) {

        /*
		ShoppingCartDTO shoppingCartDTO = getShoppingCart();
		
		try {
			
			
			
			User user = userManager.getUser(id) ;
			SurveyResponse surveyResponse = surveyResponseManager.getSurveyResponse(user.getResponse_id().toString());
			
			
			
			if(user==null ) {
				shoppingCartDTO.setStatus(User.INVALID_REPORT);
				log.error("user is null buy report for id = " +id);
				return shoppingCartDTO;
			}
			
			if( surveyResponse ==null) {
				shoppingCartDTO.setStatus(User.INVALID_REPORT);
				log.error("survey response is null buy report for id = " +id);
				return shoppingCartDTO;
			}
			
			if(user.getStatus()!=User.PURCHASE_REPORT ) {
				shoppingCartDTO.setStatus(User.INVALID_REPORT);
				log.error("unexpected status " + user.getStatus() + " buy report for id = " +id);
				return shoppingCartDTO;
			}
			
			shoppingCartDTO.addToCart(user.getId().toString(), 	new MiroCandidateAjaxDTO(user.getId(),user.getProject_id(),user.getFirstName(),user.getLastName(),user.getEmail(),user.getStatus()));  
	
			saveShoppingCart(shoppingCartDTO);
			
			
			return shoppingCartDTO;
			
			} catch(Exception e) {
				log.error("Exception in addToCartbuy for id = " + id + " - " + e.toString());
				shoppingCartDTO.setStatus(User.INVALID_REPORT);
				return shoppingCartDTO;
			}

		*/

        return null;
		
		
	}

	private void saveShoppingCart(ShoppingCartDTO shoppingCartDTO) {
		WebContext ctx = WebContextFactory.get();
		HttpSession session = ctx.getHttpServletRequest().getSession();
		
		
		session.setAttribute(Constants.SHOPPING_CART,shoppingCartDTO );
		
		
	}

	private ShoppingCartDTO getShoppingCart() {
	
		WebContext ctx = WebContextFactory.get();
		HttpSession session = ctx.getHttpServletRequest().getSession();
		
		ShoppingCartDTO shoppingCartDTO = (ShoppingCartDTO) session.getAttribute(Constants.SHOPPING_CART);
		
		if(shoppingCartDTO==null) {
			return new ShoppingCartDTO();
		}
		
		return shoppingCartDTO;
	}

	
	
	
	public MiroCandidateAjaxDTO[] getShoppingCartItems() {
	
		ShoppingCartDTO shoppingCartDTO= getShoppingCart();
		
		if(shoppingCartDTO==null){
			return null;
		}
		
		Map users = shoppingCartDTO.getItems();
		
		
		
		Iterator itr = users.keySet().iterator();
		
		
		MiroCandidateAjaxDTO miroCandidateAjaxDTO = null;
		
		MiroCandidateAjaxDTO[] candidates = new MiroCandidateAjaxDTO[users.size()];
		
		User user = null;
		
		int i = 0 ;
		String key = null;
		while(itr.hasNext()) {
			key = (String) itr.next();
			candidates[i++]=  (MiroCandidateAjaxDTO) users.get(key);
		}
		
		
		return candidates;
	}
	
	
	
}
