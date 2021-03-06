package model.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import model.IRDFGenerator;
import model.Triplet;

public class Film implements IRDFGenerator {
	private static final AtomicInteger count = new AtomicInteger(0);
	private String key;
	private final int id;

	public String titre;
	public String anneeSortie;
	public Genre genreDominant;
	public Realisateur realisateur;
	public List<Lieu> lieuxDeTournages;
	public float note;

	public Film(String titre, String annee, Genre genre, Realisateur real, Lieu lieu, float n) {
		this.titre = titre;
		anneeSortie = annee;
		genreDominant = genre;
		realisateur = real;
		lieuxDeTournages = new ArrayList<Lieu>();
		if (lieu != null) {
			lieuxDeTournages.add(lieu);
		}
		note = n;

		id = count.incrementAndGet();
		key = "film" + id;
		// key = Utils.removeAccent(titre+anneeSortie);
	}

	@Override
	public List<Triplet> generateRDFTriplet() {
		List<Triplet> result = new ArrayList<Triplet>();
		if (titre != null && !titre.equals("")) {
			result.add(new Triplet(":" + key, "rdfs:label", '"' + titre + '"'));
		}
		if (anneeSortie != null && !anneeSortie.equals("")) {
			result.add(new Triplet(":" + key, ":aEteTourneEn", '"' + anneeSortie + '"'));
		}
		if (genreDominant != null) {
			result.add(new Triplet(":" + key, ":aPourGenre", ":" + genreDominant.getKey()));
		}
		if (realisateur != null) {
			result.add(new Triplet(":" + key, ":aPourRealisateur", ":" + realisateur.getKey()));
		}
		for (Lieu l : lieuxDeTournages) {
			result.add(new Triplet(":" + key, ":seDerouleDans", ":" + l.getKey()));
		}
		if (note != -1.0) {
			result.add(new Triplet(":" + key, ":aPourNote", new String(note + "")));
		}
		
		return result;
	}

	@Override
	public String getKey() {
		return key;
	}

}
