package model;

import java.util.ArrayList;
import java.util.List;

import services.Utils;

public class Realisateur implements IRDFGenerator{

	private String key ;
	public String nom;
	public Genre genreDePredilection;
	
	public Realisateur(String token, Genre genre) {
		nom = token;
		genreDePredilection = genre;
		key = Utils.removeAccent(nom);
	}

	@Override
	public List<Triplet> generateRDFTriplet() {
		List<Triplet> result = new ArrayList<Triplet>();
		
		result.add(new Triplet(":"+key ,"rdfs:label", '"'+ nom +'"'));
		result.add(new Triplet(":"+key ,":aPourStyleDePredilection",":"+genreDePredilection.getKey()));
		return result;
	}

	@Override
	public String getKey() {
		return key;
	}
	
}
