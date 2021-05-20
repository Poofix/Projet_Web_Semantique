package model.model.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FilmBuilder {
	public int imdbId;
	public int id;
	public String titre;
	public String anneeSortie;
	public List<GenreBuilder> genres;
	public RealisateurBuilder realisateur;
	public List<LieuBuilder> lieuxDeTournages;
	public float note;
	public boolean estFrancais;

	public FilmBuilder(int id, int imdbid, String titre, String annee, RealisateurBuilder real) {
		this.id = id;
		this.imdbId = imdbid;
		this.titre = titre;
		this.anneeSortie = annee;
		this.realisateur = real;
		this.note = -1.0f;

		this.lieuxDeTournages = new ArrayList<LieuBuilder>(); // Ici on peut se permettre de faire un ArrayList
		this.genres = new ArrayList<GenreBuilder>();
		this.estFrancais = false;
	}

	public void updateEstFrancais() {	
		if (!estFrancais) {
			for(LieuBuilder l : lieuxDeTournages) {
				if (l.ville.toLowerCase().contains("paris")) {
					estFrancais = true;
					break;
				}
			}
		}
	}
}
