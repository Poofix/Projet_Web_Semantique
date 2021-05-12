package model;

import java.util.ArrayList;
import java.util.List;

import services.Utils;


public class Genre implements IRDFGenerator{
	
	private String key;
	public String label;
	
	public Genre(String token) {
		label = token;
		key= label;
	}

	@Override
	public List<Triplet> generateRDFTriplet() {
		List<Triplet> result = new ArrayList<Triplet>();
		String s = ":" + Utils.removeAccent(label); 
		String p = "rdfs:label";
		String o = '"'+this.label+'"';
		result.add(new Triplet(s, p, o));
		return result;
	}

	@Override
	public String getKey() {
		return key;
	}

}
