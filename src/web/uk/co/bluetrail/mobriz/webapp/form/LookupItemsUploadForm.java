package uk.co.bluetrail.mobriz.webapp.form;


/**
 * Command class to handle uploading of a file.
 *
 * <p>
 * <a href="LookupItemsUploadForm.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:rspence@bluetrail.co.uk">Richard Spence</a>
 */
public class LookupItemsUploadForm {
   
    private byte[] file;
    String lookupDef_id ;
    
   
    public String getLookupDef_id() {
		return lookupDef_id;
	}

	public void setLookupDef_id(String lookupDef_id) {
		this.lookupDef_id = lookupDef_id;
	}

	public void setFile(byte[] file) {
        this.file = file;
    }

    public byte[] getFile() {
        return file;
    }
}
