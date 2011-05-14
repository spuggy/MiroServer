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

import uk.co.bluetrail.mobriz.webapp.view.MiroTeamMapChart;
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
		TeamMapPlotter teamMapPlotter = new TeamMapPlotter();
		while(itr.hasNext()) {
			teamMapDTO = (TeamMapDTO) itr.next();
			teamMapPlotter.add(teamMapDTO.getInitials(),teamMapDTO.getLeadingMode(),true);
			if(teamMapImageEngaged) {
				teamMapPlotter.add(teamMapDTO.getInitials(),teamMapDTO.getSecondaryMode(),false);
			}
			
		}
		
		String backGroundFileName = request.getSession().getServletContext().getRealPath(teamMapPlotter.getBacgroundImage())  ;
		   
		
		java.awt.image.BufferedImage teamMap = miroTeamMapChart.createTeamChart(backGroundFileName, teamMapPlotter.getInitialsArray(), teamMapPlotter.getX(), teamMapPlotter.getY());
		
		
		//Set the mime type of the image
        response.setContentType("image/jpg");
        
        //Write the image as a jpg
        OutputStream out = response.getOutputStream();
        ImageIO.write(teamMap, "jpg", out);
        out.close();
		
		return null;
		
		
		
	}

}



class TeamMapPlotter{

	ArrayList plotInitials = new ArrayList();
	ArrayList plotXY = new ArrayList();
	HashMap leadingPlotStacks = new HashMap();
	HashMap secondaryPlotStacks = new HashMap();
	
	Stack eLeading = new Stack(); 
	Stack aLeading = new Stack(); 
	Stack dLeading = new Stack(); 
	Stack oLeading = new Stack(); 
	Stack e2nd = new Stack(); 
	Stack a2nd = new Stack(); 
	Stack d2nd = new Stack(); 
	Stack o2nd = new Stack(); 
	
	ArrayList leadingSeed = new ArrayList();
	ArrayList secondSeed = new ArrayList();
	
	int bgXCentre = 600;
	int bgYCentre = 450;
	
	public TeamMapPlotter() {
		buildXYStacks();
	}
	
	private void buildXYStacks() {
		
		initSmallDotSeeds();
		
		//build e
		Iterator itr = leadingSeed.iterator();
		XY xy = null;
		while(itr.hasNext()){
			xy = (XY) itr.next();
			dLeading.add(xy); //the seeds are all driving so just add these
			eLeading.add(new XY((bgXCentre-xy.x)+bgXCentre,xy.y));
			aLeading.add(new XY(xy.x,(bgYCentre-xy.y)+bgYCentre));
			oLeading.add(new XY((bgXCentre-xy.x)+bgXCentre,(bgYCentre-xy.y)+bgYCentre));
		}
		
//		build e
		itr = secondSeed.iterator();
		while(itr.hasNext()){
			xy = (XY) itr.next();
			d2nd.add(xy); //the seeds are all driving so just add these
			e2nd.add(new XY((bgXCentre-xy.x)+bgXCentre,xy.y));
			a2nd.add(new XY(xy.x,(bgYCentre-xy.y)+bgYCentre));
			o2nd.add(new XY((bgXCentre-xy.x)+bgXCentre,(bgYCentre-xy.y)+bgYCentre));
		}
		
		leadingPlotStacks.put("A",aLeading);
		leadingPlotStacks.put("E",eLeading);
		leadingPlotStacks.put("O",oLeading);
		leadingPlotStacks.put("D",dLeading);
		
		
		secondaryPlotStacks.put("A",a2nd);
		secondaryPlotStacks.put("E",e2nd);
		secondaryPlotStacks.put("O",o2nd);
		secondaryPlotStacks.put("D",d2nd);
	}

	public String getBacgroundImage() {
		
		return "/images/teammapbackground.png";
	}

	public String[] getInitialsArray() {
		
		String[] initials = new String[plotInitials.size()];
		
		Iterator itr = plotInitials.iterator();
	
		int plotIdx = 0 ;
		while(itr.hasNext()){
			initials[plotIdx++] = (String) itr.next(); 
		}
		
		return initials;
	}
	
