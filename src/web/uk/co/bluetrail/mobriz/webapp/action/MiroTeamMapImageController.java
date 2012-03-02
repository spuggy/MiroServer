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
import uk.co.bluetrail.mobriz.serviceDTO.TeamMapDTO;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;


public class MiroTeamMapImageController extends BaseController {  

	
    	
	
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
		
		/*<prop key="/teamImage/teamMapImageEngaged.jpg">miroTeamMapImageController</prop>
		   	<prop key="/teamImage/teamMapImageLeading.jpg">miroTeamMapImageController</prop>
		 */
		
		Iterator itr = teamMapData.iterator();
		TeamMapDTO teamMapDTO = null;
		MiroTeamMapPlotter teamMapPlotter = new MiroTeamMapPlotter(teamMapData.size());
		while(itr.hasNext()) {
			teamMapDTO = (TeamMapDTO) itr.next();
			teamMapPlotter.add(teamMapDTO.getInitials(),teamMapDTO.getLeadingMode(),true);
			if(teamMapImageEngaged) {
				teamMapPlotter.add(teamMapDTO.getInitials(),teamMapDTO.getSecondaryMode(),false);
			}
			
		}
		
		String backGroundFileName = request.getSession().getServletContext().getRealPath(teamMapPlotter.getBacgroundImage())  ;
		   
		
		java.awt.image.BufferedImage teamMap = miroTeamMapChart.createTeamChart(backGroundFileName, teamMapPlotter);
		 
		
		//Set the mime type of the image
        response.setContentType("image/jpg");
        
        //Write the image as a jpg
        OutputStream out = response.getOutputStream();
        ImageIO.write(teamMap, "jpg", out);
        out.close();
		
		return null;
		
		
		
	}

}



