package model.model.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import model.IRDFGenerator;
import model.Triplet;
import model.model.Genre;
import model.model.Realisateur;
import services.Utils;

public class FilmBuilder {
	public int 					imdbId 			 = -1;
	public int 					id				 = -1;
	public String 				titre			 = "";
	public String 				anneeSortie		 = "";
	public GenreBuilder			genreDominant	 = null;
	public RealisateurBuilder 	realisateur		 = null;
	public List<LieuBuilder> 	lieuxDeTournages = null;
	public float				note		 	 = -1.0f;

	public FilmBuilder(int id, int imdbid, String titre, String annee, RealisateurBuilder real) {
		this.id 		 = id;
		this.imdbId 	 = imdbid;
		this.titre 		 = titre;
		this.anneeSortie = annee;
		this.realisateur = real;
		
		this.lieuxDeTournages = new ArrayList<LieuBuilder>(); // Ici on peut se permettre de faire un ArrayList
	}
}
