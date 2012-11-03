package uk.co.bluetrail.mobriz.service.impl;

import uk.co.bluetrail.mobriz.dao.SurveyDAO;
import uk.co.bluetrail.mobriz.service.MiroStatsReportManager;

import java.util.List;


public class MiroStatsReportManagerImpl extends BaseManager implements MiroStatsReportManager {
 
	private SurveyDAO dao = null;


    public void setSurveyDAO(SurveyDAO surveyDAO) {
        this.dao =   surveyDAO;
    }

    public List getMonthTotals(int month_selected, int year_selected) {

        String[] fieldNames =  new String[] {"month_selected","year_selected"} ;
        Object[] objects = new Object[] {month_selected,year_selected};

        return  dao.getSurveyNamedQuery("findMonthTotals",fieldNames,objects );

    }

    public List getMonthDetails(int month_selected, int year_selected) {

        String[] fieldNames =  new String[] {"month_selected","year_selected"} ;
        Object[] objects = new Object[] {month_selected,year_selected};

        return dao.getSurveyNamedQuery("findMonthDetails",fieldNames,objects );

    }


}
