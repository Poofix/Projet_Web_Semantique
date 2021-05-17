package model.model.builder;


public class RealisateurBuilder {

	public int id						= -1;
	public String nom					= "";
	public GenreBuilder genreDePredilection	= null;

	public RealisateurBuilder(int id, String nom) {
		this.id 					= id;
		this.nom 					= nom;
		//this.genreDePredilection 	= genre;
	}
	
	public RealisateurBuilder(String nom) {
		this.nom 					= nom;
		//this.genreDePredilection 	= genre;
	}
}
