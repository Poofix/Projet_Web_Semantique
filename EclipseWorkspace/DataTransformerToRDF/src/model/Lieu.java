package model;

import java.util.ArrayList;
import java.util.List;

import services.Utils;

public class Lieu implements IRDFGenerator{

	private String key ;
	
	public String ville;
	public String adresse;
	public String codePostal;
	
	public Lieu(String v, String adr, String c) {
		ville = v;
		adresse = adr;
		codePostal = c;
		key = Utils.removeAccent(adresse +"-"+ codePostal);
	}

	@Override
	public List<Triplet> generateRDFTriplet() {
		List<Triplet> result = new ArrayList<Triplet>();
		result.add(new Triplet(":"+key,":aPourVille",'"' + ville + '"'));
		result.add(new Triplet(":"+key,":aPourCodePostal",'"' +codePostal+ '"'));
		result.add(new Triplet(":"+key,":aPourAdresse",'"' +adresse+ '"'));
		return result;
	}
	
	public String getKey() {
		return key;
	}
}
