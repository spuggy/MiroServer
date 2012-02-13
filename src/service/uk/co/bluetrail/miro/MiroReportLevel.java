package uk.co.bluetrail.miro;

import java.util.ArrayList;
import java.util.Iterator;

public class MiroReportLevel {

	private ArrayList<Level> levels = new ArrayList<Level>();
	
	public MiroReportLevel(String[] init) {
		
		try {
			
			levels.add(new Level(init[0],init[1],init[2]));
			levels.add(new Level(init[3],init[4],init[5]));
			levels.add(new Level(init[6],init[7],init[8]));
			levels.add(new Level(init[9],init[10],init[11]));
			
			
		} catch (Exception e) {
			throw new RuntimeException("Exception parsing miro Report Leveles" + e.getMessage());
		}
		
		
	}

	public String getLevel(int v) {
		
		Iterator itr = levels.iterator() ;
		
		while(itr.hasNext()) {
			
			Level l = (Level) itr.next();
			if (l.isMatch(v)) {
				return l.level;
			}
			
		}
		
		throw new RuntimeException("could not find level for " + v);
		
	}
	
}

class Level {
	
	public String level;
	public int upper;
	public int lower ;
	
	public Level(String level, String upper, String lower){
		this.upper = Integer.parseInt(upper);
		this.lower = Integer.parseInt(lower);
		this.level = level;
	}

	public boolean isMatch(int v) {
		if(v>=lower && v<=upper) {
			return true;
		} else {
			return false;
		}
	}
	
}
