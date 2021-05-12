package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.Reader;

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
		BufferedReader fileReader = null;
		try {

			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFilePath));
			// Skip first line
			if ((line = fileReader.readLine()) != null) {

				// Read the file line by line
				while ((line = fileReader.readLine()) != null) {
					// Get all tokens available in line
					String[] tokens = line.split(";");
					dictGenres.put(tokens[0], new Genre(tokens[0]));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void loadRealisateur(String csvFilePath) {
		BufferedReader fileReader = null;
		try {

			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFilePath));
			// Skip first line
			if ((line = fileReader.readLine()) != null) {
				// Read the file line by line
				while ((line = fileReader.readLine()) != null) {
					String[] nextLine = line.split(";");
					dictRealisateurs.put(nextLine[0], new Realisateur(nextLine[0], dictGenres.get(nextLine[1])));

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void loadLieu(String csvFilePath) {

		BufferedReader fileReader = null;
		try {

			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFilePath));
			// Skip first line
			if ((line = fileReader.readLine()) != null) {
				// Read the file line by line
				while ((line = fileReader.readLine()) != null) {
					String[] nextLine = line.split(";");
					String adr = nextLine[0];
					String ville = nextLine[1];
					String codePostal = nextLine[2];

					dictLieux.put(adr + "-" + codePostal, new Lieu(ville, adr, codePostal));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void loadFilm(String csvFilePath) {
		BufferedReader fileReader = null;
		try {

			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFilePath));
			// Skip first line
			if ((line = fileReader.readLine()) != null) {
				// Read the file line by line
				while ((line = fileReader.readLine()) != null) {
					String[] nextLine = line.split(";");
					String titre = nextLine[0];
					String annee = nextLine[1]; // TODO METTRE LE BON INDEX
					String lieu = nextLine[4];
					String filmKey = titre + annee;
					Film existingFilm = dictFilm.get(filmKey);
					if (existingFilm == null) {
						dictFilm.put(filmKey, new Film(titre, annee, dictGenres.get(nextLine[2]),
								dictRealisateurs.get(nextLine[3]), dictLieux.get(lieu))); // TODO Finir le constructeur
					} else {
						existingFilm.lieuxDeTournages.add(dictLieux.get(lieu));
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void loadModel() {

		this.loadGenre(System.getProperty("user.dir") + "/src/datas/genres.csv");
		this.loadLieu(System.getProperty("user.dir") + "/src/datas/lieux.csv");
		this.loadRealisateur(System.getProperty("user.dir") + "/src/datas/realisateurs.csv");
		this.loadFilm(System.getProperty("user.dir") + "/src/datas/film.csv");
	}

	public void convertModelToOntology() {
		for (Film f : this.dictFilm.values()) {
			System.out.println(f.titre);
			System.out.println(f.lieuxDeTournages.get(0).adresse);
		}
	}
}
