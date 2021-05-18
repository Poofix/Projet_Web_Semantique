package model.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import model.IRDFGenerator;
import model.Triplet;
import services.Utils;

public class Realisateur implements IRDFGenerator {

	private static final AtomicInteger count = new AtomicInteger(0);
	private String key;
	private final int id;

	public String nom;
	public Genre genreDePredilection;

	public Realisateur(String token, Genre genre) {
		nom = token;
		genreDePredilection = genre;
		id = count.incrementAndGet();
		key = "realisateur" + id;
		// key = Utils.removeAccent(nom);
	}

	@Override
	public List<Triplet> generateRDFTriplet() {
		List<Triplet> result = new ArrayList<Triplet>();
		if (nom != null) {			
			result.add(new Triplet(":" + key, "rdfs:label", '"' + nom + '"'));
		}
		if (genreDePredilection != null) {
			result.add(new Triplet(":" + key, ":aPourStyleDePredilection", ":" + genreDePredilection.getKey()));
		}
		return result;
	}

	@Override
	public String getKey() {
		return key;
	}

}