	public int[] getX() {
		
		int[] x = new int[plotXY.size()];
		
		Iterator itr = plotXY.iterator();
	
		int plotIdx = 0 ;
		String key = null;
		XY xy = null;
		while(itr.hasNext()){
			xy = (XY) itr.next(); 
			x[plotIdx++] = xy.x;
		}
		
		return x;
	}
	
	
	public void add(String initials, String mode, boolean isLeadingMode) {
		
		Stack plotStack = null;
	
		if(isLeadingMode) {
			plotStack = (Stack) leadingPlotStacks.get(mode);
			
 		} else {
 			plotStack = (Stack) secondaryPlotStacks.get(mode);
 			
 		}
		
		if(plotStack == null) {
			System.out.println("plotstack null for " + initials);
			return ;
		}
		XY xy = null;
		try {
			xy = (XY) plotStack.pop();
			plotInitials.add(initials);
			plotXY.add(xy);
		} catch(EmptyStackException e){
			System.out.println("Stack empty ignoring");
		}
		
		
		
	}

	

	public int[] getY() {
		int[] y = new int[plotXY.size()];
		
		Iterator itr = plotXY.iterator();
	
		int plotIdx = 0 ;
		String key = null;
		XY xy = null;
		while(itr.hasNext()){
			xy = (XY) itr.next(); 
			y[plotIdx++] = xy.y;
		}
		
		return y;
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		TeamMapPlotter tmp = new TeamMapPlotter();
		
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		tmp.add("RS", "D", true) ;
		
		tmp.add("TH", "A", false) ;
		tmp.add("ES", "O", true) ;
		
		String[] i = tmp.getInitialsArray();
		int[] x = tmp.getX();
		int[] y = tmp.getY();
		
		for(int v = 0;v < x.length;v++ ){
			System.out.print(i[v]);
			System.out.print(",");
			System.out.print(x[v]);
			System.out.print(",");
			System.out.println(y[v]);
			
		}
		
		
		

	}
	
	private void initBigDotSeeds()
	  {
	    this.leadingSeed = new ArrayList();
	    this.leadingSeed.add(new XY(359, 215));
	    this.leadingSeed.add(new XY(359, 291));
	    this.leadingSeed.add(new XY(359, 366));
	    this.leadingSeed.add(new XY(294, 330));
	    this.leadingSeed.add(new XY(294, 403));
	    this.leadingSeed.add(new XY(495, 139));
	    this.leadingSeed.add(new XY(495, 366));
	    this.leadingSeed.add(new XY(558, 178));
	    this.leadingSeed.add(new XY(558, 403));
	    this.leadingSeed.add(new XY(558, 254));
	    this.leadingSeed.add(new XY(558, 330));
	    this.leadingSeed.add(new XY(429, 403));
	    this.leadingSeed.add(new XY(429, 178));
	    this.leadingSeed.add(new XY(429, 254));
	    this.leadingSeed.add(new XY(429, 330));
	    this.leadingSeed.add(new XY(495, 215));
	    this.leadingSeed.add(new XY(495, 291));

	    this.secondSeed = new ArrayList();
	    this.secondSeed.add(new XY(60, 400));
	    this.secondSeed.add(new XY(60, 323));
	    this.secondSeed.add(new XY(60, 244));
	    this.secondSeed.add(new XY(60, 165));
	    this.secondSeed.add(new XY(60, 87));
	    this.secondSeed.add(new XY(381, 87));
	    this.secondSeed.add(new XY(147, 400));
	    this.secondSeed.add(new XY(304, 87));
	    this.secondSeed.add(new XY(227, 87));
	    this.secondSeed.add(new XY(147, 87));
	    this.secondSeed.add(new XY(147, 323));
	    this.secondSeed.add(new XY(147, 165));
	    this.secondSeed.add(new XY(227, 165));
	    this.secondSeed.add(new XY(227, 244));
	    this.secondSeed.add(new XY(147, 244));
	  }
	
