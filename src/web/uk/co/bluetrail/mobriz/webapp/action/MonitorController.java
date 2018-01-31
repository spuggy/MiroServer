package uk.co.bluetrail.mobriz.webapp.action;

import org.springframework.web.servlet.ModelAndView;
import uk.co.bluetrail.mobriz.service.MiroResponseManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class MonitorController {

    protected MiroResponseManager miroResponseManager ;

    public void setMiroResponseManager(MiroResponseManager miroResponseManager) {
        this.miroResponseManager = miroResponseManager;
    }

    

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String minsStr = request.getParameter("mins");

        int mins = getMins(minsStr);


        List unprocessedResponses = miroResponseManager.getUnprocessedMiroResponses(5);




        return null;

        //return new ModelAndView("miroProjectList", unprocessedResponses);


    }

    private int getMins(String minsStr) {
        try {
           return Integer.parseInt(minsStr);
        } catch (Exception e) {
            return 120;
        }


    }


}
