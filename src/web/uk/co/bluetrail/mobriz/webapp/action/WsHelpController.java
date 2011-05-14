package uk.co.bluetrail.mobriz.webapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.service.SettingManager;

public class WsHelpController extends BaseController {

	 private final Log log = LogFactory.getLog(WsHelpController.class);
	    private SettingManager settingManager = null;

	    public void setSettingManager(SettingManager settingManager) {
	        this.settingManager = settingManager;
	    }

	
	    public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		
	    	  Setting urlSetting = settingManager.getSettingByName(Constants.SETTING_DOWNLOADURL);
	          
	    	  if(urlSetting == null) {
	    		  log.error("Could no find ETTING_DOWNLOADURL");
	    		  return null;
	    	  }
	    	  
	    	String url = urlSetting.getSettingValue(); 
		
		
	    	 return new ModelAndView("wsHelp", "url", url);
	}

}
