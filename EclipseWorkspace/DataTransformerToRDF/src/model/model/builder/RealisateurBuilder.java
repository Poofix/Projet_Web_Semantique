package model.model.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import model.IRDFGenerator;
import model.Triplet;
import model.model.Genre;
import services.Utils;

public class RealisateurBuilder {

	public int id						= -1;
	public String nom					= "<NONE>";
	public Genre genreDePredilection	= null;

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
