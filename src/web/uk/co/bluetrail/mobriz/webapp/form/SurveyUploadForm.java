package uk.co.bluetrail.mobriz.webapp.form;


/**
 * Command class to handle uploading of a file.
 *
 * <p>
 * <a href="SurveyUploadForm.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class SurveyUploadForm {
   
    private byte[] file;

   
    public void setFile(byte[] file) {
        this.file = file;
    }

    public byte[] getFile() {
        return file;
    }
}
