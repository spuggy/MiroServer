package uk.co.bluetrail.mobriz.webapp.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import uk.co.bluetrail.mobriz.model.LabelValue;
import uk.co.bluetrail.mobriz.model.LookupDef;
import uk.co.bluetrail.mobriz.model.MiroProject;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.MiroProjectManager;
import uk.co.bluetrail.mobriz.service.MiroResponseManager;
import uk.co.bluetrail.mobriz.service.UserManager;
import uk.co.bluetrail.mobriz.serviceDTO.TeamMapDTO;
import uk.co.bluetrail.mobriz.webapp.form.MiroProjectSelectorForm;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;



public class MiroProjectSelectorFormController extends BaseFormController {

	 protected MiroProjectManager miroProjectManager = null;
	 protected MiroResponseManager miroResponseManager = null;
	 protected UserManager userManager = null;
	 protected int maxProjects ;
	 protected String graphType;
	 protected int maxPies ;
     
     
     
      
	 /**
	 * @param miroResponseManager the miroResponseManager to set
	 */
	public void setMiroResponseManager(MiroResponseManager miroResponseManager) {
		this.miroResponseManager = miroResponseManager;
	}

	/**
	 * @param maxPies the maxPies to set
	 */
	public void setMaxPies(int maxPies) {
		this.maxPies = maxPies;
	}

	/**
	 * @param graphType the graphType to set
	 */
	public void setGraphType(String graphType) {
		this.graphType = graphType;
	}

	/**
	 * @param userManager the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * @hibernate.property 
	 * @return the maxProjects
	 */
	public int getMaxProjects() {
		return maxProjects;
	}

	/**
	 * @param maxProjects the maxProjects to set
	 */
	public void setMaxProjects(int maxProjects) {
		this.maxProjects = maxProjects;
	}

	protected Object formBackingObject(HttpServletRequest request)
	    throws Exception {
	     
	        return new MiroProjectSelectorForm();
    }
	 
	    public void setMiroProjectManager(MiroProjectManager miroProjectManager) {
	        this.miroProjectManager = miroProjectManager;
	    }
	    public MiroProjectSelectorFormController() {
	        setCommandName("miroProjectSelectorForm");
	        setCommandClass(MiroProjectSelectorForm.class);
	        
	    }
	    

	    protected Map referenceData(HttpServletRequest request)  throws Exception {
	       
	    	List miroProjects = null;
   	
	    	miroProjects = miroProjectManager.getMiroProjects(null, getCurrentUser());
	        
	        Iterator itr = miroProjects.iterator();
	        
	        List miroProjectLabels = new ArrayList();
	        MiroProject mp = null ; 
	        while(itr.hasNext()){
	        	mp = (MiroProject) itr.next();
	        	miroProjectLabels.add(new LabelValue(mp.getProjectTitle(),mp.getId().toString()));
	        	
	        }
	    	
	    	
	        Map refData = new HashMap() ;
	    
	        refData.put("myProjects", miroProjectLabels);
	        
	        return refData;
	    }

	    public ModelAndView onSubmit(HttpServletRequest request,
	                                 HttpServletResponse response, Object command,
	                                 BindException errors)
	    throws Exception {
	        
	    	MiroProjectSelectorForm miroProjectSelectorForm = (MiroProjectSelectorForm) command;
	    	
	    	String[] selectedProjects = request.getParameterValues("selectedProjects");
	    	String[] teamUsers = request.getParameterValues("teamUsers");
	    	
	    	List userList = null;
	    	List allUsersList = null;
	    	List unselectedUserList = null;
	    	
	    	if(teamUsers==null || teamUsers.length==0) { 
	    	
		    	if(selectedProjects == null) {
		    		errors.reject("miroProjectSelectorForm.noSelection", "Select at leat one project");
		    		log.info("here");
		    		return showForm(request, response, errors);
		    	} 
		    	
		    	if(selectedProjects.length > getMaxProjects()) {
		    		 errors.reject("miroProjectSelectorForm.maxProjects",
	                         new Object[] { new Integer(getMaxProjects())
	                }, "maxProjects");
		    		return showForm(request, response, errors);
		    	}
		    	
		    	userList = userManager.getUsersByProjects(selectedProjects);
		    	allUsersList = userList;
		    	unselectedUserList = new ArrayList();
	    	} else {
	    		userList = userManager.getUsers(teamUsers);
	    		allUsersList = userManager.getUsersByProjects(selectedProjects);
	    		unselectedUserList = subtract(userList,allUsersList);
	    		
	    		
	    	}
	    	
	    	
	    	
	    	if(this.graphType.equals("pie")) {
	    		
	    		return doTeamPie(userList) ;
	    	
	    	} else {
	    		return doTeamMap(request,selectedProjects,userList,allUsersList,unselectedUserList);
	    		
	    	}
	    		    		    	

	    }

