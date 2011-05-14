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
import uk.co.bluetrail.mobriz.model.SurveyException;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.MiroProjectManager;
import uk.co.bluetrail.mobriz.service.MiroResponseManager;
import uk.co.bluetrail.mobriz.service.UserManager;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class MiroReportShowController extends BaseController {
    private final Log log = LogFactory.getLog(MiroReportShowController.class);
    private MiroProjectManager miroProjectManager = null;
	private UserManager userManager;
	private MiroResponseManager miroResponseManager;

	
	
	
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
        
        User user = null;
        
        if (!StringUtils.isEmpty(id)) {
          
			user = userManager.getUser(id.toString());
        } else {
        	log.error("no id");
        }
        
        String reportLocation = miroResponseManager.getMiroReportPath(RequestUtil.getAppURL(request));
        
        String filename = reportLocation + "/out/" + user.getReportFileName()+".pdf";
		File file = new File(filename);
		
		if(!file.exists()) {
			filename = reportLocation + "/out/" +user.getOldReportFileName()+".pdf";
			file = new File(filename);			
		}
		
		if(!file.exists()) {
			log.error("report file does not exist for  id="+id );
			throw new SurveyException("Report file does not exist!");
		}
		
		byte[] content = this.getBytesFromFile(file);

		
		
		String mimetype = request.getSession().getServletContext().getMimeType(filename);
		response.setContentType(mimetype);
		response.setContentLength(content.length);
		String[] fnameBits = file.getName().split("_");
		response.setHeader("Content-Disposition","attachment; filename=\"" + fnameBits[0] + "_" +  fnameBits[1] +".pdf\"");
		
		FileCopyUtils.copy(content , response.getOutputStream());
		return null;
		
	
        
//        return new ModelAndView("showPDF","pdf",user.getLastName()+"_"+user.getResponse_id());

        
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
