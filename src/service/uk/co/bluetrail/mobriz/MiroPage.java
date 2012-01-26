package uk.co.bluetrail.mobriz;

import java.util.ArrayList;

import uk.co.bluetrail.miro.MiroPageElement;

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




	public int getLength() {
		return this.pageItems.size();
	}




	public MiroPageElement get(int idx) {
		return this.pageItems.get(idx);
	}
	
}
