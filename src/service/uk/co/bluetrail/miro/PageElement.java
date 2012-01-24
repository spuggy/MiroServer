package uk.co.bluetrail.miro;

import java.util.ArrayList;
import java.util.List;

public class PageElement {
	
	public static PageElement[] create(String id) {
	
		PageElement[] tmp = new PageElement[1];
		
		tmp[0] = new PageElement(id);
		
		return tmp;
	}
	
	
	String id;
	String suffix ;
	
	public PageElement(String id) {
		this.id = id;
		this.suffix = null;
	}
	
	public PageElement(String id,String suffix) {
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
		PageElement x = new PageElement("helllo") ;
		
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
	
	

}
