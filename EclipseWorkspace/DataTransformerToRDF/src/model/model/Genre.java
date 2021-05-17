package model.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import model.IRDFGenerator;
import model.Triplet;
import services.Utils;


public class Genre implements IRDFGenerator{
	
	private static final AtomicInteger count = new AtomicInteger(0); 
	private String key ;
	private final int id;
	
	public String label;
	
	public Genre(String token) {
		label = token;
		
		id = count.incrementAndGet();
		key = "Genre"+id;
		// key= Utils.removeAccent(label);
	}

	@Override
	public List<Triplet> generateRDFTriplet() {
		List<Triplet> result = new ArrayList<Triplet>();
		String s = ":" + key; 
		String p = "rdfs:label";
		String o = '"'+this.label+'"';
		if (label != null) {
			result.add(new Triplet(s, p, o));			
		}
		return result;
	}

	@Override
	public String getKey() {
		return key;
	}

}
