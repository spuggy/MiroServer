package uk.co.bluetrail.mobriz.webapp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import uk.co.bluetrail.mobriz.webapp.action.BaseFormController;
import uk.co.bluetrail.mobriz.webapp.form.MiroProjectSelectorForm;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;
import uk.co.bluetrail.mobriz.model.LabelValue;
import uk.co.bluetrail.mobriz.model.MiroProject;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.service.MiroTeamManager;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

public class MiroTeamFormController extends MiroProjectSelectorFormController {
	private MiroTeamManager miroTeamManager = null;

	public void setMiroTeamManager(MiroTeamManager miroTeamManager) {
		this.miroTeamManager = miroTeamManager;
	}
	
	
	
	

	
	
	/* (non-Javadoc)
	 * @see uk.co.bluetrail.mobriz.webapp.action.MiroProjectSelectorFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String id = request.getParameter("id");
        MiroTeam mt = null;

        MiroProjectSelectorForm mf = null;
        if (!StringUtils.isEmpty(id)) {
            mt = miroTeamManager.getMiroTeam(id);
            mf = new MiroProjectSelectorForm();
            mf.setId(mt.getId().toString());
            mf.setMiroTeamName(mt.getMiroTeamName());
            mf.setVersion(mt.getVersion().toString());
                       
        } else {
        	mf = new MiroProjectSelectorForm();
        }
        
        return mf;
	}





	

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {   
		
		List userList = new ArrayList();   //get from team 
		List allUsersList = new ArrayList();  //get form all projects on team 
		List unselectedUserList = new ArrayList();
		
		String id = request.getParameter("id");
        MiroTeam mt = null;

        MiroProjectSelectorForm mf = null;
        if (!StringUtils.isEmpty(id)) {
            mt = miroTeamManager.getMiroTeam(id);
            mf = new MiroProjectSelectorForm();
            mf.setId(mt.getId().toString());
            mf.setMiroTeamName(mt.getMiroTeamName());
            mf.setVersion(mt.getVersion().toString());
            
            userList = new ArrayList(mt.getMembers());
    		allUsersList = userManager.getUsersByProjects(mt.getProjects());
    		unselectedUserList = subtract(userList,allUsersList);
    		return doTeamMap(request,null,userList,allUsersList,unselectedUserList,mt);
                       
        } 
        
      //  private Map doTeamMap(HttpServletRequest request, String[] selectedProjects, List userList, List allUsersList,List unselectedUserList) {

        
        
        return new HashMap();
        
    }
    


	



	







	private Map doTeamMap(HttpServletRequest request, String[] selectedProjects, List userList, List allUsersList,List unselectedUserList, MiroTeam mt) {

		List teamMapData = miroResponseManager.getTeamMap(miroResponseManager.getMiroReportPath(RequestUtil.getAppURL(request)), userList);
		
		HashMap model = new HashMap();

		boolean showRecalcEditButtons = false;
		boolean showTeamSaveCreateButtons = false ; 
		boolean showReportInprogressMessage = false ;
		boolean showDownloadLink = false;
		boolean showDeleteButton = true ;
		
		if(mt.getId()==null) {
			showDeleteButton = false;
		}
		
		if(mt.getTeamReportStatus()==MiroTeam.REPORT_CREATED) {
			showRecalcEditButtons = true;
		}
		
		if(this.getCurrentUser().isTeamReportCreator()) {
			showTeamSaveCreateButtons = true;
		}
		
		if(mt.getTeamReportStatus()==MiroTeam.REPORT_REQUESTED) {
			showReportInprogressMessage = true;
		}
		
		if(mt.getTeamReportStatus()==MiroTeam.REPORT_DOWNLOADED) {
			showDownloadLink = true;
		}
		
		model.put("showDeleteButton", showDeleteButton);
		model.put("showRecalcEditButtons" , showRecalcEditButtons);
		model.put("showTeamSaveCreateButtons" , showTeamSaveCreateButtons);
		model.put("showReportInprogressMessage" ,showReportInprogressMessage);
		model.put("showDownloadLink", showDownloadLink);
		
		model.put("miroTeam", mt);
		model.put("teamMapData", teamMapData);
		model.put("allUsers", allUsersList);
		model.put("teamUsers", usertoLabel(userList));
		model.put("selectedProjects", selectedProjects);
		model.put("unselectedUserList", usertoLabel(unselectedUserList));
		model.put("miroProjectSelectorForm", new MiroProjectSelectorForm());

		// stick it in the session too so the graph controller can get to it
		HttpSession session = request.getSession();
		session.setAttribute("teamMapData", teamMapData);

		return model ;
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		MiroProjectSelectorForm miroProjectSelectorForm = (MiroProjectSelectorForm) command;

		MiroTeam mt = new MiroTeam();
		
		if (request.getParameter("delete") != null) {
			 
			this.miroTeamManager.removeMiroTeam(miroProjectSelectorForm.getId().toString());
            saveMessage(request, "Report deleted");

            return new ModelAndView("redirect:miroTeamList.html");
		}
		
		
		if (request.getParameter("save") != null || request.getParameter("createteamreport") != null) {

			
			if(miroProjectSelectorForm.getId()==null || miroProjectSelectorForm.getId().equals("")) {
				mt = new MiroTeam();
			} else {
				mt = this.miroTeamManager.getMiroTeam(miroProjectSelectorForm.getId().toString());
			}
			
			List members = userManager.getUsers(miroProjectSelectorForm.getTeamUsers());
			mt.setMiroTeamName(miroProjectSelectorForm.getMiroTeamName());
			mt.setMembers(new HashSet(members));
			
			if(request.getParameter("createteamreport") != null) {
				mt.setTeamReportStatus(MiroTeam.REPORT_REQUESTED);
				saveMessage(request, "Team saved and report requested,  You will recieve an email alert when it has been generated");
			} else {
				saveMessage(request, "Team saved");
			}
			
			this.miroTeamManager.saveMiroTeam(mt, this.getCurrentUser());
			
			
			return new ModelAndView("redirect:miroTeamList.html");

		}

		String[] selectedProjects = request.getParameterValues("selectedProjects");
		String[] teamUsers = request.getParameterValues("teamUsers");

		List userList = null;
		List allUsersList = null;
		List unselectedUserList = null;

		if (teamUsers == null || teamUsers.length == 0) {

			if (selectedProjects == null) {
				errors.reject("miroProjectSelectorForm.noSelection",
						"Select at leat one project");
				log.info("here");
				return showForm(request, response, errors);
			}

			if (selectedProjects.length > getMaxProjects()) {
				errors.reject("miroProjectSelectorForm.maxProjects",
						new Object[] { new Integer(getMaxProjects()) },
						"maxProjects");
				return showForm(request, response, errors);
			}

			userList = userManager.getUsersByProjects(selectedProjects);
			allUsersList = userList;
			unselectedUserList = new ArrayList();
		} else {
			userList = userManager.getUsers(teamUsers);
			if(selectedProjects!=null) {
				allUsersList = userManager.getUsersByProjects(selectedProjects);
			} else {
				allUsersList = userList;
			}
			unselectedUserList = subtract(userList, allUsersList);
		}

		Map model = doTeamMap(request, selectedProjects, userList, allUsersList, unselectedUserList,mt);
		
			
			 
		return new ModelAndView(getSuccessView(),model);

	}
	
	

}
