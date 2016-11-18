package uk.co.bluetrail.mobriz.webapp.action;


import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.service.SettingManager;
import uk.co.bluetrail.mobriz.webapp.ajax.AjaxMiroProjectManager;
import uk.co.bluetrail.mobriz.webapp.ajaxDTO.MiroCandidateAjaxDTO;
import uk.co.bluetrail.mobriz.webapp.ajaxDTO.MiroFreeAssessmentReturnDto;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;


public class FreeAssessmentFormController extends BaseFormController {

	private SettingManager settingManager = null;
	private String[] capatchaAnswers = {"10","13","9"};

	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}


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

		MiroCandidateAjaxDTO miroCandidateAjaxDTO = new MiroCandidateAjaxDTO();
		
		int capatchaValue = getCaptachaIndex();

		miroCandidateAjaxDTO.setCaptchaIdx(capatchaValue);

		return miroCandidateAjaxDTO;
	}

	private int randInt(int min, int max) {


		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}


	private int getCaptachaIndex() {
		return randInt(0,2);
	}

	public boolean isValidEmailAddress(String email) {

		if(email==null) {
			return false;
		}

		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		MiroFreeAssessmentReturnDto miroFreeAssessmentReturnDto = new MiroFreeAssessmentReturnDto() ;
		String projectId = getProjectId();

		if(projectId==null) {
			miroFreeAssessmentReturnDto.addError("GENERAL");
			miroFreeAssessmentReturnDto.setStatus(MiroFreeAssessmentReturnDto.FAIL);
			return new ModelAndView("jsonView", "dto", miroFreeAssessmentReturnDto);
		}

		MiroCandidateAjaxDTO miroCandidateAjaxDTO = (MiroCandidateAjaxDTO) command;

		if(isBlank(miroCandidateAjaxDTO.getFirstName())) {
			miroFreeAssessmentReturnDto.addError("FIRST_NAME_BLANK");
			miroFreeAssessmentReturnDto.setStatus(MiroFreeAssessmentReturnDto.FAIL);
			return new ModelAndView("jsonView", "dto", miroFreeAssessmentReturnDto);
		}

		if(isBlank(miroCandidateAjaxDTO.getLastName())) {
			miroFreeAssessmentReturnDto.addError("LAST_NAME_BLANK");
			miroFreeAssessmentReturnDto.setStatus(MiroFreeAssessmentReturnDto.FAIL);
			return new ModelAndView("jsonView", "dto", miroFreeAssessmentReturnDto);
		}

		if(isBlank(miroCandidateAjaxDTO.getPhoneNumber())) {
			miroFreeAssessmentReturnDto.addError("BAD_PHONE");
			miroFreeAssessmentReturnDto.setStatus(MiroFreeAssessmentReturnDto.FAIL);
			return new ModelAndView("jsonView", "dto", miroFreeAssessmentReturnDto);
		}

		if(!isValidEmailAddress(miroCandidateAjaxDTO.getEmailAddress())) {
			miroFreeAssessmentReturnDto.addError("BAD_EMAIL");
			miroFreeAssessmentReturnDto.setStatus(MiroFreeAssessmentReturnDto.FAIL);
			return new ModelAndView("jsonView", "dto", miroFreeAssessmentReturnDto);
		}

		if(!isValidCapatcha(miroCandidateAjaxDTO.getCaptchaIdx(),miroCandidateAjaxDTO.getCaptchaAnswer())) {
			miroFreeAssessmentReturnDto.addError("BAD_CAPATCHA");
			miroFreeAssessmentReturnDto.setStatus(MiroFreeAssessmentReturnDto.FAIL);
			return new ModelAndView("jsonView", "dto", miroFreeAssessmentReturnDto);
		}


		miroCandidateAjaxDTO.setProject_id(projectId);
		miroCandidateAjaxDTO.setId("new");
		miroCandidateAjaxDTO.setIsFreeCandidate(true);

		if (log.isDebugEnabled()) {
			log.debug("entering 'onSubmit' method...");
		}



		try {

			String url = RequestUtil.getAppURL(request) ;
			String algorithm = (String) getConfiguration().get(Constants.ENC_ALGORITHM);
			String userName = miroCandidateAjaxDTO.getProject_id() + miroCandidateAjaxDTO.getEmailAddress();

			if (ajaxMiroProjectManager != null && algorithm !=null && url !=null) {
				MiroCandidateAjaxDTO newMiroCandidateAjaxDTO = ajaxMiroProjectManager.saveNonAjaxCandidateAndEmail(userName,miroCandidateAjaxDTO,algorithm,url);

				if(newMiroCandidateAjaxDTO.getStatus()== AjaxMiroProjectManager.STATUS_OK) {
					miroFreeAssessmentReturnDto.setStatus(MiroFreeAssessmentReturnDto.OK);
				} else {
					miroFreeAssessmentReturnDto.setStatus(MiroFreeAssessmentReturnDto.FAIL);
					if (newMiroCandidateAjaxDTO.getStatus() == AjaxMiroProjectManager.STATUS_OK_BAD_EMAIL) {
						miroFreeAssessmentReturnDto.addError("BAD_EMAIL");
					} else if (newMiroCandidateAjaxDTO.getStatus() == AjaxMiroProjectManager.STATUS_DUPE) {
						log.error("error on saveCandidateAndEmail" + newMiroCandidateAjaxDTO.getStatus());
						miroFreeAssessmentReturnDto.addError("DUPE");
					} else {
						miroFreeAssessmentReturnDto.addError("GENERAL");
					}
				}
			}

		} catch(Exception e) {
		   log.error("Exception " + e.getMessage());
		   miroFreeAssessmentReturnDto.addError("GENERAL");
		   miroFreeAssessmentReturnDto.setStatus(MiroFreeAssessmentReturnDto.FAIL);
		}

		return new ModelAndView("jsonView", "dto", miroFreeAssessmentReturnDto);

	}

	private boolean isValidCapatcha(int capatcha,String captchaAnswer) {
		if(captchaAnswer!=null & captchaAnswer.trim().equals(capatchaAnswers[capatcha])) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isBlank(String val) {

		if(val ==null || val.trim().equals("")) {
			return true;
		} else {
			return false;
		}

	}

	private String getProjectId() {

		Setting urlSetting = settingManager.getSettingByName(Constants.SETTING_FREE_ASSESSMENT_PROJECT_ID);

		if(urlSetting == null) {
			log.error("Could no find FREE_ASSESSMENT_PROJECT_ID");
			return null;
		}

		String url = urlSetting.getSettingValue();

		return url;
	}

}
