package uk.co.bluetrail.mobriz.webapp.action;



import java.io.OutputStream;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;

import uk.co.bluetrail.miro.MiroTeamMapChart;
import uk.co.bluetrail.miro.MiroTeamMapPlotter;
import uk.co.bluetrail.mobriz.service.MiroResponseManager;
import uk.co.bluetrail.mobriz.service.SurveyResponseManager;
import uk.co.bluetrail.mobriz.serviceDTO.TeamMapDTO;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;


public class MiroTeamMapImageController extends BaseController {  

	private MiroResponseManager miroResponseManager ;
	
	

	/**
	 * @param miroResponseManager the miroResponseManager to set
	 */
	public void setMiroResponseManager(MiroResponseManager miroResponseManager) {
		this.miroResponseManager = miroResponseManager;
	}



	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		
		List teamMapData = (List) session.getAttribute("teamMapData");
		
		if(teamMapData==null) {
			log.error("teamMapData is Nulll in MiroTeamMapImageController");
			return null;
		}
		
		//build arrays
		MiroTeamMapChart miroTeamMapChart = new MiroTeamMapChart();
		String engaged = request.getParameter("engaged");
		
		
		
		boolean teamMapImageEngaged = false ;
		if(engaged.indexOf("true")==-1) {
			teamMapImageEngaged = false;
		} else {
			teamMapImageEngaged = true;
		}
		
		MiroTeamMapPlotter teamMapPlotter = new MiroTeamMapPlotter(teamMapData,teamMapImageEngaged);
		 
		String filePath = this.miroResponseManager.getMiroReportPath(RequestUtil.getAppURL(request));
		
		System.out.println(filePath);
		
		java.awt.image.BufferedImage teamMap = miroTeamMapChart.createTeamChart(filePath, teamMapPlotter);
		 
		
		//Set the mime type of the image
        response.setContentType("image/jpg");
        
        //Write the image as a jpg
        OutputStream out = response.getOutputStream();
        ImageIO.write(teamMap, "jpg", out);
        out.close();
		
		return null;
		
		
		
	}

}



