package uk.co.bluetrail.mobriz.service;

import uk.co.bluetrail.miro.MiroResponse;
import uk.co.bluetrail.mobriz.model.Setting;
import uk.co.bluetrail.mobriz.model.SurveyResponse;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;

public interface MiroResponseManager extends Manager{
	
	public boolean isValid(SurveyResponse sr) ;
	public List getUnprocessedMiroResponses(int miroDocLimit);
	public boolean createPDF(SurveyResponse sr, MiroResponse m,  String filePath) throws Exception;
	public String getMiroReportPath(String appURL);
	public BufferedImage getMiroTeamPie(String baseDirectory,String[] userIds) throws Exception;
	public List getTeamMap(String baseDir, Collection userList) ;
	public Setting getMiroLetters();
	public void createPie(MiroResponse miroResponse, String filePath,boolean plain);
    public String getRawResults(SurveyResponse sr, MiroResponse mr,String baseDirectory) throws Exception;

    
}
