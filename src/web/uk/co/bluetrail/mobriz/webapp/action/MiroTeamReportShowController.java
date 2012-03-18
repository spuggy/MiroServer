package uk.co.bluetrail.mobriz.webapp.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.MiroProject;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.SurveyException;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.MiroProjectManager;
import uk.co.bluetrail.mobriz.service.MiroResponseManager;
import uk.co.bluetrail.mobriz.service.MiroTeamManager;
import uk.co.bluetrail.mobriz.service.MiroTeamReportManager;
import uk.co.bluetrail.mobriz.service.UserManager;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

public class MiroTeamReportShowController extends BaseController {
    private final Log log = LogFactory.getLog(MiroTeamReportShowController.class);
    private MiroProjectManager miroProjectManager = null;
	private UserManager userManager;
	private MiroResponseManager miroResponseManager;
	private MiroTeamManager miroTeamManager;
	
	
	
	
	
    

	/**
	 * @hibernate.property 
	 * @return the miroTeamManager
	 */
	public MiroTeamManager getMiroTeamManager() {
		return miroTeamManager;
	}

	/**
	 * @param miroTeamManager the miroTeamManager to set
	 */
	public void setMiroTeamManager(MiroTeamManager miroTeamManager) {
		this.miroTeamManager = miroTeamManager;
	}

	/**
	 * @param miroResponseManager the miroResponseManager to set
	 */
	public void setMiroResponseManager(MiroResponseManager miroResponseManager) {
		this.miroResponseManager = miroResponseManager;
	}

	/**
	 * @param userManager the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setMiroProjectManager(MiroProjectManager miroProjectManager) {
        this.miroProjectManager = miroProjectManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        String id = request.getParameter("id");
        
        if(id == null) {
        	saveMessage(request, "No Id in Url");
        	return new ModelAndView(new RedirectView("miroTeamList.html"));
        }
        
        MiroTeam mt = this.miroTeamManager.getMiroTeam(id);
        
        if(mt == null) {
        	saveMessage(request, "team not found");
        	return new ModelAndView(new RedirectView("miroTeamList.html"));
        }
        
        if(mt.getCreatedBy_id().longValue() != this.getCurrentUser().getId().longValue()) {
        	saveMessage(request, "only the practitioner can download");
        	return new ModelAndView(new RedirectView("miroTeamList.html"));
        }
    
        String reportLocation = miroResponseManager.getMiroReportPath(RequestUtil.getAppURL(request));
        
        String filename = reportLocation + "/out/" + mt.getMiroTeamNameFileName(Constants.PDF);
		
        File file = new File(filename);
		
		
		
		if(!file.exists()) {
			saveMessage(request, "report does not exist please contact support");
        	return new ModelAndView(new RedirectView("miroTeamList.html"));
			
		}
		
		byte[] content = this.getBytesFromFile(file);

		
		
		String mimetype = request.getSession().getServletContext().getMimeType(filename);
		response.setContentType(mimetype);
		response.setContentLength(content.length);
		response.setHeader("Content-Disposition","attachment; filename=\"" + mt.getMiroTeamNameFileName(Constants.PDF) +  "\"");
		
		FileCopyUtils.copy(content , response.getOutputStream());
		return null;

        
      }
    
    private byte[] getBytesFromFile(File file) throws IOException {
    	try
    	{
    	InputStream fileIn = new FileInputStream(file);
    	int len = fileIn.available();
    	byte bytes[] = new byte[len];

    	fileIn.read(bytes);

    	fileIn.close();
    	return bytes;
    	} catch(Exception e)
    	{
    	return null;
    	}

    	}
}
