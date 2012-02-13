package uk.co.bluetrail.miro;

import java.util.ArrayList;
import java.util.List;

public class MiroPageElement {
	
	
	
	
	String id;
	String suffix ;
	
	public MiroPageElement(String id) {
		this.id = id;
		this.suffix = null;
	}
	
	public MiroPageElement(String id,String suffix) {
		this.id = id;
		this.suffix = suffix;
	}
	
	public String toString() {
		if(suffix==null) {
			return id;
		} else {
			return id + "_" + suffix;
		}
	}
	
	public static void main(String args[]) { 
		MiroPageElement x = new MiroPageElement("helllo") ;
		
		System.out.println("parp " + x);
		
		
	}

	public String getId() {
		return id;
	}

	public String addSuffix(String id) {
		
		if(suffix!=null) {
			return id + "_" + suffix;
		} else {
			return id;
		}
		
	}
	
	public String getSuffix() {
		if(suffix==null) {
			return "";
		} else {
			return suffix;
		}
	}

	
	

}
