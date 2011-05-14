package uk.co.bluetrail.mobriz.webapp.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.co.bluetrail.mobriz.Constants;
import uk.co.bluetrail.mobriz.model.Account;
import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyException;
import uk.co.bluetrail.mobriz.service.SurveyManager;
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
public class SurveyUploadController extends BaseFormController {

	private int maxImportLines ;
	
	private SurveyManager surveyManager = null;
	  
	
	
	public void setMaxImportLines(int maxImportLines) {
		this.maxImportLines = maxImportLines;
	}



	public void setSurveyManager(SurveyManager surveyManager) {
	        this.surveyManager = surveyManager;
	}

	
	
    public SurveyUploadController() {
        setCommandName("surveyUploadForm");
        setCommandClass(SurveyUploadForm.class);
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
        SurveyUploadForm theForm = (SurveyUploadForm) command;

        // validate a file was entered
        if (theForm.getFile().length == 0) {
            Object[] args = 
                new Object[] { getText("surveyUploadForm.file", request.getLocale()) };
            errors.rejectValue("file", "errors.required", args, "File");
            
            return showForm(request, response, errors);
        }

        MultipartHttpServletRequest multipartRequest =   (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");

  
        // retrieve the file name
        String fileName = file.getName();



        // retrieve the file data
        InputStream stream = file.getInputStream();

        int lineCount = 0 ;


        FileUploadReader fileUploadReader = new FileUploadReader(stream);
        
        String line = fileUploadReader.getNextLine();
        Vector fileLines = new Vector();

         

        while(line != null) {
        	fileLines.addElement(line);
        	if(lineCount++ > maxImportLines) {
        		throw new SurveyException("Maximum number lines ("  + maxImportLines + ") exceeded") ;				
        	}
        	line = fileUploadReader.getNextLine();
        }
        		
        Survey uploadedSurvey = new Survey() ;

        try {
        	surveyManager.saveSurvey(uploadedSurvey,fileLines,getCurrentUser());
        } catch(Exception e) {
        	
        	System.out.println("errr=" + e.toString());
        	
        	  Object[] args =  new Object[] { e.toString(), e.toString(), e.toString() };
        	  
              errors.reject("surveyUpload.errors",args,e.toString() );
              
              return showForm(request, response, errors);
    	

        }

        // close the stream
        stream.close();

        // place the data into the request for retrieval on next page		
        request.setAttribute("sid", uploadedSurvey.getId());
        request.setAttribute("fileName", fileName);
        request.setAttribute("surveyTitle", uploadedSurvey.getTitle());
        
        
        return new ModelAndView(getSuccessView());
    }
}


		
