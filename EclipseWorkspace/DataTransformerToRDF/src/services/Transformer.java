package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Triplet;
import model.model.Film;
import model.model.Genre;
import model.model.Lieu;
import model.model.Realisateur;
import sparqlclient.SparqlClient;

public class Transformer {

	private SparqlClient sparqlClient;
	private boolean debugMode = false;

	Map<String, Genre> dictGenres;
	Map<String, Realisateur> dictRealisateurs;
	Map<String, Lieu> dictLieux;
	Map<String, Film> dictFilm;

	public Transformer(SparqlClient sparqlClient, boolean debugMode) {
		dictGenres = new HashMap<String, Genre>();
		dictRealisateurs = new HashMap<String, Realisateur>();
		dictLieux = new HashMap<String, Lieu>();
		dictFilm = new HashMap<String, Film>();

		this.sparqlClient = sparqlClient;
		this.debugMode = debugMode;

		String query = "ASK WHERE { ?s ?p ?o }";
		boolean serverIsUp = false;
		try {
			serverIsUp = sparqlClient.ask(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (serverIsUp) {
			System.out.println("Fuseli server is ready...");
		} else {
			System.out.println("ERROR : Fuseli server unreachable...");
		}
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
					dictGenres.put(Utils.normalize(tokens[0]), new Genre(tokens[0]));
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
					dictRealisateurs.put(Utils.normalize(nextLine[0]),
							new Realisateur(nextLine[0], dictGenres.get(Utils.normalize(nextLine[1]))));

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

					dictLieux.put(Utils.normalize(adr + "-" + codePostal), new Lieu(ville, adr, codePostal));
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
					String lieu = Utils.normalize(nextLine[4]);
					String filmKey = Utils.normalize(titre + annee);
					Film existingFilm = dictFilm.get(filmKey);
					if (existingFilm == null) {
						dictFilm.put(filmKey,
								new Film(titre, annee, dictGenres.get(Utils.normalize(nextLine[2])),
										dictRealisateurs.get(Utils.normalize(nextLine[3])), dictLieux.get(lieu),
										Float.parseFloat((nextLine[5])))); // TODO Finir le
																			// constructeur
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

	// load the model in the transformer instance
	public void loadModel() {
//		this.loadGenre(System.getProperty("user.dir") + "/src/datas/generated/genres.csv");
//		this.loadLieu(System.getProperty("user.dir") + "/src/datas/generated/lieux.csv");
//		this.loadRealisateur(System.getProperty("user.dir") + "/src/datas/generated/realisateurs.csv");
//		this.loadFilm(System.getProperty("user.dir") + "/src/datas/generated/film.csv");
		
		// load mock
		this.loadGenre(System.getProperty("user.dir") + "/src/datas/mock/genres.csv");
		this.loadLieu(System.getProperty("user.dir") + "/src/datas/mock/lieux.csv");
		this.loadRealisateur(System.getProperty("user.dir") + "/src/datas/mock/realisateurs.csv");
		this.loadFilm(System.getProperty("user.dir") + "/src/datas/mock/film.csv");
	}

	private String generateRequest(List<Triplet> triplets) {
		String prefix = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
				+ "Prefix : <http://www.semanticweb.org/adminetu/ontologies/2021/4/untitled-ontology-4>\r\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n";

		String queryString = prefix + "\n" + "insert data {";
		for (Triplet t : triplets) {
			queryString += t.toString() + "\n";
		}
		queryString += "}";
		if (!debugMode) {
			System.out.println("QUERY ## " + queryString);
		}
		return queryString;
	}

	public void convertModelToOntology() {

		for (Genre g : dictGenres.values()) {
			if (debugMode) {
				System.out.println(generateRequest(g.generateRDFTriplet()));
			} else {
				try {
					sparqlClient.update(generateRequest(g.generateRDFTriplet()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println("========================");
			for (Lieu l : dictLieux.values()) {
				if (debugMode) {
					System.out.println(generateRequest(l.generateRDFTriplet()));
				} else {
					try {
						sparqlClient.update(generateRequest(l.generateRDFTriplet()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println("========================");
			for (Realisateur r : dictRealisateurs.values()) {
				if (debugMode) {
					System.out.println(generateRequest(r.generateRDFTriplet()));
				} else {
					try {
						sparqlClient.update(generateRequest(r.generateRDFTriplet()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println("========================");
			for (Film f : this.dictFilm.values()) {
				if (debugMode) {
					System.out.println(generateRequest(f.generateRDFTriplet()));

				} else {
					try {
						sparqlClient.update(generateRequest(f.generateRDFTriplet()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