	    protected List usertoLabel(List userList) {
	    	Iterator itr = userList.iterator();
	    	return usertoLabel(itr);
	    }
	    
	    protected List usertoLabel(Set userList) {
	    	Iterator itr = userList.iterator();
	    	return usertoLabel(itr);
	    }
	    
	    
	    private List usertoLabel(Iterator itr) {
	    
	        
	        List userLabels= new ArrayList();
	        User u= null ; 
	        while(itr.hasNext()){
	        	u = (User) itr.next();
	        	userLabels.add(new LabelValue(u.getFullName(),u.getId().toString()));
	        	
	        }
	        
	        Collections.sort(userLabels,new Comparator<LabelValue>() {
				public int compare(LabelValue object1, LabelValue object2) {
					return object1.getLabel().compareTo(object2.getLabel());
				}
			});
	        
	        return userLabels;
	    }
	    

	    protected List subtract(List userList, List allUsersList) {
		
			
			
			List newList = new ArrayList();
			
			User u = null; 
			
			//build map
			Iterator itr =  userList.iterator();
			HashMap userListMap = new HashMap();
			while(itr.hasNext()) {
				u = (User) itr.next();
				userListMap.put(u.getId(), u);
			}
	
			itr =  allUsersList.iterator();
			while(itr.hasNext()) {
				u = (User) itr.next();
				if(userListMap.get(u.getId())==null) {
					newList.add(u);
				}
				
				
			}
			
				
			
			return newList;
		}

		private ModelAndView doTeamMap(HttpServletRequest request,String[] selectedProjects,List userList, List allUsersList, List unselectedUserList) {
			
			List teamMapData = miroResponseManager.getTeamMap(miroResponseManager.getMiroReportPath(RequestUtil.getAppURL(request)),userList);
			HashMap model = new HashMap();
			
			model.put("teamMapData",teamMapData);
			model.put("allUsers", allUsersList);
			model.put("teamUsers", usertoLabel(userList));
			model.put("selectedProjects", selectedProjects);
			model.put("unselectedUserList", usertoLabel(unselectedUserList));
			model.put("miroProjectSelectorForm" , new MiroProjectSelectorForm());
			
			
			//stick it in the session too so the graph controller can get to it too.
			HttpSession session = request.getSession();
			session.setAttribute("teamMapData", teamMapData);
			
			return new ModelAndView(getSuccessView(),model);
		}

		private ModelAndView doTeamPie(List userList) {
			
			HashMap model = new HashMap();
			StringBuffer sb = new StringBuffer() ;
			ArrayList userIds = new ArrayList();
			Iterator itr = userList.iterator() ;
			User user = null ;
			int uCount = 1 ;
			while(itr.hasNext()){
				user = (User) itr.next() ;
				if(uCount < this.maxPies && itr.hasNext()) { 
					sb.append(user.getId().toString());
					sb.append(",");
				} else {
					sb.append(user.getId().toString());
					userIds.add(sb.toString());
					sb = new StringBuffer();
					uCount=1;
				}
				uCount++;
			}
			
			ArrayList emptyForIE = new ArrayList();
			
			model.put("userIds", userIds);
			model.put("emptyList",emptyForIE);
			
			
			return new ModelAndView(getSuccessView(),model);
		}
}
