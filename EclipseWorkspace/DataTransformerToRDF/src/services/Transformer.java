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
	private SparqlClient dbpediaClient;
	private boolean debugMode = false;

	Map<String, Genre> dictGenres;
	Map<String, Realisateur> dictRealisateurs;
	Map<String, Lieu> dictLieux;
	Map<String, Film> dictFilm;

	public Transformer(SparqlClient sparqlClient, SparqlClient dbpediaClient, boolean debugMode) {
		dictGenres = new HashMap<String, Genre>();
		dictRealisateurs = new HashMap<String, Realisateur>();
		dictLieux = new HashMap<String, Lieu>();
		dictFilm = new HashMap<String, Film>();

		this.sparqlClient = sparqlClient;
		this.dbpediaClient = dbpediaClient;
		this.debugMode = debugMode;

		String query = "ASK WHERE { ?s ?p ?o }";
		boolean serverIsUp = false;
		try {
			serverIsUp = sparqlClient.ask(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (serverIsUp) {
			System.out.println("Fuseki server is ready...");
		} else {
			System.out.println("ERROR : Fuseki server unreachable...");
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
					dictGenres.put(tokens[0], new Genre(tokens[1]));
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
					Genre genre = (nextLine.length > 2) ? dictGenres.get(nextLine[2]) : null;
					dictRealisateurs.put(nextLine[0], new Realisateur(nextLine[1], genre));

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
					String id = (nextLine.length > 0) ? nextLine[0] : null;
					String adresse = (nextLine.length > 1) ? nextLine[1] : null;
					String ville = (nextLine.length > 2) ? nextLine[2] : null;
					String codePostal = (nextLine.length > 3) ? nextLine[3] : null;
					dictLieux.put(id, new Lieu(ville, adresse, codePostal));
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
					Film existingFilm = dictFilm.get(nextLine[0]);
					if (existingFilm == null) {
						dictFilm.put(nextLine[0],
								new Film(nextLine[1], nextLine[2], dictGenres.get(nextLine[3]),
										dictRealisateurs.get(nextLine[4]), dictLieux.get(nextLine[5]),
										Float.parseFloat((nextLine[6])))); // TODO Finir le
																			// constructeur
					} else {
						existingFilm.lieuxDeTournages.add(dictLieux.get(nextLine[4]));
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

		this.loadGenre(System.getProperty("user.dir") + "/src/datas/generated/genres.csv");
		this.loadLieu(System.getProperty("user.dir") + "/src/datas/generated/lieux.csv");
		this.loadRealisateur(System.getProperty("user.dir") + "/src/datas/generated/realisateurs.csv");
		this.loadFilm(System.getProperty("user.dir") + "/src/datas/generated/film.csv");

		// load mock
//		this.loadGenre(System.getProperty("user.dir") + "/src/datas/mock/genres.csv");
//		this.loadLieu(System.getProperty("user.dir") + "/src/datas/mock/lieux.csv");
//		this.loadRealisateur(System.getProperty("user.dir") + "/src/datas/mock/realisateurs.csv");
//		this.loadFilm(System.getProperty("user.dir") + "/src/datas/mock/film.csv");
	}

	private String generateRequest(List<Triplet> triplets) {
		String prefix = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
				+ "Prefix : <http://www.semanticweb.org/adminetu/ontologies/2021/4/untitled-ontology-9#>\r\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n";

		String queryString = prefix + "\n" + "insert data {";
		for (Triplet t : triplets) {
			queryString += t.toString() + "\n";
		}
		queryString += "}";
		if (!debugMode) {
			System.out.println("QUERY ## " + queryString);
		}
		return triplets.size() > 0 ? queryString : "";
	}
	
	public void doAlignStreet() {
		for (Lieu l : dictLieux.values()) {
			String request = 
					"PREFIX dbo: <http://dbpedia.org/ontology/>\r\n"
					+ "PREFIX geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#>\r\n"
					+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
					+ "\r\n"
					+ "SELECT * WHERE {\r\n"
					+ "?uriRessoures a dbo:Place;\r\n"
					+ "rdfs:label \""+ l.adresse +"@fr.\r\n"
					+ "}";
			
			//Iterable<Map<String, String>> response = dbpediaClient.select(request);
			
			String uri = "customUri";
			
			l.dbpediaURI = uri;
		}
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
