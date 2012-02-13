package uk.co.bluetrail.miro;

import java.util.ArrayList;


public class MiroPage {
	
	ArrayList<MiroPageElement> pageItems = new ArrayList<MiroPageElement>();

	public static MiroPage create(String id) {
		
		MiroPage mp = new MiroPage();
		mp.add(id);
		
		return mp;
	}
	
	
	
	
	public void add(String id) {
		pageItems.add(new MiroPageElement(id));
	}
	
	public void add(String id,String suffix) {
		pageItems.add(new MiroPageElement(id,suffix));
	}


	public void add(MiroPageElement mpe) {
		pageItems.add(mpe);
		
	}


	public int getLength() {
		return this.pageItems.size();
	}




	public MiroPageElement get(int idx) {
		return this.pageItems.get(idx);
	}




	
	
}
