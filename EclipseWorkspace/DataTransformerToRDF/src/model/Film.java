package model;

import java.util.ArrayList;
import java.util.List;

public class Film {

	public String titre;
	public String anneeSortie;
	public Genre genreDominant;
	public Realisateur realisateur;
	public List<Lieu> lieuxDeTournages;
	
	public Film(String titre, String annee, Genre genre, Realisateur real, Lieu lieu) {
		this.titre = titre;
		anneeSortie = annee;
		genreDominant = genre;
		realisateur = real ;
		lieuxDeTournages = new ArrayList<Lieu>();
		lieuxDeTournages.add(lieu);
	}
	 
	
}
