package uk.co.bluetrail.mobriz.webapp.ajaxDTO;

import java.util.ArrayList;

/**
 * Created by richard on 22/10/2016.
 */
public class MiroFreeAssessmentReturnDto {

    public static int OK = 1;
    public static int FAIL = 2;

    private ArrayList<String> formErrors;
    private int status = FAIL;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<String> getFormErrors() {
        return formErrors;
    }

    public void addError(String error) {
        if(formErrors==null) {
            formErrors = new ArrayList<String>();
        }

        formErrors.add(error);

    }
}
