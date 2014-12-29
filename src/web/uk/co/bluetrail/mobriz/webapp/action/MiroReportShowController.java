package uk.co.bluetrail.mobriz.webapp.action;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;
import uk.co.bluetrail.miro.MiroResponse;
import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.SurveyException;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.MiroProjectManager;
import uk.co.bluetrail.mobriz.service.MiroResponseManager;
import uk.co.bluetrail.mobriz.service.SurveyResponseManager;
import uk.co.bluetrail.mobriz.service.UserManager;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MiroReportShowController extends BaseController {
    private final Log log = LogFactory.getLog(MiroReportShowController.class);
    private MiroProjectManager miroProjectManager = null;
	private UserManager userManager;
	private MiroResponseManager miroResponseManager;
    private SurveyResponseManager surveyResponseManager ;

    public SurveyResponseManager getSurveyResponseManager() {
        return surveyResponseManager;
    }

    public void setSurveyResponseManager(SurveyResponseManager surveyResponseManager) {
        this.surveyResponseManager = surveyResponseManager;
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

        //TODO change to handle new and old ways  .. if you have the new version param then look for that else .. just get the old file name

        String id = request.getParameter("id");
        String version = request.getParameter("version");

        User user = null;
        
        if (!StringUtils.isEmpty(id)) {
			user = userManager.getUser(id.toString());
        } else {
            throw new SurveyException("no id supplied");
        }

        if (StringUtils.isEmpty(version)) {
            throw new SurveyException("no version supplied");
        }

        String reportLocation = miroResponseManager.getMiroReportPath(RequestUtil.getAppURL(request));

        SurveyResponse surveyResponse = surveyResponseManager.getSurveyResponse(user.getResponse_id().toString());

        if(surveyResponse==null) {
            throw new SurveyException("no surveyResponse  found for " + user.getResponse_id().toString());
        }


        MiroResponse mr = new MiroResponse(surveyResponse);
        mr.setFirstName(user.getFirstName());
        mr.setLastName(user.getLastName());
        mr.setMiroReportName(user.getReportFileName());

        String filename = null ;
        String fileVersion = "";


        if(surveyResponse.getSurvey_id().longValue()== Constants.Survey_id_Mirov11 && version.equals("v11")) {

            filename = reportLocation + "/out/" + mr.getMiroReportName(Constants.Survey_id_Mirov11) + ".pdf";
            fileVersion = "_v11";
            log.debug("downloading v11 from " + filename);

        } else {
            filename = reportLocation + "/out/" + mr.getMiroReportName(Constants.Survey_id_Mirov10)+ ".pdf";
            fileVersion = "";     //leave blank as old version had none on it.
            log.debug("downloading v10 from " + filename);
        }

		File file = new File(filename);

		if(!file.exists()) {
			log.error("report file does not exist for  " + filename );
			throw new SurveyException("Report file does not exist! " + filename);
		}
		
		byte[] content = this.getBytesFromFile(file);


        //TODO FIX THIS FILE NAME ... well make it select
		
		String mimetype = request.getSession().getServletContext().getMimeType(filename);
		response.setContentType(mimetype);
		response.setContentLength(content.length);
		String[] fnameBits = file.getName().split("_");
		response.setHeader("Content-Disposition","attachment; filename=\"" + fnameBits[0] + "_" +  fnameBits[1] + fileVersion + ".pdf\"");
		
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