package model.model.builder;

import java.util.HashMap;
import java.util.Map;

public class RealisateurBuilder {

	public int id						= -1;
	public String nom					= "";
	public GenreBuilder genreDePredilection	= null;
	private Map<String, Integer> genres = null;

	public RealisateurBuilder(int id, String nom) {
		this.id 	= id;
		this.nom 	= nom;
		this.genres = new HashMap<String, Integer>();
	}
	
	public RealisateurBuilder(String nom) {
		this.nom 	= nom;
		this.genres = new HashMap<String, Integer>();
	}
	
	public void incementGenre(String cleGenre) {
		
		Integer countThisGenre = this.genres.get(cleGenre);
		
		if (countThisGenre != null) {
			countThisGenre++;
			this.genres.put(cleGenre, countThisGenre);
		} else {
			this.genres.put(cleGenre, 1); // "1" -> Puisque que nous l'ajoutions.
		}
	}
	
	public String getGenrePredilection() {
		int MAX = 0;
		String predilection = null;
		
		for (String genre : this.genres.keySet()) {
			if (this.genres.get(genre) > MAX) {
				MAX = this.genres.get(genre);
				predilection = genre;
				// On prend le genre le plus commun.
			}
		}
		
		// Si SUPP à 1 on prend la prédilection
		if (MAX > 1) {
			return predilection;
		} else {
			return null;
		}
	}
}
