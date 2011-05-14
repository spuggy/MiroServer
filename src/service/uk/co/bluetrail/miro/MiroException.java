package uk.co.bluetrail.miro;



public class MiroException extends RuntimeException {
  
	private Exception e ;
	private String eMessage;
	public MiroException(String eMessage, Exception e) {
		this.eMessage = eMessage;
		this.e = e ;
		
	}
	
	public MiroException(String eMessage) {
		this.eMessage = eMessage;
		this.e = null; 
		
	}

	public String toString() {
		if(e != null) {
			return "MIRO_ERROR: " + eMessage;
		} else {
			return "MIRO_ERROR: " + eMessage + " - " + e.toString();
		}
	}
	
}