	private void initSmallDotSeeds(){
		
		this.leadingSeed = new ArrayList();
		leadingSeed.add(new XY(529,112));
		leadingSeed.add(new XY(571,112));
		leadingSeed.add(new XY(571,154));
		leadingSeed.add(new XY(571,196));
		leadingSeed.add(new XY(319,238));
		leadingSeed.add(new XY(571,238));
		leadingSeed.add(new XY(277,280));
		leadingSeed.add(new XY(319,280));
		leadingSeed.add(new XY(571,280));
		leadingSeed.add(new XY(277,322));
		leadingSeed.add(new XY(319,322));
		leadingSeed.add(new XY(571,322));
		leadingSeed.add(new XY(277,364));
		leadingSeed.add(new XY(319,364));
		leadingSeed.add(new XY(571,364));
		leadingSeed.add(new XY(235,406));
		leadingSeed.add(new XY(277,406));
		leadingSeed.add(new XY(319,406));
		leadingSeed.add(new XY(571,406));
		leadingSeed.add(new XY(571,406));
		leadingSeed.add(new XY(403,154));
		leadingSeed.add(new XY(445,154));
		leadingSeed.add(new XY(487,154));
		leadingSeed.add(new XY(529,154));
		leadingSeed.add(new XY(361,196));
		leadingSeed.add(new XY(403,196));
		leadingSeed.add(new XY(445,196));
		leadingSeed.add(new XY(487,196));
		leadingSeed.add(new XY(529,196));
		leadingSeed.add(new XY(361,406));
		leadingSeed.add(new XY(403,406));
		leadingSeed.add(new XY(445,406));
		leadingSeed.add(new XY(487,406));
		leadingSeed.add(new XY(529,406));
		leadingSeed.add(new XY(361,364));
		leadingSeed.add(new XY(403,364));
		leadingSeed.add(new XY(445,364));
		leadingSeed.add(new XY(487,364));
		leadingSeed.add(new XY(529,364));
		leadingSeed.add(new XY(361,322));
		leadingSeed.add(new XY(403,322));
		leadingSeed.add(new XY(445,322));
		leadingSeed.add(new XY(487,322));
		leadingSeed.add(new XY(529,322));
		leadingSeed.add(new XY(361,280));
		leadingSeed.add(new XY(403,280));
		leadingSeed.add(new XY(445,280));
		leadingSeed.add(new XY(487,280));
		leadingSeed.add(new XY(529,280));
		leadingSeed.add(new XY(361,238));
		leadingSeed.add(new XY(403,238));
		leadingSeed.add(new XY(445,238));
		leadingSeed.add(new XY(487,238));
		leadingSeed.add(new XY(529,238));
		
		this.secondSeed = new ArrayList();
		secondSeed.add(new XY(25,70));
		secondSeed.add(new XY(277,70));
		secondSeed.add(new XY(319,70));
		secondSeed.add(new XY(361,70));
		secondSeed.add(new XY(403,70));
		secondSeed.add(new XY(445,70));
		secondSeed.add(new XY(487,70));
		secondSeed.add(new XY(529,70));
		secondSeed.add(new XY(571,70));
		secondSeed.add(new XY(25,112));
		secondSeed.add(new XY(319,112));
		secondSeed.add(new XY(361,112));
		secondSeed.add(new XY(403,112));
		secondSeed.add(new XY(25,154));
		secondSeed.add(new XY(277,154));
		secondSeed.add(new XY(319,154));
		secondSeed.add(new XY(25,196));
		secondSeed.add(new XY(235,196));
		secondSeed.add(new XY(277,196));
		secondSeed.add(new XY(25,238));
		secondSeed.add(new XY(235,238));
		secondSeed.add(new XY(25,280));
		secondSeed.add(new XY(235,280));
		secondSeed.add(new XY(25,322));
		secondSeed.add(new XY(193,322));
		secondSeed.add(new XY(25,364));
		secondSeed.add(new XY(193,364));
		secondSeed.add(new XY(25,406));
		secondSeed.add(new XY(67,406));
		secondSeed.add(new XY(109,406));
		secondSeed.add(new XY(151,406));
		secondSeed.add(new XY(193,406));
		secondSeed.add(new XY(67,70));
		secondSeed.add(new XY(109,70));
		secondSeed.add(new XY(151,70));
		secondSeed.add(new XY(193,70));
		secondSeed.add(new XY(235,70));
		secondSeed.add(new XY(67,112));
		secondSeed.add(new XY(109,112));
		secondSeed.add(new XY(151,112));
		secondSeed.add(new XY(193,112));
		secondSeed.add(new XY(235,112));
		secondSeed.add(new XY(277,112));
		secondSeed.add(new XY(67,154));
		secondSeed.add(new XY(109,154));
		secondSeed.add(new XY(151,154));
		secondSeed.add(new XY(193,154));
		secondSeed.add(new XY(235,154));
		secondSeed.add(new XY(67,364));
		secondSeed.add(new XY(109,364));
		secondSeed.add(new XY(151,364));
		secondSeed.add(new XY(67,322));
		secondSeed.add(new XY(109,322));
		secondSeed.add(new XY(151,322));
		secondSeed.add(new XY(67,280));
		secondSeed.add(new XY(109,280));
		secondSeed.add(new XY(151,280));
		secondSeed.add(new XY(193,280));
		secondSeed.add(new XY(67,238));
		secondSeed.add(new XY(109,238));
		secondSeed.add(new XY(151,238));
		secondSeed.add(new XY(193,238));
		secondSeed.add(new XY(67,196));
		secondSeed.add(new XY(109,196));
		secondSeed.add(new XY(151,196));
		secondSeed.add(new XY(193,196));
		
	}
	
	
	
	
	
	
}

class XY {
	
	XY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int x;
	public int y;
	
}