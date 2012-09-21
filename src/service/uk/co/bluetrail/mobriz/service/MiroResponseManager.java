package uk.co.bluetrail.mobriz.service;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;

import uk.co.bluetrail.miro.MiroReport;
import uk.co.bluetrail.miro.MiroResponse;
import uk.co.bluetrail.mobriz.model.MiroProject;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.SurveyResponse;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.serviceDTO.TeamMapDTO;

public interface MiroResponseManager extends Manager{
	
	public boolean isValid(SurveyResponse sr) ;
	public List getUnprocessedMiroResponses(int miroDocLimit);
	public boolean createPDF(SurveyResponse sr, MiroResponse m,  String filePath) throws Exception;
	public String getMiroReportPath(String appURL);
	public BufferedImage getMiroTeamPie(String baseDirectory,String[] userIds) throws Exception;
	public List getTeamMap(String baseDir, Collection userList) ;
	public Setting getMiroLetters();
	public void createPie(MiroResponse miroResponse, String filePath,boolean plain);
    public List getSurveyResponsesGreaterThanId(Long last_id,int limit) ;
    public String getRawResults(SurveyResponse sr, MiroResponse mr,String baseDirectory) throws Exception;

    
}
