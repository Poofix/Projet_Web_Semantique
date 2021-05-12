package services;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;

import java.io.IOException;

import model.Film;
import model.Genre;
import model.Lieu;
import model.Realisateur;

public class Transformer {

	Map<String, Genre> dictGenres;
	Map<String, Realisateur> dictRealisateurs;
	Map<String, Lieu> dictLieux;
	Map<String, Film> dictFilm;

	public Transformer() {
		dictGenres = new HashMap<String, Genre>();
		dictRealisateurs = new HashMap<String, Realisateur>();
		dictLieux = new HashMap<String, Lieu>();
		dictFilm = new HashMap<String, Film>();
	}

	private void loadGenre(String csvFilePath) {
		CSVReader reader = null;
		try {
			// Get the CSVReader instance with specifying the delimiter to be used
			reader = new CSVReader(new FileReader(csvFilePath));

			String[] nextLine;

			// Read one line at a time
			while ((nextLine = reader.readNext()) != null) {
				String token = nextLine[0];
				dictGenres.put(token, new Genre(token));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void loadRealisateur(String csvFilePath) {
		CSVReader reader = null;
		try {
			// Get the CSVReader instance with specifying the delimiter to be used
			reader = new CSVReader(new FileReader(csvFilePath));

			String[] nextLine;

			// Read one line at a time
			while ((nextLine = reader.readNext()) != null) {

				dictRealisateurs.put(nextLine[0], new Realisateur(nextLine[0],dictGenres.get(nextLine[1])));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void loadLieu(String csvFilePath) {
		CSVReader reader = null;
		try {
			// Get the CSVReader instance with specifying the delimiter to be used
			reader = new CSVReader(new FileReader(csvFilePath));

			String[] nextLine;

			// Read one line at a time
			while ((nextLine = reader.readNext()) != null) {
				String adr = nextLine[0];
				String ville = nextLine[0];
				String codePostal = nextLine[0];
				
				dictLieux.put(adr+"-"+codePostal, new Lieu(ville,adr,codePostal));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void loadFilm(String csvFilePath) {
		CSVReader reader = null;
		try {
			// Get the CSVReader instance with specifying the delimiter to be used
			reader = new CSVReader(new FileReader(csvFilePath));

			String[] nextLine;

			// Read one line at a time
			while ((nextLine = reader.readNext()) != null) {
				String titre = nextLine[0];
				String annee = nextLine[1]; //TODO METTRE LE BON INDEX
				dictFilm.put(titre+annee,new Film()); //TODO Finir le constructeur
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void loadModel() {
		this.loadGenre("../datas/genres.csv");
		this.loadLieu("../datas/lieux.csv");
		this.loadRealisateur("../datas/realisateurs.csv");
		this.loadFilm("../datas/film.csv");
	}
	
	public void convertModelToOntology() {
		
	}
}
