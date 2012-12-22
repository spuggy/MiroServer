package uk.co.bluetrail.mobriz.service;

import uk.co.bluetrail.mobriz.dao.SurveyDAO;

import java.util.List;

public interface MiroStatsReportManager {


    public void setSurveyDAO(SurveyDAO surveyDAO);


    public List getMonthTotals(int month_selected, int year_selected) ;
    public List getMonthDetails(int month_selected, int year_selected) ;


}