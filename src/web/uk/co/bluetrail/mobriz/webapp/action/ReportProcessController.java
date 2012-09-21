package uk.co.bluetrail.mobriz.webapp.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import uk.co.bluetrail.miro.MiroResponse;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.service.*;
import uk.co.bluetrail.mobriz.webapp.util.RequestUtil;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ReportProcessController extends BatchProcessController {
	 private final Log log = LogFactory.getLog(ReportProcessController.class);


    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }


        ArrayList reportProcessResults = new ArrayList();

			try {
                String last_id_str = request.getParameter("last_id");

                if(last_id_str != null) {
                    Long last_id = Long.parseLong(last_id_str);
                    processStatsExport(reportProcessResults, miroResponseManager.getMiroReportPath(RequestUtil.getAppURL(request)),last_id,this.miroDocLimit);
                } else {
                    log.error("Exception processing individual no last_id");
                    reportProcessResults.add("Exception processing individual no last_id");
                }


			} catch (Exception e) {
				log.error("Exception processing individual pdfs: " + e.toString());
				reportProcessResults.add("Exception processing individual pdfs: " + e.toString());
			}



		HashMap model = new HashMap();
		model.put("batchProcessResults", reportProcessResults);

        return new ModelAndView("batchProcessResults",model);
    }

    private void processStatsExport(ArrayList reportProcessResults, String miroReportPath, Long last_id, int limit ) {

        List responses = this.miroResponseManager.getSurveyResponsesGreaterThanId(last_id,limit);

        Iterator itr = responses.iterator();
        if(!itr.hasNext()) {
            log.debug("No miro reports to process");
        }

        SurveyResponse sr = null;
        MiroResponse mr = null;
        String lop = null;

        while(itr.hasNext()){
            sr = (SurveyResponse) itr.next();

            mr = new MiroResponse();
            try {
                lop = this.miroResponseManager.getRawResults( sr,  mr,miroReportPath);
                reportProcessResults.add(lop);


            } catch (Exception e) {
                reportProcessResults.add("ERROR  processing id=" + sr.getId()+ ": " + e.getMessage());
            }


        }

        log.debug("end processing miro team reports ");



    }


}