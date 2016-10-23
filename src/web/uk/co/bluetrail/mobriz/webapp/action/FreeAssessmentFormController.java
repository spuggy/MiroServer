package uk.co.bluetrail.mobriz.webapp.action;


import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.webapp.ajax.AjaxMiroProjectManager;
import uk.co.bluetrail.mobriz.webapp.ajaxDTO.MiroCandidateAjaxDTO;
import uk.co.bluetrail.mobriz.webapp.ajaxDTO.MiroFreeAssessmentReturnDto;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class FreeAssessmentFormController extends BaseFormController {

	public void setAjaxMiroProjectManager(AjaxMiroProjectManager ajaxMiroProjectManager) {
		this.ajaxMiroProjectManager = ajaxMiroProjectManager;
	}

	private AjaxMiroProjectManager ajaxMiroProjectManager;

	public FreeAssessmentFormController() {
		setCommandName("miroCandidateAjaxDTO");
		setCommandClass(MiroCandidateAjaxDTO.class);
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		return new MiroCandidateAjaxDTO();
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		MiroCandidateAjaxDTO miroCandidateAjaxDTO = (MiroCandidateAjaxDTO) command;
		miroCandidateAjaxDTO.setProject_id("1401");
		miroCandidateAjaxDTO.setId("new");

		if (log.isDebugEnabled()) {
			log.debug("entering 'onSubmit' method...");
		}

		MiroFreeAssessmentReturnDto miroFreeAssessmentReturnDto = new MiroFreeAssessmentReturnDto() ;


		try {

			String url = RequestUtil.getAppURL(request) ;
			String algorithm = (String) getConfiguration().get(Constants.ENC_ALGORITHM);
			String userName = miroCandidateAjaxDTO.getProject_id() + miroCandidateAjaxDTO.getEmailAddress();

			if (ajaxMiroProjectManager != null && algorithm !=null && url !=null) {
				MiroCandidateAjaxDTO newMiroCandidateAjaxDTO = ajaxMiroProjectManager.saveCandidateAndEmail(userName,miroCandidateAjaxDTO,algorithm,url);

				if(newMiroCandidateAjaxDTO.getStatus()!= AjaxMiroProjectManager.STATUS_OK) {
					miroFreeAssessmentReturnDto.addError("DUPE");
					miroFreeAssessmentReturnDto.setStatus(MiroFreeAssessmentReturnDto.FAIL);
				} else {
					miroFreeAssessmentReturnDto.setStatus(MiroFreeAssessmentReturnDto.OK);
				}
			}

		} catch(Exception e) {
		   log.error("Exception " + e.getMessage());
		   miroFreeAssessmentReturnDto.addError("GENERAL");
		   miroFreeAssessmentReturnDto.setStatus(MiroFreeAssessmentReturnDto.FAIL);
		}

		return new ModelAndView("jsonView", "dto", miroFreeAssessmentReturnDto);

	}

}
