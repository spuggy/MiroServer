package uk.co.bluetrail.mobriz.webapp.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import uk.co.bluetrail.mobriz.model.SurveyElement;
import uk.co.bluetrail.mobriz.service.MiroResponseManager;
import uk.co.bluetrail.mobriz.service.MiroTeamReportManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

public class BatchProcessorMonitorController implements Controller {

    private final Log log = LogFactory.getLog(BatchProcessorMonitorController.class);

    protected MiroResponseManager miroResponseManager ;
    protected int miroDocLimit;
    protected long tardyLimitInMs;
    protected MiroTeamReportManager miroTeamReportManager =null;

    public void setMiroResponseManager(MiroResponseManager miroResponseManager) {
        this.miroResponseManager = miroResponseManager;
    }

    public void setMiroDocLimit(int miroDocLimit) {
        this.miroDocLimit = miroDocLimit;
    }

    public void setTardyLimitInMs(long tardyLimitInMs) {
        this.tardyLimitInMs = tardyLimitInMs;
    }

    public void setMiroTeamReportManager(MiroTeamReportManager miroTeamReportManager) {
        this.miroTeamReportManager = miroTeamReportManager;
    }
    

    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        log.debug("processing BatchProcessorMonitorController ");
        List unprocessedResponses = miroResponseManager.getUnprocessedMiroResponses(miroDocLimit);
        List unprocessedTeamResponses = miroTeamReportManager.getUnprocessedTeams(miroDocLimit);

        boolean tardyResponses = false;
        if(unprocessedResponses != null && unprocessedResponses.size() >0) {
            tardyResponses = isTardy((SurveyElement)unprocessedResponses.get(0));
        }

        boolean tardyTeams = false;
        if(unprocessedTeamResponses != null && unprocessedTeamResponses.size() >0) {
            tardyTeams = isTardy((SurveyElement) unprocessedTeamResponses.get(0));
        }



        HashMap model = new HashMap();
        model.put("unprocessedResponses", unprocessedResponses);
        model.put("unprocessedTeamResponses", unprocessedTeamResponses);
        model.put("status", tardyResponses || tardyTeams ? "STATUS_TARDY" : "STATUS_OK");
        model.put("unprocessedTeamResponses", unprocessedTeamResponses);


        return new ModelAndView("batchProcessorMonitor", model) ;

    }

    private boolean isTardy(SurveyElement surveyElement) {

        long now = System.currentTimeMillis();
        long lastUpdated = surveyElement.getUpdated_at().getTime();
        long window = now - tardyLimitInMs;
        if(lastUpdated > window)  {
            return false;
        } else {
            return true;
        }

    }
}
