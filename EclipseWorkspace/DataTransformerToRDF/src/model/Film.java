package model;

import java.util.ArrayList;
import java.util.List;

import services.Utils;

public class Film implements IRDFGenerator{
	private String key;
	
	public String titre;
	public String anneeSortie;
	public Genre genreDominant;
	public Realisateur realisateur;
	public List<Lieu> lieuxDeTournages;
	public float note;
	
	public Film(String titre, String annee, Genre genre, Realisateur real, Lieu lieu,float n) {
		this.titre = titre;
		anneeSortie = annee;
		genreDominant = genre;
		realisateur = real ;
		lieuxDeTournages = new ArrayList<Lieu>();
		lieuxDeTournages.add(lieu);
		note = n;
		
		key = Utils.removeAccent(titre+anneeSortie);
	}

	@Override
	public List<Triplet> generateRDFTriplet() {
		List<Triplet> result = new ArrayList<Triplet>();
		
		result.add(new Triplet(":"+key,"rdfs:label", '"' + titre+ '"'));
		result.add(new Triplet(":"+key,":aEteTourneEn", '"' + anneeSortie+ '"'));
		result.add(new Triplet(":"+key,":aPourGenre", ":" + genreDominant.getKey()));
		result.add(new Triplet(":"+key,":aPourRealisateur", ":" + realisateur.getKey()));
		for (Lieu l : lieuxDeTournages) {
			result.add(new Triplet(":"+key,":seDerouleDans", ":" + l.getKey()));
		}
		result.add(new Triplet(":"+key,":aPourNote", '"'+ new String(note+"")+ '"'));
		return result;
	}

	@Override
	public String getKey() {
		return key;
	}
	
	
	
}
