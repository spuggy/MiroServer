package uk.co.bluetrail.mobriz.dao;

import java.util.List;

import uk.co.bluetrail.mobriz.model.Survey;
import uk.co.bluetrail.mobriz.model.SurveyResponse;

/**
 * This class tests the current LookupDao implementation class
 * @author mraible
 */
public class SurveyResponseDaoTest extends BaseDaoTestCase {
    private SurveyResponseDAO dao;
    
    
    public void setSurveyResponseDAO(SurveyResponseDAO dao) {
        this.dao = dao;
    }
    
    
    public void testGetSurveyResponsesByExample(){

    	SurveyResponse sr = new SurveyResponse();
    	sr.setDeleted(false);
    	sr.setSurvey_id(new Long(20));
    	sr.setPhotosStamped(false);
    
    	assertNotNull(dao);
    	
    	List srs = dao.getSurveyResponsesByExample(sr);
    	assertNotNull(srs);
    	assertTrue(srs.size() > 0);
    	
    	SurveyResponse sr2 = (SurveyResponse) srs.get(0);
    	assertFalse(sr2.isPhotosStamped());
    	
    	
    	
    }
    
    
    public void testGetSurveyResponsesGreaterThanId(){
    	
    	Survey s = new Survey();
    	s.setId(new Long(20));
    	
    	List srs = dao.getSurveyResponsesGreaterThanId(s, new Long(10));
    	
    	assertNotNull(srs);
    	assertTrue(srs.size() > 0);
    	
    	SurveyResponse sr2 = (SurveyResponse) srs.get(0);
    	assertTrue(sr2.getId().longValue() > 1L);
    }
   
}
