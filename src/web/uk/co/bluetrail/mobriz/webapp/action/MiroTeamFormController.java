package uk.co.bluetrail.mobriz.webapp.action;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.MiroTeamManager;
import uk.co.bluetrail.mobriz.webapp.form.MiroProjectSelectorForm;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

public class MiroTeamFormController extends MiroProjectSelectorFormController {
	private MiroTeamManager miroTeamManager = null;

	public void setMiroTeamManager(MiroTeamManager miroTeamManager) {
		this.miroTeamManager = miroTeamManager;
	}
	
	

	private boolean isNotBlank(String thing) {
		if(thing == null || thing.trim().equals("")) {
			return false;
		}  else {
			return true;
		}
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
            mf.setCommentary(mt.getCommentary());  
                       
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
    		return doTeamMap(request,null,userList,allUsersList,unselectedUserList,mt,mf);
                       
        } 
        
      //  private Map doTeamMap(HttpServletRequest request, String[] selectedProjects, List userList, List allUsersList,List unselectedUserList) {

        
        
        return new HashMap();
        
    }
    


	



	







	private Map doTeamMap(HttpServletRequest request, String[] selectedProjects, List userList, List allUsersList,List unselectedUserList, MiroTeam mt,MiroProjectSelectorForm miroProjectSelectorForm) {

		List teamMapData = miroResponseManager.getTeamMap(miroResponseManager.getMiroReportPath(RequestUtil.getAppURL(request)), userList);
		
		HashMap model = new HashMap();

		boolean showRecalcEditButtons = true;
		boolean showTeamSaveCreateButtons = true ;
		boolean showReportInprogressMessage = false ;
		boolean showDownloadLink = false;
		boolean showDeleteButton = true ;
		
		if(mt.getId()==null) {
			showDeleteButton = false;
		}


        /*
        let people edit created reports
		if(mt.getTeamReportStatus()==MiroTeam.REPORT_CREATED || mt.getId()==null) {
			showRecalcEditButtons = true;
		}*/

		if(mt.getTeamReportStatus()==MiroTeam.REPORT_REQUESTED) {
			showReportInprogressMessage = true;
		}
		
		if(mt.getTeamReportStatus()==MiroTeam.REPORT_DOWNLOADED) {
			showDownloadLink = true;
		}

        if(mt.getTeamReportStatus()>=MiroTeam.REPORT_REQUESTED)   {
            model.put("confirmReportCreation",false);
        } else {
            model.put("confirmReportCreation",true);
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
		model.put("teamUsersPlain", toStringListWithCommas(userList));
		model.put("selectedProjects", selectedProjects);
		model.put("unselectedUserList", usertoLabel(unselectedUserList));
		model.put("miroProjectSelectorForm", miroProjectSelectorForm);





		// stick it in the session too so the graph controller can get to it
		HttpSession session = request.getSession();
		session.setAttribute("teamMapData", teamMapData);

		return model ;
	}

	private String toStringListWithCommas(List userList) {

		if(userList==null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		Iterator itr = userList.iterator()  ;
		while(itr.hasNext()) {
			User user = (User)  itr.next();
			sb.append(user.getFirstName());
			sb.append(" ");
			sb.append(user.getLastName());
			sb.append(", ");
		}

		if(sb.length()>0) {
			return sb.substring(0,sb.length()-2).trim();
		} else {
			return sb.toString();
		}

	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		MiroProjectSelectorForm miroProjectSelectorForm = (MiroProjectSelectorForm) command;

		//copy over form stuff just in case they are altering team.
		MiroTeam mt = new MiroTeam();

		if (isNotBlank(miroProjectSelectorForm.getDelete()) ) {
			 
			this.miroTeamManager.removeMiroTeam(miroProjectSelectorForm.getId().toString());
            saveMessage(request, "Report deleted");

            return new ModelAndView("redirect:miroTeamList.html");
		}
		
		
		if (isNotBlank(miroProjectSelectorForm.getSave())  || isNotBlank(miroProjectSelectorForm.getCreateteamreport()) ) {

			if (miroProjectSelectorForm.getMiroTeamName() == null || miroProjectSelectorForm.getMiroTeamName().equals("")) {
				errors.reject("miroProjectSelectorForm.noSelection","Please enter a name for the team");
				return showForm(request, response, errors);
			}
			
			
			if(miroProjectSelectorForm.getId()==null || miroProjectSelectorForm.getId().equals("")) {
				mt = new MiroTeam();
			} else {
				mt = this.miroTeamManager.getMiroTeam(miroProjectSelectorForm.getId().toString());
			}
			
			List members = userManager.getUsers(miroProjectSelectorForm.getTeamUsers());
			mt.setMiroTeamName(miroProjectSelectorForm.getMiroTeamName());
			mt.setMembers(new HashSet(members));
			mt.setCommentary(miroProjectSelectorForm.getCommentary());
			
			if(isNotBlank(miroProjectSelectorForm.getCreateteamreport())) {
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

		Map model = doTeamMap(request, selectedProjects, userList, allUsersList, unselectedUserList,mt,miroProjectSelectorForm);
		
			
			 
		return new ModelAndView(getSuccessView(),model);

	}
	
	

}
