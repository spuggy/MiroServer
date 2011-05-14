package uk.co.bluetrail.mobriz.webapp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Account;
import uk.co.bluetrail.mobriz.model.LookupDef;
import uk.co.bluetrail.mobriz.model.MiroTransaction;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.MiroTransactionManager;
import uk.co.bluetrail.mobriz.service.SettingManager;
import uk.co.bluetrail.mobriz.util.SurveyElementUtil;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;
import uk.co.bluetrail.mobriz.webapp.util.StringEncoder;

public class AddCreditsFormController extends BaseFormController {

	private MiroTransactionManager miroTransactionManager = null;
	private SettingManager settingManager;
	
	private String vpsPostUrl;

	private String vpsProtocol;

	private String txType;

	private String vendor;

	private String encrypPassword;
	
	private String currency ;
	
	private String successURL  ;
	
	private String failureURL;
	
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
        
		MiroTransaction  miroTransaction = new MiroTransaction();
		
		miroTransaction.setCredits(1);

        return miroTransaction;
    }
	
	

	/**
	 * @hibernate.property 
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @hibernate.property 
	 * @return the failureURL
	 */
	public String getFailureURL() {
		return failureURL;
	}

	/**
	 * @param failureURL the failureURL to set
	 */
	public void setFailureURL(String failureURL) {
		this.failureURL = failureURL;
	}

	/**
	 * @hibernate.property 
	 * @return the successURL
	 */
	public String getSuccessURL() {
		return successURL;
	}

	/**
	 * @param successURL the successURL to set
	 */
	public void setSuccessURL(String successURL) {
		this.successURL = successURL;
	}

	/**
	 * @param miroTransactionManager
	 *            the miroTransactionManager to set
	 */
	public void setMiroTransactionManager(
			MiroTransactionManager miroTransactionManager) {
		this.miroTransactionManager = miroTransactionManager;
	}

	public AddCreditsFormController() {
		setCommandName("miroTransaction");
		setCommandClass(MiroTransaction.class);
	}
	
	protected Map referenceData(HttpServletRequest request)  throws Exception {
	    	
	    	Map refData = new HashMap() ;
	    	
	    	String projectId = request.getParameter("pid");
			
			if(projectId !=null) {
				refData.put("projectId", projectId);
			}
	    	
	    	Setting miroCreditPriceSetting = settingManager.getSettingByName(Constants.MIRO_CREDIT_PRICE);
	    	
	    	refData.put("miroCreditPrice",miroCreditPriceSetting.getSettingValue());
	    	
	    	return refData;
	    }

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		log.debug("adding credit for " + this.getCurrentUser().getFullName());
		
		Setting miroCreditPriceSetting = settingManager.getSettingByName(Constants.MIRO_CREDIT_PRICE);
		double miroCreditPrice = Double.parseDouble(miroCreditPriceSetting.getSettingValue());
		
		MiroTransaction miroTransaction = (MiroTransaction) command;

		SurveyElementUtil.timeStamp(miroTransaction, this.getCurrentUser());

		miroTransaction.setUser_id(this.getCurrentUser().getId());
		
		
		miroTransaction.setTransValue(miroCreditPrice * miroTransaction.getCredits());

		miroTransactionManager.saveMiroTransaction(miroTransaction);

		HashMap model = new HashMap();

		String url = RequestUtil.getAppURL(request);
		
		model.put("miroCreditPrice", miroCreditPriceSetting.getSettingValue());
		model.put("miroTransaction", miroTransaction);
		model.put("VPSPostUrl", vpsPostUrl);
		model.put("VPSProtocol", vpsProtocol);
		model.put("TxType", txType);
		model.put("Vendor", vendor);
		model.put("crypt", getCryptString(miroTransaction,url,this.getCurrentUser()));

		return new ModelAndView(getSuccessView(), model);
		
		
	}

	private String getCryptString(MiroTransaction miroTransaction, String url, User user) {
		
		
		String SEP = "&";
		
		StringBuffer crypt = new StringBuffer();
		
		crypt.append("VendorTxCode=");
		crypt.append(miroTransaction.getId());
		crypt.append(SEP);
		crypt.append("Amount=");
		crypt.append(miroTransaction.getTransValueStr());
		crypt.append(SEP);
		crypt.append("Currency=");
		crypt.append(this.getCurrency());
		crypt.append(SEP);
		crypt.append("Description=");
		crypt.append(miroTransaction.getCredits() + " Miro Assessment Credits");
		crypt.append(SEP);
		crypt.append("SuccessURL=");
		crypt.append(url+this.getSuccessURL());
		crypt.append(SEP);
		crypt.append("FailureURL=");
		crypt.append(url+this.getFailureURL());
		crypt.append(SEP);
		crypt.append("CustomerEmail=");
		crypt.append(user.getEmail());
		
		String cryptyXOR = StringEncoder.simpleXor(crypt.toString(),this.getEncrypPassword());
		
		String b64 =  StringEncoder.encode64(cryptyXOR);
		 
		return b64;
	}
	
	

	/**
	 * @hibernate.property 
	 * @return the encrypPassword
	 */
	public String getEncrypPassword() {
		return encrypPassword;
	}

	/**
	 * @param encrypPassword the encrypPassword to set
	 */
	public void setEncrypPassword(String encrypPassword) {
		this.encrypPassword = encrypPassword;
	}

	/**
	 * @hibernate.property 
	 * @return the txType
	 */
	public String getTxType() {
		return txType;
	}

	/**
	 * @param txType the txType to set
	 */
	public void setTxType(String txType) {
		this.txType = txType;
	}

	/**
	 * @hibernate.property 
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * @hibernate.property 
	 * @return the vpsPostUrl
	 */
	public String getVpsPostUrl() {
		return vpsPostUrl;
	}

	/**
	 * @param vpsPostUrl the vpsPostUrl to set
	 */
	public void setVpsPostUrl(String vpsPostUrl) {
		this.vpsPostUrl = vpsPostUrl;
	}

	/**
	 * @hibernate.property 
	 * @return the vpsProtocol
	 */
	public String getVpsProtocol() {
		return vpsProtocol;
	}

	/**
	 * @param vpsProtocol the vpsProtocol to set
	 */
	public void setVpsProtocol(String vpsProtocol) {
		this.vpsProtocol = vpsProtocol;
	}



	/**
	 * @param settingManager the settingManager to set
	 */
	public void setSettingManager(SettingManager settingManager) {
		this.settingManager = settingManager;
	}
	
}
