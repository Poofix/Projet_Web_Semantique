package model.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import model.IRDFGenerator;
import model.Triplet;
import services.Utils;

public class Lieu implements IRDFGenerator{

	private static final AtomicInteger count = new AtomicInteger(0); 
	private String key ;
	private final int id;
	
	public String ville;
	public String adresse;
	public String codePostal;
	
	public Lieu(String v, String adr, String c) {
		ville = v;
		adresse = adr;
		codePostal = c;
		id = count.incrementAndGet();
		key = "lieu" + id;
		//key = Utils.removeAccent(adresse +"-"+ codePostal);
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
