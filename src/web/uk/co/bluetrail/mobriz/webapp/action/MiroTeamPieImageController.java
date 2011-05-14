package uk.co.bluetrail.mobriz.webapp.action;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import uk.co.bluetrail.mobriz.service.MiroResponseManager;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;

public class MiroTeamPieImageController extends BaseController {

	
	private final Log log = LogFactory.getLog(MiroTeamPieImageController.class);

	
	private MiroResponseManager miroResponseManager = null;
	
	
	

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		String idString = request.getParameter("ids") ;
		
		if(idString==null) {
			log.error("null ids in MiroTeamPieImageController");
			return null;
		}
		
		String[] ids  = idString.split(",");
		
		BufferedImage teamPie = miroResponseManager.getMiroTeamPie(miroResponseManager.getMiroReportPath(RequestUtil.getAppURL(request)), ids);
		
        //Set the mime type of the image
        response.setContentType("image/jpg");
        
        //Write the image as a jpg
        OutputStream out = response.getOutputStream();
        
        ImageIO.write(teamPie, "jpg", out);
        out.close();
		
		return null;
	}




	/**
	 * @param miroResponseManager the miroResponseManager to set
	 */
	public void setMiroResponseManager(MiroResponseManager miroResponseManager) {
		this.miroResponseManager = miroResponseManager;
	}

}
