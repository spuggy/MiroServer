package uk.co.bluetrail.mobriz.webapp.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import uk.co.bluetrail.mobriz.service.MiroDepartmentAccountsReportManager;
import uk.co.bluetrail.mobriz.webapp.util.DeptAccountReportRow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


public class MiroDepartmentAccountsController  extends BaseController {
    
private final Log log = LogFactory.getLog(MiroDepartmentAccountsController.class);

    private MiroDepartmentAccountsReportManager miroDepartmentAccountsReportManager = null;

    public void setMiroDepartmentAccountsReportManager(MiroDepartmentAccountsReportManager miroDepartmentAccountsReportManager) {
        this.miroDepartmentAccountsReportManager = miroDepartmentAccountsReportManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        Calendar calendar = Calendar.getInstance();

        String year_selected_str = request.getParameter("year_selected");
        String month_selected_str = request.getParameter("month_selected");

        int year_selected =  0;
        int month_selected  = 0;

        try {
            year_selected = Integer.parseInt(year_selected_str);
            month_selected = Integer.parseInt(month_selected_str) ;
        }  catch(Exception e) {
            year_selected = calendar.get(Calendar.YEAR);
            month_selected = calendar.get(Calendar.MONTH)+1;
         }

        String[] mths = {"","January","February","March","April","May","June","July","August","September","October","November","December"};
        month_selected_str = mths[ month_selected];


		HashMap pracs = new HashMap();
        String department = this.getCurrentUser().getDepartment();

		//add the monthtotals to the report
        List indTotals = miroDepartmentAccountsReportManager.getMonthTotals(department,month_selected,year_selected) ;
        List teamTotals = miroDepartmentAccountsReportManager.getMonthTeamTotals(department,month_selected,year_selected) ;
        addMonthTotals(pracs,indTotals,teamTotals);


		ArrayList l = createList(pracs);



        Map model = new HashMap() ;
        model.put("reportLines" , l) ;
        model.put("year_selected" , year_selected) ;
        model.put("month_selected" , month_selected) ;
        model.put("department",department);
        model.put("month_selected_str" , month_selected_str) ;
        return new ModelAndView("miroDepartmentAccounts", model);
    }


    /**
     *
     * add the team and individual totals
     *
     * @param pracs
     * @param indTotals
     * @param teamTotals
     */
    private void addMonthTotals(HashMap pracs,List indTotals,List teamTotals) {

        Iterator itr = indTotals.iterator();

        while(itr.hasNext()) {

            Object[] obj = (Object[]) itr.next();

            DeptAccountReportRow row = (DeptAccountReportRow) pracs.get(obj[0]);

            if(row==null) {
                row = new DeptAccountReportRow(obj[0]);
                row.setIndnumber(obj[1])  ;
                pracs.put(obj[0],row);

            } else {
                row.setIndnumber( obj[1]);
            }


        }


        itr = teamTotals.iterator();

        while(itr.hasNext()) {

            Object[] obj = (Object[]) itr.next();

            DeptAccountReportRow row = (DeptAccountReportRow) pracs.get(obj[0]);

            if(row==null) {
                row = new DeptAccountReportRow(obj[0]);
                row.setTeamnumber(obj[1]);
                pracs.put(obj[0],row);

            } else {
                row.setTeamnumber( obj[1]);
            }


        }


    }


    private ArrayList createList(HashMap pracs) {

        ArrayList l = new ArrayList();

        Iterator itr = pracs.keySet().iterator();

        while(itr.hasNext()) {

            String key = (String) itr.next()  ;

            DeptAccountReportRow row = (DeptAccountReportRow) pracs.get(key);


            l.add(row);


        }


        return l;


    }




}




