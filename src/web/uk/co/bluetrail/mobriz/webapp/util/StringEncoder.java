package uk.co.bluetrail.mobriz.webapp.util;

import org.apache.commons.codec.binary.Base64;

public class StringEncoder {

	
	public static String encode64(String str) {
		byte[] b64 =  Base64.encodeBase64(str.getBytes());
		
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < b64.length; i++) {
			sb.append((char) b64[i]);
		}
		return sb.toString();
	}
	
	public static String decode64(String str) {
		byte[] b64 =  Base64.decodeBase64(str.replaceAll(" ","+").getBytes());
		
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < b64.length; i++) {
			sb.append((char) b64[i]);
		}
		return sb.toString();	
	}
	
	public static String simpleXor(String strIn,String strKey) {
		
		if (strIn.length()==0 || strKey.length()==0) {
			return "" ;
		}
		
	int iInIndex=0 ;
	int iKeyIndex=0 ;
	StringBuffer strReturn = new StringBuffer();
	
	int inAsc = 0 ;
	int keyAsc =0 ;  
	

		
	//** Step through the plain text source XORing the character at each point with the next character in the key **
	//** Loop through the key characters as necessary **
	do {
		
		inAsc = (int) strIn.charAt(iInIndex);
		keyAsc = (int) strKey.charAt(iKeyIndex);
		
		strReturn.append((char) (inAsc ^ keyAsc)) ;
		iInIndex = iInIndex + 1;
        iKeyIndex = iKeyIndex + 1;
        if (iKeyIndex < strKey.length()) {
			// do nowt
		} else {
			iKeyIndex = 0;
		}
	} while (iInIndex<strIn.length());
 
    return strReturn.toString();
	}
}
