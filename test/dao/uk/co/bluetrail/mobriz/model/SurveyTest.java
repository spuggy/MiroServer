package uk.co.bluetrail.mobriz.model;

import junit.framework.TestCase;

public class SurveyTest extends TestCase {

	public void testEmptyConstructor() throws Exception {
		
		Survey s = new Survey() ;
		assertNotNull(s);

	}    
	
	
	public void testGetSetLastZipResponse() {
		
		Survey s = new Survey() ;
						
		s.setLastZipResponse_id(new Long(100));
		
		Long lastZipResponse_id = s.getLastZipResponse_id(); 
		
		assertEquals(100L,lastZipResponse_id.longValue());
		
	}
	
	public void testGetSetLastPhotoStampResponse() {
		
		Survey s = new Survey() ;
						
		s.setLastPhotoStampResponse_id(new Long(100));
		
		Long lastPhotoStampResponse_id = s.getLastPhotoStampResponse_id(); 
		
		assertEquals(100L,lastPhotoStampResponse_id.longValue());
		
	}
	
	
	
}
