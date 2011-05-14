package uk.co.bluetrail.mobriz.webapp.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Account;
import uk.co.bluetrail.mobriz.model.LookupDef;
import uk.co.bluetrail.mobriz.model.LookupDefItem;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyException;
import uk.co.bluetrail.mobriz.service.LookupDefItemManager;
import uk.co.bluetrail.mobriz.service.LookupDefManager;
import uk.co.bluetrail.mobriz.service.SurveyManager;
import uk.co.bluetrail.mobriz.webapp.form.LookupItemsUploadForm;
import uk.co.bluetrail.mobriz.webapp.form.SurveyUploadForm;
import uk.co.bluetrail.mobriz.webapp.util.FileUploadReader;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller class to upload Files.
 *
 * <p>
 * <a href="FileUploadFormController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class LookupItemsUploadFormController extends BaseFormController {

	
	private LookupDefManager lookupDefManager = null;
	private LookupDefItemManager lookupDefItemManager = null;  
	public static final String TAB = "\t";
	
	public void setLookupDefManager(LookupDefManager lookupDefManager) {
	        this.lookupDefManager = lookupDefManager;
	}

	protected Map referenceData(HttpServletRequest request)  throws Exception {

		String id = request.getParameter("id");
        LookupDef lookupDef = null;
        List lookupDefItemList = null; 
        
        
        if (!StringUtils.isEmpty(id)) {
        	lookupDef = lookupDefManager.getLookupDef(id);
        	LookupDefItem lookupDefItem = new LookupDefItem() ; 
        	lookupDefItem.setLookupDef_id(lookupDef.getId());
        	lookupDefItemList = lookupDefItemManager.getLookupDefItemsQBE(lookupDefItem);
        } else {
        	lookupDef= new LookupDef();
        }

        
        
        
    	Map refData = new HashMap() ;
    	
    	refData.put("lookupDef" , lookupDef) ;
    	refData.put("lookupDefItemList" , lookupDefItemList) ;
    	
    	
    	return refData;
    }
	
			
    public LookupItemsUploadFormController() {
        setCommandName("lookupItemsUploadForm");
        setCommandClass(LookupItemsUploadForm.class);
    }
    
    
    
    public ModelAndView processFormSubmission(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Object command,
                                              BindException errors)
    throws Exception {
       

        return super.processFormSubmission(request, response, command, errors);
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
    	
    	
    	int lineCount = 0 ;
    	
    	try {
    	
    	LookupItemsUploadForm theForm = (LookupItemsUploadForm) command;
    	
    	LookupDefItem lookupDefItem = null;
    	
        // validate a file was entered
        if (theForm.getFile().length == 0) {
            Object[] args = 
                new Object[] { getText("LookupItemsUploadForm.file", request.getLocale()) };
            errors.rejectValue("file", "errors.required", args, "File");
            
            return showForm(request, response, errors);
        }

        MultipartHttpServletRequest multipartRequest =   (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");

  
        // retrieve the file name
        String fileName = file.getName();



        // retrieve the file data
        InputStream stream = file.getInputStream();

        


        FileUploadReader fileUploadReader = new FileUploadReader(stream);
        
        String line = fileUploadReader.getNextLine();
        
        lineCount++;
       
        
        LookupDef lookupDef = lookupDefManager.getLookupDef(theForm.getLookupDef_id());
        
        //assume this is the header line.
        int[] fieldIndex = lookupDef.getFieldIndex(line.split(TAB));  
        
        
        line = fileUploadReader.getNextLine();
        
        lineCount++;

        while(line != null && !line.equals("")) {
        	
        	lookupDefItem = new LookupDefItem();
        	lookupDefItem.setLookupDef_id(lookupDef.getId());        	
        	lookupDefItem.copy(fieldIndex,line.split(TAB));        	
        	lookupDefItemManager.saveLookupDefItem(lookupDefItem, this.getCurrentUser());
        	
        	line = fileUploadReader.getNextLine();
        	lineCount++;
        }
        		

        
        
  

        // close the stream
        stream.close();

        //request.setAttribute("sid", uploadedSurvey.getId());
        request.setAttribute("fileName", fileName);
        
   
        saveMessage(request, getText("lookupDef.importSuccess", request.getLocale()));

       
        return new ModelAndView("redirect:lookupItemsUpload.html", "id", lookupDefItem.getLookupDef_id());
        
        
    	} catch (SurveyException se) {
    		
    		Object[] args =  new Object[] { se.toString(), new Integer(lineCount), se.toString() };
        	  
            errors.reject("lookupDefItem.importErrors",args,se.toString() );
            
            return showForm(request, response, errors);
  	
    		
    		
    	} catch(Exception e) {
        	
    		log.error("Exception on lookupDef Import; " + e.toString());
      	  
    		Object[] args =  new Object[] { new Integer(lineCount),  new Integer(lineCount), e.toString() };
      	  
            errors.reject("lookupDefItem.importException",args,e.toString() );
            
            return showForm(request, response, errors);
  	

      }      	
        
        
    }

	public void setLookupDefItemManager(LookupDefItemManager lookupDefItemManager) {
		this.lookupDefItemManager = lookupDefItemManager;
	}
}


		
